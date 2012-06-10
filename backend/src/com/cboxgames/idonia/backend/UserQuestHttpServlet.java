package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.*;
import com.cboxgames.idonia.backend.commons.accessorygenerator.AccessoryGenerator;
import com.cboxgames.idonia.backend.commons.battle.PveBattle;
import com.cboxgames.idonia.backend.commons.db.battle.BattleDBSQL;
import com.cboxgames.idonia.backend.commons.db.mob.MobDBSQL;
import com.cboxgames.idonia.backend.commons.db.node.INodeDB;
import com.cboxgames.idonia.backend.commons.db.node.NodeDBSQL;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.userquest.UserQuestDBSQL.*;
import static com.cboxgames.idonia.backend.UserQuestHttpServlet.RequestType.*;

import com.cboxgames.idonia.backend.commons.db.userquest.UserQuestDBSQL;
import com.cboxgames.utils.idonia.types.*;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.UserQuest.*;
import com.cboxgames.utils.idonia.types.proto.UserQuestProto;
import com.cboxgames.utils.idonia.types.RewardResponse.CompletedQuestWrapper;
import com.cboxgames.utils.idonia.types.RewardResponse.CompletedQuest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/18/11
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserQuestHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private UserDBSQL _user_db_sql;
    private UserCharacterDBSQL _uc_db_sql;
    private UserQuestDBSQL _uq_db_sql;
    private NodeDBSQL _n_db_sql;
    private MobDBSQL _m_db_sql;
    private BattleDBSQL _b_db_sql;
    private ObjectMapper _mapper;

	@Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        
        try {
        	_user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _uc_db_sql = new UserCharacterDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _uq_db_sql = new UserQuestDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _n_db_sql = new NodeDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _m_db_sql = new MobDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _b_db_sql = new BattleDBSQL(_sql_data_source.get_data_source(), this.getServletContext());

        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_quests");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

		RequestType req_type = setPutRequestType(uta).getRequestType();
       	if (req_type == URT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	switch (req_type) {
	   		case URT_COMPLETE: {
                InputStream stream = request.getInputStream();
                QuestComplete quest_complete = _mapper.readValue(stream, QuestComplete.class);

                //Get the user to do various checks and operations
                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");
                
                ResponseGetUserQuests responseGetUserQuests = _uq_db_sql.getUserQuests(user_id);
                List<UserQuestWrapper> user_quests = responseGetUserQuests.user_quests;
                UserQuestWrapper user_quest_wrapper = null;
                for (UserQuestWrapper tmp : user_quests) {
                    if (tmp.user_quest.id != quest_complete.user_quest_id)
                    	continue;
                    user_quest_wrapper = tmp;
                    break;
                }
                
                if (user_quest_wrapper == null) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                	return;
                }
                
                User user = _user_db_sql.getUserByID(user_id);

                //Get user characters and level them up
                Character[] user_characters = user.user_characters;
                List<Character> uc_list = new ArrayList<Character>();
                for (Character uc : user_characters) {
                    uc.experience += 50*uc.level;
                    PveBattle.levelUpUserCharacter(uc, user);
                    uc_list.add(uc);
                }
                _uc_db_sql.saveUserCharacters(uc_list);
               
                Random rand = new Random();
                CompletedQuest completed_quest = new CompletedQuest();
                Accessory gen_acc = null;
                String quest_type =user_quest_wrapper.user_quest.quest_type;

                //Check for quest type to determine which reward to generate
                if (quest_type.equals("Booty")) {
                    //Check for full inventory and if Booty is already completed
                    Accessory[] inventory = user.user_character_accessories_inventory;
                    if (inventory != null && inventory.length == user.inventory_spots ) {
                        response.sendError(Constants.SC_BAD_REQUEST, ResponseMessages.ACCESSORY_INVENTORY_FULL);
                        return;
                    }
                    if (user_quest_wrapper.user_quest.complete) {
                        response.sendError(Constants.SC_BAD_REQUEST, ResponseMessages.ALREADY_COMPLETED_BOOTY);
                        return;
                    }

                    //Pick a random User Character for which to generate an accessory
                    Character rand_uc = user_characters[rand.nextInt(user_characters.length)];

                    gen_acc = AccessoryGenerator.generateAccessoryForCharacter(rand_uc.name, rand_uc.level,
                                                                                        Constants.UNIQUE);
                    gen_acc.user_character_id = 0;
                    gen_acc.user_id = user_id;

                    completed_quest.reward.accessory = gen_acc;
                    CBoxLoggerSyslog.log("reward","acc_rewarded",gen_acc.name);
                }
                else if (quest_type.equals("Boss")) {
                    int money = 300;
                    user.money += money;
                    completed_quest.reward.gold = money;
                    CBoxLoggerSyslog.log("reward","gold_rewarded",money);
                }
                else if (quest_type.equals("Node")) {
                    int token_reward = 2;
                    user.tokens += token_reward;
                    completed_quest.reward.tokens = token_reward;
                    CBoxLoggerSyslog.log("reward","tokens_rewarded",token_reward);
                }
                //Set the quest to Complete and save it
                user_quest_wrapper.user_quest.complete = true;
                completed_quest.user_quest_id = user_quest_wrapper.user_quest.id;
                _uq_db_sql.saveUserQuests(user_id, user_quests);

                //Update the user using BattleDBSQL
                if (_b_db_sql.updatePveUser(user, gen_acc, true )) {
                    CompletedQuestWrapper cqw = new CompletedQuestWrapper();
                    cqw.completed_quest = completed_quest;
                    ResponseTools.prepareResponseJson(response, _mapper, cqw, Constants.SC_OK);
                    CBoxLoggerSyslog.log("complete_quest","user_id","quest_type",user_id,quest_type);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

                break;
	   		}
	   		default: {
                throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
	   		}
	   	}
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_quests");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

		RequestType req_type = setGetRequestType(uta).getRequestType();
       	if (req_type == URT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	switch (req_type) {
	   		case URT_GENERATE: {
                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");
                boolean success = true;

                //Generate quests for the user if no quests were found or the found quests were too old
                ResponseGetUserQuests result = _uq_db_sql.getUserQuests(user_id);
                if ((result.user_quests.size() <= 0) || Utility.isOldData(result.updated_at)) {
                    List<Node> boss_nodes = _n_db_sql.getNodes(INodeDB.NodeType.Boss);
                    List<Node> booty_nodes = _n_db_sql.getNodes(INodeDB.NodeType.Booty);
                    List<Node> normal_nodes = _n_db_sql.getNodes(INodeDB.NodeType.Node);

                    Random rand = new Random();
                    int rand_boss = rand.nextInt(boss_nodes.size());
                    int rand_node = rand.nextInt(normal_nodes.size());
                    int booty_node = rand.nextInt(booty_nodes.size());

                    List<Mob> normal_mobs = _m_db_sql.getMobsByDifficulty("Normal");
                    int rand_mob = rand.nextInt(normal_mobs.size());
                    
                    UserQuestWrapper uqw_one = new UserQuestWrapper();
                    UserQuest quest_one = uqw_one.user_quest;
                    quest_one.quest_type = UserQuestProto.QuestType.Node.toString();
                    quest_one.complete = false;
                    quest_one.mob_id = normal_mobs.get(rand_mob).id;
                    quest_one.node_id = normal_nodes.get(rand_node).id;
                    quest_one.id = 1;

                    UserQuestWrapper uqw_two = new UserQuestWrapper();
                    UserQuest quest_two = uqw_two.user_quest;
                    quest_two.quest_type = UserQuestProto.QuestType.Boss.toString();
                    quest_two.complete = false;
                    quest_two.mob_id = 0;
                    quest_two.node_id = boss_nodes.get(rand_boss).id;
                    quest_two.id = 2;

                    UserQuestWrapper uqw_three = new UserQuestWrapper();
                    UserQuest quest_three = uqw_three.user_quest;
                    quest_three.quest_type = UserQuestProto.QuestType.Booty.toString();
                    quest_three.complete = false;
                    quest_three.mob_id = 0;
                    quest_three.node_id = booty_nodes.get(booty_node).id;
                    quest_three.id = 3;

                    List<UserQuestWrapper> user_quests = result.user_quests;
                    user_quests.clear();
                    user_quests.add(uqw_one);
                    user_quests.add(uqw_two);
                    user_quests.add(uqw_three);

                    success = _uq_db_sql.saveUserQuests(user_id, result.user_quests);
                }
                if (success) {
                   ResponseTools.prepareResponseJson(response, _mapper, result.user_quests, Constants.SC_OK);
                }
                else {
                   response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
                break;
	   		}
	   		default: {
                throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
	   		}
	   	}
    }

//    private int getUserCharacterMaximumLevel(Character[] user_characters) {
//		
//		int max = 0;
//		for (Character character : user_characters) {
//			if (character.level <= max) continue;
//			max = character.level;
//		}
//		
//		return max;
//	}

    public static enum RequestType {

    	URT_COMPLETE,
    	URT_GENERATE,
    	URT_UNDEFINED;

    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {

        	RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

           		if (indx >= argv.length) {
           			rt.setRequestType(URT_UNDEFINED); // user_character_accessories/****
                    return rt;
    	       	}
           		if (argv[indx].equals("complete_quest")) {
    	   			rt.setRequestType(URT_COMPLETE); // PUT 1 user
    	   			return rt;
    	    	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	return rt;
        }

        public static RequestTypeUserId setGetRequestType(UriToArgv uta) {

        	RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

           	if (indx >= argv.length) {
           		return rt; // UNDEFINED
           	}
           	if (argv[indx].equals("generate")) {
           		rt.setRequestType(URT_GENERATE); // POST
           		return rt;
           	}
       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.

        	return rt; // UNDEFINED
        }

    }

    public static class RequestTypeUserId {
    	public RequestTypeUserId(int aid, RequestType type) {
    		_acc_id = aid;
    		_req_type = type;
    	}
    	void setAccId(int id) { _acc_id = id; }
    	public int getAccId() { return _acc_id; }
    	void setRequestType(RequestType type) { _req_type = type; }
    	public RequestType getRequestType() { return _req_type; }

    	private int _acc_id;
    	private RequestType _req_type;
    }

}
