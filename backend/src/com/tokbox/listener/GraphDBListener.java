package com.tokbox.listener;

import com.tokbox.graphdb.TokBoxDB;
import com.tokbox.types.Entity;
import com.tokbox.types.User;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.neo4j.graphdb.Node;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
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
//        TokBoxDB.init();
//        User user = new User();
//        user.display_name = "Alamgir Mand";
//        user.referral_link = "blaaklds";
//        user.country = "US";

//        HashMap<String, Long> quota_info = new HashMap<String, Long>();
//        quota_info.put("available", new Long(213222231));
//        user.quota_info = quota_info;

        //user.email = "alamgir@berkeley.edu";
        //TokBoxDB.createUser(user);
        //TokBoxDB.getUser("Alamgir Mand");
        //TokBoxDB.deleteUser("Alamgir Mand");
        //TokBoxDB.getUser(123456789);
        try {
            ObjectMapper _mapper = new ObjectMapper();
            _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
            _mapper.getDeserializationConfig().addMixInAnnotations(Entity.class, Entity.EntityFilter.class);

            URL url = new URL("http://www.meethue.com/api/nupnp");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            //connection.connect();

            InputStream url_stream = connection.getInputStream();
            @SuppressWarnings("unchecked")
            ArrayList<Map<String, Object>> data = _mapper.readValue(url_stream, ArrayList.class);


            Map<String, Object> data_map = data.get(0);
            String bridge_ip = (String)data_map.get("internalipaddress");


            //GET HUE data from Bridge
            URL api_url = new URL("http://" + bridge_ip + "/api/newdeveloper");
            HttpURLConnection api_connection = (HttpURLConnection) url.openConnection();
            api_connection.setRequestMethod("GET");
            //api_connection.setDoOutput(true);
            api_connection.connect();

            InputStream api_stream = api_connection.getInputStream();
            @SuppressWarnings("unchecked")
            Map<String, Object>[] api_data = _mapper.readValue(api_stream, Map[].class);



//            if ((String)data[2].get("macaddress") != null) {
//                String bridge_mac = (String)data[2].get("macaddress");
//            }
        } catch (JsonMappingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
