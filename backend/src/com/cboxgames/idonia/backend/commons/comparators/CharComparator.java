package com.cboxgames.idonia.backend.commons.comparators;

import com.cboxgames.utils.idonia.types.Character;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/19/11
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CharComparator implements Comparator<Character.CharacterWrapper> {
    //Sort User_Characters by Level
    public int compare(Character.CharacterWrapper char1, Character.CharacterWrapper char2) {
        int char1level = ((Character.CharacterWrapper)char1).character.level;
        int char2level = ((Character.CharacterWrapper)char2).character.level;

        if (char1level > char2level) {
            return 1;
        }
        else if (char1level < char2level) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
