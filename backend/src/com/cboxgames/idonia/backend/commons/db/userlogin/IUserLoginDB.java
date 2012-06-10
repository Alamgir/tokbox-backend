package com.cboxgames.idonia.backend.commons.db.userlogin;

import com.cboxgames.utils.idonia.types.UserLogin;
/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 11/29/11
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserLoginDB {
    /**
     * Get a User's credentials by their username. This is used to check
     * 1) if the user exists and
     * 2) get the user's hashed password to authenticate them
     * @param username The user's username String
     * @return A UserLogin object with set parameters for username and hashed_password
     */
    public UserLogin getUserCred(String username);
}
