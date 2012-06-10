package com.cboxgames.idonia.backend.commons.db.node;

import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserNode;
import com.cboxgames.utils.idonia.types.UserNode.UserNodeWrapper;

import java.sql.Connection;

public interface IUserNodeDB {
	
	/**
	 * Create user nodes for a new user.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean createUserNodes(int user_id);
	
	/**
	 * Check if a user_node of user_id exists 
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return true or false.
	 */
	public boolean userNodeExist(int user_id);
	
	/**
	 * Get details of all user nodes.
	 * 
	 * @param 
	 * 
	 */
	public UserNodeWrapper[] getUserNodeDetails();
	
	/**
	 * Get details of a user node.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 */
	public UserNodeWrapper[] getUserNodeDetails(int user_id);
	
	/**
	 * Update the node with node_id as 'complete'
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean userNodeComplete(int user_id, int node_id);

    /**
     * Save an Array of UserNodes
     * @param user_nodes
     * @param user_id
     * @return
     */
    public boolean saveUserNodes(UserNodeWrapper[] user_nodes, int user_id);
	
	/**
	 * Delete a user_node entry for a new user with user_id.
	 * Pre-condition: user with user_id must exist.
	 * 
	 * @param user_id the user's id.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean deleteUserNode(int user_id);
}
