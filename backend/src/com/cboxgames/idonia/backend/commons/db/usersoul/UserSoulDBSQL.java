package com.cboxgames.idonia.backend.commons.db.usersoul;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Soul;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserSoul;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 1/11/12
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserSoulDBSQL extends DBSQL implements IUserSoulDB{
	
    public UserSoulDBSQL (DataSource connection_pool, ServletContext servlet_context)
        throws SQLException {
        super(connection_pool, servlet_context);
    }
    
    @Override
    public boolean createUserSoul(User user, Connection conn) {

        boolean updated = false;
        try {
	        PreparedStatement create_us = conn.prepareStatement("INSERT INTO user_souls(user_id, soul_id, state)" +
	                                                                    "VALUES (?,?,?)");
	        create_us.setInt(1, user.id);
	        create_us.setInt(2, 0);
	        create_us.setInt(3, 0);
	        create_us.executeUpdate();
	        updated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return updated;
    }

    @Override
    public boolean depositUserSoul(User user, int soul_id) {
        Connection conn = null;
        boolean updated = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            Soul lookup_soul = new Soul();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM souls WHERE id = ?");
            statement.setInt(1, soul_id);
			ResultSet result = statement.executeQuery();
            if (result != null) {
                while (result.next()) {
                    getSoulFromResult(result, lookup_soul);
                }
            }

            if (user.souls >= lookup_soul.cost) {
            	user.souls -= lookup_soul.cost;
                PreparedStatement deduct_souls = conn.prepareStatement("UPDATE users SET souls = ? WHERE id = ?");
                deduct_souls.setInt(1, user.souls);
                deduct_souls.setInt(2, user.id);
                deduct_souls.executeUpdate();

                PreparedStatement update_user_soul = conn.prepareStatement("UPDATE user_souls SET state = ?, bought_at = ?, soul_id = ? WHERE user_id = ?");
                update_user_soul.setInt(1, UserSoul.COUNTDOWN);

                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                update_user_soul.setTimestamp(2, timestamp);

                update_user_soul.setInt(3, lookup_soul.id);
                update_user_soul.setInt(4, user.id);
                update_user_soul.executeUpdate();

                conn.commit();
                updated = true;
            }

        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return updated;
    }

    @Override
    public boolean collectUserSoul(int user_id, int user_soul_id) {
        Connection conn = null;
        boolean rc = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement get_user_soul = conn.prepareStatement("SELECT * FROM user_souls, souls WHERE user_souls.soul_id = souls.id " +
                                                                            "AND user_souls.id = ?");
            get_user_soul.setInt(1, user_soul_id);
            ResultSet us_result = get_user_soul.executeQuery();

            int payout = 0;
            if (us_result != null) {
                while (us_result.next()) {
                    payout = us_result.getInt("payout");
                }
            }

            PreparedStatement update_user = conn.prepareStatement("UPDATE users SET money = money + ? WHERE id = ?");
            update_user.setInt(1, payout);
            update_user.setInt(2, user_id);
            update_user.executeUpdate();

            PreparedStatement update_user_soul = conn.prepareStatement("UPDATE user_souls SET state = ? WHERE id = ?");
            update_user_soul.setInt(1, UserSoul.NONE);
            update_user_soul.setInt(2, user_soul_id);
            update_user_soul.executeUpdate();

            conn.commit();

            rc = true;
        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return rc;
    }

    // static methods
    public static UserSoul[] getUserSouls(int user_id, Connection conn) throws SQLException {
        List<UserSoul> user_soul_list = new ArrayList<UserSoul>();

        PreparedStatement get_souls = conn.prepareStatement("SELECT * FROM user_souls WHERE user_id = ?");
        get_souls.setInt(1, user_id);
        ResultSet us_results = get_souls.executeQuery();

        if (us_results != null) {
            while (us_results.next()) {
                UserSoul us = new UserSoul();
                getUserSoulsFromResult(us_results, us);
                checkUserSoulState(us, conn);
                user_soul_list.add(us);
            }
        }

        if (user_soul_list.isEmpty()) { return null; }

        return (UserSoul[]) user_soul_list.toArray(new UserSoul[0]);
    }

    private static void checkUserSoulState(UserSoul user_soul, Connection conn) {
        //Check the user_soul's timestamp, if it's currently in countdown and past collection, set the state to collectible and save it
        if (user_soul.state != UserSoul.COUNTDOWN)
        	return;
        
        int time_limit = 0;
        try {
            PreparedStatement get_lookup_soul = conn.prepareStatement("SELECT * FROM souls WHERE id = ?");
            get_lookup_soul.setInt(1, user_soul.soul_id);
            ResultSet lookup_result = get_lookup_soul.executeQuery();
            if (lookup_result != null && lookup_result.next()) {
                time_limit = lookup_result.getInt("time_limit");
            }

            Calendar now = Calendar.getInstance();
            Calendar soul_time = Calendar.getInstance();
            soul_time.setTime(user_soul.bought_at);
            soul_time.add(Calendar.SECOND, time_limit);
            if (now.after(soul_time)) {
                user_soul.state = UserSoul.COLLECTIBLE;

                PreparedStatement change_state = conn.prepareStatement("UPDATE user_souls SET state = ? WHERE id = ?");
                change_state.setInt(1, UserSoul.COLLECTIBLE);
                change_state.setInt(2, user_soul.id);
                change_state.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
	    }
    }

    private static void getUserSoulsFromResult(ResultSet us_results, UserSoul us) throws SQLException {
        us.id = us_results.getInt("id");
        us.soul_id = us_results.getInt("soul_id");
        us.bought_at = us_results.getTimestamp("bought_at");
        us.state = us_results.getInt("state");
    }

    private static void getSoulFromResult(ResultSet result, Soul soul) throws SQLException {
        soul.id = result.getInt("id");
		soul.name = result.getString("name");
        soul.cost = result.getInt("cost");
        soul.payout = result.getInt("payout");
        soul.time_limit = result.getInt("time_limit");
	}

}
