package com.cboxgames.idonia.backend.commons;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 11/29/11
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserLogin {
    public String username;
    public String hashed_password;
    public String password;
    public int id;
    public String device_token;

    public static class UserLoginWrapper {
        public UserLogin user;
        public boolean test;
    }
}
