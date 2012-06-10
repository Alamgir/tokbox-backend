package com.cboxgames.idonia.backend.commons.db.node;

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
import com.cboxgames.utils.idonia.types.Node;
import com.cboxgames.utils.idonia.types.Node.NodeWrapper;

public class NodeDBSQL extends DBSQL implements INodeDB {

	public NodeDBSQL(DataSource connection_pool, ServletContext servlet_context)
			throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public List<Node> getNodes(Connection conn) {
		List<Node> nodes = new ArrayList<Node>();

		
		try {

			PreparedStatement statement = conn.prepareStatement("SELECT * FROM nodes");
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Node node = new Node();
				getNodeFromResult(result, node);
				nodes.add(node);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
        }
		
		return nodes;
	}
	
	@Override
	public NodeWrapper[] getNodeDetails() {
		
		List<NodeWrapper> nw_list = new ArrayList<NodeWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM nodes");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				NodeWrapper nw = new NodeWrapper();
				getNodeFromResult(result, nw.node);
				nw_list.add(nw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (NodeWrapper[]) nw_list.toArray(new NodeWrapper[0]);
	}

	@Override
	public Node getNode( int id ) {
		Node node = new Node();
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM nodes WHERE id = ?");
			statement.setInt(1, id);
			
			ResultSet result = statement.executeQuery();
			if ((result != null) && result.next()) {
				getNodeFromResult(result, node);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return node;
	}

	/**
	 * Convert node type to its id which is the one stored in db.
	 */
	@Override
	public List<Node> getNodes(NodeType type) {
		
		List<Node> nodes = new ArrayList<Node>();

		Connection conn = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM nodes WHERE node_type = ?");
			statement.setInt(1, type.getId());
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Node node = new Node();
				getNodeFromResult(result, node);
				nodes.add(node);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return nodes;
	}
	
	public static Map<Integer, Node> getHashNodes(NodeType type, Connection conn) throws SQLException {

		Map<Integer, Node> map = new HashMap<Integer, Node>(0);
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM nodes WHERE node_type = ?");
		statement.setInt(1, type.getId());	
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			Node node = new Node();
			getNodeFromResult(result, node);
			map.put(node.id, node);
		}		
	
		return map;
	}
	
	private static void getNodeFromResult(ResultSet result, Node node) throws SQLException {
		
		node.id = result.getInt("id");
		node.name = result.getString("name");
		node.description = result.getString("description");
		node.node_type = NodeType.nameFromId(result.getInt("node_type"));
		node.num_waves = result.getInt("num_waves");
		node.child_nodes = result.getString("child_nodes");
		node.mob_id = result.getString("mob_id");
        node.region = result.getInt("region");
        node.min_lvl = result.getInt("min_lvl");
        node.max_lvl = result.getInt("max_lvl");
	}
}
