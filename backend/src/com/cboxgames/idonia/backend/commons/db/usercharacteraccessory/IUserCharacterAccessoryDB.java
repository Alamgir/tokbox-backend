package com.cboxgames.idonia.backend.commons.db.usercharacteraccessory;

import com.cboxgames.utils.idonia.types.Accessory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/12/11
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserCharacterAccessoryDB {
    //Create accessory
    /**
     * Create a user_character_accessory for a user, automatically places it into inventory
     * @param accessory the accessory object that needs to be created
     * @param user_id the user for which the accessory is being created
     * @return the id of the accessory just created in the database
     */
    public int createUserCharacterAccessory(Accessory accessory, int user_id);

    //Get accessory by id
    /**
     * Get an accessory according to its ID
     * @param uca_id the user_character_accessory's id
     * @return a corresponding accessory object
     */
    public Accessory getUserCharacterAccessoryByID(int uca_id);

    //Get accessory by type
    /**
     * Get an array of accessories for a user according to their type. Useful for getting all weapons or accessories for a user
     * @param acc_type the type of accessory being retrieved
     * @param user_id the user's id
     * @return a list of accessories belonging to the user of the corresponding type
     */
    public List<Accessory> getUserCharacterAccessoriesByType(String acc_type, int user_id);

    //Get accessory by user_character_id
    /**
     * Get all the accessories for the corresponding user_character
     * @param uc_id the user_character's id
     * @return a list of accessories for that user_character
     */
    public List<Accessory> getUserCharacterAccessoriesByUserCharacterID(int uc_id);
    
    //Save accessory
    /**
     * Save a user_character_accessory
     * @param accessory the accessory object being saved
     * @return
     */
    public boolean updateUserCharacterAccessory(Accessory accessory);

    //Get Inventory
    /**
     * Get a user's accessory_inventory. Effectively combines the query for accessory by user_id and uc_id of 0
     * @param user_id the user being queried for
     * @return a list of user_character_accessories with user_character_id of 0
     */
    public List<Accessory> getUserCharacterAccessoryInventory(int user_id);

    /**
     * Delete a user_character_accessory when selling
     * @param uca_id user_character_accessory id of the accessory being sold
     * @return
     */
    public boolean sellUserCharacterAccessory(int uca_id);
}
