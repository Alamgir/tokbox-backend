package com.cboxgames.idonia.backend.commons.db.achievement;

import com.cboxgames.utils.idonia.types.UserAchievement;
import com.cboxgames.utils.idonia.types.UserAchievement.UserAchievementWrapper;

import java.sql.Connection;
import com.cboxgames.utils.idonia.types.User;

public interface IUserAchievementDB {
	
//	/**
//	 * Create user analytics for a new user.
//	 * Pre-condition: user with user_id must exist.
//	 *
//	 * @param user: the user.
//	 *
//	 * @return weather the operation was successful.
//	 */
//	public boolean createUserAchievements(User user, Connection conn);
//
//	/**
//	 * Create user analytics for a new user.
//	 * Pre-condition: user with user_id must exist.
//	 *
//	 * @param user_id the user's id.
//	 *
//	 * @return weather the operation was successful.
//	 */
//	public boolean createUserAchievements(int user_id);
//
//	/**
//	 * Get details of all user achievements.
//	 *
//	 * @param
//	 *
//	 */
//	public UserAchievementWrapper[] getUserAchievementDetails();
//
//	/**
//	 * Get details of a user achievement.
//	 * Pre-condition: user with user_id must exist.
//	 *
//	 * @param user_id the user's id.
//	 *
//	 */
//	public UserAchievementWrapper[] getUserAchievementDetails(int user_id);
//
//	/**
//	 * Check if a user_achievement of user_id exists
//	 * Pre-condition: user with user_id must exist.
//	 *
//	 * @param user_id the user's id.
//	 *
//	 * @return true or false.
//	 */
//	public boolean userAchievementExist(int user_id);
//
//	/**
//	 * Complete a user achievement.
//	 * Pre-condition: user_id & achievement_id must exist.
//	 *
//	 * @param user_id the user's id.
//	 * @param achievement_id the achievement's id
//	 *
//	 * @return weather the operation was successful.
//	 */
//	public boolean userAchievementComplete(int user_id, int achievement_id);
    
    public boolean userAchievementComplete(int user_id, UserAchievement achievement, int character_id, int amount);

//	/**
//	 * Delete all of a user's tutorials.
//	 *
//	 * @param user_id the user's id.
//	 *
//	 * @return weather the operation was successful.
//	 */
//	public boolean deleteUserAchievement(int user_id);
}
