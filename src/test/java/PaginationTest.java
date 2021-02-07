import dao.Impl.RoomDaoImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import pojo.Amenity;
import pojo.Room;
import pojo.RoomType;
import pojo.UserApplication;
import util.SortingPagination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaginationTest {

    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    SortingPagination sortingPagination = new SortingPagination();
    HttpSession httpSession = mock(HttpSession.class);
    RoomDaoImpl roomDao;

    @BeforeEach
    public void onStartUp() {
        when(httpServletRequest.getParameter("page")).thenReturn(null);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("language")).thenReturn("en");
        Mockito.doAnswer((Answer<Void>) invocation -> null).when(httpServletRequest).setAttribute(eq("pageQuantity"), Mockito.any());
        Mockito.doAnswer((Answer<Void>) invocation -> null).when(httpServletRequest).setAttribute(eq("currentPage"), Mockito.any());
        roomDao = RoomDaoImpl.getInstance();
    }

    @Test
    public void RoomPaginationTest() throws SQLException {
        when(httpServletRequest.getParameter("checkin")).thenReturn("2021-02-10");
        when(httpServletRequest.getParameter("checkin")).thenReturn("2021-02-20");
        when(httpServletRequest.getParameter("sortBy")).thenReturn("CapacityLH");

        List<Room> actual = new ArrayList<>();
        List<Room> expected = roomDao.getRoomsListDefault(0, 6, LocalDate.parse("2021-02-04"), LocalDate.parse("2021-02-20"), 3, "en");

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<Room> tempList = (List<Room>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("rooms"), Mockito.any());

        sortingPagination.updateRoomRequest(httpServletRequest, roomDao);

        for (int i = 0; i < 6; i++) {
            Assert.assertEquals(expected.get(i).getNumber(), actual.get(i).getNumber());
        }
    }

    @Test
    public void ApplicationPaginationTest() throws SQLException {

        List<UserApplication> actual = new ArrayList<>();
        List<UserApplication> expected = roomDao.getUserApplicationsList(0, 6, 0, "en");

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<UserApplication> tempList = (List<UserApplication>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("applications"), Mockito.any());

        sortingPagination.updateApplicationRequest(httpServletRequest, roomDao);

        Iterator<UserApplication> iterator1 = actual.iterator();
        Iterator<UserApplication> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(iterator1.next().getId(), iterator2.next().getId());
        }
    }

    @Test
    public void UserApplicationPaginationTest() throws SQLException {

        List<UserApplication> actual = new ArrayList<>();
        List<UserApplication> expected = roomDao.getUserApplicationsList(0, 6, 2, "en");

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<UserApplication> tempList = (List<UserApplication>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("applications"), Mockito.any());

        sortingPagination.updateUserApplicationsRequest(httpServletRequest, roomDao, 2);

        Iterator<UserApplication> iterator1 = actual.iterator();
        Iterator<UserApplication> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(iterator1.next().getId(), iterator2.next().getId());
        }
    }

    @Test
    public void SubTypePaginationTest() throws SQLException {

        List<RoomType> actual = new ArrayList<>();
        List<RoomType> expected = roomDao.getSubtypeList(0, 6, "en");

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<RoomType> tempList = (List<RoomType>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("subtypes"), Mockito.any());

        sortingPagination.updateRoomTypeRequest(httpServletRequest, roomDao);

        Iterator<RoomType> iterator1 = actual.iterator();
        Iterator<RoomType> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(iterator1.next().getId(), iterator2.next().getId());
        }
    }

    @Test
    public void AmenityPaginationTest() throws SQLException {
        List<Amenity> actual = new ArrayList<>();
        List<Amenity> expected = roomDao.getAmenityListPagination(0, 6, "en");

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<Amenity> tempList = (List<Amenity>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("amenities"), Mockito.any());

        sortingPagination.updateRoomTypeRequest(httpServletRequest, roomDao);

        Iterator<Amenity> iterator1 = actual.iterator();
        Iterator<Amenity> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(iterator1.next().getId(), iterator2.next().getId());
        }
    }
}
