package com.cboxgames.idonia.backend.commons.db.userpotion;

import com.cboxgames.utils.idonia.types.User;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 3/9/12
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserPotionDB {
    public int buyUserPotion(User user, int potion_id);
    
    public boolean useUserPotion(int user_potion_id);
}
