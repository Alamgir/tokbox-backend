package com.cboxgames.idonia.backend.commons.requestclasses;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/10/11
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCharacterUpdateRequest {
    public UserCharacterUpdate[] user_characters;
    public UserPotions[] user_potions;

    public static class UserCharacterUpdate {
        public int agility;
        public int will;
        public int intelligence;
        public int id;  // user_character id
        public int strength;
        public int vitality;
        public boolean in_lineup;
        public UserCharacterAccessory user_character_accessory;
        public UserCharacterSkills[] user_character_skills;
    }

    public static class UserCharacterAccessory {
        public int accessory_id;
        public int weapon_accessory_id;
        public int armor_accessory_id;
        public int helmet_accessory_id;
    }

    public static class UserCharacterSkills {
        public int id;
        public boolean in_use;
    }
    
    public static class UserPotions {
        public int id;
        public boolean in_use;
    }
    
}
