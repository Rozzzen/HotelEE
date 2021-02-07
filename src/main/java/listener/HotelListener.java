package listener;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener()
public class HotelListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    private static final Logger LOG = Logger.getLogger(HotelListener.class);

    // Public constructor is required by servlet spec
    public HotelListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed).
         You can initialize servlet context related data here.
      */
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context
         (the Web application) is undeployed or
         Application Server shuts down.
      */
    }

    public void sessionCreated(HttpSessionEvent se) {

    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
        if (sbe.getSession().getAttribute("user") != null
                && sbe.getSession().getAttribute("alert") == null
                && sbe.getSession().getAttribute("success") == null) {
            if (sbe.getSession().getAttribute("language").equals("uk_UA")) {
                sbe.getSession().setAttribute("success", "Ласкаво просимо!");
            }
            else if (sbe.getSession().getAttribute("language").equals("ru")) {
                sbe.getSession().setAttribute("success", "Добро пожаловать!");
            } else {
                sbe.getSession().setAttribute("success", "Welcome!");
            }
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
    }
}
