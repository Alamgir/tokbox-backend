package com.cboxgames.idonia.backend.commons.db.achievement;

import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Achievement;
import com.cboxgames.utils.idonia.types.Achievement.AchievementWrapper;

public interface IAchievementDB {
	
	/**
	 * Retrieve a list of all the achievements.
	 * 
	 * @return
	 */
	public List<Achievement> getAchievements();
	
	/**
	 * Retrieve a wrapper array of all the achievements.
	 * 
	 * @return
	 */
	public AchievementWrapper[] getAchievementDetails();
	
	/**
	 * Retrieve a list of achievements given their IDs.
	 * 
	 * @param ids the list of achievements to retrieve.
	 * 
	 * @return a list of achievements, mapped to their IDs.
	 */
	public Map<Integer, Achievement> getAchievements(List<Integer> ids);

    /**
     * Retrieve a achievement by its achievement_id
     * @param id the id of the achievement
     * @return a corresponding achievement object
     */
    public Achievement getAchievementByID(int id);
}