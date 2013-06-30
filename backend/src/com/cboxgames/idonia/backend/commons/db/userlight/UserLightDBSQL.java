package com.cboxgames.idonia.backend.commons.db.userlight;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.huemate.HueLight;
import com.huemate.HueUser;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/15/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserLightDBSQL extends DBSQL{
    public UserLightDBSQL(DataSource connection_pool, ServletContext servlet_context)
            throws SQLException {
        super(connection_pool, servlet_context);
    }

    public static void getUserLights(HueUser user, Connection conn) throws SQLException {

        for (HueLight light : user.lights) {

        }

    }

//    private static void getNodeFromResult(ResultSet result, Node node) throws SQLException {
//
//        node.id = result.getInt("id");
//        node.name = result.getString("name");
//        node.description = result.getString("description");
//        node.node_type = NodeType.nameFromId(result.getInt("node_type"));
//        node.num_waves = result.getInt("num_waves");
//        node.child_nodes = result.getString("child_nodes");
//        node.mob_id = result.getString("mob_id");
//        node.region = result.getInt("region");
//        node.min_lvl = result.getInt("min_lvl");
//        node.max_lvl = result.getInt("max_lvl");
//    }


}
