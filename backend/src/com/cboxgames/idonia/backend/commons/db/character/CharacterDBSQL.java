package com.cboxgames.idonia.backend.commons.db.character;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Character.CharacterWrapper;

public class CharacterDBSQL extends DBSQL implements ICharacterDB {

	public CharacterDBSQL(DataSource connection_pool,
			ServletContext servlet_context) throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public List<Character> getCharacters() {
		List<Character> characters = new ArrayList<Character>();
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM characters");
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Character ch = new Character();
				getCharacterFromResult(result, ch);
				characters.add(ch);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return characters;
	}

	@Override
	public Map<Integer, Character> getCharacters(List<Integer> ids) {
		Map<Integer, Character> map = new HashMap<Integer, Character>();
		int ids_size = ids.size();
		if (ids == null || ids_size <= 0)
			return map;
		
		Connection conn = null;
		try {
			conn = getConnection();
			
			StringBuilder query = new StringBuilder();
			query.append("SELECT * FROM characters WHERE ");
			boolean first = true;
			Iterator<Integer> itr = ids.iterator();
			while (itr.hasNext()) {
				if (!first) query.append(" OR ");
	        	else first = false;
				int id = itr.next();
	            query.append(" id = " + id);
			}
		
			PreparedStatement statement = conn.prepareStatement(query.toString());
			
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Character character = new Character();
				getCharacterFromResult(result, character);
				map.put(character.id, character);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return map;
	}

    @Override
    public Character getCharacterByID(int id) {
        Connection conn = null;
        Character character = new Character();
        try{
            conn = getConnection();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM characters WHERE id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if ((result != null) && result.next()) {
               getCharacterFromResult(result, character);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return character;
    }
    
    @Override
	public CharacterWrapper[] getCharacterDetails() {
		List<CharacterWrapper> cw_list = new ArrayList<CharacterWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM characters");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				CharacterWrapper cw = new CharacterWrapper();
				getCharacterFromResult(result, cw.character);
				cw_list.add(cw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (CharacterWrapper[]) cw_list.toArray(new CharacterWrapper[0]);
	}
    
    
    // static methods
    public static Map<Integer, String> getCharacterMap(Connection conn) throws SQLException { 	
    	PreparedStatement query = conn.prepareStatement("SELECT * FROM characters");
		ResultSet result = query.executeQuery();
		if (result == null) return null;
		
		Map<Integer, String> map = new HashMap<Integer, String>();

		while (result.next()) {
			map.put(result.getInt("id"), result.getString("name"));
		}
		
		return map;
    }
    
    public static Map<Integer, Character> getCharacterMap(List<Integer> ids, Connection conn) throws SQLException {
		Map<Integer, Character> map = new HashMap<Integer, Character>();
		int ids_size = ids.size();
		if (ids == null || ids_size <= 0)
			return map;
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM characters WHERE ");
		boolean first = true;
		Iterator<Integer> itr = ids.iterator();
		while (itr.hasNext()) {
			if (!first) query.append(" OR ");
        	else first = false;
			int id = itr.next();
            query.append(" id = " + id);
		}
		
		PreparedStatement statement = conn.prepareStatement(query.toString());
		
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			Character character = new Character();
			getCharacterFromResult(result, character);
			map.put(character.id, character);
		}

		return map;
	}
    
    private static void getCharacterFromResult(ResultSet result, Character ch) throws SQLException {		
		ch.agility = result.getInt("agility");
		ch.armor = result.getFloat("armor");
		ch.id = result.getInt("id");
		ch.description = result.getString("description");
		ch.name = result.getString("name");
		ch.level = result.getInt("level");
		ch.strength = result.getInt("strength");
		ch.dodge = result.getFloat("dodge");
		ch.physical_crit = result.getFloat("physical_crit");
		ch.spell_crit = result.getFloat("spell_crit");
		ch.vitality = result.getInt("vitality");
        ch.intelligence = result.getInt("intelligence");
		ch.experience = result.getInt("experience");
	}
}
