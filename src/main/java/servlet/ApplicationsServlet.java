package servlet;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.RoomType;
import pojo.User;
import pojo.UserApplication;
import servlet.admin.UserApplicationsServlet;
import util.NameGenerator;
import util.SortingPagination;

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

@WebServlet(name = "ApplicationsServlet", urlPatterns = "/applications")
public class ApplicationsServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(ApplicationsServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int applicationId = Integer.parseInt(request.getParameter("applicationId"));

        if (request.getParameter("checkin") != null && request.getParameter("checkout") != null) {
            try {
                RoomType roomType = roomDaoImpl.getRoomByApplicationId(applicationId, (String) request.getSession().getAttribute("language"));

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate checkout = LocalDate.parse(request.getParameter("checkout"), dtf);
                LocalDate checkin = LocalDate.parse(request.getParameter("checkin"), dtf);

                int daysBetween = (int) ChronoUnit.DAYS.between(checkin, checkout);

                roomType.setPrice(roomType.getPrice() * daysBetween);
                request.setAttribute("room", roomType);
                LOG.debug("Enter payment confirmation page");
                request.getRequestDispatcher("/WEB-INF/view/room_confirmation.jsp").forward(request, response);
            } catch (SQLException e) {
                LOG.error("Room by application id exception", e);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("applicationId") != null) {
            int applicationId = Integer.parseInt(request.getParameter("applicationId"));

            switch (request.getParameter("message")) {
                case "Confirm":
                    try {
                        roomDaoImpl.confirmApplication(applicationId);
                        LOG.debug("Confirmed room for application[" + applicationId + "]");
                        UserApplication userApplication = roomDaoImpl.getApplicationById(applicationId);
                        int roomNum = userApplication.getRoom().getNumber();
                        int userId = userApplication.getUser().getId();
                        LocalDate checkin = userApplication.getCheckin();
                        LocalDate checkout = userApplication.getCheckout();
                        roomDaoImpl.insertRoomBooking(roomNum, userId, checkin, checkout);
                        LOG.debug("Created booking for room[" + roomNum + "] on dates[" + checkin + ":" + checkout + "]");
                        String eventName = new NameGenerator().generateName();
                        roomDaoImpl.createPaymentEvent(applicationId, eventName);
                        LOG.debug("Created payment event");
                    } catch (SQLException ex) {
                        LOG.error("Room confirmation exception", ex);
                    }
                    break;
                case "Reject":
                    try {
                        roomDaoImpl.rejectApplicationRoom(applicationId);
                        LOG.debug("Rejected application[" + applicationId + "]");
                    } catch (SQLException ex) {
                        LOG.error("Room rejection exception", ex);
                    }
                    break;
                case "Payment":
                    try {
                        roomDaoImpl.confirmPayment(applicationId);
                        LOG.debug("Confirmed payment on application[" + applicationId + "]");
                    } catch (SQLException ex) {
                        LOG.error("Payment confirment exception", ex);
                    }
                    break;
            }
            response.sendRedirect("/applications");
        } else {
            User user = (User) request.getSession().getAttribute("user");

            try {
                SortingPagination sortingPagination = new SortingPagination();
                sortingPagination.updateUserApplicationsRequest(request, roomDaoImpl, user.getId());

                LOG.debug("Enter application page");
                request.getRequestDispatcher("/WEB-INF/view/applications.jsp").forward(request, response);
            } catch (SQLException e) {
                LOG.error("Failed to get room list exception", e);
            }
        }
    }
}
