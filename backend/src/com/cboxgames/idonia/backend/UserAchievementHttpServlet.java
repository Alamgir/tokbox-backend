package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.cboxgames.utils.idonia.types.UserAchievement.UserAchievementWrapper;
import com.cboxgames.utils.json.JsonConverter;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.db.achievement.UserAchievementDBSQL;

import static com.cboxgames.idonia.backend.UserAchievementHttpServlet.RequestType.*;

/**
 * Handle "/user_achievements and /user_achievements/*"
 * HTTP Method: GET/POST/PUT/DELETE
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class UserAchievementHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private JsonConverter _json_converter;
	private UserAchievementDBSQL _user_db_sql;
	private ObjectMapper _mapper;
	
	@Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();     
        _json_converter = JsonConverter.getInstance();
        _mapper = _json_converter.getMapper();
        try {
        	_user_db_sql = new UserAchievementDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	System.out.println("URI: " + uri_str);
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_achievements");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setGetRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UACT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
     	response.setContentType("application/json");
       	PrintWriter out = response.getWriter();
       	UserAchievementWrapper[] userarray = null;
       	
       	switch (req_type) {
       		case UACT_DETAILS: {
       	       	//TODO: DESABLED BECAUSE IT WILL DESTROY US.
       	       	//userarray = _user_db_sql.getUserAchievementDetails();
       	       	break;
       		}
       		case UACT_UID_DETAILS: {
       		  	//userarray = _user_db_sql.getUserAchievementDetails(user_id);
       		  	break;
       		}
       		default: { // internal error
       			break;
       		}
       	}
       	
       	if (userarray == null) {
           	out.print("[]");
       	} else {
       	   	String json = _json_converter.getJson(userarray);
           	out.print(json);
       	}
    }
    
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	System.out.println("URI: " + uri_str);
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_achievements");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPutRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UACT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
       	try {
	       	switch (req_type) {
	       		case UACT_COMPLETE: {
	       			break;
	       		}
	       		case UACT_AID_COMPLETE: {
	       			int achievement_id = ret.getAchievementId();
//	       			if (_user_db_sql.userAchievementComplete(user_id, achievement_id)) {
//	       				response.setStatus(Constants.SC_OK);
//	       			}
//	       			else {
//	       				throw new Exception("Unable to update a new user_achievement with user_id = " + user_id +
//	       						" and achievement_id = " + achievement_id);
//	       			}
	       			break;
	       		}	
	    		default: { // Ineternal error
	    			break;
	    		}
	       	}
      	} catch (Exception e) {
       		e. printStackTrace();
       	} 
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	System.out.println("URI: " + uri_str);
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_achievements");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPostRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	// int user_id = ret.getUserId();
       	
       	if (req_type == UACT_UNDEFINED) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	ServletInputStream stream = request.getInputStream();
       	try {
	       	switch (req_type) {
	       		case UACT_CREATE: {
	       			UserAchievementWrapper unw = _json_converter.getObject(stream, UserAchievementWrapper.class);
	       			
//	       			if (_user_db_sql.userAchievementExist(unw.user_achievement.character_id)) {
//	       	      		response.sendError(Constants.SC_CONFLICT, "User achievement with user id = " + unw.user_achievement.character_id + " already exists");
//	       	      		return;
//	       			}
//
//	       			if (_user_db_sql.createUserAchievements(unw.user_achievement.character_id) == true) {
//	       				response.setStatus(Constants.SC_OK);
//	       			} else {
//	       				throw new Exception("Unable to create a new user_achievement");
//	       			}
	       			break;
	       		}
	       		default: { // internal error
	       			throw new Exception("Undefined request type " + req_type.name() + " when posting a user_achievement");
	       		}
	       	}
       	} catch (Exception e) {
       		e. printStackTrace();
       	} finally {
       		if (stream != null) {
       			stream.close();
       		}
       	}
    }
    
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	System.out.println("URI: " + uri_str);
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_achievements");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setDeleteRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UACT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
   
       	try {
	       	switch (req_type) {
		   		case UACT_DESTROY: {
//		   			if (_user_db_sql.deleteUserAchievement(user_id) == true) {
//	       				response.setStatus(Constants.SC_OK);
//	       			} else {
//	       				throw new Exception("Unable to delete a new user_achievement with user_id = " + user_id);
//	       			}
		   			break;
		   		}
		   		default: { // internal error
		   			break;
		   		}
		   	}
    	} catch (Exception e) {
       		e. printStackTrace();
       	}
    }
	
    public static enum RequestType {
    	
    	UACT_DETAILS, // GET details for all user_achievements < /user_achievements/details >
    	UACT_UID_DETAILS, // GET details for a given user_id < /user_achievements/{user_id}/details >
    	
    	UACT_COMPLETE, // PUT (update) < /user_achievements/{user_id}/complete >
    	UACT_AID_COMPLETE, // PUT (update) < /user_achievements/{user_id}/{achievement_id} >
    	
    	UACT_CREATE, // POST (create) < /user_achievements/create
    	
    	UACT_DESTROY, // DELTE < /user_achievements/{user_id}/destroy >
    	
    	UACT_UNDEFINED;
    	
    	public static RequestTypeUserId setGetRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, 0, UACT_UNDEFINED);
      		
    		try {
    			int indx = uta.getBaseIndex() + 1;
    			String[] argv = uta.getArgv();
    			
    	       	if (indx >= argv.length) {
    	       		rt.setRequestType(UACT_DETAILS);
    	       		return rt;  // GET (all user_achievements)
    	       	} 
    	       	if (argv[indx].equals("details")) {
    	       		rt.setRequestType(UACT_DETAILS);
    	       		return rt; // GET (all user_achievements)
    	       	} 

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
    	       		rt.setRequestType(UACT_UID_DETAILS);
           			return rt; // GET 1 user
           		}
           		if (argv[indx].equals("details")) {
           			rt.setRequestType(UACT_UID_DETAILS);
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
        	
        	return rt;
        }
    	
    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, 0, UACT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

 	       	if (indx >= argv.length) {
	       		return rt;
	       	}
 	       	
        	try {
        		
           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
           			return rt;
    	       	}
           		if (argv[indx].equals("complete")) {
    	   			rt.setRequestType(UACT_COMPLETE); // PUT 1 user
    	   			return rt;
    	    	}
           		
          		// if successfully reading argv[idx], it is a node id
           		rt.setAchievementId(Integer.parseInt(argv[indx]));
           		rt.setRequestType(UACT_AID_COMPLETE); // PUT 1 user
           		
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
        	
        	return rt;
        }
    	
        public static RequestTypeUserId setPostRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, 0, UACT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();
    		
           	if (indx >= argv.length) {
           		return rt; // UNDEFINED
           	}
           	
        	try {
	        	rt.setUserId(Integer.parseInt(argv[indx]));
	       		
	       		indx++;
	       		if (indx >= argv.length) {
	       			return rt;
		       	}
	       		
	           	if (argv[indx].equals("create")) {
	           		rt.setRequestType(UACT_CREATE); // POST
	           		return rt;
	           	}
	       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
      
        	return rt; // UNDEFINED
        }
        
        public static RequestTypeUserId setDeleteRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, 0, UACT_UNDEFINED);
      		
    		try {
    			int indx = uta.getBaseIndex() + 1;
    			String[] argv = uta.getArgv();
    			
    	       	if (indx >= argv.length) {
    	       		return rt;  // UNDEFINED
    	       	} 

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
           			return rt; // UNDEFINED
           		}
           		if (argv[indx].equals("destroy")) {
           			rt.setRequestType(UACT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
           		if (argv[indx].equals("delete")) {
           			rt.setRequestType(UACT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
        	
        	return rt;
        }
    }
    
    public static class RequestTypeUserId {
      	
    	private int _user_id;
    	private int _achievement_id;
    	private RequestType _req_type;
    	
    	public RequestTypeUserId(int uid, int aid, RequestType type) {
    		_user_id = uid;
    		_achievement_id = aid;
    		_req_type = type;
    	}
    	
    	void setUserId(int id) { _user_id = id; }
    	public int getUserId() { return _user_id; }
    	void setAchievementId(int id) { _achievement_id = id; }
    	public int getAchievementId() { return _achievement_id; }
    	void setRequestType(RequestType type) { _req_type = type; }
    	public RequestType getRequestType() { return _req_type; }
    }
}