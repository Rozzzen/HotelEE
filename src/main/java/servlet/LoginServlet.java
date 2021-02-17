package servlet;

import dao.Impl.UserDaoImpl;
import org.apache.log4j.Logger;
import pojo.User;
import sun.rmi.runtime.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private final UserDaoImpl userDaoImpl = UserDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(LoginServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int userID;
        try {
            userID = userDaoImpl.loginUserValidation(email, password);
            if (userID > 0) {
                User user = userDaoImpl.selectUserByID(userID);
                LOG.debug("User[" + userID + "] logged in");
                request.getSession().setAttribute("user", user);
                response.sendRedirect("/");
            } else {
                LOG.debug("User[" + userID + "] entered wrong email/password");
                request.getSession().setAttribute("alert", "Wrong email or password");
                response.sendRedirect("/login");
            }
        } catch (SQLException e) {
            LOG.error("Login exception", e);
            response.sendRedirect("/");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Enter login page");
        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }
}
