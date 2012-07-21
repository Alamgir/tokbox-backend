package com.tokbox.types;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/10/12
 * Time: 9:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Entity {
    public String size;
    public String rev;
    public String hash;
    public boolean thumb_exists;
    public long bytes;
    public Date modified;
    public String client_mtime;
    public String path;
    public String name;
    public String parent_dir;
    public boolean is_dir;
    public String icon;
    public String root; 
    public String mime_type;
    public Entity[] contents;

    public void setModified(String modified_string) {
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        try {
            modified = format.parse(modified_string);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    public void setPath(String path) {
        ArrayList<String> path_array = new ArrayList<String>(Arrays.asList(path.split("/")));
        //isolate the name
        name = path_array.get(path_array.size());
        //remove the name
        path_array.remove(path_array.get(path_array.size()));

        //rebuild the parent path
        StringBuilder path_builder = new StringBuilder();
        for (String string : path_array) {
            path_builder.append(string);
            path_builder.append("/");
        }
        parent_dir = path_builder.toString();
        
    }
    
    @JsonIgnoreProperties({"revision"})
    public static interface EntityFilter {
    }
}
