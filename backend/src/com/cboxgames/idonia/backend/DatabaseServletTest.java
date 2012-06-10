package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DatabaseTest;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;

@SuppressWarnings("serial")
public class DatabaseServletTest extends HttpServlet {

	@SuppressWarnings("unused")
	private DatabaseTest _idata_source;
    private UserDBSQL _user_db_sql;
    private SqlDataSource _sql_data_source;
	
	@Override
	public void init() {
		try {
            // Get DataSource 
            Context init_context  = new InitialContext();
            Context env_context  = (Context)init_context.lookup("java:comp/env"); // or java:/comp/env
            DataSource source = (DataSource)env_context.lookup("jdbc/db");
            _idata_source = new DatabaseTest(source);
            _sql_data_source = new SqlDataSource();
            _user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (NamingException e) { 
            e.printStackTrace(); 
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		for (int x=0;x<100;x++) {
            String username = UUID.randomUUID().toString();
		    String password = "password";
            String device_token = null;
            String udid = "fakeUDID123211";
            String mac_address = "fakeMAC313121";
            _user_db_sql.createUser(username, password, device_token, udid, mac_address);
        }
        resp.setStatus(200);

	}
}
