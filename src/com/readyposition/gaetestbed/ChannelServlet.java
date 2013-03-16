package com.readyposition.gaetestbed;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class ChannelServlet extends AbstractCommandServlet {
    final static Logger logger =
            LoggerFactory.getLogger(ChannelServlet.class);

    private static HashMap<String, Command> dispatchTable =
            new HashMap<String, Command>();

    @Override
    protected HashMap<String, Command> getDispatchTable() {
        return dispatchTable;
    }

    @Override
    protected void nullCommand(final HttpServletRequest req,
            final HttpServletResponse resp)
                    throws IOException, ServletException
    {
        req.getRequestDispatcher("Channel.ftl").forward(req, resp);
    }

    protected static Command connectCommand = new Command() {
        @Override
        protected void doCommand(
                final HttpServletRequest req,
                final HttpServletResponse resp)
                        throws IOException, ServletException
        {
            final HttpSession session = req.getSession();
            final String clientId =
                    (String)session.getAttribute("username") +
                    ((new Date()).getTime() ^ session.getCreationTime());
            final ChannelService channelService =
                    ChannelServiceFactory.getChannelService();
            final String token = channelService.createChannel(clientId);

            // XXX - Add this to memcache and datastore
            session.setAttribute("clientId", clientId);
            session.setAttribute("token", token);

            final HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("clientId", clientId);
            map.put("token", token);
            map.put("connectTime", new Date());

            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), map);
        }
    };

    protected static Command subscribeCommand = new Command() {
        @Override
        protected void doCommand(
                final HttpServletRequest req,
                final HttpServletResponse resp)
                        throws IOException, ServletException
        {
            final HttpSession session = req.getSession();
            final String clientId = (String)session.getAttribute("clientId");
            if (clientId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Not connected to a channel.");
                return;
            }
            final String topicName = req.getParameter("topic");
            if (topicName == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Missing topic parameter.");
                return;
            }
            Publisher.subscribe(topicName, clientId);

            final HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("topic", topicName);
            map.put("clientId", clientId);
            final ObjectMapper mapper = new ObjectMapper();
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), map);
        }
    };

    protected static Command publishCommand = new Command() {
        @Override
        protected void doCommand(
                final HttpServletRequest req,
                final HttpServletResponse resp)
                        throws IOException, ServletException
        {
            final String topicName = req.getParameter("topic");
            if (topicName == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Missing topic parameter.");
                return;
            }

            final String msg = req.getParameter("msg");
            if (msg == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Missing msg parameter.");
                return;
            }

            Publisher.broadcastMessage(topicName, msg);

            final HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("topic", topicName);
            final ObjectMapper mapper = new ObjectMapper();
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), map);

        }
    };

    static {
        dispatchTable.put("home", homeCommand);
        dispatchTable.put("connect", connectCommand);
        dispatchTable.put("subscribe", subscribeCommand);
        dispatchTable.put("publish", publishCommand);
    }
}
