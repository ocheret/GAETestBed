package com.readyposition.gaetestbed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class HelloWorldServlet extends HttpServlet {
    final static Logger logger =
            LoggerFactory.getLogger(HelloWorldServlet.class);

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws IOException, ServletException
    {
        logger.info("Servlet '" + this.getClass().getName() + "' invoked.");
        req.setAttribute("clown", "Bozo");
        req.getRequestDispatcher("HelloWorld.ftl").forward(req, resp);
    }
}
