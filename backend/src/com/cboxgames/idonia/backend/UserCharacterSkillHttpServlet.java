package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.CBoxLoggerSyslog;
import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.idonia.backend.commons.ResponseMessages;
import com.cboxgames.idonia.backend.commons.ResponseTools;
import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.UriToArgv;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacterskill.UserCharacterSkillDBSQL;
import com.cboxgames.idonia.backend.commons.db.skill.SkillDBSQL;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Skill;
import com.cboxgames.utils.idonia.types.Skill.*;
import com.cboxgames.utils.idonia.types.User;
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

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/16/11
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserCharacterSkillHttpServlet extends HttpServlet{

	private SqlDataSource _sql_data_source;
	private UserDBSQL _user_db_sql;
    private UserCharacterDBSQL _uc_db_sql;
    private UserCharacterSkillDBSQL _ucs_db_sql;
    private SkillDBSQL _s_db_sql;
    private ObjectMapper _mapper;

    public void init() {
    	
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        
        try {
            _user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _uc_db_sql = new UserCharacterDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _ucs_db_sql = new UserCharacterSkillDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _s_db_sql = new SkillDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
    	// URI: /user_character_skills/buy
    	String uri_str = request.getRequestURI();
       	if (UriToArgv.verifyUrl(uri_str, "user_character_skills", "buy") == false) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
    	InputStream stream = request.getInputStream();
        SkillBuy skill_buy = _mapper.readValue(stream, SkillBuy.class);

        HttpSession session = request.getSession();
        int user_id = (Integer) session.getAttribute("user_id");
        User user = _user_db_sql.getUserOnlyByID(user_id);
        Skill lookup_skill = _s_db_sql.getSkill(skill_buy.skill_id);
        Character user_character = _uc_db_sql.getUserCharacterByID(skill_buy.user_character_id).character;

        if (user_character.character_id != lookup_skill.character_id) {
			ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.SKILL_DOES_NOT_BELONG_TO_CHARACTER,
					Constants.SC_BAD_REQUEST);
            return;
        }

        //Check if the user_character already has the skill
        int in_use_count = 0;
        for (Skill user_character_skill : user_character.user_character_skills) {
            if (user_character_skill.skill_id == skill_buy.skill_id) {
            	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.SKILL_EXISTS,
    					Constants.SC_BAD_REQUEST);
                return;
            }
            if (user_character_skill.in_use) {
                in_use_count++;
            }
        }

        //Check if the user has sufficient money
        if (user.money < lookup_skill.cost) {
            ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INSUFFICIENT_MONEY,
					Constants.SC_BAD_REQUEST);
            return;
        }
       
        user.money -= lookup_skill.cost;
        if (_user_db_sql.updateUserOneField(user.money, "money", user_id) == false) {
        	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
            CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
        	return;
        }

        int id = _ucs_db_sql.createUserCharacterSkill(skill_buy, (in_use_count >= 5));
        if (id == -1) {
            response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
            CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_ucs_create",user.id);
            return;
        }
        
        // Write skill.id back to the client
        ResponseTools.prepareResponseJson(response, _mapper, id, Constants.SC_OK);
        CBoxLoggerSyslog.log("buy_skill","user_id","gold_spent","skill_name",user_id,lookup_skill.cost,lookup_skill.name);
    }
}
