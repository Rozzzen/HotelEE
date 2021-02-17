package servlet;

import dao.Impl.UserDaoImpl;
import org.apache.log4j.Logger;
import pojo.Gender;
import pojo.User;
import pojo.UserRole;
import util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(RegisterServlet.class);
    private final UserDaoImpl userDaoImpl = UserDaoImpl.getInstance();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        int day = Integer.parseInt(request.getParameter("day"));
        int month = Integer.parseInt(request.getParameter("month")) + 1;
        int year = Integer.parseInt(request.getParameter("year"));

        DateUtil dateUtil = new DateUtil();

        try {
            LocalDate date = dateUtil.validateDate(day, month, year);

            Gender gender = new Gender();
            gender.setId(Integer.parseInt(request.getParameter("gender")));

            User user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setGender(gender);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(password);
            user.setBdate(date);
            user.setRole(UserRole.USER);

            userDaoImpl.registerUser(user);
            LOG.debug("User[" + user.getName() + "] registered");
            request.getSession().setAttribute("success", "Successfully registered");
            response.sendRedirect("/login");
        } catch (DateTimeParseException e) {
            LOG.debug("User entered wrong birth day", e);
            request.getSession().setAttribute("alert", "Date validation failure");
            response.sendRedirect("register");
        } catch (SQLException ex) {
            if(ex instanceof SQLIntegrityConstraintViolationException) {
                LOG.debug("User entered alredy existing email", ex);
                request.getSession().setAttribute("alert", "User with this email already exists");
                response.sendRedirect("register");
            }
            else {
                LOG.error("Registration exception");
            }
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Gender> genderList = Collections.emptyList();

        try {
            genderList = userDaoImpl.getGenderList();

        } catch (SQLException e) {
            LOG.error("Gender list get exception", e);
        }

        request.setAttribute("genders", genderList);
        LOG.debug("Enter registration page");
        request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
    }
}
