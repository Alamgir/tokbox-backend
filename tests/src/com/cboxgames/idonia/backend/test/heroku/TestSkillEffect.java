package com.cboxgames.idonia.backend.test.heroku;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.User.UserWrapper;
import com.cboxgames.utils.idonia.types.SkillEffect.SkillEffectWrapper;
import com.cboxgames.utils.tools.General;

public class TestSkillEffect extends HttpTest {
	private static String _skill_effect_uri = "/skill_effects/details"; // HTTP Method: GET
	
	public TestSkillEffect(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
			ServerResponse response_login = remote_data.authenticate(IdoniaBackendTest.get_user_name(), IdoniaBackendTest.get_password());

			UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response_login.getJsonData(), UserWrapper.class);
			
			assert uw != null : "Returned skill_effect object from login was null.  Our model is probably" +
					"missing some fields.";
			
			HttpGet get = new HttpGet(new URI(IdoniaBackendTest.SERVER_ROOT + _skill_effect_uri));
			get.setHeader("Accept", "application/json");
			get.setHeader("Content-Type", "application/json");
			
			HttpResponse response = http_client.execute(get);
			HttpEntity entity = response.getEntity();
			
			String data = General.inputStreamToString(entity.getContent());

			Class<SkillEffectWrapper[]> class_type = SkillEffectWrapper[].class;
			SkillEffectWrapper[] unw = IdoniaBackendTest.json_converter.getObject(data, class_type);
			
			// Do some checking here.
			assert unw != null : "Returned skill_effect object was null.  Our model is probably missing some fields.";
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}