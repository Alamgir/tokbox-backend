package com.cboxgames.idonia.backend.commons.db.skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Skill;
import com.cboxgames.utils.idonia.types.SkillEffect;
import com.cboxgames.utils.idonia.types.Skill.SkillWrapper;
import com.cboxgames.utils.idonia.types.SkillEffect.SkillEffectWrapper;

public class SkillDBSQL extends DBSQL implements ISkillDB {

	public SkillDBSQL(DataSource connection_pool, ServletContext servlet_context)
			throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public List<Skill> getSkills() {
		List<Skill> skills = new ArrayList<Skill>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM skills a INNER JOIN skill_effects b ON a.id = b.skill_id");
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Skill s = new Skill();
				getSkillFromResult(result, s);
				skills.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return skills;
	}

	@Override
	public Skill getSkill(int id) {
		Skill s = null;
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM skills WHERE id = ?");
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			if ((result == null) || (result.next() == false))
				return null;
			
			s = new Skill();
			getSkillFromResult(result, s);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return s;
	}

	@Override
	public Map<Integer, List<Skill>> getSkillsByCharacter(List<Integer> ids) {
		Map<Integer, List<Skill>> map = new HashMap<Integer, List<Skill>>();
		if (ids == null || ids.size() <= 0)
			return map;
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			// Fill the hashmap with lists for each character ID.
			for (Integer id : ids) {
				map.put(id, new ArrayList<Skill>());
			}
			
			StringBuilder query = new StringBuilder("SELECT " +
                                                            "a.id as skill_id," +
                                                            "a.character_id as skill_character_id," +
                                                            "a.name as skill_name," +
                                                            "a.description as skill_description," +
                                                            "a.damage_low as skill_damage_low," +
                                                            "a.damage_high as skill_damage_high," +
                                                            "a.damage_subtype as skill_damage_subtype," +
                                                            "a.damage_type as skill_damage_type," +
                                                            "a.spell_target as skill_spell_target," +
                                                            "a.level_requirement as skill_level_requirement," +
                                                            "a.cost as skill_cost," +
                                                            "a.mana_points as skill_mana_points," +
                                                            "a.multiplier as skill_multiplier," +
                                                            "a.skill_type as skill_type," +
                                                            "a.refresh_time as skill_refresh_time," +
                                                            "a.refresh_cost as skill_refresh_cost," +
                                                            "a.premium as skill_premium," +
                                                            "a.special as skill_special" +
                                                            " FROM skills as a WHERE a.level_requirement = 1 AND (a.character_id=");
			query.append(ids.get(0));
            if (ids.size()>1) {
                for (int x=1;x<ids.size();x++) {
                    Integer id = ids.get(x);
                    if (x==ids.size()-1) {
                        query.append(" OR a.character_id=");
                        query.append(id);
                        query.append(")");
                    }
                    else {
                        query.append(" OR a.character_id=");
                        query.append(id);
                    }
                }
            }
            else {
                query.append(")");
            }
			PreparedStatement statement = conn.prepareStatement(query.toString());
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Skill s = new Skill();
				getSkillFromResultForUserCharacters(result, s);
				map.get(s.character_id).add(s);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return map;
	}

	public SkillWrapper[] getSkillDetails() {
		
		List<SkillWrapper> sw_list = new ArrayList<SkillWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM skills ORDER BY premium ASC, cost ASC");
			ResultSet result = statement.executeQuery();
            if (result != null) {
                while (result.next()) {
                    SkillWrapper sw = new SkillWrapper();
                    getSkillFromResult(result, sw.skill);
                    sw_list.add(sw);
                }
            }

            Map<Integer, SkillEffect> se_map = getSkillEffectMap(conn);
            for (SkillWrapper sw : sw_list)
            	sw.skill.skill_effect = se_map.get(sw.skill.id);
            
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (SkillWrapper[]) sw_list.toArray(new SkillWrapper[0]);
	}

	public SkillEffectWrapper[] getSkillEffectDetails() {
		List<SkillEffectWrapper> se_list = new ArrayList<SkillEffectWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM skill_effects");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				SkillEffectWrapper sew = new SkillEffectWrapper();
				getSkillEffectFromResult(result, sew.skill_effect);
				se_list.add(sew);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (SkillEffectWrapper[]) se_list.toArray(new SkillEffectWrapper[0]);
	}

	// static method
	public static Map<Integer, SkillEffect> getSkillEffectMap(Connection conn) throws SQLException  {
		Map<Integer, SkillEffect> map = new HashMap<Integer, SkillEffect>();
		
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM skill_effects");
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			SkillEffect se = new SkillEffect();
			getSkillEffectFromResult(result, se);
			map.put(se.skill_id, se);
        }

		return map;
	}
	
	public static Map<Integer, List<Skill>> getSkillMap(List<Integer> ids, Connection conn) throws SQLException  {
		Map<Integer, List<Skill>> map = new HashMap<Integer, List<Skill>>();
		int ids_size = ids.size();
		if (ids == null || ids_size <= 0)
			return map;
		
		// Fill the hashmap with lists for each character ID.
		for (Integer id : ids) {
			map.put(id, new ArrayList<Skill>());
		}
		
		StringBuilder query = new StringBuilder("SELECT * FROM skills as a WHERE a.level_requirement = 1 AND (a.character_id = ");
		query.append(ids.get(0));
        if (ids_size > 1) {
            for (int ix = 1; ix < ids_size; ix++) {
                query.append(" OR a.character_id = ");
                query.append(ids.get(ix));
            }
        }    
        query.append(")");
        
		PreparedStatement statement = conn.prepareStatement(query.toString());
		
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			Skill s = new Skill();
			getSkillFromResult(result, s);
			map.get(s.character_id).add(s);
        }

		return map;
	}
	
	private static void getSkillFromResult(ResultSet result, Skill s) throws SQLException {
		s.id = result.getInt("id");
		s.character_id = result.getInt("character_id");
		s.name = result.getString("name");
		s.description = result.getString("description");
		s.damage_low = result.getInt("damage_low");
		s.damage_high = result.getInt("damage_high");
		s.damage_subtype = result.getString("damage_subtype");
		s.damage_type = result.getString("damage_type");
		s.spell_target = result.getString("spell_target");
		s.level_requirement = result.getInt("level_requirement");
		s.cost = result.getInt("cost");
		s.mana_points = result.getInt("mana_points");
		s.multiplier = result.getFloat("multiplier");
		s.special = result.getBoolean("special");
        s.skill_type = SkillType.idToName(result.getInt("skill_type"));
        s.refresh_time = result.getInt("refresh_time");
        s.refresh_cost = result.getInt("refresh_cost");
        s.premium = result.getBoolean("premium");
	}

    public static void getSkillFromResultForUserCharacters(ResultSet result, Skill s) throws SQLException {
        s.id = result.getInt("skill_id");
		s.character_id = result.getInt("skill_character_id");
		s.name = result.getString("skill_name");
		s.description = result.getString("skill_description");
		s.damage_low = result.getInt("skill_damage_low");
		s.damage_high = result.getInt("skill_damage_high");
		s.damage_subtype = result.getString("skill_damage_subtype");
		s.damage_type = result.getString("skill_damage_type");
		s.spell_target = result.getString("skill_spell_target");
		s.level_requirement = result.getInt("skill_level_requirement");
		s.cost = result.getInt("skill_cost");
		s.mana_points = result.getInt("skill_mana_points");
		s.multiplier = result.getFloat("skill_multiplier");
		s.special = result.getBoolean("skill_special");
        s.skill_type = SkillType.idToName(result.getInt("skill_type"));
        s.refresh_time = result.getInt("skill_refresh_time");
        s.refresh_cost = result.getInt("skill_refresh_cost");
        s.premium = result.getBoolean("skill_premium");
    }
	
	private static void getSkillEffectFromResult(ResultSet result, SkillEffect se) throws SQLException {
		se.id = result.getInt("id");
		se.skill_id = result.getInt("skill_id");
		se.strength = result.getFloat("strength");
		se.intelligence = result.getFloat("intelligence");
		se.vitality = result.getFloat("vitality");
		se.will = result.getFloat("will");
		se.physical_crit = result.getFloat("physical_crit");
		se.skill_crit = result.getFloat("skill_crit");
		se.armor = result.getFloat("armor");
		se.dodge = result.getFloat("dodge");
		se.turn = result.getInt("turn");
		se.damage = result.getInt("damage")	;
		se.agility = result.getFloat("agility");
		se.multiplier = result.getFloat("multiplier");
		se.effect_type = result.getString("effect_type");
		se.subtype = result.getString("subtype");
		se.duration_type = result.getString("duration_type");
		se.remove_number = result.getInt("remove_number");
		se.stack_limit = result.getInt("stack_limit");
		se.stackable = result.getBoolean("stackable");
		se.proc = result.getFloat("proc");
	}
}
