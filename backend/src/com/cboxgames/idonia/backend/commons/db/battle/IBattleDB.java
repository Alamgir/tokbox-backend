package com.cboxgames.idonia.backend.commons.db.battle;

import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserPlaylist;
import com.cboxgames.utils.idonia.types.Mob.MobWrapper;

public interface IBattleDB {
	
	/**
	 * Get a wrapper array of all mobs.
	 * 
	 * Include skills and skill_effects.
	 * 
	 * @return
	 */
	public MobWrapper[] getMobDetails();
	
	/**
	 * Save user modified fields to the users table.
	 * 
	 * @return
	 */
	public boolean updatePveUser(User user, Accessory acc, boolean update_user_node);
	
	/**
	 * Save user modified fields to the users table for both winner and loser.
	 * 
	 * @return
	 */
	public boolean updatePvpUsers(User winner, User loser, UserPlaylist winner_playlist, UserPlaylist loser_playlist);
}
