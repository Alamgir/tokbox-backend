package com.cboxgames.idonia.backend.commons.filefetch;

import javax.servlet.ServletContext;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/2/11
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFileFetch {
    public String getDataFromFile(ServletContext context, String file_path);
}
