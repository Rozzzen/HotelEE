package util;

import dao.Impl.RoomDaoImpl;
import org.apache.log4j.Logger;
import pojo.UserApplication;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class SortingPagination {

    private static final Logger LOG = Logger.getLogger(SortingPagination.class);

    public void updateRoomRequest(HttpServletRequest request, RoomDaoImpl roomDaoImpl) throws SQLException {

        String language = (String) request.getSession().getAttribute("language");

        LocalDate checkin = LocalDate.now();
        LocalDate checkout = LocalDate.now();

        if (request.getParameter("checkin") != null && request.getParameter("checkout") != null) {
            checkin = LocalDate.parse(request.getParameter("checkin"));
            checkout = LocalDate.parse(request.getParameter("checkout"));
        }

        int page = 1;
        int recordsPerPage = 6;
        if (request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));

        int sortingIndex = 6;
        if (request.getParameter("sortBy") != null) {
            switch (request.getParameter("sortBy")) {
                case "PriceHL": sortingIndex = 0;break;
                case "PriceLH": sortingIndex = 1;break;
                case "CapacityHL": sortingIndex = 2;break;
                case "CapacityLH": sortingIndex = 3;break;
                case "Appartment": sortingIndex = 4;break;
                case "Aviability": sortingIndex = 5; break;
            }
        }
        request.setAttribute("rooms", roomDaoImpl.getRoomsListDefault((page - 1) * recordsPerPage, recordsPerPage, checkin, checkout, sortingIndex, language));

        int roomQuantity = roomDaoImpl.getRoomQuantity();
        int pageQuantity = (int) Math.ceil(roomQuantity * 1.0 / recordsPerPage);

        request.setAttribute("pageQuantity", pageQuantity);
        request.setAttribute("currentPage", page);
    }

    public void updateApplicationRequest(HttpServletRequest request, RoomDaoImpl roomDaoImpl) throws SQLException {

        String language = (String) request.getSession().getAttribute("language");

        int page = 1;
        int recordsPerPage = 5;
        if (request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));

        request.setAttribute("applications", roomDaoImpl.getUserApplicationsList((page - 1) * recordsPerPage, recordsPerPage, 0, language));

        int roomQuantity = roomDaoImpl.getApplicationQuantity();
        int pageQuantity = (int) Math.ceil(roomQuantity * 1.0 / recordsPerPage);

        request.setAttribute("pageQuantity", pageQuantity);
        request.setAttribute("currentPage", page);
    }

    public void updateUserApplicationsRequest(HttpServletRequest request, RoomDaoImpl roomDaoImpl, int userId) throws SQLException {

        String language = (String) request.getSession().getAttribute("language");

        int page = 1;
        int recordsPerPage = 5;
        if (request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));

        List<UserApplication> tempList = roomDaoImpl.getUserApplicationsList((page - 1) * recordsPerPage, recordsPerPage, userId, language);

        List<UserApplication> userApplications = new LinkedList<>();

        for (UserApplication userApplication : tempList) {

            LocalDate checkinDate = userApplication.getCheckin();
            LocalDate checkoutDate = userApplication.getCheckout();

            int daysBetween = (int) ChronoUnit.DAYS.between(checkinDate, checkoutDate);

            userApplication.getRoomType().setPrice(userApplication.getRoomType().getPrice() * daysBetween);
            userApplications.add(userApplication);

        }

        request.setAttribute("applications", userApplications);

        int roomQuantity = roomDaoImpl.getUserApplicationQuantity(userId);
        int pageQuantity = (int) Math.ceil(roomQuantity * 1.0 / recordsPerPage);

        request.setAttribute("pageQuantity", pageQuantity);
        request.setAttribute("currentPage", page);
    }

    public void updateRoomTypeRequest(HttpServletRequest request, RoomDaoImpl roomDaoImpl) throws SQLException {

        String language = (String) request.getSession().getAttribute("language");

        int page = 1;
        int recordsPerPage = 6;
        if (request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));

        request.setAttribute("subtypes", roomDaoImpl.getSubtypeList((page - 1) * recordsPerPage, recordsPerPage, language));

        int roomQuantity = roomDaoImpl.getSubtypeQuantity();
        int pageQuantity = (int) Math.ceil(roomQuantity * 1.0 / recordsPerPage);

        request.setAttribute("pageQuantity", pageQuantity);
        request.setAttribute("currentPage", page);
    }

    public void updateAmenityRequest(HttpServletRequest request, RoomDaoImpl roomDaoImpl) throws SQLException {

        String language = (String) request.getSession().getAttribute("language");

        int page = 1;
        int recordsPerPage = 6;
        if (request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));

        request.setAttribute("amenities", roomDaoImpl.getAmenityListPagination((page - 1) * recordsPerPage, recordsPerPage, language));

        int roomQuantity = roomDaoImpl.getAmenityQuantity();
        int pageQuantity = (int) Math.ceil(roomQuantity * 1.0 / recordsPerPage);

        request.setAttribute("pageQuantity", pageQuantity);
        request.setAttribute("currentPage", page);
    }

}
