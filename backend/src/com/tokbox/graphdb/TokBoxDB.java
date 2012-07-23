package com.tokbox.graphdb;


import com.tokbox.types.Comment;
import com.tokbox.types.Entity;
import com.tokbox.types.User;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 7/7/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TokBoxDB {
    public static GraphDatabaseService graphDB;
    public static Index<Node> user_index;
    public static Index<Node> entity_index;
    public static Index<Node> parent_dir_index;
    public static Node user_reference_node;
    public static Node entity_reference_node;
    public static Node parent_dir_reference_node;

    private static enum RelTypes implements RelationshipType {
        USER_REFERENCE,
        PARENT_DIR_REFERENCE,
        ENTITY_REFERENCE,
        USER,
        ENTITY,
        AUTHOR,
        COMMENT,
        USER_EVENT,
        PARENT_DIR,
        PARENT
    }
    
    public static void init() {
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("E:\\Alamgir\\neo4jdb");
        user_index = graphDB.index().forNodes("users");
        entity_index = graphDB.index().forNodes("entities");
        parent_dir_index = graphDB.index().forNodes("parent_dir");
        user_reference_node = getUserRefNode();
        entity_reference_node = getEntityRefNode();
        parent_dir_reference_node = getParentDirRefNode();
        registerShutdownHook(graphDB);
    }
    
    private static void registerShutdownHook(final GraphDatabaseService graphDB) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDB.shutdown();
            }
        });
    }
    
    private static Node getUserRefNode() {
        //check for node with relationship USERS_REFERENCE
        Transaction tx = graphDB.beginTx();
        try {
            Iterable<Relationship> user_ref = graphDB.getReferenceNode().getRelationships(Direction.OUTGOING, RelTypes.USER_REFERENCE);
            if (user_ref.iterator().hasNext()) {
                return user_ref.iterator().next().getEndNode();
            }
            else {
                Node user_ref_node = graphDB.createNode();
                graphDB.getReferenceNode().createRelationshipTo(user_ref_node, RelTypes.USER_REFERENCE);
                tx.success();
                return user_ref_node;
            }
        }
        finally {
            tx.finish();
        }
    }

    private static Node getEntityRefNode() {
        //check for node with relationship ENTITY_REFERENCE
        Transaction tx = graphDB.beginTx();
        try {
            Iterable<Relationship> entity_ref = graphDB.getReferenceNode().getRelationships(Direction.OUTGOING, RelTypes.ENTITY_REFERENCE);
            if (entity_ref.iterator().hasNext()) {
                return entity_ref.iterator().next().getEndNode();
            }
            else {
                Node entity_ref_node = graphDB.createNode();
                graphDB.getReferenceNode().createRelationshipTo(entity_ref_node, RelTypes.ENTITY_REFERENCE);
                tx.success();
                return entity_ref_node;
            }
        }
        finally {
            tx.finish();
        }
    }

    private static Node getParentDirRefNode() {
        //check for node with relationship PARENT_DIR_REFERENCE
        Transaction tx = graphDB.beginTx();
        try {
            Iterable<Relationship> parent_dir_ref = graphDB.getReferenceNode().getRelationships(Direction.OUTGOING, RelTypes.PARENT_DIR_REFERENCE);
            if (parent_dir_ref.iterator().hasNext()) {
                return parent_dir_ref.iterator().next().getEndNode();
            }
            else {
                Node parent_dir_ref_node = graphDB.createNode();
                graphDB.getReferenceNode().createRelationshipTo(parent_dir_ref_node, RelTypes.PARENT_DIR_REFERENCE);
                tx.success();
                return parent_dir_ref_node;
            }
        }
        finally {
            tx.finish();
        }
    }


    public static void createUser(User user) {
        if (user_reference_node != null) {
            Transaction tx = graphDB.beginTx();
            try {
                Node user_node = graphDB.createNode();
                user_node.setProperty("display_name", user.display_name);
                user_node.setProperty("referral_link", user.referral_link);
                user_node.setProperty("country", user.country);
                //user_node.setProperty("quota_info", user.quota_info);
                user_node.setProperty("email", user.email);
                user_node.setProperty("uid", user.uid);

                user_index.add(user_node, "uid", user.uid);
                
                user_reference_node.createRelationshipTo(user_node, RelTypes.USER);
                //user_rel.setProperty("created_at", new Date());
                tx.success();
            }
            finally {
                tx.finish();
            }
        }
    }
    
    public static User getUser(long uid) {
        User user = new User();
        Node found_user = user_index.get("uid", uid).getSingle();

        if (found_user != null) {
            user.display_name = (String) found_user.getProperty("display_name");
            user.country = (String) found_user.getProperty("country");
            user.email = (String) found_user.getProperty("email");
            //user.quota_info = (HashMap<String,Long>) found_user.getProperty("quota_info");
            user.referral_link = (String) found_user.getProperty("referral_link");
        }

        return user;
    }
    
    public static void deleteUser(long uid) {
        Node found_user = user_index.get("uid", uid).getSingle();
        Transaction tx = graphDB.beginTx();
        try {
            while (found_user.getRelationships().iterator().hasNext()) {
                found_user.getRelationships().iterator().next().delete();
            }
            found_user.delete();
            tx.success();
        }
        finally {
            tx.finish();
        }
    }
    
    public static Node createEntity(Entity entity) {
        Node entity_node = graphDB.createNode();
        Node parent_dir_node = graphDB.createNode();
        if (user_reference_node != null) {
            Transaction tx = graphDB.beginTx();
            try {
                //Create the entity node
                entity_node.setProperty("rev",entity.rev);
                entity_node.setProperty("name",entity.name);
                entity_node.setProperty("orphan", entity.orphan);
                entity_index.add(entity_node, "entity_id", entity_node.getId());
                entity_index.add(entity_node, "parent_dir", entity.parent_dir);
                //set the entity id to the node id
                entity.id = entity_node.getId();
                //add entity to index
                entity_reference_node.createRelationshipTo(entity_node, RelTypes.ENTITY_REFERENCE);
                //user_rel.setProperty("created_at", new Date());

                //Create the parent_dir node
                parent_dir_node.setProperty("path", entity.parent_dir);
                //add parent_dir to index
                parent_dir_index.add(parent_dir_node, "path", entity.parent_dir);
                //connect parent_dir node to entity node
                parent_dir_reference_node.createRelationshipTo(parent_dir_node, RelTypes.PARENT_DIR);
                parent_dir_node.createRelationshipTo(entity_node, RelTypes.PARENT);


                tx.success();
            }
            finally {
                tx.finish();
            }
        }
        return entity_node;
    }
    
    public static boolean updateEntity(Entity entity) {
        boolean result = false;
        if (entity_reference_node != null) {
            Node found_entity = entity_index.get("entity_id", entity.id).getSingle();
            Transaction tx = graphDB.beginTx();
            try {
                found_entity.setProperty("parent_dir", entity.parent_dir);
                found_entity.setProperty("name", entity.name);
                if (entity.id == found_entity.getId()) {
                    tx.success();
                    result = true;
                }
            }
            finally {
                tx.finish();
            }
        }
        return result;
    }
    
    public static void createComment(String body, User user, Entity parent_entity) {
        if (entity_reference_node != null && user_reference_node != null) {
            Node found_user = user_index.get("uid", user.uid).getSingle();
            Node found_entity = entity_index.get("entity_id", parent_entity.id).getSingle();
            long current_epoch = System.currentTimeMillis();
            Transaction tx = graphDB.beginTx();
            if (found_entity == null) {
                //the entity needs to be created for this comment
                found_entity = createEntity(parent_entity);
            }
            
            try {
                //create a new comment node and attach body
                Node new_comment = graphDB.createNode();
                new_comment.setProperty("body", body);
                new_comment.setProperty("author_name", user.display_name);

                //create relationshipTo the author's user node, set author_date
                Relationship author_rel = found_user.createRelationshipTo(new_comment, RelTypes.AUTHOR);
                author_rel.setProperty("created_at", current_epoch);

                //create relationshipTo the parentEntity node, set attribution_date
                Relationship comment_rel = found_entity.createRelationshipTo(new_comment, RelTypes.COMMENT);
                comment_rel.setProperty("created_at", current_epoch);

                tx.success();
            }
            finally {
                tx.finish();
            }
        }
    }

    /**
     * Pass in the parent entity (folder and its contents) and attribute comments
     * @param entity the parent_entity, path is the parent_dir to all children entities
     */
    public static void getEntityComments(Entity entity) {
        Transaction tx = graphDB.beginTx();
        try {
            //path is the parent_dir to all children entities
            if (parent_dir_reference_node != null && entity_reference_node != null) {
                //find the parent_dir node for the child entities
                Node parent_dir_node = parent_dir_index.get("path", entity.path).getSingle();
                Iterable<Relationship> parent_dir_rel = parent_dir_node.getRelationships(RelTypes.PARENT, Direction.OUTGOING);
                //iterate through the child entities of the parent_dir_node and get their comments
                while (parent_dir_rel.iterator().hasNext()) {
                    //Deal with each entity_node in the parent_dir
                    Node entity_node = parent_dir_rel.iterator().next().getEndNode();
                    String entity_node_rev = (String)entity_node.getProperty("rev");
                    String entity_node_name = (String)entity_node.getProperty("name");
                    //check if the entity node even exists in the entity given by dropbox
                    ArrayList<Comment> comments = getEntityComments(entity_node);
                    for (Entity child_entity : entity.contents) {
                        if (!child_entity.name.equals(entity_node_name)) {
                            //name doesn't match, so rev doesn't either: match the rev and attribute comments
                            entity_node.setProperty("rev", child_entity.rev);
                            entity_node.setProperty("name", child_entity.name);
                            child_entity.comments = comments;
                        }
                        else if (!child_entity.rev.equals(entity_node_name) && !child_entity.name.equals(entity_node_name)){
                            Entity orphaned_entity = new Entity();
                            orphaned_entity.orphan = true;
                            //nothing matches, the node is an orphaned entity:
                            entity_node.setProperty("orphan", orphaned_entity.orphan);
                            //attribute comments to a new entity and add it to entity.contents
                            orphaned_entity.rev = (String)entity_node.getProperty("rev");
                            orphaned_entity.name = (String)entity_node.getProperty("name");
                            orphaned_entity.parent_dir = entity.path;
                            orphaned_entity.icon = "orphan";
                            orphaned_entity.comments = comments;
                            
                            entity.contents.add(orphaned_entity);
                        }
                        else {
                            //success, rev and name match: go ahead as usual
                            child_entity.comments = comments;
                        }
                    }

                }
                tx.success();
            }
        }
        finally {
            tx.finish();
        }
        
    }
    
    private static ArrayList<Comment> getEntityComments(Node entity_node) {
        Iterable<Relationship> entity_comment_rel = entity_node.getRelationships(RelTypes.COMMENT, Direction.OUTGOING);
        ArrayList<Comment> comments = new ArrayList<Comment>();
        while (entity_comment_rel.iterator().hasNext()) {
            Relationship comment_rel = entity_comment_rel.iterator().next();
            Comment comment = new Comment();
            Node comment_node = comment_rel.getEndNode();
            comment.author_name = (String)comment_node.getProperty("author_name");
            comment.body = (String)comment_node.getProperty("body");
            comment.created_at = (Long)comment_rel.getProperty("created_at");

            comments.add(comment);
        }
        return comments;
    }

}
