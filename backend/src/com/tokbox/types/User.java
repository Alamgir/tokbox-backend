package com.tokbox.types;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 6/10/12
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class User {
    public String referral_link;
    public String display_name;
    public long uid;
    public String country;
    public HashMap<String,Long> quota_info;
    public String email;
    public String gravatar_url;
    public ArrayList<User> team_members;
}
