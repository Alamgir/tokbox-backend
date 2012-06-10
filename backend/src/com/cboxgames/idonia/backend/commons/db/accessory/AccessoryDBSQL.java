package com.cboxgames.idonia.backend.commons.db.accessory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.AccessorySkill;
import com.cboxgames.utils.idonia.types.AccessorySkillEffect;
import com.cboxgames.utils.idonia.types.Accessory.AccessoryWrapper;
import com.cboxgames.utils.idonia.types.AccessorySkill.AccessorySkillWrapper;
import com.cboxgames.utils.idonia.types.AccessorySkillEffect.AccessorySkillEffectWrapper;

public class AccessoryDBSQL extends DBSQL implements IAccessoryDB {
	
	private static int CHARACTER_ID_ALL = 0;

	public AccessoryDBSQL( DataSource connection_pool,
			ServletContext servlet_context ) throws SQLException {
		super(connection_pool, servlet_context);
	}

	public List<Accessory> getAccessories(AccessoryType type) {
		return getAccessories(type, CHARACTER_ID_ALL);
	}

	public List<Accessory> getAccessories( AccessoryType type, int character_type ) {
		List<Accessory> accessories = new ArrayList<Accessory>();
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM accessories WHERE accessory_type = ?";
			if (character_type != CHARACTER_ID_ALL)
				query += " AND character_id = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, type.getId());
			if (character_type != CHARACTER_ID_ALL)
				statement.setInt(2, character_type);
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Accessory acc = new Accessory();
				getAccessoryFromResult(result, acc);
				accessories.add(acc);		
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return accessories;
	}

	public Accessory getAccessory( int id ) {
		Accessory accessory = null;
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM accessories WHERE id = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, id);
			
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				accessory = new Accessory();
				getAccessoryFromResult(result, accessory);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return accessory;
	}

    public List<Accessory> getAccessoryByNodeID(int node_id) {
        List<Accessory> accessory_list = new ArrayList<Accessory>();
        Connection conn = null;

        try {
			conn = getConnection();

			String query = "SELECT * FROM accessories WHERE node_id = ?";

			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, node_id);

			ResultSet result = statement.executeQuery();
            if (result != null) {
                while (result.next()) {
                    Accessory accessory = new Accessory();
                    getAccessoryFromResult(result, accessory);
                    accessory_list.add(accessory);
                }
            }

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}

		return accessory_list;
    }

    public Map<Integer, List<Accessory>> getAccessoriesByCharacter(List<Integer> ids) {
        Map<Integer, List<Accessory>> map = new HashMap<Integer, List<Accessory>>();
        if (ids == null || ids.size() <= 0)
            return map;

        Connection conn = null;

        try {
            conn = getConnection();

            // Fill the hashmap with lists for each character ID.
            for (Integer id : ids) {
                map.put(id, new ArrayList<Accessory>());
            }

            StringBuilder query = new StringBuilder(
                    "SELECT * FROM accessories a INNER JOIN skill_effects b ON a.id = b.skill_id WHERE a.character_id=");
            query.append(ids.get(0));
            for (int x=1;x<ids.size();x++) {
                Integer id = ids.get(x);
                query.append(" OR a.character_id=");
                query.append(id);
            }

            PreparedStatement statement = conn.prepareStatement(query.toString());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
        		Accessory acc = new Accessory();
                getAccessoryFromResult(result, acc);
                map.get(acc.character_id).add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return map;
    }

    public Map<Integer, List<Accessory>> getAccessoriesByCharacterForCreation(List<Integer> ids) {
    	
        Connection conn = null;

        Map<Integer, List<Accessory>> map = new HashMap<Integer, List<Accessory>>();
        if (ids == null || ids.size() <= 0)
            return map;

        try {
            conn = getConnection();

            // Fill the hashmap with lists for each character ID.
            for (Integer id : ids) {
                map.put(id, new ArrayList<Accessory>());
            }

            StringBuilder query = new StringBuilder("SELECT * FROM accessories a WHERE a.tier = 0 AND (a.character_id=");
            query.append(ids.get(0));
            int ids_size = ids.size();
            for (int x = 1; x < ids_size; x++) {
                query.append(" OR a.character_id=");
                query.append(ids.get(x));
            }
            query.append(")");

            PreparedStatement statement = conn.prepareStatement(query.toString());

            ResultSet result = statement.executeQuery();
            while (result.next()) {
        		Accessory acc = new Accessory();
                getAccessoryFromResult(result, acc);
                map.get(acc.character_id).add(acc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return map;
    }

	@Override
	public AccessoryWrapper[] getAccessoryDetails() {
		
		List<AccessoryWrapper> accw_list = new ArrayList<AccessoryWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM accessories");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				AccessoryWrapper accw = new AccessoryWrapper();
				getAccessoryFromResult(result, accw.accessory);
				accw_list.add(accw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		if (accw_list.isEmpty())
			return null;
		
		return (AccessoryWrapper[]) accw_list.toArray(new AccessoryWrapper[0]);
	}
	
	public AccessorySkillWrapper[] getAccessorySkillDetails() {
		
		List<AccessorySkillWrapper> asw_list = new ArrayList<AccessorySkillWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM accessory_skills");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				AccessorySkillWrapper asw = new AccessorySkillWrapper();
				getAccessorySkillFromResult(result, asw.accessory_skill);
				asw_list.add(asw);
			}
			
			Map<Integer, AccessorySkillEffect> mse_map = getAccessorySkillEffectMap(conn);
			for (AccessorySkillWrapper asw : asw_list)
            	asw.accessory_skill.accessory_skill_effect = mse_map.get(asw.accessory_skill.id);

//			PreparedStatement query  = conn.prepareStatement("SELECT * FROM accessory_skill_effects WHERE accessory_skill_id = ?");
//			for (AccessorySkillWrapper asw : asw_list) {				
//				query.setInt(1, asw.accessory_skill.id);
//				ResultSet res = query.executeQuery();
//				if ((res == null) || !res.next()) continue;
//				
//				AccessorySkillEffect ase = asw.accessory_skill.accessory_skill_effect = new AccessorySkillEffect();
//				getAccessorySkillEffectFromResult(res, ase);
//			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		if (asw_list.isEmpty())
			return null;
		
		return (AccessorySkillWrapper[]) asw_list.toArray(new AccessorySkillWrapper[0]);
	}
	
	public AccessorySkillEffectWrapper[] getAccessorySkillEffectDetails() {
		
		List<AccessorySkillEffectWrapper> asw_list = new ArrayList<AccessorySkillEffectWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM accessory_skill_effects");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				AccessorySkillEffectWrapper asw = new AccessorySkillEffectWrapper();
				getAccessorySkillEffectFromResult(result, asw.accessory_skill_effect);
				asw_list.add(asw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		if (asw_list.isEmpty())
			return null;
		
		return (AccessorySkillEffectWrapper[]) asw_list.toArray(new AccessorySkillEffectWrapper[0]);
	}
	
	// static methods
	public static Map<Integer, AccessorySkillEffect> getAccessorySkillEffectMap(Connection conn) throws SQLException  {
		Map<Integer, AccessorySkillEffect> map = new HashMap<Integer, AccessorySkillEffect>();
		
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM accessory_skill_effects");
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			AccessorySkillEffect ase = new AccessorySkillEffect();
			getAccessorySkillEffectFromResult(result, ase);
			map.put(ase.accessory_skill_id, ase);
        }

		return map;
	}

	/**
	 * Get an accessory object from the result set.  ResultSet.next() must be called prior to calling
	 * this method.
	 * @param acc
	 * @param result
	 * @return
	 * @throws SQLException 
	 */
	private static void getAccessoryFromResult(ResultSet result, Accessory acc) throws SQLException {	
		acc.id =  result.getInt("id");
        acc.accessory_id = result.getInt("id");
		acc.accessory_type = AccessoryType.idToName(result.getInt("accessory_type"));
		acc.agility = result.getInt("agility");
		acc.armor = result.getFloat("armor");
		acc.character_id = result.getInt("character_id");
		acc.cost = result.getInt("cost");
		acc.damage_high = result.getInt("damage_high");
		acc.damage_low = result.getInt("damage_low");
		acc.description = result.getString("description");
		acc.dodge = result.getFloat("dodge");
		acc.generated = result.getBoolean("generated");
		acc.intelligence = result.getInt("intelligence");
		//	acc.level = result.getInt("level"); // removed because not in schema
		acc.level_requirement = result.getInt("level_requirement");
		acc.name = result.getString("name");
		acc.physical_crit = result.getFloat("physical_crit");
		acc.rarity = result.getString("rarity");
		acc.spell_crit = result.getFloat("spell_crit");
		acc.strength = result.getInt("strength");
		acc.tier = result.getInt("tier");
		acc.vitality = result.getInt("vitality");
		acc.will = result.getInt("will");
        acc.rating_requirement = result.getInt("rating_requirement");
        acc.filenames = result.getString("filenames");
        acc.premium = result.getBoolean("premium");
	}
	
	private static void getAccessorySkillFromResult(ResultSet result, AccessorySkill ms) throws SQLException {
		
		ms.id = result.getInt("id");
		ms.accessory_id = result.getInt("accessory_id");
		ms.name = result.getString("name");
		ms.description = result.getString("description");
		ms.damage_low = result.getInt("damage_low");
		ms.damage_high = result.getInt("damage_high");
		ms.damage_type = result.getString("damage_type");
		ms.damage_subtype = result.getString("damage_subtype");
		ms.spell_target = result.getString("spell_target");
		ms.multiplier = result.getDouble("multiplier");
		ms.weight = result.getInt("weight");
	}
	
	private static void getAccessorySkillEffectFromResult(ResultSet result, AccessorySkillEffect ase) throws SQLException {
	
		ase.id = result.getInt("id");
		ase.accessory_skill_id = result.getInt("accessory_skill_id");
		ase.strength = result.getDouble("strength");
		ase.intelligence = result.getDouble("intelligence");
		ase.vitality = result.getDouble("vitality");
		ase.will = result.getDouble("will");
		ase.physical_crit = result.getDouble("physical_crit");
		ase.skill_crit = result.getDouble("skill_crit");
		ase.armor = result.getDouble("armor");
		ase.dodge = result.getDouble("dodge");
		ase.turn = result.getInt("turn");
		ase.damage = result.getInt("damage");
		ase.agility = result.getDouble("agility");
		ase.multiplier = result.getDouble("multiplier");
		ase.effect_type = result.getString("effect_type");
		ase.subtype = result.getString("subtype");
		ase.duration_type = result.getString("duration_type");
		ase.remove_number = result.getInt("remove_number");
		ase.stackable = result.getBoolean("stackable");
		ase.stack_limit = result.getInt("stack_limit");
		ase.proc = result.getInt("proc");
	}
	
	public static Map<Integer, List<Accessory>> getAccessoryMap(List<Integer> ids, Connection conn) throws SQLException {
        Map<Integer, List<Accessory>> map = new HashMap<Integer, List<Accessory>>();
        int ids_size = ids.size();
        if (ids == null || ids_size <= 0)
            return map;

        // Fill the hashmap with lists for each character ID.
        for (Integer id : ids) {
            map.put(id, new ArrayList<Accessory>());
        }

        StringBuilder query = new StringBuilder("SELECT * FROM accessories a WHERE a.tier = 0 AND (a.character_id=");
        query.append(ids.get(0));
        for (int x = 1; x < ids_size; x++) {
            query.append(" OR a.character_id=");
            query.append(ids.get(x));
        }
        query.append(")");

        PreparedStatement statement = conn.prepareStatement(query.toString());

        ResultSet result = statement.executeQuery();
        while (result.next()) {
    		Accessory acc = new Accessory();
            getAccessoryFromResult(result, acc);
            map.get(acc.character_id).add(acc);
        }

        return map;
    }
}
