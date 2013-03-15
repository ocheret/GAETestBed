package com.readyposition.gaetestbed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class ChannelPresenceServlet extends HttpServlet {
    final static Logger logger =
            LoggerFactory.getLogger(ChannelPresenceServlet.class);

    @Override
    public void doGet(final HttpServletRequest req,
            final HttpServletResponse resp)
                    throws IOException, ServletException
{
        processCommand(req, resp);
}

    @Override
    public void doPost(final HttpServletRequest req,
            final HttpServletResponse resp)
                    throws IOException, ServletException
    {
        processCommand(req, resp);
    }

    public void processCommand(final HttpServletRequest req,
            final HttpServletResponse resp)
                    throws IOException, ServletException
    {
        final ChannelService channelService =
                ChannelServiceFactory.getChannelService();
        final ChannelPresence presence = channelService.parsePresence(req);

        logger.info("Channel Presence - clientId={}, isConnected={}",
                presence.clientId(), presence.isConnected());
    }
}
