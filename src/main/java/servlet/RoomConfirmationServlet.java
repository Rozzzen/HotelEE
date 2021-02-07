package servlet;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.RoomType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "RoomConfirmationServlet", urlPatterns = "/rooms/confirmation")
public class RoomConfirmationServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(RoomConfirmationServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("userID") == null) {
            RoomType roomType;
            try {
                roomType = roomDaoImpl.getRoomById(Integer.parseInt(request.getParameter("roomNum")),
                        (String) request.getSession().getAttribute("language"));
                request.setAttribute("room", roomType);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate checkout = LocalDate.parse(request.getParameter("checkout"), dtf);
                LocalDate checkin = LocalDate.parse(request.getParameter("checkin"), dtf);

                int daysBetween = (int) ChronoUnit.DAYS.between(checkin, checkout);

                roomType.setPrice(roomType.getPrice() * daysBetween);
                LOG.debug("Enter room confirmation page");
                request.getRequestDispatcher("/WEB-INF/view/room_confirmation.jsp").forward(request, response);
            } catch (SQLException e) {
                LOG.error("Room confirmation exception");
            }
        }
        else {
            int roomID = Integer.parseInt(request.getParameter("roomNum"));
            int userID = Integer.parseInt(request.getParameter("userID"));
            LocalDate checkin = LocalDate.parse(request.getParameter("checkin"));
            LocalDate checkout = LocalDate.parse(request.getParameter("checkout"));
            try {
                if(roomDaoImpl.insertRoomBooking(roomID, userID, checkin, checkout) > 0) {
                    LOG.debug("Room payment confirmed");
                    LOG.debug("Room successfully booked");
                    request.getSession().setAttribute("success", "Room successfully booked");
                }
            } catch (SQLException e) {
                LOG.error("Room confirmation exception");
                request.getSession().setAttribute("alert", "Failed to confirm room");
            }
            finally {
                response.sendRedirect("/");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/rooms");
    }
}
