package com.cboxgames.idonia.backend.commons.db.usercharacter;

import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog;
import com.cboxgames.idonia.backend.commons.db.accessory.AccessoryDBSQL;
import com.cboxgames.idonia.backend.commons.db.character.CharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.skill.SkillDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacterskill.UserCharacterSkillDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacteraccessory.UserCharacterAccessoryDBSQL;
import com.cboxgames.idonia.backend.commons.db.userpotion.UserPotionDBSQL;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest.*;
import com.cboxgames.idonia.backend.commons.DBSQL;

import com.cboxgames.utils.idonia.types.*;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Character.CharacterWrapper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/4/11
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCharacterDBSQL extends DBSQL implements IUserCharacterDB {

    private CharacterDBSQL _c_db_sql;
    private SkillDBSQL _sk_db_sql;
    private AccessoryDBSQL _acc_db_sql;

    public UserCharacterDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
        super(data_source, servlet_context);
        _c_db_sql = new CharacterDBSQL(data_source, servlet_context);
        _sk_db_sql = new SkillDBSQL(data_source, servlet_context);
        _acc_db_sql = new AccessoryDBSQL(data_source, servlet_context);
    }
    
    public boolean createUserCharacters(List<Integer> character_ids, User user, boolean in_lineup) {
    	
    	if (character_ids.isEmpty())
    		return false;
    	
    	boolean status = false;
    	Connection conn = null;
    	
    	try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            Map<Integer, Character> character_map = CharacterDBSQL.getCharacterMap(character_ids, conn);
            Map<Integer, List<Skill>> skill_map = SkillDBSQL.getSkillMap(character_ids, conn);
            Map<Integer, List<Accessory>> accessory_map = AccessoryDBSQL.getAccessoryMap(character_ids, conn);

            List<Character> user_char_list = new ArrayList<Character>();

            if (user.user_characters != null) {
                user_char_list.addAll(Arrays.asList(user.user_characters));
            }
            else {
                //set the max_level for the user to 1 when creating chars for the first time
                PreparedStatement set_max_lvl = conn.prepareStatement("UPDATE users SET max_level = 1 WHERE id = ?");
                set_max_lvl.setInt(1, user.id);
                set_max_lvl.executeUpdate();
                //Analytics check for character creation
                CBoxLoggerSyslog.log("tutorial", "user_id", "created_chars", user.id, character_ids);
            }

	        for (int char_id : character_ids) {
	        	Character charx = character_map.get(char_id);
	        	Character uc = createUserCharacter(charx, user.id, in_lineup);
                addStatPointsToCharacter(user, uc);
	            user_char_list.add(uc);
	            saveUserCharacter(uc, conn);

                List<Skill> skill_list = skill_map.get(char_id);
                createAndSaveUserSkill(skill_list, uc, conn);
                
                List<Accessory> accessory_list = accessory_map.get(char_id);
                createAndSaveUserAccessory(accessory_list, uc, conn);
	        }

	        user.user_characters = user_char_list.toArray(new Character[0]);
	        conn.commit();
	        status = true;

    	} catch (SQLException e) {
	        e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
    	return status;
    }

    public List<Character> getUserCharactersForShop(int user_id) {
    	
        Connection conn = null;
        List<Character> user_character_list = null;

        try {
            conn = getConnection();
            Map<Integer, String> map = CharacterDBSQL.getCharacterMap(conn);
            user_character_list = new ArrayList<Character>();
            PreparedStatement query = conn.prepareStatement("SELECT character_id, level FROM user_characters WHERE user_id = ? ");

            query.setInt(1, user_id);
            ResultSet result = query.executeQuery();

            if (result != null) {
                while (result.next()) {
                    Character uc = new Character();
                    uc.character_id = result.getInt("character_id");
                    uc.level = result.getInt("level");
                    uc.name = map.get(uc.character_id);
                    user_character_list.add(uc);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        return user_character_list;
    }
    
    public List<Character> getUserCharacters(UserCharacterUpdate[] ucu_requests) {
    	
    	if (ucu_requests == null) return null;
    	int list_size = ucu_requests.length;
    	if (list_size <= 0) return null;
    	
    	List<Character> user_character_list = null;
    	Connection conn = null;
		
		try {
			conn = getConnection();
			Map<Integer, String> map = CharacterDBSQL.getCharacterMap(conn);
	    	StringBuilder query = new StringBuilder("SELECT id,character_id,user_id,level,strength,agility,intelligence," +
                                                            "vitality,will,armor,dodge,spell_crit,physical_crit,experience,max_experience," +
                                                            "in_lineup,stats,weapon_swap FROM user_characters WHERE id = ");
	    	query.append(ucu_requests[0].id);
	        for (int ix = 1; ix < list_size; ix++) {
	            query.append(" OR id = ");
	            query.append(ucu_requests[ix].id);
	        }
	    	
			PreparedStatement statement = conn.prepareStatement(query.toString());
			ResultSet result = statement.executeQuery();
			List<Character> tmp_list = new ArrayList<Character>();
			while (result.next()) {
				Character uc = new Character();
				getUserCharacterFromResult(result, uc, map);
				tmp_list.add(uc);
			}
			
			UserCharacterAccessoryDBSQL.getUserCharacterAccessories(tmp_list, conn);
			UserCharacterSkillDBSQL.getUserCharacterSkills(tmp_list, conn);
			
			// Make the list of user characters in the same order as ucu_requests
	       	user_character_list = new ArrayList<Character>();
	       	for (UserCharacterUpdate ucu : ucu_requests) {
        		for (Character user_char : tmp_list) {
        			if (user_char.id != ucu.id) continue;
        			user_character_list.add(user_char);
        			break;
        		}
        	}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
    	return user_character_list;
    }
    
    public static Map<Integer, Character> getUserCharacterMap(User u) {
        Map<Integer, Character> map = new HashMap<Integer, Character>();
        
        for (Character c : u.user_characters) {
            map.put(c.id, c);
        }
        
        return map;
    }
    
    public Map<Integer, Character> getUserCharacterMap(UserCharacterUpdate[] ucu_requests) {
        if (ucu_requests == null) return null;
        int list_size = ucu_requests.length;
        if (list_size <= 0) return null;

        Map<Integer,Character> user_character_list = new HashMap<Integer, Character>();
        Connection conn = null;

        try {
            conn = getConnection();
            Map<Integer, String> map = CharacterDBSQL.getCharacterMap(conn);
            StringBuilder query = new StringBuilder("SELECT id,character_id,user_id,level,strength,agility,intelligence," +
                                                            "vitality,will,armor,dodge,spell_crit,physical_crit,experience,max_experience," +
                                                            "in_lineup,stats,weapon_swap FROM user_characters WHERE id = ");
            query.append(ucu_requests[0].id);
            for (int ix = 1; ix < list_size; ix++) {
                query.append(" OR id = ");
                query.append(ucu_requests[ix].id);
            }

            PreparedStatement statement = conn.prepareStatement(query.toString());
            ResultSet result = statement.executeQuery();
            List<Character> tmp_list = new ArrayList<Character>();
            while (result.next()) {
                Character uc = new Character();
                getUserCharacterFromResult(result, uc, map);
                tmp_list.add(uc);
            }

            UserCharacterAccessoryDBSQL.getUserCharacterAccessories(tmp_list, conn);
            UserCharacterSkillDBSQL.getUserCharacterSkills(tmp_list, conn);

            // Make the list of user characters in the same order as ucu_requests
            for (UserCharacterUpdate ucu : ucu_requests) {
                for (Character user_char : tmp_list) {
                    if (user_char.id != ucu.id) continue;
                    user_character_list.put(user_char.id, user_char);
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return user_character_list;
    }
 
    public boolean saveUserCharacters(List<Character> character_list) {
        Connection conn = null;
        boolean saved = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement update_uc = conn.prepareStatement("UPDATE user_characters SET level=?, strength=?," +
                                                                    "agility=?,intelligence=?,will=?,vitality=?," +
                                                                    "armor=?,dodge=?,physical_crit=?,spell_crit=?," +
                                                                    "experience=?,max_experience=?,in_lineup=?,stats=?," +
                                                                    "weapon_swap=? WHERE id = ? ");

            for (Character c : character_list) {
                update_uc.setInt(1, c.level);
                update_uc.setInt(2, c.strength);
                update_uc.setInt(3, c.agility);
                update_uc.setInt(4, c.intelligence);
                update_uc.setInt(5, c.will);
                update_uc.setInt(6, c.vitality);
                update_uc.setDouble(7, c.armor);
                update_uc.setDouble(8, c.dodge);
                update_uc.setDouble(9, c.physical_crit);
                update_uc.setDouble(10, c.spell_crit);
                update_uc.setInt(11, c.experience);
                update_uc.setInt(12, c.max_experience);
                update_uc.setBoolean(13, c.in_lineup);
                update_uc.setInt(14, c.stats);
                update_uc.setBoolean(15, c.weapon_swap);
                update_uc.setInt(16, c.id);
                update_uc.addBatch();

                PreparedStatement update_uca = conn.prepareStatement("UPDATE user_character_accessories SET user_character_id=?,level_requirement=?," +
                                                                        "strength=?,vitality=?,agility=?,intelligence=?," +
                                                                        "will=?,armor=?,dodge=?,physical_crit=?,spell_crit=?," +
                                                                        "damage_high=?,damage_low=?,rarity=?,accessory_id=? " +
                                                                         "WHERE id = ? ");

                for (Accessory accessory : c.user_character_accessories) {
                    update_uca.setInt(1, accessory.user_character_id);
                    update_uca.setInt(2, accessory.level_requirement);
                    update_uca.setInt(3, accessory.strength);
                    update_uca.setInt(4, accessory.vitality);
                    update_uca.setInt(5, accessory.agility);
                    update_uca.setInt(6, accessory.intelligence);
                    update_uca.setInt(7, accessory.will);
                    update_uca.setDouble(8, accessory.armor);
                    update_uca.setDouble(9, accessory.dodge);
                    update_uca.setDouble(10, accessory.physical_crit);
                    update_uca.setDouble(11, accessory.spell_crit);
                    update_uca.setInt(12, accessory.damage_high);
                    update_uca.setInt(13, accessory.damage_low);
                    update_uca.setString(14, accessory.rarity);
                    update_uca.setInt(15, accessory.accessory_id);
                    update_uca.setInt(16, accessory.id);
                    update_uca.addBatch();
                }
                update_uca.executeBatch();                
            }
            update_uc.executeBatch();
            conn.commit();
            saved = true;
        }
        catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
        	closeConnection(conn);
        }
        return saved;
    }
    
    public boolean updateUserCharacterAll(User user, UserCharacterUpdateRequest ucu_request) {
    	Map<Integer,Character> user_char_list = getUserCharacterMap(user);
        Connection conn = null;
        boolean saved = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            //update the max_level
            //check if any character being swapped in has a higher level
            boolean update_lvl = false;
            for (UserCharacterUpdate u_request : ucu_request.user_characters) {
                Character user_char = user_char_list.get(u_request.id);
                if (user_char.level > user.max_level && u_request.in_lineup) {
                    update_lvl = true;
                    user.max_level = user_char.level;
                }
            }
            if (update_lvl) {
                PreparedStatement update_max_lvl = conn.prepareStatement("UPDATE users SET max_level = ? WHERE id = ?");
                update_max_lvl.setInt(1, user.max_level);
                update_max_lvl.setInt(2, user.id);
                update_max_lvl.executeUpdate();
            }
            
            updateUserCharactersStats(user_char_list, ucu_request.user_characters, conn);
         	UserCharacterAccessoryDBSQL.updateAccessoryUserCharacterID(user_char_list, ucu_request.user_characters, conn, user.id);
          	UserCharacterSkillDBSQL.updateUserCharacterSkills(ucu_request.user_characters, conn);
            //UserPotionDBSQL.updateUserPotions(user.id, ucu_request, conn);
            conn.commit();
            saved = true;
        }
        catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
         	closeConnection(conn);
        }
        return saved;
    }

    public boolean saveUserCharactersStats(List<Character> character_list) {
        Connection conn = null;
        boolean saved = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement update_uc = conn.prepareStatement("UPDATE user_characters SET level=?, strength=?," +
                                                                    "agility=?,intelligence=?,will=?,vitality=?," +
                                                                    "armor=?,dodge=?,physical_crit=?,spell_crit=?," +
                                                                    "experience=?,max_experience=?,in_lineup=?,stats=?," +
                                                                    "weapon_swap=? WHERE id = ? ");

            for (Character c : character_list) {
                update_uc.setInt(1, c.level);
                update_uc.setInt(2, c.strength);
                update_uc.setInt(3, c.agility);
                update_uc.setInt(4, c.intelligence);
                update_uc.setInt(5, c.will);
                update_uc.setInt(6, c.vitality);
                update_uc.setDouble(7, c.armor);
                update_uc.setDouble(8, c.dodge);
                update_uc.setDouble(9, c.physical_crit);
                update_uc.setDouble(10, c.spell_crit);
                update_uc.setInt(11, c.experience);
                update_uc.setInt(12, c.max_experience);
                update_uc.setBoolean(13, c.in_lineup);
                update_uc.setInt(14, c.stats);
                update_uc.setBoolean(15, c.weapon_swap);
                update_uc.setInt(16, c.id);
                update_uc.addBatch();
            }
            update_uc.executeBatch();
            conn.commit();
            saved = true;
        }
        catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
        	closeConnection(conn);
        }
        return saved;
    }

    public CharacterWrapper getUserCharacterByID(int user_character_id) {
    	
        Connection conn = null;
        CharacterWrapper user_character_wrapper = new CharacterWrapper();

        try {
            conn = getConnection();
            Map<Integer, String> map = CharacterDBSQL.getCharacterMap(conn);

            PreparedStatement query = conn.prepareStatement("SELECT id,character_id,user_id,level,strength," +
                                                                    "agility,intelligence,vitality,will,armor,dodge," +
                                                                    "spell_crit,physical_crit,experience,max_experience," +
                                                                    "in_lineup,stats,weapon_swap FROM user_characters WHERE id = ? ");
 
            query.setInt(1, user_character_id);
            ResultSet result = query.executeQuery();
            if ((result != null) && result.next()) {
                Character uc = user_character_wrapper.character;
                getUserCharacterFromResult(result, uc, map);         
                uc.user_character_accessories = UserCharacterAccessoryDBSQL.getUserCharacterAccessories(uc.id, conn); 
                uc.user_character_skills = UserCharacterSkillDBSQL.getAllSkillsForUserCharacter(uc.id, conn);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        return user_character_wrapper ;
    }

    public boolean updateUserCharacterWeaponSwap(int user_char_id) {
    	boolean update = false;
    	Connection conn = null;

        try {
            conn = getConnection();
            PreparedStatement update_stmnt = conn.prepareStatement("UPDATE user_characters " +
            		"SET weapon_swap = ? WHERE id = ? ");
            update_stmnt.setBoolean(1, true);
            update_stmnt.setInt(2, user_char_id);
            update_stmnt.executeUpdate();
            update = true;
            
        } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	    	closeConnection(conn);
	    }
        
    	return update;
    }
    
    // static method. It is used when connection is provided
    private static void updateUserCharactersStats(Map<Integer,Character> user_char_list, UserCharacterUpdate[] uc_update, Connection conn) throws SQLException {

        //Set user_character values to that of the request
        for (UserCharacterUpdate u_request : uc_update) {
            Character user_char = user_char_list.get(u_request.id);
            if (checkStats(user_char, u_request) == false)
                continue;

            user_char.agility = u_request.agility;
            user_char.strength = u_request.strength;
            user_char.will = u_request.will;
            user_char.intelligence = u_request.intelligence;
            user_char.vitality = u_request.vitality;
            user_char.in_lineup = u_request.in_lineup;
        }


        //Write the user_characters to the DB
        PreparedStatement update_uc = conn.prepareStatement("UPDATE user_characters SET level=?, strength=?," +
                                                                "agility=?,intelligence=?,will=?,vitality=?," +
                                                                "armor=?,dodge=?,physical_crit=?,spell_crit=?," +
                                                                "experience=?,max_experience=?,in_lineup=?,stats=?," +
                                                                "weapon_swap=? WHERE id = ? ");

        for (Character user_char : user_char_list.values()) {
            update_uc.setInt(1, user_char.level);
            update_uc.setInt(2, user_char.strength);
            update_uc.setInt(3, user_char.agility);
            update_uc.setInt(4, user_char.intelligence);
            update_uc.setInt(5, user_char.will);
            update_uc.setInt(6, user_char.vitality);
            update_uc.setDouble(7, user_char.armor);
            update_uc.setDouble(8, user_char.dodge);
            update_uc.setDouble(9, user_char.physical_crit);
            update_uc.setDouble(10, user_char.spell_crit);
            update_uc.setInt(11, user_char.experience);
            update_uc.setInt(12, user_char.max_experience);
            update_uc.setBoolean(13, user_char.in_lineup);
            update_uc.setInt(14, user_char.stats);
            update_uc.setBoolean(15, user_char.weapon_swap);
            update_uc.setInt(16, user_char.id);
            update_uc.addBatch();
        }
        update_uc.executeBatch();
    }

    public int saveStatPoints(int stat_points, int uc_id) {
        Connection conn = null;
        int new_stat_points = -1;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE user_characters SET stats = stats + ? WHERE id = ?");
            query.setInt(1, stat_points);
            query.setInt(2, uc_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT stats from user_characters WHERE id = ? ");
            query_two.setInt(1, uc_id);

            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_stat_points = result.getInt("stats");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return new_stat_points;
    }

    private static boolean checkStats(Character user_character, UserCharacterUpdate u_request) {
        if (user_character == null) {return false;}

        int current_stats = user_character.strength +
                user_character.agility +
                user_character.intelligence +
                user_character.vitality +
                user_character.will;

        int new_stats = u_request.strength +
                u_request.agility +
                u_request.intelligence +
                u_request.vitality +
                u_request.will;

        int delta = new_stats - current_stats;
        if (user_character.stats < delta) {return false;}

        user_character.stats -= delta;

        //Analytics call for stat point allocation during tutorial
        if (user_character.level == 2 && delta > 0)
            CBoxLoggerSyslog.log("tutorial", "user_id", "level_2_stats", user_character.user_id, true);


        return true;
    }

    public boolean correctUserCharacterInLineup(List<Character> uc_list) {
        Connection conn = null;
        boolean updated = false;

        try {
            conn = getConnection();
            StringBuilder query = new StringBuilder("UPDATE user_characters SET in_lineup = true WHERE id = ");
            query.append(uc_list.get(0).id);
	        for (int ix = 1; ix < uc_list.size(); ix++) {
	            query.append(" OR id = ");
	            query.append(uc_list.get(ix).id);
	        }
	        
	        PreparedStatement update_query = conn.prepareStatement(query.toString());
	        update_query.executeUpdate();
            updated = true;

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            closeConnection(conn);
        }
        return updated;
    }
    
    public static Character[] getAllUserCharacters(int user_id, Connection conn) throws SQLException {
    	
    	Map<Integer, String> map = CharacterDBSQL.getCharacterMap(conn);
    	
        List<Character> user_char_list = new ArrayList<Character>();

        PreparedStatement get_characters = conn.prepareStatement("SELECT id,character_id,user_id,level,strength,agility," +
                                                                         "intelligence,vitality,will,armor,dodge," +
                                                                         "spell_crit,physical_crit,experience,max_experience," +
                                                                         "in_lineup,stats,weapon_swap FROM user_characters WHERE user_id = ? ");
        get_characters.setInt(1, user_id);
        ResultSet uc_results = get_characters.executeQuery();
        if (uc_results == null) return null;
        
        while (uc_results.next()) {
            Character uc = new Character();
            getUserCharacterFromResult(uc_results, uc, map);
            uc.user_character_accessories = UserCharacterAccessoryDBSQL.getUserCharacterAccessories(uc.id, conn); 
            uc.user_character_skills = UserCharacterSkillDBSQL.getAllSkillsForUserCharacter(uc.id, conn);
            user_char_list.add(uc);
        }
        
        if (user_char_list.isEmpty()) return null;
        
        return (Character[]) user_char_list.toArray(new Character[0]);
    }
    
    public static Character[] getUserCharacterByID(int user_char_id, boolean include_accessory, boolean include_skill, Connection conn)
    		throws SQLException {
    	
    	Map<Integer, String> map = CharacterDBSQL.getCharacterMap(conn);
        List<Character> user_char_list = new ArrayList<Character>();
        PreparedStatement get_characters = conn.prepareStatement("SELECT * FROM user_characters WHERE id = ? ");
        get_characters.setInt(1, user_char_id);
        ResultSet uc_results = get_characters.executeQuery();
        if ((uc_results != null) && uc_results.next()) {
            Character uc = new Character();
            getUserCharacterFromResult(uc_results, uc, map);
            if (include_accessory)
            	uc.user_character_accessories = UserCharacterAccessoryDBSQL.getUserCharacterAccessories(uc.id, conn);
            if (include_skill)
            	uc.user_character_skills = UserCharacterSkillDBSQL.getAllSkillsForUserCharacter(uc.id, conn);
            user_char_list.add(uc);
        }
        
        if (user_char_list.isEmpty()) return null;
        
        return (Character[]) user_char_list.toArray(new Character[0]);
    }
    
    private static Character createUserCharacter(Character lookup_char, int user_id, boolean in_lineup) {
    	Character uc = new Character();
        // uc.id is set after uc is saved into the user_characters table
        uc.character_id = lookup_char.id;
        uc.user_id = user_id;
        uc.level = lookup_char.level;
        uc.strength = lookup_char.strength;
        uc.agility = lookup_char.agility;
        uc.intelligence = lookup_char.intelligence;
        uc.vitality = lookup_char.vitality;
        uc.will = lookup_char.will;
        uc.armor = lookup_char.armor;
        uc.dodge = lookup_char.dodge;
        uc.physical_crit = lookup_char.physical_crit;
        uc.spell_crit = lookup_char.spell_crit;
        uc.experience = lookup_char.experience;
        uc.max_experience = lookup_char.max_experience;
        uc.in_lineup = in_lineup;
        uc.stats = lookup_char.stats;
        uc.weapon_swap = true;
        uc.name = lookup_char.name; // This is not saved into the user_characters table
    	return uc;
    }
    
    private static void saveUserCharacter(Character user_char, Connection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("INSERT INTO user_characters(user_id, character_id, level," +
                " strength, agility, intelligence, vitality, will, armor," +
                "dodge, physical_crit, spell_crit, experience, max_experience," +
                "in_lineup, stats, weapon_swap) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

		query.setInt(1, user_char.user_id);
		query.setInt(2, user_char.character_id);
		query.setInt(3, user_char.level);
		query.setInt(4, user_char.strength);
		query.setInt(5, user_char.agility);
		query.setInt(6, user_char.intelligence);
		query.setInt(7, user_char.vitality);
		query.setInt(8, user_char.will);
		query.setDouble(9, user_char.armor);
		query.setDouble(10, user_char.dodge);
		query.setDouble(11, user_char.physical_crit);
		query.setDouble(12, user_char.spell_crit);
		query.setInt(13, user_char.experience);
		query.setInt(14, setMaxExperience(user_char.max_experience));
		query.setBoolean(15, user_char.in_lineup);
		query.setInt(16, user_char.stats);
		query.setBoolean(17, user_char.weapon_swap);
		
		query.executeUpdate();
		ResultSet gen_keys = query.getGeneratedKeys();
		if (gen_keys.next()) {
			user_char.id = gen_keys.getInt(1);
		}
    }
 
    public static void UpdateBattleUserCharacters(Character[] user_characters, Connection conn) throws SQLException {
 
        PreparedStatement query = conn.prepareStatement("UPDATE user_characters SET level=?," +
                                                                "experience=?,max_experience=?,stats=? " +
                                                                "WHERE id = ? ");

        for (int ix = 0, ixe = user_characters.length; ix < ixe; ix++) {
            Character ch = user_characters[ix];
            query.setInt(1, ch.level);
            query.setInt(2, ch.experience);
            query.setInt(3, ch.max_experience);
            query.setInt(4, ch.stats);
            query.setInt(5, ch.id);
            query.addBatch();
        } 
        query.executeBatch();
        
        // Removed since no level up is done for user character accessories
        // UserCharacterAccessoryDBSQL.updateBattleUserCharacterAccessories(user_characters, conn);
    }
    
    private static void createAndSaveUserSkill(List<Skill> lookup_skill_list, Character user_char, Connection conn) throws SQLException {
    	PreparedStatement query = conn.prepareStatement("INSERT INTO user_character_skills(user_character_id, skill_id, in_use)" +
                "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
    	List<Skill> user_char_skill_list = new ArrayList<Skill>();
    	for (Skill lookup_skill : lookup_skill_list) {
            if (lookup_skill.level_requirement != user_char.level) continue;
            Skill user_skill = new Skill();
            user_skill.user_character_id = user_char.id;
            user_skill.skill_id = lookup_skill.id;
            user_skill.in_use = true;
            user_char_skill_list.add(user_skill);
            
            // use user_skill to ensure that user_skill is consistent with what is saved in db
            query.setInt(1, user_skill.user_character_id);
            query.setInt(2, user_skill.skill_id);
            query.setBoolean(3, user_skill.in_use);
        	
    		query.executeUpdate();
    		ResultSet gen_keys = query.getGeneratedKeys();
    		if (gen_keys.next()) {
    			user_skill.id = gen_keys.getInt(1);
    		}
        }
    	
    	if (user_char_skill_list.isEmpty())
    		return;
    		
    	user_char.user_character_skills = (Skill[]) user_char_skill_list.toArray(new Skill[0]);
    }
    
    private static void createAndSaveUserAccessory(List<Accessory> lookup_acc_list, Character user_char, Connection conn) throws SQLException {
    	List<Accessory> user_accessory_list = new ArrayList<Accessory>();
    	for (Accessory lookup_acc : lookup_acc_list) {
    		Accessory user_acc = createUserAccessory(lookup_acc, user_char);	
    		UserCharacterAccessoryDBSQL.createUserCharacterAccessory(user_acc, user_char.id, conn);
    		user_accessory_list.add(user_acc);
    	}
    	
    	if (user_accessory_list.isEmpty())
    		return;
    	
    	user_char.user_character_accessories = (Accessory[]) user_accessory_list.toArray(new Accessory[0]);
    }
    
    private static Accessory createUserAccessory(Accessory lookup_acc, Character user_char) {
    	Accessory uca = new Accessory();
        uca.user_id = user_char.user_id;
        uca.user_character_id = user_char.id;
        uca.character_id = lookup_acc.character_id;
        uca.level = lookup_acc.level;
        uca.cost = lookup_acc.cost;
        uca.strength = lookup_acc.strength;
        uca.vitality = lookup_acc.vitality;
        uca.agility = lookup_acc.agility;
        uca.intelligence = lookup_acc.intelligence;
        uca.will = lookup_acc.will;
        uca.armor = lookup_acc.armor;
        uca.dodge = lookup_acc.dodge;
        uca.physical_crit = lookup_acc.physical_crit;
        uca.spell_crit = lookup_acc.spell_crit;
        uca.experience = lookup_acc.experience;
        uca.max_experience = lookup_acc.max_experience;
        uca.stats = lookup_acc.stats;
        uca.tier = lookup_acc.tier;
        uca.damage_high = lookup_acc.damage_high;
        uca.damage_low = lookup_acc.damage_low;
        uca.rarity = lookup_acc.rarity; // need to new String(lookup_acc.rarity)?
        uca.accessory_id = lookup_acc.id;
        uca.accessory_type = lookup_acc.accessory_type;// need to new String(lookup_acc.accessory_type)?
        uca.name = lookup_acc.name;// need to new String(lookup_acc.name)?
        uca.filenames = lookup_acc.filenames;
        uca.level_requirement = lookup_acc.level_requirement;
        return uca;
    }
    
    private static void getUserCharacterFromResult(ResultSet result, Character user_char, Map<Integer, String> map)
    		throws SQLException {     
	    user_char.id = result.getInt("id");
	    user_char.character_id = result.getInt("character_id");
	    user_char.user_id = result.getInt("user_id");
	    user_char.level = result.getInt("level");
	    user_char.strength = result.getInt("strength");
	    user_char.agility = result.getInt("agility");
	    user_char.intelligence = result.getInt("intelligence");
	    user_char.vitality = result.getInt("vitality");
	    user_char.will = result.getInt("will");
	    user_char.armor = result.getDouble("armor");
	    user_char.dodge = result.getDouble("dodge");
	    user_char.physical_crit = result.getDouble("physical_crit");
	    user_char.spell_crit = result.getDouble("spell_crit");
	    user_char.experience = result.getInt("experience");
	    user_char.max_experience = result.getInt("max_experience");
	    user_char.in_lineup = result.getBoolean("in_lineup");
	    user_char.stats = result.getInt("stats");
	    user_char.weapon_swap = result.getBoolean("weapon_swap");
	    user_char.name = map.get(user_char.character_id);
    }

    private static int setMaxExperience(int level) {
        int max_level = (int)Math.round((1000/(1+(1*Math.exp((10-(level))*0.358)))));
        return max_level;
    }
    
    //
    // Obsolete methods
    //
    public boolean createUserCharacters(List<Integer> character_ids, int user_id) {
    	boolean created = false;
        Connection conn = null;
        Map<Integer, Character> character_lookup_map = _c_db_sql.getCharacters(character_ids);
        Map<Integer, List<Skill>> skill_lookup_map = _sk_db_sql.getSkillsByCharacter(character_ids);
        Map<Integer, List<Accessory>> accessory_lookup_map = _acc_db_sql.getAccessoriesByCharacterForCreation(character_ids);

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement query = conn.prepareStatement("INSERT INTO user_characters(user_id, character_id, level," +
                                                                                 " strength, agility, intelligence, vitality, will, armor," +
                                                                                 "dodge, physical_crit, spell_crit, experience, max_experience," +
                                                                                 "in_lineup, stats, weapon_swap) " +
                                                                                 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            PreparedStatement query_ucs = conn.prepareStatement("INSERT INTO user_character_skills(user_character_id, skill_id, in_use)" +
                                                                                     "VALUES (?,?,?)");

            PreparedStatement query_uca = conn.prepareStatement("INSERT INTO user_character_accessories(user_id, user_character_id," +
                                                                                             "level_requirement, strength, vitality, agility, intelligence, will, armor," +
                                                                                             "dodge, physical_crit, spell_crit, experience, max_experience," +
                                                                                             "stats, tier, damage_high, damage_low, rarity, accessory_id, accessory_type, name)" +
                                                                                             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            for (int request_character_id : character_ids) {
                Character lookup_character = character_lookup_map.get(request_character_id);
                query.setInt(1, user_id);
                query.setInt(2, lookup_character.id);
                query.setInt(3, lookup_character.level);
                query.setInt(4, lookup_character.strength);
                query.setInt(5, lookup_character.agility);
                query.setInt(6, lookup_character.intelligence);
                query.setInt(7, lookup_character.vitality);
                query.setInt(8, lookup_character.will);
                query.setDouble(9, lookup_character.armor);
                query.setDouble(10, lookup_character.dodge);
                query.setDouble(11, lookup_character.physical_crit);
                query.setDouble(12, lookup_character.spell_crit);
                query.setInt(13, lookup_character.experience);
                query.setInt(14, setMaxExperience(lookup_character.max_experience));
                query.setBoolean(15, true);
                query.setInt(16, lookup_character.stats);
                query.setBoolean(17, true);

                query.executeUpdate();
                ResultSet gen_keys = query.getGeneratedKeys();
                int gen_user_character_id = 0;
                if (gen_keys.next()) {
                    gen_user_character_id = gen_keys.getInt(1);
                }

                List<Skill> lookup_skills = skill_lookup_map.get(request_character_id);
                for (Skill lookup_skill : lookup_skills) {
                    if (lookup_skill.level_requirement == lookup_character.level) {
                        query_ucs.setInt(1, gen_user_character_id);
                        query_ucs.setInt(2, lookup_skill.id);
                        query_ucs.setBoolean(3, true);
                        query_ucs.addBatch();
                    }
                }

                List<Accessory> lookup_accessories = accessory_lookup_map.get(request_character_id);
                for (Accessory lookup_accessory : lookup_accessories) {
                    if (lookup_accessory.tier == 0 ) {
                        query_uca.setInt(1, user_id);
                        query_uca.setInt(2, gen_user_character_id);
                        query_uca.setInt(3, lookup_accessory.level);
                        query_uca.setInt(4, lookup_accessory.strength);
                        query_uca.setInt(5, lookup_accessory.vitality);
                        query_uca.setInt(6, lookup_accessory.agility);
                        query_uca.setInt(7, lookup_accessory.intelligence);
                        query_uca.setInt(8, lookup_accessory.will);
                        query_uca.setDouble(9, lookup_accessory.armor);
                        query_uca.setDouble(10, lookup_accessory.dodge);
                        query_uca.setDouble(11, lookup_accessory.physical_crit);
                        query_uca.setDouble(12, lookup_accessory.spell_crit);
                        query_uca.setInt(13, lookup_accessory.experience);
                        query_uca.setInt(14, lookup_accessory.max_experience);
                        query_uca.setInt(15, lookup_accessory.stats);
                        query_uca.setInt(16, lookup_accessory.tier);
                        query_uca.setInt(17, lookup_accessory.damage_high);
                        query_uca.setInt(18, lookup_accessory.damage_low);
                        query_uca.setString(19, lookup_accessory.rarity);
                        query_uca.setInt(20, lookup_accessory.id);
                        query_uca.setString(21, lookup_accessory.accessory_type);
                        query_uca.setString(22, lookup_accessory.name);
                        query_uca.addBatch();
                    }
                }
                query_uca.executeBatch();
                query_ucs.executeBatch();
            }
            conn.commit();
            created = true;
        } catch (SQLException e) {
            rollbackConnection(conn);
	        e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
        	closeConnection(conn);
        }
        return created;
    }

    public boolean addUserCharacter(List<Integer> character_ids, int user_id) {
    	boolean added = false;
        Connection conn = null;
        Map<Integer, Character> character_lookup_map = _c_db_sql.getCharacters(character_ids);
        Map<Integer, List<Skill>> skill_lookup_map = _sk_db_sql.getSkillsByCharacter(character_ids);
        Map<Integer, List<Accessory>> accessory_lookup_map = _acc_db_sql.getAccessoriesByCharacterForCreation(character_ids);

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement query = conn.prepareStatement("INSERT INTO user_characters(user_id, character_id, level," +
                                                                                 " strength, agility, intelligence, vitality, will, armor," +
                                                                                 "dodge, physical_crit, spell_crit, experience, max_experience," +
                                                                                 "in_lineup, stats, weapon_swap) " +
                                                                                 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            PreparedStatement query_ucs = conn.prepareStatement("INSERT INTO user_character_skills(user_character_id, skill_id, in_use)" +
                                                                                     "VALUES (?,?,?)");

            PreparedStatement query_uca = conn.prepareStatement("INSERT INTO user_character_accessories(user_id, user_character_id," +
                                                                                             "level_requirement, strength, vitality, agility, intelligence, will, armor," +
                                                                                             "dodge, physical_crit, spell_crit, experience, max_experience," +
                                                                                             "stats, tier, damage_high, damage_low, rarity, accessory_id, accessory_type, name)" +
                                                                                             "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            for (int request_character_id : character_ids) {
                Character lookup_character = character_lookup_map.get(request_character_id);
                query.setInt(1, user_id);
                query.setInt(2, lookup_character.id);
                query.setInt(3, lookup_character.level);
                query.setInt(4, lookup_character.strength);
                query.setInt(5, lookup_character.agility);
                query.setInt(6, lookup_character.intelligence);
                query.setInt(7, lookup_character.vitality);
                query.setInt(8, lookup_character.will);
                query.setDouble(9, lookup_character.armor);
                query.setDouble(10, lookup_character.dodge);
                query.setDouble(11, lookup_character.physical_crit);
                query.setDouble(12, lookup_character.spell_crit);
                query.setInt(13, lookup_character.experience);
                query.setInt(14, setMaxExperience(lookup_character.max_experience));
                query.setBoolean(15, false);
                query.setInt(16, lookup_character.stats);
                query.setBoolean(17, true);

                query.executeUpdate();
                ResultSet gen_keys = query.getGeneratedKeys();
                int gen_user_character_id = 0;
                if (gen_keys != null && gen_keys.next()) {
                    gen_user_character_id = gen_keys.getInt(1);
                }

                List<Skill> lookup_skills = skill_lookup_map.get(request_character_id);
                for (Skill lookup_skill : lookup_skills) {
                    if (lookup_skill.level_requirement == lookup_character.level) {
                        query_ucs.setInt(1, gen_user_character_id);
                        query_ucs.setInt(2, lookup_skill.id);
                        query_ucs.setBoolean(3, true);
                        query_ucs.addBatch();
                    }
                }

                List<Accessory> lookup_accessories = accessory_lookup_map.get(request_character_id);
                for (Accessory lookup_accessory : lookup_accessories) {
                    if (lookup_accessory.tier == 0 ) {
                        query_uca.setInt(1, user_id);
                        query_uca.setInt(2, gen_user_character_id);
                        query_uca.setInt(3, lookup_accessory.level);
                        query_uca.setInt(4, lookup_accessory.strength);
                        query_uca.setInt(5, lookup_accessory.vitality);
                        query_uca.setInt(6, lookup_accessory.agility);
                        query_uca.setInt(7, lookup_accessory.intelligence);
                        query_uca.setInt(8, lookup_accessory.will);
                        query_uca.setDouble(9, lookup_accessory.armor);
                        query_uca.setDouble(10, lookup_accessory.dodge);
                        query_uca.setDouble(11, lookup_accessory.physical_crit);
                        query_uca.setDouble(12, lookup_accessory.spell_crit);
                        query_uca.setInt(13, lookup_accessory.experience);
                        query_uca.setInt(14, lookup_accessory.max_experience);
                        query_uca.setInt(15, lookup_accessory.stats);
                        query_uca.setInt(16, lookup_accessory.tier);
                        query_uca.setInt(17, lookup_accessory.damage_high);
                        query_uca.setInt(18, lookup_accessory.damage_low);
                        query_uca.setString(19, lookup_accessory.rarity);
                        query_uca.setInt(20, lookup_accessory.id);
                        query_uca.setString(21, lookup_accessory.accessory_type);
                        query_uca.setString(22, lookup_accessory.name);
                        query_uca.addBatch();
                    }
                }
                query_uca.executeBatch();
                query_ucs.executeBatch();
            }
            conn.commit();

            added = true;
        } catch (SQLException e) {
            rollbackConnection(conn);
	        e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
        	closeConnection(conn);
        }
        return added;
    }
    

    public List<CharacterWrapper> getAllUserCharactersByUserID(int user_id) {
    	
        Connection conn = null;
        List<CharacterWrapper> user_character_list = null;

        try {
            conn = getConnection();
            Map<Integer, String> map = CharacterDBSQL.getCharacterMap(conn);
            user_character_list = new ArrayList<CharacterWrapper>();
            PreparedStatement query = conn.prepareStatement("SELECT id,character_id,user_id,level,strength,agility," +
                                                                    "intelligence,vitality,will,armor,dodge," +
                                                                    "physical_crit,spell_crit,experience,max_experience," +
                                                                    "in_lineup,stats,weapon_swap FROM user_characters WHERE user_id = ? ");

            query.setInt(1, user_id);
            ResultSet result = query.executeQuery();

            if (result != null) {
                while (result.next()) {
                    CharacterWrapper ucw = new CharacterWrapper();
                    Character uc = ucw.character;
                    uc.id = result.getInt("id");
                    uc.character_id = result.getInt("character_id");
                    uc.user_id = result.getInt("user_id");
                    uc.level = result.getInt("level");
                    uc.strength = result.getInt("strength");
                    uc.agility = result.getInt("agility");
                    uc.intelligence = result.getInt("intelligence");
                    uc.vitality = result.getInt("vitality");
                    uc.will = result.getInt("will");
                    uc.armor = result.getDouble("armor");
                    uc.dodge = result.getDouble("dodge");
                    uc.physical_crit = result.getDouble("physical_crit");
                    uc.spell_crit = result.getDouble("spell_crit");
                    uc.experience = result.getInt("experience");
                    uc.max_experience = result.getInt("max_experience");
                    uc.in_lineup = result.getBoolean("in_lineup");
                    uc.stats = result.getInt("stats");
                    uc.weapon_swap = result.getBoolean("weapon_swap");
                    uc.name = map.get(uc.character_id);
                    
                    uc.user_character_accessories = UserCharacterAccessoryDBSQL.getUserCharacterAccessories(uc.id, conn); 
                    uc.user_character_skills = UserCharacterSkillDBSQL.getAllSkillsForUserCharacter(uc.id, conn);
                    user_character_list.add(ucw);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        return user_character_list;
    }

    public static void addStatPointsToCharacter(User user, Character uc) {
        for (UserAchievement ua : user.user_achievements) {
            if (ua.character_id == uc.character_id) {
                uc.stats++;
            }
        }
    }
}
