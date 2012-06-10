package com.cboxgames.idonia.backend.commons;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/8/11
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Constants {
    public static final String STR = "str";
    public static final String VIT = "vit";
    public static final String AGI = "agi";
    public static final String INTEL = "intel";
    public static final String WIL = "wil";

    public static final String ARMOR = "armor";
    public static final String DODGE = "dodge";
    public static final String PCRIT = "pcrit";
    public static final String SCRIT = "scrit";

    public static final String EARRINGS = "Earrings";
    public static final String RING = "Ring";
    public static final String NECKLACE = "Necklace";
    public static final String TALISMAN = "Talisman";
    public static final String ORB = "Orb";

    public static final String COMMON = "Common";
    public static final String UNIQUE = "Unique";
    public static final String EPIC = "Epic";
    public static final String RANDOM = "NONE";
    
	public static final int COMMON_LB = 5;
	public static final int RARE_LB = 10;
	public static final int EPIC_LB = 15;

    public static final String WARRIOR = "Warrior";
    public static final String HEALER = "Healer";
    public static final String ROGUE = "Rogue";
    public static final String ARCHER = "Archer";
    public static final String MAGE = "Mage";

    public static final Integer TOTAL_POINTS = 200;
    public static final Double POINTS_PER_LEVEL = 1.0;

    public static final String ACCESSORY = "Accessory";
    public static final String WEAPON = "Weapon";
    public static final String ACC_ARMOR = "Armor";
    public static final String HELMET = "Helmet";

    public static final int MAX_SESSION_INTERVAL_UNLIMITED = -1;
    public static final int MAX_SESSION_INTERVAL_ONE_DAY = 86400;
    public static final int GOLD_TOKEN_EXCHANGE_RATE = 150/1;

    public static final int INITIAL_MONEY = 1000;
    public static final int INITIAL_TOKENS = 10;
    public static final int INITIAL_INVENTORY_SPOTS = 9;
    
    public static final int HANDFUL_OF_GOODIES_TOKENS = 20;
    public static final int MOUTHFUL_OF_GOODIES_TOKENS = 105;
    public static final int SMALL_BAG_OF_GOODIES_TOKENS = 220;
    public static final int BOX_OF_GOODIES_TOKENS = 460;
    public static final int GIANT_SACK_OF_GOODIES_TOKENS = 1200;
    public static final int MASSIVE_SACK_OF_GOODIES_TOKENS = 3000;

    public static final int MAX_CHARACTER_SLOTS = 5;

    public static final int BREADSTICK_TURNS = 3;
    public static final double BREADSTICK_BOOST = 1.2;
    public static final int BREADSLICE_TURNS = 3;
    public static final double BREADSLICE_BOOST = 1.4;
    public static final int BREADLOAF_TURNS = 3;
    public static final double BREADLOAF_BOOST = 2.0;
    public static final int BTL_TURNS = 5;
    
    public static final int EXP_SCROLL_GAIN = 1000;

    public static final float EXP_POTION_BOOST = 0.65f;
    public static final float GOLD_POTION_BOOST = 0.65f;

    public static final int PVE_SURVIVAL_TOKEN_FACTOR = 10;

    public static final int MAX_LEVEL = 20;
    public static final int USER_CHARACTER_STATS_GAIN = 3;
    public static final int USER_CHARACTER_ACCESSORY_STATS_GAIN = 1;

    public static final int MAX_USER_CHARACTER = 5;
    public static final int GLADIATOR_ARENA_COST = 250;
    public static final int APPRENTICE_WINNER_MONEY = 200;
    public static final int APPRENTICE_LOSER_MONEY = 50;
    public static final int NORMALIZE_RATING = 10;
    public static final int CHANGE_WEAPON_MIN_LEVEL = 3;
    public static final int TIER1_TO_TIER2_WEAPON_UPGRADE_MIN_LEVEL = 10;
    public static final int TIER2_TO_TIER3_WEAPON_UPGRADE_MIN_LEVEL = 15;
    
    public static final int WINNER_SOULS_EARNED = 3;

    public static final String TRAINEE = "Trainee";       //                 rating <= TRAINEE_UB
    public static final String APPRENTICE = "Apprentice"; // TRAINEE_UB    < rating <= APPRENTICE_UB
    public static final String FIGHTER = "Fighter";       // APPRENTICE_UB < rating <= FIGHTER_UB
    public static final String GLADIATOR = "Gladiator";   // FIGHTER_UB    < rating <= GLADIATOR_UB
    public static final String KNIGHT = "Knight";         // GLADIATOR_UB  < rating <= KNIGHT_UB
    public static final String TEMPLAR = "Templar";       // KNIGHT_UB     < rating <= TEMPLAR_UB
    public static final String JUSTICAR = "Justicar";     // TEMPLAR_UB    < rating <= JUSTICAR_UB
    public static final String CHAMPION = "Champion";     // JUSTICAR_UB   < rating
    
    public static final int TRAINEE_UB = 1500; // UB: Upper bound
    public static final int APPRENTICE_UB = 1600;
    public static final int FIGHTER_UB = 1700;
    public static final int GLADIATOR_UB = 1800;
    public static final int KNIGHT_UB = 1900;
    public static final int TEMPLAR_UB = 2000;
    public static final int JUSTICAR_UB = 2100;

    public static final int WINNER_TOKENS = 2;
    public static final int LOSER_TOKENS = 0;
    
    public static final int INITIAL_RATING = 1500;
    public static final int MIN_RATING = 1500;
    public static final int MAX_RATING = 999999;
	public static final int DEFAULT_DELTA = 15;
    public static final int DEFAULT_OFFLINE_DELTA = 10;
	public static final int INCREMENTAL_DELTA = 3;
	public static final int STANDARD_RANGE = 20;
    
    public static final int OFFLINE_PVP_COUNT = 5;

    public static final String HFG = "HFG";
    public static final String MFG = "MFG";
    public static final String SBG = "SBG";
    public static final String BOG = "BOG";
    public static final String GSG = "GSG";
    public static final String MSG = "MSG";
    public static final String BRE = "BRE";
    public static final String BSL = "BSL";
    public static final String BLF = "BLF";
    public static final String BTL = "BTL";
    public static final String FRB = "FRB";
    public static final String WPN = "WPN";
    public static final String GOM = "GOM";
    public static final String AMN = "AMN";
    public static final String CHS = "CHS";
    public static final String WRT = "WRT";
    public static final String WSA = "WSA";
    public static final String SCK = "SCK";
    public static final String NRP = "NRP";
    public static final String EXP = "EXP";
    public static final String NAD = "NAD";
    
    // leaderboard constants
    public static final int TOP_LIST_LIMIT = 100;
    public static final int UP_NEIGHBOR_LIMIT = 50;
    public static final int DOWN_NEIGHBOR_LIMIT = 50;

    // HTTP response Status Code (SC)
    public static final int SC_OK = 200;
    
    public static final int SC_BAD_REQUEST = 400;
    public static final int SC_UNAUTHORIZED = 401;
    public static final int SC_FORBIDDEN = 403;
    public static final int SC_NOT_FOUND = 404;
	public static final int SC_CONFLICT = 409;
    public static final int SC_EXPECTATION_FAILED = 417;
    
    public static final int SC_INTERNAL_SERVER_ERROR = 500;
	
}
