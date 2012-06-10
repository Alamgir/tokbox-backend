package com.cboxgames.idonia.backend.listener;

import javax.servlet.*;

/**
 * Listener for reading in all cbox static tables when Tomcat server starts up.
 * Author: Michael Chang
 */

import com.cboxgames.idonia.backend.commons.DefaultValueTable;
import com.cboxgames.utils.json.JsonConverter;

public class DefaultValueTableInitListener implements ServletContextListener {

	private ServletContext _context = null;
	private JsonConverter _json_converter;
	private DefaultValueTable _cbox_static_table;

	/* This method is invoked when the Web Application has been removed 
	 * and is no longer able to accept requests
	 */
	public void contextDestroyed(ServletContextEvent event) {
		
	    // Output a simple message to the server's console
	    // System.out.println("The Simple Web App. Has Been Removed");
	   	_context = null;

	}

	// This method is invoked when the Web Application
	// is ready to service requests
	public void contextInitialized(ServletContextEvent event) {
		
		_context = event.getServletContext();
		_json_converter = JsonConverter.getInstance();
        _cbox_static_table = new DefaultValueTable(_json_converter, _context);
        _cbox_static_table.createAllDefaultValueTable();

		//Output a simple message to the server's console
		System.out.println("All static tables have been loaded");
	}
}
