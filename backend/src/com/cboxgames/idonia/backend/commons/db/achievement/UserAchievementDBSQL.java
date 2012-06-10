package com.cboxgames.idonia.backend.commons.db.achievement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

// import com.cboxgames.idonia.backend.commons.DefaultValueTable;
import com.cboxgames.idonia.backend.UserAchievementHttpServlet;
import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.achievement.AchievementDBSQL;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.Achievement;
import com.cboxgames.utils.idonia.types.UserAchievement;
import com.cboxgames.utils.idonia.types.UserAchievement.UserAchievementWrapper;
import com.cboxgames.utils.idonia.types.proto.UserAchievementArrayProto;
import com.cboxgames.utils.idonia.types.proto.UserAchievementProto;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;

public class UserAchievementDBSQL extends DBSQL implements IUserAchievementDB {
	
    private static int DEFAULT_BUFFER_SIZE = 256;
	
	public UserAchievementDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
		super(data_source, servlet_context);
	}
	
//	private List<Achievement> getAchievements() {
//
//		 AchievementDBSQL a_db_sql = null;
//	     try {
//           a_db_sql = new AchievementDBSQL(getDataSource(), getServletContext());
//       }
//       catch (SQLException e) {
//           e.printStackTrace();
//       }
//
//		return a_db_sql.getAchievements();
//	}
//
//   //Populate User Achievement upon creating a new user, build a set of achievements, and then save it to the DB
//	public boolean createUserAchievements(User user, Connection conn) {
//
//		List<Achievement> ach_list = getAchievements();
//		List<UserAchievement> ua_list = new ArrayList<UserAchievement>();
//		// AchievementWrapper[] aw = DefaultValueTable.getAchievementWrapperArray();
//		Iterator<Achievement> itr = ach_list.iterator();
//		while (itr.hasNext()) {
//			Achievement a = itr.next();
//			UserAchievement ua = new UserAchievement();
//			ua.id = ua.achievement_id = a.id;
//			ua.user_id = user.id;
//			ua.user_analytic_id = a.analytic_id;
//			ua.complete = false;
//
//			Achievement ach = new Achievement();
//			ua.achievement = ach;
//			ach.id = a.id;
//			ach.name = new String(ach.name);
//			ach.description = new String(a.description);
//			ach.created_at = new String(a.created_at);
//			ach.updated_at = new String(a.updated_at);
//			ach.task = new String(a.task);
//			ach.analytic_id = a.analytic_id;
//			ach.max_count = a.max_count;
//			ua_list.add(ua);
//		}
//
//		if (!ua_list.isEmpty()) {
//			user.user_achievements = (UserAchievement[]) ua_list.toArray(new UserAchievement[0]);
//		}
//
//		try {
//			PreparedStatement query = conn.prepareStatement("INSERT INTO user_achievements(user_id, data) VALUES(?, ?)");
//			query.setInt(1, user.id);
//			query.setBytes(2, getUserAchievementProtoData());
//			query.executeUpdate();
//
//		} catch (SQLException e) {
//            e.printStackTrace();
//            try {
//                System.err.print("Transaction is being rolled back");
//                conn.rollback();
//            }
//            catch (SQLException f) {
//                f.printStackTrace();
//            }
//			return false;
//		}
//		return true;
//	}
//
//	public boolean createUserAchievements(int user_id) {
//
//		Connection conn = null;
//
//		try {
//			conn = getConnection();
//			PreparedStatement query = conn.prepareStatement("INSERT INTO user_achievements(user_id, data) VALUES(?, ?)");
//			query.setInt(1, user_id);
//			query.setBytes(2, getUserAchievementProtoData());
//			query.executeUpdate();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//
//		return true;
//	}
//
//	private byte[] getUserAchievementProtoData() {
//
//		List<Achievement> ach_list = getAchievements();
//		// AchievementWrapper[] aw = DefaultValueTable.getAchievementWrapperArray();
//		List<UserAchievementProto> user_ach_list = new ArrayList<UserAchievementProto>();
//		Iterator<Achievement> itr = ach_list.iterator();
//		while (itr.hasNext()) {
//			Achievement t = itr.next();
//			UserAchievementProto unp = new UserAchievementProto();
//			unp.setComplete(false);
//			unp.setAchievementId(t.id);
//			user_ach_list.add(unp);
//		}
//
//		UserAchievementArrayProto tut_array = new UserAchievementArrayProto();
//		tut_array.setUserAchievementList(user_ach_list);
//		byte[] user_achievement_data = ProtostuffIOUtil.toByteArray(tut_array, UserAchievementArrayProto.getSchema(),
//				LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
//		return user_achievement_data;
//	}
//
//	/**
//	 * Check if a user achievement of user_id exists
//	 */
//    public boolean userAchievementExist(int user_id) {
//
//        Connection conn = null;
//        boolean exist = false;
//        try {
//            conn = getConnection();
//            PreparedStatement query = conn.prepareStatement("SELECT * FROM user_achievements WHERE user_id = ? ");
//            query.setInt(1, user_id);
//            ResultSet results = query.executeQuery();
//            if (results.next()) {
//            	exist = true;
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//
//        return exist;
//    }
//
//    /**
//     *
//     * @return an array of user achievement wrapper
//     */
//    public UserAchievementWrapper[] getUserAchievementDetails() {
//
//		Connection conn = null;
//		List<UserAchievementWrapper> user_achievement_list = new ArrayList<UserAchievementWrapper>();
//		UserAchievementWrapper[] user_achievement_array = null;
//
//		try {
//			conn = getConnection();
//
//			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_achievements");
//			ResultSet result = query.executeQuery();
//			if (result != null) {
//				while (result.next()) {
//					byte[] data = result.getBytes("data");
//					if (data == null) continue;
//
//					int user_id = result.getInt("user_id");
//					UserAchievementArrayProto tut_array = new UserAchievementArrayProto();
//					ProtostuffIOUtil.mergeFrom(data, tut_array, UserAchievementArrayProto.getSchema());
//
//					List<UserAchievementProto> unp_list = tut_array.getUserAchievementList();
//					for (UserAchievementProto unp : unp_list) {
//						UserAchievementWrapper unw = new UserAchievementWrapper();
//						UserAchievement un = unw.user_achievement;
//						un.user_id = user_id;
//						un.achievement_id = unp.getAchievementId();
//						un.complete = unp.getComplete();
//						user_achievement_list.add(unw);
//					}
//				}
//
//				if (!user_achievement_list.isEmpty()) {
//					user_achievement_array = (UserAchievementWrapper[]) user_achievement_list.toArray(new UserAchievementWrapper[0]);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//
//		return user_achievement_array;
//	}
//
//    /**
//     * @param user_id the user's id
//     * @return an array of user achievement wrappers for a given user_id
//     */
//    public UserAchievementWrapper[] getUserAchievementDetails(int user_id) {
//
//		Connection conn = null;
//		List<UserAchievementWrapper> user_achievement_list = new ArrayList<UserAchievementWrapper>();
//		UserAchievementWrapper[] user_achievement_array = null;
//
//		try {
//			conn = getConnection();
//
//			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_achievements WHERE user_id = ?");
//			query.setInt(1, user_id);
//			ResultSet result = query.executeQuery();
//			if (result != null) {
//				while (result.next()) {
//					byte[] data = result.getBytes("data");
//					if (data == null) continue;
//
//					UserAchievementArrayProto tut_array = new UserAchievementArrayProto();
//					ProtostuffIOUtil.mergeFrom(data, tut_array, UserAchievementArrayProto.getSchema());
//
//					List<UserAchievementProto> unp_list = tut_array.getUserAchievementList();
//					for (UserAchievementProto unp : unp_list) {
//						UserAchievementWrapper unw = new UserAchievementWrapper();
//						UserAchievement un = unw.user_achievement;
//						un.user_id = user_id;
//						un.achievement_id = unp.getAchievementId();
//						un.complete = unp.getComplete();
//						user_achievement_list.add(unw);
//					}
//				}
//
//				if (!user_achievement_list.isEmpty()) {
//					user_achievement_array = (UserAchievementWrapper[]) user_achievement_list.toArray(new UserAchievementWrapper[0]);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//
//		return user_achievement_array;
//	}
//
//    /**
//     * Returns an array of user achievements for a given user.
//     * Pre-Condition: user with user_id must exit
//     * @param user_id the user's id
//     * @return an array of user achievements for a given user_id
//     */
//    public UserAchievement[] getUserAchievementDetailsByUserID(int user_id) {
//
//		Connection conn = null;
//		List<UserAchievement> user_achievement_list = new ArrayList<UserAchievement>();
//		UserAchievement[] user_achievement_array = null;
//
//		try {
//			conn = getConnection();
//
//			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_achievements WHERE user_id = ?");
//			query.setLong(1, user_id);
//			ResultSet result = query.executeQuery();
//			if (result != null) {
//				while (result.next()) {
//					byte[] data = result.getBytes("data");
//					if (data == null) continue;
//
//					UserAchievementArrayProto tut_array = new UserAchievementArrayProto();
//					ProtostuffIOUtil.mergeFrom(data, tut_array, UserAchievementArrayProto.getSchema());
//
//					List<UserAchievementProto> unp_list = tut_array.getUserAchievementList();
//					for (UserAchievementProto unp : unp_list) {
//						UserAchievement un = new UserAchievement();
//						un.user_id = user_id;
//						un.achievement_id = unp.getAchievementId();
//						un.complete = unp.getComplete();
//						user_achievement_list.add(un);
//					}
//				}
//
//				if (!user_achievement_list.isEmpty()) {
//					user_achievement_array = (UserAchievement[]) user_achievement_list.toArray(new UserAchievement[0]);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//
//		return user_achievement_array;
//	}
//
//    public boolean userAchievementComplete(int user_id, int achievement_id) {
//
//		boolean update = false;
//		Connection conn = null;
//
//		try {
//			conn = getConnection();
//
//			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_achievements WHERE user_id = ?");
//			query.setInt(1, user_id);
//			ResultSet result = query.executeQuery();
//			if (result != null) {
//				while (result.next()) {
//					byte[] data = result.getBytes("data");
//					if (data == null) continue;
//
//					UserAchievementArrayProto tut_array = new UserAchievementArrayProto();
//					ProtostuffIOUtil.mergeFrom(data, tut_array, UserAchievementArrayProto.getSchema());
//
//					boolean found = false;
//					List<UserAchievementProto> unp_list = tut_array.getUserAchievementList();
//					for (UserAchievementProto unp : unp_list) {
//						if (unp.getAchievementId() != achievement_id) continue;
//						unp.setComplete(true);
//						found = true;
//						break;
//					}
//
//					if (found == false) continue;
//
//					byte[] user_achievement_data = ProtostuffIOUtil.toByteArray(tut_array, UserAchievementArrayProto.getSchema(),
//							LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
//
//					PreparedStatement update_query = conn.prepareStatement("UPDATE user_achievements SET data = ? WHERE user_id = ?");
//					update_query.setBytes(1, user_achievement_data);
//					update_query.setInt(2, user_id);
//					update_query.executeUpdate();
//					update = true;
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//
//		return update;
//	}
//
//
//
//    public boolean deleteUserAchievement(int user_id) {
//		boolean delete = false;
//		Connection conn = null;
//
//		try {
//			conn = getConnection();
//
//			PreparedStatement query = conn.prepareStatement("DELETE FROM user_achievements WHERE user_id = ?");
//			query.setInt(1, user_id);
//			int cnt = query.executeUpdate();
//			System.out.println(cnt + " of user_achievement with user_id = " + user_id + " are deleted");
//			delete = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//
//		return delete;
//	}


    @Override
    public boolean userAchievementComplete(int user_id, UserAchievement achievement, int character_id, int amount) {
        boolean created = false;
        Connection conn = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            
            PreparedStatement query = conn.prepareStatement("SELECT data FROM user_achievements WHERE user_id = ?");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();
            if (!results.isBeforeFirst()) {
                //no data
                //create the first achievement and save it to db
                UserAchievementArrayProto ach_array = new UserAchievementArrayProto();

                List<UserAchievementProto> uap_list = new ArrayList<UserAchievementProto>();
                UserAchievementProto new_uap = new UserAchievementProto();
                new_uap.setAchievementId(achievement.achievement_id);
                new_uap.setCharacterId(achievement.character_id);
                new_uap.setComplete(achievement.complete);

                uap_list.add(new_uap);

                ach_array.setUserAchievementList(uap_list);
                byte[] new_data = ProtostuffIOUtil.toByteArray(ach_array, UserAchievementArrayProto.getSchema(),
                                                               LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
                PreparedStatement create = conn.prepareStatement("INSERT INTO user_achievements(user_id, data) VALUES (?,?) ");
                create.setInt(1, user_id);
                create.setBytes(2, new_data);
                create.executeUpdate();
                created = true;
            }
            else {
                while (results.next()) {
                    byte[] data = results.getBytes("data");
                    if (data == null) continue;

                    UserAchievementArrayProto ach_array = new UserAchievementArrayProto();
                    ProtostuffIOUtil.mergeFrom(data, ach_array, UserAchievementArrayProto.getSchema());

                    List<UserAchievementProto> uap_list = ach_array.getUserAchievementList();

                    UserAchievementProto new_uap = new UserAchievementProto();
                    new_uap.setAchievementId(achievement.achievement_id);
                    new_uap.setCharacterId(achievement.character_id);
                    new_uap.setComplete(achievement.complete);

                    uap_list.add(new_uap);

                    ach_array.setUserAchievementList(uap_list);
                    byte[] new_data = ProtostuffIOUtil.toByteArray(ach_array, UserAchievementArrayProto.getSchema(),
                                                                   LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
                    PreparedStatement update = conn.prepareStatement("UPDATE user_achievements SET data = ? WHERE user_id = ?");
                    update.setBytes(1, new_data);
                    update.setInt(2, user_id);
                    update.executeUpdate();
                    created = true;
                }
            }

            if (character_id <= 5) {
                PreparedStatement give_stats = conn.prepareStatement("UPDATE user_characters SET stats = stats + ? WHERE user_id = ? AND character_id = ?");
                give_stats.setInt(1, amount);
                give_stats.setInt(2, user_id);
                give_stats.setInt(3, character_id);
                give_stats.executeUpdate();
            }
            else if (character_id == 6) {
                PreparedStatement give_money = conn.prepareStatement("UPDATE users SET money = money + ? WHERE id = ?");
                give_money.setInt(1, amount);
                give_money.setInt(2, user_id);
                give_money.executeUpdate();
            }
            else if (character_id == 7) {
                PreparedStatement give_tokens = conn.prepareStatement("UPDATE users SET tokens = tokens + ? WHERE id = ?");
                give_tokens.setInt(1, amount);
                give_tokens.setInt(2, user_id);
                give_tokens.executeUpdate();
            }
            
            conn.commit();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return created;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static UserAchievement[] getUserAchievementDetails(int user_id, Connection conn) {

        List<UserAchievement> ua_list = new ArrayList<UserAchievement>();

        try {
            PreparedStatement get_achievements = conn.prepareStatement("SELECT data FROM user_achievements WHERE user_id = ?");
            get_achievements.setInt(1, user_id);
            ResultSet results = get_achievements.executeQuery();
            while (results != null && results.next()) {
                byte[] data = results.getBytes("data");
                if (data == null) continue;

                UserAchievementArrayProto ach_array = new UserAchievementArrayProto();
                ProtostuffIOUtil.mergeFrom(data, ach_array, UserAchievementArrayProto.getSchema());

                List<UserAchievementProto> uap_list = ach_array.getUserAchievementList();
                for (UserAchievementProto uap : uap_list) {
                    UserAchievement ua = new UserAchievement();
                    ua.character_id = uap.getCharacterId();
                    ua.achievement_id = uap.getAchievementId();
                    ua.complete = uap.getComplete();
                    ua_list.add(ua);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return (UserAchievement[]) ua_list.toArray(new UserAchievement[0]);  //To change body of implemented methods use File | Settings | File Templates.
    }
    
}
