package com.cboxgames.idonia.backend.commons.db.usersoul;

import java.sql.Connection;

import com.cboxgames.utils.idonia.types.User;


/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 1/11/12
 * Time: 8:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserSoulDB {
	
	public boolean createUserSoul(User user, Connection conn);
	
    public boolean depositUserSoul(User user, int soul_id);

    public boolean collectUserSoul(int user_id, int user_soul_id);

}
