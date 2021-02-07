package servlet.admin;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import util.SortingPagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UserApplicationsServlet", urlPatterns = "/admin/applications")
public class UserApplicationsServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(UserApplicationsServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("message") == null) {
            try {
                SortingPagination sortingPagination = new SortingPagination();
                sortingPagination.updateApplicationRequest(request, roomDaoImpl);

                LOG.debug("Enter application page as admin");
                request.getRequestDispatcher("/WEB-INF/view/applications.jsp").forward(request, response);
            } catch (SQLException ex) {
                LOG.error("Failed to get room list exception");
            }
        } else {
            switch (request.getParameter("message")) {
                case "Reject": {
                    try {
                        int applicationId = Integer.parseInt(request.getParameter("applicationId"));
                        roomDaoImpl.deleteApplication(applicationId);
                        LOG.debug("Deleted application[" + applicationId + "] as admin");
                    } catch (SQLException e) {
                        LOG.error("Application deletion exception");
                    } finally {
                        response.sendRedirect("/admin/applications");
                    }
                    break;
                }
                case "Select room": {
                    try {
                        int roomNumber = Integer.parseInt(request.getParameter("roomNum"));
                        int applicationId = Integer.parseInt(request.getParameter("applicationId"));
                        roomDaoImpl.insertApplicationRoom(roomNumber, applicationId);
                        LOG.debug("Chosen room [" + roomNumber + "] for user as admin");
                    } catch (SQLException e) {
                        LOG.error("Application room selection exception");
                    } finally {
                        response.sendRedirect("/admin/applications");
                    }
                    break;
                }
                case "Find room":
                    try {
                        SortingPagination sortingPagination = new SortingPagination();
                        sortingPagination.updateRoomRequest(request, roomDaoImpl);
                        LOG.debug("Enter room selection page as admin");
                        request.getRequestDispatcher("/WEB-INF/view/rooms.jsp").forward(request, response);
                    } catch (SQLException ex) {
                        LOG.error("Failed to get room list exception");
                    }
                    break;
            }
        }
    }
}
