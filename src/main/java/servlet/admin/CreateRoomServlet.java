package servlet.admin;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.RoomType;
import pojo.WindowView;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "CreateRoomServlet", urlPatterns = "/admin/create/rooms")
public class CreateRoomServlet extends HttpServlet {

    private RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(CreateRoomServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            int roomNum = Integer.parseInt(request.getParameter("roomNum"));
            int subtypeId = Integer.parseInt(request.getParameter("subtypeId"));
            int windowViewId = Integer.parseInt(request.getParameter("windowViewId"));

            roomDaoImpl.createRoom(roomNum, subtypeId, windowViewId);
            LOG.debug("Created room: [" + roomNum + "]");
        } catch (SQLException throwables) {
            LOG.error("Room creation exception");
        }
        finally {
            response.sendRedirect("/admin/edit/rooms");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        LOG.debug("Enter room creation page");
        request.getRequestDispatcher("/WEB-INF/view/admin/create_room.jsp").forward(request, response);
    }
}
