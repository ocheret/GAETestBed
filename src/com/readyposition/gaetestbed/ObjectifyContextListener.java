package com.readyposition.gaetestbed;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.ObjectifyService;

public class ObjectifyContextListener implements ServletContextListener {
    final static Logger logger =
            LoggerFactory.getLogger(ObjectifyContextListener.class);

    @Override
    public void contextDestroyed(final ServletContextEvent arg0) {
        // Nothing to do
    }

    @Override
    public void contextInitialized(final ServletContextEvent arg0) {
        // Register Entity classes for Objectify before any other operations.
        ObjectifyService.register(Publisher.Topic.class);
        ObjectifyService.register(Publisher.Subscriber.class);

        logger.info("Registered Entities with ObjectifyService");
    }
}
