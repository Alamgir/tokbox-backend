package com.cboxgames.idonia.backend.commons;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * @author Michael
 *
 */
public class SqlDataSource {

	private DataSource _data_source;
	public DataSource get_data_source() { return _data_source; }
	
	public SqlDataSource() {
		try {
            // Get DataSource 
            Context init_context  = new InitialContext();
            Context env_context  = (Context)init_context.lookup("java:comp/env"); // or java:/comp/env
            _data_source = (DataSource) env_context.lookup("jdbc/db");
        } catch (NamingException e) { 
            e.printStackTrace(); 
        } 
	}
}