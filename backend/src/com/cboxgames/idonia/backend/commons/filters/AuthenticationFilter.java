package com.cboxgames.idonia.backend.commons.filters;

import com.cboxgames.idonia.backend.commons.authentication.AuthenticateUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Array;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/14/11
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationFilter extends AuthenticateUser implements Filter {
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURL = request.getRequestURI();
        String context_path = request.getContextPath();
        String sub_requestURL = requestURL.substring(context_path.length());
        if (sub_requestURL.equals("/users/sign_in") ||
            sub_requestURL.equals("/versions/details") ||
            sub_requestURL.equals("/skills/details") ||
            sub_requestURL.equals("/purchases/details") ||
            sub_requestURL.equals("/accessory_skills/details") ||
            sub_requestURL.equals("/playlists/details") ||
            sub_requestURL.equals("/accessories/details") ||
            sub_requestURL.equals("/characters/details") ||
            sub_requestURL.equals("/nodes/details") ||
            sub_requestURL.equals("/mobs/details") ||
            sub_requestURL.equals("/item_sets/details") ||
            sub_requestURL.equals("/users/tapjoy") ||
            sub_requestURL.equals("/souls/details")) {
            filterChain.doFilter(request,response);
        }
        else {
            if (AuthenticateUser.isAuthenticated(request)) {
                filterChain.doFilter(request,servletResponse);
            }
            else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not authenticated");
            }
        }
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
