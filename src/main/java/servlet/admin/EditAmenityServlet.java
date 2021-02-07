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

@WebServlet(name = "EditAmenityServlet", urlPatterns = "/admin/edit/amenities")
public class EditAmenityServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(EditAmenityServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("message") != null) {
            try {
                int amenityId = Integer.parseInt(request.getParameter("amenityId"));

                if (request.getParameter("message").equals("Edit")) {
                    String name = request.getParameter("name");
                    roomDaoImpl.updateAmenitity(amenityId, name, (String) request.getSession().getAttribute("language"));
                    LOG.debug("Updated amenity: [" + name + "]");
                } else if (request.getParameter("message").equals("Remove")) {
                    LOG.debug("Deleted amenity: [" + amenityId + "]");
                    roomDaoImpl.deleteAmenity(amenityId);
                }
            } catch (SQLException e) {
                LOG.error("Amenity edit exception", e);
            }
            finally {
                response.sendRedirect("/admin/edit/amenities");
            }
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            SortingPagination sortingPagination = new SortingPagination();
            sortingPagination.updateAmenityRequest(request, roomDaoImpl);

            LOG.debug("Enter amenity edit page");
            request.getRequestDispatcher("/WEB-INF/view/admin/edit_amenities.jsp").forward(request, response);
        } catch (SQLException ex) {
            LOG.error("Failed to get room list exception", ex);
        }

    }
}
