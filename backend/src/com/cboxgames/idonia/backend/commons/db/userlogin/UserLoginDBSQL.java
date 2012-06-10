package com.cboxgames.idonia.backend.commons.db.userlogin;

import com.cboxgames.utils.idonia.types.UserLogin;
import com.cboxgames.idonia.backend.commons.DBSQL;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 11/29/11
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserLoginDBSQL extends DBSQL implements IUserLoginDB {

    public UserLoginDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
		super(data_source, servlet_context);
	}
    
    public UserLogin getUserCred(String username) {
        Connection conn = null;
        UserLogin user_login = null;
        try {
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("SELECT id,username,password FROM USER WHERE username = ?");
            query.setString(1, username);
            ResultSet results = query.executeQuery();
            if (!results.wasNull()) {
            	user_login = new UserLogin();
                user_login.username = results.getString("username");
                user_login.hashed_password = results.getString("password");
                user_login.id = results.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return user_login;
    }
}
