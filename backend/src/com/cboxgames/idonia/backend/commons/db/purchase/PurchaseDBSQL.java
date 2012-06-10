package com.cboxgames.idonia.backend.commons.db.purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.cboxgames.idonia.backend.commons.DBSQL;
import com.cboxgames.utils.idonia.types.Purchase;
import com.cboxgames.utils.idonia.types.Purchase.PurchaseWrapper;

public class PurchaseDBSQL extends DBSQL implements IPurchaseDB {

	public PurchaseDBSQL(DataSource connection_pool,
			ServletContext servlet_context) throws SQLException {
		super(connection_pool, servlet_context);
	}

	@Override
	public boolean incrementPurchase(String abbr) {
		
		Connection conn = null;
		boolean update = false;
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("UPDATE purchases SET total = total + 1 WHERE purchase_abbr = ?");
			statement.setString(1, abbr);
			
			statement.executeUpdate();
			
			update = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return update;
	}

	@Override
	public List<Purchase> getPurchases() {
		
		List<Purchase> purchases = new ArrayList<Purchase>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM purchases");
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Purchase p = new Purchase();
				getPurchaseFromResult(result, p);
				purchases.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return purchases;
	}
	
	@Override
	public PurchaseWrapper[] getPurchaseDetails() {
		
		List<PurchaseWrapper> pw_list = new ArrayList<PurchaseWrapper>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM purchases ORDER BY purchase_type asc , price asc, name asc");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				PurchaseWrapper pw = new PurchaseWrapper();
				getPurchaseFromResult(result, pw.purchase);
				pw_list.add(pw);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		
		return (PurchaseWrapper[]) pw_list.toArray(new PurchaseWrapper[0]);
	}

    @Override
    public List<Purchase> getTokenPurchases() {
        List<Purchase> purchases = new ArrayList<Purchase>();
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM purchases WHERE purchase_type = ? ");
            statement.setString(1, "Token");

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Purchase p = new Purchase();
				getPurchaseFromResult(result, p);
				purchases.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}

		return purchases;
    }

    @Override
    public Purchase getPurchaseByAbbr(String abbr) {
    	
        Connection conn = null;
        Purchase purchase = null;

        try {
            conn = getConnection();
            PreparedStatement query = conn.prepareStatement("SELECT * FROM purchases WHERE purchase_abbr = ? ");
            query.setString(1, abbr);

            ResultSet result = query.executeQuery();
            if ((result != null) && result.next()) {
                purchase = new Purchase();
                getPurchaseFromResult(result, purchase);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection(conn);
        }
        return purchase;
    }

    private void getPurchaseFromResult(ResultSet result, Purchase p) throws SQLException {
		
		p.id = result.getInt("id");
		p.total = result.getInt("total");
		p.purchase_abbr = result.getString("purchase_abbr");
		p.name = result.getString("name");
		p.description = result.getString("description");
		p.price = result.getFloat("price");
		p.purchase_type = result.getString("purchase_type");
	}
}
