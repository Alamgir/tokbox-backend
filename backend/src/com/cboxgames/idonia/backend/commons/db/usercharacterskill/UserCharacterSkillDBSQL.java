package com.cboxgames.idonia.backend.commons.db.usercharacterskill;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest.*;

import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Skill;
import com.cboxgames.utils.idonia.types.Skill.*;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/12/11
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCharacterSkillDBSQL extends DBSQL implements IUserCharacterSkillDB{

    public UserCharacterSkillDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
        super(data_source, servlet_context);
    }

    @Override
    public int createUserCharacterSkill(SkillBuy skill_buy, boolean in_use) {
        Connection conn = null;
        int id = -1;

        try {
            conn = getConnection();
            PreparedStatement create_ucs = conn.prepareStatement("INSERT INTO user_character_skills(user_character_id," +
                                                                         "skill_id, in_use)" +
                                                                         "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            create_ucs.setInt(1, skill_buy.user_character_id);
            create_ucs.setInt(2, skill_buy.skill_id);
            create_ucs.setBoolean(3, in_use);
            create_ucs.executeUpdate();
            
            ResultSet gen_keys = create_ucs.getGeneratedKeys();
            if (gen_keys.next()) {
                id = gen_keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return id;
    }

    @Override
    public boolean putUserCharacterSkillsInUse(List<Integer> skill_ids) {
        Connection conn = null;
        boolean updated = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement update_ucs = conn.prepareStatement("UPDATE user_character_skills SET in_use=? " +
                                                                  "WHERE id = ?");
            for (int skill_id : skill_ids) {
                update_ucs.setBoolean(1, true);
                update_ucs.setInt(2, skill_id);
                update_ucs.addBatch();
            }
            update_ucs.executeBatch();
            conn.commit();

            updated = true;
        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return updated;
    }

    @Override
    public boolean setAllUserCharacterSkillsFalse(int user_character_id) {
        Connection conn = null;
        boolean updated = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement update_ucs = conn.prepareStatement("UPDATE user_character_skills SET in_use=? " +
                                                                  "WHERE user_character_id = ?");
            update_ucs.setBoolean(1, false);
            update_ucs.setInt(2, user_character_id);
            update_ucs.executeUpdate();
            conn.commit();
            updated = true;
        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return updated;
    }

    @Override
    public List<Skill> getAllSkillsForUserCharacter(int user_character_id) {
    	
        Connection conn = null;
        List<Skill> user_character_skills = null;
        
        try {
            conn = getConnection();
            user_character_skills = new ArrayList<Skill>();
            PreparedStatement get_ucs = conn.prepareStatement("SELECT id,skill_id,in_use FROM user_character_skills WHERE user_character_id = ?");
            get_ucs.setInt(1, user_character_id);
            ResultSet results = get_ucs.executeQuery();

            if (results != null) {
                while (results.next()) {
                    Skill skill = new Skill();
                    skill.id = results.getInt("id");
                    skill.user_character_id = user_character_id;
                    skill.skill_id = results.getInt("skill_id");
                    skill.in_use = results.getBoolean("in_use");
                    user_character_skills.add(skill);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return user_character_skills;
    }
 
    @Override
    public boolean checkUserCharacterSkill(int user_character_id, int skill_id) {
        Connection conn = null;
        boolean rc = false;
        try {
            conn = getConnection();

            PreparedStatement get_ucs = conn.prepareStatement("SELECT id FROM user_character_skills WHERE user_character_id = ? AND skill_id = ?");
            get_ucs.setInt(1, user_character_id);
            get_ucs.setInt(2, skill_id);
            ResultSet results = get_ucs.executeQuery();

            if (results != null) rc = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return rc;
    }
    
    // static methods
    public static void updateUserCharacterSkills(UserCharacterUpdate[] ucu_requests, Connection conn) throws SQLException {

        // Set all skills for all user_character's to false and switch skill in_use to true
        PreparedStatement update_ucs_false = conn.prepareStatement("UPDATE user_character_skills SET in_use = ? " +
                "WHERE user_character_id = ?");
        PreparedStatement update_ucs_true = conn.prepareStatement("UPDATE user_character_skills SET in_use = ? " +
                "WHERE id = ?");
        
        boolean any_ucs_false = false, any_ucs_true = false;
        for (UserCharacterUpdate ucu : ucu_requests) {
        	update_ucs_false.setBoolean(1, false);
            update_ucs_false.setInt(2, ucu.id);
            update_ucs_false.addBatch();
            any_ucs_false = true;
            if (ucu.user_character_skills == null) continue;
            
            for (UserCharacterSkills skill : ucu.user_character_skills) {
            	update_ucs_true.setBoolean(1, true);
            	update_ucs_true.setInt(2, skill.id);
            	update_ucs_true.addBatch();
            	any_ucs_true = true;
            }
        }
        if (any_ucs_false)
        	update_ucs_false.executeBatch();
        if (any_ucs_true)
        	update_ucs_true.executeBatch();
    }

    public static Skill[] getAllSkillsForUserCharacter(int user_character_id, Connection conn) throws SQLException {
    	
        List<Skill> user_character_skills = new ArrayList<Skill>();
        
        PreparedStatement get_ucs = conn.prepareStatement("SELECT id,skill_id,in_use FROM user_character_skills WHERE user_character_id = ?");
        get_ucs.setInt(1, user_character_id);
        ResultSet results = get_ucs.executeQuery();

        if (results == null) return null;
        
        while (results.next()) {
            Skill skill = new Skill();
            skill.id = results.getInt("id");
            skill.user_character_id = user_character_id;
            skill.skill_id = results.getInt("skill_id");
            skill.in_use = results.getBoolean("in_use");
            user_character_skills.add(skill);
        }
        
        if (user_character_skills.isEmpty())
        	return null;
     
        return (Skill[]) user_character_skills.toArray(new Skill[0]);
    }
    
    public static boolean getUserCharacterSkills(List<Character> user_char_list, Connection conn)
    		throws SQLException {
    	
    	if ((user_char_list == null) || (user_char_list.size() == 0))
    		return false;
    	
    	boolean first = true;
    	Iterator<Character> itr = user_char_list.iterator();
       	StringBuilder query = new StringBuilder("SELECT id,user_character_id,skill_id,in_use FROM user_character_skills WHERE ");;
        while (itr.hasNext()) {
        	if (!first) query.append(" OR ");
        	else first = false;
        	Character user_char = itr.next();
            query.append(" user_character_id = " + user_char.id);
        }

        PreparedStatement statement = conn.prepareStatement(query.toString());
        ResultSet results = statement.executeQuery();
        if (results == null) return false;
        
        Map<Integer, List<Skill>> map = new HashMap<Integer, List<Skill>>();
        for (Character uc : user_char_list) {
            map.put(uc.id, new ArrayList<Skill>());
        }
        
        while (results.next()) {
            Skill ucs = new Skill();
            ucs.id = results.getInt("id");
            ucs.user_character_id = results.getInt("user_character_id");
            ucs.skill_id = results.getInt("skill_id");
            ucs.in_use = results.getBoolean("in_use");
            
            List<Skill> ucs_list = map.get(ucs.user_character_id);
            ucs_list.add(ucs);
        }
        
        for (Character uc : user_char_list) {
        	List<Skill> ucs_list = map.get(uc.id);
        	if (ucs_list.isEmpty()) continue;
        	uc.user_character_skills = ucs_list.toArray(new Skill[0]);
        }
        
        return true;
    }
}
