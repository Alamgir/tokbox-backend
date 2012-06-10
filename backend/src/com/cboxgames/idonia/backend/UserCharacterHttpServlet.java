package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.*;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest.*;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import static com.cboxgames.idonia.backend.UserCharacterHttpServlet.RequestType.*;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Skill;
import com.cboxgames.utils.idonia.types.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/10/11
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserCharacterHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
    private UserCharacterDBSQL _uc_db_sql;
    private UserDBSQL _u_db_sql;
    private ObjectMapper _mapper;

	@Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        
        try {
            _uc_db_sql = new UserCharacterDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _u_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        String uri_str = request.getRequestURI();
        UriToArgv uta = new UriToArgv(uri_str, "user_characters");
        if (uta.getBaseIndex() >= uta.getArgv().length) {
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }

        RequestTypeUserCharacterId ret = setPutRequestType(uta);
        RequestType req_type = ret.getRequestType();
        int uc_id = ret.getUcId();

        if (req_type == RequestType.UCRT_UNDEFINED)	{
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }
    	

        
        switch (req_type) {
            case UCRT_UPDATE: {
                InputStream stream = request.getInputStream();
                UserCharacterUpdateRequest ucu_request = _mapper.readValue(stream, UserCharacterUpdateRequest.class);

                //Get user_id for logging/get_user purposes
                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");

                if (ucu_request.user_characters == null) {
                    ResponseTools.prepareResponseJsonWithUserID(user_id, response, _mapper, "Empty user_character_update_all request",
                                                                Constants.SC_BAD_REQUEST);
                    return;
                }

                //Check for sent accessory IDs of 0
                //ArrayList<Integer> request_ids = new ArrayList<Integer>();
                int in_lineup_count = 0;
                for (UserCharacterUpdate ucu : ucu_request.user_characters) {
                    if (ucu.in_lineup) {
                        in_lineup_count++;
                    }
                    if (ucu.id == 0) {
                        ResponseTools.prepareResponseJsonWithUserID(user_id, response, _mapper, "Received a user_character id of 0",
                                                                    Constants.SC_BAD_REQUEST);
                        // CBoxLoggerSyslog.log("received_ucid_of_zero","ucid","user_id",0,user_id);
                        // System.out.println("received_ucaid_of_zero");
                        return;
                    }
                    UserCharacterAccessory uca = ucu.user_character_accessory;
                    if (uca.accessory_id == 0 || uca.armor_accessory_id == 0 || uca.helmet_accessory_id == 0 || uca.weapon_accessory_id == 0) {
                        ResponseTools.prepareResponseJsonWithUserID(user_id, response, _mapper, "Received a user_character_accessory id of 0",
                                                                    Constants.SC_BAD_REQUEST);
                        // CBoxLoggerSyslog.log("received_ucaid_of_zero","ucaid","user_id",0,user_id);
                        // System.out.println("received_ucaid_of_zero");
                        return;
                    }
//            if (request_ids.contains(uca.accessory_id)) {
//                System.out.println("repeated accessory_id");
//            }
//            else {
//            	request_ids.add(uca.accessory_id);
//            }
//
//            if (request_ids.contains(uca.armor_accessory_id)) {
//                System.out.println("repeated armor_accessory_id");
//            }
//            else {
//                request_ids.add(uca.armor_accessory_id);
//            }
//
//            if (request_ids.contains(uca.helmet_accessory_id)) {
//                System.out.println("repeated helmet_accessory_id");
//            }
//            else {
//                request_ids.add(uca.helmet_accessory_id);
//            }
//
//            if (request_ids.contains(uca.weapon_accessory_id)) {
//                System.out.println("repeated weapon_accessory_id");
//            }
//            else {
//                request_ids.add(uca.weapon_accessory_id);
//            }
                }

                if (in_lineup_count < 3) {
                    ResponseTools.prepareResponseJsonWithUserID(user_id, response, _mapper, "Less than 3 characters in_lineup for request",
                                                                Constants.SC_BAD_REQUEST);
                    // CBoxLoggerSyslog.log("received_less_than_3_characters_in_lineup","ucid","user_id",0,user_id);
                    // System.out.println("received_less_than_3_characters_in_lineup");
                    return;
                }

                User user = _u_db_sql.getUserByID(user_id);
                //check user_character_skill_ids
                //check user_character_accessory_ids - inventory, in-use
                if (!checkRequest(user, ucu_request.user_characters)) {
                    ResponseTools.prepareResponseJsonWithUserID(user_id, response, _mapper, "malicious user_characters update_all request", Constants.SC_FORBIDDEN);
                    // response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    // CBoxLoggerSyslog.log("malicious_ucu_request","user_id",user_id);
                    return;
                }

                if (_uc_db_sql.updateUserCharacterAll(user, ucu_request)) {
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                }
                else {
                    ResponseTools.prepareResponseJsonWithUserID(user_id, response, _mapper, "updateUserCharacterAll failed", Constants.SC_INTERNAL_SERVER_ERROR);
                }
                
                break;
            }
            case UCRT_STAT_POINT: {
                InputStream stream = request.getInputStream();
                @SuppressWarnings("unchecked")
                Map<String, Object> data = _mapper.readValue(stream, Map.class);

                int new_stat_points = 0;
                if (data.containsKey("stat_points")) {
                    new_stat_points = (Integer)data.get("stat_points");
                }
                
                if (_uc_db_sql.saveStatPoints(new_stat_points, uc_id) != -1) {
                    ResponseTools.prepareResponseJson(response, _mapper, uc_id, HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }


                break;
            }
            default: {
                response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                break;
            }
        }
        
        
        
        
    }
    
    private static boolean checkRequest(User user, UserCharacterUpdate[] uc_update) {
        //Add inventory ids to array list
        //ArrayList<Integer> acc_ids = new ArrayList<Integer>();
        Map<Integer, Integer> acc_map = null;
        Accessory[] acc_inventory = user.user_character_accessories_inventory;
        if (acc_inventory != null) {
        	acc_map = new HashMap<Integer, Integer>();
            for (Accessory acc : acc_inventory) {
                acc_map.put(acc.id, acc.id);
            }
        }
        
        Map<Integer,Character> uc_map = UserCharacterDBSQL.getUserCharacterMap(user);

        ArrayList<Integer> check_ids = new ArrayList<Integer>();
        for (UserCharacterUpdate u_request : uc_update) {
            
            Character user_char = uc_map.get(u_request.id);
            if (user_char == null) {
            	String message = String.format("Request user_character_id=%d is not owned by user_id=%d", u_request.id, user.id);
        		CBoxLoggerSyslog.log("Bad_request","user_id", "message", user.id, message);
                return false;
            }

            UserCharacterAccessory request_uca = u_request.user_character_accessory;
            
            for (Accessory acc : user_char.user_character_accessories) {
            	int aid = 0;
                if (acc.accessory_type.equals(Constants.ACCESSORY)) {
                    if (request_uca.accessory_id != acc.id) {
                        aid = request_uca.accessory_id;
                    }
                }
                else if (acc.accessory_type.equals(Constants.WEAPON)) {
                    if (request_uca.weapon_accessory_id != acc.id) {
                        aid = request_uca.accessory_id;
                    }
                }
                else if (acc.accessory_type.equals(Constants.ACC_ARMOR)) {
                    if (request_uca.armor_accessory_id != acc.id) {
                        aid = request_uca.accessory_id;
                    }
                }
                else if (acc.accessory_type.equals(Constants.HELMET)) {
                    if (request_uca.helmet_accessory_id != acc.id) {
                        aid = request_uca.accessory_id;
                    }
                }
                
                if (acc_map != null) acc_map.put(acc.id, acc.id);
                if (aid != 0) check_ids.add(aid);
            }

            if (checkCharacterSkills(user_char, u_request, user.id) == false)
            	return false;
        }
        
        //Check All User_Character_Accessories for ids in check_ids
        return checkAcc(acc_map, check_ids, user.id);
    }
    
    private static boolean checkCharacterSkills(Character user_char, UserCharacterUpdate u_request, int user_id) {
    	if ((u_request.user_character_skills == null) || (user_char.user_character_skills == null))
        	return true;
    	
    	// check if a requested skill is owned by user_char
        Map<Integer, Integer> skill_map = new HashMap<Integer, Integer>();
        for (Skill skill : user_char.user_character_skills)
        	skill_map.put(skill.id, skill.id);
        
        for (UserCharacterSkills request_skill : u_request.user_character_skills) {
        	if (skill_map.get(request_skill.id) != null) continue;
        	String message = String.format("Request skill_id=%d is not owned by user_character_id=%d",request_skill.id, user_char.id);
    		CBoxLoggerSyslog.log("Bad_request","user_id", "message", user_id, message);
    		return false;
        }
        
        return true;
    }
    
    private static boolean checkAcc(Map<Integer, Integer> acc_map, ArrayList<Integer> check_ids, int user_id) {
        for (int check_id : check_ids) {
            if (acc_map != null) {
                if (acc_map.get(check_id) != null) continue;
                String message = String.format("Request user_accessory_id=%d is not owned by user_character_id=%d",check_id, user_id);
                CBoxLoggerSyslog.log("Bad_request","user_id", "message", user_id, message);
                return false;
            }
        }
        return true;
    }

    public static enum RequestType {

        UCRT_UPDATE,
        UCRT_STAT_POINT,
        UCRT_UNDEFINED;

        public static RequestTypeUserCharacterId setPutRequestType(UriToArgv uta) {

            RequestTypeUserCharacterId rt = new RequestTypeUserCharacterId(0, UCRT_UNDEFINED);
            int indx = uta.getBaseIndex() + 1;
            String[] argv = uta.getArgv();

            try{
                if (indx >= argv.length) {
                    return rt;
                }
                if (argv[indx].equals("update_all")) {
                    rt.setRequestType(UCRT_UPDATE); // PUT 1 user
                    return rt;
                }

                rt.setUcId(Integer.parseInt(argv[indx]));
                indx++;
                if (indx >= argv.length) {
                    return rt;
                }
                if (argv[indx].equals("stat_point")) {
                    rt.setRequestType(UCRT_STAT_POINT);
                    return rt;
                }
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                System.err.println("Unable to parse " + argv[indx] + " as an user_character id");
            }
            
            // Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
            return rt;
        }

    }

    public static class RequestTypeUserCharacterId {
        public RequestTypeUserCharacterId(int ucid, RequestType type) {
            _uc_id = ucid;
            _req_type = type;
        }
        void setUcId(int id) { _uc_id = id; }
        public int getUcId() { return _uc_id; }
        void setRequestType(RequestType type) { _req_type = type; }
        public RequestType getRequestType() { return _req_type; }

        private int _uc_id;
        private RequestType _req_type;
    }
    
}
