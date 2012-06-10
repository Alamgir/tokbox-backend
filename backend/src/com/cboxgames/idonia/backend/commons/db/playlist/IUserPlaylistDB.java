package com.cboxgames.idonia.backend.commons.db.playlist;

import java.sql.Connection;

import com.cboxgames.utils.idonia.types.UserPlaylist.UserPlaylistWrapper;
import com.cboxgames.utils.idonia.types.Leaderboard;
import com.cboxgames.utils.idonia.types.User;

public interface IUserPlaylistDB {
	
	/**
	 * Create user playlists for a new user.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean createUserPlaylists(int user_id);
	
	/**
	 * Create user playlists
	 * @param user
	 * @param conn
	 * @return
	 */
	public boolean createUserPlaylists(User user, Connection conn);
	
	/**
	 * Check if a user_playlist of user_id exists 
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return true or false.
	 */
	public boolean userPlaylistExist(int user_id);
	
	/**
	 * Get details of all user playlists.
	 * 
	 * @param 
	 * 
	 */
	public UserPlaylistWrapper[] getUserPlaylistDetails();
	
	/**
	 * Get details of a user playlist.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 */
	public UserPlaylistWrapper[] getUserPlaylistDetails(int user_id);
	
	/**
	 * Get leaderborad with the new leaderboard class.
	 * Pre-condition: user with user_id and playlist_id must exist.
	 * 
	 * @param user_id the user's id. playlist_id: the playlist's id
	 * 
	 */
	public Leaderboard getLeaderboardNew(int user_id, int playlist_id);
	
	/**
	 * Delete a user_playlist entry for a new user with user_id.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean deleteUserPlaylist(int user_id);
}
