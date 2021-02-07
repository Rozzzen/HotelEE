package filter;

import org.apache.log4j.Logger;
import pojo.User;
import pojo.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter")
public class AdminFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AdminFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if(request.getSession().getAttribute("user") == null) {
            LOG.debug("Denied access for guest");
            request.getSession().setAttribute("alert", "You need to login to access this page!");
            response.sendRedirect("/login");
        }
        else {
            User user = (User) request.getSession().getAttribute("user");
            if(!user.getRole().equals(UserRole.ADMIN)) {
                LOG.debug("Denied access to admin page");
                request.getSession().setAttribute("alert", "You dont own rights to enter this page");
                response.sendRedirect("/");
            }
            else {
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                response.setHeader("Expires", "0"); // Proxies.
                chain.doFilter(req, resp);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
