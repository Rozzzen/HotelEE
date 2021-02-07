package dao.Impl;

import dao.UserDao;
import pojo.Gender;
import pojo.User;
import pojo.UserRole;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class UserDaoImpl implements UserDao{

    private static UserDaoImpl instance;
    private Connection connection;

    public static UserDaoImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }

    private UserDaoImpl() {
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

    private final String SELECT_GENDERS = "SELECT * FROM genders;";
    private final String INSERT_USER = "INSERT INTO users(role_id, gender_id, name, surname, email, phone, password, birthDate)\n" +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
    private final String LOGIN_VALIDATION = "SELECT id FROM users WHERE email = ? AND password = ?;";
    private final String SELECT_USER_BY_ID = "SELECT role_id, gender_id, name, surname, email, phone, birthDate FROM users WHERE id = ?";

    public List<Gender> getGenderList() throws SQLException {
        List<Gender> result = new LinkedList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GENDERS);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String genderName = resultSet.getString("gender_name");
            String appeal = resultSet.getString("appeal");
            Gender theme = new Gender();
            theme.setId(id); //id, genderName, appeal
            theme.setName(genderName);
            theme.setAppeal(appeal);
            result.add(theme);
        }
        return result;
    }

    @Override
    public int registerUser(User user) throws SQLException {
        int result;
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
        preparedStatement.setInt(1, user.getRole().getId());
        preparedStatement.setInt(2, user.getGender().getId());
        preparedStatement.setString(3, user.getName());
        preparedStatement.setString(4, user.getSurname());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getPhone());
        preparedStatement.setString(7, user.getPassword());
        preparedStatement.setObject(8, user.getBdate());
        result = preparedStatement.executeUpdate();

        return result;
    }

    @Override
    public int loginUserValidation(String email, String password) throws SQLException {

        int id = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_VALIDATION);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) id = resultSet.getInt("id");
        return id;
    }

    @Override
    public User selectUserByID(int id) throws SQLException {

        User user = null;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            UserRole userRole = UserRole.USER;
            switch (resultSet.getInt("role_id")) {
                case 1:
                    userRole = UserRole.USER;
                    break;
                case 2:
                    userRole = UserRole.ADMIN;
                    break;
            }
            Gender gender = new Gender();
            gender.setId(resultSet.getInt("gender_id")); //resultSet.getInt("gender_id")
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");
            Date date = (Date) resultSet.getObject("birthDate");
            LocalDate localDate = date.toLocalDate();
            user = new User(); //id, name, surname, gender, email, phone, localDate, userRole
            user.setId(id);
            user.setName(name);
            user.setSurname(surname);
            user.setGender(gender);
            user.setEmail(email);
            user.setPhone(phone);
            user.setBdate(localDate);
            user.setRole(userRole);
        }

        return user;
    }
}
