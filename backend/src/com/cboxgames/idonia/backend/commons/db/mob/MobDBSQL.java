package com.cboxgames.idonia.backend.commons.db.mob;

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
import com.cboxgames.utils.idonia.types.Mob;
import com.cboxgames.utils.idonia.types.MobSkill;
import com.cboxgames.utils.idonia.types.MobSkillEffect;
import com.cboxgames.utils.idonia.types.Mob.MobWrapper;
import com.cboxgames.utils.idonia.types.MobSkill.MobSkillWrapper;
import com.cboxgames.utils.idonia.types.MobSkillEffect.MobSkillEffectWrapper;


public class MobDBSQL extends DBSQL implements IMobDB {

	public MobDBSQL(DataSource connection_pool, ServletContext servlet_context)
			throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public List<Mob> getMobs() {
		List<Mob> mobs = new ArrayList<Mob>();
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM mobs");
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Mob mob = new Mob();
				getMobFromResult(result, mob);
				mobs.add(mob);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return mobs;
	}

    @Override
    public List<Mob> getMobsByDifficulty(String difficulty) {
        List<Mob> mobs = new ArrayList<Mob>();

		Connection conn = null;

		try {
			conn = getConnection();

			PreparedStatement statement = conn.prepareStatement("SELECT * FROM mobs WHERE difficulty_type = ?");
            statement.setString(1, difficulty);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Mob mob = new Mob();
				getMobFromResult(result, mob);
				mobs.add(mob);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}

		return mobs;
    }

    @Override
	public Map<Integer, Mob> getMobs(List<Integer> mob_ids) {
		Map<Integer, Mob> map = new HashMap<Integer, Mob>(0);
		
		if (mob_ids == null || mob_ids.size() <= 0)
			return map;
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			StringBuilder query = new StringBuilder();
			
			query.append("SELECT * FROM mobs WHERE");
			query.append(" id=");
			query.append(mob_ids.remove(0));
			
			for (Integer id : mob_ids)  {
				query.append(" OR id=");
				query.append(id);
			}
			
			PreparedStatement statement = conn.prepareStatement(query.toString());
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Mob mob = new Mob();
				getMobFromResult(result, mob);
				map.put(mob.id, mob);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return map;
	}
	public MobWrapper[] getMobDetails() {
		
		List<MobWrapper> mw_list = new ArrayList<MobWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM mobs");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				MobWrapper mw = new MobWrapper();
				getMobFromResult(result, mw.mob);
				mw_list.add(mw);
			}
			
			Map<String, List<MobSkill>> ms_map = getMosSkillMap(conn);
			for (MobWrapper mw : mw_list) {
				List<MobSkill> ms_list = ms_map.get(mw.mob.mob_type);
				if (ms_list.isEmpty() == false)
					mw.mob.mob_skills = (MobSkill[]) ms_list.toArray(new MobSkill[0]);		
			}
			
//			PreparedStatement ms_query  = conn.prepareStatement("SELECT * FROM mob_skills WHERE mob_type = ?");
//			PreparedStatement mse_query = conn.prepareStatement("SELECT * FROM mob_skill_effects WHERE mob_skill_id = ?");
//			for (MobWrapper mw : mw_list) {
//				ms_query.setString(1, mw.mob.mob_type);
//				result = ms_query.executeQuery();
//				if (result == null) continue;
//				
//				List<MobSkill> ms_list = new ArrayList<MobSkill>();
//				while (result.next()) {
//					MobSkill ms = new MobSkill();
//					getMobSkillFromResult(result, ms);
//					ms_list.add(ms);
//						
//					mse_query.setInt(1, ms.id);
//					ResultSet res = mse_query.executeQuery();
//					if ((res == null) || !res.next()) continue;
//						
//					MobSkillEffect mse = ms.mob_skill_effect = new MobSkillEffect();
//					getMobSkillEffectFromResult(res, mse);
//				}
//				
//				if (ms_list.isEmpty()) continue;
//				
//				mw.mob.mob_skills = (MobSkill[]) ms_list.toArray(new MobSkill[0]);
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (MobWrapper[]) mw_list.toArray(new MobWrapper[0]);
	}
	
	public MobSkillWrapper[] getMobSkillDetails() {
		
		List<MobSkillWrapper> msw_list = new ArrayList<MobSkillWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM mob_skills");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				MobSkillWrapper msw = new MobSkillWrapper();
				getMobSkillFromResult(result, msw.mob_skill);
				msw_list.add(msw);
			}
			
			Map<Integer, MobSkillEffect> mse_map = getMobSkillEffectMap(conn);
			for (MobSkillWrapper msw : msw_list)
				msw.mob_skill.mob_skill_effect = mse_map.get(msw.mob_skill.id);
			
//			PreparedStatement query  = conn.prepareStatement("SELECT * FROM mob_skill_effects WHERE mob_skill_id = ?");
//			for (MobSkillWrapper msw : msw_list) {				
//				query.setInt(1, msw.mob_skill.id);
//				ResultSet res = query.executeQuery();
//				if ((res == null) || !res.next()) continue;
//				
//				MobSkillEffect mse = msw.mob_skill.mob_skill_effect = new MobSkillEffect();
//				getMobSkillEffectFromResult(res, mse);
//			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (MobSkillWrapper[]) msw_list.toArray(new MobSkillWrapper[0]);
	}
	
	public MobSkillEffectWrapper[] getMobSkillEffectDetails() {
		
		List<MobSkillEffectWrapper> mse_list = new ArrayList<MobSkillEffectWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM mob_skill_effects");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				MobSkillEffectWrapper mse = new MobSkillEffectWrapper();
				getMobSkillEffectFromResult(result, mse.mob_skill_effect);
				mse_list.add(mse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (MobSkillEffectWrapper[]) mse_list.toArray(new MobSkillEffectWrapper[0]);
	}
	
	@Override
	public List<MobSkill> getMobSkills() {
		List<MobSkill> mob_skills = new ArrayList<MobSkill>();
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM mob_skills");
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				MobSkill mob_skill = new MobSkill();
				getMobSkillFromResult(result, mob_skill);
				mob_skills.add(mob_skill);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return mob_skills;
	}
	
	// static methods
    public Map<String, List<MobSkill>> getMosSkillMap(Connection conn) throws SQLException {
  
        Map<Integer, MobSkillEffect> mse_map = getMobSkillEffectMap(conn);

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM mob_skills");
        ResultSet result = statement.executeQuery();
        Map<String, List<MobSkill>> map = new HashMap<String, List<MobSkill>>();
        while (result.next()) {
    		MobSkill ms = new MobSkill();
            getMobSkillFromResult(result, ms);
           
        	List<MobSkill> ms_list = map.get(ms.mob_type);
        	if (ms_list == null) {
        		ms_list = new ArrayList<MobSkill>();
        		map.put(ms.mob_type, ms_list);
        	}
    		ms_list.add(ms);
     		ms.mob_skill_effect  = mse_map.get(ms.id); // may be null
        }
  
        return map;
    }
    
	public static Map<Integer, MobSkillEffect> getMobSkillEffectMap(Connection conn) throws SQLException  {
		Map<Integer, MobSkillEffect> map = new HashMap<Integer, MobSkillEffect>();
		
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM mob_skill_effects");
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			MobSkillEffect mse = new MobSkillEffect();
			getMobSkillEffectFromResult(result, mse);
			map.put(mse.mob_skill_id, mse);
        }

		return map;
	}

	private static void getMobFromResult(ResultSet result, Mob mob) throws SQLException {
		
		mob.id = result.getInt("id");
		mob.will = result.getInt("will");
		mob.name = result.getString("name");
		mob.armor = result.getFloat("armor");
		mob.strength = result.getInt("strength");
		mob.money = result.getInt("money");
		mob.vitality = result.getInt("vitality");
		mob.physical_crit = result.getFloat("physical_crit");
		mob.description = result.getString("description");
		mob.level = result.getInt("level");
		mob.intelligence = result.getInt("intelligence");
		mob.spell_crit = result.getFloat("spell_crit");
		mob.experience = result.getInt("experience");
		mob.mob_type = result.getString("mob_type");
		mob.difficulty_type = result.getString("difficulty_type");
		mob.dodge = result.getFloat("dodge");
		mob.agility = result.getInt("agility");
	}
	
	private static void getMobSkillFromResult(ResultSet result, MobSkill ms) throws SQLException {
		
		ms.id = result.getInt("id");
		ms.name = result.getString("name");
		ms.description = result.getString("description");
		ms.damage_low = result.getInt("damage_low");
		ms.damage_high = result.getInt("damage_high");
		ms.damage_type = result.getString("damage_type");
		ms.damage_subtype = result.getString("damage_subtype");
		ms.spell_target = result.getString("spell_target");
		ms.multiplier = result.getDouble("multiplier");
		ms.mana_points = result.getInt("mana_points");
		ms.weight = result.getInt("weight");
		ms.difficulty_type = result.getString("difficulty_type");
		ms.mob_type = result.getString("mob_type");
	}
	
	private static void getMobSkillEffectFromResult(ResultSet result, MobSkillEffect mse) throws SQLException {
		
		mse.id = result.getInt("id");
		mse.mob_skill_id = result.getInt("mob_skill_id");
		mse.strength = result.getDouble("strength");
		mse.intelligence = result.getDouble("intelligence");
		mse.vitality = result.getDouble("vitality");
		mse.will = result.getDouble("will");
		mse.physical_crit = result.getDouble("physical_crit");
		mse.skill_crit = result.getDouble("skill_crit");
		mse.armor = result.getDouble("armor");
		mse.dodge = result.getDouble("dodge");
		mse.turn = result.getInt("turn");
		mse.damage = result.getInt("damage");
		mse.agility = result.getDouble("agility");
		mse.multiplier = result.getDouble("multiplier");
		mse.effect_type = result.getString("effect_type");
		mse.subtype = result.getString("subtype");
		mse.duration_type = result.getString("duration_type");
		mse.remove_number = result.getInt("remove_number");
		mse.stackable = result.getBoolean("stackable");
		mse.stack_limit = result.getInt("stack_limit");
		mse.proc = result.getInt("proc");
	}
	
	//
	// Unused codes
	//
	@Override
	public boolean createMob(Mob mob) {
	
		Connection conn = null;
		boolean rc = false;
		try {
			conn = getConnection();
			
			// PreparedStatement query;
			rc = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return rc;
	}


//
//	// The following method was used to create new static tables from DefaultValueTable
//
//
//	public boolean createMob() {
//	
//		Connection conn = null;
//		boolean rc = false;
//		try {
//			conn = getConnection();
//			
//			PreparedStatement query;
//			// Create Accessory table data
//			query = conn.prepareStatement("INSERT INTO accessories(id, will, name, armor," +
//				" strength, character_id, vitality, physical_crit, description, intelligence, spell_crit," +
//				" cost, rarity, dodge, agility, tier, generated, accessory_type, damage_high, damage_low, level_requirement)" +
//				" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
//			AccessoryWrapper[] accessory_wrapper_array = DefaultValueTable.getAccessoryWrapperArray();
//			for (int ix = 0; ix < accessory_wrapper_array.length; ix++) {
//				Accessory accessory = accessory_wrapper_array[ix].accessory;
//				query.setInt(1, accessory.id);
//				query.setInt(2, accessory.will);
//				query.setString(3, accessory.name);
//				query.setDouble(4, accessory.armor);
//				query.setInt(5, accessory.strength);
//				query.setInt(6, accessory.character_id);
//				query.setInt(7, accessory.vitality);
//				query.setDouble(8, accessory.physical_crit);
//				query.setString(9, accessory.description);
//				query.setInt(10, accessory.intelligence);
//				query.setDouble(11, accessory.spell_crit);
//				query.setInt(12, accessory.cost);
//				query.setString(13, accessory.rarity);
//				query.setDouble(14, accessory.dodge);
//				query.setInt(15, accessory.agility);
//				query.setInt(16, accessory.tier);
//				query.setBoolean(17, accessory.generated);
//				query.setInt(18, AccessoryType.valueOf(accessory.accessory_type).ordinal());
//				query.setInt(19, accessory.damage_high);
//				query.setInt(20, accessory.damage_low);
//				query.setInt(21, accessory.level_requirement);
//				query.executeUpdate();
//			}
//		

			// accessory_skills 
//			query = conn.prepareStatement("INSERT INTO accessory_skills(id, accessory_id, name, description, " +
//					"damage_low, damage_high, damage_type, damage_subtype, spell_target, multiplier, weight) " +
//					"VALUES(?,?,?,?,?,?,?,?,?,?,?)");
//			AccessorySkillWrapper[] acc_skill_wrapper_array = DefaultValueTable.getAccessorySkillWrapperArray();
//			for (int ix = 0; ix < acc_skill_wrapper_array.length; ix++) {
//				AccessorySkill acc_skill = acc_skill_wrapper_array[ix].accessory_skill;
//				query.setInt(1, acc_skill.id);
//				query.setInt(2, acc_skill.accessory_id);
//				query.setString(3, acc_skill.name);
//				query.setString(4, acc_skill.description);
//				query.setInt(5, acc_skill.damage_low);
//				query.setInt(6, acc_skill.damage_high);
//				query.setString(7, acc_skill.damage_type);
//				query.setString(8, acc_skill.damage_subtype);
//				query.setString(9, acc_skill.spell_target);
//				query.setDouble(10, acc_skill.multiplier);
//				query.setInt(11, acc_skill.weight);
//				query.executeUpdate();
//			}
		
			// Nodes
//			query = conn.prepareStatement("INSERT INTO nodes(id, name, num_waves, " +
//					"description, node_type, region, mob_id, child_nodes) VALUES(?,?,?,?,?,?,?,?)");
//			NodeWrapper[] node_wrapper_array = DefaultValueTable.getNodeWrapperArray();
//			for (int ix = 0; ix < node_wrapper_array.length; ix++) {
//				Node node = node_wrapper_array[ix].node;
//				query.setInt(1, node.id);
//				query.setString(2, node.name);
//				query.setInt(3, node.num_waves);
//				query.setString(4, node.description);
//				query.setInt(5, NodeType.valueOf(node.node_type).ordinal());
//				query.setInt(6, node.region);
//				query.setString(7, node.mob_id);
//				query.setString(8, node.child_nodes);
//				query.executeUpdate();
//			}
		
		// skills (done)
//		query = conn.prepareStatement("INSERT INTO skills(id, character_id, description, " +
//				"damage_low, damage_high, damage_type, spell_target, level_requirement, cost, " +
//				"mana_points, multiplier, special, damage_subtype) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		SkillWrapper[] skill_wrapper_array = DefaultValueTable.getSkillWrapperArray();
//		for (int ix = 0; ix < skill_wrapper_array.length; ix++) {
//			Skill skill = skill_wrapper_array[ix].skill;
//			query.setInt(1, skill.id);
//			query.setInt(2, skill.character_id);
//			query.setString(3, skill.description);
//			query.setInt(4, skill.damage_low);
//			query.setInt(5, skill.damage_high);
//			query.setString(6, skill.damage_type);
//			query.setString(7, skill.spell_target);
//			query.setInt(8, skill.level_requirement);
//			query.setInt(9, skill.cost);
//			query.setInt(10, skill.mana_points);
//			query.setDouble(11, skill.multiplier);
//			query.setBoolean(12, skill.special);
//			query.setString(13, skill.damage_subtype);
//			query.executeUpdate();
//		}
		
		// skill_effects
//			query = conn.prepareStatement("INSERT INTO skill_effects(id, skill_id, strength, " +
//					"intelligence, vitality, will, physical_crit, skill_crit, armor, " +
//					"dodge, turn, effect_type, damage, agility, multiplier, subtype, duration_type, " +
//					"remove_number, stackable, stack_limit, proc) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//			SkillEffectWrapper[] skill_effect_wrapper_array = DefaultValueTable.getSkillEffectWrapperArray();
//			for (int ix = 0; ix < skill_effect_wrapper_array.length; ix++) {
//				SkillEffect skill_effect = skill_effect_wrapper_array[ix].skill_effect;
//				query.setInt(1, skill_effect.id);
//				query.setInt(2, skill_effect.skill_id);
//				query.setDouble(3, skill_effect.strength);
//				query.setDouble(4, skill_effect.intelligence);
//				query.setDouble(5, skill_effect.vitality);
//				query.setDouble(6, skill_effect.will);
//				query.setDouble(7, skill_effect.physical_crit);
//				query.setDouble(8, skill_effect.skill_crit);
//				query.setDouble(9, skill_effect.armor);
//				query.setDouble(10, skill_effect.dodge);
//				query.setInt(11, skill_effect.turn);
//				query.setString(12, skill_effect.effect_type);
//				query.setInt(13, skill_effect.damage);
//				query.setDouble(14, skill_effect.agility);
//				query.setDouble(15, skill_effect.multiplier);
//				query.setString(16, skill_effect.subtype);
//				query.setString(17, skill_effect.duration_type);
//				query.setInt(18, skill_effect.remove_number);
//				query.setBoolean(19, skill_effect.stackable);
//				query.setInt(20, skill_effect.stack_limit);
//				query.setDouble(21, skill_effect.proc);
//				query.executeUpdate();
//			}
//
//		
			// accessory_skill_effects (DONE)
//			query = conn.prepareStatement("INSERT INTO accessory_skill_effects(id, accessory_skill_id, strength, intelligence," +
//					" vitality, will, physical_crit, skill_crit, armor, dodge, turn," +
//					" damage, agility, multiplier, effect_type, subtype, duration_type, remove_number, stackable, stack_limit, proc)" +
//					" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//			AccessorySkillEffectWrapper[] acc_skill_effect_wrapper_array = DefaultValueTable.getAccessorySkillEffectWrapperArray();
//			for (int ix = 0; ix < acc_skill_effect_wrapper_array.length; ix++) {
//				AccessorySkillEffect acc_skill_effect = acc_skill_effect_wrapper_array[ix].accessory_skill_effect;
//				query.setInt(1, acc_skill_effect.id);
//				query.setInt(2, acc_skill_effect.accessory_skill_id);
//				query.setDouble(3, acc_skill_effect.strength);
//				query.setDouble(4, acc_skill_effect.intelligence);
//				query.setDouble(5, acc_skill_effect.vitality);
//				query.setDouble(6, acc_skill_effect.will);
//				query.setDouble(7, acc_skill_effect.physical_crit);
//				query.setDouble(8, acc_skill_effect.skill_crit);
//				query.setDouble(9, acc_skill_effect.armor);
//				query.setDouble(10, acc_skill_effect.dodge);
//				query.setInt(11, acc_skill_effect.turn);
//				query.setInt(12, acc_skill_effect.damage);
//				query.setDouble(13, acc_skill_effect.agility);
//				query.setDouble(14, acc_skill_effect.multiplier);
//				query.setString(15, acc_skill_effect.effect_type);
//				query.setString(16, acc_skill_effect.subtype);
//				query.setString(17, acc_skill_effect.duration_type);
//				query.setInt(18, acc_skill_effect.remove_number);
//				query.setBoolean(19, acc_skill_effect.stackable);
//				query.setInt(20, acc_skill_effect.stack_limit);
//				query.setDouble(21, acc_skill_effect.proc);
//				query.executeUpdate();
//			}
//		
			// Create Mob table data (done)
//			query = conn.prepareStatement("INSERT INTO mobs(id, will, name, armor," +
//					" strength, money, vitality, physical_crit, description, level, intelligence, spell_crit," +
//					" experience, mob_type, difficulty_type, dodge, agility)" +
//					"  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//			MobWrapper[] mob_wrapper_array = DefaultValueTable.getMobWrapperArray();
//			for (int ix = 0; ix < mob_wrapper_array.length; ix++) {
//				Mob mob = mob_wrapper_array[ix].mob;
//				query.setInt(1, mob.id);
//				query.setInt(2, mob.will);
//				query.setString(3, mob.name);
//				query.setFloat(4, (float) mob.armor);
//				query.setInt(5, mob.strength);
//				query.setInt(6, mob.money);
//				query.setInt(7, mob.vitality);
//				query.setFloat(8, (float) mob.physical_crit);
//				query.setString(9, mob.description);
//				query.setInt(10, mob.level);
//				query.setInt(11, mob.intelligence);
//				query.setFloat(12, (float) mob.spell_crit);
//				query.setInt(13, mob.experience);
//				query.setString(14, mob.mob_type);
//				query.setString(15, mob.difficulty_type);
//				query.setFloat(16, (float) mob.dodge);
//				query.setInt(17, mob.agility);
//				query.executeUpdate();
//			}
//
//		// create Playlist table data (DONE)
//		query = conn.prepareStatement("INSERT INTO playlists(id, initial, name, description) VALUES(?,?,?,?)");
//		PlaylistWrapper[] playlist_wrapper_array = DefaultValueTable.getPlaylistWrapperArray();
//		for (int ix = 0; ix < playlist_wrapper_array.length; ix++) {
//			Playlist playlist = playlist_wrapper_array[ix].playlist;
//			query.setInt(1, playlist.id);
//			query.setBoolean(2, playlist.initial);
//			query.setString(3, playlist.name);
//			query.setString(4, playlist.description);
////			query.setString(5, playlist.created_at);
////			query.setString(6, playlist.updated_at);
//			query.executeUpdate();
//		}
//		
		
		// create purchase table data (DONE)
//		query = conn.prepareStatement("INSERT INTO purchases(id, name, price, total, purchase_type, purchase_abbr, description) VALUES(?,?,?,?,?,?,?)");
//		PurchaseWrapper[] purchase_wrapper_array = DefaultValueTable.getPurchaseWrapperArray();
//		for (int ix = 0; ix < purchase_wrapper_array.length; ix++) {
//			Purchase purchase = purchase_wrapper_array[ix].purchase;
//			query.setInt(1, purchase.id);
//			query.setString(2, purchase.name);
//			query.setDouble(3, purchase.price);
//			query.setInt(4, purchase.total);
//			query.setString(5, purchase.purchase_type);
//			query.setString(6, purchase.purchase_abbr);
//			query.setString(7, purchase.description);
////			query.setString(8, playlist.created_at);
////			query.setString(9, playlist.updated_at);
//			query.executeUpdate();
//		}

		// create Tutorial table data (DONE)
//		query = conn.prepareStatement("INSERT INTO tutorials(id, name) VALUES(?,?)");
//		TutorialWrapper[] tutorial_wrapper_array = DefaultValueTable.getTutorialWrapperArray();
//		for (int ix = 0; ix < tutorial_wrapper_array.length; ix++) {
//			Tutorial tutorial = tutorial_wrapper_array[ix].tutorial;
//			query.setInt(1, tutorial.id);
//			query.setString(2, tutorial.name);
//			query.executeUpdate();
//		}
//		
//			rc = true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//	
//		return rc;
//	}
}
