
package com.cboxgames.idonia.backend.commons.db.tutorial;

import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Tutorial;
import com.cboxgames.utils.idonia.types.Tutorial.TutorialWrapper;

public interface ITutorialDB {
	
	/**
	 * Retrieve a list of all the tutorials.
	 * 
	 * @return
	 */
	public List<Tutorial> getTutorials();
	
	/**
	 * Retrieve an wrapper array of all the tutorials.
	 * 
	 * @return
	 */
	public TutorialWrapper[] getTutorialDetails();
	
	/**
	 * Retrieve a list of tutorials given their IDs.
	 * 
	 * @param ids the list of tutorials to retrieve.
	 * 
	 * @return a list of tutorials, mapped to their IDs.
	 */
	public Map<Integer, Tutorial> getTutorials(List<Integer> ids);

    /**
     * Retrieve a tutorial by its tutorial_id
     * @param id the id of the tutorial
     * @return a corresponding tutorial object
     */
    public Tutorial getTutorialByID(int id);
}