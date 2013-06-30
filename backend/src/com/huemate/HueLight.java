package com.huemate;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alamgir
 * Date: 4/9/13
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class HueLight {
    public int id;
    public boolean user_approved;

    public static class HueLightDataWrapper {
        public ArrayList<HueLightData> lights;
    }

    public static class HueLightData {
        public int id;
        public HueLightDataState state;
        public String type;
        public String name;

        @JsonIgnoreProperties({"modelid","swversion","pointsymbol"})
        public static interface HueLightDataFilter {}
    }

    public static class HueLightDataState {
        public int hue;
        public boolean on;
        public String effect;
        public String alert;
        public int bri;
        public int sat;
        public int ct;
        public float[] xy;

        @JsonIgnoreProperties({"reachable","colormode"})
        public static interface HueLightDataStateFilter {}
    }

    public static class HueLightApprovalWrapper {
        public ArrayList<HueLight> lights_approval;
    }
}
