package com.cboxgames.idonia.backend.commons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class DatabaseTest {

	private DataSource _data_source;
	
	public DatabaseTest(DataSource data_source) {
		_data_source = data_source;
	}
	
	public List<String> getUsers() {
		List<String> users = new ArrayList<String>();
		
		Connection conn = null;
		
		try {
			conn = _data_source.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM users");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				users.add(result.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return users;
	}
	
	public String getUser(String name) {
		
		Connection conn = null;
		
		try {
			conn = _data_source.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
			statement.setString(1, name);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return result.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return "";
	}
}
