package com.readyposition.gaetestbed;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.channel.ChannelFailureException;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Simple Pub/Sub mechanism for the GAE Channel Service.  Maintains a cached
 * and persisted watchlist of topics and subscribers using Objectify.
 * Messages sent to a topic will be broadcasted to all subscribers of that
 * topic.
 */
public class Publisher {
    final static Logger logger =
            LoggerFactory.getLogger(Publisher.class);

    /** The Channel Service */
    private static ChannelService channelSvc =
            ChannelServiceFactory.getChannelService();

    /** Topic entities hold a list of subscribers */
    @Entity
    @Cache
    public static class Topic {
        @Id
        protected String name;
        protected HashSet<String> subscribers;
        protected Topic() {}
        public Topic(final String name) {
            this.name = name;
            this.subscribers = new HashSet<String>();
        }
        public String getName() { return name; }
        public HashSet<String> getSubscribers() { return subscribers; }
    }

    /** Subscriber entities hold a list of topics */
    @Entity
    @Cache
    public static class Subscriber {
        @Id
        protected String clientId;
        protected HashSet<String> topics;
        protected Subscriber() {}
        public Subscriber(final String clientId) {
            this.clientId = clientId;
            this.topics = new HashSet<String>();
        }
        public String getClientId() { return clientId; }
        public HashSet<String> getTopics() { return topics; }
    }

    /**
     * Broadcasts a message to all subscribers of a topic.  Messages are
     * currently assumed to be JSON.  Before messages are sent they will be
     * wrapped in a JSON array with the topic name as the first element and
     * the supplied payload as the second element.  Any channels that
     * experience errors in transmission are assumed to be dead and their
     * subscriptions will be shut down.
     *
     * @param topicName the name of the topic being published to
     * @param msg the JSON message to send to all subscribers of the topic
     * (XXX - we should support non-JSON messages in the future)
     */
    public static void broadcastMessage(
            final String topicName,
            final String msg)
    {
        // Fetch the topic in question
        final Topic topic = ofy().load().type(Topic.class).id(topicName).get();
        if (topic == null) {
            logger.info("Broadcast to nonexistant topic '{}'", topicName);
            return;
        }

        // List of clients that can't receive messages.  We will collect these
        // during broadcast and clean them up afterwards.
        final ArrayList<String> failed =
                new ArrayList<String>(topic.getSubscribers().size());

        // Wrap the message to send in an array with the topic name as the
        // first element and the original payload as the second element.
        final String topicMsg = "[\"" + topicName + "\"]," + msg + "]";

        // Send the enhanced payload to all subscribers and collect any
        // failed subscribers.
        for (final String clientId : topic.getSubscribers()) {
            try {
                channelSvc.sendMessage(new ChannelMessage(clientId, topicMsg));
            } catch (final ChannelFailureException e) {
                logger.warn("Failed on topic '{}' for client '{}'",
                        topicName, clientId);
                failed.add(clientId);
            }
        }

        // Clean up any failed subscribers.
        for (final String clientId : failed) {
            logger.info("Unsubscribing all for client '{}'", clientId);
            unsubscribeAll(clientId);
        }
    }

    /**
     * Subscribe a client to updates on a topic.
     * @param topicName The name of the topic to subscribe to.
     * @param clientId The name of the client subscribing.
     */
    public static void subscribe(
            final String topicName,
            final String clientId)
    {
        // Do this all within a cross group transaction
        ofy().transact(new VoidWork() {
            @Override
            public void vrun() {
                // Start asynchronous fetches of the Topic and Subscriber
                final Ref<Topic> topicRef =
                        ofy().load().type(Topic.class).id(topicName);
                final Ref<Subscriber> subRef =
                        ofy().load().type(Subscriber.class).id(clientId);

                // If this is a new topic, create a new one
                Topic topic = topicRef.get();
                if (topic == null) {
                    topic = new Topic(topicName);
                }

                // If this is a new subscriber, create a new one
                Subscriber sub = subRef.get();
                if (sub == null) {
                    sub = new Subscriber(clientId);
                }

                // Add the client to the topic's subscriber set.
                topic.getSubscribers().add(clientId);

                // Add the topic to the client's topic set.
                sub.getTopics().add(topicName);

                // Make it so.
                ofy().save().entities(topic, sub).now();
            }
        });
    }

    /**
     * Unsubscribe a client from a topic.
     * @param topicName the name of the topic to unsubscribe from.
     * @param clientId the client unsubscribing from the topic.
     */
    public static void unsubscribe(
            final String topicName,
            final String clientId)
    {
        // Do this all within a cross group transaction
        ofy().transact(new VoidWork() {
            @Override
            public void vrun() {
                // Asynchronously retrieve that Topic and Subscriber
                final Ref<Topic> topicRef =
                        ofy().load().type(Topic.class).id(topicName);
                final Ref<Subscriber> subRef =
                        ofy().load().type(Subscriber.class).id(clientId);

                // Remove the client from the topic's subscriber set.  If
                // there are no more subscribers, remove the topic altogether.
                Result<?> resultA = null;
                final Topic topic = topicRef.get();
                if (topic != null) {
                    topic.getSubscribers().remove(clientId);
                    if (topic.getSubscribers().size() == 0) {
                        resultA = ofy().delete().entity(topic);
                    } else {
                        resultA = ofy().save().entity(topic);
                    }
                }

                // Remove the topic from the subscribers's topic set.  If
                // there are no more topics, remove the subscriber altogether.
                Result<?> resultB = null;
                final Subscriber sub = subRef.get();
                if (sub != null) {
                    sub.getTopics().remove(topicName);
                    if (sub.getTopics().size() == 0) {
                        resultB = ofy().delete().entity(sub);
                    } else {
                        resultB = ofy().save().entity(sub);
                    }
                }

                // XXX - do we need these?
                if (resultA != null) resultA.now();
                if (resultB != null) resultB.now();
            }
        });
    }

    /**
     * Get all of the subscribers to a topic.
     * @param topicName the name of the topic.
     * @return an array of clientId's of subscribed clients
     */
    public static String[] getSubscribers(final String topicName) {
        final Topic topic =
                ofy().load().type(Topic.class).id(topicName).get();

        if (topic == null) return null;

        final String[] result = new String[topic.getSubscribers().size()];
        topic.getSubscribers().toArray(result);
        return result;
    }

    /**
     * Get all of the topics to a client is subscribed to.
     * @param clientId the clientId of the subscriber.
     * @return an array of names of topics the client is subscribed to.
     */
    public static String[] getTopics(final String clientId) {
        final Subscriber sub =
                ofy().load().type(Subscriber.class).id(clientId).get();

        if (sub == null) return null;

        final String[] result = new String[sub.getTopics().size()];
        sub.getTopics().toArray(result);
        return result;
    }

    /**
     * Remove all of a client's topic subscriptions.
     * @param clientId
     */
    public static void unsubscribeAll(final String clientId) {
        final Subscriber sub =
                ofy().load().type(Subscriber.class).id(clientId).get();

        // For each topic subscribed to, remove this client from the topic's
        // set of subscribed clients.
        for (final String topicName : sub.getTopics()) {
            ofy().transact(new VoidWork() {
                @Override
                public void vrun() {
                    final Topic topic =
                            ofy().load().type(Topic.class).id(topicName).get();
                    topic.getSubscribers().remove(clientId);
                    ofy().save().entity(topic);
                }
             });
        }

        // Remove the client entity
        ofy().delete().entity(sub);
    }
}
