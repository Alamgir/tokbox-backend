package com.tokbox.listener;

import com.tokbox.graphdb.TokBoxDB;
import com.tokbox.types.User;
import org.neo4j.graphdb.Node;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 7/7/12
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphDBListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        TokBoxDB.init();
        User user = new User();
        user.display_name = "Alamgir Mand";
        user.referral_link = "blaaklds";
        user.country = "US";

//        HashMap<String, Long> quota_info = new HashMap<String, Long>();
//        quota_info.put("available", new Long(213222231));
//        user.quota_info = quota_info;

        user.email = "alamgir@berkeley.edu";
        //TokBoxDB.createUser(user);
        //TokBoxDB.getUser("Alamgir Mand");
        //TokBoxDB.deleteUser("Alamgir Mand");
        TokBoxDB.getUser("Alamgir Mand");


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
