package com.cboxgames.idonia.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldTest extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("rawtypes")
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	System.out.println("URI: " + uri_str);
       
    	StringBuffer url_str = request.getRequestURL();
    	System.out.println("URL: " + url_str);
    	
    	System.out.println("Method: " + request.getMethod());
    	
    	System.out.println("Query string: " + request.getQueryString());
    	System.out.println("Path info: " + request.getPathInfo());
    	System.out.println("Path translated: " + request.getPathTranslated());
      	System.out.println("Context path: " + request.getContextPath());
      	System.out.println("ServletContext path: " + this.getServletContext().getContextPath());
      	
      	check(new Integer[0]);
    	
    	boolean first = true;
    	Enumeration<String> hdr_names = request.getHeaderNames();
    	StringBuffer buf = new StringBuffer("Header: ");
    	while (hdr_names.hasMoreElements()) {
    		if (first) {
    			// System.out.print(hdr_name);
    			first = false;
    		}
    		else {
    			// System.out.print(", " + hdr_name);
    			buf.append(", ");
    		}
    		String hdr_name = hdr_names.nextElement();
    		buf.append(hdr_name);
    	}
    	if (!first) {
    		// System.out.print("\n");
    		System.out.println(buf);
    	}
    	
    	Map<String, String[]> param_map = request.getParameterMap();
        Set<?> s = param_map.entrySet();
        Iterator<?> it = s.iterator();
        while (it.hasNext()) {
            // key=value separator this by Map.Entry to get key and value
            Map.Entry entry = (Map.Entry) it.next();

            // getKey is used to get key of Map
            String param = (String) entry.getKey();
            System.out.println("Param :" + param);

            // getValue is used to get value of key in Map
            String[] values = (String[]) entry.getValue();
            for (String value : values)
            	System.out.println("    Value :" + value);
        }
    	
    	Enumeration paramNames = request.getParameterNames();
    	while (paramNames.hasMoreElements()) {
    		String paramName = (String) paramNames.nextElement();
    	    System.out.print(paramName + ":");
    	    String[] paramValues = request.getParameterValues(paramName);
    	    if (paramValues.length == 1) {
    	        String paramValue = paramValues[0];
    	        if (paramValue.length() == 0)
    	        	System.out.println("<No Value>");
    	        else
    	        	System.out.println(paramValue);
    	    }
    	    else {
    	        for(int ix = 0; ix < paramValues.length; ix++)
    	        	System.out.println("    " + paramValues[ix]);
    	    }
    	}

    	PrintWriter out = response.getWriter();
    	// response.setContentType("application/json");
    	// print json string

        out.println("<html>");
        out.println("<head>");

        String title = "Hello, how are you?";

        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

        // note that all links are created to be relative. this
        // ensures that we can move the web application that this
        // servlet belongs to to a different place in the url
        // tree and not have any harmful side effects.

        // making these absolute till we work out the
        // addition of a PathInfo issue

        out.println("<a href=\"../helloworld.html\">");
        out.println("<img src=\"../images/code.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"view code\"></a>");
        out.println("<a href=\"../index.html\">");
        out.println("<img src=\"../images/return.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"return\"></a>");
        out.println("<h1>" + title + "</h1>");
        out.println("</body>");
        out.println("</html>");
    }
	
	private void check(Integer[] int_list) {
		
		if (int_list.length == 0) {
			System.out.println("Empty");
		}
		else System.out.println("Otherwise");
		
		for (Integer ix : int_list) {
			System.out.println(" ix = " + ix.intValue());
		}
		
		int purchase = 100;
		System.out.println("What is purchase*-1 = " + purchase*-1);
		System.out.println("What is -purchase = " + -purchase);
		
		purchase++;
	}
}