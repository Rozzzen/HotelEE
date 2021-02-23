import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import pojo.Gender;
import pojo.User;
import pojo.UserRole;
import servlet.LoginServlet;
import servlet.RegisterServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationTest {

    @Test
    public void SuccessLoginTest() throws ServletException, IOException {

        Gender gender = new Gender();
        gender.setId(1);
        User expectedUser = new User();
        expectedUser.setRole(UserRole.ADMIN);
        expectedUser.setId(62);
        expectedUser.setGender(gender);
        expectedUser.setName("test2");
        expectedUser.setSurname("test2");
        expectedUser.setEmail("test2@gmail.com");
        expectedUser.setPhone("test2");
        expectedUser.setPassword("test2");
        expectedUser.setBdate(LocalDate.parse("2021-02-17"));

        LoginServlet loginServlet = new LoginServlet();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);

        final User[] actual = new User[1];

        Mockito.doAnswer((Answer<Void>) invocation -> {
            actual[0] = (User) invocation.getArgument(1, Object.class);
            return null;
        }).when(httpSession).setAttribute(anyString(), Mockito.any());

        when(httpServletRequest.getParameter("email")).thenReturn("test2@gmail.com");
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getParameter("password")).thenReturn("test2");

        loginServlet.doPost(httpServletRequest, httpServletResponse);

        Assert.assertEquals(actual[0].getId(), expectedUser.getId());
        Assert.assertEquals(actual[0].getName(), expectedUser.getName());
        Assert.assertEquals(actual[0].getSurname(), expectedUser.getSurname());
    }

    @Test
    public void LoginErrorTest() throws ServletException, IOException {
        LoginServlet loginServlet = new LoginServlet();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);

        Mockito.doAnswer((Answer<Void>) invocation -> null).when(httpSession).setAttribute(anyString(), Mockito.any());

        when(httpServletRequest.getParameter("email")).thenReturn("wrong email");
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getParameter("password")).thenReturn("admin");

        loginServlet.doPost(httpServletRequest, httpServletResponse);
    }

    @Test
    public void ShouldThrowDateValidationExceptionRegisterTest() throws ServletException, IOException {
        RegisterServlet registerServlet = new RegisterServlet();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);

        when(httpServletRequest.getSession()).thenReturn(httpSession);

        when(httpServletRequest.getParameter("name")).thenReturn(anyString());
        when(httpServletRequest.getParameter("surname")).thenReturn(anyString());
        when(httpServletRequest.getParameter("email")).thenReturn(anyString());
        when(httpServletRequest.getParameter("phone")).thenReturn(anyString());
        when(httpServletRequest.getParameter("password")).thenReturn(anyString());
        when(httpServletRequest.getParameter("day")).thenReturn("11");
        when(httpServletRequest.getParameter("month")).thenReturn("30");
        when(httpServletRequest.getParameter("year")).thenReturn("2001");
        when(httpServletRequest.getParameter("gender")).thenReturn("1");

        registerServlet.doPost(httpServletRequest, httpServletResponse);
    }

    @Test
    public void ShouldThrowSQLExceptionRegisterTest() throws ServletException, IOException {
        RegisterServlet registerServlet = new RegisterServlet();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);

        when(httpServletRequest.getSession()).thenReturn(httpSession);

        when(httpServletRequest.getParameter("name")).thenReturn("test");
        when(httpServletRequest.getParameter("surname")).thenReturn("test");
        when(httpServletRequest.getParameter("email")).thenReturn("admin1@gmail.com");
        when(httpServletRequest.getParameter("phone")).thenReturn("test");
        when(httpServletRequest.getParameter("password")).thenReturn("test");
        when(httpServletRequest.getParameter("day")).thenReturn("20");
        when(httpServletRequest.getParameter("month")).thenReturn("6");
        when(httpServletRequest.getParameter("year")).thenReturn("2001");
        when(httpServletRequest.getParameter("gender")).thenReturn("1");

        registerServlet.doPost(httpServletRequest, httpServletResponse);
    }
}
