package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;

import com.cboxgames.utils.idonia.types.UserPlaylist.UserPlaylistWrapper;
import com.cboxgames.utils.idonia.types.Leaderboard;
import com.cboxgames.utils.json.JsonConverter;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UnimplementedEnumException;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.db.playlist.UserPlaylistDBSQL;

import static com.cboxgames.idonia.backend.UserPlaylistHttpServlet.RequestType.*;

/**
 * Handle "/user_playlists and /user_playlists/*"
 * HTTP Method: GET/POST/PUT/DELETE
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class UserPlaylistHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private JsonConverter _json_converter;
	private UserPlaylistDBSQL _user_playlist_db_sql;
    private ObjectMapper _mapper;
	
	@Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _json_converter = JsonConverter.getInstance();
        _mapper = _json_converter.getMapper();
        try {
        	_user_playlist_db_sql = new UserPlaylistDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_playlists");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setGetRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UPT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
     	response.setContentType("application/json");
       	PrintWriter out = response.getWriter();
       	
       	switch (req_type) {
       		case UPT_DETAILS: {
       	       	//TODO: DESABLED BECAUSE IT WILL DESTROY US.
       			UserPlaylistWrapper[] userarray = null;
       			if (userarray == null) {
       	           	out.print("[]");
       	       	} else {
       	       	   	String json = _json_converter.getJson(userarray);
       	           	out.print(json);
       	       	}
       	       	break;
       		}
       		case UPT_UID_DETAILS: {
       			UserPlaylistWrapper[] userarray = _user_playlist_db_sql.getUserPlaylistDetails(user_id);
       			if (userarray == null) {
       	           	out.print("[]");
       	       	} else {
       	       	   	String json = _json_converter.getJson(userarray);
       	           	out.print(json);
       	       	}
       		  	break;
       		}
//       		case UPT_LEADERBOARD: {
//       			if (getUserIdAndPlaylistId(request, ret) == false) {
//       				response.sendError(Constants.SC_BAD_REQUEST);
//       				return;
//       			}
//       			Leaderboard lbn = _user_playlist_db_sql.getLeaderboard(ret.getUserId(), ret.getPlaylistId(), ret.getPage());
//       			if (lbn == null) {
//       	           	out.print("[]");
//       	       	} else {
//       	       	   	String json = _json_converter.getJson(lbn);
//       	           	out.print(json);
//       	       	}
//       			break;
//       		}
       		case UPT_LEADERBOARD_NEW: {
                HttpSession session = request.getSession();
                int uid = (Integer) session.getAttribute("user_id");
                // User user = _user_db_sql.getUserInfoByID(uid);
       			// Leaderboard lbn = _user_playlist_db_sql.getUserLeaderboard(uid, user.username);
                Leaderboard lbn = _user_playlist_db_sql.getLeaderboardNew(uid, 1 /* playlist_id */);
       			if (lbn == null) {
       	           	out.print("[]");
       	       	} else {
       	       	   	String json = _json_converter.getJson(lbn);
       	           	out.print(json);
       	       	}
       			break;
       		}
       		default: {
       			throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
       		}
       	}
    }
    
//    private boolean getUserIdAndPlaylistId(HttpServletRequest request, RequestTypeUserId reqtype) {
//    	
//    	boolean user_id_found = false, playlist_id_found = false, page_found = false;
//    	Map<String, String[]> param_map = request.getParameterMap();
//        Set<?> s = param_map.entrySet();
//        Iterator<?> it = s.iterator();
//        try {
//	        while (it.hasNext()) {
//	            // key=value separator this by Map.Entry to get key and value
//	            Map.Entry entry = (Map.Entry) it.next();
//	
//	            // getKey is used to get key of Map
//	            String param = (String) entry.getKey();
//	            if (param.equals("user_id")) {
//	            	user_id_found = true;
//	            	String[] values = (String[]) entry.getValue();
//	            	reqtype.setUserId(Integer.parseInt(values[0]));
//	            }
//	            else if (param.equals("playlist_id")) {
//	            	playlist_id_found = true;
//	            	String[] values = (String[]) entry.getValue();
//	            	reqtype.setPlaylistId(Integer.parseInt(values[0]));
//	            }
//	            else if (param.equals("page")) {
//	            	page_found = true;
//	            	String[] values = (String[]) entry.getValue();
//	            	reqtype.setPage(Integer.parseInt(values[0]));
//	            }
//	        }
//		} catch (NumberFormatException e) {
//			System.err.println("Unable to parse an integer value from user_id or playlist_id string ");
//		}
//	
//        
//        if (!user_id_found && !page_found) {
//        	System.err.println("User id or page is not provided when accessing /user_playlist/leaderboard");
//        	return false;
//        }
//        
//        if (!playlist_id_found) {
//        	System.err.println("Playlist id is not provided when accessing /user_playlist/leaderboard");
//        	return false;
//        }
//        
//        return true;
//    }
    
//    private static void sortHeap(UserPlaylist[] user_playlist_array) {
//		
//		if (user_playlist_array.length <= 1) return;
//		
//		for (int aindex = user_playlist_array.length; aindex > 1; aindex--) {
//			int mid_item = (aindex - 1) >> 1;
//	
//			for (int kx = mid_item; kx >= 0; kx--) {
//				for (int ix = mid_item; ix >= 0; ix--) {
//					int left_child = (2 * ix) + 1;
//					int right_child = (2 * ix) + 2;
//					int mChild;
//					
//					if ((left_child <= aindex) && (right_child <= aindex)) {
//						if (user_playlist_array[right_child].rating >= user_playlist_array[left_child].rating)
//							mChild = right_child;
//						else
//							mChild = left_child;
//					} else {
//						if (right_child > aindex)
//							mChild = left_child;
//						else
//							mChild = right_child;
//					}
//				
//					if (user_playlist_array[ix].rating < user_playlist_array[mChild].rating) {
//						UserPlaylist temp = user_playlist_array[ix];
//						user_playlist_array[ix] = user_playlist_array[mChild];
//						user_playlist_array[mChild] = temp;
//					}
//				}
//			}
//			
//			UserPlaylist tmp = user_playlist_array[0];
//			user_playlist_array[0] = user_playlist_array[aindex];
//			user_playlist_array[aindex] = tmp;
//		}
//	}
    
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_playlists");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPutRequestType(uta);
       	RequestType req_type = ret.getRequestType();
//       	int user_id = ret.getUserId();
       	
       	if (req_type == UPT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
       	// request to provide the playlist object to update.
       	
       	try {
	       	switch (req_type) {
	       		case UPT_UPDATE: {
//	       			int playlist_id = ret.getPlaylistId();
//	       			int count = ret.getCount();
//	       			if (_user_playlist_db_sql.UpdateUserPlaylist(user_id, user_playlist)) {
//	       				response.setStatus(Constants.SC_OK);
//	       			}
//	       			else {
//	       				throw new Exception("Unable to update a new user_playlist with user_id = " + user_id + 
//	       						" and playlist_id = " + playlist_id);
//	       			}
	       			break;
	       		}	
	    		default: {
	    			throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
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
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_playlists");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPostRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	// int user_id = ret.getUserId();
       	
       	if (req_type == UPT_UNDEFINED) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	ServletInputStream stream = request.getInputStream();
       	try {
	       	switch (req_type) {
	       		case UPT_CREATE: {
	       			UserPlaylistWrapper unw = _json_converter.getObject(stream, UserPlaylistWrapper.class);
	       			
	       			if (_user_playlist_db_sql.userPlaylistExist(unw.user_playlist.user_id)) {
	       	      		response.sendError(Constants.SC_CONFLICT, "User playlist with user id = " + unw.user_playlist.user_id + " already exists");
	       	      		return;
	       			}
	       			
	       			if (_user_playlist_db_sql.createUserPlaylists(unw.user_playlist.user_id) == true) {
	       				response.setStatus(Constants.SC_OK);
	       			} else {
	       				throw new Exception("Unable to create a new user_playlist");
	       			}
	       			
	       			break;
	       		}
	       		default: {
	       			throw new Exception("Undefined request type " + req_type.name() + " when posting a user_playlist");
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
       	
       	UriToArgv uta = new UriToArgv(uri_str, "user_playlists");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setDeleteRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UPT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
   
      	try {
	       	switch (req_type) {
		   		case UPT_DESTROY: {
		   			if (_user_playlist_db_sql.deleteUserPlaylist(user_id) == true) {
	       				response.setStatus(Constants.SC_OK);
	       			} else {
	       				throw new Exception("Unable to delete a new user_playlist with user_id = " + user_id);
	       			}
		   			break;
		   		}
		   		default: {
		   			throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
		   		}
		   	}
    	} catch (Exception e) {
       		e. printStackTrace();
       	}
    }
	
    public static enum RequestType {
    	
    	UPT_DETAILS, // GET details for all user_playlists < /user_playlists/details >
    	UPT_UID_DETAILS, // GET details for a given user_id < /user_playlists/{user_id}/details >
    	// UPT_LEADERBOARD, // GET leaderboard < /user_playlists/leaderboard >
    	UPT_LEADERBOARD_NEW, // GET leaderboard < /user_playlists/leaderboard_new >
    	
    	UPT_UPDATE, // PUT (update) < /user_playlists/update >
    	UPT_PID_UPDATE, // PUT (update) < /user_playlists/{user_id}/update >
    	
    	UPT_CREATE, // POST (create) < /user_playlists/create
    	
    	UPT_DESTROY, // DELTE < /user_playlists/{user_id}/destroy >
    	
    	UPT_UNDEFINED;
    	
    	public static RequestTypeUserId setGetRequestType(UriToArgv uta) {
    		
 			int indx = uta.getBaseIndex() + 1;
			String[] argv = uta.getArgv();
      		RequestTypeUserId rt = new RequestTypeUserId(-1, -1, -1, UPT_UNDEFINED);
      		
    		try {
    	       	if (indx >= argv.length) {
    	       		rt.setRequestType(UPT_DETAILS);
    	       		return rt;  // GET (all user_playlists)
    	       	} 
    	       	if (argv[indx].equals("details")) {
    	       		rt.setRequestType(UPT_DETAILS);
    	       		return rt; // GET (all user_playlists)
    	       	}
//    	       	if (argv[indx].equals("leaderboard")) {
//    	       		rt.setRequestType(UPT_LEADERBOARD);
//    	       		return rt; // GET (all user_playlists)
//    	       	} 
    	       	if (argv[indx].equals("leaderboard_new")) {
    	       		rt.setRequestType(UPT_LEADERBOARD_NEW);
    	       		return rt; // GET (all user_playlists)
    	       	} 

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
    	       		rt.setRequestType(UPT_UID_DETAILS);
           			return rt; // GET 1 user
           		}
           		if (argv[indx].equals("details")) {
           			rt.setRequestType(UPT_UID_DETAILS);
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
    	 	} catch (NumberFormatException e) {
        		System.err.println("Unable to parse an integer value from string " + argv[indx]);
    		}
        	
        	return rt;
        }
    	
    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(-1, -1, -1, UPT_UNDEFINED);
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
           		if (argv[indx].equals("update")) {
    	   			rt.setRequestType(UPT_UPDATE); // PUT 1 user
    	   			return rt;
           		}
           		
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
	     	} catch (NumberFormatException e) {
	    		System.err.println("Unable to parse an integer value from string " + argv[indx]);
			}
        	
        	return rt;
        }
    	
        public static RequestTypeUserId setPostRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(-1, -1, -1, UPT_UNDEFINED);
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
	           		rt.setRequestType(UPT_CREATE); // POST
	           		return rt;
	           	}
	       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		System.err.println("Unable to parse an integer value from string " + argv[indx]);
    		}
      
        	return rt; // UNDEFINED
        }
        
        public static RequestTypeUserId setDeleteRequestType(UriToArgv uta) {
    		
      		int indx = uta.getBaseIndex() + 1;
      		String[] argv = uta.getArgv();
      		RequestTypeUserId rt = new RequestTypeUserId(-1, -1, -1, UPT_UNDEFINED);
      		
    		try {
    	       	if (indx >= argv.length) {
    	       		return rt;  // UNDEFINED
    	       	} 

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
           			return rt; // UNDEFINED
           		}
           		if (argv[indx].equals("destroy")) {
           			rt.setRequestType(UPT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
        		if (argv[indx].equals("delete")) {
           			rt.setRequestType(UPT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		System.err.println("Unable to parse an integer value from string " + argv[indx]);
    		}
        	
        	return rt;
        }
    }
    
    public static class RequestTypeUserId {
      	
    	private int _user_id;
    	private int _playlist_id;
    	private int _page;
    	private RequestType _req_type;
    	
    	public RequestTypeUserId(int uid, int pid, int page, RequestType type) {
    		_user_id = uid;
    		_playlist_id = pid;
    		_page = page;
    		_req_type = type;
    	}
    	
    	void setUserId(int id) { _user_id = id; }
    	public int getUserId() { return _user_id; }
    	void setPlaylistId(int id) { _playlist_id = id; }
    	public int getPlaylistId() { return _playlist_id; }
    	void setPage(int id) { _page = id; }
    	public int getPage() { return _page; }
    	void setRequestType(RequestType type) { _req_type = type; }
    	public RequestType getRequestType() { return _req_type; }
    }
}