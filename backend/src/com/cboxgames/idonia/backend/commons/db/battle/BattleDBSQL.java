package com.cboxgames.idonia.backend.commons.db.battle;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.mob.MobDBSQL;
import com.cboxgames.idonia.backend.commons.db.node.UserNodeDBSQL;
import com.cboxgames.idonia.backend.commons.db.playlist.UserPlaylistDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacteraccessory.UserCharacterAccessoryDBSQL;

import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.UserPlaylist;
import com.cboxgames.utils.idonia.types.Mob.MobWrapper;

public class BattleDBSQL extends DBSQL implements IBattleDB {
	
	private MobDBSQL mob_db_sql;

	public BattleDBSQL(DataSource connection_pool, ServletContext servlet_context)
			throws SQLException {
		super(connection_pool, servlet_context);
    	mob_db_sql = new MobDBSQL(getDataSource(), getServletContext());
	}
	
	@Override
	public MobWrapper[] getMobDetails() {
		return mob_db_sql.getMobDetails();
	}
	
	@Override
	public boolean updatePveUser(User user, Accessory acc_reward, boolean update_user_node) {
		
		// tokens, money, breadstick, bread_slice, bread_loaf
        Connection conn = null;
        boolean update = false;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            //Check if inventory is full
            if (acc_reward != null) {
                if (user.user_character_accessories_inventory == null ||
                        user.user_character_accessories_inventory.length <= user.inventory_spots) {
                    acc_reward.id = UserCharacterAccessoryDBSQL.createUserCharacterAccessory(acc_reward, 0, conn); // may return -1.
                    if (acc_reward.id == -1) {
                        try {
                            conn.rollback();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    }
                }
            }
            UserDBSQL.updateBattleUser(user, conn);
            if (update_user_node) {
                UserNodeDBSQL.updateUserNodes(user, conn);
            }
            UserCharacterDBSQL.UpdateBattleUserCharacters(user.user_characters, conn);
            conn.commit();
            update = true;
        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        
        return update;
	}
	
	@Override
	public boolean updatePvpUsers(User winner, User loser, UserPlaylist winner_playlist, UserPlaylist loser_playlist) {
		
		Connection conn = null;
        boolean update = false;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            UserDBSQL.updateBattleUser(winner, conn);
            UserDBSQL.updateBattleUser(loser, conn);
            UserPlaylistDBSQL.updateUserPlaylist(winner_playlist, conn);
            UserPlaylistDBSQL.updateUserPlaylist(loser_playlist, conn);
            UserCharacterDBSQL.UpdateBattleUserCharacters(winner.user_characters, conn);
            UserCharacterDBSQL.UpdateBattleUserCharacters(loser.user_characters, conn);
            conn.commit();
            update = true;
        } catch (SQLException e) {
        	rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
        	closeConnection(conn);
        }
        return update;
	}
    
    public boolean updateOfflinePvpUser(User user, UserPlaylist user_playlist) {
        Connection conn = null;
        boolean update = false;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            UserDBSQL.updateBattleOfflineUser(user, conn);
            UserPlaylistDBSQL.updateUserPlaylist(user_playlist, conn);
            UserCharacterDBSQL.UpdateBattleUserCharacters(user.user_characters, conn);
            conn.commit();
            update = true;
        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return update;
    }
}