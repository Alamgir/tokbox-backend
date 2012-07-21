package com.tokbox.graphdb;


import com.tokbox.types.Comment;
import com.tokbox.types.Entity;
import com.tokbox.types.User;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;

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
    public static Node user_reference_node;
    public static Node entity_reference_node;

    private static enum RelTypes implements RelationshipType {
        USER_REFERENCE,
        ENTITY_REFERENCE,
        USER,
        ENTITY,
        AUTHOR,
        COMMENT
    }
    
    public static void init() {
        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("E:\\Alamgir\\neo4jdb");
        user_index = graphDB.index().forNodes("users");
        entity_index = graphDB.index().forNodes("entities");
        user_reference_node = getUserRefNode();
        entity_reference_node = getEntityRefNode();
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
        //check for node with relationship USERS_REFERENCE
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

                user_index.add(user_node, "display_name", user.display_name);
                
                user_reference_node.createRelationshipTo(user_node, RelTypes.USER);
                //user_rel.setProperty("created_at", new Date());
                tx.success();
            }
            finally {
                tx.finish();
            }
        }
    }
    
    public static User getUser(String username) {
        User user = new User();
        Node found_user = user_index.get("display_name", username).getSingle();

        if (found_user != null) {
            user.display_name = (String) found_user.getProperty("display_name");
            user.country = (String) found_user.getProperty("country");
            user.email = (String) found_user.getProperty("email");
            //user.quota_info = (HashMap<String,Long>) found_user.getProperty("quota_info");
            user.referral_link = (String) found_user.getProperty("referral_link");
        }

        return user;
    }
    
    public static void deleteUser(String username) {
        Node found_user = user_index.get("display_name", username).getSingle();
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
    
    public static void createEntity(Entity entity) {
        if (user_reference_node != null) {
            Transaction tx = graphDB.beginTx();
            try {
                Node entity_node = graphDB.createNode();
                entity_node.setProperty("rev",entity.rev);
                entity_node.setProperty("parent_dir",entity.parent_dir);
                entity_node.setProperty("name",entity.name);

                entity_index.add(entity_node, "entity_id", entity_node.getId());
                entity_index.add(entity_node, "parent_dir", entity.parent_dir);
                //set the entity id to the node id
                entity.id = entity_node.getId();

                entity_reference_node.createRelationshipTo(entity_node, RelTypes.ENTITY_REFERENCE);
                //user_rel.setProperty("created_at", new Date());
                tx.success();
            }
            finally {
                tx.finish();
            }
        }
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
    
    public static void createComment(String body, String author_name, long parent_entity_id) {
        if (entity_reference_node != null && user_reference_node != null) {
            Node found_user = user_index.get("display_name", author_name).getSingle();
            Node found_entity = entity_index.get("entity_id", parent_entity_id).getSingle();
            long current_epoch = System.currentTimeMillis();
            Transaction tx = graphDB.beginTx();
            try {
                //create a new comment node and attach body
                Node new_comment = graphDB.createNode();
                new_comment.setProperty("body", body);
                new_comment.setProperty("author_name", author_name);

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
    
    public static void getEntityComments(Entity entity) {
        if (entity_reference_node != null) {
            Node found_entity = entity_index.get("parent_dir", entity.parent_dir).getSingle();
            //Get all comment relationships
            Iterable<Relationship> comments = found_entity.getRelationships(RelTypes.COMMENT, Direction.OUTGOING);
            while (comments.iterator().hasNext()) {
                Comment new_comment = new Comment();

                Relationship comment_rel = comments.iterator().next();
                Node comment_node = comment_rel.getEndNode();

                new_comment.author_name = (String)comment_node.getProperty("author_name");
                new_comment.body = (String)comment_node.getProperty("body");
                new_comment.created_at = (Long)comment_rel.getProperty("created_at");

                entity.comments.add(new_comment);
            }
        }
    }
}
