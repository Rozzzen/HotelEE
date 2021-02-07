package dao;

import pojo.Gender;
import pojo.User;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface UserDao {
    public List<Gender> getGenderList() throws SQLException;
    public int registerUser(User user) throws SQLException;
    public int loginUserValidation(String email, String password) throws SQLException;
    public User selectUserByID(int id) throws SQLException;
}
