package com.huemate;

import com.cboxgames.idonia.backend.commons.*;
import com.cboxgames.idonia.backend.commons.authentication.AuthenticateUser;
import com.cboxgames.idonia.backend.commons.authentication.BCrypt;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.utils.idonia.types.UserLogin;
import com.cboxgames.utils.json.JsonConverter;
import com.tokbox.graphdb.TokBoxDB;
import com.tokbox.service.TokBoxOAuth;
import com.tokbox.types.Entity;
import com.tokbox.types.User;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.omg.CORBA.ObjectHelper;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/9/12
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserHttpServlet extends HttpServlet {
    private SqlDataSource _sql_data_source;
    private UserDBSQL _user_db_sql;
    private JsonConverter _json_converter;
    private ObjectMapper _mapper;
    
    public void init() {
        _sql_data_source = new SqlDataSource();
        _json_converter = JsonConverter.getInstance();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        _mapper.getDeserializationConfig().addMixInAnnotations(HueLight.HueLightData.class, HueLight.HueLightData.HueLightDataFilter.class);
        _mapper.getDeserializationConfig().addMixInAnnotations(HueLight.HueLightDataState.class, HueLight.HueLightDataState.HueLightDataStateFilter.class);
        try {
            _user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
        
        RequestTypeUserId ret = UserHttpServlet.RequestType.setGetRequestType(uta);
        RequestType req_type = ret.getRequestType();
        int user_id = ret.getUserId();

        if (req_type == UserHttpServlet.RequestType.URT_UNDEFINED)	{
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }

        switch (req_type) {
            case URT_STATUS: {
                try {
                    HueLogin.HueResponse hue_response_data = new HueLogin.HueResponse();

                    HttpSession session = request.getSession();
                    String bridge_url = (String) session.getAttribute("bridge_url");

                    getHueData(bridge_url, hue_response_data);

                    //Write new_user to JSON
                    _mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
                    ResponseTools.prepareResponseJson(response, _mapper, hue_response_data, Constants.SC_OK);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
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

        RequestTypeUserId ret = UserHttpServlet.RequestType.setPostRequestType(uta);
        RequestType req_type = ret.getRequestType();
        int user_id = ret.getUserId();

        if (req_type == UserHttpServlet.RequestType.URT_UNDEFINED)	{
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }

        switch (req_type) {
            case URT_LOGIN: {
                try {
                    //NEED TO GET USERNAME/PASSWORD FROM INPUTSTREAM
                    InputStream stream = request.getInputStream();
                    HueLogin u = _mapper.readValue(stream, HueLogin.class);
                    String username = u.username;
                    String password = u.password;
                    HueGroup group;
                    HueLogin.HueResponse hue_response_data = new HueLogin.HueResponse();
                    group = _user_db_sql.getGroup();

                    //Retrive the bridge no matter what
                    String bridge_ip = retrieveBridge();
                    String bridge_url = "http://" + bridge_ip + "/api";

                    //Query for user here using "username"
                    HueUser user = _user_db_sql.getHueUserByUsername(username);
                    boolean sign_in = true;
                    if (user != null) {
                        if (!BCrypt.checkpw(password, user.hashed_pw)) {
                            ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INCORRECT_PASSWORD, Constants.SC_BAD_REQUEST);
                            return;
                        }
                        //get the group for this user, and by association get the bridge_ip

                        //Set hashed_password to null after it is checked, we don't want to send it in the json
                        user.hashed_pw = null;
                    }
                    else {
                        sign_in = false; // sign_up

                        //If the user is new Auth the user with HUE
//                        HueLogin.HueBridgeLogin login = new HueLogin.HueBridgeLogin("HueMate", group.username);
//
//                        URL url = new URL(bridge_url);
//                        HttpURLConnection api_connection = (HttpURLConnection) url.openConnection();
//                        api_connection.setDoOutput(true);
//                        api_connection.setRequestProperty("Accept-Charset", "UTF-8");
//
//                        OutputStreamWriter output = new OutputStreamWriter(api_connection.getOutputStream());
//                        _mapper.writeValue(output, login);
//
//                        InputStream bridge_response = api_connection.getInputStream();
//                        @SuppressWarnings("unchecked")
//                        Map<String, Object>[] data = _mapper.readValue(bridge_response, Map[].class);
//                        for (Map<String, Object> data_map : data) {
//                            if (data_map.containsKey("error")) {
//                                Map<String,Object> error = (Map<String,Object>) data_map.get("error");
//                                int error_type = (Integer) error.get("type");
//                                if (error_type == 101)
//                                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.LINK_BUTTON_NOT_PRESSED, Constants.SC_BAD_REQUEST);
//                                else if (error_type == 7)
//                                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USERNAME_TOO_SHORT, Constants.SC_BAD_REQUEST);
//                                return;
//                            }
//                        }


                        //Create a new user with BCrypt hashed password
                        //Use this line of code to generate password from plain text: String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt(12));
                        user = _user_db_sql.createHueUser(username, password);

                    }

                    if (user == null) {
                        response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        return;
                    }

                    bridge_url = bridge_url + "/" + group.username;
                    AuthenticateUser.authenticate(request, response);
                    //store user_id in a session attribute
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", user.id);
                    session.setAttribute("bridge_url", bridge_url);

                    //Set the admin status
                    if (user.admin) {
                        session.setAttribute(AuthenticateUser.USER_ADMIN_KEY, true);
                        session.setMaxInactiveInterval(-1);

                        //pull the admin_data
                        user.admin_data = _user_db_sql.getAdminDatabyAdmin(user);
                    }
                    else {
                        //A negative value for setMaxInactiveInterval causes the session to never expire
                        session.setMaxInactiveInterval(Constants.MAX_SESSION_INTERVAL_ONE_DAY);
                    }


                    getHueData(bridge_url, hue_response_data);
                    //finally write the user to the Hue Response
                    hue_response_data.user_data = user;

                    //Write new_user to JSON
                    _mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
                    ResponseTools.prepareResponseJson(response, _mapper, hue_response_data, Constants.SC_OK);
                    //if (!u.test) CBoxLoggerSyslog.log("sign_in","user_id",user.id);
                } catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
                break;
            }
            case URT_ADMIN_SET_APPROVAL: {
                try {
                    if (AuthenticateUser.isAdmin(request)) {
                        //NEED TO GET user_id FROM INPUTSTREAM
                        InputStream stream = request.getInputStream();
                        @SuppressWarnings("unchecked")
                        HueLight.HueLightApprovalWrapper hue_light_approval  = _mapper.readValue(stream, HueLight.HueLightApprovalWrapper.class);

                        //pull the user and assign the light approval state to them
                        HueUser user = _user_db_sql.getHueUserByID(user_id);
                        user.lights = hue_light_approval.lights_approval;
                        //save the user
                        if (_user_db_sql.saveHueUser(user)) {
                            //pass a success back
                            response.setStatus(Constants.SC_OK);
                        }
                        else response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    }
                    else response.setStatus(Constants.SC_UNAUTHORIZED);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException f) {
                    f.printStackTrace();
                }
                break;
            }
            case URT_SET_LIGHT_STATE: {
                try {
                    InputStream stream = request.getInputStream();
                    HueLight.HueLightDataWrapper light_state_request = _mapper.readValue(stream, HueLight.HueLightDataWrapper.class);

                    HttpSession session = request.getSession();
                    String bridge_url = (String) session.getAttribute("bridge_url");

                    //get the user, iterate through the light_state and check each id with the user's lights
                    HueUser user = _user_db_sql.getHueUserByID(user_id);

                    ArrayList<Integer> approved_light_ids = new ArrayList<Integer>();
                    for (HueLight user_light : user.lights) {
                        if (user_light.user_approved) approved_light_ids.add(user_light.id);
                    }

                    boolean successful_bridge_request = true;
                    for (HueLight.HueLightData light_data : light_state_request.lights) {
                        if (approved_light_ids.contains(light_data.id) || user.admin) {
                            //make a PUT request to the bridge to set the state using the HueLightDataState
                            successful_bridge_request = setLightState(light_data, bridge_url);
                        }
                    }

                    if (successful_bridge_request) {
                        response.addHeader("Access-Control-Allow-Origin", "http://localhost:83");
                        response.addHeader("Access-Control-Allow-Credentials", "true");
                        response.setStatus(Constants.SC_OK);
                    }
                    else response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException f) {
                    f.printStackTrace();
                }
                break;
            }
        }
    }

    private void getHueData(String bridge_url, HueLogin.HueResponse hue_response_data) throws IOException {
        //This will only get the light IDs
        URL url = new URL(bridge_url + "/lights/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        //Create the HueLightDataWrapper object
        HueLight.HueLightDataWrapper huelight_wrapper = new HueLight.HueLightDataWrapper();
        ArrayList<HueLight.HueLightData> huelight_data_list = new ArrayList<HueLight.HueLightData>();

        InputStream url_stream = connection.getInputStream();
        @SuppressWarnings("unchecked")
        Map<String, Object> light_data_map = _mapper.readValue(url_stream, Map.class);
        //for each light id, look up the light state
        Set<String> keys = light_data_map.keySet();
        for (String key : keys) {
            int light_id = Integer.parseInt(key);
            //get the light's state
            URL light_url = new URL(bridge_url + "/lights/" + light_id);
            HttpURLConnection light_connection = (HttpURLConnection) light_url.openConnection();
            light_connection.setRequestMethod("GET");
            light_connection.connect();

            InputStream light_stream = light_connection.getInputStream();
            @SuppressWarnings("unchecked")
            HueLight.HueLightData light = _mapper.readValue(light_stream, HueLight.HueLightData.class);
            light.id = light_id;
            huelight_data_list.add(light);
        }

        huelight_wrapper.lights = huelight_data_list;
        hue_response_data.hue_data = huelight_wrapper;
    }

    private boolean setLightState (HueLight.HueLightData light_data, String bridge_url) throws IOException {
        URL url = new URL(bridge_url + "/lights/" + light_data.id + "/state");
        HttpURLConnection api_connection = (HttpURLConnection) url.openConnection();
        api_connection.setDoOutput(true);
        api_connection.setRequestMethod("PUT");
        api_connection.setRequestProperty("Accept-Charset", "UTF-8");

        OutputStreamWriter output = new OutputStreamWriter(api_connection.getOutputStream());
        _mapper.writeValue(output, light_data.state);

        InputStream bridge_response = api_connection.getInputStream();
        @SuppressWarnings("unchecked")
        Map<String, Object>[] data = _mapper.readValue(bridge_response, Map[].class);
        for (Map<String, Object> data_map : data) {
            if (data_map.containsKey("error")) {
                return false;
            }
        }
        return true;
    }

    private String retrieveBridge() throws MalformedURLException, IOException{
        //Look up the Bridge IP on the network
        URL url = new URL("http://www.meethue.com/api/nupnp");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        InputStream url_stream = connection.getInputStream();
        @SuppressWarnings("unchecked")
        Map<String, Object>[] data = _mapper.readValue(url_stream, Map[].class);
        Map<String, Object> data_map = data[0];

        if ((String)data_map.get("internalipaddress") != null) {
             return (String)data_map.get("internalipaddress");
        }
        else return null;
    }

    public static enum RequestType {
        URT_LOGIN, //POST </users/login>
        URT_DETAILS,
        URT_UID_DETAILS,
        URT_STATUS, //GET </users/status> the status of the bridge
        URT_ADMIN_SET_APPROVAL, //POST </users/approval> set the status for a user by the admin
        URT_SET_LIGHT_STATE, //POST </users/light_state> set the state for lights on the HUE
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
                if (argv[indx].equals("status")) {
                    rt.setRequestType(URT_STATUS);
                    return rt;
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
//                if (argv[indx].equals("pvp")) {
//                    rt.setRequestType(URT_PVP); // PUT
//                    return rt;
//                }
//                if (argv[indx].equals("offline_pvp")) {
//                    rt.setRequestType(URT_OFFLINE_PVP); // PUT
//                    return rt;
//                }

                rt.setUserId(Integer.parseInt(argv[indx]));

                indx++;
                if (indx >= argv.length) {
                    return rt;
                }
//                if (argv[indx].equals("characters")) {
//                    rt.setRequestType(URT_CHARACTERS); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("sbg")) {
//                    rt.setRequestType(URT_SMALL_BAG_OF_GOODIES); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("bog")) {
//                    rt.setRequestType(URT_BOX_OF_GOODIES); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("gsg")) {
//                    rt.setRequestType(URT_GIANT_SAG_OF_GOODIES); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("msg")) {
//                    rt.setRequestType(URT_MASSIVE_SAG_OF_GOODIES); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("hfg")) {
//                    rt.setRequestType(URT_HAND_FULL_OF_GOODIES); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("mfg")) {
//                    rt.setRequestType(URT_MOUTH_FULL_OF_GOODIES); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("gom")) {
//                    rt.setRequestType(URT_GOLD_MINE); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("amn")) {
//                    rt.setRequestType(URT_AMNESIA); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("chs")) {
//                    rt.setRequestType(URT_CHARACTER_SLOT); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("bre")) {
//                    rt.setRequestType(URT_BREAD_STICK); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("bsl")) {
//                    rt.setRequestType(URT_BREAD_SLICE); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("blf")) {
//                    rt.setRequestType(URT_BREAD_LOAF); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("frb")) {
//                    rt.setRequestType(URT_FRESH_BOOTY); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("wrt")) {
//                    rt.setRequestType(URT_REFORGE); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("wpn")) {
//                    rt.setRequestType(URT_WEAPON); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("sck")) {
//                    rt.setRequestType(URT_EXPANDED_SACK); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("nrp")) {
//                    rt.setRequestType(URT_NO_RP); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("nad")) {
//                    rt.setRequestType(URT_NO_ADS); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("exp")) {
//                    rt.setRequestType(URT_EXP_SCROLL); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("pve")) {
//                    rt.setRequestType(URT_UID_PVE); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("tokens")) {
//                    rt.setRequestType(URT_ADD_TOKENS); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("money")) {
//                    rt.setRequestType(URT_ADD_MONEY); // PUT 1 user
//                    return rt;
//                }
//                if (argv[indx].equals("achievement")) {
//                    rt.setRequestType(URT_ACHIEVEMENT); // PUT 1 user
//                    return rt;
//                }
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
            else if (argv[indx].equals("login")) {
                rt.setRequestType(URT_LOGIN); // POST
                return rt;
            }

            rt.setUserId(Integer.parseInt(argv[indx]));

            indx++;
            if (indx >= argv.length) {
                return rt;
            }
            else if (argv[indx].equals("approve_user")) {
                rt.setRequestType(URT_ADMIN_SET_APPROVAL); // POST
                return rt;
            }
            else if (argv[indx].equals("set_light_state")) {
                rt.setRequestType(URT_SET_LIGHT_STATE); // POST
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
//                if (argv[indx].equals("destroy")) {
//                    rt.setRequestType(URT_DESTROY); // DELETE
//                    return rt; // GET 1 user
//                }
//                if (argv[indx].equals("delete")) {
//                    rt.setRequestType(URT_DESTROY); // DELETE
//                    return rt; // GET 1 user
//                }
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
