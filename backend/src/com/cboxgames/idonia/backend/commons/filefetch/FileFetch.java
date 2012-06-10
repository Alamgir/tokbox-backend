package com.cboxgames.idonia.backend.commons.filefetch;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import javax.servlet.ServletContext;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/2/11
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileFetch {
	
    public static String getDataFromFile(ServletContext context, String file_path) {

    	String rets = null;
    	
        try {
        	StringBuilder text = new StringBuilder();
            String NL = System.getProperty("line.separator");
            InputStream stream = context.getResourceAsStream(file_path);
            if (stream != null) {
                Scanner scanner = new Scanner(stream , "UTF-8");
                try {
                    while (scanner.hasNextLine()) {
                        text.append(scanner.nextLine() + NL);
                    }
                    
                    rets = text.toString();
                }
                finally {
                    scanner.close();
                }
            }
            else {
                throw new FileNotFoundException("Could not find " + file_path);
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return rets;
    }
}

