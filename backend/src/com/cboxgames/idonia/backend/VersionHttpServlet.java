package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.cboxgames.utils.idonia.types.Version.VersionWrapper;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.db.versions.VersionDBSQL;

/**
 * Handle "/versions/details"
 * HTTP Method: GET
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class VersionHttpServlet extends HttpServlet {
	
	private SqlDataSource _idata_source;
	private ObjectMapper _mapper;
	private VersionDBSQL version_db;
	
	@Override
	public void init() {
		
        _idata_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        
        try {
			version_db = new VersionDBSQL(_idata_source.get_data_source(), getServletContext());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
     
       	List<VersionWrapper> version_array = version_db.getCurrentVersions();
       	ResponseTools.prepareResponseJson(response, _mapper, version_array, HttpServletResponse.SC_OK);
    }
}