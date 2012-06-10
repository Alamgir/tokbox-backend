package com.cboxgames.idonia.backend.commons.db.usercharacterskill;

import com.cboxgames.utils.idonia.types.Skill;
import com.cboxgames.utils.idonia.types.Skill.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/12/11
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserCharacterSkillDB {
    //Create skill for user_character for buying
    public int createUserCharacterSkill(SkillBuy skill_buy, boolean in_use);

    //Switch skill in_use to true
    public boolean putUserCharacterSkillsInUse(List<Integer> skill_ids);

    //Set all skills for a user_character to false
    public boolean setAllUserCharacterSkillsFalse(int user_character_id);

    //Get all the user_character_skills for a given character
    public List<Skill> getAllSkillsForUserCharacter(int user_character_id);
    
    //Check for user_character_skill
    public boolean checkUserCharacterSkill(int user_character_id, int skill_id);
}
