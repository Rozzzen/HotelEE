package servlet.admin;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.Amenity;
import pojo.RoomType;
import util.SortingPagination;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "EditRoomTypeServlet", urlPatterns = "/admin/edit/roomtypes")
public class EditRoomTypeServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(EditRoomTypeServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("message") != null) {
            try {
                int subtypeId = Integer.parseInt(request.getParameter("subtypeId"));

                if (request.getParameter("message").equals("Edit")) {

                    String subname = request.getParameter("subname");
                    int price = Integer.parseInt(request.getParameter("price"));
                    int capacity = Integer.parseInt(request.getParameter("capacity"));
                    String image = request.getParameter("image");
                    int roomtype = Integer.parseInt(request.getParameter("roomtype"));

                    List<Integer> amenityList;
                    if (request.getParameterValues("amenity") == null) amenityList = Collections.emptyList();
                    else
                        amenityList = Arrays.stream(request.getParameterValues("amenity")).map(Integer::parseInt).collect(Collectors.toList());
                    roomDaoImpl.updateSubType(price, capacity, image, roomtype, subname, subtypeId, amenityList, (String) request.getSession().getAttribute("language"));
                    LOG.debug("Updated roomtype: [" + subname + "]");
                } else if (request.getParameter("message").equals("Remove")) {
                    roomDaoImpl.deleteSubtype(subtypeId);
                    LOG.debug("Deleted roomtype: [" + subtypeId + "]");
                }
            } catch (SQLException ex) {
                if(ex instanceof SQLIntegrityConstraintViolationException) {
                    request.getSession().setAttribute("alert", "Before removing this roomtype you have to change roomtypes of all rooms with this roomtype");
                    LOG.error("Warned admin about roomtype and room", ex);
                }
                else LOG.error("Roomtype edit page exception");
            } finally {
                response.sendRedirect("/admin/edit/roomtypes");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            SortingPagination sortingPagination = new SortingPagination();
            sortingPagination.updateRoomTypeRequest(request, roomDaoImpl);

            List<Amenity> amenityList = Collections.emptyList();
            List<RoomType> roomTypeList = Collections.emptyList();

            try {
                amenityList = roomDaoImpl.getAllAmenities((String) request.getSession().getAttribute("language"));
                roomTypeList = roomDaoImpl.getRoomTypeList((String) request.getSession().getAttribute("language"));
            } catch (SQLException throwables) {
                LOG.error("Amenity/roomtype list get exception", throwables);
            }

            request.setAttribute("roomtypes", roomTypeList);
            request.setAttribute("amenities", amenityList);

            LOG.debug("Enter roomtype edit page");
            request.getRequestDispatcher("/WEB-INF/view/admin/edit_roomtypes.jsp").forward(request, response);
        } catch (SQLException ex) {
            LOG.error("Failed to get room lost exception", ex);
        }
    }
}
