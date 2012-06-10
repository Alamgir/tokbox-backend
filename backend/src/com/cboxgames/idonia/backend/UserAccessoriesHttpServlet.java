package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.Utility;
import com.cboxgames.idonia.backend.commons.authentication.AuthenticateUser;
import com.cboxgames.idonia.backend.commons.accessorygenerator.AccessoryGenerator;
import com.cboxgames.idonia.backend.commons.db.accessory.UserAccessoriesShopDBSQL;
import com.cboxgames.idonia.backend.commons.db.accessory.UserAccessoriesShopDBSQL.ResponseGetUserAccessories;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;

import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.Character;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * 
 * Handle for "/user_accessories/shop"
 * 
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/6/11
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserAccessoriesHttpServlet extends HttpServlet {

    private SqlDataSource _sql_data_source;
    private UserAccessoriesShopDBSQL _user_acc_db_sql;
    private UserCharacterDBSQL db_user_characters;
    private ObjectMapper _mapper;

    @Override
    public void init() {
    	
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        
        try {
            _user_acc_db_sql = new UserAccessoriesShopDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            db_user_characters = new UserCharacterDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	if (UriToArgv.verifyUrl(uri_str, "user_accessories", "shop") == false) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
    	if (AuthenticateUser.isAuthenticated(request) == false) {
    		ResponseTools.prepareResponseJson(response, _mapper, "User is not authenticated", HttpServletResponse.SC_UNAUTHORIZED);
    		return;
    	}
    	
    	HttpSession session = request.getSession();
    	int user_id = (Integer) session.getAttribute("user_id");
    	boolean success = true;
    	
		// Generate accessories for the user if no accessories were found or the found accessories were too old.
    	ResponseGetUserAccessories result = _user_acc_db_sql.getShopAccessories(user_id);
    	if ((result.accessories.size() <= 0) || Utility.isOldData(result.updated_at)) {
    		List<Character> characters = db_user_characters.getUserCharactersForShop(user_id);
    		int n_chars = characters.size();
    		Random rand = new Random();
    		List<Accessory> accessories = result.accessories;
    		accessories.clear();
    		for (int ctr = 0; ctr < 10; ctr++) {
    			int ran_no = rand.nextInt(n_chars);
    			accessories.add(AccessoryGenerator.generateAccessoryForCharacter(
    												characters.get(ran_no).name,
    												characters.get(ran_no).level,
    												AccessoryGenerator.RANDOM));
    		}
    		
    		// Write the newly generated accessories to the database.
    		success = _user_acc_db_sql.saveShopAccessories(user_id, result.accessories);
    	}
    	if (success)
    		ResponseTools.prepareResponseJson(response, _mapper, result.accessories, Constants.SC_OK);
    	else
    		response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
    }
}
