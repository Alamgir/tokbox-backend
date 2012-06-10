package com.cboxgames.idonia.backend.listener.APNS;

import com.cboxgames.idonia.backend.commons.SqlDataSource;
import com.cboxgames.idonia.backend.commons.db.purchase.PurchaseDBSQL;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.utils.idonia.types.Purchase;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

import javax.servlet.http.HttpServlet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/17/11
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class PurchaseSale extends HttpServlet implements Runnable{
    private UserDBSQL _user_db_sql;
    private PurchaseDBSQL _p_db_sql;
    private SqlDataSource _sql_data_source = new SqlDataSource();



    @Override
    public void run() {
        ApnsService service = APNS.newService().withCert("/src/com/cboxgames/idonia/backend/listener/APNS/APNScert.p12", "ff5c8bd950").withSandboxDestination().build();
        try {
            _user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _p_db_sql = new PurchaseDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        List<String> device_tokens = _user_db_sql.getDeviceTokens();
        List<Purchase> purchases = _p_db_sql.getTokenPurchases();

        //Randomly generated sale

        //Randomly generate # of items to be on sale
        int num_purchases = 0;
        double price_multiplier = 0.0;
        Map<String,Double> purchase_sale_map = new HashMap<String,Double>();
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        double chance = random.nextInt(10);
        if (chance < 5) {
            num_purchases = 3;
        }
        else if (chance >= 5 && chance < 8) {
            num_purchases = 2;
        }
        else if (chance >= 8) {
            num_purchases = 1;
        }

        for (int x=0; x<num_purchases; x++) {
            Purchase purchase = purchases.get(random.nextInt(purchases.size()-1));
            if (purchase.price <= 20) {
                price_multiplier = 0.5;
            }
            else if (purchase.price >20 && purchase.price <40) {
                price_multiplier = 0.3;
            }
            else if (purchase.price >=40) {
                price_multiplier = 0.1;
            }

            builder.append(purchase.name);
            if (x == num_purchases - 1) {
                builder.append(" (" + price_multiplier*100 + "% off)");
            }
            else {
                builder.append(" (" + price_multiplier*100 + "% off),  ");
            }

            purchase_sale_map.put(purchase.purchase_abbr,price_multiplier);
        }

        String purchase_string = builder.toString();


        //TODO: If the sale is updated in the DB, run the push notification

        for (String device_token : device_tokens) {
            String payload = APNS.newPayload().badge(num_purchases).customField("Idonia: Items on Sale!", purchase_string).actionKey("Check it out!").build();
            service.push(device_token,payload);
        }



    }
}
