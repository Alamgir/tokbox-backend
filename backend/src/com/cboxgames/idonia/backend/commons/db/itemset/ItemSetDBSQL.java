package com.cboxgames.idonia.backend.commons.db.itemset;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.ItemSet;
import com.cboxgames.utils.idonia.types.ItemSet.*;


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
 * Date: 1/21/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemSetDBSQL extends DBSQL implements IItemSetDB{

    public ItemSetDBSQL(DataSource data_source, ServletContext servlet_context) throws SQLException {
        super(data_source, servlet_context);
    }

    @Override
    public ItemSetWrapper[] getItemSetDetails() {
        Connection conn = null;
        List<ItemSetWrapper> item_set_list = new ArrayList<ItemSetWrapper>();

        try {
            conn = getConnection();

            PreparedStatement get_sets = conn.prepareStatement("SELECT * FROM item_sets ORDER BY level asc, character_id asc");
            ResultSet set_results = get_sets.executeQuery();

            if (set_results != null) {
                while (set_results.next()) {
                    ItemSetWrapper itw = new ItemSetWrapper();
                    ItemSet it = itw.item_set;
                    it.id = set_results.getInt("id");
                    it.name = set_results.getString("name");
                    it.level = set_results.getInt("level");
                    it.character_id = set_results.getInt("character_id");
                    it.armor_accessory_id = set_results.getInt("armor_accessory_id");
                    it.helmet_accessory_id = set_results.getInt("helmet_accessory_id");
                    it.weapon_accessory_id = set_results.getInt("weapon_accessory_id");
                    item_set_list.add(itw);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }

        return (ItemSetWrapper[]) item_set_list.toArray(new ItemSetWrapper[0]);
    }
}
