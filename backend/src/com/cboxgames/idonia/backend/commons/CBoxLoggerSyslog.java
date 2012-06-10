package com.cboxgames.idonia.backend.commons;

import com.cboxgames.utils.tools.Security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConfigIF;

/**
 * A class containing helper methods to create messages to be sent to a Splunk cluster.
 * 
 * When calling any of the logging methods, the number of keys have to equal the number of values.
 * Each logging event also requires you to provide a name for the event.
 * 
 * @author Irwin
 *
 */
public class CBoxLoggerSyslog {

    private static Security sec;

	public final static boolean LOG_ENABLED = true;
	
	public final static String SYSLOG_PROTOCOL = "tcp";
	public final static String SYSLOG_HOST = "logs3.splunkstorm.com";
	public final static int SYSLOG_PORT = 20144;
	
	public final static String LOG_EVENT_NAME = "event_type";
	public final static String CLIENT_NAME = "client_name";
	
	public final static String LOG_EVENT_STARTUP = "startup";
	public final static String LOG_EVENT_EXCEPTION = "exception";
	
	public final static String LOGIN_FAIL_REASON_PASSWORD = "password";
	public final static String LOGIN_FAIL_REASON_ONLINE = "online";
	
	private static String client_name = "stack";
	
	/**
	 * Initialize the static logger.  This method must be called before any sort of logging can occur.
	 * 
	 * @param host
	 * @param port
	 */
	public static void initializeLogger(String host, int port, String protocol, String client_name) {
		SyslogConfigIF config = Syslog.getInstance(protocol).getConfig();
		config.setHost(host);
		config.setPort(port);
		CBoxLoggerSyslog.client_name = client_name;
        try {
            sec = new Security("FUCKYOURDAD");
        }
        catch (NoSuchAlgorithmException f) {
            f.printStackTrace();
        }
        catch (InvalidKeyException g) {
            g.printStackTrace();
        }
	}
	
	/**
	 * Log a server startup event.
	 * 
	 *
	 */
	public static void logStartUp() {
		String[] keys = new String[1];
		keys[0] = "start";
		
		Object[] values = new Object[1];
		values[0] = Boolean.TRUE;
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(LOG_EVENT_STARTUP, keys, values);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log an event with 1 parameter.
	 * 
	 */
	public static void log(String event_name, String k_1, Object v_1) {
		String[] keys = new String[1];
		keys[0] = k_1;
		
		Object[] vals = new Object[1];
		vals[0] = v_1;
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(event_name, keys, vals);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log an event with 2 parameters.
	 */
	public static void log(String event_name, String k_1, String k_2, Object v_1, Object v_2) {
		String[] keys = new String[2];
		keys[0] = k_1;
		keys[1] = k_2;
		
		Object[] vals = new Object[2];
		vals[0] = v_1;
		vals[1] = v_2;
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(event_name, keys, vals);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log an event with 3 parameters.
	 * 
	 */
	public static void log(String event_name, String k_1, String k_2, String k_3,
			Object v_1, Object v_2, Object v_3) {
		
		String[] keys = new String[3];
		keys[0] = k_1;
		keys[1] = k_2;
		keys[2] = k_3;
		
		Object[] vals = new Object[3];
		vals[0] = v_1;
		vals[1] = v_2;
		vals[2] = v_3;
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(event_name, keys, vals);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log an event with 4 parameters.
	 * 
	 */
	public static void log(String event_name, String k_1, String k_2, String k_3, String k_4,
			Object v_1, Object v_2, Object v_3, Object v_4) {
		
		String[] keys = new String[4];
		keys[0] = k_1;
		keys[1] = k_2;
		keys[2] = k_3;
		keys[3] = k_4;
		
		Object[] vals = new Object[4];
		vals[0] = v_1;
		vals[1] = v_2;
		vals[2] = v_3;
		vals[3] = v_4;
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(event_name, keys, vals);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log an event with a variable length of parameters.
	 * 
	 * keys.size() == vals.size()
	 * 
	 * @param event_name name of the event
	 * @param keys list of keys.
	 * @param vals list of corresponding values.
	 */
	public static void log(String event_name, List<String> keys, List<Object> vals) {
		assert keys.size() == vals.size() : "keys.size() != vals.size()";
		
		String[] keys_a = new String[keys.size()];
		int ctr = 0;
		for (String key :  keys) {
			keys_a[ctr] = key;
			ctr++;
		}
		
		ctr = 0;
		Object[] vals_a = new Object[vals.size()];
		for (Object obj : vals) {
			vals_a[ctr] = obj;
			ctr++;
		}
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(event_name, keys_a, vals_a);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log an event with a variable length of parameters.
	 * 
	 * keys.size() == vals.size()
	 * 
	 * @param event_name name of the event
	 * @param keys list of keys.
	 * @param vals list of corresponding values.
	 */
	public static void log(String event_name, String[] keys, Object[] vals) {
		assert keys.length == vals.length : "keys.size() != vals.size()";
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(event_name, keys, vals);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log an exception.
	 * 
	 * @param exception the exception to log.
	 * @param tag a tag to go along with the exception.
	 */
	public static void logException(Exception exception, String tag) {
		
		String[] keys = new String[3];
		keys[0] = "exception";
		keys[1] = "exception_tag";
		keys[2] = "exception_message";
		
		Object[] values = new Object[3];
		values[0] = exception.getClass().toString();
		values[1] = tag;
		values[2] = exception.getMessage();
		
		String message = CBoxLoggerSyslog.createKeyValuePairs(LOG_EVENT_EXCEPTION, keys, values);
		CBoxLoggerSyslog.logMessage(message);
	}
	
	/**
	 * Log a prepared message.
	 * 
	 * Do NOT use this method unless you know what you are doing.
	 * 
	 * @param message to log. 
	 */
	public static void logMessage(String message) {
		if (LOG_ENABLED)
			Syslog.getInstance(SYSLOG_PROTOCOL).info(message);
	}
	
	/**
	 * Construct a key-value pair string with the format key=value,key=value.
	 * 
	 * This method calls the toString() method on all values.
	 * 
	 * @param keys the keys.
	 * @param values the values that map to the keys.
	 * 
	 * @return the constructed string.
	 */
	private static String createKeyValuePairs(String event_name, String[] keys, Object[] values) {
		assert keys != null && values != null && keys.length == values.length && event_name != null
				: "invalid parameters.";
		
		StringBuilder builder = new StringBuilder();
		int length = keys.length;

        // The backslashes
        builder.append("\\\\\\");
		
		// Client name.
		builder.append(CLIENT_NAME);
		builder.append("=");
		builder.append(client_name);
		builder.append(",");
		
		// What type of event is this?
		builder.append(LOG_EVENT_NAME);
		builder.append("=");
		builder.append(event_name);
		builder.append(",");
		
		// At what time did this event happen?
		builder.append("time");
		builder.append("=");
		builder.append(System.currentTimeMillis());
		builder.append(",");
		
		for (int ctr = 0; ctr < length; ctr++) {
			builder.append(keys[ctr]);
			builder.append("=");
			builder.append(values[ctr].toString());
			builder.append(",");
		}
		
		// Guaranteed to have atleast one element.
		return sec.encodeString(builder.substring(0, builder.length() - 1));
	}
}