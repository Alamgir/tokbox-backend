package com.cboxgames.idonia.backend.commons.db.playlist;

import java.sql.Connection;
import java.util.List;
import com.cboxgames.utils.idonia.types.Playlist.PlaylistWrapper;

import com.cboxgames.utils.idonia.types.Playlist;

public interface IPlaylistDB {

	/**
	 * Retrieve all playlists.
	 * 
	 * @return
	 */
	public List<Playlist> getPlaylists(Connection conn);
	
	/**
	 * Get a playlist by its id.
	 * 
	 * @param playlist_id
	 * @return
	 */
	public Playlist getPlaylist(int playlist_id);
	
	/**
	 * Get a playlist by its name.
	 * 
	 * @param name
	 * @return
	 */
	public Playlist getPlaylist(String name);
	
	/**
	 * Get all playlists.
	 *
	 * @return
	 */
	public PlaylistWrapper[] getPlaylistDetails();
}
