package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.*;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.idonia.backend.commons.db.usersoul.UserSoulDBSQL;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserSoul.UserSoulRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import static com.cboxgames.idonia.backend.UserSoulHttpServlet.RequestType.*;


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
 * Date: 1/11/12
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserSoulHttpServlet extends HttpServlet{
    private SqlDataSource _sql_data_source;
    private UserSoulDBSQL _us_db_sql;
    private UserDBSQL _user_db_sql;
    private ObjectMapper _mapper;

    public void init() {
    	
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        try {
            _us_db_sql = new UserSoulDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_souls");
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
            case URT_COLLECT: {
                InputStream stream = request.getInputStream();
                UserSoulRequest us_request = _mapper.readValue(stream, UserSoulRequest.class);
                int user_soul_id = us_request.user_soul_id;

                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");

                if (_us_db_sql.collectUserSoul(user_id, user_soul_id)) {
                    response.setStatus(Constants.SC_OK);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_us_collection",user_id);
                }
                break;
            }
            case URT_DEPOSIT: {
                InputStream stream = request.getInputStream();
                UserSoulRequest us_request = _mapper.readValue(stream, UserSoulRequest.class);
                int soul_id = us_request.soul_id;

                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");

                User user = _user_db_sql.getUserOnlyByID(user_id);
                if (_us_db_sql.depositUserSoul(user, soul_id)) {
                    PrintWriter wrt = response.getWriter();
                    wrt.print(System.currentTimeMillis());
                    response.setStatus(Constants.SC_OK);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_us_deposit",user_id);
                }
                break;
            }
            default: {
                throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
	   		}
        }
    }

    public static enum RequestType {

    	URT_COLLECT,
    	URT_DEPOSIT,
    	URT_UNDEFINED;

    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {

        	RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

            if (indx >= argv.length) {
                rt.setRequestType(URT_UNDEFINED); // user_character_accessories/****
                return rt;
            }
            if (argv[indx].equals("deposit")) {
                rt.setRequestType(URT_DEPOSIT); // PUT 1 user
                return rt;
            }
            if (argv[indx].equals("collect")) {
                rt.setRequestType(URT_COLLECT);
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
