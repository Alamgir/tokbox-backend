package com.cboxgames.idonia.backend.commons;

public class Formulas {
	
	public static int getUserCharacterMaxExperience(int level) {
		//return (int) Math.round(10000.0 / (1.0 + (1.0 * Math.exp((10.0 - level) * 0.358))));
        return (int) Math.round(25000.0 / (1.0 + (2.5 * Math.exp((10.0 - level) * 0.358))));
	}
	
	public static double getBaseFormula(int user_level, int opponent_level) {
		return Math.abs( (1.5 - (user_level - opponent_level)) / 5.0);
	}
	
	public static int getUserCharacterAccessoryMaxExperience(int level) {
		 return (int) Math.round(getUserCharacterMaxExperience(level) / 3.0);
	}
}
