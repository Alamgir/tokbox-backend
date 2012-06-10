package com.cboxgames.idonia.backend.commons.db.usercharacteraccessory;

import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog;
import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.accessory.IAccessoryDB.AccessoryType;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest.UserCharacterUpdate;
import com.cboxgames.idonia.backend.commons.requestclasses.UserCharacterUpdateRequest.UserCharacterAccessory;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Accessory.AccessoryWrapper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/12/11
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCharacterAccessoryDBSQL extends DBSQL implements IUserCharacterAccessoryDB{

    public UserCharacterAccessoryDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
        super(data_source, servlet_context);
    }

    @Override
    public int createUserCharacterAccessory(Accessory accessory, int user_id) {
        Connection conn = null;
        int acc_id = -1;

        try {
            conn = getConnection();
            
            accessory.user_id = user_id;
            acc_id = createUserCharacterAccessory(accessory, 0 /* user_character_id */, conn);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return acc_id;
    }

    @Override
    public Accessory getUserCharacterAccessoryByID(int uca_id) {
        Connection conn = null;
        Accessory accessory = null;
        try {
            conn = getConnection();
            
            PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE id = ?");
            get_uca.setInt(1, uca_id);
            ResultSet acc_results = get_uca.executeQuery();
            
            if ((acc_results != null) && acc_results.next()) {
            	accessory = new Accessory();
            	getUserCharacterAccessoryFromResult(acc_results, accessory);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return accessory;
    }
    
    public AccessoryWrapper[] getUserCharacterAccessoryDetails() {
        Connection conn = null;
        List<AccessoryWrapper> aw_list = new ArrayList<AccessoryWrapper>();
        try {
            conn = getConnection();
            
            PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories");
            ResultSet acc_results = get_uca.executeQuery();
            
            if ((acc_results != null) && acc_results.next()) {
            	AccessoryWrapper aw = new AccessoryWrapper();
            	getUserCharacterAccessoryFromResult(acc_results, aw.accessory);
            	aw_list.add(aw);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        
        return (AccessoryWrapper[]) aw_list.toArray(new AccessoryWrapper[0]);
    }

    @Override
    public List<Accessory> getUserCharacterAccessoriesByType(String acc_type, int user_id) {
        Connection conn = null;
        List<Accessory> accessory_list = new ArrayList<Accessory>();
        try {
            conn = getConnection();
            
            PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE accessory_type = ? AND user_id= ? ");
            // get_uca.setString(1, acc_type);
            AccessoryType acc_enum_type = AccessoryType.valueOf(acc_type);
            get_uca.setInt(1, acc_enum_type.getId());
            get_uca.setInt(2, user_id);
            ResultSet acc_results = get_uca.executeQuery();
            
            if (acc_results != null) {
                while (acc_results.next()) {
                    Accessory accessory = new Accessory();
                    getUserCharacterAccessoryFromResult(acc_results, accessory);
                    accessory_list.add(accessory);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            closeConnection(conn);
        }
        return accessory_list;
    }

    @Override
    public List<Accessory> getUserCharacterAccessoriesByUserCharacterID(int uc_id) {
        Connection conn = null;
        List<Accessory> accessory_list = new ArrayList<Accessory>();
        try {
            conn = getConnection();
            
            PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE user_character_id = ? ");
            get_uca.setInt(1, uc_id);
            ResultSet acc_results = get_uca.executeQuery();
            
            if (acc_results != null) {
                while (acc_results.next()) {
                    Accessory accessory = new Accessory(); 
                    getUserCharacterAccessoryFromResult(acc_results, accessory);
                    accessory_list.add(accessory);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            closeConnection(conn);
        }
        return accessory_list;
    }
 
    @Override
    public boolean updateUserCharacterAccessory(Accessory accessory) {
        Connection conn = null;
        boolean updated = false;
        try {
            conn = getConnection();

            PreparedStatement update_uca = conn.prepareStatement("UPDATE user_character_accessories SET user_character_id=?," +
                                                                        " rarity=?,accessory_id=?," +
                                                                        "user_id=?,cost=?,accessory_type=?" +
                                                                         " WHERE id = ? ");

            update_uca.setInt(1, accessory.user_character_id);
            update_uca.setString(2, accessory.rarity);
            update_uca.setInt(3, accessory.accessory_id);
            update_uca.setInt(4, accessory.user_id);
            update_uca.setInt(5, accessory.cost);
            // update_uca.setString(6, accessory.accessory_type);
            AccessoryType acc_enum_type = AccessoryType.valueOf(accessory.accessory_type);
            update_uca.setInt(6, acc_enum_type.getId());
            update_uca.setInt(7, accessory.id);
            update_uca.executeUpdate();
            updated = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return updated;
        
    }

    @Override
    public List<Accessory> getUserCharacterAccessoryInventory(int user_id) {
        Connection conn = null;
        List<Accessory> accessory_list = new ArrayList<Accessory>();
        try {
            conn = getConnection();

            PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE user_character_id=0 AND user_id= ? ");
            get_uca.setInt(1, user_id);
            ResultSet acc_results = get_uca.executeQuery();

            if (acc_results != null) {
                while (acc_results.next()) {
                    Accessory accessory = new Accessory();
                    getUserCharacterAccessoryFromResult(acc_results, accessory);
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

    @Override
    public boolean sellUserCharacterAccessory(int uca_id) {
    	boolean sold = false;
        Connection conn = null;
        try {
            conn = getConnection();

            PreparedStatement sell_uca = conn.prepareStatement("UPDATE user_character_accessories SET user_character_id = -1 WHERE id = ?");
            sell_uca.setInt(1, uca_id);
            sell_uca.executeUpdate();
            sold = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return sold;
    }

    public boolean deleteUserCharacterAccessory(int uca_id) {
        boolean deleted = false;
        Connection conn = null;
        try {
            conn = getConnection();

            PreparedStatement delete_uca = conn.prepareStatement("DELETE FROM user_character_accessories WHERE id = ?");
            delete_uca.setInt(1, uca_id);
            delete_uca.execute();
            deleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return deleted;
    }
    
    // static methods
    public static void updateAccessoryUserCharacterID(Map<Integer, Character> user_char_list, UserCharacterUpdate[] ucu_requests, Connection conn, int user_id)
    		throws SQLException {

        //assign all user_character_accessories either in-use or in-inventory to user_character_id of 0
        PreparedStatement clear_uca = conn.prepareStatement("UPDATE user_character_accessories SET user_character_id = 0 WHERE user_id = ? AND user_character_id != -1 ");
        clear_uca.setInt(1, user_id);
        clear_uca.executeUpdate();

    	
        PreparedStatement update_uca = conn.prepareStatement("UPDATE user_character_accessories SET user_character_id = ? " +
                 " WHERE id = ? ");

        boolean any_update = false;
        for (UserCharacterUpdate ucu : ucu_requests) {
            Character user_char = user_char_list.get(ucu.id);
            if (user_char == null) continue;
            UserCharacterAccessory uca = ucu.user_character_accessory;
            if (uca == null) continue;

            //assign accessory
            update_uca.setInt(1, user_char.id);
            update_uca.setInt(2, uca.accessory_id);
            update_uca.addBatch();

            //assign armor
            update_uca.setInt(1, user_char.id);
            update_uca.setInt(2, uca.armor_accessory_id);
            update_uca.addBatch();

            //assign weapon
            update_uca.setInt(1, user_char.id);
            update_uca.setInt(2, uca.weapon_accessory_id);
            update_uca.addBatch();

            //assign helmet
            update_uca.setInt(1, user_char.id);
            update_uca.setInt(2, uca.helmet_accessory_id);
            update_uca.addBatch();

            any_update = true;
        }

        if (any_update)
            update_uca.executeBatch();
    }
    
    public static Accessory[] getUserCharacterAccessories(int user_character_id, Connection conn) throws SQLException {

        PreparedStatement query_two = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE user_character_id = ?");
        query_two.setInt(1, user_character_id);
        ResultSet acc_results = query_two.executeQuery();
        List<Accessory> user_character_accessories_list = new ArrayList<Accessory>();
        if (acc_results == null) return null;
        
        while (acc_results.next()) {
            Accessory uca = new Accessory();
            getUserCharacterAccessoryFromResult(acc_results, uca);
            user_character_accessories_list.add(uca);
        }
        
        if (user_character_accessories_list.isEmpty())
        	return null;

        return (Accessory[]) user_character_accessories_list.toArray(new Accessory[0]);
    }
    
    public static boolean getUserCharacterAccessories(List<Character> user_char_list, Connection conn) throws SQLException {

    	if ((user_char_list == null) || (user_char_list.size() == 0))
    		return false;
    	
    	boolean first = true;
    	Iterator<Character> itr = user_char_list.iterator();
    	StringBuilder query = new StringBuilder("SELECT * FROM user_character_accessories WHERE ");
        while (itr.hasNext()) {
        	if (!first) query.append(" OR ");
        	else first = false;
        	Character user_char = itr.next();
            query.append(" user_character_id = " + user_char.id);
        }

        PreparedStatement statement = conn.prepareStatement(query.toString());
        ResultSet result = statement.executeQuery();
        if (result == null) return false;
        
        Map<Integer, List<Accessory>> map = new HashMap<Integer, List<Accessory>>();
        for (Character uc : user_char_list) {
            map.put(uc.id, new ArrayList<Accessory>());
        }

        while (result.next()) {
        	Accessory uca = new Accessory();
            getUserCharacterAccessoryFromResult(result, uca);
            List<Accessory> uca_list = map.get(uca.user_character_id);
            uca_list.add(uca);
        }
        
        for (Character uc : user_char_list) {
        	List<Accessory> uca_list = map.get(uc.id);
        	if (uca_list.isEmpty()) continue;
        	uc.user_character_accessories = uca_list.toArray(new Accessory[0]);
        }
    	
        return true;
    }
   
    public static int createUserCharacterAccessory(Accessory accessory, int user_char_id, Connection conn) throws SQLException {
    	
        PreparedStatement create_uca = conn.prepareStatement("INSERT INTO user_character_accessories(user_id, user_character_id, " +
                                                                     "accessory_id, accessory_type, name, cost, rarity," +
                                                                     "strength,vitality,agility,intelligence,will,armor,dodge,physical_crit," +
                                                                     "spell_crit,damage_high,damage_low,filenames,character_id,level_requirement,premium)" +
                                                                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

		create_uca.setInt(1, accessory.user_id);
		create_uca.setInt(2, user_char_id); // user_character_id = 0
		create_uca.setInt(3, accessory.accessory_id);

		AccessoryType acc_enum_type = AccessoryType.valueOf(accessory.accessory_type);
		create_uca.setInt(4, acc_enum_type.getId());

        create_uca.setString(5, accessory.name);
        create_uca.setInt(6, accessory.cost);
        create_uca.setString(7, accessory.rarity);
        create_uca.setInt(8, accessory.strength);
        create_uca.setInt(9, accessory.vitality);
        create_uca.setInt(10, accessory.agility);
        create_uca.setInt(11, accessory.intelligence);
        create_uca.setInt(12, accessory.will);
        create_uca.setFloat(13, accessory.armor);
        create_uca.setFloat(14, accessory.dodge);
        create_uca.setFloat(15, accessory.physical_crit);
        create_uca.setFloat(16, accessory.spell_crit);
        create_uca.setInt(17, accessory.damage_high);
        create_uca.setInt(18, accessory.damage_low);
        create_uca.setString(19, accessory.filenames);
        create_uca.setInt(20, accessory.character_id);
        create_uca.setInt(21, accessory.level_requirement);
        create_uca.setBoolean(22, accessory.premium);
		
		create_uca.executeUpdate();
		ResultSet gen_keys = create_uca.getGeneratedKeys();
		if ((gen_keys != null) && gen_keys.next()) {
            accessory.id = gen_keys.getInt(1);
        }
		return accessory.id;
    }
    
    public static void getAccessoryInventoryAndSold(User user, Connection conn) throws SQLException {
    	PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE user_id = ? AND " +
    			" (user_character_id = 0 OR user_character_id = -1)");
        get_uca.setInt(1, user.id);
        ResultSet acc_results = get_uca.executeQuery();

        if (acc_results == null) return;
        
        List<Accessory> inventory_list = new ArrayList<Accessory>(),
        					 sold_list = new ArrayList<Accessory>();
        while (acc_results.next()) {
            Accessory accessory = new Accessory();
            getUserCharacterAccessoryFromResult(acc_results, accessory);
            if (accessory.user_character_id == 0)
            	inventory_list.add(accessory);
            else sold_list.add(accessory);
        }
        
        if (!inventory_list.isEmpty())
        	user.user_character_accessories_inventory = (Accessory[]) inventory_list.toArray(new Accessory[0]);
        if (!sold_list.isEmpty())
        	user.user_character_accessories_sold = (Accessory[]) inventory_list.toArray(new Accessory[0]);
    }
     
    public static Accessory[] getAccessoryInventory(int user_id, Connection conn) throws SQLException {
       
        PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE user_character_id = 0 AND user_id= ? ");
        get_uca.setInt(1, user_id);
        ResultSet acc_results = get_uca.executeQuery();

        if (acc_results == null) return null;
        
        List<Accessory> accessory_list = new ArrayList<Accessory>();
        while (acc_results.next()) {
            Accessory accessory = new Accessory();
            getUserCharacterAccessoryFromResult(acc_results, accessory);
            accessory_list.add(accessory);
        }
        
        if (accessory_list.isEmpty()) return null;
        
    	return (Accessory[]) accessory_list.toArray(new Accessory[0]);
    }

    public static Accessory[] getSoldAccessories(int user_id, Connection conn) throws SQLException {
        PreparedStatement get_uca = conn.prepareStatement("SELECT * FROM user_character_accessories WHERE user_character_id = -1 AND user_id= ? ");
        get_uca.setInt(1, user_id);
        ResultSet acc_results = get_uca.executeQuery();

        if (acc_results == null) return null;

        List<Accessory> accessory_list = new ArrayList<Accessory>();
        while (acc_results.next()) {
            Accessory accessory = new Accessory();
            getUserCharacterAccessoryFromResult(acc_results, accessory);
            accessory_list.add(accessory);
        }

        if (accessory_list.isEmpty()) return null;

        return (Accessory[]) accessory_list.toArray(new Accessory[0]);
    }
    
    private static void getUserCharacterAccessoryFromResult(ResultSet result, Accessory accessory)
    		throws SQLException {
        
        accessory.id = result.getInt("id");
        accessory.name = result.getString("name");
        accessory.cost = result.getInt("cost");
        accessory.user_id = result.getInt("user_id");
        accessory.accessory_id = result.getInt("accessory_id");
        accessory.user_character_id = result.getInt("user_character_id");

        int acc_type_id = result.getInt("accessory_type");
        accessory.accessory_type = AccessoryType.idToName(acc_type_id);

        accessory.rarity = result.getString("rarity");
        accessory.strength = result.getInt("strength");
        accessory.vitality = result.getInt("vitality");
        accessory.agility = result.getInt("agility");
        accessory.intelligence = result.getInt("intelligence");
        accessory.will = result.getInt("will");
        accessory.armor = result.getFloat("armor");
        accessory.dodge = result.getFloat("dodge");
        accessory.physical_crit = result.getFloat("physical_crit");
        accessory.spell_crit = result.getFloat("spell_crit");
        accessory.damage_high = result.getInt("damage_high");
        accessory.damage_low = result.getInt("damage_low");
        accessory.filenames = result.getString("filenames");
        accessory.character_id = result.getInt("character_id");
        accessory.level_requirement = result.getInt("level_requirement");
        accessory.node_id = result.getInt("node_id");
        accessory.premium = result.getBoolean("premium");
    }
}
