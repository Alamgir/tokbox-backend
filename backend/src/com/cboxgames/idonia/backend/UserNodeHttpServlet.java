package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cboxgames.idonia.backend.commons.*;
import com.cboxgames.idonia.backend.commons.accessorygenerator.AccessoryGenerator;
import com.cboxgames.idonia.backend.commons.db.battle.BattleDBSQL;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.RewardResponse.*;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserNode;
import com.cboxgames.utils.idonia.types.UserNode.NodeComplete;
import com.cboxgames.utils.idonia.types.UserNode.UserNodeWrapper;
import com.cboxgames.utils.idonia.types.Character;

import com.cboxgames.idonia.backend.commons.db.node.UserNodeDBSQL;
import org.codehaus.jackson.map.ObjectMapper;
import com.cboxgames.utils.json.JsonConverter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import static com.cboxgames.idonia.backend.UserNodeHttpServlet.RequestType.*;


/**
 * Handle "/user_nodes/*" and "/user_nodes"
 * HTTP Method: GET/POST(create)/PUT(update)/DELETE
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class UserNodeHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private JsonConverter _json_converter;
	private UserNodeDBSQL _un_db_sql;
    private UserDBSQL _user_db_sql;
    private BattleDBSQL _b_db_sql;
    private ObjectMapper _mapper;

	@Override
	public void init() {
        _sql_data_source = new SqlDataSource();
        _json_converter = JsonConverter.getInstance();
        _mapper = _json_converter.getMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        try {
        	_un_db_sql = new UserNodeDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _b_db_sql = new BattleDBSQL(_sql_data_source.get_data_source(), this.getServletContext());

        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_nodes");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setGetRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UNT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
     	response.setContentType("application/json");
       	PrintWriter out = response.getWriter();
    	UserNodeWrapper[] userarray = null;
       	
       	switch (req_type) {
       		case UNT_DETAILS: {
       	       	//TODO: DESABLED BECAUSE IT WILL DESTROY US.
       	       	//userarray = _un_db_sql.getUserNodeDetails();
       	       	break;
       		}
       		case UNT_UID_DETAILS: {
       		  	userarray = _un_db_sql.getUserNodeDetails(user_id);
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
       	UriToArgv uta = new UriToArgv(uri_str, "user_nodes");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPutRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	
       	if (req_type == UNT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	try {
	       	switch (req_type) {
	       		case UNT_COMPLETE: {
                    InputStream stream = request.getInputStream();
                    NodeComplete node_complete = _mapper.readValue(stream, NodeComplete.class);
                    CompletedNode completed_node = new CompletedNode();

                    //Get the user to do various checks
                    HttpSession session = request.getSession();
                    int uid = (Integer) session.getAttribute("user_id");
                    User user = _user_db_sql.getUserByID(uid);

                    //Set the Node to Complete
                    for (UserNode un : user.user_nodes) {
                        if (un.id == node_complete.user_node_id) {
                            un.complete = true;
                            completed_node.user_node_id = un.id;
                            break;
                        }
                    }

                    //Generate the reward according to random chance
                    Character[] user_characters = user.user_characters;
                    // int strongest_level = getUserCharacterMaximumLevel(user_characters);
                    Random rand = new Random();
//                    int r = rand.nextInt(10);
                    Accessory gen_acc = null;
//                    if (r < 5) {
//                        completed_node.reward.gold = (int) Math.sqrt(strongest_level*400);
//                        user.money += completed_node.reward.gold;
//                        CBoxLoggerSyslog.log("reward","gold_rewarded",completed_node.reward.gold);
//                    }
//                    else if (r >= 5 && r < 8) {
//                        completed_node.reward.tokens = (rand.nextInt(5));
//                        user.tokens += completed_node.reward.tokens;
//                        CBoxLoggerSyslog.log("reward","tokens_rewarded",completed_node.reward.tokens);
//                    }
//                    else {
                        if (user.user_character_accessories_inventory != null && user.user_character_accessories_inventory.length == user.inventory_spots) {
                            ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.ACCESSORY_INVENTORY_FULL,
                					Constants.SC_BAD_REQUEST);
                            return;
                        }
                        
                        int count = user_characters.length;
                        int rand_uc = rand.nextInt(count);
                        gen_acc = AccessoryGenerator.generateAccessoryForCharacter(user_characters[rand_uc].name,
                                                                                       user_characters[rand_uc].level,
                                                                                       Constants.EPIC);
                        gen_acc.user_character_id = 0;
                        gen_acc.user_id = uid;
                        gen_acc.accessory_id = 0;

                        CBoxLoggerSyslog.log("reward","acc_rewarded",gen_acc.name);
                    
//                    }

                   //Update the user using BattleDBSQL
                    if (_b_db_sql.updatePveUser(user, gen_acc, true ) == false) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    	return;
                    }
                    
                    CompletedNodeWrapper cnw = new CompletedNodeWrapper();
                    completed_node.reward.accessory = gen_acc;
                    cnw.completed_node = completed_node;
                    ResponseTools.prepareResponseJson(response, _mapper, cnw, Constants.SC_OK);
                    CBoxLoggerSyslog.log("complete_node","user_id","node_id",user.id,node_complete.user_node_id);
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

//    private int getUserCharacterMaximumLevel(Character[] user_characters) {
//
//		int max = 0;
//		for (Character character : user_characters) {
//			if (character.level <= max) continue;
//			max = character.level;
//		}
//
//		return max;
//	}
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_nodes");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPostRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	// int user_id = ret.getUserId();
       	
       	if (req_type == UNT_UNDEFINED) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	ServletInputStream stream = request.getInputStream();
       	try {
	       	switch (req_type) {
	       		case UNT_CREATE: {
	       			UserNodeWrapper unw = _json_converter.getObject(stream, UserNodeWrapper.class);
	       			
	       			if (_un_db_sql.userNodeExist(unw.user_node.user_id)) {
	       	      		response.sendError(Constants.SC_CONFLICT, "User node with user id = " + unw.user_node.user_id + " already exists");
	       	      		return;
	       			}
	       			
	       			if (_un_db_sql.createUserNodes(unw.user_node.user_id) == true) {
	       				response.setStatus(Constants.SC_OK);
	       			} else {
	       				throw new Exception("Unable to create a new user_node");
	       			}
	       			break;
	       		}
	       		default: { // internal error
	       			throw new Exception("Undefined request type " + req_type.name() + " when posting a user_node");
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
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_nodes");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setDeleteRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == UNT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
   
       	try {
	       	switch (req_type) {
		   		case UNT_DESTROY: {
		   			if (_un_db_sql.deleteUserNode(user_id) == true) {
	       				response.setStatus(Constants.SC_OK);
	       			} else {
	       				throw new Exception("Unable to delete a new user_node with user_id = " + user_id);
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
    	
    	UNT_DETAILS, // GET details for all user_nodes < /user_nodes/details >
    	UNT_UID_DETAILS, // GET details for a given user_id < /user_nodes/{user_id}/details >
    	
    	UNT_COMPLETE, // PUT (update) < /user_nodes/{user_id}/complete >
    	UNT_NID_COMPLETE, // PUT (update) < /user_nodes/{user_id}/{node_id} >
    	
    	UNT_CREATE, // POST (create) < /user_nodes/create
    	
    	UNT_DESTROY, // DELTE < /user_nodes/{user_id}/destroy or /user_nodes/{user_id}/delete >
    	
    	UNT_UNDEFINED;
    	
    	public static RequestTypeUserId setGetRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, 0, UNT_UNDEFINED);
      		
    		try {
    			int indx = uta.getBaseIndex() + 1;
    			String[] argv = uta.getArgv();
    			
    	       	if (indx >= argv.length) {
    	       		rt.setRequestType(UNT_DETAILS);
    	       		return rt;  // GET (all user_nodes)
    	       	} 
    	       	if (argv[indx].equals("details")) {
    	       		rt.setRequestType(UNT_DETAILS);
    	       		return rt; // GET (all user_nodes)
    	       	} 

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
    	       		rt.setRequestType(UNT_UID_DETAILS);
           			return rt; // GET 1 user
           		}
           		if (argv[indx].equals("details")) {
           			rt.setRequestType(UNT_UID_DETAILS);
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
        	
        	return rt;
        }
    	
    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, 0, UNT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

 	       	if (indx >= argv.length) {
                rt.setRequestType(UNT_UNDEFINED);
	       		return rt;
	       	}
            if (argv[indx].equals("complete")) {
                rt.setRequestType(UNT_COMPLETE);
                return rt;
            }
        	return rt;
        }
    	
        public static RequestTypeUserId setPostRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, 0, UNT_UNDEFINED);
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
	           		rt.setRequestType(UNT_CREATE); // POST
	           		return rt;
	           	}
	       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		// TODO
    		}
      
        	return rt; // UNDEFINED
        }
        
        public static RequestTypeUserId setDeleteRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, 0, UNT_UNDEFINED);
      		
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
           			rt.setRequestType(UNT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
           		if (argv[indx].equals("delete")) {
           			rt.setRequestType(UNT_DESTROY); // DELETE
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
    	private int _node_id;
    	private RequestType _req_type;
    	
    	public RequestTypeUserId(int uid, int nid, RequestType type) {
    		_user_id = uid;
    		_req_type = type;
    		_node_id = nid;
    	}
    	
    	void setUserId(int id) { _user_id = id; }
    	public int getUserId() { return _user_id; }
    	void setNodeId(int id) { _node_id = id; }
    	public int getNodeId() { return _node_id; }
    	void setRequestType(RequestType type) { _req_type = type; }
    	public RequestType getRequestType() { return _req_type; }
    }
}