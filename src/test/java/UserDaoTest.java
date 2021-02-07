import dao.Impl.UserDaoImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import pojo.Gender;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDaoTest {

    @Test
    public void GenderListTest() throws SQLException {
        UserDaoImpl userDao = UserDaoImpl.getInstance();

        List<Gender> genderList = userDao.getGenderList();

        List<String> actual = new ArrayList<>();
        List<String> expected = Arrays.asList("Male", "Female");

        genderList.forEach(gender -> actual.add(gender.getName()));

        Assert.assertEquals(actual, expected);
    }
}
