package com.readyposition.gaetestbed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class GAETestBedServlet extends HttpServlet {
    final static Logger logger = 
            LoggerFactory.getLogger(GAETestBedServlet.class);

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException
    {
        logger.info("SLF4J logger works!");
        req.getRequestDispatcher("HelloWorld.ftl").forward(req, resp); 
    }
}
