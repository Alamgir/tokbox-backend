package com.cboxgames.idonia.backend.test.tomcat;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.utils.idonia.types.Accessory.AccessoryWrapper;
import com.cboxgames.utils.tools.General;


public class TestAccessoryServlet extends HttpTest {

	private static String _accessory_uri = "/accessories/details"; // HTTP Method: GET
	
	public TestAccessoryServlet(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
			// ServerResponse response_login = remote_data.tomcat_authenticate(IdoniaBackendTest.get_user_name(), IdoniaBackendTest.get_password());

			// UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response_login.getJsonData(), UserWrapper.class);
			
			// assert uw != null : "Returned User object from login was null.  Our model is probably" +
			//		"missing some fields.";
			
			HttpGet get = new HttpGet(new URI(IdoniaBackendTest.TOMCAT_SERVER_ROOT + _accessory_uri));
			get.setHeader("Accept", "application/json");
			get.setHeader("Content-Type", "application/json");
			
			HttpResponse response = http_client.execute(get);
			HttpEntity entity = response.getEntity();
			String data = General.inputStreamToString(entity.getContent());
			Class<AccessoryWrapper[]> class_type = AccessoryWrapper[].class;
			AccessoryWrapper[] unw = IdoniaBackendTest.json_converter.getObject(data, class_type);
			
			// Do some checking here.
			assert unw != null : "Returned Accessory object was null.  Our model is probably missing some fields.";
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}