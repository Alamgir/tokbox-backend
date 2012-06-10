package com.cboxgames.idonia.backend.commons.db.tutorial;

import java.sql.Connection;
import java.sql.SQLException;

import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserTutorial;
import com.cboxgames.utils.idonia.types.UserTutorial.UserTutorialWrapper;

public interface IUserTutorialDB {
	
	/**
	 * Create a user_tutorial entry for a new user.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean createUserTutorials(User user, Connection conn);
	
	/**
	 * Create a user_tutorial entry for a new user.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean createUserTutorials(int user_id);
	
	/**
	 * Get a user_tutorial entry for a given user_id
	 * Pre-condition: user with user_id  and user_tutorial_data must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public UserTutorial[] getUserTutorialArray(int user_id, byte[] user_tutorials_data) ;
	
	/**
	 * Check if a user_tutorial entry of user_id exists 
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return true or false.
	 */
	public boolean userTutorialExist(int user_id);
	
	/**
	 * Get details of all user tutorials.
	 * 
	 * @param 
	 * 
	 */
	public UserTutorialWrapper[] getUserTutorialDetails();
	
	/**
	 * Get details of a user tutorial.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 */
	public UserTutorialWrapper[] getUserTutorialDetails(int user_id);
	
	/**
	 * Get details of a user tutorial.
	 * Pre-condition: user with user_id and connection must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 */
	public UserTutorial[] getUserTutorialDetailsByID(int user_id, Connection conn) throws SQLException;
	
	/**
	 * Update the tutorial with tutorial_id as 'complete'
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean userTutorialComplete(int user_id, int tutorial_id);
	
	/**
	 * Delete a user_tutorial entry for a new user with user_id.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean deleteUserTutorial(int user_id);
}
