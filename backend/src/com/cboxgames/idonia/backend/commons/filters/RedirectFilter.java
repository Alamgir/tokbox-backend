package com.cboxgames.idonia.backend.commons.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 2/28/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedirectFilter implements Filter {
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURL = request.getRequestURI();
        String context_path = request.getContextPath();
        String sub_requestURL = requestURL.substring(context_path.length());
        response.sendRedirect("https://idonia-backend.cboxgames.com" + sub_requestURL);

    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
