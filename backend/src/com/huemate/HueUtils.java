package com.huemate;

import com.tokbox.types.Entity;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/15/13
 * Time: 2:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class HueUtils {
    public static String bridge_url;
    /**
     * Enter a URL, request_method, and data
     * @param request_url this is the url required for the request
     * @param data post the object that you want to serialize here
     * @return a success or failure
     */
//    public static boolean post(String request_url, String data) throws IOException {
//        URL url = new URL(request_url);
//        HttpURLConnection api_connection = (HttpURLConnection) url.openConnection();
//        api_connection.setDoOutput(true);
//        api_connection.setRequestProperty("Accept-Charset", charset);
//
//
//
//        OutputStreamWriter output = new OutputStreamWriter(api_connection.getOutputStream());
//        output.write(data);
//        output.flush();
//        output.close();
//
//        InputStream stream = api_connection.getInputStream();
//        @SuppressWarnings("unchecked")
//        Map<String, Object>
//
//
//    }
//
//    public static boolean put(String request_url, Object data) {
//
//    }
//
//    public static Map<String,Object>[] get(String request_url, Object data) {
//
//    }
//
//    public static boolean delete(String request_url, Object data) {
//
//    }
}
