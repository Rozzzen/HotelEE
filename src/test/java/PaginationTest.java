import dao.Impl.RoomDaoImpl;
import org.junit.Assert;
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
import java.util.*;

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
        List<Integer> expected = Arrays.asList(101, 102, 103, 104, 105, 106);

        for (Integer integer : expected) {
            roomDao.createRoom(
                    integer,
                    roomDao.createSubType(100, 2, "test", 5, "test", "test", "test", Collections.emptyList()),
                    5
            );
        }

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<Room> tempList = (List<Room>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("rooms"), Mockito.any());

        sortingPagination.updateRoomRequest(httpServletRequest, roomDao);
        roomDao.deleteAllRooms();
        roomDao.deleteAllSubtypes();

        for (int i = 0; i < 6; i++) {
            Assert.assertEquals(Optional.of(actual.get(i).getNumber()).get(), expected.get(i));
        }
    }

    @Test
    public void ApplicationPaginationTest() throws SQLException {

        List<UserApplication> actual = new ArrayList<>();
        List<Integer> expected = Arrays.asList(
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 62, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 62, 2, LocalDate.now(), LocalDate.now())
        );

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<UserApplication> tempList = (List<UserApplication>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("applications"), Mockito.any());

        sortingPagination.updateApplicationRequest(httpServletRequest, roomDao);
        roomDao.deleteAllApplications();

        Iterator<UserApplication> iterator1 = actual.iterator();
        Iterator<Integer> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(Optional.of(iterator1.next().getId()).get(), iterator2.next());
        }
    }

    @Test
    public void UserApplicationPaginationTest() throws SQLException {
        List<UserApplication> actual = new ArrayList<>();
        List<Integer> expected = Arrays.asList(
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 61, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 62, 2, LocalDate.now(), LocalDate.now()),
                roomDao.insertUserApplication(4, 62, 2, LocalDate.now(), LocalDate.now())
        );

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<UserApplication> tempList = (List<UserApplication>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("applications"), Mockito.any());

        sortingPagination.updateUserApplicationsRequest(httpServletRequest, roomDao, 61);
        roomDao.deleteAllApplications();

        Iterator<UserApplication> iterator1 = actual.iterator();
        Iterator<Integer> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(Optional.of(iterator1.next().getId()).get(), iterator2.next());
        }
    }

    @Test
    public void SubTypePaginationTest() throws SQLException {
        List<RoomType> actual = new ArrayList<>();
        List<Integer> expected = Arrays.asList(
                roomDao.createSubType(100, 2, "test", 5, "test", "test", "test", Collections.emptyList()),
                roomDao.createSubType(100, 2, "test", 5, "test", "test", "test", Collections.emptyList()),
                roomDao.createSubType(100, 2, "test", 5, "test", "test", "test", Collections.emptyList()),
                roomDao.createSubType(100, 2, "test", 5, "test", "test", "test", Collections.emptyList()),
                roomDao.createSubType(100, 2, "test", 5, "test", "test", "test", Collections.emptyList()),
                roomDao.createSubType(100, 2, "test", 5, "test", "test", "test", Collections.emptyList())
        );

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<RoomType> tempList = (List<RoomType>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("subtypes"), Mockito.any());

        sortingPagination.updateRoomTypeRequest(httpServletRequest, roomDao);
        roomDao.deleteAllSubtypes();

        Iterator<RoomType> iterator1 = actual.iterator();
        Iterator<Integer> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(Optional.of(iterator1.next().getId()).get(), iterator2.next());
        }
    }

    @Test
    public void AmenityPaginationTest() throws SQLException {

        List<Integer> expected = Arrays.asList(
                roomDao.createAmenity("test", "test", "test"),
                roomDao.createAmenity("test", "test", "test"),
                roomDao.createAmenity("test", "test", "test"),
                roomDao.createAmenity("test", "test", "test"),
                roomDao.createAmenity("test", "test", "test"),
                roomDao.createAmenity("test", "test", "test")
        );

        List<Amenity> actual = new ArrayList<>();

        Mockito.doAnswer((Answer<Void>) invocation -> {
            List<Amenity> tempList = (List<Amenity>) invocation.getArgument(1, Object.class);
            actual.addAll(tempList);
            return null;
        }).when(httpServletRequest).setAttribute(eq("amenities"), Mockito.any());

        sortingPagination.updateAmenityRequest(httpServletRequest, roomDao);
        roomDao.deleteAllAmenities();

        Iterator<Amenity> iterator1 = actual.iterator();
        Iterator<Integer> iterator2 = expected.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            Assert.assertEquals(Optional.of(iterator1.next().getId()).get(), iterator2.next());
        }
    }
}
