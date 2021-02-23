package dao.Impl;

import dao.RoomDao;
import pojo.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class RoomDaoImpl implements RoomDao {

    private static RoomDaoImpl instance;
    private static Connection connection;

    public static RoomDaoImpl getInstance() {
        if (instance == null) {
            instance = new RoomDaoImpl();
        }
        return instance;
    }

    private RoomDaoImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            connection = DriverManager.getConnection(properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password"));
        } catch (ClassNotFoundException | IOException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    private final String SELECT_ROOM_INFO_DEFAULT = "SELECT room.room_number, r.id AS roomtypeID, r.price, r.capacity, r.img_path, rt.roomtype_name, rst.subtype_name, wv.id as window_id,\n" +
            "                   wvt.window_view_name,\n" +
            "       CASE WHEN s.id IS NULL THEN 'Free' ELSE s.name END AS status_name\n" +
            "FROM rooms room\n" +
            "         JOIN room_subtypes r ON room.roomtype_id = r.id\n" +
            "         JOIN roomtype_translations rt ON r.roomtype_id = rt.roomtype_id AND rt.language = ?\n" +
            "         JOIN window_views wv ON room.window_view_id = wv.id\n" +
            "         JOIN room_subtype_translations rst on r.id = rst.roomtype_id AND rst.language = ?\n" +
            "         JOIN window_view_translations wvt ON wv.id = wvt.window_view_id AND wvt.language = ?\n" +
            "         LEFT JOIN room_bookings rb ON room.room_number = rb.room_id\n" +
            "    AND (? < unavailable_until_date AND ? > unavailable_since_date)\n" +
            "         LEFT JOIN statuses s on rb.status_id = s.id\n" +
            "ORDER BY room.room_number\n" +
            "LIMIT ? OFFSET ?";

    private final String SELECT_ROOM_BY_ID = "SELECT r.price, r.img_path, rst.subtype_name\n" +
            "FROM rooms\n" +
            "         JOIN room_subtypes r\n" +
            "              ON rooms.roomtype_id = r.id\n" +
            "         JOIN room_subtype_translations rst on r.id = rst.roomtype_id AND rst.language = ?\n" +
            "WHERE room_number = ?";

    private final String SORT_PRICEHL = SELECT_ROOM_INFO_DEFAULT.replace("ORDER BY room.room_number", "ORDER BY r.price DESC");
    private final String SORT_PRICELH = SELECT_ROOM_INFO_DEFAULT.replace("ORDER BY room.room_number", "ORDER BY r.price");
    private final String SORT_CAPACITYHL = SELECT_ROOM_INFO_DEFAULT.replace("ORDER BY room.room_number", "ORDER BY r.capacity DESC");
    private final String SORT_CAPACITYLH = SELECT_ROOM_INFO_DEFAULT.replace("ORDER BY room.room_number", "ORDER BY r.capacity");
    private final String SORT_APPARTMENT = SELECT_ROOM_INFO_DEFAULT.replace("ORDER BY room.room_number", "ORDER BY rt.roomtype_name");

    int aviabilityIndex = SELECT_ROOM_INFO_DEFAULT.indexOf("ORDER BY room.room_number");
    private final String SORT_AVIABILITY = SELECT_ROOM_INFO_DEFAULT.substring(0, aviabilityIndex) +
            "WHERE s.id IS NULL " + SELECT_ROOM_INFO_DEFAULT.substring(aviabilityIndex);

    List<String> sortingList = Arrays.asList(SORT_PRICEHL, SORT_PRICELH, SORT_CAPACITYHL, SORT_CAPACITYLH,
            SORT_APPARTMENT, SORT_AVIABILITY, SELECT_ROOM_INFO_DEFAULT);

    private final String INSERT_ROOM_BOOKING = "INSERT INTO room_bookings(room_id, unavailable_until_date, status_id, booked_by_id, unavailable_since_date) VALUES\n" +
            "    (?, ?, 4, ?, ?)";

    private final String SELECT_AMENITIES_BY_ID = "SELECT roomtype_amenity.amenity_id, amenity_translations.amenity_name\n" +
            "FROM room_subtypes\n" +
            "         JOIN roomtype_amenity\n" +
            "              ON roomtype_amenity.roomtype_id = room_subtypes.id\n" +
            "         JOIN amenity_translations\n" +
            "              ON amenity_translations.amenity_id = roomtype_amenity.amenity_id AND amenity_translations.language = ?\n" +
            "WHERE roomtype_amenity.roomtype_id = ?";

    private final String COUNT_ROOMS = "SELECT COUNT(*) AS quantity FROM rooms;";
    private final String COUNT_APPLICATIONS = "SELECT COUNT(*) AS quantity FROM user_applications;";
    private final String COUNT_SUBTYPES = "SELECT COUNT(*) AS quantity FROM room_subtypes;";
    private final String COUNT_AMENITIES = "SELECT COUNT(*) AS quantity FROM amenities;";
    private final String COUNT_USER_APPLICATIONS = "SELECT COUNT(*) AS quantity FROM user_applications ua WHERE ua.user_id = ?;";
    private final String SELECT_ROOMTYPE_CAPACITIES = "SELECT capacity FROM room_subtypes GROUP BY capacity ORDER BY capacity ASC";
    private final String SELECT_ROOMTYPES = "SELECT rt.roomtype_id as id, rt.roomtype_name\n" +
            "FROM roomtypes r\n" +
            "         JOIN roomtype_translations rt on r.id = rt.roomtype_id and rt.language = ?\n" +
            "ORDER BY rt.roomtype_id";
    private final String SELECT_SUBTYPE_LIST = "SELECT rs.id, rs.price, rs.capacity, rs.img_path, rt.roomtype_name, rst.subtype_name\n" +
            "FROM room_subtypes rs\n" +
            "         JOIN room_subtype_translations rst on rs.id = rst.roomtype_id AND rst.language = ?\n" +
            "         JOIN roomtype_translations rt on rs.roomtype_id = rt.roomtype_id AND rt.language = ?\n" +
            "LIMIT ? OFFSET ?;";
    private final String INSERT_USER_APPLICATION = "INSERT INTO user_applications(user_id, roomtype_id, capacity, checkin, checkout)\n" +
            "VALUES(?, ?, ?, ?, ?);";
    private final String SELECT_USER_APPLICATION_LIST = "SELECT ua.id, u.name, u.surname, u.phone, rt.roomtype_name, ua.capacity, ua.response_room_number, ua.is_paid, ua.is_booked, ua.checkin, ua.checkout, rs.price\n" +
            "FROM user_applications ua\n" +
            "         JOIN users u on ua.user_id = u.id\n" +
            "         JOIN roomtype_translations rt on ua.roomtype_id = rt.roomtype_id AND rt.language = ?\n" +
            "         LEFT JOIN rooms r ON ua.response_room_number = r.room_number\n" +
            "         LEFT JOIN room_subtypes rs ON r.roomtype_id = rs.id\n" +
            "WHERE u.id = ?\n" +
            "ORDER BY ua.id\n" +
            "LIMIT ? OFFSET ?";
    private final String SELECT_APPLICATION_LIST = "SELECT ua.id, u.name, u.surname, u.phone, rt.roomtype_name, ua.capacity, ua.response_room_number, ua.is_paid, ua.is_booked, ua.checkin, ua.checkout, rs.price\n" +
            "FROM user_applications ua\n" +
            "         JOIN users u on ua.user_id = u.id\n" +
            "         JOIN roomtype_translations rt on ua.roomtype_id = rt.roomtype_id AND rt.language = ?\n" +
            "         LEFT JOIN rooms r ON ua.response_room_number = r.room_number\n" +
            "         LEFT JOIN room_subtypes rs ON r.roomtype_id = rs.id\n" +
            "ORDER BY ua.id\n" +
            "LIMIT ? OFFSET ?";
    private final String INSERT_APPLICATION_ROOM = "UPDATE user_applications t SET t.response_room_number = ? WHERE t.id = ?";
    private final String DELETE_APPLICATION = "DELETE FROM user_applications ua WHERE ua.id = ?;";
    private final String CONFIRM_APPLICATION = "UPDATE user_applications t SET t.is_booked = 1 WHERE t.id = ?";
    private final String REJECT_APPLICATION = "UPDATE user_applications t SET t.response_room_number = null WHERE t.id = ?";
    private final String SELECT_APPLICATION_BY_ID = "SELECT ua.response_room_number, ua.user_id, ua.checkin, ua.checkout FROM user_applications ua WHERE ua.id = ?";
    private final String SELECT_ROOM_BY_APPLICATION_ID = "SELECT r.price, r.img_path, rt.roomtype_name, r2.room_number\n" +
            "FROM user_applications\n" +
            "         JOIN rooms r2 on user_applications.response_room_number = r2.room_number\n" +
            "         JOIN room_subtypes r ON r2.roomtype_id = r.id\n" +
            "         JOIN roomtype_translations rt ON r.roomtype_id = rt.roomtype_id AND rt.language = ?\n" +
            "WHERE user_applications.id = ?";
    private final String CREATE_PAYMENT_EVENT = "CREATE EVENT ?\n" +
            "    ON SCHEDULE AT CURDATE() + INTERVAL 2 DAY\n" +
            "    DO\n" +
            "    DELETE rb\n" +
            "    FROM room_bookings rb\n" +
            "             INNER JOIN user_applications ua ON\n" +
            "            ua.user_id = rb.booked_by_id AND ua.checkin = rb.unavailable_since_date AND\n" +
            "            ua.checkout = rb.unavailable_until_date AND ua.response_room_number = rb.room_id\n" +
            "    WHERE ua.is_paid = 0\n" +
            "      AND ua.id = ?;";
    private final String CONFIRM_PAYMENT = "UPDATE user_applications t SET t.is_paid = 1 WHERE t.id = ?";
    private final String SELECT_SUBTYPE_NAMES = "SELECT rst.subtype_name, rs.id FROM room_subtypes AS rs\n" +
            "                JOIN room_subtype_translations rst on rs.id = rst.roomtype_id AND rst.language = ?";
    private final String SELECT_WINDOW_VIEW_NAMES = "SELECT wvt.window_view_name, wv.id FROM window_views wv\n" +
            "            join window_view_translations wvt on wv.id = wvt.window_view_id AND wvt.language = ?";
    private final String UPDATE_ROOM = "UPDATE rooms t SET t.roomtype_id = ?, t.window_view_id = ? WHERE t.room_number = ?";
    private final String DELETE_ROOM = "delete from rooms r where r.room_number = ?";
    private final String DELETE_SUBTYPE = "DELETE rs FROM room_subtypes rs WHERE id = ?;";
    private final String DELETE_AMENITY = "DELETE a FROM amenities a WHERE a.id = ?;";
    private final String SELECT_AMENITIES_LIST = "SELECT a.id, at.amenity_name\n" +
            "FROM amenities a\n" +
            "         JOIN amenity_translations at on a.id = at.amenity_id AND at.language = ?";
    private final String SELECT_PAGINATED_AMENITIES_LIST = "SELECT a.id, at.amenity_name\n" +
            "FROM amenities a\n" +
            "         JOIN amenity_translations at on a.id = at.amenity_id AND at.language = ?\n" +
            "LIMIT ? OFFSET ?;";

    private final String UPDATE_SUBTYPE = "UPDATE room_subtypes t\n" +
            "    JOIN room_subtype_translations rst on t.id = rst.roomtype_id AND rst.language = ?\n" +
            "SET t.price          = ?,\n" +
            "    t.capacity       = ?,\n" +
            "    t.img_path       = ?,\n" +
            "    t.roomtype_id    = ?,\n" +
            "    rst.subtype_name = ?\n" +
            "WHERE t.id = ?";
    private final String UPDATE_AMENITY = "UPDATE amenities a\n" +
            "    JOIN amenity_translations t on a.id = t.amenity_id AND t.language = ?\n" +
            "SET t.amenity_name = ?\n" +
            "WHERE a.id = ?";
    private final String DELETE_AMENITIES_BY_ID = "DELETE ra FROM roomtype_amenity ra\n" +
            "JOIN room_subtypes rs on ra.roomtype_id = rs.id\n" +
            "WHERE rs.id = ?;";
    private final String INSERT_AMENITIES = "INSERT INTO roomtype_amenity(amenity_id, roomtype_id) VALUE(?,?);";
    private final String CREATE_ROOM = "INSERT INTO rooms (room_number, roomtype_id, window_view_id) VALUES (?, ?, ?)";
    private final String CREATE_AMENITY = "INSERT INTO amenities VALUES ()";
    private final String CREATE_AMENITY_TRANSLATIONS = "INSERT INTO amenity_translations(amenity_id, language, amenity_name) VALUES (?,?,?);";
    private final String CREATE_SUBTYPE = "INSERT INTO room_subtypes (price, capacity, img_path, roomtype_id) VALUES (?, ?, ?, ?)";
    private final String CREATE_SUBTYPE_TRANSLATION = "INSERT INTO room_subtype_translations(roomtype_id, language, subtype_name) VALUES (?,?,?);";

    @Override
    public int getRoomQuantity() throws SQLException {
        int result = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROOMS);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) result = resultSet.getInt("quantity");


        return result;
    }

    @Override
    public int getApplicationQuantity() throws SQLException {

        int result = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(COUNT_APPLICATIONS);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) result = resultSet.getInt("quantity");


        return result;
    }

    @Override
    public int getUserApplicationQuantity(int userId) throws SQLException {

        int result = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(COUNT_USER_APPLICATIONS);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) result = resultSet.getInt("quantity");
        return result;
    }

    @Override
    public int getSubtypeQuantity() throws SQLException {

        int result = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(COUNT_SUBTYPES);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) result = resultSet.getInt("quantity");


        return result;
    }

    @Override
    public int getAmenityQuantity() throws SQLException {

        int result = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(COUNT_AMENITIES);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) result = resultSet.getInt("quantity");


        return result;
    }

    @Override
    public List<Room> getRoomsListDefault(int offset, int limit, LocalDate checkin, LocalDate checkout, int sortingIndex, String language) throws SQLException {

        List<Room> result = new ArrayList<>();
        List<Amenity> amenities = new ArrayList<>();

        PreparedStatement roomStatement = connection.prepareStatement(sortingList.get(sortingIndex));
        PreparedStatement amenityStatement = connection.prepareStatement(SELECT_AMENITIES_BY_ID);
        roomStatement.setString(1, language);
        roomStatement.setString(2, language);
        roomStatement.setString(3, language);
        roomStatement.setObject(4, checkin);
        roomStatement.setObject(5, checkout);
        roomStatement.setInt(6, limit);
        roomStatement.setInt(7, offset);
        ResultSet roomResultSet = roomStatement.executeQuery();
        while (roomResultSet.next()) {
            int roomNumber = roomResultSet.getInt("room_number");
            int roomtypeId = roomResultSet.getInt("roomtypeID");
            String name = roomResultSet.getString("roomtype_name");
            String subname = roomResultSet.getString("subtype_name");
            int price = roomResultSet.getInt("price");
            int capacity = roomResultSet.getInt("capacity");
            String imgPath = roomResultSet.getString("img_path");
            String windowName = roomResultSet.getString("window_view_name");
            int windowId = roomResultSet.getInt("window_id");

            WindowView windowViewName = new WindowView(); //windowId, windowName
            windowViewName.setId(windowId);
            windowViewName.setName(windowName);

            String status = roomResultSet.getString("status_name");

            amenityStatement.setString(1, language);
            amenityStatement.setInt(2, roomtypeId);
            ResultSet amenityResultSet = amenityStatement.executeQuery();
            while (amenityResultSet.next()) {
                int amenityId = amenityResultSet.getInt("amenity_id");
                String amenityName = amenityResultSet.getString("amenity_name");

                Amenity amenity = new Amenity(); //amenityId, amenityName
                amenity.setId(amenityId);
                amenity.setName(amenityName);

                amenities.add(amenity);
            }

            RoomType roomType = new RoomType(); //roomtypeId, name, subname, price, capacity, imgPath, amenities
            roomType.setId(roomtypeId);
            roomType.setName(name);
            roomType.setSubname(subname);
            roomType.setPrice(price);
            roomType.setCapacity(capacity);
            roomType.setImg(imgPath);
            roomType.setAmenityList(amenities);

            Room room = new Room(); //roomNumber, roomType, windowViewName, status
            room.setNumber(roomNumber);
            room.setRoomType(roomType);
            room.setWindowView(windowViewName);
            room.setStatus(status);

            result.add(room);
            amenities = new ArrayList<>();
        }

        return result;
    }

    @Override
    public RoomType getRoomById(int id, String language) throws SQLException {

        RoomType result = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOM_BY_ID);
        preparedStatement.setString(1, language);
        preparedStatement.setInt(2, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int price = resultSet.getInt("price");
            String img = resultSet.getString("img_path");
            String subname = resultSet.getString("subtype_name");

            result = new RoomType(); //id, subname, price, img
            result.setId(id);
            result.setSubname(subname);
            result.setPrice(price);
            result.setImg(img);
        }


        return result;
    }

    @Override
    public int insertRoomBooking(int roomID, int userID, LocalDate checkin, LocalDate checkout) throws SQLException {
        int result;
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROOM_BOOKING);
        preparedStatement.setInt(1, roomID);
        preparedStatement.setObject(2, checkout);
        preparedStatement.setInt(3, userID);
        preparedStatement.setObject(4, checkin);
        result = preparedStatement.executeUpdate();

        return result;
    }

    @Override
    public List<Amenity> getAllAmenities(String language) throws SQLException {

        List<Amenity> result = new LinkedList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_AMENITIES_LIST);
        preparedStatement.setString(1, language);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("amenity_name");
            Amenity amenity = new Amenity(); //id, name
            amenity.setId(id);
            amenity.setName(name);
            result.add(amenity);
        }


        return result;
    }

    @Override
    public List<Amenity> getAmenityListPagination(int offset, int limit, String language) throws SQLException {

        List<Amenity> result = new LinkedList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PAGINATED_AMENITIES_LIST);
        preparedStatement.setString(1, language);
        preparedStatement.setInt(2, limit);
        preparedStatement.setInt(3, offset);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("amenity_name");
            Amenity amenity = new Amenity(); //id, name
            amenity.setId(id);
            amenity.setName(name);
            result.add(amenity);
        }


        return result;
    }

    @Override
    public List<RoomType> getRoomTypeList(String language) throws SQLException {
        List<RoomType> result = new LinkedList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOMTYPES);
        preparedStatement.setString(1, language);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("roomtype_name");

            RoomType roomType = new RoomType(); //id, name
            roomType.setId(id);
            roomType.setName(name);

            result.add(roomType);
        }

        return result;
    }

    @Override
    public List<Integer> getCapacityList() throws SQLException {
        List<Integer> result = new LinkedList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOMTYPE_CAPACITIES);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int capacity = resultSet.getInt("capacity");
            result.add(capacity);
        }

        return result;
    }

    @Override
    public int insertUserApplication(int roomtypeID, int userID, int capacity, LocalDate checkin, LocalDate checkout) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_APPLICATION, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, userID);
        preparedStatement.setInt(2, roomtypeID);
        preparedStatement.setInt(3, capacity);
        preparedStatement.setObject(4, checkin);
        preparedStatement.setObject(5, checkout);
        preparedStatement.executeUpdate();

        int applicationId = 0;
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) applicationId = generatedKeys.getInt(1);

        return applicationId;
    }

    @Override
    public List<UserApplication> getUserApplicationsList(int offset, int limit, int userId, String language) throws SQLException {

        List<UserApplication> result = new LinkedList<>();
        String query = SELECT_USER_APPLICATION_LIST;
        if (userId == 0) query = SELECT_APPLICATION_LIST;

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        if (userId != 0) {
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, limit);
            preparedStatement.setInt(4, offset);
        } else {
            preparedStatement.setString(1, language);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String phone = resultSet.getString("phone");
            String roomtypeName = resultSet.getString("roomtype_name");
            int capacity = resultSet.getInt("capacity");
            int roomNum = resultSet.getInt("response_room_number");
            int isPaid = resultSet.getInt("is_paid");
            int isBooked = resultSet.getInt("is_booked");
            int price = resultSet.getInt("price");
            LocalDate checkin = ((Date) resultSet.getObject("checkin")).toLocalDate();
            LocalDate checkout = ((Date) resultSet.getObject("checkout")).toLocalDate();

            User user = new User(); //name, surname, phone
            user.setName(name);
            user.setSurname(surname);
            user.setPhone(phone);

            RoomType roomType = new RoomType(); //roomtypeName, price
            roomType.setName(roomtypeName);
            roomType.setPrice(price);

            Room room = new Room(); //roomNum
            room.setNumber(roomNum);

            UserApplication userApplication = new UserApplication(); //id, user, roomType, capacity, checkin, checkout, room, isPaid, isBooked
            userApplication.setId(id);
            userApplication.setUser(user);
            userApplication.setRoomType(roomType);
            userApplication.setCapacity(capacity);
            userApplication.setCheckin(checkin);
            userApplication.setCheckout(checkout);
            userApplication.setRoom(room);
            userApplication.setIsBooked(isBooked);
            userApplication.setIsPaid(isPaid);

            result.add(userApplication);
        }
        return result;
    }

    @Override
    public void insertApplicationRoom(int roomNumber, int applicationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_APPLICATION_ROOM);
        preparedStatement.setInt(1, roomNumber);
        preparedStatement.setInt(2, applicationId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteApplication(int applicationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_APPLICATION);
        preparedStatement.setInt(1, applicationId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void confirmApplication(int applicationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CONFIRM_APPLICATION);
        preparedStatement.setInt(1, applicationId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void rejectApplicationRoom(int applicationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(REJECT_APPLICATION);
        preparedStatement.setInt(1, applicationId);
        preparedStatement.executeUpdate();
    }

    @Override
    public RoomType getRoomByApplicationId(int applicationId, String language) throws SQLException {

        RoomType result = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOM_BY_APPLICATION_ID);
        preparedStatement.setString(1, language);
        preparedStatement.setInt(2, applicationId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int price = resultSet.getInt("price");
            String img = resultSet.getString("img_path");
            String name = resultSet.getString("roomtype_name");
            int roomNum = resultSet.getInt("room_number");

            result = new RoomType(); //roomNum, name, price, img
            result.setId(roomNum);
            result.setName(name);
            result.setPrice(price);
            result.setImg(img);
        }

        return result;
    }

    @Override
    public UserApplication getApplicationById(int applicationId) throws SQLException {

        UserApplication result = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_APPLICATION_BY_ID);
        preparedStatement.setInt(1, applicationId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int roomNum = resultSet.getInt("response_room_number");
            int userId = resultSet.getInt("user_id");
            LocalDate checkin = ((Date) resultSet.getObject("checkin")).toLocalDate();
            LocalDate checkout = ((Date) resultSet.getObject("checkout")).toLocalDate();
            Room room = new Room(); //roomNum
            room.setNumber(roomNum);
            User user = new User(); //userId
            user.setId(userId);

            result = new UserApplication(); //user, room, checkin, checkout
            result.setUser(user);
            result.setRoom(room);
            result.setCheckin(checkin);
            result.setCheckout(checkout);
        }

        return result;
    }

    @Override
    public void createPaymentEvent(int applicationId, String eventName) throws SQLException {
        String CREATE_PAYMENT_EVENT_QUERY = CREATE_PAYMENT_EVENT.replace(
                "CREATE EVENT ?", "CREATE EVENT ".concat(eventName));
        PreparedStatement preparedStatement = connection.prepareStatement(CREATE_PAYMENT_EVENT_QUERY);
        preparedStatement.setInt(1, applicationId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void confirmPayment(int applicationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CONFIRM_PAYMENT);
        preparedStatement.setInt(1, applicationId);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<RoomType> getSubTypeNames(String language) throws SQLException {

        List<RoomType> result = new LinkedList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBTYPE_NAMES);
        preparedStatement.setString(1, language);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String subname = resultSet.getString("subtype_name");
            RoomType roomType = new RoomType();
            roomType.setId(id);
            roomType.setSubname(subname);
            result.add(roomType);
        }

        return result;
    }

    @Override
    public List<WindowView> getWindowViewNames(String language) throws SQLException {

        List<WindowView> result = new LinkedList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WINDOW_VIEW_NAMES);
        preparedStatement.setString(1, language);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("window_view_name");
            WindowView windowView = new WindowView(); //id, name
            windowView.setId(id);
            windowView.setName(name);
            result.add(windowView);
        }


        return result;
    }

    @Override
    public void updateRoom(int roomtypeId, int windowId, int roomNum) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROOM);
        preparedStatement.setInt(1, roomtypeId);
        preparedStatement.setInt(2, windowId);
        preparedStatement.setInt(3, roomNum);
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteRoom(int roomNum) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ROOM);
        preparedStatement.setInt(1, roomNum);
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteSubtype(int subtypeId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBTYPE);
        preparedStatement.setInt(1, subtypeId);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<RoomType> getSubtypeList(int offset, int limit, String language) throws SQLException {

        List<RoomType> result = new ArrayList<>();
        List<Amenity> amenities = new ArrayList<>();

        PreparedStatement subTypeStatement = connection.prepareStatement(SELECT_SUBTYPE_LIST);
        PreparedStatement amenityStatement = connection.prepareStatement(SELECT_AMENITIES_BY_ID);
        subTypeStatement.setString(1, language);
        subTypeStatement.setString(2, language);
        subTypeStatement.setInt(3, limit);
        subTypeStatement.setInt(4, offset);
        ResultSet subTypeResultSet = subTypeStatement.executeQuery();
        while (subTypeResultSet.next()) {
            int id = subTypeResultSet.getInt("id");
            int price = subTypeResultSet.getInt("price");
            int capacity = subTypeResultSet.getInt("capacity");
            String imgPath = subTypeResultSet.getString("img_path");
            String name = subTypeResultSet.getString("roomtype_name");
            String subname = subTypeResultSet.getString("subtype_name");

            amenityStatement.setString(1, language);
            amenityStatement.setInt(2, id);
            ResultSet amenityResultSet = amenityStatement.executeQuery();
            while (amenityResultSet.next()) {
                int amenityId = amenityResultSet.getInt("amenity_id");
                String amenityName = amenityResultSet.getString("amenity_name");

                Amenity amenity = new Amenity(); //amenityId, amenityName
                amenity.setId(amenityId);
                amenity.setName(amenityName);

                amenities.add(amenity);
            }
            RoomType roomType = new RoomType(); //id, name, subname, price, capacity, imgPath, amenities
            roomType.setId(id);
            roomType.setName(name);
            roomType.setSubname(subname);
            roomType.setPrice(price);
            roomType.setCapacity(capacity);
            roomType.setImg(imgPath);
            roomType.setAmenityList(amenities);
            result.add(roomType);
            amenities = new ArrayList<>();
        }
        return result;
    }

    @Override
    public void updateSubType(int price, int capacity, String image, int roomType, String subname, int subtypeId, List<Integer> amenityList, String language) throws SQLException {
        PreparedStatement subTypeStatement = connection.prepareStatement(UPDATE_SUBTYPE);
        PreparedStatement amenityDeleTionStatement = connection.prepareStatement(DELETE_AMENITIES_BY_ID);
        PreparedStatement amenityInsertionStatement = connection.prepareStatement(INSERT_AMENITIES);
        subTypeStatement.setString(1, language);
        subTypeStatement.setInt(2, price);
        subTypeStatement.setInt(3, capacity);
        subTypeStatement.setString(4, image);
        subTypeStatement.setInt(5, roomType);
        subTypeStatement.setString(6, subname);
        subTypeStatement.setInt(7, subtypeId);
        subTypeStatement.executeUpdate();

        amenityDeleTionStatement.setInt(1, subtypeId);
        amenityDeleTionStatement.executeUpdate();

        for (int amenityId : amenityList) {
            amenityInsertionStatement.setInt(1, amenityId);
            amenityInsertionStatement.setInt(2, subtypeId);
            amenityInsertionStatement.addBatch();
        }
        amenityInsertionStatement.executeBatch();
    }

    @Override
    public void updateAmenitity(int id, String name, String language) throws SQLException {
        PreparedStatement amenityStatement = connection.prepareStatement(UPDATE_AMENITY);
        amenityStatement.setString(1, language);
        amenityStatement.setString(2, name);
        amenityStatement.setInt(3, id);
        amenityStatement.executeUpdate();
    }

    @Override
    public void deleteAmenity(int amenityId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_AMENITY);
        preparedStatement.setInt(1, amenityId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void createRoom(int roomNum, int subtypeId, int windowViewID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ROOM);
        preparedStatement.setInt(1, roomNum);
        preparedStatement.setInt(2, subtypeId);
        preparedStatement.setInt(3, windowViewID);
        preparedStatement.executeUpdate();
    }

    @Override
    public int createAmenity(String nameEN, String nameRU, String nameUA) throws SQLException {

        PreparedStatement amenityStatement = connection.prepareStatement(CREATE_AMENITY, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement translationStatement = connection.prepareStatement(CREATE_AMENITY_TRANSLATIONS);

        amenityStatement.executeUpdate();

        int amenityId = 0;
        ResultSet generatedKeys = amenityStatement.getGeneratedKeys();
        if (generatedKeys.next()) amenityId = generatedKeys.getInt(1);

        translationStatement.setInt(1, amenityId);
        translationStatement.setString(2, "en");
        translationStatement.setString(3, nameEN);
        translationStatement.addBatch();

        translationStatement.setInt(1, amenityId);
        translationStatement.setString(2, "ru");
        translationStatement.setString(3, nameRU);
        translationStatement.addBatch();

        translationStatement.setInt(1, amenityId);
        translationStatement.setString(2, "uk_UA");
        translationStatement.setString(3, nameUA);
        translationStatement.addBatch();

        translationStatement.executeBatch();

        return amenityId;
    }

    @Override
    public int createSubType(int price, int capacity, String img, int roomtypeId, String nameEN, String nameRU, String nameUA, List<Integer> amenityList) throws SQLException {

        PreparedStatement subtypeStatement = connection.prepareStatement(CREATE_SUBTYPE, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement translationStatement = connection.prepareStatement(CREATE_SUBTYPE_TRANSLATION);
        PreparedStatement amenityInsertionStatement = connection.prepareStatement(INSERT_AMENITIES);
        subtypeStatement.setInt(1, price);
        subtypeStatement.setInt(2, capacity);
        subtypeStatement.setString(3, img);
        subtypeStatement.setInt(4, roomtypeId);

        subtypeStatement.executeUpdate();

        int subtypeId = 0;
        ResultSet generatedKeys = subtypeStatement.getGeneratedKeys();
        if (generatedKeys.next()) subtypeId = generatedKeys.getInt(1);

        translationStatement.setInt(1, subtypeId);
        translationStatement.setString(2, "en");
        translationStatement.setString(3, nameEN);
        translationStatement.addBatch();

        translationStatement.setInt(1, subtypeId);
        translationStatement.setString(2, "ru");
        translationStatement.setString(3, nameRU);
        translationStatement.addBatch();

        translationStatement.setInt(1, subtypeId);
        translationStatement.setString(2, "uk_UA");
        translationStatement.setString(3, nameUA);
        translationStatement.addBatch();

        translationStatement.executeBatch();

        for (int amenityId : amenityList) {
            amenityInsertionStatement.setInt(1, amenityId);
            amenityInsertionStatement.setInt(2, subtypeId);
            amenityInsertionStatement.addBatch();
        }
        amenityInsertionStatement.executeBatch();

        return subtypeId;
    }

    @Override
    public void deleteAllAmenities() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM hoteltest.amenities;");
        PreparedStatement preparedStatementTwo = connection.prepareStatement("DELETE FROM hoteltest.amenity_translations;");
        preparedStatement.executeUpdate();
        preparedStatementTwo.executeUpdate();
    }

    @Override
    public void deleteAllSubtypes() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM hoteltest.room_subtypes;");
        PreparedStatement preparedStatementTwo = connection.prepareStatement("DELETE FROM hoteltest.room_subtype_translations;");
        preparedStatement.executeUpdate();
        preparedStatementTwo.executeUpdate();
    }

    @Override
    public void deleteAllApplications() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM hoteltest.user_applications;");
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteAllRooms() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM hoteltest.rooms;");
        preparedStatement.executeUpdate();
    }
}