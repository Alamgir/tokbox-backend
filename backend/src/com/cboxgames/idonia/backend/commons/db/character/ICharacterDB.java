package com.cboxgames.idonia.backend.commons.db.character;

import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Character.CharacterWrapper;

public interface ICharacterDB {
	
	/**
	 * Retrieve a list of all the characters.
	 * 
	 * @return
	 */
	public List<Character> getCharacters();
	
	/**
	 * Retrieve a list of characters given their IDs.
	 * 
	 * @param ids the list of characters to retrieve.
	 * 
	 * @return a list of characters, mapped to their IDs.
	 */
	public Map<Integer, Character> getCharacters(List<Integer> ids);

    /**
     * Retrieve a character by its character_id
     * @param id the id of the character
     * @return a corresponding character object
     */
    public Character getCharacterByID(int id);
    
    /**
	 * Retrieve a wrapper array of all the characters.
	 * 
	 * @return
	 */
    public CharacterWrapper[] getCharacterDetails();
}
