package com.cboxgames.idonia.backend.commons;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.User;

public class Utility {
	
    public static boolean isOldData(Date time) {
    	Calendar current = Calendar.getInstance();
    	Calendar updated = Calendar.getInstance();
    	updated.setTime(time);	
    	current.set(Calendar.HOUR_OF_DAY, 0);
    	return updated.before(current);
    }
    
    // 1. Must have at least 3 characters
    // 2. Each user character must have a helmet, weapon, armor and accessory and their user_character_id must not be 0
    // 3. User character accessory must not be in user_character_accessory_inventories
    // 4. Accessory in inventory: user_character_id must be 0
    public static void verifyUserData(User user, UserCharacterDBSQL _uc_db_sql) {
    	Character[] user_chars = user.user_characters;
    	int n_chars = (user_chars == null) ? 0 : user_chars.length;
    	
//    	if (n_chars < 3) {
//    		CBoxLoggerSyslog.log("bad_user_data", "user_id", "num_of_characters",user.id, n_chars);
//    	}

        int in_lineup_count = 0;
    	if (user_chars != null) {
    		String[] typetbl = { "Accessory", "Weapon", "Armor", "Helmet" };
    		for (Character user_char : user_chars) {
    			int flag = 0;
    			for (Accessory acc : user_char.user_character_accessories) {
    				if (acc.accessory_type.equals(Constants.ACCESSORY)) {
        				flag |= 1;
        			}
        			else if (acc.accessory_type.equals(Constants.WEAPON)) {
        				flag |= (1 << 1);
        			}
        			else if (acc.accessory_type.equals(Constants.ACC_ARMOR)) {
        				flag |= (1 << 2);
        			}
        			else if (acc.accessory_type.equals(Constants.HELMET)) {
        				flag |= (1 << 3);
        			}
    			}
    			
    			for (int ix = 0; ix < 4; ix++) {
    				if ((flag & (1 << ix)) != 0) continue;
    				CBoxLoggerSyslog.log("error","error_type","user_id", "user_character_id","missing","bad_user_data", user.id, user_char.id, typetbl[ix]);
    			}

                if (user_char.in_lineup) {
                    in_lineup_count++;
                }
    		}
    	}

        //Auto correct in_lineup bug

        if ((in_lineup_count < 3) && (n_chars >= 3)) {
            List<Character> chars_out_lineup = new ArrayList<Character>();
            for (Character user_char : user_chars) {
                if (user_char.in_lineup) continue;

                chars_out_lineup.add(user_char);
                user_char.in_lineup = true;
                in_lineup_count++;
                if (in_lineup_count >= 3) break;
            }

            if (!chars_out_lineup.isEmpty())
            	_uc_db_sql.correctUserCharacterInLineup(chars_out_lineup);
        }

    	
    	if (user.user_character_accessories_inventory != null) {
    		for (Accessory iacc : user.user_character_accessories_inventory) {
    			if (iacc.user_character_id != 0)
                    CBoxLoggerSyslog.log("error","error_type", "user_id", "user_character_id of inventory","bad_user_data", user.id, iacc.user_character_id);
    			if ((user_chars == null) || iacc.accessory_type.equals("Accessory")) continue;
    			for (Character user_char : user_chars) {
    				for (Accessory uacc : user_char.user_character_accessories) {
    					if (iacc.accessory_id != uacc.accessory_id) continue;
    					CBoxLoggerSyslog.log("error,","error_type", "user_id", "more than one accessory","bad_user_data", user.id, iacc.accessory_id);
    				}
    			}
    		}
    	}
    }
}
