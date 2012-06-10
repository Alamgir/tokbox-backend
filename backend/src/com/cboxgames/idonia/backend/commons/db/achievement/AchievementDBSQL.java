package com.cboxgames.idonia.backend.commons.db.achievement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Achievement;
import com.cboxgames.utils.idonia.types.Achievement.AchievementWrapper;

public class AchievementDBSQL extends DBSQL implements IAchievementDB {

	public AchievementDBSQL(DataSource connection_pool,
			ServletContext servlet_context) throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public List<Achievement> getAchievements() {
		List<Achievement> achievements = new ArrayList<Achievement>();
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM achievements");
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Achievement ach = new Achievement();
				getAchievementFromResult(result, ach);
				achievements.add(ach);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return achievements;
	}
	
	@Override
	public AchievementWrapper[] getAchievementDetails() {
		
		List<AchievementWrapper> pw_list = new ArrayList<AchievementWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM achievements");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				AchievementWrapper aw = new AchievementWrapper();
				getAchievementFromResult(result, aw.achievement);
				pw_list.add(aw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (AchievementWrapper[]) pw_list.toArray(new AchievementWrapper[0]);
	}

	@Override
	public Map<Integer, Achievement> getAchievements(List<Integer> ids) {
		Map<Integer, Achievement> map = new HashMap<Integer, Achievement>();
		
		if (ids == null || ids.size() <= 0)
			return map;
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			StringBuilder query = new StringBuilder();
			query.append("SELECT * FROM achievements WHERE id=");
			query.append(ids.remove(0));
			for (Integer id : ids) {
				query.append(" OR id=");
				query.append(id);
			}
			
			PreparedStatement statement = conn.prepareStatement(query.toString());
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Achievement ach = new Achievement();
				getAchievementFromResult(result, ach);
				map.put(ach.id, ach);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return map;
	}

    @Override
    public Achievement getAchievementByID(int id) {
    	
        Connection conn = null;
        Achievement achievement = new Achievement();
        try{
            conn = getConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM achievements WHERE id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if ((result != null) && result.next()) {
                getAchievementFromResult(result, achievement);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return achievement;
    }

    private void getAchievementFromResult(ResultSet result, Achievement ach) throws SQLException {
    		
		ach.id = result.getInt("id");
		ach.name = result.getString("name");
		ach.description = result.getString("description");
		ach.created_at = result.getString("created_at");
		ach.updated_at = result.getString("updated_at");
		ach.task = result.getString("task");
		ach.analytic_id = result.getInt("analytic_id");
		ach.max_count = result.getInt("max_count");
	}
}
