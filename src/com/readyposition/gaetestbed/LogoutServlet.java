package com.readyposition.gaetestbed;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {
    final static Logger logger =
            LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    public void doPost(final HttpServletRequest req,
            final HttpServletResponse resp)
                    throws IOException, ServletException
   {
        doGet(req, resp);
   }

    @Override
    public void doGet(final HttpServletRequest req,
            final HttpServletResponse resp)
                    throws IOException, ServletException
    {
        final HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect(".");
    }
}
