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
public class LoginServlet extends HttpServlet {
    final static Logger logger =
            LoggerFactory.getLogger(LoginServlet.class);

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException, ServletException
   {
        // XXX
        String username = req.getParameter("username");
        final boolean loginValidated =
                (username != null && username.trim().length() != 0);
        username = username.trim();

        final HttpSession session = req.getSession();
        final String url = (String)session.getAttribute("url");

        if (loginValidated) {
            resp.sendRedirect(url != null ? url : ".");
            session.setAttribute("username", username);
        } else {
                session.setAttribute("errorMessage", "Invalid credentials");
            req.getRequestDispatcher("Login.ftl").forward(req, resp);
        }
   }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws IOException, ServletException
    {
        // Display login form
        req.getRequestDispatcher("Login.ftl").forward(req, resp);
    }


}
