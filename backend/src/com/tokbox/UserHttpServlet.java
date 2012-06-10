package com.tokbox;

import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.utils.json.JsonConverter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/9/12
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserHttpServlet extends HttpServlet {
    private JsonConverter _json_converter;
    private ObjectMapper _mapper;
    
    public void init() {
        _json_converter = JsonConverter.getInstance();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        
        
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        String uri_str = request.getRequestURI();
        UriToArgv uta = new UriToArgv(uri_str, "users");

    }


    public static enum RequestType {

        URT_REQUEST_TOKEN, //GET request_token from dropbox </users/req_auth>
        URT_ACCESS_TOKEN, //POST </users/auth>
        
        URT_DETAILS, // GET details for all users < /users/details >
        URT_TAPJOY, // GET < /users/tapjoy >
        URT_UID_DETAILS, // GET details for a given user_id < /users/{user_id}/details >
        URT_GET_ACCESSORIES, // GET accessories for a given user_id < /users/{user_id}/get_accessories >
        URT_VERIFY, // GET < /users/verify >
        URT_UID_VERIFY, // GET < /users/verify >
        URT_OFFLINE_USER, // GET < /users/offline_user >

        URT_ACHIEVEMENT, // PUT </users/achievement>
        URT_PVP, // PUT < /users/pvp >
        URT_OFFLINE_PVP, //PUT < /users/offline_pvp >
        URT_CHARACTERS, // PUT < /users/{user_id}/characters >
        URT_SMALL_BAG_OF_GOODIES, // PUT < /users/{user_id}/sbg >
        URT_BOX_OF_GOODIES, // PUT < /users/{user_id}/bog >
        URT_GIANT_SAG_OF_GOODIES, // PUT < /users/{user_id}/gsg >
        URT_MASSIVE_SAG_OF_GOODIES, // PUT < /users/{user_id}/msg >
        URT_HAND_FULL_OF_GOODIES, // PUT < /users/{user_id}/hfg >
        URT_MOUTH_FULL_OF_GOODIES, // PUT < /users/{user_id}/mfsg >
        URT_GOLD_MINE, // PUT < /users/{user_id}/gom >
        URT_AMNESIA, // PUT < /users/{user_id}/amn >
        URT_CHARACTER_SLOT, // PUT < /users/{user_id}/chs >
        URT_BREAD_STICK, // PUT < /users/{user_id}/bre >
        URT_BREAD_SLICE, // PUT < /users/{user_id}/bsl >
        URT_BREAD_LOAF, // PUT < /users/{user_id}/blf >
        URT_FRESH_BOOTY, // PUT < /users/{user_id}/frb >
        URT_REFORGE, // PUT < /users/{user_id}/wrt >
        URT_WEAPON, // PUT < /users/{user_id}/wpn >
        URT_EXPANDED_SACK, // PUT < /users/{user_id}/sck >
        URT_NO_RP, //PUT < /users/{user_id}/nrp >
        URT_NO_ADS, //PUT < /users/{user_id}/nad >
        URT_EXP_SCROLL, //PUT < /users/{user_id}/exp >
        URT_UID_PVE, // PUT for a given user_id < /users/{user_id}/pve >

        URT_ADD_TOKENS, // PUT for a given user_id < /users/{user_id}/tokens>
        URT_ADD_MONEY, // PUT for a given user_id < /users/{user_id}/money>

        URT_EXIST, // POST < /users/exist >
        URT_SIGNIN, // POST < /users/sign_in >

        URT_DESTROY, // DELTE < /users/{user_id}/destroy >

        URT_UNDEFINED;

        public static RequestTypeUserId setGetRequestType(UriToArgv uta) {

            RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
            int indx = uta.getBaseIndex() + 1;
            String[] argv = uta.getArgv();

            try {
                if (indx >= argv.length) {
                    rt.setRequestType(URT_DETAILS);
                    return rt;  // GET (all users)
                }
                if (argv[indx].equals("req_auth")) {
                    rt.setRequestType(URT_REQUEST_TOKEN);
                    return rt; // GET
                }
                if (argv[indx].equals("tapjoy")) {
                    rt.setRequestType(URT_TAPJOY);
                    return rt; // GET
                }
                if (argv[indx].equals("details")) {
                    rt.setRequestType(URT_DETAILS);
                    return rt; // GET (all users)
                }
                if (argv[indx].equals("verify")) {
                    rt.setRequestType(URT_VERIFY);
                    return rt; // GET 1 user										
                }
                if (argv[indx].equals("offline_user")) {
                    rt.setRequestType(URT_OFFLINE_USER);
                    return rt; // GET 1 user										
                }


                rt.setUserId(Integer.parseInt(argv[indx]));

                indx++;
                if (indx >= argv.length) {
                    rt.setRequestType(URT_UID_DETAILS);
                    return rt; // GET 1 user
                }
                if (argv[indx].equals("details")) {
                    rt.setRequestType(URT_UID_DETAILS);
                    return rt; // GET 1 user
                }
                if (argv[indx].equals("get_accessories")) {
                    rt.setRequestType(URT_GET_ACCESSORIES);
                    return rt; // GET 1 user										
                }
                if (argv[indx].equals("verify")) {
                    rt.setRequestType(URT_UID_VERIFY);
                    return rt; // GET 1 user										
                }

                // Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
            } catch (NumberFormatException e) {
                System.err.println("Unable to parse " + argv[indx] + " as a user id");
                e.printStackTrace();
            }

            return rt;
        }

        public static RequestTypeUserId setPutRequestType(UriToArgv uta) {

            RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
            int indx = uta.getBaseIndex() + 1;
            String[] argv = uta.getArgv();

            try {
                if (indx >= argv.length) {
                    return rt;
                }
                if (argv[indx].equals("pvp")) {
                    rt.setRequestType(URT_PVP); // PUT
                    return rt;
                }
                if (argv[indx].equals("offline_pvp")) {
                    rt.setRequestType(URT_OFFLINE_PVP); // PUT
                    return rt;
                }

                rt.setUserId(Integer.parseInt(argv[indx]));

                indx++;
                if (indx >= argv.length) {
                    return rt;
                }
                if (argv[indx].equals("characters")) {
                    rt.setRequestType(URT_CHARACTERS); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("sbg")) {
                    rt.setRequestType(URT_SMALL_BAG_OF_GOODIES); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("bog")) {
                    rt.setRequestType(URT_BOX_OF_GOODIES); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("gsg")) {
                    rt.setRequestType(URT_GIANT_SAG_OF_GOODIES); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("msg")) {
                    rt.setRequestType(URT_MASSIVE_SAG_OF_GOODIES); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("hfg")) {
                    rt.setRequestType(URT_HAND_FULL_OF_GOODIES); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("mfg")) {
                    rt.setRequestType(URT_MOUTH_FULL_OF_GOODIES); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("gom")) {
                    rt.setRequestType(URT_GOLD_MINE); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("amn")) {
                    rt.setRequestType(URT_AMNESIA); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("chs")) {
                    rt.setRequestType(URT_CHARACTER_SLOT); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("bre")) {
                    rt.setRequestType(URT_BREAD_STICK); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("bsl")) {
                    rt.setRequestType(URT_BREAD_SLICE); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("blf")) {
                    rt.setRequestType(URT_BREAD_LOAF); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("frb")) {
                    rt.setRequestType(URT_FRESH_BOOTY); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("wrt")) {
                    rt.setRequestType(URT_REFORGE); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("wpn")) {
                    rt.setRequestType(URT_WEAPON); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("sck")) {
                    rt.setRequestType(URT_EXPANDED_SACK); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("nrp")) {
                    rt.setRequestType(URT_NO_RP); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("nad")) {
                    rt.setRequestType(URT_NO_ADS); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("exp")) {
                    rt.setRequestType(URT_EXP_SCROLL); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("pve")) {
                    rt.setRequestType(URT_UID_PVE); // PUT 1 user	
                    return rt;
                }
                if (argv[indx].equals("tokens")) {
                    rt.setRequestType(URT_ADD_TOKENS); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("money")) {
                    rt.setRequestType(URT_ADD_MONEY); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("achievement")) {
                    rt.setRequestType(URT_ACHIEVEMENT); // PUT 1 user
                    return rt;
                }
                // Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
            } catch (NumberFormatException e) {
                System.err.println("Unable to parse " + argv[indx] + " as a user id");
                e.printStackTrace();
            }

            return rt;
        }

        public static RequestTypeUserId setPostRequestType(UriToArgv uta) {

            RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
            int indx = uta.getBaseIndex() + 1;
            String[] argv = uta.getArgv();

            if (indx >= argv.length) {
                return rt; // UNDEFINED
            }

            if (argv[indx].equals("exist")) {
                rt.setRequestType(URT_EXIST); // POST
                return rt;
            }

            if (argv[indx].equals("sign_in")) {
                rt.setRequestType(URT_SIGNIN); //POST
                return rt;
            }

            // Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.

            return rt; // UNDEFINED
        }

        public static RequestTypeUserId setDeleteRequestType(UriToArgv uta) {

            RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
            int indx = uta.getBaseIndex() + 1;
            String[] argv = uta.getArgv();

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
                    rt.setRequestType(URT_DESTROY); // DELETE
                    return rt; // GET 1 user
                }
                if (argv[indx].equals("delete")) {
                    rt.setRequestType(URT_DESTROY); // DELETE
                    return rt; // GET 1 user
                }
                // Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
            } catch (NumberFormatException e) {
                System.err.println("Unable to parse " + argv[indx] + " as a user id");
                e.printStackTrace();
            }

            return rt;
        }
    }

    public static class RequestTypeUserId {
        public RequestTypeUserId(int uid, RequestType type) {
            _user_id = uid;
            _req_type = type;
        }
        void setUserId(int id) { _user_id = id; }
        public int getUserId() { return _user_id; }
        void setRequestType(RequestType type) { _req_type = type; }
        public RequestType getRequestType() { return _req_type; }

        private int _user_id;
        private RequestType _req_type;
    }
}
