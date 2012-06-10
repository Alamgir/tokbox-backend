package com.cboxgames.idonia.backend.test.remote;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.cboxgames.utils.idonia.types.Achievement;
import com.cboxgames.utils.json.JsonConverter;
import com.cboxgames.utils.tools.General;

public class ServerResponse {

	private String json_data;
	private int response_code;
	private Achievement[] achievements;
	
	public ServerResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		
		json_data = "";
		
		try {
			json_data = General.inputStreamToString(entity.getContent());
			
			response_code = response.getStatusLine().getStatusCode();
			
			// Any achievements?
			Header achiev_header = response.getFirstHeader("Achievement");
			
			if (achiev_header != null) {
				achievements = JsonConverter.getInstance().getObject(achiev_header.getValue(), Achievement[].class);
			}
		} catch (IllegalStateException e) {
			// ...
		} catch (IOException e) {
			// ...
		}
	}
	
	public ServerResponse() {
		json_data = "";
		response_code = -1;
	}
	
	/**
	 * Retrieve the JSON data (if any) returned by the request.
	 * 
	 * @return the JSON.
	 */
	public String getJsonData() { return json_data; }
	
	/**
	 * Return the request response code.  -1 if the request failed.
	 * 
	 * @return the response code.
	 */
	public int getResponseCode() { return response_code; }
	
	/**
	 * Retrieve any achievements that may have been included in the response.
	 * Make sure to check containsAchievements() before calling this method.
	 * 
	 * @return any achievements that may have been included in the response.
	 */
	public Achievement[] getAchievements() { return achievements; }
	
	/**
	 * DOes
	 * @return
	 */
	public boolean containsAchievements() { return achievements != null && achievements.length > 0; }
	public boolean containsData() { return !json_data.equals(""); };
}
