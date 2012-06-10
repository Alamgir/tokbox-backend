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

import com.cboxgames.utils.idonia.types.UserTutorial.UserTutorialWrapper;
import com.cboxgames.utils.json.JsonConverter;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.db.tutorial.UserTutorialDBSQL;

import static com.cboxgames.idonia.backend.UserTutorialHttpServlet.RequestType.*;


/**
 * Handle "/user_tutorials/*"
 * HTTP Method: PUT
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class UserTutorialHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private JsonConverter _json_converter;
	private UserTutorialDBSQL _user_db_sql;
	private ObjectMapper _mapper;
	
	@Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _json_converter = JsonConverter.getInstance();
        _mapper = _json_converter.getMapper();
        try {
        	_user_db_sql = new UserTutorialDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	System.out.println("URI: " + uri_str);

       	UriToArgv uta = new UriToArgv(uri_str, "user_tutorials");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setGetRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UTT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
     	response.setContentType("application/json");
       	PrintWriter out = response.getWriter();
       	UserTutorialWrapper[] userarray = null;
       	
       	switch (req_type) {
       		case UTT_DETAILS: {
       	       	//TODO: DESABLED BECAUSE IT WILL DESTROY US.
       	       //	userarray = _user_db_sql.getUserTutorialDetails();
       	       	break;
       		}
       		case UTT_UID_DETAILS: {
       		 	userarray = _user_db_sql.getUserTutorialDetails(user_id);
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
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_tutorials");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPutRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	
       	if (req_type == UTT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
      	int user_id = ret.getUserId();
      	
       	try {
	       	switch (req_type) {
	       		case UTT_COMPLETE: {
	       			break;
	       		}
	       		case UTT_TID_COMPLETE: {
	       			int tutorial_id = ret.getTutorialId();
	       			if (_user_db_sql.userTutorialComplete(user_id, tutorial_id)) {
	       				response.setStatus(Constants.SC_OK);
	       			}
	       			else {
	       				throw new Exception("Unable to update a new user_tutorial with user_id = " + user_id + 
	       						" and tutorial_id = " + tutorial_id);
	       			}
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
       	UriToArgv uta = new UriToArgv(uri_str, "user_tutorials");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

		RequestType req_type = setPostRequestType(uta).getRequestType();     	
       	if (req_type == UTT_UNDEFINED) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	ServletInputStream stream = request.getInputStream();
       	
       	try {
	       	switch (req_type) {
	       		case UTT_CREATE: {
	       			UserTutorialWrapper utw = _json_converter.getObject(stream, UserTutorialWrapper.class);
	       			
	       			if (_user_db_sql.userTutorialExist(utw.user_tutorial.user_id)) {
	       	      		response.sendError(Constants.SC_CONFLICT, "User tutorial with user id = " + utw.user_tutorial.user_id + " already exists");
	       	      		return;
	       			}
	       			
	       			if (_user_db_sql.createUserTutorials(utw.user_tutorial.user_id) == true) {
	       				response.setStatus(Constants.SC_OK);
	       			} else {
	       				throw new Exception("Unable to create a new user_tutorial");
	       			}
	       			break;
	       		}
	       		default: { // internal error
	       			break;
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
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_tutorials");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setDeleteRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UTT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
   
       	try {
	       	switch (req_type) {
		   		case UTT_DESTROY: {
		   			if (_user_db_sql.deleteUserTutorial(user_id) == true) {
	       				response.setStatus(Constants.SC_OK);
	       			} else {
	       				throw new Exception("Unable to delete a new user_tutorial with user_id = " + user_id);
	       			}
		   			
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
    	
    	UTT_DETAILS, // GET details for all user_tutorials < /user_tutorials/details >
    	UTT_UID_DETAILS, // GET details for a given user_id < /user_tutorials/{user_id}/details >
    	
    	UTT_COMPLETE, // PUT (update) < /user_tutorials/{user_id}/complete >
    	UTT_TID_COMPLETE, // PUT (update) < /user_tutorials/{user_id}/{tutorial_id} >
    	
    	UTT_CREATE, // POST (create) < /user_tutorials/create
    	
    	UTT_DESTROY, // DELTE < /user_tutorials/{user_id}/destroy  or  /user_tutorials/{user_id}/delete >
    	
    	UTT_UNDEFINED;
    	
    	public static RequestTypeUserId setGetRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, 0, UTT_UNDEFINED);
      		
    		try {
    			int indx = uta.getBaseIndex() + 1;
    			String[] argv = uta.getArgv();
    			
    	       	if (indx >= argv.length) {
    	       		rt.setRequestType(UTT_DETAILS);
    	       		return rt;  // GET (all user_tutorials)
    	       	} 
    	       	if (argv[indx].equals("details")) {
    	       		rt.setRequestType(UTT_DETAILS);
    	       		return rt; // GET (all user_tutorials)
    	       	} 

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
    	       		rt.setRequestType(UTT_UID_DETAILS);
           			return rt; // GET 1 user
           		}
           		if (argv[indx].equals("details")) {
           			rt.setRequestType(UTT_UID_DETAILS);
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
        	
        	return rt;
        }
    	
    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, 0, UTT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

        	try {
    	       	if (indx >= argv.length) {
    	       		return rt;
    	       	}
    	       	
           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
           			return rt;
    	       	}
           		if (argv[indx].equals("complete")) {
    	   			rt.setRequestType(UTT_COMPLETE); // PUT 1 user
    	   			return rt;
    	    	}
           		
           		// if successfully reading argv[idx], it is a tutorial id
           		rt.setTutorialId(Integer.parseInt(argv[indx]));
           		rt.setRequestType(UTT_TID_COMPLETE); // PUT 1 user
           		
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
        	
        	return rt;
        }
    	
        public static RequestTypeUserId setPostRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, 0, UTT_UNDEFINED);
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
	           		rt.setRequestType(UTT_CREATE); // POST
	           		return rt;
	           	}
	       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
           	
       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
      
        	return rt; // UNDEFINED
        }
        
        public static RequestTypeUserId setDeleteRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, 0, UTT_UNDEFINED);
      		
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
           			rt.setRequestType(UTT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
          		if (argv[indx].equals("delete")) {
           			rt.setRequestType(UTT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
        	
        	return rt;
        }
    }
    
    private static class RequestTypeUserId {
      	
    	private int _user_id;
    	private RequestType _req_type;
    	private int _tutorial_id;
    	
    	public RequestTypeUserId(int uid, int tid, RequestType type) {
    		_user_id = uid;
    		_tutorial_id = tid;
    		_req_type = type;
    	}
    	
    	void setUserId(int id) { _user_id = id; }
    	public int getUserId() { return _user_id; }
    	void setTutorialId(int id) { _tutorial_id = id; }
    	public int getTutorialId() { return _tutorial_id; }
    	void setRequestType(RequestType type) { _req_type = type; }
    	public RequestType getRequestType() { return _req_type; }
    }
}