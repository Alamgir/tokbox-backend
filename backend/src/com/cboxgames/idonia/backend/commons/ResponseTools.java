package com.cboxgames.idonia.backend.commons;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.cboxgames.idonia.backend.commons.Constants;

public class ResponseTools {

	public static String ENCODING_JSON = "UTF-8";
	
	/**
	 * Prepare an HttpServletResponse with a JSON body.
	 * 
	 * @param response
	 * @param mapper
	 * @param data
	 * @param code
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void prepareResponseJson(HttpServletResponse response, ObjectMapper mapper, Object data,
			int code) throws JsonGenerationException, JsonMappingException, IOException {

        response.addHeader("Access-Control-Allow-Origin", "http://localhost:83");
		response.setContentType("application/json");
		response.setCharacterEncoding(ENCODING_JSON);
		response.setStatus(code);
		if (data != null)
			mapper.writeValue(response.getWriter(), data);
	}
	
	public static void prepareResponseJsonWithUserID(int user_id, HttpServletResponse response, ObjectMapper mapper, Object data,
			int code) throws JsonGenerationException, JsonMappingException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding(ENCODING_JSON);
		response.setStatus(code);
		if (data != null)
			mapper.writeValue(response.getWriter(), data);
		if (code >= Constants.SC_INTERNAL_SERVER_ERROR) {
			CBoxLoggerSyslog.log("Server_error", "status_code", "message", "user_id", code, (data != null) ? data : "<none>", user_id);
		}
		else if (code >= Constants.SC_BAD_REQUEST) {
			CBoxLoggerSyslog.log("Bad_request", "status_code", "message", "user_id", code, (data != null) ? data : "<none>", user_id);
		}
	}
}
