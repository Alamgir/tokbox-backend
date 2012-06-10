package com.cboxgames.idonia.backend.commons;

public class ResponseMessages {
	
	public static final String HELLO = "Hello world";
	public static final String NOT_ENOUGH_TOKENS = "You currently do not have enough tokens for this purchase.";
	public static final String INSUFFICIENT_MONEY = "Sorry, you don't have enough money to buy this skill";
	public static final String SKILL_EXISTS = "Sorry, you cannot buy a skill you already have";
	public static final String SKILL_DOES_NOT_BELONG_TO_CHARACTER = "Sorry, this skill does not belong to the character";
	public static final String SKILLS_MUST_BELONG_TO_CHARACTER = "Sorry, you can only swap skills that belong to the same character and user";
	public static final String STATS_CHEATING = "Nice try, buddy but no cheating is allowed";
	public static final String ACCESSORY_MUST_BELONG_TO_CHARACTER = "Sorry, the weapon, armor or helmet must belong to the character class";
	public static final String ACCESSORY_NOT_BELONG_TO_CHARACTER = "Sorry, the weapon, armor or helmet doesn't belong to the character";
	public static final String ALREADY_OWN_EQUIPMENT = "Sorry, you already own the equipment";
	public static final String NOT_A_WEAPON = "Sorry, you can only change or upgrade a weapon";
	public static final String WEAPON_CHARACTER_MISMATCH = "Sorry, this weapon isn't meant for the character you are trying to swap to";
	public static final String INSUFFICIENT_MONEY_TO_UPGRADE_WEAPON = "Sorry, you don't have enough money to upgrade the weapon";
	public static final String INSUFFICIENT_TOKENS_TO_CHANGE_WEAPON = "Sorry, you don't have enough tokens to change the weapon";
	public static final String MUST_BE_ACCESSORY = "Sorry, you can only swap accessories, not weapons or armors";
	public static final String ACCESSORY_NOT_INVENTORY = "This accessory is currently equipped, you cannot sell it";
	public static final String INSUFFICIENT_TOKENS = "Sorry, you don't have enough tokens for this transaction";
	public static final String OTHER_BREAD = "Sorry, you can't purchase a different type of bread at this time. Please use up your current stock.";
	public static final String EXCEEDS_MAX_USER_CHARACTER = "Sorry, you cannot have more than 5 characters";
	public static final String CHARACTER_SLOT_EXISTS = "You have already purchased a character slot, please use it first";
	public static final String NEEDS_CHARACTER_SLOT = "You need to first purchase a character slot to add a new character";
	public static final String INSUFFICIENT_MONEY_TO_BUY_ACCESSORY = "Sorry, you don't have enough money to buy the accessory";
	public static final String ACCESSORY_INVENTORY_FULL = "Sorry, you cannot buy another accessory at this time, your inventory is full";
	public static final String INSUFFICIENT_TOKENS_GLADIATOR_ARENA = "Sorry, you don't have enough tokens to play in the gladiator arena";
	public static final String ACCESSORY_EXCEEDS_CHARACTER_LEVEL = "Sorry, you cannot use an accessory higher than your character's level";
	public static final String REQUIRES_MINIMUM_LEVEL_3 = "Sorry, your character must be at least at level 3 or above to use this feature";
	public static final String TIER1_to_TIER2_WEAPON_UPGRADE_MIN_LEVEL = "Sorry, your character must be at least at level 10 to upgrade to a tier 2 weapon";
	public static final String TIER2_to_TIER3_WEAPON_UPGRADE_MIN_LEVEL = "Sorry, your character must be at least at level 15 to upgrade to a tier 3 weapon";
	public static final String CHARACTER_ALREADY_IN_LINEUP = "This character is already in your lineup. How are you even doing this?";
	public static final String LOCKED_BANNER = "You need to get the required achievements before you can buy or swap this banner";
	public static final String BANNER_ALREADY_ACQUIRED = "You already have this banner";
	public static final String BANNER_INSUFFICIENT_MONEY = "Sorry, you don't have enough money to buy this banner";
	public static final String BANNER_NOT_ACQUIRED = "Sorry, one of the banners you are trying to get hasn't been bought yet";
	public static final String ALREADY_COMPLETED_BOOTY = "You have already completed today's quest for Booty. Try again tomorrow!";
	public static final String MISS_CHARACTER_IDS = "You did't send characters ids";
	public static final String UNVERIFIED_APPLE_RECEIPTT = "This purchase has not been verified by Apple. We don't condone cheating!!";
	public static final String USE_BREAD_FIRST = "You already have bread. Use it first.";
	public static final String USE_CHAR_SLOT_FIRST = "You already have a character slot. Use it first";
	public static final String MORE_TAHN_5_CHARS = "You can't have more than 5 characters";
	public static final String INCORRECT_PASSWORD = "Incorrect password!";
	public static final String INCORRECT_STATS = "Where did you get these stat points? They weren't here when I last looked...";
	
	public static final String USER_WITH_ID_NOT_FOUND = "Bad user id = %d";
	public static final String USER_WITH_ID_HAS_NO_CHAR = "User with id = %d has no character";
	public static final String PLAYLIST_WITH_ID_NOT_FOUND = "Bad playlist id = %d";
	public static final String UNKNOWN_PVE_TYPE = "Unknown PVE type = %s";
	public static final String BAD_USER_CHAR_ID = "Bad user character id = %d";
}
