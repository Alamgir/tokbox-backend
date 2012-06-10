package com.cboxgames.idonia.backend.commons.db.versions;

import java.util.List;

import com.cboxgames.utils.idonia.types.Version.VersionWrapper;

public interface IVersionDB {

	/**
	 * Get the list of current versions.
	 * @return
	 */
	public List<VersionWrapper> getCurrentVersions();
	
	/**
	 * Increment the version number for a specific data set.
	 * 
	 * @param version the version type to increment.
	 */
	public void incrementVersion(VersionType version);
	
	public enum VersionType {
		Accessories(8),
		AccessorySkills(4),
		Banners(7),
		Characters(9),
		Mobs(1),
		Nodes(5),
		Playlists(6),
		Purchases(3),
		Skills(2),
		Version(10);
		
		private final int id;
		
		VersionType(int id) {
			this.id = id;
		}
		
		public int getValue() { return id; }
	}
}
