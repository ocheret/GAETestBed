package com.readyposition.gaetestbed;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    protected abstract void nullCommand(HttpServletRequest req,
            HttpServletResponse resp)
                    throws IOException, ServletException;

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
        if (!loginVerified(req, resp)) {
            return;
        }

        final String commandString = req.getParameter("c");
        final Command cmd = getDispatchTable().get(commandString);
        if (cmd == null) {
            nullCommand(req, resp);
            logger.info("{} invoked with {} command",
                    this.getClass().getName(),
                    (commandString == null) ? "missing" : "unrecognized");
        } else {
            logger.info("{} invoked with command 'c={}'",
                    this.getClass().getName(), commandString);
            cmd.doCommand(req, resp);
        }
    }

    private boolean loginVerified(
            final HttpServletRequest req,
            final HttpServletResponse resp)
                    throws ServletException, IOException
    {
        // See if we have a logged in session
        HttpSession session = req.getSession(false);
        if (session != null) {
            if (session.getAttribute("username") != null) {
                return true;
            } else {
                // This session is in an unexpected state
                session.invalidate();
                logger.warn("Session without username will be invalidated.");
            }
        }

        // Create a session to represent the login attempt
        session = req.getSession(true);
        session.setAttribute("url", req.getRequestURI());

        // Redirect to our login servlet
        logger.info("Initiating login");
        resp.sendRedirect("login");

        return false;
    }

    protected static Command homeCommand = new Command() {
        @Override
        protected void doCommand(final HttpServletRequest req,
                final HttpServletResponse resp)
                        throws IOException, ServletException
        {
            resp.sendRedirect("."); // Default home page (index.html)
        }
    };
}
