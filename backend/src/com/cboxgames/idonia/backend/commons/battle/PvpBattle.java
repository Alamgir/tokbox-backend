package com.cboxgames.idonia.backend.commons.battle;

import java.util.List;

import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog;
import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseMessages;
import com.cboxgames.idonia.backend.commons.Formulas;
import com.cboxgames.idonia.backend.commons.db.battle.BattleDBSQL;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.Playlist;
import com.cboxgames.utils.idonia.types.UserPlaylist;
import com.cboxgames.utils.json.messages.BattleOver;

public class PvpBattle {

	public static String battlePVP(User winner, User loser, Playlist playlist, int battle_end_state, int loser_souls_earned,
			BattleDBSQL battle_db_sql, boolean gold, boolean experience) {
		
		if (playlist.name.equals(Constants.TRAINEE)) {
			updatePvpExperienceAndMoneyReward(winner, loser, battle_end_state, loser_souls_earned, gold, experience);
		}
		else if (playlist.name.equals(Constants.APPRENTICE)) {
			winner.money += Constants.APPRENTICE_WINNER_MONEY;
			winner.souls += Constants.WINNER_SOULS_EARNED;
            CBoxLoggerSyslog.log("reward", "gold_rewarded", Constants.APPRENTICE_WINNER_MONEY);
			if (battle_end_state == BattleOver.BATTLE_END_STATE_NORMAL) {
				loser.money += Constants.APPRENTICE_LOSER_MONEY;
				loser.souls += loser_souls_earned;
                CBoxLoggerSyslog.log("reward", "gold_rewarded", Constants.APPRENTICE_LOSER_MONEY);
			}
        }
		else {
			assert (playlist.name.equals(Constants.GLADIATOR) == true) : "Unknown playlist name = " + playlist.name +
				" is encountered";
			if ((winner.money < Constants.GLADIATOR_ARENA_COST) || (loser.money < Constants.GLADIATOR_ARENA_COST)) {
				// return I18n.t : insufficient_tokens_gladiator_arena
				return ResponseMessages.INSUFFICIENT_TOKENS_GLADIATOR_ARENA;
			}
			
			winner.tokens += Constants.WINNER_TOKENS;
			winner.money -= Constants.GLADIATOR_ARENA_COST;
			winner.souls += Constants.WINNER_SOULS_EARNED;
			
			loser.tokens += Constants.LOSER_TOKENS;
			loser.money -= Constants.GLADIATOR_ARENA_COST;
			loser.souls += loser_souls_earned;
		}
		
		UserPlaylist winner_playlist = findUserPlaylistByID(winner, playlist.id); // first user_playlist for winner
		UserPlaylist loser_playlist = findUserPlaylistByID(loser, playlist.id); // first user_playlist for loser
		
		assert (winner_playlist != null) && (loser_playlist != null) :
			"Unable to find user_playlist with id = " + playlist.id + ". (battlePVP())";
		
	    updateUserPlaylistRatings(winner, loser, winner_playlist, loser_playlist, battle_end_state);
	    
	    battle_db_sql.updatePvpUsers(winner, loser, winner_playlist, loser_playlist);
	    
	    return null;
	}
    
    public static String offlineBattlePvp(User user, boolean win, int battle_end_state, Playlist playlist, BattleDBSQL battle_db_sql, boolean gold, boolean experience) {
        if (playlist.name.equals(Constants.TRAINEE)) {
            List<Character> winner_user_char_list = PveBattle.findUserCharacterNotDead(user, new Integer[0]);
            int winner_highest_level = PveBattle.getUserCharacterMaximumLevel(user);

            double factor = PveBattle.applyUserBread(user);
            for (Character user_char : winner_user_char_list) {
                if (user_char.level >= Constants.MAX_LEVEL) {
                    //increase the max_level for the user if the user_character's ending level is higher
                    if (user_char.level > user.max_level  && user_char.in_lineup) user.max_level = user_char.level;
                    continue; // when the character level is >= MAX_LEVEL, not experience gained. (1/24/2012)
                }
                // Formula changed to use user character level instead of user_highest_level
                // Leave the user_highest_level param in case it is needed in the future
                //double experience_gained = (user_char.max_experience / ((3.0/4.0) * (user_char.level + 6.0))) *
                        //Formulas.getBaseFormula(winner_highest_level, winner_highest_level) * factor;
                double experience_gained = (0.5)*(((360.00 * ((winner_highest_level / 7.00) + 0.81)) + 50) * factor);
                if (experience_gained < 0)
                    experience_gained = 0;

                if (experience && battle_end_state == 0) {
                    experience_gained += Math.round(user_char.experience*Constants.EXP_POTION_BOOST);
                }
                user_char.experience += Math.round(experience_gained);
                PveBattle.levelUpUserCharacter(user_char, user);
            }

            user.souls++;

//            double money = Math.sqrt(winner_highest_level *1000) * Formulas.getBaseFormula(winner_highest_level, winner_highest_level);
//            if (gold && battle_end_state == 0) {
//                money += money*Constants.GOLD_POTION_BOOST;
//            }
//            user.money += (int) money;
//            CBoxLoggerSyslog.log("reward", "gold_rewarded",(int)money);
        }

        UserPlaylist winner_playlist = findUserPlaylistByID(user, playlist.id); // first user_playlist for winner
        if (battle_end_state == 0) {
            if (win) {
                winner_playlist.rating += Constants.DEFAULT_OFFLINE_DELTA;
                if (winner_playlist.rating > Constants.MAX_RATING)
                    winner_playlist.rating = Constants.MAX_RATING;
                winner_playlist.wins++;
            }
        }
        else if (battle_end_state == 1) {
            winner_playlist.rating -= 5;
            if (winner_playlist.rating < Constants.MIN_RATING)
                winner_playlist.rating = Constants.MIN_RATING;
        }

        battle_db_sql.updateOfflinePvpUser(user, winner_playlist);

        return null;
    }
	
	private static void updatePvpExperienceAndMoneyReward(User winner, User loser, int battle_end_state, int loser_souls_earned, boolean gold, boolean experience) {
		
		List<Character> winner_user_char_list = PveBattle.findUserCharacterNotDead(winner, new Integer[0]);
		int winner_highest_level = PveBattle.getUserCharacterMaximumLevel(winner);
		List<Character> loser_user_char_list = PveBattle.findUserCharacterNotDead(loser, new Integer[0]);
		int loser_highest_level = PveBattle.getUserCharacterMaximumLevel(loser);
		
	    update_PVP_experience(winner, winner_user_char_list, winner_highest_level, loser_highest_level, true, experience);
	    if (battle_end_state == BattleOver.BATTLE_END_STATE_NORMAL)
	    	update_PVP_experience(loser, loser_user_char_list, loser_highest_level, winner_highest_level, false, experience);
	    winner.souls += Constants.WINNER_SOULS_EARNED;
	    moneyReward(winner, winner_highest_level, loser_highest_level, true, gold);
	    if (battle_end_state == BattleOver.BATTLE_END_STATE_NORMAL) {
	    	moneyReward(loser, loser_highest_level, winner_highest_level, false, gold);
	    	loser.souls += loser_souls_earned;
	    }
	}
	
	private static void update_PVP_experience(User user, List<Character> user_char_list, int user_highest_level,
											  int opponent_highest_level, boolean is_winner, boolean experience_boost) {
		double factor = PveBattle.applyUserBread(user);
		for (Character user_char : user_char_list) {
			if (user_char.level >= Constants.MAX_LEVEL) {
                //increase the max_level for the user if the user_character's ending level is higher
                if (user_char.level > user.max_level && user_char.in_lineup) user.max_level = user_char.level;
                continue; // when the character level is >= MAX_LEVEL, not experience gained. (1/24/2012)
            }
		    // Formula changed to use user character level instead of user_highest_level
		    // Leave the user_highest_level param in case it is needed in the future
			//double experience_gained = (user_char.max_experience / ((3.0/4.0) * (user_char.level + 6.0))) *
					//Formulas.getBaseFormula(user_highest_level, opponent_highest_level) * factor;
            int experience_gained = (int) (((360.00 * ((user.max_level / 7.00) + 0.81)) + 50) * factor);

            if (experience_gained < 0)
				experience_gained = 0;
            
            if (is_winner && experience_boost) {
                experience_gained += Math.round(user_char.experience*Constants.EXP_POTION_BOOST);
            }
            
			// Formula changed to only give loser 1/3 of experience gained instead of 1/2
			if (!is_winner)
				experience_gained /= 3;
			user_char.experience += Math.round(experience_gained);
			PveBattle.levelUpUserCharacter(user_char, user);
		}
	}

	private static void moneyReward(User user, int user_highest_level, int opponent_highest_level, boolean is_winner, boolean gold) {
	    //double money = Math.sqrt(user_highest_level *1000) * Formulas.getBaseFormula(user_highest_level, opponent_highest_level);
	    int money = 50 + (user_highest_level*11);
        if (is_winner && gold) {
            money += money*Constants.GOLD_POTION_BOOST;
        }
        
        if (!is_winner)
            money /= 2;
	    	//money /= 3;

	    user.money += money;
        CBoxLoggerSyslog.log("reward", "gold_rewarded",money);
	}
	
	private static void updateUserPlaylistRatings(User winner, User loser, UserPlaylist winner_playlist, 
			UserPlaylist loser_playlist, int battle_end_state) {
		
		//int diff = Math.abs(winner_playlist.rating - loser_playlist.rating);
		int delta = Constants.DEFAULT_DELTA;
//		if (diff > 20) {
//			delta = (diff / Constants.STANDARD_RANGE) * Constants.INCREMENTAL_DELTA;
////			if (winner_playlist.rating > loser_playlist.rating) {
////				delta = (diff > 10000) ? 0 : (Constants.DEFAULT_DELTA - delta);
////			}
////			else {
//				delta += Constants.DEFAULT_DELTA;
//				if (delta > 30)
//					delta = 30;
////			}
//		}
		delta = Math.abs(delta);
		int forfeit = (battle_end_state == BattleOver.BATTLE_END_STATE_FORFEIT) ? 1 : 0;
		int disconnect = (battle_end_state == BattleOver.BATTLE_END_STATE_DISCONNECT) ? 1 : 0;
		winner_playlist.rating += delta;
		if (winner_playlist.rating > Constants.MAX_RATING)
			winner_playlist.rating = Constants.MAX_RATING;
		winner_playlist.wins++;

        if (forfeit == 1) delta = -5;
        else delta = delta/3;
		loser_playlist.rating += delta;

//		if (loser_playlist.rating < Constants.MIN_RATING)
//			loser_playlist.rating = Constants.MIN_RATING;
        if (loser_playlist.rating > Constants.MAX_RATING) loser_playlist.rating = Constants.MAX_RATING;
        else if (loser_playlist.rating < Constants.MIN_RATING) loser_playlist.rating = Constants.MIN_RATING;
		loser_playlist.losses++;
		loser_playlist.forfeit_count += forfeit;
		loser_playlist.disconnect_count += disconnect;
	}
	
	private static UserPlaylist findUserPlaylistByID(User user, int playlist_id) {
		
		for (UserPlaylist upl : user.user_playlists) {
			if (upl.playlist_id == playlist_id)
				return upl;
		}
		
		return null;
	}
	
	public static class PvpManager {
		
		private User _winner;
		private User _loser;
		private UserPlaylist _winner_playlist;
		private UserPlaylist _loser_playlist;
		
		public PvpManager(User winner, User loser) {
			_winner = winner;
			_loser = loser;
		}
		
		public void set_winner_playlist(UserPlaylist up) { _winner_playlist = up; }
		public void set_loser_playlist(UserPlaylist up) { _loser_playlist = up; }
		
		public User get_winner() { return _winner; }
		public User get_loser() { return _loser; }
		public UserPlaylist get_winner_playlist() { return _winner_playlist; }
		public UserPlaylist get_loser_playlist() { return _loser_playlist; }
	}
}
