package servlet;

import org.apache.log4j.Logger;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(LogoutServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        String language = "";

        if (httpSession.getAttribute("language") != null) {
            language = (String) httpSession.getAttribute("language");
        }

        httpSession.removeAttribute("username");
        LOG.debug("User[" + ((User) httpSession.getAttribute("user")).getName() + "] logged out");
        httpSession.invalidate();

        if (!"".equals(language)) {
            request.getSession().setAttribute("language", language);
        }

        response.sendRedirect("/");
    }
}
