import org.junit.jupiter.api.Test;
import servlet.admin.CreateRoomServlet;
import servlet.admin.EditRoomServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminCreationTest {

    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

    @Test
    public void CreateAndDeleteRoomTest() throws ServletException, IOException {
        CreateRoomServlet createRoomServlet = new CreateRoomServlet();
        EditRoomServlet editRoomServlet = new EditRoomServlet();

        when(httpServletRequest.getParameter("roomNum")).thenReturn("999");
        when(httpServletRequest.getParameter("message")).thenReturn("Remove");
        when(httpServletRequest.getParameter("subtypeId")).thenReturn("5");
        when(httpServletRequest.getParameter("windowViewId")).thenReturn("1");

        createRoomServlet.doPost(httpServletRequest, httpServletResponse);
        editRoomServlet.doPost(httpServletRequest, httpServletResponse);
    }
}
