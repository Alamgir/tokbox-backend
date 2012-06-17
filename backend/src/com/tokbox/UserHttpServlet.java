package com.tokbox;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.authentication.AuthenticateUser;
import com.cboxgames.utils.json.JsonConverter;
import com.tokbox.service.TokBoxOAuth;
import com.tokbox.types.Entity;
import com.tokbox.types.User;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.scribe.builder.api.DropBoxApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.tokbox.UserHttpServlet.RequestType.*;
import static com.tokbox.UserHttpServlet.RequestType.URT_LOGIN;
import static com.tokbox.UserHttpServlet.RequestType.URT_USER_INFO;

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
        _mapper.getDeserializationConfig().addMixInAnnotations(Entity.class, Entity.EntityFilter.class);
        
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        String uri_str = request.getRequestURI();
        UriToArgv uta = new UriToArgv(uri_str, "users");
        if (uta.getBaseIndex() >= uta.getArgv().length) {
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }
        
        RequestTypeUserId ret = setGetRequestType(uta);
        RequestType req_type = ret.getRequestType();
        int user_id = ret.getUserId();

        if (req_type == URT_UNDEFINED)	{
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }

        switch (req_type) {
            case URT_REQUEST_TOKEN: {
                try {
                    Token request_token = TokBoxOAuth.service.getRequestToken();
                    if (request_token != null) {
                        ResponseTools.prepareResponseJson(response, _mapper, request_token,Constants.SC_OK);
                    }
                    else {
                        ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                    }
                }
                catch (OAuthException e) {
                    e.printStackTrace();
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                }

                break;
            }
            case URT_USER_INFO: {
                HttpSession session = request.getSession();
                Token access_token = (Token)session.getAttribute("access_token");

                try {
                    HashMap<String,Object> dp_data_map = new HashMap<String,Object>();
                    dp_data_map.put("access_token", access_token);
                    if (access_token != null) {

                        OAuthRequest user_data_request = new OAuthRequest(Verb.GET, "https://api.dropbox.com/1/account/info");
                        TokBoxOAuth.service.signRequest(access_token, user_data_request);
                        Response user_data_response = user_data_request.send();
                        if (user_data_response.isSuccessful()) {
                            User user_data = _mapper.readValue(user_data_response.getBody(), User.class);
                            dp_data_map.put("user_data", user_data);
                        }

                        OAuthRequest data_request = new OAuthRequest(Verb.GET, "https://api.dropbox.com/1/metadata/dropbox/");
                        TokBoxOAuth.service.signRequest(access_token, data_request);
                        Response data_response = data_request.send();
                        if (data_response.isSuccessful()) {
                            Entity root_data = _mapper.readValue(data_response.getBody(), Entity.class);
                            dp_data_map.put("root_data", root_data);
                        }

                        ResponseTools.prepareResponseJson(response, _mapper,dp_data_map,Constants.SC_OK);

                    }
                    else {
                        ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                    }
                }
                catch (OAuthException e) {
                    e.printStackTrace();
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                }
                break;
            }
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        String uri_str = request.getRequestURI();
        UriToArgv uta = new UriToArgv(uri_str, "users");
        if (uta.getBaseIndex() >= uta.getArgv().length) {
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }

        RequestTypeUserId ret = setPostRequestType(uta);
        RequestType req_type = ret.getRequestType();
        int user_id = ret.getUserId();

        if (req_type == URT_UNDEFINED)	{
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }

        switch (req_type) {
            case URT_ACCESS_TOKEN: {
                
                InputStream stream = request.getInputStream();
                String req_token = "";
                String req_secret = "";
                @SuppressWarnings("unchecked")
                Map<String, Object> auth_data = _mapper.readValue(stream, Map.class);

                if (auth_data.containsKey("request_token")) {
                    req_token = (String)auth_data.get("request_token");
                }
                
                if (auth_data.containsKey("request_secret")) {
                    req_secret = (String)auth_data.get("request_secret");
                }
                Token token = new Token(req_token, req_secret);
                Verifier verifier = new Verifier("");

                try {
                    Token access_token = TokBoxOAuth.service.getAccessToken(token, verifier);
                    HashMap<String,Object> dp_data_map = new HashMap<String,Object>();
                    dp_data_map.put("access_token", access_token);
                    if (access_token != null) {
                        authenticateUser(request, response, access_token);

                        OAuthRequest user_data_request = new OAuthRequest(Verb.GET, "https://api.dropbox.com/1/account/info");
                        TokBoxOAuth.service.signRequest(access_token, user_data_request);
                        Response user_data_response = user_data_request.send();
                        if (user_data_response.isSuccessful()) {
                            User user_data = _mapper.readValue(user_data_response.getBody(), User.class);
                            dp_data_map.put("user_data", user_data);
                        }

                        OAuthRequest data_request = new OAuthRequest(Verb.GET, "https://api.dropbox.com/1/metadata/dropbox/");
                        TokBoxOAuth.service.signRequest(access_token, data_request);
                        Response data_response = data_request.send();
                        if (data_response.isSuccessful()) {
                            Entity root_data = _mapper.readValue(data_response.getBody(), Entity.class);
                            dp_data_map.put("root_data", root_data);
                        }

                        ResponseTools.prepareResponseJson(response, _mapper,dp_data_map,Constants.SC_OK);

                    }
                    else {
                        ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                    }
                }
                catch (OAuthException e) {
                    e.printStackTrace();
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                }
                catch (JsonParseException e) {
                    e.printStackTrace();
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                }

                break;
            }
            case URT_LOGIN: {
                InputStream stream = request.getInputStream();
                String access_token = "";
                String access_secret = "";
                @SuppressWarnings("unchecked")
                Map<String, Object> auth_data = _mapper.readValue(stream, Map.class);

                if (auth_data.containsKey("access_token")) {
                    access_token = (String)auth_data.get("access_token");
                }

                if (auth_data.containsKey("access_secret")) {
                    access_secret = (String)auth_data.get("access_secret");
                }
                Token token = new Token(access_token, access_secret);

                try {
                    if (token != null) {
                        HashMap<String,Object> dp_data_map = new HashMap<String,Object>();
                        dp_data_map.put("access_token", token);


                        OAuthRequest user_data_request = new OAuthRequest(Verb.GET, "https://api.dropbox.com/1/account/info");
                        TokBoxOAuth.service.signRequest(token, user_data_request);
                        Response user_data_response = user_data_request.send();
                        if (user_data_response.isSuccessful()) {
                            User user_data = _mapper.readValue(user_data_response.getBody(), User.class);
                            dp_data_map.put("user_data", user_data);
                        }
                        else {
                            //access token has expired, get a new one
                            ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                            return;
                        }
                        
                        //Dropbox is still responding to the access token
                        //Reauthenticate the user
                        authenticateUser(request, response, token);

                        OAuthRequest data_request = new OAuthRequest(Verb.GET, "https://api.dropbox.com/1/metadata/dropbox/");
                        TokBoxOAuth.service.signRequest(token, data_request);
                        Response data_response = data_request.send();
                        if (data_response.isSuccessful()) {
                            Entity root_data = _mapper.readValue(data_response.getBody(), Entity.class);
                            dp_data_map.put("root_data", root_data);
                        }

                        ResponseTools.prepareResponseJson(response, _mapper,dp_data_map,Constants.SC_OK);

                    }
                    else {
                        ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                        return;
                    }
                }
                catch (OAuthException e) {
                    e.printStackTrace();
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                }
                catch (JsonParseException e) {
                    e.printStackTrace();
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_BAD_REQUEST);
                }
                break;
            }
        }
    }
    
    private static void authenticateUser(HttpServletRequest request, HttpServletResponse response, Token access_token)
        throws ServletException {
        //User is authenticated from dropbox
        //Authenticate the user with TokBox using a cookie
        AuthenticateUser.authenticate(request, response);
        //Store the access_token object in the session
        HttpSession session = request.getSession();
        session.setAttribute("access_token", access_token);
    }

    public static enum RequestType {

        URT_REQUEST_TOKEN, //GET request_token from dropbox </users/req_auth>
        URT_USER_INFO, //GET </users/info>
        URT_ACCESS_TOKEN, //POST </users/auth>
        URT_LOGIN, //POST </users/login>
        
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
                if (argv[indx].equals("info")) {
                    rt.setRequestType(URT_USER_INFO);
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

            if (argv[indx].equals("auth")) {
                rt.setRequestType(URT_ACCESS_TOKEN); // POST
                return rt;
            }
            if (argv[indx].equals("login")) {
                rt.setRequestType(URT_LOGIN); // POST
                return rt;
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
