package com.cboxgames.idonia.backend.commons.db.usercharacter;

import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest.*;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Character.CharacterWrapper;
import com.cboxgames.utils.idonia.types.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/4/11
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserCharacterDB {
    /**
     * Create User Characters by taking in an array of integers (Character IDs) and the user_id for which to create them.
     * @param character_ids
     * @param user_id
     * @return
     */
    public boolean createUserCharacters(List<Integer> character_ids, int user_id);

    /**
     * Get a User's Characters in an array in order to either modify them or to retrieve info from them
     * @param user_id
     * @return
     */
    public List<CharacterWrapper> getAllUserCharactersByUserID(int user_id);
    
    /**
     * Get all user characters with fields (name and level) only
     * @param user_id
     * @return
     */
    public List<Character> getUserCharactersForShop(int user_id);
    
    /**
     * Get all user characters specified by uc_ids
     * @param uc_ids: a list of user_character ids
     * @return
     */
    public List<Character> getUserCharacters(UserCharacterUpdate[] ucu_requests);

    /**
     * Save an array of UserCharacters, this method is intended for /user_characters/update_all
     * @param character_array
     * @return
     */
    public boolean saveUserCharacters(List<Character> character_array);

    /**
     * Save a list of UserCharacters and their stats. Does not save accessories/skills.
     * @param character_array
     * @return
     */
    public boolean saveUserCharactersStats(List<Character> character_array);

    /**
     * Get a single UserCharacter object according to a user_character_id
     * @param user_character_id the user_character_id being queried
     * @return
     */
    public CharacterWrapper getUserCharacterByID(int user_character_id);

    /**
     * Set weapon_swap to true
     * @param user_char_id: User character id
     * @return
     */
    public boolean updateUserCharacterWeaponSwap(int user_char_id);
    
    /**
     * Update user character stats, ...
     * @param user_char_list
     * @param ucu_requests
     * @return
     */
    public boolean updateUserCharacterAll(User user, UserCharacterUpdateRequest ucu_request);
}
