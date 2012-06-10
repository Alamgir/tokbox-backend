package com.cboxgames.idonia.backend.commons.db.accessory;

import java.util.List;

import com.cboxgames.idonia.backend.commons.db.accessory.UserAccessoriesShopDBSQL.ResponseGetUserAccessories;
import com.cboxgames.utils.idonia.types.Accessory;


public interface IUserAccessoriesShopDB {

	/**
	 * Retrieve a user's list of accessories for the current 24 hour period.
	 * 
	 * @param user_id the user in question.
	 * 
	 * @return the list of accessories (may have a size of 0).
	 */
	public ResponseGetUserAccessories getShopAccessories(int user_id);
	
	/**
	 * Save (override) the given user's list of accessories for the current 24 hour period.
	 * 
	 * @param user_id the user in question.
	 * @param accessories the accessories to save.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean saveShopAccessories(int user_id, List<Accessory> accessories);
}
