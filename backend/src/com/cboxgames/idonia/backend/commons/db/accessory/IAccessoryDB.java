package com.cboxgames.idonia.backend.commons.db.accessory;

import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.Accessory.AccessoryWrapper;
import com.cboxgames.utils.idonia.types.AccessorySkill.AccessorySkillWrapper;
import com.cboxgames.utils.idonia.types.AccessorySkillEffect.AccessorySkillEffectWrapper;

public interface IAccessoryDB {

	public static int ACCESSORY_TYPE_WEAPON_ID = 0;
	public static int ACCESSORY_TYPE_ACCESSORY_ID = 1;
	public static int ACCESSORY_TYPE_ARMOR_ID = 2;
    public static int ACCESSORY_TYPE_HELMET_ID = 3;

	/**
	 * Get a list of all accessories of this type.
	 * 
	 * @param type the accessory type to search for.
	 * 
	 * @return
	 */
	public List<Accessory> getAccessories(AccessoryType type);
	
	/**
	 * Get a list of all accessories of the requested type built for the requested character type.
	 * 
	 * @param type the accessory type to search for.
	 * @param character_type the character type the accessories should be searched for.
	 * 
	 * @return
	 */
	public List<Accessory> getAccessories(AccessoryType type, int character_type);
	
	/**
	 * Get a single accessory by its ID.
	 * 
	 * @param id
	 * 
	 * @return
	 */
	public Accessory getAccessory(int id);

    /**
     * Get a List of Accessories for each character_id passed in
     * @param ids
     * @return a map with keys = character_ids / values = lists of corresponding accessories
     */
    public Map<Integer, List<Accessory>> getAccessoriesByCharacter(List<Integer> ids);
    
    /**
	 * Get a wrapper array of accessories.
	 * 
	 * @return
	 */
    public AccessoryWrapper[] getAccessoryDetails();
    
    /**
   	 * Get a wrapper array of accessory skills.
   	 * 
   	 * @return
   	 */
    public AccessorySkillWrapper[] getAccessorySkillDetails();
    
    /**
   	 * Get a wrapper array of accessory skill effects.
   	 * 
   	 * @return
   	 */
    public AccessorySkillEffectWrapper[] getAccessorySkillEffectDetails();
    
	
	public enum AccessoryType {
		Weapon(ACCESSORY_TYPE_WEAPON_ID),
		Accessory(ACCESSORY_TYPE_ACCESSORY_ID),
		Armor(ACCESSORY_TYPE_ARMOR_ID),
        Helmet(ACCESSORY_TYPE_HELMET_ID);
		
		private int id;
		
		private AccessoryType(int id) {
			this.id = id;
		}
		
		public int getId() { return id; }
		
		/**
		 * Translate an accessory type ID to its Enum name.
		 * 
		 * @param id
		 * @return
		 */
		public static String idToName(int id) {
			
			switch(id) {
			
			case ACCESSORY_TYPE_WEAPON_ID:
				return Weapon.name();
			case ACCESSORY_TYPE_ACCESSORY_ID:
				return Accessory.name();
			case ACCESSORY_TYPE_ARMOR_ID:
				return Armor.name();
            case ACCESSORY_TYPE_HELMET_ID:
                return Helmet.name();
			default:
				return "NONE";
					
			}
			
		}
	}
}
