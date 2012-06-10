package com.cboxgames.idonia.backend.commons.db.accessory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.proto.UserAccessoryArrayProto;
import com.cboxgames.utils.idonia.types.proto.UserAccessoryProto;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;

public class UserAccessoriesShopDBSQL extends DBSQL implements
		IUserAccessoriesShopDB {

	public UserAccessoriesShopDBSQL(DataSource connection_pool, ServletContext servlet_context)
			throws SQLException {
		super(connection_pool, servlet_context);
	}

	public ResponseGetUserAccessories getShopAccessories(int user_id) {
		ResponseGetUserAccessories response = new ResponseGetUserAccessories();
		response.accessories = new ArrayList<Accessory>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM user_accessories WHERE user_id = ?");
			statement.setInt(1, user_id);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				// We are expecting there to be a list of accessories in the PROTOSTUFF format.
				UserAccessoryArrayProto array = new UserAccessoryArrayProto();
				ProtostuffIOUtil.mergeFrom(result.getBinaryStream("temp_accessories"), array, UserAccessoryArrayProto.getSchema());
				
				List<UserAccessoryProto> user_accessory_proto = array.getUserAccessoriesList();
				for (UserAccessoryProto temp : user_accessory_proto) {
					response.accessories.add(protoToAccessory(temp));
				}
				
				response.updated_at = result.getTimestamp("updated_at");
			} else {
				response.updated_at = new Date();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return response;
	}

	public boolean saveShopAccessories(int user_id, List<Accessory> accessories) {

		UserAccessoryArrayProto array = new UserAccessoryArrayProto();
		List<UserAccessoryProto> items = new ArrayList<UserAccessoryProto>();
		for (Accessory acc : accessories) {
			items.add(accessoryToProto(acc));
		}
		array.setUserAccessoriesList(items);
		
		byte[] data = ProtostuffIOUtil.toByteArray(array, UserAccessoryArrayProto.getSchema(),
				LinkedBuffer.allocate(512));
	
		Connection conn = null;
		boolean saved = false;
		try {
			conn = getConnection();
			PreparedStatement statement;
			statement = conn.prepareStatement("REPLACE INTO user_accessories(user_id, temp_accessories) VALUES(?, ?)");
			statement.setInt(1, user_id);
			statement.setBytes(2, data);	
			statement.executeUpdate();
			saved = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return saved;
	}
	
	/**
	 * Convert an Accessory object to its equivalent PROTOSTUFF class.
	 * 
	 * @param accessory
	 * @return
	 */
	private static UserAccessoryProto accessoryToProto(Accessory accessory) {
		UserAccessoryProto proto = new UserAccessoryProto();
		
		proto.setAccessoryType(accessory.accessory_type);
		proto.setAgility(accessory.agility);
		proto.setArmor(accessory.armor);
		proto.setCost(accessory.cost);
		proto.setDescription(accessory.description);
		proto.setDisplayOrder(accessory.display_order);
		proto.setDodge(accessory.dodge);
		proto.setIntelligence(accessory.intelligence);
		proto.setLevelRequirement(accessory.level_requirement);
		proto.setName(accessory.name);
		proto.setPhysicalCrit(accessory.physical_crit);
		proto.setRarity(accessory.rarity);
		proto.setSpellCrit(accessory.spell_crit);
		proto.setStrength(accessory.strength);
		proto.setVitality(accessory.vitality);
		proto.setWill(accessory.will);
		
		return proto;
	}
	
	/**
	 * Convert a PROTOSTUFF object to it's equivalent Accessory class.
	 * 
	 * @param accessory
	 * @return
	 */
	private static Accessory protoToAccessory(UserAccessoryProto accessory) {
		Accessory acc = new Accessory();
		
		acc.accessory_type = accessory.getAccessoryType();
		acc.agility = accessory.getAgility();
		acc.armor = accessory.getArmor();
		acc.cost = accessory.getCost();
		acc.description = accessory.getDescription();
		acc.display_order = accessory.getDisplayOrder();
		acc.dodge = accessory.getDodge();
		acc.generated = true;
		acc.intelligence = accessory.getIntelligence();
		acc.level_requirement = accessory.getLevelRequirement();
		acc.name = accessory.getName();
		acc.physical_crit = accessory.getPhysicalCrit();
		acc.rarity = accessory.getRarity();
		acc.spell_crit = accessory.getSpellCrit();
		acc.strength = accessory.getStrength();
		acc.vitality = accessory.getVitality();
		acc.will = accessory.getWill();
		
		return acc;
	}
	
	public class ResponseGetUserAccessories {
		public List<Accessory> accessories;
		public Date updated_at;
	}

}
