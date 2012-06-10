package com.cboxgames.idonia.backend.commons.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog;
import com.cboxgames.idonia.backend.commons.db.accessory.AccessoryDBSQL;
import com.cboxgames.idonia.backend.commons.db.node.NodeDBSQL;
import com.cboxgames.utils.idonia.types.*;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Mob.MobWrapper;

import com.cboxgames.idonia.backend.commons.db.battle.BattleDBSQL;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.Formulas;
import com.cboxgames.idonia.backend.commons.accessorygenerator.AccessoryGenerator;


public class PveBattle {
	

	public static Accessory battlePVE(User user, PveRequest pve, BattleDBSQL battle_db_sql, AccessoryDBSQL _acc_db_sql, NodeDBSQL _n_db_sql) {

		assert (pve.mobs_id.length == pve.mobs_level.length) : "PVE: length of mob_ids (" + pve.mobs_id.length +
			") is not equal to that of mob_levels (" + pve.mobs_level.length + ")";
		
		Map<Integer, Item> mob_map = getMobs(pve.mobs_id, battle_db_sql);
		List<Mob> mob_list = cloneDuplicateMobs(pve.mobs_id, mob_map);
		
		Accessory acc_reward = null;
		if (pve.user_node_id != -1) {
            UserNode[] user_nodes = user.user_nodes;
			for (int ix = 0, ixe = user.user_nodes.length; ix < ixe; ix++) {
				if (user_nodes[ix].node_id == pve.user_node_id) {
					user_nodes[ix].complete = true;
					break;
				}
			}

			if (pve.pve_type.equals("c")) {
				// for boss nodes
				pveUpdateUserMoney(user, mob_list);
				updatePveBossExperience(user, mob_list, pve);
				// int max_level = getMobsMaximumLevel(mob_list);
                acc_reward = getNodeAccReward(user, pve.user_node_id, _acc_db_sql);
				if (acc_reward != null) {
                    CBoxLoggerSyslog.log("reward","acc_rewarded",acc_reward.name);
                }
			}
			else {
				// update user tokens
				//user.tokens += (int) Math.floor(pve.wave_count / Constants.PVE_SURVIVAL_TOKEN_FACTOR);
				
				int max_level = getUserCharacterMaximumLevel(user);
                int mob_level = pve.mobs_level[0];
				// update user node money
                int money_reward = 200 + (mob_level * 15);

                //Potion used, so give a boost!
                if (pve.gold) {
                    money_reward += money_reward*Constants.GOLD_POTION_BOOST;
                }

				user.money += money_reward;
				acc_reward = getNodeAccReward(user, pve.user_node_id, _acc_db_sql);
				if (acc_reward == null) {
					CBoxLoggerSyslog.log("reward","gold_rewarded",money_reward);
				}
				else {
					CBoxLoggerSyslog.log("reward","gold_rewarded","acc_rewarded",money_reward,acc_reward.name);
				}

				List<Character> char_list = findUserCharacterNotDead(user, pve.dead_user_characters_id);
				updatePveNodeExperience(user, mob_level, char_list, pve);
			}
		}
		else {
			// (pve.pve_type.equals("s"))
			// update user tokens
			user.tokens += (int) Math.floor(pve.wave_count / Constants.PVE_SURVIVAL_TOKEN_FACTOR);
			
			pveUpdateUserSurvivalMoney(user, mob_list, pve.mobs_level);
			// ignore dead_user_characters for survival
			List<Character> char_list = findUserCharacterNotDead(user, new Integer[0]);
			updateUserPveExperience(user, char_list, mob_list, pve.mobs_level, pve);
			if (pve.wave_count >= Constants.COMMON_LB) {
				Character random_char = getRandomUserCharacterInLineup(user);
				if (random_char != null) {
					int random_level = random_char.level;
//					Random rand = new Random();
//					if ((rand.nextInt(100) + 1) <= pve.mobs_id.length) {
						final String type;
						if (pve.wave_count < Constants.RARE_LB) type = Constants.COMMON;
						else if (pve.wave_count < Constants.EPIC_LB) type = Constants.UNIQUE;
						else type = Constants.EPIC;
						acc_reward = generateAccessory(user, random_level, type);
						if (acc_reward != null)
							CBoxLoggerSyslog.log("reward","acc_rewarded",acc_reward.name);
//					}
				}
			}
		}

		if (acc_reward != null)
			acc_reward.user_id = user.id;
		
		battle_db_sql.updatePveUser(user, acc_reward, (pve.user_node_id != -1) ? true : false);
	
		return acc_reward;
	}

	public static class Item {
		
		private Object _object;
		private boolean _first;

		public Item(Object obj, boolean first) {
			_object = obj;
			_first = first;
		}
		
		public boolean getFirst() { return _first; }
		public void setFirst(boolean val) { _first = val; }
		public Object getObject() { return _object; }
	}
	
	private static Map<Integer, Item> getMobs(Integer[] mob_ids, BattleDBSQL battle_db_sql) {
		
		Map<Integer, Item> map = new HashMap<Integer, Item>(0);
		
		if (mob_ids == null || mob_ids.length <= 0)
			return map;
		
		// MobWrapper[] mob_wrapper_array = DefaultValueTable.getMobWrapperArray();
		MobWrapper[] mob_wrapper_array = battle_db_sql.getMobDetails();
		for (Integer id : mob_ids)  {
			Item item = map.get(id.intValue());
			if (item != null) continue;

			for (int ix = 0; ix < mob_wrapper_array.length; ix++) {
				Mob mob = mob_wrapper_array[ix].mob;
				if (mob.id != id.intValue()) continue;
				item = new Item(mob, true);
				map.put(mob.id, item);
				break;
			}
		}
		
		return map;
	}
	
	private static List<Mob> cloneDuplicateMobs(Integer[] mob_ids, Map<Integer, Item> mob_map) {
		
		List<Mob> mob_list = new ArrayList<Mob>();
		for (Integer Id : mob_ids) {
			Item item = mob_map.get(Id.intValue());
			Mob mob = (Mob) item.getObject();
			if (item.getFirst() == true) {
				item.setFirst(false);
				mob_list.add(mob);
			}
			else {
				mob_list.add(mob.clone());
			}
		}

		return mob_list;
	}
	
//	private static int getMobsMaximumLevel(List<Mob> mob_list) {
//		
//		int max = 0;
//		for (Mob mob : mob_list) {
//			if (mob.level > max)
//				max = mob.level;
//		}
//		
//		return max;
//	}
	
	private static void pveUpdateUserMoney(User user, List<Mob> mob_list) {
		
		int mod = 1; // redundant
		for (Mob mob : mob_list) {
			int mob_money = mob.money / mod;
			user.money += mob_money;
			
            CBoxLoggerSyslog.log("reward","gold_rewarded", mob_money);
		}
	}
	
	private static void updatePveBossExperience(User user, List<Mob> mob_list, PveRequest pve) {
		
		// get a list of characters not in the dead_user_character list
		List<Character> char_list = findUserCharacterNotDead(user, pve.dead_user_characters_id);
		
		Integer[] mob_levels = pve.mobs_level;
		double factor = applyUserBread(user);
        
		for (Character user_char : char_list) {
			if (user_char.level >= Constants.MAX_LEVEL) {
                //increase the max_level for the user if the user_character's ending level is higher
                if (user_char.level > user.max_level && user_char.in_lineup) user.max_level = user_char.level;
                continue; // when the character level is >= MAX_LEVEL, not experience gained. (1/24/2012)
            }

			int ix = 0;
			for (Mob mob : mob_list) {
				int level = mob_levels[ix].intValue();
				ix++;
				
				int experience_gained = 0;
				if (level == 0) {
					// experience_gained = mob.experience * base_formula(user_character.level, mob.level) * factor
					experience_gained = (int) (mob.experience * factor);
				}
				else {
					// experience_gained = (mob.experience * (1 + (level / 30))) * base_formula(user_character.level, level) * factor
					experience_gained = (int) (mob.experience * factor);
				}

				if (experience_gained < 0)
					experience_gained = 0;
				user_char.experience += experience_gained;
                
                //Potion used, so give a boost!
                if (pve.experience) {
                    int experience_boost = Math.round(user_char.experience*Constants.EXP_POTION_BOOST);
                    user_char.experience += experience_boost;
                }
                
				
				levelUpUserCharacter(user_char, user);
			}
		}
	}

    public static Accessory getNodeAccReward(User user, int node_id, AccessoryDBSQL _acc_db_sql) {
    	
    	if (user.user_character_accessories_inventory != null) {
			if (user.user_character_accessories_inventory.length >= user.inventory_spots)
				return null;
		}
        
        
        List<Accessory> reward_acc = new ArrayList<Accessory>();
        List<Accessory> acc_list = _acc_db_sql.getAccessoryByNodeID(node_id);

        if (user.user_character_accessories_sold != null) {
            for (Accessory node_acc : acc_list) {
                for (Accessory sold_acc : user.user_character_accessories_sold) {
                    if (node_acc.id == sold_acc.accessory_id) {
                        return null;
                    }
                }
            }
        }

        for (Accessory acc : acc_list) {
            boolean in_inventory = false;
            boolean in_uc = false;

            for (Character uc : user.user_characters) {
                for (Accessory uc_acc : uc.user_character_accessories) {
                    if (uc_acc.accessory_id == acc.id) {
                        in_uc = true;
                        break;
                    }
                }
                if (in_uc) break;
            }
            
            if (in_uc) continue;

            if (user.user_character_accessories_inventory != null) {
	            for (Accessory inv_acc : user.user_character_accessories_inventory) {
	                if (inv_acc.accessory_id == acc.id) {
	                    in_inventory = true;
	                    break;
	                }
	            }
            }

            if (!in_inventory) {
                reward_acc.add(acc);
            }
        }
        
        if (reward_acc.size() == 0)
        	return null;
        if (reward_acc.size() == 1)
        	reward_acc.get(0);

        Random rand = new Random();
        int rand_indx = rand.nextInt(reward_acc.size());
        return reward_acc.get(rand_indx);
    }
	
	// Get a list of characters not in the dead_user_character list
	// Assume that all user characters have been cashed into user.user_characters.
	public static List<Character> findUserCharacterNotDead(User user, Integer[] dead_user_chars) {
		
		List<Character> char_list = new ArrayList<Character>();
		
		for (Character character : user.user_characters) {
			boolean found = false;
			for (int ix = 0, length = dead_user_chars.length; ix < length; ix++) {
				if (character.in_lineup == false) continue; // not sure if this is needed.
				if (character.id != dead_user_chars[ix].intValue()) continue;
				found = true;
				break;
			}
			
			if (found) continue;
			
			char_list.add(character);
		}
		
		return char_list;
	}
	
	public static final double applyUserBread(User user) {
		
		if (user.breadstick > 0) {
			user.breadstick--;
			return Constants.BREADSTICK_BOOST;
			// Increment the count for BreadStick Count
		    // user_analytic = UserAnalytic.find(:first, :conditions => ['user_id=? and analytic_id=?', self.id, 13])
		    // user_analytic.update_attribute(:count, user_analytic.count + 1)
		}
		
		if (user.bread_slice > 0) {
			user.bread_slice--;
			return Constants.BREADSLICE_BOOST;
		}
		
		if (user.bread_loaf > 0) {
			user.bread_loaf--;
			return Constants.BREADLOAF_BOOST;
		}
		
		return 1;
	}
	
	public static void levelUpUserCharacter(Character user_char, User user) {

		if ((user_char.level >= Constants.MAX_LEVEL) || (user_char.experience < user_char.max_experience)) {
            return;
        }

        //There's a possibility that a character can level up twice in the same battle
		while (user_char.experience > user_char.max_experience) {
            // Level up, reset experience to carryover, set max_experience to next level, bump stats by 3
            user_char.level++;
            if (user_char.level == Constants.MAX_LEVEL) {
                // Add advanced playlists if character reaches level 20 (in rube on rails)
                // in Tomcat, all playlists are already added to the users when they is created. (Michael 12/12/2011)
            }

            user_char.experience -= user_char.max_experience;
            user_char.max_experience = Formulas.getUserCharacterMaxExperience(user_char.level);
            user_char.stats += Constants.USER_CHARACTER_STATS_GAIN;

            // Also level up weapon and armor for User Character, even weapons in inventory
            // Do this by looking up all user_character_accessories by user and matching character IDs to the user_character's character ID.
            // Should find all weapons
            // Not to level up user accessories (1/16/2012)
    //		for (Accessory accessory : user_char.user_character_accessories) {
    //			if (accessory.character_id != user_char.character_id) continue;
    //			if (accessory.accessory_type.equals(Accessory.ACCESSORY_TYPE_ACCESSORY)) continue;
    //			accessory.level += 1;
    //			accessory.stats += Constants.USER_CHARACTER_ACCESSORY_STATS_GAIN;
    //		}
        }
        //increase the max_level for the user if the user_character's ending level is higher
        if (user_char.level > user.max_level  && user_char.in_lineup) user.max_level = user_char.level;

        CBoxLoggerSyslog.log("level_up","user_id","character_id",user.id,user_char.character_id);
	}
	
	private static Accessory generateAccessory(User user, int level, String rare) {
		
		// A user has a maximum of 9 slots in inventory
		if (user.user_character_accessories_inventory != null) {
			if (user.user_character_accessories_inventory.length >= user.inventory_spots)
				return null;
		}
		
		int n_user_char = user.user_characters.length;
		if (n_user_char <= 0) return null;
		
		Random rand = new Random();
		int ix = rand.nextInt(n_user_char);
		Character random_user_character = user.user_characters[ix];
		int max_level = (level != 0) ? level : random_user_character.level;
		
		Accessory acc_reward = AccessoryGenerator.generateAccessoryForCharacter(random_user_character.name, max_level, rare);
		// Note: set cost of loot to 1/2 of what the algorithm generates (not seen implemented 12/12/11 - Michael)
		
		acc_reward.user_id = user.id;
		acc_reward.user_character_id = 0;
		acc_reward.generated = true;
		
		return acc_reward;
	}
	
	public static int getUserCharacterMaximumLevel(User user) {
		
		int max = 0;
		for (Character character : user.user_characters) {
			if (character.in_lineup == false) continue;
			if (character.level <= max) continue;
			max = character.level;
		}
		
		return max;
	}
	
	private static void updatePveNodeExperience(User user, int max_level, List<Character> char_list, PveRequest pve) {
		
		boolean found = false;
		for (Character user_char : char_list) {
			if (user_char.level < Constants.MAX_LEVEL) {
				found = true;
				break;
			}
		}
		
		if (!found) return;
		
		double factor = applyUserBread(user); // only if at least one user_char.level is below MAX_LEVEL
		for (Character user_char : char_list) {
			if (user_char.level >= Constants.MAX_LEVEL) {
                //increase the max_level for the user if the user_character's ending level is higher
                if (user_char.level > user.max_level && user_char.in_lineup) user.max_level = user_char.level;
                continue; // when the character level is >= MAX_LEVEL, not experience gained. (1/24/2012)
            }

			//int experience_gained = (int) (((360.00 * ((max_level / 7.00) + 1.00)) + 50) * factor);
            int experience_gained = (int) (((360.00 * ((max_level / 7.00) + 0.81)) + 50) * factor);
			user_char.experience += experience_gained;

            //Potion used, so give a boost!
            if (pve.experience) {
                int experience_boost = Math.round(user_char.experience*Constants.EXP_POTION_BOOST);
                user_char.experience += experience_boost;
            }

			levelUpUserCharacter(user_char, user);
		}
	}
	
	private static void pveUpdateUserSurvivalMoney(User user, List<Mob> mob_list, Integer[] mob_levels) {
		
		int ix = 0;
		for (Mob mob : mob_list) {
			int level = mob_levels[ix].intValue();
			double mod = (Math.sqrt(level) + (level * 0.3)) * 0.5;
			user.money += (int) (mob.money * mod);
			ix++;
		}
	}
	
	private static void updateUserPveExperience(User user, List<Character> char_list, List<Mob> mob_list, Integer[] mob_levels, PveRequest pve) {
		
		// Dead user characters for PVE gets no experience
		double factor = applyUserBread(user);
		for (Character user_char : char_list) {
			if (user_char.level >= Constants.MAX_LEVEL) {
                //increase the max_level for the user if the user_character's ending level is higher
                if (user_char.level > user.max_level && user_char.in_lineup) user.max_level = user_char.level;
                continue; // when the character level is >= MAX_LEVEL, not experience gained. (1/24/2012)
            }

			int ix = 0;

            //Potion used, so give a boost!
            if (pve.experience) {
                int experience_boost = Math.round(user_char.experience*Constants.EXP_POTION_BOOST);
                user_char.experience += experience_boost;
            }

			for (Mob mob : mob_list) {
				int level = mob_levels[ix].intValue();
				
				int experience_gained = 0;
				if (level == 0) {
			        // experience_gained = mob.experience * base_formula(user_character.level, mob.level) * factor
			    	experience_gained = (int) (mob.experience * factor);
				}
				else {
					 // experience_gained = (mob.experience * (1 + (level / 30))) * base_formula(user_character.level, level) * factor
					 experience_gained = (int) ((mob.experience * Math.sqrt(level)) * factor);
				}

				if (experience_gained < 0)
					experience_gained = 0;

				user_char.experience += experience_gained;
				
				levelUpUserCharacter(user_char, user);
				
				ix++;
			}
		}
	}
	
	private static Character getRandomUserCharacterInLineup(User user) {
		
		List<Character> char_list = new ArrayList<Character>();
		for (Character user_char : user.user_characters) {
			if (user_char.in_lineup == false) continue;
			char_list.add(user_char);
		}
		
		if (char_list.isEmpty()) return null;
		
		Character[] char_array = char_list.toArray(new Character[0]);
		Random rand = new Random();
		return char_array[rand.nextInt(char_array.length)];
	}
}
