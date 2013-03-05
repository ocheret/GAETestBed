package com.readyposition.gaetestbed;

import java.io.IOException;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class GAETestBedServlet extends HttpServlet {
    final static Logger logger = 
            LoggerFactory.getLogger(GAETestBedServlet.class);

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException
    {
        logger.info("GAETetBedServlet logger works!");
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world");
    }
}
