package com.readyposition.gaetestbed;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class LowLevelDatastoreServlet extends AbstractCommandServlet {
    final static Logger logger =
            LoggerFactory.getLogger(LowLevelDatastoreServlet.class);

    private static HashMap<String, Command> dispatchTable = new HashMap<String, Command>();

    @Override
    protected HashMap<String, Command> getDispatchTable() {
        return dispatchTable;
    }

    @Override
    protected void nullCommand(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException, ServletException
    {
        req.getRequestDispatcher("LowLevelDatastore.ftl").forward(req, resp);
    }

    protected static Command homeCommand = new Command() {
        @Override
        protected void doCommand(final HttpServletRequest req, final HttpServletResponse resp)
                throws IOException, ServletException
        {
            resp.sendRedirect("."); // Default home page (index.html)
        }
    };

    static {
        dispatchTable.put("home", homeCommand);
    }
}
