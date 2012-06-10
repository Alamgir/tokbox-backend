package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.db.itemset.ItemSetDBSQL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 1/21/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class ItemSetHttpServlet extends HttpServlet{

    private SqlDataSource _sql_data_source;
    private ItemSetDBSQL _is_db_sql;
    private ObjectMapper _mapper;

    public void init() {

        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        
        try {
            _is_db_sql = new ItemSetDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

    	// URI: /item_sets/details
    	String uri_str = request.getRequestURI();
       	if (UriToArgv.verifyUrl(uri_str, "item_sets", "details") == false) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
        ResponseTools.prepareResponseJson(response, _mapper, _is_db_sql.getItemSetDetails(),
                                          Constants.SC_OK);
    }
}
