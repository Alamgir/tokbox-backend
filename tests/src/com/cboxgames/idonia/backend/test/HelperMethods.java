package com.cboxgames.idonia.backend.test;

public class HelperMethods {

	public static final String PASSWORD_DEFAULT = "password";
	
	/**
	 * Create a brand new user name.  The user name is based on the current epoch time so it will *always* be unique.
	 * 
	 * @return the newly generated user name.
	 */
	public static String getNewusername() {
		return "user" + System.currentTimeMillis();
	}
}
