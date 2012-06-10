package com.cboxgames.idonia.backend.commons.authentication;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 11/23/11
 * Time: 10:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticateUser {
	
	public static String USER_ADMIN_KEY = "admin";

     //Checks to see if user is authenticated. This will run in the before filter to check if user is authorized and authenticated.
     public static boolean isAuthenticated(HttpServletRequest req) throws ServletException {
         String authhead=req.getHeader("Cookie");
         if (authhead == null) return false;

         HttpSession session = req.getSession();
         String sessionCookie = (String) session.getAttribute("Cookie");
         if (sessionCookie == null) return false;

         Cookie[] cookies = req.getCookies();
         if (cookies == null) return false;
         for (int i=0; i<cookies.length; i++) {
             if (sessionCookie.equals(cookies[i].getValue())) {
                 return true;
             }
         }
         return false;
     }
     
    /**
    * Is the request comming from an admin?
    *
    * @param request
    * @return
    * @throws ServletException
    */
    public static boolean isAdmin(HttpServletRequest request) throws ServletException {
        return isAuthenticated(request) && (request.getSession().getAttribute(USER_ADMIN_KEY) != null);
    }

    public static void authenticate(HttpServletRequest req, HttpServletResponse res) throws ServletException {

        //Start a new session and generate the cookie string
        HttpSession session = req.getSession();
        String cookieString = UUID.randomUUID().toString();
        session.setAttribute("Cookie", cookieString);

        //Create a cookie
        Cookie sessionCookie = new Cookie("cookie", cookieString);
        sessionCookie.setMaxAge(60*60*24*365*2);
        sessionCookie.setPath("/");

        //Send cookie in the response
        res.addCookie(sessionCookie);
    }
}
