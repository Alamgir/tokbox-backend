package com.cboxgames.idonia.backend.commons.db.purchase;

import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Purchase;
import com.cboxgames.utils.idonia.types.Purchase.PurchaseWrapper;

public interface IPurchaseDB {

	/**
	 * Increment the total count for a purchase.
	 * 
	 * @param abbr the abbreviation of the purchase.
	 * 
	 * @return weather the operation was successful.
	 */
	public boolean incrementPurchase(String abbr);
	
	/**
	 * Get a list of all purchases.
	 * 
	 * @return
	 */
	public List<Purchase> getPurchases();
	
	/**
	 * Get an wrapper array of purchases.
	 * 
	 * @return
	 */
	public PurchaseWrapper[] getPurchaseDetails();

    public List<Purchase> getTokenPurchases();

    /**
     * Return a purchase by a corresponding abbreviation
     * @param abbr the purchase's abbreviation
     * @return
     */
    public Purchase getPurchaseByAbbr(String abbr);

}
