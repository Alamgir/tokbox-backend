package com.cboxgames.idonia.backend.commons.db.tutorial;

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
import com.cboxgames.utils.idonia.types.Tutorial;
import com.cboxgames.utils.idonia.types.Tutorial.TutorialWrapper;

public class TutorialDBSQL extends DBSQL implements ITutorialDB {

	public TutorialDBSQL(DataSource connection_pool,
			ServletContext servlet_context) throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public List<Tutorial> getTutorials() {
		List<Tutorial> tutorials = new ArrayList<Tutorial>();
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM tutorials");
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Tutorial tutorial = new Tutorial();	
				getTutorialFromResult(result, tutorial);
				tutorials.add(tutorial);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return tutorials;
	}
	
	@Override
	public TutorialWrapper[] getTutorialDetails() {
		
		List<TutorialWrapper> tw_list = new ArrayList<TutorialWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM tutorials");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				TutorialWrapper tw = new TutorialWrapper();
				getTutorialFromResult(result, tw.tutorial);
				tw_list.add(tw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (TutorialWrapper[]) tw_list.toArray(new TutorialWrapper[0]);
	}

	@Override
	public Map<Integer, Tutorial> getTutorials(List<Integer> ids) {
		Map<Integer, Tutorial> map = new HashMap<Integer, Tutorial>();
		
		if (ids == null || ids.size() <= 0)
			return map;
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			StringBuilder query = new StringBuilder();
			query.append("SELECT * FROM tutorials WHERE id=");
			query.append(ids.remove(0));
			for (Integer id : ids) {
				query.append(" OR id=");
				query.append(id);
			}
			
			PreparedStatement statement = conn.prepareStatement(query.toString());
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Tutorial tutorial = new Tutorial();	
				getTutorialFromResult(result, tutorial);
				map.put(tutorial.id, tutorial);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return map;
	}

    @Override
    public Tutorial getTutorialByID(int id) {
    	
        Connection conn = null;
        Tutorial tutorial = new Tutorial();
        try{
            conn = getConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM tutorials WHERE id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
           if ((result != null) && result.next()) {
                getTutorialFromResult(result, tutorial);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return tutorial;
    }

    private void getTutorialFromResult(ResultSet result, Tutorial tutorial) throws SQLException {
 
		tutorial.id = result.getInt("id");
		tutorial.name = result.getString("name");
	}
}
