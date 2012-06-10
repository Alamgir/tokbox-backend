/**
 * 
 */
package com.cboxgames.idonia.backend.commons.db.versions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Version;
import com.cboxgames.utils.idonia.types.Version.VersionWrapper;

/**
 * @author Irwin
 *
 */
public class VersionDBSQL extends DBSQL implements IVersionDB {

	public VersionDBSQL(DataSource connection_pool,
			ServletContext servlet_context) throws SQLException {
		super(connection_pool, servlet_context);
	}

	/* (non-Javadoc)
	 * @see com.cboxgames.idonia.backend.commons.db.versions.IVersionDB#getCurrentVersion()
	 */
	@Override
	public List<VersionWrapper> getCurrentVersions() {
		List<VersionWrapper> versions = new ArrayList<VersionWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM versions");
			ResultSet result = statement.executeQuery();
			if (result != null) {
				while (result.next()) {
					VersionWrapper vw = new VersionWrapper();
					Version v = vw.version;
					v.id = result.getInt("id");
					v.version = result.getInt("version");
					v.call_name = result.getString("call_name");
					v.description = result.getString("description");
					versions.add(vw);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return versions;
	}

	/* (non-Javadoc)
	 * @see com.cboxgames.idonia.backend.commons.db.versions.IVersionDB#incrementVersion(
	 * 		com.cboxgames.idonia.backend.commons.db.versions.IVersionDB.VersionType)
	 */
	@Override
	public void incrementVersion(VersionType version) {
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("UPDATE versions SET version = version + 1 WHERE id = ? ");
			statement.setInt(1, version.getValue());
			
			statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		closeConnection(conn);
	}
}
