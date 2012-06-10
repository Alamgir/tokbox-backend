package com.cboxgames.idonia.backend.test.remote;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.cboxgames.utils.http.messages.idonia.MessageLogin;
import com.cboxgames.utils.http.messages.idonia.MessageLogin.MessageLoginWrapper;
import com.cboxgames.utils.idonia.types.UserLogin;
import com.cboxgames.utils.idonia.types.UserLogin.UserLoginWrapper;
import com.cboxgames.utils.idonia.types.CharacterNew;
import com.cboxgames.utils.idonia.types.CharacterNew.CharacterNewAttribute;
import com.cboxgames.utils.idonia.types.CharacterNew.CharacterNewWrapper;

import com.cboxgames.utils.json.JsonConverter;

public class RemoteData {

	public static final String SERVER_IP = "http://savage.heroku.com";
	public static final String TOMCAT_SERVER_IP = "http://localhost:80/IdoniaBackend";
	public static final String SERVER_CONTENT_TYPE = "application/json";
	public static final String SERVER_ACCEPT_TYPE = "application/json";
	public static final String ADMIN_USERNAME = "alamgir";
	public static final String ADMIN_PASSWORD = "aaaaaa";

	/* An HTTP client to perform HTTP calls. */
	protected DefaultHttpClient http_client;
	
	/* An object to store the current session's cookies. */
	protected BasicCookieStore cookie_store;
	
	protected JsonConverter json_converter;
	
	public RemoteData(int port_num) {
		SchemeRegistry scheme_registry = new SchemeRegistry();
		scheme_registry.register(new Scheme("http", port_num, PlainSocketFactory.getSocketFactory()));
		ClientConnectionManager cm = new SingleClientConnManager(scheme_registry);
		http_client = new DefaultHttpClient(cm);
		cookie_store = new BasicCookieStore();
		http_client.setCookieStore(cookie_store);
		
		json_converter = JsonConverter.getInstance();
	}
	
	/**
	 * Perform an HTTP POST.
	 * 
	 * @param url the URL to POST to.
	 * @param object the object to serialize and send.
	 * 
	 * @return the result of the POST.
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public ServerResponse POSTJson(String url, Object object) throws JsonGenerationException,
				JsonMappingException, IOException {
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", SERVER_CONTENT_TYPE);
		post.addHeader("Accept", SERVER_ACCEPT_TYPE);
		
		String json = json_converter.getJson(object);
		
		StringEntity body = new StringEntity(json, "UTF-8");
		post.setEntity(body);
		
		HttpResponse response = http_client.execute(post);
		
		ServerResponse serverResponse = new ServerResponse(response);
		
		return serverResponse;
	}
	
	/**
	 * Perform an HTTP PUT.
	 * 
	 * @param url the url to PUT to.
	 * @param object the object to serialize.
	 * 
	 * @return the result of the PUT.
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public ServerResponse PUTJson(String url, Object object) throws JsonGenerationException,
					JsonMappingException, IOException {
		HttpPut put = new HttpPut(url);
		put.addHeader("Content-Type", SERVER_CONTENT_TYPE);
		put.addHeader("Accept", SERVER_ACCEPT_TYPE);
		
		if (object != null) {
			String json = json_converter.getJson(object);
			
			StringEntity body = new StringEntity(json, "UTF-8");
			put.setEntity(body);
		}
		
		HttpResponse response = http_client.execute(put);
		
		ServerResponse serverResponse = new ServerResponse(response);
		
		return serverResponse;
	}
	
	/**
	 * Perform an HTTP GET.
	 * 
	 * @param url the url to GET from.
	 * 
	 * @return the result of the GET.
	 * 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ServerResponse GETJson(String url) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		get.addHeader("Accept", SERVER_ACCEPT_TYPE);
		
		HttpResponse response = http_client.execute(get);
		
		ServerResponse serverResponse = new ServerResponse(response);
		
		return serverResponse;
	}
	
	/**
	 * Perform an HTTP DELETE.
	 * 
	 * @param url the url to DELETE.
	 * 
	 * @return the result of the DELETE.
	 * 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ServerResponse DELETEJson(String url) throws ClientProtocolException, IOException {
		HttpDelete get = new HttpDelete(url);
		get.addHeader("Accept", SERVER_ACCEPT_TYPE);
		
		HttpResponse response = http_client.execute(get);
		
		ServerResponse serverResponse = new ServerResponse(response);
		
		return serverResponse;
	}
	
	/**
	 * Authenticate this session.
	 * 
	 * @param user_name the user_name to login with.
	 * @param password the password to login with.
	 * 
	 * @return the result of the authentication.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public ServerResponse authenticate(String user_name, String password) throws JsonGenerationException,
					JsonMappingException, IOException {
		MessageLogin login = new MessageLogin(user_name, password);
		this.clearCookies();
		
		ServerResponse serverResponse = POSTJson(RemoteData.TOMCAT_SERVER_IP + "/users"
				+ "/sign_in", new MessageLoginWrapper(login));		
		
		return serverResponse;
	}
	
	public ServerResponse tomcat_authenticate(String user_name, String password) throws JsonGenerationException,
		JsonMappingException, IOException {
		UserLogin login = new UserLogin();
		login.username = user_name;
		login.password = password;
		UserLoginWrapper ulw = new UserLoginWrapper();
		ulw.user = login;
		this.clearCookies();

		ServerResponse serverResponse = POSTJson(RemoteData.TOMCAT_SERVER_IP + "/users/sign_in", ulw);		

		return serverResponse;
	}
	
	/**
	 * Create new characters for the currently logged in user.
	 * 
	 * @param ids
	 * @param in_lineup
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public ServerResponse createUserCharacters(int[] ids, boolean in_lineup) throws JsonGenerationException,
					JsonMappingException, IOException {
		CharacterNew char_new = new CharacterNew(ids, in_lineup, new CharacterNewAttribute[0]);
		
		return PUTJson(TOMCAT_SERVER_IP, new CharacterNewWrapper(char_new));
	}
	
	/**
	 * Clear all cookies for this session.
	 */
	public void clearCookies() {
		this.http_client.getCookieStore().clear();
	}
	
	/**
	 * Logout, login as an admin, and delete a user.
	 * 
	 * After this operation, the remote data client will be left logged in as an admin.
	 * 
	 * @param user_id
	 * @return weather the deletion was successful.
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public boolean deleteUserAsAdmin(int user_id) throws JsonGenerationException, 
						JsonMappingException, IOException {
		this.clearCookies();
		
		ServerResponse response = this.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD);
		if (response.getResponseCode() == 200) {
			response = this.DELETEJson(TOMCAT_SERVER_IP + "/users/" + user_id);
			if (response.getResponseCode() == 200)
				return true;
		}
		
		return false;
		
	}
	
	/**
	 * Purchase an item.  It will PUT to /users/{id}/{item} with a null body.
	 * 
	 * @param id
	 * @param item
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public ServerResponse purchaseItem(int id, String item) throws JsonGenerationException,
						JsonMappingException, IOException {
		return this.PUTJson(TOMCAT_SERVER_IP + "/users/" + id + "/" + item, "smd");
	}
}
