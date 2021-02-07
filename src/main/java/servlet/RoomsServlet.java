package servlet;

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

@WebServlet(name = "RoomsServlet", urlPatterns = "/rooms")
public class RoomsServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(RoomsServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            SortingPagination sortingPagination = new SortingPagination();
            sortingPagination.updateRoomRequest(request, roomDaoImpl);
            LOG.debug("Enter rooms page");
            request.getRequestDispatcher("/WEB-INF/view/rooms.jsp").forward(request, response);
        } catch (SQLException e) {
            LOG.error("Failed to get room list exception");
        }
    }
}
