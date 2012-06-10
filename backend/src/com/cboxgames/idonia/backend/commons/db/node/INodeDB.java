package com.cboxgames.idonia.backend.commons.db.node;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Node;
import com.cboxgames.utils.idonia.types.Node.NodeWrapper;

public interface INodeDB {
	
	public static final int NODE_TYPE_BOSS_ID = 0;
	public static final int NODE_TYPE_NODE_ID = 1;
	public static final int NODE_TYPE_TREASURE_ID = 2;
	public static final int NODE_TYPE_BOOTY_ID = 3;
    public static final int NODE_TYPE_DAILY_ID = 4;
	public static final int NODE_TYPE_NONE_ID = 5;

	/**
	 * Get a list of all the nodes.
	 * 
	 * @return
	 */
	public List<Node> getNodes(Connection conn);
	
	/**
	 * Get a list of all the node wrappers.
	 * 
	 * @return
	 */
	public NodeWrapper[] getNodeDetails();
	
	/**
	 * Get a node by its id.
	 * 
	 * @param id
	 * @return
	 */
	public Node getNode(int id);
	
	/**
	 * Retrieve a list of nodes by it's type.
	 * 
	 * @param type
	 * @return
	 */
	public List<Node> getNodes(NodeType type);
	
	public enum NodeType {
		Boss(NODE_TYPE_BOSS_ID),
		Node(NODE_TYPE_NODE_ID),
		Treasure(NODE_TYPE_TREASURE_ID),
		Booty(NODE_TYPE_BOOTY_ID),
        Daily(NODE_TYPE_DAILY_ID),
		None(NODE_TYPE_NONE_ID);
		
		private int id;
		
		private NodeType(int id) {
			this.id = id;
		}
		
		public int getId() { return id; }
		
		public static String nameFromId(int id) {
			switch(id) {
			
			case NODE_TYPE_BOSS_ID:
				return Boss.name();
			case NODE_TYPE_NODE_ID:
				return Node.name();
			case NODE_TYPE_TREASURE_ID:
				return Treasure.name();
			case NODE_TYPE_BOOTY_ID:
				return Booty.name();
            case NODE_TYPE_DAILY_ID:
                return Daily.name();
			default:
				return None.name();
			}
		}
	}
}
