package com.cboxgames.idonia.backend.commons.db.souls;

import com.cboxgames.utils.idonia.types.Soul;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 1/11/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISoulsDB {
    public Soul.SoulWrapper[] getSoulsDetails();

    public Soul getSoulByID(int soul_id);
}
