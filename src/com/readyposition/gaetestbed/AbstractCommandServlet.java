package com.readyposition.gaetestbed;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public abstract class AbstractCommandServlet extends HttpServlet {
    final static Logger logger =
            LoggerFactory.getLogger(AbstractCommandServlet.class);

    protected abstract static class Command {
        protected abstract void doCommand(
                HttpServletRequest req, HttpServletResponse resp)
                        throws IOException, ServletException;
    }

    protected abstract HashMap<String, Command> getDispatchTable();

    protected abstract void nullCommand(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException;

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException, ServletException
    {
        processCommand(req, resp);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException, ServletException
    {
        processCommand(req, resp);
    }

    public void processCommand(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException, ServletException
    {
        final String commandString = req.getParameter("command");
        final Command cmd = getDispatchTable().get(commandString);
        if (cmd == null) {
            nullCommand(req, resp);
            logger.info("{} invoked with no command='{}'", this.getClass().getName());
        } else {
            logger.info("{} invoked with command='{}'", this.getClass().getName(), commandString);
            cmd.doCommand(req, resp);
        }
    }
}
