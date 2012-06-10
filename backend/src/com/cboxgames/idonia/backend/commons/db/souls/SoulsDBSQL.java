package com.cboxgames.idonia.backend.commons.db.souls;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Soul;
import com.cboxgames.utils.idonia.types.Soul.*;


import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 1/11/12
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SoulsDBSQL extends DBSQL implements ISoulsDB {

    public SoulsDBSQL( DataSource connection_pool, ServletContext servlet_context) throws SQLException {
        super(connection_pool, servlet_context);
    }
    @Override
    public Soul.SoulWrapper[] getSoulsDetails() {
        List<SoulWrapper> sw_list = new ArrayList<SoulWrapper>();
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM souls");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				SoulWrapper sw = new SoulWrapper();
				getSoulFromResult(result, sw.soul);
				sw_list.add(sw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}

		if (sw_list.isEmpty())
			return null;

		return (SoulWrapper[]) sw_list.toArray(new SoulWrapper[0]);
    }

    @Override
    public Soul getSoulByID(int soul_id) {
        Connection conn = null;
        Soul sw = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM souls WHERE id = ?");
            statement.setInt(1, soul_id);
			ResultSet result = statement.executeQuery();
            if (result != null) {
                while (result.next()) {
                    sw = new Soul();
                    getSoulFromResult(result, sw);
                }
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
        return sw;
    }

    private static void getSoulFromResult(ResultSet result, Soul soul) throws SQLException {
        soul.id = result.getInt("id");
		soul.name = result.getString("name");
        soul.cost = result.getInt("cost");
        soul.payout = result.getInt("payout");
        soul.time_limit = result.getInt("time_limit");
	}
}
