package com.cboxgames.idonia.backend.commons.db.userquest;

import com.cboxgames.utils.idonia.types.UserQuest;
import com.cboxgames.utils.idonia.types.UserQuest.*;
import com.cboxgames.idonia.backend.commons.db.userquest.UserQuestDBSQL.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/18/11
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserQuestDB {
    /**
     * Retrieve a user's list of quests for the current 24 hour period
     * @param user_id
     * @return
     */
    public ResponseGetUserQuests getUserQuests(int user_id);

    /**
     * Save (override) the given user's list of quests for the current 24 hour period
     * @param user_id
     * @param user_quests
     * @return
     */
    public boolean saveUserQuests(int user_id, List<UserQuestWrapper> user_quests);
}
