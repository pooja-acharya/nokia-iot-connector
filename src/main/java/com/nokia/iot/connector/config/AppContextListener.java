package com.nokia.iot.connector.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();
        sc.setAttribute("ctx", sc.getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	
    }

}
