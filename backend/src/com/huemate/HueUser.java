package com.huemate;

import java.awt.peer.LightweightPeer;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alamgir
 * Date: 4/9/13
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class HueUser {
    public int id;
    public int group_id;
    public String username;
    public String hashed_pw;
    public ArrayList<HueLight> lights;
    public boolean admin;
    public ArrayList<HueUser> admin_data;
    public boolean approved;
}
