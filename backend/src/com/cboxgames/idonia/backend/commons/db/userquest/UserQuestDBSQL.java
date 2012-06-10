package com.cboxgames.idonia.backend.commons.db.userquest;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.UserQuest;
import com.cboxgames.utils.idonia.types.UserQuest.*;
import com.cboxgames.utils.idonia.types.proto.UserQuestArrayProto;
import com.cboxgames.utils.idonia.types.proto.UserQuestProto;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/18/11
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserQuestDBSQL extends DBSQL implements IUserQuestDB {

    public UserQuestDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
        super(data_source,servlet_context);
    }

    @Override
    public ResponseGetUserQuests getUserQuests(int user_id) {
        Connection conn = null;
        ResponseGetUserQuests responseGetUserQuests = new ResponseGetUserQuests();
        responseGetUserQuests.user_quests = new ArrayList<UserQuestWrapper>();

        try {
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("SELECT * FROM user_quests WHERE user_id = ?");
            query.setInt(1, user_id);
            ResultSet results = query.executeQuery();

            if (results != null && results.next()) {
                UserQuestArrayProto uqap = new UserQuestArrayProto();
                ProtostuffIOUtil.mergeFrom(results.getBytes("data"), uqap, UserQuestArrayProto.getSchema());

                List<UserQuestProto> uqp_list = uqap.getUserQuestList();
                for (UserQuestProto uqp : uqp_list) {
                    UserQuestWrapper uqw = new UserQuestWrapper();
                    UserQuest uq = new UserQuest();
                    uqw.user_quest = uq;
                    uq.id = uqp.getQuestId();
                    uq.quest_type = uqp.getQuestType().toString();
                    uq.complete = uqp.getComplete();
                    uq.mob_id = uqp.getMobId();
                    uq.node_id = uqp.getNodeId();
                    uq.updated_at = results.getString("updated_at");
                    responseGetUserQuests.user_quests.add(uqw);
                }
                responseGetUserQuests.updated_at = results.getTimestamp("updated_at");
            }
            else {
                responseGetUserQuests.updated_at = new Date();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return responseGetUserQuests;
    }

    @Override
    public boolean saveUserQuests(int user_id, List<UserQuestWrapper> user_quests) {
        UserQuestArrayProto user_quest_array = new UserQuestArrayProto();
        List<UserQuestProto> user_quests_protos = new ArrayList<UserQuestProto>();
        
        for (UserQuestWrapper uqw : user_quests) {
            UserQuest uq = uqw.user_quest;
            UserQuestProto uqp = new UserQuestProto();
            uqp.setQuestId(uq.id);
            uqp.setQuestType(UserQuestProto.QuestType.valueOf(uq.quest_type));
            uqp.setComplete(uq.complete);
            uqp.setMobId(uq.mob_id);
            uqp.setNodeId(uq.node_id);
            user_quests_protos.add(uqp);
        }
        user_quest_array.setUserQuestList(user_quests_protos);

        byte[] user_quest_data = ProtostuffIOUtil.toByteArray(user_quest_array, UserQuestArrayProto.getSchema(),
                                                              LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        boolean saved = false;
        Connection conn = null;
        try {
            conn = getConnection();

            PreparedStatement query = conn.prepareStatement("REPLACE INTO user_quests(data, user_id) VALUES(?,?)");
            query.setBytes(1, user_quest_data);
            query.setInt(2, user_id);
            query.executeUpdate();
            saved = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return saved;
    }

    public class ResponseGetUserQuests {
		public List<UserQuestWrapper> user_quests;
		public Date updated_at;
	}
}
