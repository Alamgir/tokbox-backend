package com.cboxgames.idonia.backend.commons.db.playlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Playlist;
import com.cboxgames.utils.idonia.types.Playlist.PlaylistWrapper;

public class PlaylistDBSQL extends DBSQL implements IPlaylistDB {

	public PlaylistDBSQL(DataSource connection_pool,
			ServletContext servlet_context) throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public List<Playlist> getPlaylists(Connection conn) {
		
		List<Playlist> playlists = new ArrayList<Playlist>();
		
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM playlists");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Playlist p = new Playlist();
				getPlaylistFromResult(result, p);
				playlists.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
        }

		return playlists;
	}
	
	@Override
	public PlaylistWrapper[] getPlaylistDetails() {
		
		List<PlaylistWrapper> pw_list = new ArrayList<PlaylistWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM playlists");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				PlaylistWrapper pw = new PlaylistWrapper();
				getPlaylistFromResult(result, pw.playlist);
				pw_list.add(pw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (PlaylistWrapper[]) pw_list.toArray(new PlaylistWrapper[0]);
	}
	
	@Override
	public Playlist getPlaylist(int playlist_id) {
		
		Playlist p = new Playlist();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM playlists WHERE id = ?");
			statement.setInt(1, playlist_id);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				getPlaylistFromResult(result, p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return p;
	}

	@Override
	public Playlist getPlaylist(String name) {
		
		Playlist p = new Playlist();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM playlists WHERE name = ?");
			statement.setString(1, name);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				getPlaylistFromResult(result, p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return p;
	}

	private static void getPlaylistFromResult(ResultSet result, Playlist p) throws SQLException {
		
		p.id = result.getInt("id");
		p.name = result.getString("name");
		p.description = result.getString("description");
		p.initial = result.getBoolean("initial");
		p.created_at = result.getString("created_at");
		p.updated_at = result.getString("updated_at");
	}
}
