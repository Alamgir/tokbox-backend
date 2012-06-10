package com.tokbox.listener;

import com.tokbox.service.TokBoxOAuth;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/9/12
 * Time: 10:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OAuthListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        TokBoxOAuth.init();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
