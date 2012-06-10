package com.cboxgames.idonia.backend;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.utils.idonia.types.UserBanner.UserBannerWrapper;
import com.cboxgames.utils.json.JsonConverter;

/**
 * Handle "/user_banners/details"
 * HTTP Method: GET
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class UserBannerHttpServlet extends HttpServlet {
	
	private SqlDataSource _idata_source;
	private JsonConverter _json_converter;
	
	@Override
	public void init() {
        _idata_source = new SqlDataSource();
        _json_converter = JsonConverter.getInstance();
	}
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
       	
     	response.setContentType("application/json");
       	PrintWriter out = response.getWriter();

       	//TODO: DESABLED BECAUSE IT WILL DESTROY US.
       	UserBannerWrapper[] usr_banner_array = null;
       	if (usr_banner_array == null) {
           	out.print("[]");
       	} else {
           	String json = _json_converter.getJson(usr_banner_array);
           	out.print(json);
       	}
    }
    
    private UserBannerWrapper[] get_user_banners() {
		
		Connection conn = null;
		List<UserBannerWrapper> user_banner_list = new ArrayList<UserBannerWrapper>();
		UserBannerWrapper[] user_banner_array = null;
	
		try {
			conn = _idata_source.get_data_source().getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM user_banners");
			ResultSet result = statement.executeQuery();
			if (result != null) {
				while (result.next()) {
					UserBannerWrapper ubw = new UserBannerWrapper();
//					UserBanner ub = ubw.user_banner;
//					ub.node_id = result.getInt("node_id");
//					ub.id = result.getInt("id");
//					ub.user_id = result.getInt("user_id");
//					ub.complete = result.getBoolean("complete");
					user_banner_list.add(ubw);
				}
				
				if (!user_banner_list.isEmpty()) {
					user_banner_array = (UserBannerWrapper[]) user_banner_list.toArray(new UserBannerWrapper[0]);
				}
			}
		} catch (Exception e) {
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
		
		return user_banner_array;
	}
}