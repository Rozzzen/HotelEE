package servlet.admin;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CreateAmenityServlet", urlPatterns = "/admin/create/amenities")
public class CreateAmenityServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(CreateAmenityServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String nameEN = request.getParameter("nameEN");
            String nameRU = request.getParameter("nameRU");
            String nameUA = request.getParameter("nameUA");

            roomDaoImpl.createAmenity(nameEN, nameRU, nameUA);
            LOG.debug("Created amenity: [" + nameEN + "]");
        } catch (SQLException throwables) {
            LOG.error("Amenity creation exception", throwables);
        }
        finally {
            response.sendRedirect("/admin/edit/amenities");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Enter amenity creation page");
        request.getRequestDispatcher("/WEB-INF/view/admin/create_amenity.jsp").forward(request, response);
    }
}
