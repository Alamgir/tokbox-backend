package com.cboxgames.idonia.backend.commons.db.tutorial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

//import com.cboxgames.idonia.backend.commons.DefaultValueTable;
import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.tutorial.TutorialDBSQL;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.UserTutorial;
import com.cboxgames.utils.idonia.types.Tutorial;
import com.cboxgames.utils.idonia.types.UserTutorial.UserTutorialWrapper;
import com.cboxgames.utils.idonia.types.proto.UserTutorialArrayProto;
import com.cboxgames.utils.idonia.types.proto.UserTutorialProto;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;;

/**
 * Handle All SQL DB accesses for "/user_tutorials/*" and "/user_tutorials"
 *
 * @author Michael Chang
 *
 */
public class UserTutorialDBSQL extends DBSQL implements IUserTutorialDB {

    private static int DEFAULT_BUFFER_SIZE = 256;
	
	public UserTutorialDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
		super(data_source, servlet_context);
	}
	
	private List<Tutorial> getTutorials() {
		
		 TutorialDBSQL t_db_sql = null;
	     try {
            t_db_sql = new TutorialDBSQL(getDataSource(), getServletContext());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
		return t_db_sql.getTutorials();
	}
	
	public boolean createUserTutorials(User user, Connection conn) {
	
		List<Tutorial> tw = getTutorials();
		List<UserTutorial> ut_list = new ArrayList<UserTutorial>();
		// TutorialWrapper[] tw = DefaultValueTable.getTutorialWrapperArray();
		for (int ix = 0; ix < tw.size(); ix++) {
			Tutorial t = tw.get(ix);
			UserTutorial ut = new UserTutorial();
			ut.id = ut.tutorial_id = t.id;
			ut.user_id = user.id;
			ut.tutorial_name = new String(t.name);
			ut.complete = false;
			ut_list.add(ut);
		}
		
		if (!ut_list.isEmpty()) {
			user.user_tutorials = (UserTutorial[]) ut_list.toArray(new UserTutorial[0]);
		}
		
		boolean created = false;
		try {
			
			PreparedStatement query = conn.prepareStatement("INSERT INTO user_tutorials(user_id, data) VALUES(?, ?)");
			query.setInt(1, user.id);
			query.setBytes(2, getUserTutorialProtoData());
			
			query.executeUpdate();
			created = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return created;
	}

    //Populate User Tutorial upon creating a new user, build a set of tutorials, and then save it to the DB
	public boolean createUserTutorials(int user_id) {

		Connection conn = null;
		boolean created = false;
		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("INSERT INTO user_tutorials(user_id, data) VALUES(?, ?)");
			query.setInt(1, user_id);
			query.setBytes(2, getUserTutorialProtoData());
			
			query.executeUpdate();
			created = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return created;
	}
	
	private byte[] getUserTutorialProtoData() {
		
		List<Tutorial> tw = getTutorials();
		// TutorialWrapper[] tw = DefaultValueTable.getTutorialWrapperArray();
		List<UserTutorialProto> tutorials = new ArrayList<UserTutorialProto>();
		for (int ix = 0; ix < tw.size(); ix++) {
			Tutorial t = tw.get(ix);
			UserTutorialProto utp = new UserTutorialProto();
			utp.setComplete(false);
			utp.setTutorialId(t.id);
			tutorials.add(utp);
		}

		UserTutorialArrayProto tut_array = new UserTutorialArrayProto();
		tut_array.setUserTutorialList(tutorials);
		byte[] user_tutorial_data = ProtostuffIOUtil.toByteArray(tut_array, UserTutorialArrayProto.getSchema(),
				LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
		
		return user_tutorial_data;
	}
	
	public UserTutorial[] getUserTutorialArray(int user_id, byte[] user_tutorials_data) {
		
        UserTutorialArrayProto utap = new UserTutorialArrayProto();
        ProtostuffIOUtil.mergeFrom(user_tutorials_data, utap, UserTutorialArrayProto.getSchema());
    	
		List<UserTutorial> user_tutorial_list = new ArrayList<UserTutorial>();
        List<UserTutorialProto> utp_list = utap.getUserTutorialList();
        for (UserTutorialProto utp : utp_list) {
            UserTutorial ut = new UserTutorial();
            ut.user_id = user_id;
            ut.tutorial_id = utp.getTutorialId();
            ut.complete = utp.getComplete();
            user_tutorial_list.add(ut);
        }

        return (UserTutorial[]) user_tutorial_list.toArray(new UserTutorial[0]);
	}
	
	/**
	 * Check if a user tutorial of user_id exists
	 */
    public boolean userTutorialExist(int user_id) {
	   
        Connection conn = null;
        boolean exist = false;
        try {
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("SELECT * FROM user_tutorials WHERE user_id = ? ");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();
            if (results.next()) {
            	exist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
        return exist;
    }
    
    /**
     * 
     * @return an array of user tutorial wrapper
     */
    public UserTutorialWrapper[] getUserTutorialDetails() {
		
		Connection conn = null;
		List<UserTutorialWrapper> user_tutorial_list = new ArrayList<UserTutorialWrapper>();
		UserTutorialWrapper[] user_tutorial_array = null;
	
		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_tutorials");
			ResultSet result = query.executeQuery();
			if (result != null) {
				while (result.next()) {
					byte[] data = result.getBytes("data");
					if (data == null) continue;
					
					int user_id = result.getInt("user_id");
					UserTutorialArrayProto tut_array = new UserTutorialArrayProto();
					ProtostuffIOUtil.mergeFrom(data, tut_array, UserTutorialArrayProto.getSchema());
					
					List<UserTutorialProto> utp_list = tut_array.getUserTutorialList();
					for (UserTutorialProto utp : utp_list) {
						UserTutorialWrapper utw = new UserTutorialWrapper();
						UserTutorial ut = utw.user_tutorial;
						ut.user_id = user_id;
						ut.tutorial_id = utp.getTutorialId();
						ut.complete = utp.getComplete();
						user_tutorial_list.add(utw);
					}
				}
				
				if (!user_tutorial_list.isEmpty()) {
					user_tutorial_array = (UserTutorialWrapper[]) user_tutorial_list.toArray(new UserTutorialWrapper[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return user_tutorial_array;
	}
    
    /**
     * 
     * @return an array of user tutorial wrappers for a given user_id
     */
    public UserTutorialWrapper[] getUserTutorialDetails(int user_id) {
		
		Connection conn = null;
		UserTutorialWrapper[] user_tutorial_array = null;
	
		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_tutorials WHERE user_id = ?");
			query.setInt(1, user_id);
			ResultSet result = query.executeQuery();
			user_tutorial_array = createUserTutorialFromResultSet(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return user_tutorial_array;
	}
    
    private UserTutorialWrapper[] createUserTutorialFromResultSet(ResultSet result) throws SQLException {
    	
    	if ((result == null) || !result.next()) return null;
    	
    	byte[] data = result.getBytes("data");
    	int user_id = result.getInt("user_id");
    	
    	UserTutorialArrayProto tut_array = new UserTutorialArrayProto();
		ProtostuffIOUtil.mergeFrom(data, tut_array, UserTutorialArrayProto.getSchema());
		
		List<UserTutorialWrapper> user_tutorial_list = new ArrayList<UserTutorialWrapper>();
		List<UserTutorialProto> utp_list = tut_array.getUserTutorialList();
		for (UserTutorialProto utp : utp_list) {
			UserTutorialWrapper utw = new UserTutorialWrapper();
			UserTutorial ut = utw.user_tutorial;
			ut.user_id = user_id;
			ut.tutorial_id = utp.getTutorialId();
			ut.complete = utp.getComplete();
			user_tutorial_list.add(utw);
		}
		
		return (UserTutorialWrapper[]) user_tutorial_list.toArray(new UserTutorialWrapper[0]);
    }
    
    /**
     * @param user_id the user's id
     * @return an array of user tutorials for a given user_id and connection
     * @throws SQLException 
     */
    public UserTutorial[] getUserTutorialDetailsByID(int user_id, Connection conn) throws SQLException {

		PreparedStatement query = conn.prepareStatement("SELECT * FROM user_tutorials WHERE user_id = ?");
		query.setInt(1, user_id);
		ResultSet result = query.executeQuery();
		UserTutorialWrapper[] wrapper_array = createUserTutorialFromResultSet(result);
		
		if (wrapper_array == null) return null;
		
		List<UserTutorial> list = new ArrayList<UserTutorial>();
		for (int ix = 0, length = wrapper_array.length; ix < length; ix++) {
			list.add(wrapper_array[ix].user_tutorial);
		}
			
		return (UserTutorial[]) list.toArray(new UserTutorial[0]);
	}
    
	public boolean userTutorialComplete(int user_id, int tutorial_id) {
		
		boolean update = false;
		Connection conn = null;

		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_tutorials WHERE user_id = ?");
			query.setInt(1, user_id);
			ResultSet result = query.executeQuery();
			if (result != null) {
				while (result.next()) {
					byte[] data = result.getBytes("data");
					if (data == null) continue;
					
					UserTutorialArrayProto tut_array = new UserTutorialArrayProto();
					ProtostuffIOUtil.mergeFrom(data, tut_array, UserTutorialArrayProto.getSchema());
					
					boolean found = false;
					List<UserTutorialProto> utp_list = tut_array.getUserTutorialList();
					for (UserTutorialProto utp : utp_list) {
						if (utp.getTutorialId() != tutorial_id) continue;
						utp.setComplete(true);
						found = true;
						break;
					}
					
					if (found == false) continue;
					
					byte[] user_tutorial_data = ProtostuffIOUtil.toByteArray(tut_array, UserTutorialArrayProto.getSchema(),
							LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
						
					PreparedStatement update_query = conn.prepareStatement("UPDATE user_tutorials SET data = ? WHERE user_id = ?");
					update_query.setBytes(1, user_tutorial_data);
					update_query.setInt(2, user_id);
					update_query.executeUpdate();
					update = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (update == false) {
			System.out.println("No user tutorial update for 'complete' is done for user_id = " + user_id + 
					" and tutorial_id = " + tutorial_id);
		}
		
		closeConnection(conn);
		return update;
	}

	public boolean deleteUserTutorial(int user_id) {
		boolean delete = false;
		Connection conn = null;

		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("DELETE FROM user_tutorials WHERE user_id = ?");
			query.setInt(1, user_id);
			int cnt = query.executeUpdate();
			System.out.println(cnt + " of user_tutorial with user_id = " + user_id + " are deleted");
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (delete == false) {
			System.out.println("Deletion of a user_tutorial failed for user_id = " + user_id);
		}
		
		closeConnection(conn);
		return delete;
	}
}
