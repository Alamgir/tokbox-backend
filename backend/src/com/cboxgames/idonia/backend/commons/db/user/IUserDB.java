package com.cboxgames.idonia.backend.commons.db.user;

import com.cboxgames.utils.idonia.types.User;

import java.util.List;

public interface IUserDB {
    /**
     * Create the actual User and insert columns with data
     *
     * @param username the user's username
     * @param password the user's password
     * @param device_token the user's device_token if they have one
     *
     * @return whether the operation was successful
     */
    public User createUser(String username, String password, String device_token, String udid, String mac_address);

    /**
     * Get the user by their id
     * 
     *@param user_id the user's id
     *
     * @return the user model
     */
    public User getUserByID(int user_id);

    /**
     * Get the user's basic info by their user_id, no characters, nodes, achievements, or analytics
     * Access users and user_id_name_map tables
     * @param user_id - the user's id
     * @return
     */
    public User getUserInfoByID(int user_id);
    
    /**
     * Get user info from the users table only
     * @param user_id
     * @return
     */
    public User getUserOnlyByID(int user_id);

    /**
     * Boolean function that saves a User object's main attributes. Does not save the User's array
     * attributes 
      * @param user
     * @return
     */
    public boolean saveUser(User user);

    /**
     * Get a user according to their username
     * @param username the username for the user in question
     * @return a User object if the user exists
     */
    public User getUserByUsername(String username);
    
    /**
     * update one User field in db
     * @param value: A number to update the specified field in db
     * @param fieldname: A user field name
     * @param user_id: User id
     * @return
     */
    public boolean updateUserOneField(int value, String fieldname, int user_id);
    
    /**
     * Update two user fields
     * @param value1
     * @param fieldname1
     * @param value2
     * @param fieldname2
     * @param user_id
     * @return
     */
    public boolean updateUserTwoFields(int value1, String fieldname1, int value2, String fieldname2, int user_id);

    /**
     * Save the number of tokens for the user
     * @param tokens Positive if you want to add tokens, negative if you want to subtract tokens
     * @param user_id the user's id
     * @return
     */
    public int saveTokens(int tokens, int user_id);

    /**
     * Save the amount of money for the user
     * @param money positive if you want to add money, negative if you want to subtract money
     * @param user_id the user's id
     * @return
     */
    public int saveMoney(int money, int user_id);

    /**
     * Save the number of character slots for the user
     * @param slots positive if you want to add slots, negative if you want to subtract slots
     * @param user_id the user's id
     * @return
     */
    public int saveCharacterSlots(int slots, int user_id);

    /**
     * Save the number of breadsticks for the user
     * @param breadstick positive if you want to add breadsticks, negative if you want to take them away
     * @param user_id the user's id
     * @return
     */
    public int saveBreadStick(int breadstick, int user_id);

    /**
     * Save the number of bread slices for the user
     * @param bread_slice positive if you want to add bread slices, negative if you want to take them away
     * @param user_id the user's id
     * @return
     */
    public int saveBreadSlice(int bread_slice, int user_id);

    /**
     * Save the number of bread loaves for the user
     * @param bread_loaf positive if you want to add bread slices, negative if you want to take them away
     * @param user_id the user's id
     * @return
     */
    public int saveBreadLoaf(int bread_loaf, int user_id);

    /**
     * Save the number of inventory spots for the user
     * @param inv_spots positive if you want to add inventory spots, negative if you want to take them away
     * @param user_id the user's id
     * @return
     */
    public int saveInventorySpots(int inv_spots, int user_id);

    /**
     * Save the number of tapjoy tokens stored temporarily
     * @param tapjoy_tokens positive if you want to add tapjoy_tokens, negative if you want to take them away
     * @param identifier the unique identifier of the user, whether it is a UDID or MAC address
     * @return
     */
    public int saveTapjoyTokensIdentifier(int tapjoy_tokens, String identifier);

    /**
     * Save whether the user has completed the first day retention reward
     * @param first_day either true or false
     * @param user_id the user's id
     * @return
     */
    public boolean saveFirstDay(boolean first_day, int user_id);

    /**
     * Save whether the user has completed the second day retention reward.
     * @param second_day either true or false
     * @param user_id the user's id
     * @return
     */
    public boolean saveSecondDay(boolean second_day, int user_id);

    /**
     * Save whether the user ahs completed the third day retention reward.
     * @param third_day either true or false
     * @param user_id the user's id
     * @return
     */
    public boolean saveThirdDay(boolean third_day, int user_id);

    /**
     * Returns a list of all the device tokens
     * @return
     */
    public List<String> getDeviceTokens();

    /**
     * Saves either the UDID or MAC address
     * @param user_id the user's id for which an identifier is being saved
     * @param udid the actual UDID in String form
     * @param mac_address the actual MAC Address in String form
     */
    public boolean saveUniqueIdentifier(int user_id, String udid, String mac_address);
}
