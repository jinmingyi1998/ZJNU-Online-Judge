package com.jinmy.onlinejudge.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Order(1)
public class BeforeRequestFilter implements Filter {

    @Autowired
    HttpSession session;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        session.setAttribute("last_page", ((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
