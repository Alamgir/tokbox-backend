package com.cboxgames.idonia.backend.commons.db.node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.idonia.backend.commons.db.node.IUserNodeDB;
import com.cboxgames.idonia.backend.commons.db.node.NodeDBSQL;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.Node;
import com.cboxgames.utils.idonia.types.UserNode;
import com.cboxgames.utils.idonia.types.UserNode.UserNodeWrapper;
import com.cboxgames.utils.idonia.types.proto.UserNodeArrayProto;
import com.cboxgames.utils.idonia.types.proto.UserNodeProto;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;

public class UserNodeDBSQL extends DBSQL implements IUserNodeDB {
	
    private static int DEFAULT_BUFFER_SIZE = 256;
    private NodeDBSQL n_db_sql;
    
	public UserNodeDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
		super(data_source, servlet_context);

        n_db_sql = new NodeDBSQL(getDataSource(), getServletContext());

	}
	 
    // Populate User Node upon creating a new user, build a set of nodes, and then save it to the DB
	public boolean createUserNodes(User user, Connection conn) {

		boolean created = false;
		List<Node> nodes = n_db_sql.getNodes(conn);
		List<UserNode> un_list = new ArrayList<UserNode>();
		for (int ix = 0; ix < nodes.size(); ix++) {
			Node a = nodes.get(ix);
			UserNode un = new UserNode();
			un.id = un.node_id = a.id;
			un.user_id = user.id;
			un.complete = false;
			un_list.add(un);
		}
		
		if (!un_list.isEmpty()) {
			user.user_nodes = (UserNode[]) un_list.toArray(new UserNode[0]);
		}
		
		try {
            conn.setAutoCommit(false);
			PreparedStatement query = conn.prepareStatement("INSERT INTO user_nodes(user_id, data) VALUES(?, ?)");
			query.setInt(1, user.id);
			query.setBytes(2, getUserNodeProtoData(nodes));
			
			query.executeUpdate();
			created = true;
			
		} catch (SQLException e) {
            e.printStackTrace();
            try {
                System.err.print("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException f) {
                f.printStackTrace();
            }
		}
		return created;
	}
	
	public boolean createUserNodes(int user_id) {

		Connection conn = null;
		boolean created = false;

		try {
			conn = getConnection();

            List<Node> nodes = n_db_sql.getNodes(conn);

			PreparedStatement query = conn.prepareStatement("INSERT INTO user_nodes(user_id, data) VALUES(?, ?)");
			query.setInt(1, user_id);
			query.setBytes(2, getUserNodeProtoData(nodes));

			query.executeUpdate();
			created = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return created;
	}
	
	private byte[] getUserNodeProtoData(List<Node> nodes) {
		
		List<UserNodeProto> proto_nodes = new ArrayList<UserNodeProto>();
		for (int ix = 0; ix < nodes.size(); ix++) {
			Node t = nodes.get(ix);
			UserNodeProto unp = new UserNodeProto();
			unp.setComplete(false);
			unp.setNodeId(t.id);
            unp.setId(t.id);
			proto_nodes.add(unp);
		}

		UserNodeArrayProto tut_array = new UserNodeArrayProto();
		tut_array.setUserNodeList(proto_nodes);
		byte[] user_node_data = ProtostuffIOUtil.toByteArray(tut_array, UserNodeArrayProto.getSchema(),
				LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
		return user_node_data;
	}

	/**
	 * Check if a user node of user_id exists
	 */
    public boolean userNodeExist(int user_id) {
	   
        Connection conn = null;
        boolean exist = false;
        try {
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("SELECT * FROM user_nodes WHERE user_id = ? ");
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
     * @return an array of user node wrapper
     */
    public UserNodeWrapper[] getUserNodeDetails() {
		
		Connection conn = null;
		List<UserNodeWrapper> user_node_list = new ArrayList<UserNodeWrapper>();
		UserNodeWrapper[] user_node_array = null;
	
		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_nodes");
			ResultSet result = query.executeQuery();
			if (result != null) {
				while (result.next()) {
					byte[] data = result.getBytes("data");
					if (data == null) continue;
					
					int user_id = result.getInt("user_id");
					UserNodeArrayProto tut_array = new UserNodeArrayProto();
					ProtostuffIOUtil.mergeFrom(data, tut_array, UserNodeArrayProto.getSchema());
					
					List<UserNodeProto> unp_list = tut_array.getUserNodeList();
					for (UserNodeProto unp : unp_list) {
						UserNodeWrapper unw = new UserNodeWrapper();
						UserNode un = unw.user_node;
						un.user_id = user_id;
						un.id = un.node_id = unp.getNodeId();
						un.complete = unp.getComplete();
						user_node_list.add(unw);
					}
				}
				
				if (!user_node_list.isEmpty()) {
					user_node_array = (UserNodeWrapper[]) user_node_list.toArray(new UserNodeWrapper[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
	    } finally {
	    	closeConnection(conn);
		}
		
		return user_node_array;
	}
    
    /**
     * 
     * @return an array of user node wrappers for a given user_id
     */
    public UserNodeWrapper[] getUserNodeDetails(int user_id) {
		
		Connection conn = null;
		List<UserNodeWrapper> user_node_list = new ArrayList<UserNodeWrapper>();
		UserNodeWrapper[] user_node_array = null;
	
		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_nodes WHERE user_id = ?");
			query.setInt(1, user_id);
			ResultSet result = query.executeQuery();
			if (result != null) {
				while (result.next()) {
					byte[] data = result.getBytes("data");
					if (data == null) continue;
					
					UserNodeArrayProto tut_array = new UserNodeArrayProto();
					ProtostuffIOUtil.mergeFrom(data, tut_array, UserNodeArrayProto.getSchema());
					
					List<UserNodeProto> unp_list = tut_array.getUserNodeList();
					for (UserNodeProto unp : unp_list) {
						UserNodeWrapper unw = new UserNodeWrapper();
						UserNode un = unw.user_node;
						un.user_id = user_id;
						un.id = un.node_id = unp.getNodeId();
						un.complete = unp.getComplete();
						user_node_list.add(unw);
					}
				}
				
				if (!user_node_list.isEmpty()) {
					user_node_array = (UserNodeWrapper[]) user_node_list.toArray(new UserNodeWrapper[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
	    } finally {
	    	closeConnection(conn);
		}
		
		return user_node_array;
	}

    /**
     * @param user_id the user's id
     * @return an array of user node wrappers for a given user_id
     */
    public UserNode[] getUserNodeDetailsByID(int user_id) {

		Connection conn = null;
		UserNode[] user_node_array = null;

		try {
			conn = getConnection();

			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_nodes WHERE user_id = ?");
			query.setLong(1, user_id);
			ResultSet result = query.executeQuery();
			if ((result != null) && result.next()) {
				byte[] data = result.getBytes("data");
				user_node_array = getUserNodeArray(user_id, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
	    } finally {
	    	closeConnection(conn);
		}
		
		return user_node_array;
	}
    
    public boolean updateUserFreshBooty(User user) {
    	
        Connection conn = null;
        boolean update = false;
        try {
            conn = getConnection();
            
            Map<Integer, Node> node_hash_tbl = NodeDBSQL.getHashNodes(INodeDB.NodeType.Treasure, conn);
            for (UserNode user_node : user.user_nodes) {
            	Node node = node_hash_tbl.get(user_node.node_id);
            	if (node == null) continue;
            	user_node.complete = false;
            }
            updateUserNodes(user, conn);
            update = true;
            
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
        	closeConnection(conn);
        }
 
        return update;
    }

    public boolean saveUserNodes(UserNodeWrapper[] user_nodes, int user_id) {
    	
    	boolean saved = false;
        UserNodeArrayProto user_node_array_proto = new UserNodeArrayProto();
        List<UserNodeProto> user_nodes_protos = new ArrayList<UserNodeProto>();

        for (int ix = 0; ix < user_nodes.length; ix++) {
            UserNode n = user_nodes[ix].user_node;
            UserNodeProto unp = new UserNodeProto();
            unp.setComplete(n.complete);
            unp.setNodeId(n.node_id);
            unp.setId(n.id);
            user_nodes_protos.add(unp);
        }
        user_node_array_proto.setUserNodeList(user_nodes_protos);

        byte[] user_node_data = ProtostuffIOUtil.toByteArray(user_node_array_proto, UserNodeArrayProto.getSchema(),
                                                             LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
        Connection conn = null;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("UPDATE user_nodes SET "
                                                          + "data = ? WHERE user_id = ? ");
            query.setBytes(1, user_node_data);
            query.setInt(2, user_id);
            query.executeUpdate();
            saved = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
	    } finally {
	    	closeConnection(conn);
		}
        return saved;
    }
    
    public boolean userNodeComplete(int user_id, int node_id) {
		
		boolean update = false;
		Connection conn = null;

		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("SELECT * FROM user_nodes WHERE user_id = ?");
			query.setInt(1, user_id);
			ResultSet result = query.executeQuery();
			if (result != null) {
				while (result.next()) {
					byte[] data = result.getBytes("data");
					if (data == null) continue;
					
					UserNodeArrayProto tut_array = new UserNodeArrayProto();
					ProtostuffIOUtil.mergeFrom(data, tut_array, UserNodeArrayProto.getSchema());
					
					boolean found = false;
					List<UserNodeProto> unp_list = tut_array.getUserNodeList();
					for (UserNodeProto unp : unp_list) {
						if (unp.getNodeId() != node_id) continue;
						unp.setComplete(true);
						found = true;
						break;
					}
					
					if (found == false) continue;
					
					byte[] user_node_data = ProtostuffIOUtil.toByteArray(tut_array, UserNodeArrayProto.getSchema(),
							LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
						
					PreparedStatement update_query = conn.prepareStatement("UPDATE user_nodes SET data = ? WHERE user_id = ?");
					update_query.setBytes(1, user_node_data);
					update_query.setInt(2, user_id);
					update_query.executeUpdate();
					update = true;
				}
			}
	
		} catch (Exception e) {
			e.printStackTrace();
	    } finally {
	    	closeConnection(conn);
		}
		
		return update;
	}

	public boolean deleteUserNode(int user_id) {
		boolean delete = false;
		Connection conn = null;

		try {
			conn = getConnection();
			
			PreparedStatement query = conn.prepareStatement("DELETE FROM user_nodes WHERE user_id = ?");
			query.setInt(1, user_id);
			int cnt = query.executeUpdate();
			System.out.println(cnt + " of user_node with user_id = " + user_id + " are deleted");
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
	    } finally {
	    	closeConnection(conn);
		}
		
		return delete;
	}
	
	// static method
    public static UserNode[] getUserNodeArray(int user_id, byte[] user_nodes_data) {
	
        UserNodeArrayProto unap = new UserNodeArrayProto();
        ProtostuffIOUtil.mergeFrom(user_nodes_data, unap, UserNodeArrayProto.getSchema());
    	
		List<UserNode> user_node_list = new ArrayList<UserNode>();
        List<UserNodeProto> unp_list = unap.getUserNodeList();
        for (UserNodeProto unp : unp_list) {
            UserNode un = new UserNode();
            un.user_id = user_id;
            un.id = un.node_id = unp.getNodeId();
            un.complete = unp.getComplete();
            user_node_list.add(un);
        }

        return (UserNode[]) user_node_list.toArray(new UserNode[0]);
	}

    public static void updateUserNodes(User user, Connection conn) throws SQLException {
    	//check if user_nodes already exists
        //if not, create the first user_node
        //if they do exist, save the new list

        UserNodeArrayProto user_node_array_proto = new UserNodeArrayProto();
        List<UserNodeProto> user_nodes_protos = new ArrayList<UserNodeProto>();

        UserNode[] user_nodes = user.user_nodes;
        for (int ix = 0, ixe = user_nodes.length; ix < ixe; ix++) {
            UserNode n = user_nodes[ix];
            UserNodeProto unp = new UserNodeProto();
            unp.setId(n.id);
            unp.setComplete(n.complete);
            unp.setNodeId(n.node_id);
            user_nodes_protos.add(unp);
        }
        user_node_array_proto.setUserNodeList(user_nodes_protos);

        byte[] user_node_data = ProtostuffIOUtil.toByteArray(user_node_array_proto, UserNodeArrayProto.getSchema(),
                                                             LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE));
        PreparedStatement query = conn.prepareStatement("UPDATE user_nodes SET "
                                                      + "data = ? WHERE user_id = ? ");
        query.setBytes(1, user_node_data);
        query.setInt(2, user.id);
        query.executeUpdate();
    }
}
