package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.db.accessory.AccessoryDBSQL;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.Accessory.*;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Handle "/accessories/details"
 * HTTP Method: GET
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class AccessoryHttpServlet extends HttpServlet {
    
	private SqlDataSource _sql_data_source;
	private ObjectMapper _mapper;
	private AccessoryDBSQL _a_db_sql;
	
	
    @Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().addMixInAnnotations(Accessory.class, AccessoryFilter.class);
        
        try {
            //Omit the character_id field for character details using a Mix-In annotation.
            _a_db_sql = new AccessoryDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        }
        catch (SQLException e) {
        	e.printStackTrace();
        }
	}
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

       	String uri_str = request.getRequestURI();
       	if (UriToArgv.verifyUrl(uri_str, "accessories", "details") == false) {
       		ResponseTools.prepareResponseJson(response, _mapper,
       				uri_str + " is not supported by " + this.getServletName(), Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
        ResponseTools.prepareResponseJson(response, _mapper, _a_db_sql.getAccessoryDetails(),
        		Constants.SC_OK);
    }
}