package com.cboxgames.idonia.backend.commons.db.userpotion;

import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog;
import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.skill.ISkillDB;
import com.cboxgames.utils.idonia.types.Potion;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserPotion;
import com.newrelic.agent.instrumentation.pointcuts.database.ConnectionClassTransformer;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 3/9/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserPotionDBSQL extends DBSQL implements IUserPotionDB {
    public UserPotionDBSQL(DataSource connection_pool, ServletContext servlet_context) throws SQLException {
        super(connection_pool, servlet_context);
    }
    
    public boolean createUserPotion (User user, Connection conn) {
        UserPotion up = new UserPotion();
        boolean updated = false;
        try {
            PreparedStatement create_up = conn.prepareStatement("INSERT INTO user_potions(user_id, potion_id, state) VALUES (?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            create_up.setInt(1, user.id);
            create_up.setInt(2, 120);
            create_up.setInt(3, 1);
            create_up.executeUpdate();

            ResultSet gen_keys = create_up.getGeneratedKeys();
            if (gen_keys.next()) {
                up.id = gen_keys.getInt(1);
            }
            up.potion_id = 120;
            up.bought_at = new Date(System.currentTimeMillis());
            up.state = 1;

            ArrayList<UserPotion> up_list = new ArrayList<UserPotion>();
            up_list.add(up);
            UserPotion[] up_array = up_list.toArray(new UserPotion[0]);
            user.user_potions = up_array;

            updated = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }
    
    public int buyUserPotion(User user, int potion_id) {
        Connection conn = null;
        int user_potion_id = 0;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            Potion lookup_potion = new Potion();
            PreparedStatement statement = conn.prepareStatement("SELECT id, cost, refresh_cost, premium, refresh_time FROM skills WHERE (id = ? AND skill_type = 1)");
            statement.setInt(1, potion_id);
            ResultSet result = statement.executeQuery();
            while (result != null && result.next()) {
                getPotionFromResult(result, lookup_potion);
            }

            if (lookup_potion.premium) {
                //if the potion costs only tokens, the cost will be 0, token cost != 0
                if (user.tokens >= lookup_potion.cost) {
                    //Subtract tokens from the user
                    deductCost(lookup_potion.cost, user, "tokens", conn);
                }
            }
            else if (user.money >= lookup_potion.cost) {
                //Subtract money from the user
                deductCost(lookup_potion.cost, user, "money", conn);
            }

            PreparedStatement create_up = conn.prepareStatement("INSERT INTO user_potions(user_id, potion_id, state) VALUES (?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            create_up.setInt(1, user.id);
            create_up.setInt(2, lookup_potion.id);
            create_up.setInt(3, 1);
            create_up.executeUpdate();

            ResultSet gen_keys = create_up.getGeneratedKeys();
            if (gen_keys.next()) {
                user_potion_id = gen_keys.getInt(1);
            }
            conn.commit();
            CBoxLoggerSyslog.log("user_potion_buy", "user_id", "potion_id", user.id, potion_id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection(conn);
            CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_up_buy",user.id);
        }
        finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return  user_potion_id;
    }

    public boolean useUserPotion(int user_potion_id) {
        Connection conn = null;
        boolean updated = false;
        try {
            conn = getConnection();
            PreparedStatement use_up = conn.prepareStatement("UPDATE user_potions SET bought_at = ?, state = 0 WHERE id = ?");
            use_up.setTimestamp(1, new Timestamp(System.currentTimeMillis()) );
            use_up.setInt(2, user_potion_id);
            use_up.executeUpdate();
            updated = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return updated;
    }
    
    public int refreshUserPotion(User user, int user_potion_id, boolean battle) {
        Connection conn = null;
        int return_refresh = 0;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            int potion_id = 0;
            for (UserPotion up : user.user_potions) {
                if (up.id == user_potion_id) {
                    potion_id = up.potion_id;
                }
            }

            Potion lookup_potion = new Potion();
            PreparedStatement statement = conn.prepareStatement("SELECT id, cost, refresh_cost, premium, refresh_time FROM skills WHERE (id = ? AND skill_type = 1)");
            statement.setInt(1, potion_id);
            ResultSet result = statement.executeQuery();
            while (result != null && result.next()) {
                getPotionFromResult(result, lookup_potion);
            }

            if (user.tokens >= lookup_potion.refresh_cost) {
                //Subtract tokens from the user
                int cost = lookup_potion.refresh_cost;
                if (battle) {
                    cost = (int)(cost*1.5f);
                }
                deductCost(cost, user, "tokens", conn);

                //set state to AVAILABLE in both cases
                PreparedStatement update_up = conn.prepareStatement("UPDATE user_potions SET state = 1  WHERE id = ?");
                update_up.setInt(1, user_potion_id);
                update_up.executeUpdate();
                return_refresh = lookup_potion.refresh_time;
            }
            conn.commit();
        }
        catch (SQLException e) {
            e.printStackTrace();
            CBoxLoggerSyslog.log("error","error_type","user_potion_id","unsuccessful_up_refresh",user_potion_id);
        }
        finally {
            setAutoCommitTrue(conn);
            closeConnection(conn);
        }
        return return_refresh;
    }
    
    public static UserPotion[] getUserPotions(int user_id, Connection conn) throws SQLException {
        List<UserPotion> user_potion_list = new ArrayList<UserPotion>();

        PreparedStatement get_potions = conn.prepareStatement("SELECT * FROM user_potions WHERE user_id = ?");
        get_potions.setInt(1, user_id);
        ResultSet up_results = get_potions.executeQuery();

        if (up_results != null) {
            while (up_results.next()) {
                UserPotion up = new UserPotion();
                getUserPotionsFromResult(up_results, up);
                checkUserPotionState(up, conn);
                user_potion_list.add(up);
            }
        }

        if (user_potion_list.isEmpty()) { return null; }

        return (UserPotion[]) user_potion_list.toArray(new UserPotion[0]);
    }

    private static void checkUserPotionState(UserPotion user_potion, Connection conn) {
        //Check the user_potion's timestamp, if it's currently in refresh mode and past the time, set the state to available and save it
        if (user_potion.state != UserPotion.REFRESHING)
            return;

        int time_limit = 0;
        try {
            PreparedStatement get_lookup_potion = conn.prepareStatement("SELECT refresh_time FROM skills WHERE id = ?");
            get_lookup_potion.setInt(1, user_potion.potion_id);
            ResultSet lookup_result = get_lookup_potion.executeQuery();
            if (lookup_result != null && lookup_result.next()) {
                time_limit = lookup_result.getInt("refresh_time");
            }

            Calendar now = Calendar.getInstance();
            Calendar potion_time = Calendar.getInstance();
            potion_time.setTime(user_potion.bought_at);
            potion_time.add(Calendar.SECOND, time_limit);

            //compute the time left
            long time_left = potion_time.getTimeInMillis()-now.getTimeInMillis();
            if (time_left<0) {
                time_left = 0;
            }
            user_potion.time_left = time_left;

            if (now.after(potion_time)) {
                user_potion.state = UserPotion.AVAILABLE;

                PreparedStatement change_state = conn.prepareStatement("UPDATE user_potions SET state = ? WHERE id = ?");
                change_state.setInt(1, UserPotion.AVAILABLE);
                change_state.setInt(2, user_potion.id);
                change_state.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getUserPotionsFromResult(ResultSet up_results, UserPotion up) throws SQLException {
        up.id = up_results.getInt("id");
        up.potion_id = up_results.getInt("potion_id");
        up.bought_at = up_results.getTimestamp("bought_at");
        up.state = up_results.getInt("state");
    }

    private static void getPotionFromResult(ResultSet result, Potion potion) throws SQLException {
        potion.id = result.getInt("id");
        potion.cost = result.getInt("cost");
        potion.refresh_cost = result.getInt("refresh_cost");
        potion.premium = result.getBoolean("premium");
        potion.refresh_time = result.getInt("refresh_time");
    }
    
    private static void deductCost(int cost, User user, String cost_type, Connection conn) throws SQLException {
        if (cost_type.equals("money")) {
            user.money -= cost;
            PreparedStatement deduct_money = conn.prepareStatement("UPDATE users SET money = ? WHERE id = ? ");
            deduct_money.setInt(1, user.money);
            deduct_money.setInt(2, user.id);
            deduct_money.executeUpdate();
        }
        else if (cost_type.equals("tokens")) {
            user.tokens -= cost;
            PreparedStatement deduct_money = conn.prepareStatement("UPDATE users SET tokens = ? WHERE id = ? ");
            deduct_money.setInt(1, user.tokens);
            deduct_money.setInt(2, user.id);
            deduct_money.executeUpdate();
        }
    }
        
}
