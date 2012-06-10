package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.db.tutorial.TutorialDBSQL;

/**
 * Handle "/tutorials/details"
 * HTTP Method: GET
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class TutorialHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private TutorialDBSQL _db_sql;
	private ObjectMapper _mapper;
	
    @Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        
        try {
            _db_sql = new TutorialDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        }
        catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
 
    	String uri_str = request.getRequestURI();
       	if (UriToArgv.verifyUrl(uri_str, "tutorials", "details") == false) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
        ResponseTools.prepareResponseJson(response, _mapper, _db_sql.getTutorialDetails(), Constants.SC_OK);
    }
}