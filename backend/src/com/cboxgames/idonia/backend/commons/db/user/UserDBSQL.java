package com.cboxgames.idonia.backend.commons.db.user;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.Utility;
import com.cboxgames.idonia.backend.commons.db.achievement.UserAchievementDBSQL;
import com.cboxgames.idonia.backend.commons.db.userpotion.UserPotionDBSQL;
import com.cboxgames.idonia.backend.commons.db.usersoul.UserSoulDBSQL;
import com.cboxgames.stack.system.services.logging.CBoxLoggerSyslog;
import com.cboxgames.utils.idonia.types.*;
import com.cboxgames.utils.idonia.types.User.UserWrapper;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.node.UserNodeDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.playlist.UserPlaylistDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacteraccessory.UserCharacterAccessoryDBSQL;
import com.cboxgames.idonia.backend.commons.authentication.BCrypt;

public class UserDBSQL extends DBSQL implements IUserDB {
	
	public UserDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
		super(data_source, servlet_context);
	}

    public User createUser(String username, String password, String device_token, String udid, String mac_address) {
    	
        Connection conn = null;
        ResultSet gen_keys = null;
        User user = null;
        String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt(4));

        try {
            conn = getConnection();
            UserNodeDBSQL un_dbsql = new UserNodeDBSQL(getDataSource(), getServletContext());
            UserPlaylistDBSQL up_dbsql = new UserPlaylistDBSQL(getDataSource(), getServletContext());
            UserSoulDBSQL us_dbsql = new UserSoulDBSQL(getDataSource(), getServletContext());
            UserPotionDBSQL upotion_dbsql = new UserPotionDBSQL(getDataSource(), getServletContext());
            user =  new User();
            PreparedStatement query = conn.prepareStatement("INSERT INTO users(money, tokens, inventory_spots, created_at, last_sign_in_at, offline_pvp_timestamp) "
                                                      + "VALUES (?, ?, ?, ?, ?, ?) ",
                                                       Statement.RETURN_GENERATED_KEYS);
            user.money = Constants.INITIAL_MONEY;
            user.tokens = Constants.INITIAL_TOKENS;
            user.inventory_spots = Constants.INITIAL_INVENTORY_SPOTS;  
            user.first_day = false;
            user.second_day = false;
            user.third_day = false;
            user.admin = false;

            query.setInt(1, user.money);
            query.setInt(2, user.tokens);
            query.setInt(3, user.inventory_spots);

            java.util.Date date = new java.util.Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            query.setTimestamp(4, timestamp);
            query.setTimestamp(5, timestamp);
            query.setTimestamp(6, timestamp);
            query.executeUpdate();
            
            user.created_at = timestamp;
            user.last_sign_in_at = timestamp;
            user.offline_pvp_timestamp = timestamp;
 
            gen_keys = query.getGeneratedKeys();
            if (gen_keys.next()) {
                user.id = gen_keys.getInt(1);
            }
       
            user.username = username;

            PreparedStatement create_identifiers = conn.prepareStatement("INSERT INTO user_identifiers(user_id,udid,mac_address) VALUES (?,?,?)");
            create_identifiers.setInt(1, user.id);
            create_identifiers.setString(2, udid);
            create_identifiers.setString(3, mac_address);
            create_identifiers.executeUpdate();
            
            ArrayList<String> udids = new ArrayList<String>();
            udids.add(udid);
            user.udids = udids;
            
            ArrayList<String> mac_addresses = new ArrayList<String>();
            mac_addresses.add(mac_address);
            user.mac_addresses = mac_addresses;

            PreparedStatement query_two = conn.prepareStatement("INSERT INTO user_id_name_map(user_id, username, hashed_password, device_token)"
                                                              + "VALUES (?, ?, ?, ?)");
            query_two.setInt(1, user.id);
            query_two.setString(2, user.username);
            query_two.setString(3, pw_hash);
            query_two.setString(4, device_token);
            query_two.executeUpdate();
 

            un_dbsql.createUserNodes(user, conn);
            up_dbsql.createUserPlaylists(user, conn);
            us_dbsql.createUserSoul(user, conn);
            upotion_dbsql.createUserPotion(user, conn);

            conn.commit();

        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
            user = null;
        }
        finally {
            closeConnection(conn);
        }
        return user;
    }

    public User getUserByID(int user_id) {
    	
        Connection conn = null;
        User user = null;
        try {
            conn = getConnection();
            user = new User();
            user.id = user_id;
            if (!getUserByID(user, conn)) {
				CBoxLoggerSyslog.logMessage("Not able to get a user by ID = " + user_id);
			}
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        
        return user;
    }
    
    public User getUserOnlyByID(int user_id) {
        Connection conn = null;
        User user = null;

        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("SELECT money,tokens,character_slot,breadstick,bread_slice,bread_loaf,inventory_spots," +
                                                                    "tapjoy_tokens,message_of_the_day,admin,souls, no_rp FROM users WHERE users.id = ? ");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();
            if (results.next()) {
                user = new User();
                user.id = user_id;
                user.money = results.getInt("money");
                user.tokens = results.getInt("tokens");
                user.character_slot = results.getInt("character_slot");
                user.breadstick = results.getInt("breadstick");
                user.bread_slice = results.getInt("bread_slice");
                user.bread_loaf = results.getInt("bread_loaf");
                user.inventory_spots = results.getInt("inventory_spots");
                user.tapjoy_tokens = results.getInt("tapjoy_tokens");
                user.message_of_the_day = results.getString("message_of_the_day");
                user.admin = results.getBoolean("admin");
                user.souls = results.getInt("souls");
                user.no_rp = results.getBoolean("no_rp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        
        return user;
    }
    
    public User getUserWithAccessoryByID(int user_id, int user_char_id) {
        Connection conn = null;
        User user = null;

        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("SELECT money, inventory_spots, tokens FROM users WHERE users.id = ? ");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();
            if ((results != null) && results.next()) {
                user = new User();
                user.id = user_id;
                user.money = results.getInt("money");
                user.inventory_spots = results.getInt("inventory_spots");
                user.tokens = results.getInt("tokens");
                
                // Get one user character by user character id
                if (user_char_id != 0) {
                    user.user_characters = UserCharacterDBSQL.getUserCharacterByID(user_char_id, true, false, conn);
                }
                // Get Accessory Inventory
                user.user_character_accessories_inventory = UserCharacterAccessoryDBSQL.getAccessoryInventory(user.id, conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        
        return user;
    }

    public User getUserByUsername(String username) {
        Connection conn = null;
        User user = null;
        try {
            conn = getConnection();
  
            PreparedStatement query = conn.prepareStatement("SELECT users.id as user_id," +
                                                                    "user_id_name_map.username as username," +
                                                                    "user_id_name_map.hashed_password as hash_pw," +
                                                                    "users.money as user_money," +
                                                                    "users.tokens as user_tokens," +
                                                                    "users.character_slot as user_character_slots," +
                                                                    "users.breadstick as user_breadstick," +
                                                                    "users.bread_slice as user_breadslice," +
                                                                    "users.bread_loaf as user_breadloaf," +
                                                                    "users.inventory_spots as user_inventory_spots," +
                                                                    "users.tapjoy_tokens as user_tapjoy_tokens," +
                                                                    "users.message_of_the_day as user_message," +
                                                                    "users.admin as admin," +
                                                                    "users.souls as souls," +
                                                                    "users.no_rp as no_rp," +
                                                                    "users.last_sign_in_at as last_sign_in_at," +
                                                                    "users.created_at as created_at," +
                                                                    "users.max_level as max_level," +
                                                                    "users.offline_pvp_count as offline_pvp_count," +
                                                                    "users.offline_pvp_timestamp as offline_pvp_timestamp," +
                                                                    "user_nodes.data as user_node_data" +
																	" FROM users, user_id_name_map, user_nodes" +
                                                                    " WHERE user_id_name_map.user_id = users.id" +
                                                                    " AND user_nodes.user_id = users.id" +
                                                                    " AND user_id_name_map.username = ? ");
            query.setString(1, username);
            ResultSet results = query.executeQuery();
            if ((results != null) && results.next()) {
                user = new User();
                getUserFromResult(user, results, conn);

                java.util.Date date = new java.util.Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                
                StringBuilder builder = new StringBuilder("UPDATE users SET last_sign_in_at = ? ");
                if (user.offline_pvp_timestamp == null || Utility.isOldData(user.offline_pvp_timestamp)) {
                    builder.append(", offline_pvp_timestamp = ?, offline_pvp_count = ? WHERE id = ?");
                    PreparedStatement update_login = conn.prepareStatement(builder.toString());
                    update_login.setTimestamp(1, timestamp);
                    update_login.setTimestamp(2, timestamp);
                    update_login.setInt(3, Constants.OFFLINE_PVP_COUNT);
                    update_login.setInt(4, user.id);
                    update_login.executeUpdate();
                    user.offline_pvp_count = Constants.OFFLINE_PVP_COUNT;
                }
                else {
                    builder.append("WHERE id = ?");
                    PreparedStatement update_login = conn.prepareStatement(builder.toString());
                    update_login.setTimestamp(1, timestamp);
                    update_login.setInt(2, user.id);
                    update_login.executeUpdate();
                }

                

                

//                user.id = results.getInt("user_id");
//                user.username = results.getString("username");
//                user.hashed_pw = results.getString("hash_pw");
//                user.money = results.getInt("user_money");
//                user.tokens = results.getInt("user_tokens");
//                user.character_slot = results.getInt("user_character_slots");
//                user.breadstick = results.getInt("user_breadstick");
//                user.bread_slice = results.getInt("user_breadslice");
//                user.bread_loaf = results.getInt("user_breadloaf");
//                user.inventory_spots = results.getInt("user_inventory_spots");
//                user.tapjoy_tokens = results.getInt("user_tapjoy_tokens");
//                user.message_of_the_day = results.getString("user_message");
//                user.admin = results.getBoolean("admin");
//                user.souls = results.getInt("souls");
//
//                //Work with UserNodes result set
//                user.user_nodes = UserNodeDBSQL.getUserNodeArray(user.id, results.getBytes("user_node_data"));
//                //Work with UserCharacters result set
//                user.user_characters = UserCharacterDBSQL.getAllUserCharacters(user.id, conn);
//                //Get Accessory Inventory
//                user.user_character_accessories_inventory = UserCharacterAccessoryDBSQL.getAccessoryInventory(user.id, conn);
//                //Work with UserPlaylists result set
//                user.user_playlists = UserPlaylistDBSQL.getUserPlaylistDetailsByID(user.id, conn);
//                //Work with UserSouls results set
//                user.user_souls = UserSoulDBSQL.getUserSouls(user.id, conn);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        
        return user;
    }

    public UserWrapper[] getUserDetails() {
		
 		Connection conn = null;
 		List<UserWrapper> user_list = new ArrayList<UserWrapper>();
 		UserWrapper[] user_array = null;
 	
 		try {
 			conn = getConnection();
 			PreparedStatement query = conn.prepareStatement("SELECT users.id as user_id," +
                    "user_id_name_map.username as username," +
                    "user_id_name_map.hashed_password as hash_pw," +
                    "users.money as user_money," +
                    "users.tokens as user_tokens," +
                    "users.character_slot as user_character_slots," +
                    "users.breadstick as user_breadstick," +
                    "users.bread_slice as user_breadslice," +
                    "users.bread_loaf as user_breadloaf," +
                    "users.inventory_spots as user_inventory_spots," +
                    "users.tapjoy_tokens as user_tapjoy_tokens," +
                    "users.message_of_the_day as user_message," +
                    "users.admin as admin," +
                    "users.souls as souls," +
                    "users.no_rp as no_rp," +
                    "users.last_sign_in_at as last_sign_in_at," +
                    "users.created_at as created_at," +
                    "users.max_level as max_level," +
                    "users.offline_pvp_count as offline_pvp_count," +
                    "users.offline_pvp_timestamp as offline_pvp_timestamp," +
                    "user_nodes.data as user_node_data" +
					" FROM users, user_id_name_map, user_nodes" +
                    " WHERE user_id_name_map.user_id = users.id" +
                    " AND user_nodes.user_id = users.id ");
 			ResultSet results = query.executeQuery();

			while (results.next()) {
				UserWrapper uw = new UserWrapper();
				getUserFromResult(uw.user, results, conn);
				user_list.add(uw);
			}
			
			if (!user_list.isEmpty()) {
				user_array = (UserWrapper[]) user_list.toArray(new UserWrapper[0]);
			}

 		} catch (Exception e) {
 			e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
 		
 		return user_array;
 	}
    
    public UserWrapper getUserDetails(int user_id) {
		
 		Connection conn = null;
 		UserWrapper uw = null;
 	
 		try {
 			conn = getConnection();
			uw = new UserWrapper();
			uw.user.id = user_id;
			if (!getUserByID(uw.user, conn)) {
				CBoxLoggerSyslog.logMessage("Not able to get user details by ID = " + user_id);
			}
 		} catch (Exception e) {
 			e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
 		
 		return uw;
 	}

    public boolean saveUser(User user) {
        Connection conn = null;
        boolean saved = false;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET money = ?, tokens = ?, character_slots = ?, inventory_spots = ?, "
                                                          + "tapjoy_tokens = ?, breadstick = ?, bread_slice = ?, bread_loaf = ?, first_day = ?, "
                                                          + "second_day = ?, third_day = ? "
                                                          + "WHERE id = ? ");
            query.setInt(1, user.money);
            query.setInt(2, user.tokens);
            query.setInt(3, user.character_slot);
            query.setInt(4, user.inventory_spots);
            query.setInt(5, user.tapjoy_tokens);
            query.setInt(6, user.breadstick);
            query.setInt(7, user.bread_slice);
            query.setInt(8, user.bread_loaf);
            query.setBoolean(9, user.first_day);
            query.setBoolean(10, user.second_day);
            query.setBoolean(11, user.third_day);
            query.setInt(12, user.id);
            query.executeUpdate();
            saved = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        
        return saved;
    }
    
    public boolean updateUserOneField(int value, String fieldname, int user_id) {
        Connection conn = null;
        boolean update = false;
        try {
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("UPDATE users SET " + fieldname + " = ? WHERE id = ?");
            query.setInt(1, value);
            query.setInt(2, user_id);
            query.executeUpdate();
            update = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return update;
    }
    
    public boolean updateUserTwoFields(int value1, String fieldname1, int value2, String fieldname2, int user_id) {
        Connection conn = null;
        boolean update = false;
        try {
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("UPDATE users SET " + fieldname1 + " = ?, " +
            		fieldname2 + " = ? WHERE id = ?");
            query.setInt(1, value1);
            query.setInt(2, value2);
            query.setInt(3, user_id);
            query.executeUpdate();
            update = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return update;
    }

    public int saveTokens(int tokens, int user_id) {
        Connection conn = null;
        int new_tokens = -1;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET tokens = tokens + ? WHERE id = ?");
            query.setInt(1, tokens);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT tokens from users WHERE id = ? ");
            query_two.setInt(1, user_id);

            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_tokens = result.getInt("tokens");
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return new_tokens;
    }

    public int saveMoney(int money, int user_id) {
        Connection conn = null;
        int new_money = -1;
        try {
            conn = getConnection();
 
            PreparedStatement query = conn.prepareStatement("UPDATE users SET money = money + ? WHERE users.id = ? ");
            query.setInt(1, money);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT money from users WHERE users.id = ? ");
            query_two.setInt(1, user_id);

            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_money = result.getInt("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return new_money;
    }

    public int saveCharacterSlots(int slots, int user_id) {
        Connection conn = null;
        int new_slots = -1;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET character_slot = character_slot + ?"
                                                          + " WHERE users.id = ? ");

            query.setInt(1, slots);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT character_slot from users WHERE users.id = ? ");
            query_two.setInt(1, user_id);
            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_slots = result.getInt("character_slot");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return new_slots;
    }

    public boolean saveNoRP(User user) {
        Connection conn = null;
        boolean updated = false;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET no_rp = ?, tokens = ?"
                                                                    + " WHERE users.id = ? ");

            query.setBoolean(1, user.no_rp);
            query.setInt(2, user.tokens);
            query.setInt(3, user.id);
            query.executeUpdate();
            updated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        return updated;
    }

    public int saveBreadStick(int breadstick, int user_id) {
        Connection conn = null;
        int new_breadstick = -1;

        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET breadstick = breadstick + ? WHERE users.id = ?");
            query.setInt(1, breadstick);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT breadstick from users WHERE users.id = ? ");
            query_two.setInt(1, user_id);

            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_breadstick = result.getInt("breadstick");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return new_breadstick;
    }

    public int saveBreadSlice(int bread_slice, int user_id) {
        Connection conn = null;
        int new_breadslice = -1;

        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET bread_slice = bread_slice + ? WHERE users.id = ?");
            query.setInt(1, bread_slice);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT bread_slice from users WHERE users.id = ? ");
            query_two.setInt(1, user_id);


            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_breadslice = result.getInt("bread_slice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return new_breadslice;
    }

    public int saveBreadLoaf(int bread_loaf, int user_id) {
        Connection conn = null;
        int new_breadloaf = -1;

        try {
            conn = getConnection();
 
            PreparedStatement query = conn.prepareStatement("UPDATE users SET bread_loaf = bread_loaf + ? WHERE users.id = ?");
            query.setInt(1, bread_loaf);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT bread_loaf from users WHERE users.id = ? ");
            query_two.setInt(1, user_id);


            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_breadloaf = result.getInt("bread_loaf");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return new_breadloaf;
    }

    public int saveInventorySpots(int inv_spots, int user_id) {
        Connection conn = null;
        int new_inventory_spots = -1;

        try {
            conn = getConnection();
  
            PreparedStatement query = conn.prepareStatement("UPDATE users SET inventory_spots = inventory_spots + ? WHERE users.id = ?");
            query.setInt(1, inv_spots);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT inventory_spots from users WHERE users.id = ? ");
            query_two.setInt(1, user_id);

            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_inventory_spots = result.getInt("inventory_spots");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        return new_inventory_spots;
    }

    public int saveTapjoyTokensIdentifier(int tapjoy_tokens, String identifier) {
        Connection conn = null;
        int new_tapjoy_tokens = 0;
        try {
            conn = getConnection();
            ArrayList<Integer> user_ids = new ArrayList<Integer>();

            PreparedStatement get_user_id = conn.prepareStatement("SELECT user_id FROM user_identifiers WHERE udid = ? or mac_address = ?");
            get_user_id.setString(1, identifier);
            get_user_id.setString(2, identifier);
            ResultSet results = get_user_id.executeQuery();
            while (results != null && results.next()) {
                user_ids.add(results.getInt("user_id"));
            }

            if (!user_ids.isEmpty()) {
                for (int user_id : user_ids) {
                    PreparedStatement query = conn.prepareStatement("UPDATE users SET tapjoy_tokens = tapjoy_tokens + ? WHERE id = ?");
                    query.setInt(1, tapjoy_tokens);
                    query.setInt(2, user_id);
                    query.executeUpdate();
                }


//                PreparedStatement query_two = conn.prepareStatement("SELECT tapjoy_tokens from users WHERE users.id = ? ");
//                query_two.setInt(1, user_id);
//
//
//                ResultSet result = query_two.executeQuery();
//                if (result != null && result.next()) {
//                    new_tapjoy_tokens = result.getInt("tapjoy_tokens");
//                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new_tapjoy_tokens = -1;
 
        } finally {
            closeConnection(conn);
        }
        
        return new_tapjoy_tokens;
    }

    public int saveTapjoyTokensUID(int tapjoy_tokens, int user_id) {
        Connection conn = null;
        int new_tapjoy_tokens = -1;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET tapjoy_tokens = tapjoy_tokens + ? WHERE users.id = ?");
            query.setInt(1, tapjoy_tokens);
            query.setInt(2, user_id);
            query.executeUpdate();

            PreparedStatement query_two = conn.prepareStatement("SELECT tapjoy_tokens from users WHERE users.id = ? ");
            query_two.setInt(1, user_id);


            ResultSet result = query_two.executeQuery();
            if (result != null && result.next()) {
                new_tapjoy_tokens = result.getInt("tapjoy_tokens");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeConnection(conn);
        }

        return new_tapjoy_tokens;
    }

    public boolean saveFirstDay(boolean first_day, int user_id) {
        Connection conn = null;
        boolean saved = false;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET first_day = ? WHERE users.id = ?");
            query.setBoolean(1, first_day);
            query.setInt(2, user_id);
            query.executeUpdate();
            saved = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return saved;
    }

    public boolean saveSecondDay(boolean second_day, int user_id) {
        Connection conn = null;
        boolean saved = false;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET first_day = ? WHERE user_id = ?");
            query.setBoolean(1, second_day);
            query.setInt(2, user_id);
            query.executeUpdate();
            saved = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return saved;
    }

    public boolean saveThirdDay(boolean third_day, int user_id) {
        Connection conn = null;
        boolean saved = false;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE users SET third_day = ? WHERE user_id = ?");
            query.setBoolean(1, third_day);
            query.setInt(2, user_id);
            query.executeUpdate();
            saved = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return saved;
    }

    @Override
    public List<String> getDeviceTokens() {
        Connection conn = null;
        List<String> device_tokens = new ArrayList<String>();

        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("SELECT device_token FROM user_id_name_map" +
                                                                    " WHERE device_token IS NOT NULL");
            ResultSet results = query.executeQuery();

            if(results != null) {
                while(results.next()) {
                    device_tokens.add(results.getString("device_token"));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return device_tokens;
    }
    
    public boolean saveUniqueIdentifier(int user_id, String udid, String mac_address) {
        Connection conn = null;
        boolean saved = false;

        ArrayList<String> identifiers = new ArrayList<String>();
        
        try {
            conn = getConnection();

            int num_q = 0;
            StringBuilder builder = new StringBuilder("INSERT INTO user_identifiers(");
            if (udid != null) {
                builder.append("udid,");
                num_q++;
                identifiers.add(udid);
            }
            if (mac_address != null) {
                builder.append("mac_address,");
                num_q++;
                identifiers.add(mac_address);
            }
            builder.append("user_id) VALUES (");
            for (int i=0;i<num_q;i++) {
                builder.append("?,");
            }
            builder.append("?)");
            String query = builder.toString();
            
            PreparedStatement save_identifier = conn.prepareStatement(query);
            for (int i=0;i<num_q;i++) {
                save_identifier.setString(i+1, identifiers.get(i));
            }
            save_identifier.setInt(num_q+1, user_id);
            save_identifier.executeUpdate();
            saved = true;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            closeConnection(conn);
        }
        return saved;
    }

    public ArrayList<String> getUserMACAddressesByID(int user_id) {
        Connection conn = null;
        ArrayList<String> identifiers = new ArrayList<String>();
        try {
            conn = getConnection();
            PreparedStatement get_identifiers = conn.prepareStatement("SELECT mac_address FROM user_identifiers WHERE user_id = ?");
            get_identifiers.setInt(1, user_id);
            ResultSet id_results = get_identifiers.executeQuery();
            while (id_results != null && id_results.next()) {
                identifiers.add(id_results.getString("mac_address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            closeConnection(conn);
        }
        return identifiers;
    }

    public static void getUserIdentifiers(User user, Connection conn) throws SQLException {
        ArrayList<String> udids = new ArrayList<String>();
        ArrayList<String> macs = new ArrayList<String>();
        PreparedStatement get_identifiers = conn.prepareStatement("SELECT * FROM user_identifiers WHERE user_id=?");
        get_identifiers.setInt(1, user.id);
        ResultSet id_results = get_identifiers.executeQuery();
        while (id_results != null && id_results.next()) {
            udids.add(id_results.getString("udid"));
            macs.add(id_results.getString("mac_address"));
        }
        user.udids = udids;
        user.mac_addresses = macs;
    }

    // static methods
    private static boolean getUserByID(User user, Connection conn) throws SQLException {

        PreparedStatement query = conn.prepareStatement("SELECT users.id as user_id," +
                                                                "user_id_name_map.username as username," +
                                                                "user_id_name_map.hashed_password as hash_pw," +
                                                                "users.money as user_money," +
                                                                "users.tokens as user_tokens," +
                                                                "users.character_slot as user_character_slots," +
                                                                "users.breadstick as user_breadstick," +
                                                                "users.bread_slice as user_breadslice," +
                                                                "users.bread_loaf as user_breadloaf," +
                                                                "users.inventory_spots as user_inventory_spots," +
                                                                "users.tapjoy_tokens as user_tapjoy_tokens," +
                                                                "users.message_of_the_day as user_message," +
                                                                "users.admin as admin," +
                                                                "users.souls as souls," +
                                                                "users.no_rp as no_rp," +
                                                                "users.last_sign_in_at as last_sign_in_at," +
                                                                "users.created_at as created_at," +
                                                                "users.max_level as max_level," +
                                                                "users.offline_pvp_count as offline_pvp_count," +
                                                                "users.offline_pvp_timestamp as offline_pvp_timestamp," +
                                                                "user_nodes.data as user_node_data" +
																" FROM users, user_id_name_map, user_nodes" +
                                                                " WHERE user_id_name_map.user_id = users.id" +
                                                                " AND user_nodes.user_id = users.id" +
                                                                " AND users.id = ? ");
        query.setInt(1, user.id);
        ResultSet results = query.executeQuery();
        if ((results == null) || !results.next())
        	return false;
        
        getUserFromResult(user, results, conn);
        return true;
    }
    
    private static void getUserFromResult(User user, ResultSet results, Connection conn) throws SQLException {

        user.id = results.getInt("user_id");
        user.username = results.getString("username");
        user.hashed_pw = results.getString("hash_pw");
        user.money = results.getInt("user_money");
        user.tokens = results.getInt("user_tokens");
        user.character_slot = results.getInt("user_character_slots");
        user.breadstick = results.getInt("user_breadstick");
        user.bread_slice = results.getInt("user_breadslice");
        user.bread_loaf = results.getInt("user_breadloaf");
        user.inventory_spots = results.getInt("user_inventory_spots");
        user.tapjoy_tokens = results.getInt("user_tapjoy_tokens");
        user.message_of_the_day = results.getString("user_message");
        user.admin = results.getBoolean("admin");
        user.souls = results.getInt("souls");
        user.no_rp = results.getBoolean("no_rp");
        user.last_sign_in_at = results.getTimestamp("last_sign_in_at");
        user.created_at = results.getTimestamp("created_at");
        user.max_level = results.getInt("max_level");
        user.offline_pvp_count = results.getInt("offline_pvp_count");
        user.offline_pvp_timestamp = results.getTimestamp("offline_pvp_timestamp");


        //Get User Achievements
        user.user_achievements = UserAchievementDBSQL.getUserAchievementDetails(user.id, conn);

        //Work with UserNodes result set      
        user.user_nodes = UserNodeDBSQL.getUserNodeArray(user.id, results.getBytes("user_node_data"));

        //Work with UserCharacters result set
        user.user_characters = UserCharacterDBSQL.getAllUserCharacters(user.id, conn);

        //Get Accessory Inventory and Sold Accessories
        UserCharacterAccessoryDBSQL.getAccessoryInventoryAndSold(user, conn);

        //Work with UserPlaylists result set
        user.user_playlists = UserPlaylistDBSQL.getUserPlaylistDetailsByID(user.id, conn);

        //Get user_souls
        user.user_souls = UserSoulDBSQL.getUserSouls(user.id, conn);

        //Get user_potions
        user.user_potions = UserPotionDBSQL.getUserPotions(user.id, conn);

        //Get identifiers
        getUserIdentifiers(user, conn);
    }
    
	public static void updateBattleUser(User user, Connection conn) throws SQLException {
		
        PreparedStatement query = conn.prepareStatement("UPDATE users SET money = ?, tokens = ?, "
                + "breadstick = ?, bread_slice = ?, bread_loaf = ?, souls = ?, max_level =? "
                + "WHERE id = ? ");
		query.setInt(1, user.money);
		query.setInt(2, user.tokens);
		query.setInt(3, user.breadstick);
		query.setInt(4, user.bread_slice);
		query.setInt(5, user.bread_loaf);
		query.setInt(6, user.souls);
        query.setInt(7, user.max_level);
		query.setInt(8, user.id);
		query.executeUpdate();
	}
    
    public static void updateBattleOfflineUser(User user, Connection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("UPDATE users SET money = ?, tokens = ?, "
                                                                + "breadstick = ?, bread_slice = ?, " +
                                                                "bread_loaf = ?, souls = ?, max_level =? "
                                                                + "WHERE id = ? ");
        query.setInt(1, user.money);
        query.setInt(2, user.tokens);
        query.setInt(3, user.breadstick);
        query.setInt(4, user.bread_slice);
        query.setInt(5, user.bread_loaf);
        query.setInt(6, user.souls);
        query.setInt(7, user.max_level);
        query.setInt(8, user.id);
        query.executeUpdate();
    }
    
    public boolean updateUserPvpTimestamp(int user_id) {
        boolean updated = false;
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement update_login = conn.prepareStatement("UPDATE users SET offline_pvp_timestamp = ? WHERE id = ?");
            java.util.Date date = new java.util.Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            update_login.setTimestamp(1, timestamp);
            update_login.setInt(2, user_id);
            update_login.executeUpdate();
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

    public static Map<Integer, String> getUserIdNameHashMap(Connection conn) throws SQLException {
   
    	Map<Integer, String> map = new HashMap<Integer, String>();
    	
        PreparedStatement query = conn.prepareStatement("SELECT username, user_id FROM user_id_name_map");
        ResultSet results = query.executeQuery();

        if (results != null) {
	        while (results.next()) {
				map.put(results.getInt("user_id"), results.getString("username"));
			}
        }

        return map;
    }
    
    //
    // Obsolete methods
    //
    public User getUserInfoByID(int user_id) {
        Connection conn = null;
        User user = null;

        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("SELECT user_id_name_map.username," +
                                                                    "users.money,users.tokens,users.character_slot," +
                                                                    "users.breadstick,users.bread_slice,users.bread_loaf," +
                                                                    "users.inventory_spots,users.tapjoy_tokens,users.message_of_the_day," +
                                                                    "users.souls,users.no_rp,users.admin FROM users, user_id_name_map"
                                                          + " WHERE user_id_name_map.user_id = users.id"
                                                          + " AND users.id = ? ");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();
            if (results.next()) {
                user = new User();
                user.id = user_id;
                user.username = results.getString("user_id_name_map.username");
                user.money = results.getInt("users.money");
                user.tokens = results.getInt("users.tokens");
                user.character_slot = results.getInt("users.character_slot");
                user.breadstick = results.getInt("users.breadstick");
                user.bread_slice = results.getInt("users.bread_slice");
                user.bread_loaf = results.getInt("users.bread_loaf");
                user.inventory_spots = results.getInt("users.inventory_spots");
                user.tapjoy_tokens = results.getInt("users.tapjoy_tokens");
                user.message_of_the_day = results.getString("users.message_of_the_day");
                user.admin = results.getBoolean("users.admin");
                user.souls = results.getInt("users.souls");
                user.no_rp = results.getBoolean("users.no_rp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	closeConnection(conn);
        }
        
        return user;
    }
    
    public User findRandUserByMaxLvl(int max_level, int self_id) {
        Connection conn = null;
        User user = new User();
        try {
            conn = getConnection();

            int user_id = 0;
            while (user_id == 0) {
                PreparedStatement get_user_id = conn.prepareStatement("SELECT id FROM users WHERE max_level = ? AND id != ? AND  id >= RAND() * (SELECT MAX(id) FROM users ) LIMIT 1");
                get_user_id.setInt(1, max_level);
                get_user_id.setInt(2, self_id);
                ResultSet get_user_results = get_user_id.executeQuery();
                if (!get_user_results.isBeforeFirst()) {
                    //no data
                    max_level--;
                }
                else {
                    while (get_user_results.next()) {
                        user_id = get_user_results.getInt("id");
                    }
                }
            }

            PreparedStatement decrement_offline_count = conn.prepareStatement("UPDATE users SET offline_pvp_count = offline_pvp_count - 1 WHERE id = ?");
            decrement_offline_count.setInt(1, self_id);
            decrement_offline_count.executeUpdate();


            PreparedStatement query = conn.prepareStatement("SELECT users.id as user_id," +
                                                                    "user_id_name_map.username as username," +
                                                                    "user_id_name_map.hashed_password as hash_pw," +
                                                                    "users.money as user_money," +
                                                                    "users.tokens as user_tokens," +
                                                                    "users.character_slot as user_character_slots," +
                                                                    "users.breadstick as user_breadstick," +
                                                                    "users.bread_slice as user_breadslice," +
                                                                    "users.bread_loaf as user_breadloaf," +
                                                                    "users.inventory_spots as user_inventory_spots," +
                                                                    "users.tapjoy_tokens as user_tapjoy_tokens," +
                                                                    "users.message_of_the_day as user_message," +
                                                                    "users.admin as admin," +
                                                                    "users.souls as souls," +
                                                                    "users.no_rp as no_rp," +
                                                                    "users.last_sign_in_at as last_sign_in_at," +
                                                                    "users.created_at as created_at," +
                                                                    "users.max_level as max_level," +
                                                                    "users.offline_pvp_count as offline_pvp_count," +
                                                                    "users.offline_pvp_timestamp as offline_pvp_timestamp," +
                                                                    "user_nodes.data as user_node_data" +
                                                                    " FROM users, user_id_name_map, user_nodes" +
                                                                    " WHERE user_id_name_map.user_id = users.id" +
                                                                    " AND user_nodes.user_id = users.id" +
                                                                    " AND users.id = ? ");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();
            while (results != null && results.next()) getUserFromResult(user, results, conn);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return user;
    }
    
    public boolean deleteUser(int user_id) {
        Connection conn = null;
        boolean deleted = false;
        
        try {
            conn = getConnection();
            
            PreparedStatement delete_ua = conn.prepareStatement("DELETE FROM user_accessories WHERE user_id = ?");
            delete_ua.setInt(1, user_id);
            delete_ua.executeUpdate();

            PreparedStatement delete_ui = conn.prepareStatement("DELETE FROM user_identifiers WHERE user_id = ?");
            delete_ui.setInt(1, user_id);
            delete_ui.executeUpdate();

            PreparedStatement delete_un = conn.prepareStatement("DELETE FROM user_nodes WHERE user_id = ?");
            delete_un.setInt(1, user_id);
            delete_un.executeUpdate();

            PreparedStatement delete_up = conn.prepareStatement("DELETE FROM user_playlists WHERE user_id = ?");
            delete_up.setInt(1, user_id);
            delete_up.executeUpdate();

            PreparedStatement delete_uq = conn.prepareStatement("DELETE FROM user_quests WHERE user_id = ?");
            delete_uq.setInt(1, user_id);
            delete_uq.executeUpdate();

            PreparedStatement delete_us = conn.prepareStatement("DELETE FROM user_souls WHERE user_id = ?");
            delete_us.setInt(1, user_id);
            delete_us.executeUpdate();

            PreparedStatement delete_upotions = conn.prepareStatement("DELETE FROM user_potions WHERE user_id = ?");
            delete_upotions.setInt(1, user_id);
            delete_upotions.executeUpdate();

            PreparedStatement delete_uca = conn.prepareStatement("DELETE FROM user_character_accessories WHERE user_id = ?");
            delete_uca.setInt(1, user_id);
            delete_uca.executeUpdate();
            
            PreparedStatement get_uc = conn.prepareStatement("SELECT * FROM user_characters WHERE user_id = ?");
            get_uc.setInt(1, user_id);
            ResultSet uc_results = get_uc.executeQuery();
            while(uc_results != null && uc_results.next()) {
                PreparedStatement delete_ucs = conn.prepareStatement("DELETE FROM user_character_skills WHERE user_character_id = ?");
                delete_ucs.setInt(1, uc_results.getInt("id"));
                delete_ucs.executeUpdate();
                PreparedStatement delete_uc = conn.prepareStatement("DELETE FROM user_characters WHERE id = ?");
                delete_uc.setInt(1, uc_results.getInt("id"));
                delete_uc.executeUpdate();
            }

            PreparedStatement delete_uidnmp = conn.prepareStatement("DELETE FROM user_id_name_map WHERE user_id = ?");
            delete_uidnmp.setInt(1, user_id);
            delete_uidnmp.executeUpdate();

            PreparedStatement delete_user = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            delete_user.setInt(1, user_id);
            delete_user.executeUpdate();

            deleted = true;
            
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            closeConnection(conn);
        }
        return deleted;
    }
    
}
