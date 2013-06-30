package com.huemate;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alamgir
 * Date: 4/9/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class HueLogin {
    public String username;
    public String password;

    public static class HueResponse {
        public HueUser user_data;
        public HueLight.HueLightDataWrapper hue_data;
    }

    public static class HueBridgeLogin {
        public String devicetype;
        public String username;

        public HueBridgeLogin(String start_devicetype, String start_username) {
            username = start_username;
            devicetype = start_devicetype;
        }
    }

}
