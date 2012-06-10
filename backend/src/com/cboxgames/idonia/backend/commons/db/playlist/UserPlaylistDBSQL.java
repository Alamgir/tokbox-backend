package com.cboxgames.idonia.backend.commons.db.playlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.playlist.IUserPlaylistDB;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.Playlist;
import com.cboxgames.utils.idonia.types.UserPlaylist;
import com.cboxgames.utils.idonia.types.UserPlaylist.UserPlaylistWrapper;
import com.cboxgames.utils.idonia.types.Leaderboard;
import com.cboxgames.utils.idonia.types.Leaderboard.RatingEntry;

public class UserPlaylistDBSQL extends DBSQL implements IUserPlaylistDB {
	
	//	private static final int PER_PAGE_SIZE = 10;
	//	private static final int NUM_PAGE_BEFORE = 3;
	//	private static final int NUM_PAGE_AFTER = 3;
    private PlaylistDBSQL p_db_sql;
    
	public UserPlaylistDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
		super(data_source, servlet_context);
        
        p_db_sql = new PlaylistDBSQL(getDataSource(), getServletContext());
	}
	
	private void saveUserPlaylist(Connection conn, int user_id, int rating) throws SQLException {
		
		List<Playlist> plw = p_db_sql.getPlaylists(conn);
		PreparedStatement query = conn.prepareStatement("INSERT INTO user_playlists" +
				"(user_id, playlist_id, rating, wins, losses, forfeit_count, disconnect_count) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?)");

		Iterator<Playlist> itr = plw.iterator();
		while (itr.hasNext()) {
			Playlist pl = itr.next();
			query.setInt(1, user_id);
			query.setInt(2, pl.id);
			query.setInt(3, rating);
			query.setInt(4, 0);
			query.setInt(5, 0);
			query.setInt(6, 0);
			query.setInt(7, 0);
			query.executeUpdate();
		}
	}

    private void saveUserPlaylistNew(Connection conn, int user_id, int rating, String username) throws SQLException {
        List<Playlist> plw = p_db_sql.getPlaylists(conn);
		PreparedStatement query = conn.prepareStatement("INSERT INTO user_playlists" +
				"(user_id, playlist_id, rating, wins, losses, forfeit_count, disconnect_count, username) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		Iterator<Playlist> itr = plw.iterator();
		while (itr.hasNext()) {
			Playlist pl = itr.next();
			query.setInt(1, user_id);
			query.setInt(2, pl.id);
			query.setInt(3, rating);
			query.setInt(4, 0);
			query.setInt(5, 0);
			query.setInt(6, 0);
			query.setInt(7, 0);
            query.setString(8, username);
			query.executeUpdate();
		}
    }
	
	public boolean createUserPlaylists(User user, Connection conn) {
		
		List<UserPlaylist> up_list = new ArrayList<UserPlaylist>();
		List<Playlist> plw = p_db_sql.getPlaylists(conn);
		Iterator<Playlist> itr = plw.iterator();
		while (itr.hasNext()) {
			Playlist a = itr.next();
			UserPlaylist ua = new UserPlaylist();
			ua.id = ua.playlist_id = a.id;
			ua.user_id = user.id;
            ua.username = user.username;
			up_list.add(ua);
		}
		
		boolean created = false;
		if (!up_list.isEmpty()) {
			user.user_playlists = (UserPlaylist[]) up_list.toArray(new UserPlaylist[0]);
		}
		
		try {
			saveUserPlaylistNew(conn, user.id, Constants.INITIAL_RATING, user.username);
			created = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return created;
	}
	 
    // Populate User Playlist upon creating a new user, build a set of playlists, and then save it to the DB
	public boolean createUserPlaylists(int user_id) {
		
		Connection conn = null;
		boolean created = false;
		try {
			conn = getConnection();
			saveUserPlaylist(conn, user_id, Constants.INITIAL_RATING);
			created = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return created;
	}
	
	/**
	 * Check if a user playlist of user_id exists
	 */
    public boolean userPlaylistExist(int user_id) {
	   
        Connection conn = null;
        boolean exist = false;
        try {
        	
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("SELECT id FROM user_playlists WHERE user_id = ? ");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();
            if (results.next()) {
            	exist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
        
        return exist;
    }

    /**
     * 
     * @return an array of user playlist wrapper
     */
    public UserPlaylistWrapper[] getUserPlaylistDetails() {
		
		Connection conn = null;
		UserPlaylistWrapper[] wrapper_array = null;
	
		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT user_id,playlist_id,rating,wins,losses," +
                                                                    "forfeit_count,disconnect_count FROM user_playlists");
			ResultSet result = query.executeQuery();
			wrapper_array = createUserPlaylistFromResultSet(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return wrapper_array;
	}
    
    /**
     * 
     * @return an array of user playlist wrappers for a given user_id
     */
    public UserPlaylistWrapper[] getUserPlaylistDetails(int user_id) {
		
		Connection conn = null;
		UserPlaylistWrapper[] wrapper_array = null;
	
		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT user_id,playlist_id,rating,wins,losses,forfeit_count,disconnect_count  FROM user_playlists WHERE user_id = ?");
			query.setInt(1, user_id);
			ResultSet result = query.executeQuery();
			wrapper_array = createUserPlaylistFromResultSet(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return wrapper_array;
	}

    /**
     * @param user_id the user's id
     * @return an array of user playlist wrappers for a given user_id
     */
    public UserPlaylist[] getUserPlaylistDetailsByID(int user_id) {

		Connection conn = null;
		UserPlaylist[] user_playlist_array = null;

		try {
			conn = getConnection();

			PreparedStatement query = conn.prepareStatement("SELECT user_id,playlist_id,rating,wins,losses,forfeit_count,disconnect_count FROM user_playlists WHERE user_id = ?");
			query.setInt(1, user_id);
			ResultSet result = query.executeQuery();
			UserPlaylistWrapper[] wrapper_array = createUserPlaylistFromResultSet(result);
			if (wrapper_array != null) {
				List<UserPlaylist> list = new ArrayList<UserPlaylist>();
				for (int ix = 0, length = wrapper_array.length; ix < length; ix++) {
					list.add(wrapper_array[ix].user_playlist);
				}
				
				if (!list.isEmpty()) {
					user_playlist_array = (UserPlaylist[]) list.toArray(new UserPlaylist[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return user_playlist_array;
	}
    

    public Leaderboard getLeaderboardNew(int user_id, int playlist_id) {
    	
    	Connection conn = null;
    	Leaderboard leaderboard = null;
    	
    	try {
			conn = getConnection();
			leaderboard = new Leaderboard();
			leaderboard.playlist_id = Integer.toString(playlist_id);
			List<RatingEntry> top_list = leaderboard.top_hundred;
			List<RatingEntry> neighbor_list = leaderboard.user_hundred;
			
	    	PreparedStatement query_playlists = conn.prepareStatement("SELECT user_id,username,rating,wins,losses,forfeit_count,disconnect_count FROM user_playlists WHERE playlist_id = ? ORDER BY rating DESC");
	    	query_playlists.setInt(1, playlist_id);
			ResultSet playlist_result = query_playlists.executeQuery();
			int top_rank = 0, user_rank = 0, up_neighbor_count = 0, down_neighbor_count = 0;
			boolean current_user_id_found = false;
			while (playlist_result.next()) {
				RatingEntry entry = new RatingEntry();
                entry.username = playlist_result.getString("username");
                entry.rating = playlist_result.getInt("rating");
                entry.wins = playlist_result.getInt("wins");
                entry.losses = playlist_result.getInt("losses");
                entry.forfeit_count = playlist_result.getInt("forfeit_count");
                entry.disconnect_count = playlist_result.getInt("disconnect_count");
                // entry.title = getRatingTitle(entry.rating);
				int uid = playlist_result.getInt("user_id");
				if (top_rank < Constants.TOP_LIST_LIMIT) {
					top_list.add(entry);
					top_rank++;
				}
				
				if (down_neighbor_count >= Constants.DOWN_NEIGHBOR_LIMIT) {
					if (top_rank >= Constants.TOP_LIST_LIMIT) break;
					continue;
				}

				user_rank++;
				neighbor_list.add(entry);
				if (uid == user_id) {
					leaderboard.user.ranking = user_rank;
					leaderboard.user.rating = entry.rating;
					leaderboard.user.user_id = user_id;
					leaderboard.user.username = entry.username;
					current_user_id_found = true;
				}
				else if (current_user_id_found) {
					down_neighbor_count++;
				}
				else {
					up_neighbor_count++;
					if (up_neighbor_count > Constants.UP_NEIGHBOR_LIMIT) {
						up_neighbor_count--;
						neighbor_list.remove(0);
					}
				}
			}
			
			// calculate user's position in the user_hundred list (starting from 0)
			leaderboard.user.user_position = (up_neighbor_count < Constants.UP_NEIGHBOR_LIMIT) ? 
											  up_neighbor_count : Constants.UP_NEIGHBOR_LIMIT;
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
    		closeConnection(conn);
    	}
    	
    	return leaderboard;
    }

	public boolean deleteUserPlaylist(int user_id) {
		boolean is_deleted = false;
		Connection conn = null;

		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("DELETE FROM user_playlists WHERE user_id = ?");
			query.setInt(1, user_id);
			int cnt = query.executeUpdate();
			System.out.println(cnt + " of user_playlist with user_id = " + user_id + " are deleted");
			is_deleted = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return is_deleted;
	}
	
    // static method 
    /**
     * @param user_id the user's id
     * @return an array of user playlists for a given user_id and connection
     * @throws SQLException 
     */
    public static UserPlaylist[] getUserPlaylistDetailsByID(int user_id, Connection conn) throws SQLException {

		PreparedStatement query = conn.prepareStatement("SELECT user_id,playlist_id,rating,wins,losses,forfeit_count,disconnect_count FROM user_playlists WHERE user_id = ?");
		query.setInt(1, user_id);
		ResultSet result = query.executeQuery();
		UserPlaylistWrapper[] wrapper_array = createUserPlaylistFromResultSet(result);
		
		if (wrapper_array == null) return null;
		
		List<UserPlaylist> list = new ArrayList<UserPlaylist>();
		for (int ix = 0, length = wrapper_array.length; ix < length; ix++) {
			list.add(wrapper_array[ix].user_playlist);
		}
			
		return (UserPlaylist[]) list.toArray(new UserPlaylist[0]);
	}

    private static UserPlaylistWrapper[] createUserPlaylistFromResultSet(ResultSet result) throws SQLException {
    	
    	if (result == null) return null;
	
		List<UserPlaylistWrapper> wrapper_list = new ArrayList<UserPlaylistWrapper>();
		
		while (result.next()) {
			UserPlaylistWrapper upw = new UserPlaylistWrapper();
			UserPlaylist up = upw.user_playlist;
			up.user_id = result.getInt("user_id");
			up.playlist_id = result.getInt("playlist_id");
			up.rating = result.getInt("rating");
			up.wins = result.getInt("wins");
			up.losses = result.getInt("losses");
			up.forfeit_count  = result.getInt("forfeit_count");
			up.disconnect_count  = result.getInt("disconnect_count");
			wrapper_list.add(upw);
		}
		
		if (wrapper_list.isEmpty()) return null;
		
		return (UserPlaylistWrapper[]) wrapper_list.toArray(new UserPlaylistWrapper[0]);
    }
    
    public static void updateUserPlaylist(UserPlaylist userplaylist, Connection conn) throws SQLException {
		
		 PreparedStatement query = conn.prepareStatement("UPDATE user_playlists SET disconnect_count = ?, rating = ?, "
	                + "wins = ?, losses = ?, forfeit_count = ? "
	                + "WHERE user_id = ? AND playlist_id = ?");
		query.setInt(1, userplaylist.disconnect_count);
		query.setInt(2, userplaylist.rating);
		query.setInt(3, userplaylist.wins);
		query.setInt(4, userplaylist.losses);
		query.setInt(5, userplaylist.forfeit_count);
		query.setInt(6, userplaylist.user_id);
		query.setInt(7, userplaylist.playlist_id);
		query.executeUpdate();
	}
    
    // Implemented by the client (1/23/2012)
//    public static final int APPRENTICE_UB = 1600;
//    public static final int FIGHTER_UB = 1700;
//    public static final int GLADIATOR_UB = 1800;
//    public static final int KNIGHT_UB = 1900;
//    public static final int TEMPLAR_UB = 2000;
//    public static final int JUSTICAR_UB = 2100;
//    
//    private static String getRatingTitle(int rating) {
//    	if (rating < Constants.TRAINEE_UB)
//    		return new String(Constants.TRAINEE);
//    	if (rating < Constants.APPRENTICE_UB)
//    		return new String(Constants.APPRENTICE);
//    	if (rating < Constants.FIGHTER_UB)
//    		return new String(Constants.FIGHTER);
//    	if (rating < Constants.GLADIATOR_UB)
//    		return new String(Constants.GLADIATOR);
//    	if (rating < Constants.KNIGHT_UB)
//    		return new String(Constants.KNIGHT);
//    	if (rating < Constants.TEMPLAR_UB)
//    		return new String(Constants.TEMPLAR);
//    	if (rating < Constants.JUSTICAR_UB)
//    		return new String(Constants.JUSTICAR);
//    	return new String(Constants.CHAMPION);
    
    // if both user_id and page are -1, page is set to 1 ?
    // Does the page start with 0 or 1?
//    public Leaderboard getLeaderboard(int user_id, int playlist_id, int page) {
//    	
//    	Connection conn = null;
//    	Leaderboard leaderboard = null;
//    	try {
//			conn = getConnection();
//			Map<Integer, String> name_id_map = UserDBSQL.getUserIdNameHashMap(conn);
//			leaderboard = new Leaderboard();
//			leaderboard.playlist_id = Integer.toString(playlist_id);
//			leaderboard.per_page = PER_PAGE_SIZE;
//			
//			PreparedStatement query_playlists = conn.prepareStatement("SELECT user_id, rating FROM user_playlists WHERE playlist_id = ? ORDER BY rating DESC");
//	    	query_playlists.setInt(1, playlist_id);
//			ResultSet result = query_playlists.executeQuery();
//			
//			List<PageEntry> player_list = new ArrayList<PageEntry>();
//			List<Integer> user_id_tbl = new ArrayList<Integer>();
//			int user_index = -1;
//			while (result.next()) {
//				PageEntry entry = new PageEntry();
//				entry.rating = result.getInt("rating");
//				int uid = result.getInt("user_id");
//				if ((user_id != -1) && (user_id == uid))
//					user_index = player_list.size();
//				user_id_tbl.add(uid);
//				player_list.add(entry);
//			}
//			
//			int plsize = player_list.size();
//			if (plsize > 0) {
//				int tmp = plsize / PER_PAGE_SIZE;
//				leaderboard.total_pages = ((tmp * PER_PAGE_SIZE) != plsize) ? (tmp + 1) : tmp;
//				int st_index, end_index;
//				if (user_id != -1) {
//					if (user_index != 0) {
//						tmp = user_index / PER_PAGE_SIZE;
//						leaderboard.user_rating_page = ((tmp * PER_PAGE_SIZE) != user_index) ? (tmp + 1) : tmp;
//					}
//					else leaderboard.user_rating_page = 1;
//					
//					st_index = (leaderboard.user_rating_page <= (NUM_PAGE_BEFORE + 1)) ? 0 : 
//							((leaderboard.user_rating_page - (NUM_PAGE_BEFORE + 1)) * PER_PAGE_SIZE);
//					
//					tmp = (leaderboard.user_rating_page + NUM_PAGE_AFTER) * PER_PAGE_SIZE;
//					end_index = (tmp < plsize) ? tmp : plsize;
//				}
//				else {
//					if (page > leaderboard.total_pages) {
//						System.err.println("The specified page " + page + " is bigger than the total page " +
//								leaderboard.total_pages);
//						leaderboard = null;
//					}
//					else {
//						leaderboard.start_page = page;
//					}
//					
//					st_index = (leaderboard.start_page <= (NUM_PAGE_BEFORE + 1)) ? 0 : 
//						((leaderboard.start_page - (NUM_PAGE_BEFORE + 1)) * PER_PAGE_SIZE);
//				
//					tmp = (leaderboard.start_page + NUM_PAGE_AFTER) * PER_PAGE_SIZE;
//					end_index = (tmp < plsize) ? tmp : plsize;
//				}
//				
//				if (leaderboard != null) {
//					int ix = st_index;
//					while (ix < end_index) {
//						List<PageEntry> list = new ArrayList<PageEntry>();
//						for (int iy = 0; iy < PER_PAGE_SIZE; iy++) {
//							if (ix >= end_index) break;
//							
//							PageEntry entry = player_list.get(ix);
//							entry.username = name_id_map.get(user_id_tbl.get(ix));
//							list.add(entry);
//							ix++;
//						}
//						
//						leaderboard.pages.add((PageEntry[]) list.toArray(new PageEntry[0]));
//					}
//				}
//			}
//			else leaderboard = null;
//			
//    	} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//    	return leaderboard;
//    }

//    public Leaderboard getUserLeaderboard(int user_id, String username) {
//        Connection conn = null;
//        Leaderboard leaderboard = null;
//
//        try {
//            conn = getConnection();
//            leaderboard = new Leaderboard();
//
//            PreparedStatement get_ranking = conn.prepareStatement("SELECT Count(*) as count FROM user_playlists WHERE rating > (" +
//                                                                          " SELECT rating FROM user_playlists WHERE user_id = ?)" +
//                                                                          " OR username >= (SELECT username FROM user_playlists WHERE " +
//                                                                          " user_id = ?)");
//            get_ranking.setInt(1, user_id);
//            get_ranking.setInt(2, user_id);
//            ResultSet get_ranking_results = get_ranking.executeQuery();
//            if (get_ranking_results != null) {
//                while (get_ranking_results.next()) {
//                    leaderboard.user.ranking = get_ranking_results.getInt("count");
//                }
//            }
//
//            PreparedStatement get_top_list = conn.prepareStatement("SELECT * FROM user_playlists ORDER BY rating DESC, username DESC LIMIT 100");
//            ResultSet top_list_results = get_top_list.executeQuery();
//            if (top_list_results != null) {
//                while (top_list_results.next()) {
//                    RatingEntry entry = new RatingEntry();
//                    entry.username = top_list_results.getString("username");
//                    entry.rating = top_list_results.getInt("rating");
//                    entry.wins = top_list_results.getInt("wins");
//                    entry.losses = top_list_results.getInt("losses");
//                    entry.forfeit_count = top_list_results.getInt("forfeit_count");
//                    entry.disconnect_count = top_list_results.getInt("disconnect_count");
//                    // entry.title = getRatingTitle(entry.rating);
//                    leaderboard.top_hundred.add(entry);
//                }
//            }
//
//            PreparedStatement get_fifty_above = conn.prepareStatement("SELECT * FROM user_playlists WHERE rating > " +
//                                                                              "(SELECT rating FROM user_playlists WHERE user_id = ?) ORDER BY rating DESC LIMIT 50");
//            get_fifty_above.setInt(1, user_id);
//            ResultSet fifty_above_results = get_fifty_above.executeQuery();
//            if (fifty_above_results != null) {
//                while (fifty_above_results.next()) {
//                    RatingEntry entry = new RatingEntry();
//                    entry.username = fifty_above_results.getString("username");
//                    entry.rating = fifty_above_results.getInt("rating");
//                    entry.wins = fifty_above_results.getInt("wins");
//                    entry.losses = fifty_above_results.getInt("losses");
//                    entry.forfeit_count = fifty_above_results.getInt("forfeit_count");
//                    entry.disconnect_count = fifty_above_results.getInt("disconnect_count");
//                    // entry.title = getRatingTitle(entry.rating);
//                    leaderboard.user_hundred.add(entry);
//                }
//            }
//
//            PreparedStatement get_user_info = conn.prepareStatement("SELECT * FROM user_playlists WHERE user_id = ?");
//            get_user_info.setInt(1, user_id);
//            ResultSet user_info = get_user_info.executeQuery();
//
//            if (user_info != null) {
//                while (user_info.next()) {
//                    RatingEntry entry = new RatingEntry();
//                    entry.username = user_info.getString("username");
//                    entry.rating = user_info.getInt("rating");
//                    entry.wins = user_info.getInt("wins");
//                    entry.losses = user_info.getInt("losses");
//                    entry.forfeit_count = user_info.getInt("forfeit_count");
//                    entry.disconnect_count = user_info.getInt("disconnect_count");
//                    // entry.title = getRatingTitle(entry.rating);
//                    leaderboard.user_hundred.add(entry);
//                    leaderboard.user.rating = entry.rating;
//                    leaderboard.user.user_id = user_id;
//                }
//            }
//
//            PreparedStatement get_fifty_below = conn.prepareStatement("SELECT * FROM user_playlists WHERE rating <= " +
//                                                                              "(SELECT rating FROM user_playlists WHERE user_id = ?) ORDER BY rating DESC LIMIT 50");
//            get_fifty_below.setInt(1, user_id);
//            ResultSet fifty_below_results = get_fifty_below.executeQuery();
//
//            if (fifty_below_results != null) {
//                while (fifty_below_results.next()) {
//                    RatingEntry entry = new RatingEntry();
//                    entry.username = fifty_below_results.getString("username");
//                    entry.rating = fifty_below_results.getInt("rating");
//                    entry.wins = fifty_below_results.getInt("wins");
//                    entry.losses = fifty_below_results.getInt("losses");
//                    entry.forfeit_count = fifty_below_results.getInt("forfeit_count");
//                    entry.disconnect_count = fifty_below_results.getInt("disconnect_count");
//                    // entry.title = getRatingTitle(entry.rating);
//                    leaderboard.user_hundred.add(entry);
//                }
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//		} finally {
//			closeConnection(conn);
//		}
//        return leaderboard;
//    }
//    
//    }
}