package dao;

import pojo.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RoomDao {
    int getRoomQuantity() throws SQLException;
    int getApplicationQuantity() throws SQLException;
    int getSubtypeQuantity() throws SQLException;
    int getAmenityQuantity() throws SQLException;
    int getUserApplicationQuantity(int userId) throws SQLException;
    List<Room> getRoomsListDefault(int offset, int limit, LocalDate checkin, LocalDate checkout, int sortingIndex, String language) throws SQLException;
    RoomType getRoomById(int id, String language) throws SQLException;
    int insertRoomBooking(int roomID, int userID, LocalDate checkin, LocalDate checkout) throws SQLException;
    List<Amenity> getAllAmenities(String language) throws SQLException;
    List<Amenity> getAmenityListPagination(int offset, int limit, String language) throws SQLException;
    List<RoomType> getRoomTypeList(String language) throws SQLException;
    List<Integer> getCapacityList() throws SQLException;
    void insertUserApplication(int roomtypeID, int userID, int capacity, LocalDate checkin, LocalDate checkout) throws SQLException;
    List<UserApplication> getUserApplicationsList(int offset, int limit, int userId, String language) throws SQLException;
    void insertApplicationRoom(int roomNumber, int applicationId) throws SQLException;
    void deleteApplication(int applicationId) throws SQLException;
    void confirmApplication(int applicationId) throws SQLException;
    void rejectApplicationRoom(int applicationId) throws SQLException;
    RoomType getRoomByApplicationId(int applicationId, String language) throws SQLException;
    UserApplication getApplicationById(int applicationId) throws SQLException;
    void createPaymentEvent(int applicationId, String eventName) throws SQLException;
    void confirmPayment(int applicationId) throws SQLException;
    List<RoomType> getSubTypeNames(String language) throws SQLException;
    List<WindowView> getWindowViewNames(String language) throws SQLException;
    void updateRoom(int roomtypeId, int windowId, int roomNum) throws SQLException;
    void deleteRoom(int roomNum) throws SQLException;
    void deleteSubtype(int subtypeId) throws SQLException;
    List<RoomType> getSubtypeList(int offset, int limit, String language) throws SQLException;
    void updateSubType(int price, int capacity, String image, int roomType, String subname, int subtypeId, List<Integer> amenityList, String language) throws SQLException;
    void updateAmenitity(int id, String name, String language) throws SQLException;
    void deleteAmenity(int amenityId) throws SQLException;
    void createRoom(int roomNum, int subtypeId, int windowViewID) throws SQLException;
    void createAmenity(String nameEN, String nameRU, String nameUA) throws SQLException;
    void createSubType(int price, int capacity, String img, int roomtypeId, String nameEN, String nameRU, String nameUA, List<Integer> amenityList) throws SQLException;
}
