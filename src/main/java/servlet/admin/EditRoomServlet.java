package servlet.admin;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.RoomType;
import pojo.WindowView;
import util.SortingPagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "EditRoomServlet", urlPatterns = "/admin/edit/rooms")
public class EditRoomServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(EditRoomServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("message") != null) {
            try {
                int roomNum = Integer.parseInt(request.getParameter("roomNum"));

                if (request.getParameter("message").equals("Edit")) {
                    int subtypeId = Integer.parseInt(request.getParameter("subtype"));
                    int windowId = Integer.parseInt(request.getParameter("windowName"));
                    roomDaoImpl.updateRoom(subtypeId, windowId, roomNum);
                    LOG.debug("Updated room: [" + roomNum + "]");
                } else if (request.getParameter("message").equals("Remove")) {
                    roomDaoImpl.deleteRoom(roomNum);
                    LOG.debug("Deleted room: [" + roomNum + "]");
                }
            } catch (SQLException e) {
                if(e instanceof SQLIntegrityConstraintViolationException) {
                    request.getSession().setAttribute("alert", "Cannot remove booked room");
                    LOG.error("Warned admin about booked rooms");
                }
                else LOG.error("Room edit exception");
            }
            finally {
                response.sendRedirect("/admin/edit/rooms");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            SortingPagination sortingPagination = new SortingPagination();
            sortingPagination.updateRoomRequest(request, roomDaoImpl);

            List<RoomType> roomTypeList = Collections.emptyList();
            List<WindowView> windowViewList = Collections.emptyList();

            try {
                roomTypeList = roomDaoImpl.getSubTypeNames((String) request.getSession().getAttribute("language"));
                windowViewList = roomDaoImpl.getWindowViewNames((String) request.getSession().getAttribute("language"));
            } catch (SQLException throwables) {
                LOG.error("Room/View list get exception");
            }

            request.setAttribute("subtypes", roomTypeList);
            request.setAttribute("windowViews", windowViewList);

            LOG.debug("Enter room edit page");
            request.getRequestDispatcher("/WEB-INF/view/admin/edit_rooms.jsp").forward(request, response);
        } catch (SQLException ex) {
            LOG.error("Failed to get room list exception");
        }
    }
}
