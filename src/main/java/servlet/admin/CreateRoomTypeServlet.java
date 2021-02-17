package servlet.admin;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.Amenity;
import pojo.RoomType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "CreateRoomTypeServlet", urlPatterns = "/admin/create/roomtypes")
public class CreateRoomTypeServlet extends HttpServlet {

    private final RoomDaoImpl roomDaoImpl = RoomDaoImpl.getInstance();
    private static final Logger LOG = Logger.getLogger(CreateRoomTypeServlet.class);


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String nameEN = request.getParameter("nameEN");
            String nameRU = request.getParameter("nameRU");
            String nameUA = request.getParameter("nameUA");
            int price = Integer.parseInt(request.getParameter("price"));
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String image = request.getParameter("image");
            int roomtype = Integer.parseInt(request.getParameter("roomtype"));

            List<Integer> amenityList;
            if (request.getParameterValues("amenity") == null) amenityList = Collections.emptyList();
            else amenityList = Arrays.stream(request.getParameterValues("amenity")).map(Integer::parseInt).collect(Collectors.toList());

            roomDaoImpl.createSubType(price, capacity, image, roomtype, nameEN, nameRU, nameUA, amenityList);
            LOG.debug("Created roomtype: [" + nameEN + "]");
        } catch (SQLException throwables) {
            LOG.error("Roomtype creation exception", throwables);
            throwables.printStackTrace();
        }
        finally {
            response.sendRedirect("/admin/edit/roomtypes");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        LOG.debug("Enter roomtype creation page");
        request.getRequestDispatcher("/WEB-INF/view/admin/create_roomtype.jsp").forward(request, response);

    }
}
