package servlet;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.RoomType;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "BookingServlet", urlPatterns = "/booking")
public class BookingServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(BookingServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int roomtypeID = Integer.parseInt(request.getParameter("roomtypeID"));
            int capacity =  Integer.parseInt(request.getParameter("capacity"));
            LocalDate checkin = LocalDate.parse(request.getParameter("checkin"));
            LocalDate checkout = LocalDate.parse(request.getParameter("checkout"));
            User user  = (User) request.getSession().getAttribute("user");
            int userID = user.getId();
            roomDaoImpl.insertUserApplication(roomtypeID, userID, capacity, checkin, checkout);
            LOG.debug("User[" + user.getName() + "] created booking[" + roomtypeID + ":" + capacity + "]");
            request.getSession().setAttribute("success", "Booking application created");
        } catch (SQLException e) {
            LOG.error("Booking exception", e);
            request.getSession().setAttribute("alert", "Unexpected error occured");
        }
        finally {
            response.sendRedirect("/");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Integer> list = Collections.emptyList();
        List<RoomType> roomTypeList = Collections.emptyList();

        try {
            list = roomDaoImpl.getCapacityList();
            roomTypeList = roomDaoImpl.getRoomTypeList((String) request.getSession().getAttribute("language"));

        } catch (SQLException e) {
            LOG.error("Capacities/roomtypes get list exception", e);
        }

        request.setAttribute("capacities", list);
        request.setAttribute("roomtypes", roomTypeList);
        LOG.debug("Enter booking page");
        request.getRequestDispatcher("/WEB-INF/view/booking.jsp").forward(request, response);
    }
}
