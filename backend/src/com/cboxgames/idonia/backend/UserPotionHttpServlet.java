package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.*;

import static com.cboxgames.idonia.backend.UserPotionHttpServlet.RequestType.*;
import static com.cboxgames.idonia.backend.UserPotionHttpServlet.RequestType.URT_REFRESH;

import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.idonia.backend.commons.db.userpotion.UserPotionDBSQL;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserPotion;
import com.cboxgames.utils.idonia.types.UserPotion.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 3/9/12
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserPotionHttpServlet extends HttpServlet {
    private SqlDataSource _sql_data_source;
    private UserPotionDBSQL _up_db_sql;
    private UserDBSQL _u_db_sql;
    private ObjectMapper _mapper;
    
    public void init() {
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        
        try {
            _up_db_sql = new UserPotionDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _u_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri_str = request.getRequestURI();
        UriToArgv uta = new UriToArgv(uri_str, "user_potions");
        if (uta.getBaseIndex() >= uta.getArgv().length) {
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }

        RequestType req_type = setPutRequestType(uta).getRequestType();
        if (req_type == URT_UNDEFINED)	{
            ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
                                              Constants.SC_BAD_REQUEST);
            return;
        }
        
        switch (req_type) {
            case URT_BUY: {
                InputStream stream = request.getInputStream();
                UserPotionRequest up_request = _mapper.readValue(stream, UserPotionRequest.class);
                int potion_id = up_request.potion_id;

                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");
                
                User user = _u_db_sql.getUserByID(user_id);
                int user_potion_id = _up_db_sql.buyUserPotion(user, potion_id);
                if (user_potion_id != 0) {
                    up_request.user_potion_id = user_potion_id;
                    up_request.bought_at = System.currentTimeMillis();
                    ResponseTools.prepareResponseJson(response, _mapper, up_request, HttpServletResponse.SC_OK);
                }
                else {
                    response.sendError(400, ResponseMessages.INSUFFICIENT_MONEY);
                }
                break;
            }
            case URT_USE: {
                InputStream stream = request.getInputStream();
                UserPotionRequest up_request = _mapper.readValue(stream, UserPotionRequest.class);
                int user_potion_id = up_request.user_potion_id;

                if (_up_db_sql.useUserPotion(user_potion_id)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_potion_id","unsuccessful_up_use",user_potion_id);
                }

                break;
            }
            case URT_REFRESH: {
                InputStream stream = request.getInputStream();
                UserPotionRequest up_request = _mapper.readValue(stream, UserPotionRequest.class);
                int user_potion_id = up_request.user_potion_id;

                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");

                User user = _u_db_sql.getUserByID(user_id);
                int refresh_time = _up_db_sql.refreshUserPotion(user, user_potion_id, up_request.battle);
                if (refresh_time != 0) {
                    ResponseTools.prepareResponseJson(response, _mapper, refresh_time, HttpServletResponse.SC_OK);
                }
                else {
                    response.sendError(400, ResponseMessages.INSUFFICIENT_MONEY);
                }

                break;
            }
            default: {
                throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
            }
        }
    }

    public static enum RequestType {

        URT_BUY,
        URT_USE,
        URT_REFRESH,
        URT_UNDEFINED;

        public static RequestTypeUserId setPutRequestType(UriToArgv uta) {

            RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
            int indx = uta.getBaseIndex() + 1;
            String[] argv = uta.getArgv();

            if (indx >= argv.length) {
                rt.setRequestType(URT_UNDEFINED); // user_character_accessories/****
                return rt;
            }
            if (argv[indx].equals("buy")) {
                rt.setRequestType(URT_BUY); // PUT 1 user
                return rt;
            }
            if (argv[indx].equals("use")) {
                rt.setRequestType(URT_USE);
                return rt;
            }
            if (argv[indx].equals("refresh")) {
                rt.setRequestType(URT_REFRESH);
                return rt;
            }

            // Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
            return rt;
        }

    }

    public static class RequestTypeUserId {
        public RequestTypeUserId(int aid, RequestType type) {
            _acc_id = aid;
            _req_type = type;
        }
        void setAccId(int id) { _acc_id = id; }
        public int getAccId() { return _acc_id; }
        void setRequestType(RequestType type) { _req_type = type; }
        public RequestType getRequestType() { return _req_type; }

        private int _acc_id;
        private RequestType _req_type;
    }
}
