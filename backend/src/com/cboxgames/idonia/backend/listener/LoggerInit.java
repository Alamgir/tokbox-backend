package com.cboxgames.idonia.backend.listener;

import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog;
import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 1/9/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoggerInit implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        CBoxLoggerSyslog.initializeLogger("107.22.164.151",80,CBoxLoggerSyslog.SYSLOG_PROTOCOL,"idonia_backend");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
