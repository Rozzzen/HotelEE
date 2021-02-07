package filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;

@WebFilter(filterName = "LanguageFilter", urlPatterns = { "/*" })
public class LanguageFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(LanguageFilter.class);


    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;

        if (request.getParameter("language") != null) {
            request.getSession().setAttribute("language", request.getParameter("language"));
            LOG.debug("Language has been changed to[" + request.getParameter("language") + "]");
        }
        else if(request.getSession().getAttribute("language") == null) {
            request.getSession().setAttribute("language", "en");
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
