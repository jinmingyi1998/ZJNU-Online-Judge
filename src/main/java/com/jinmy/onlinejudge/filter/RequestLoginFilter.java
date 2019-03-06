package com.jinmy.onlinejudge.filter;

import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class RequestLoginFilter implements Filter {
    UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        User user = null;
        if (session.getAttribute("currentUser") != null) {
            user = (User) session.getAttribute("currentUser");
        }
        if (user == null) {
            ((HttpServletResponse) response).sendRedirect("/login");
            return;
        }
        if (!userService.isExist(user.getId())) {
            log.error("User doesn't exist");
            ((HttpServletResponse) response).sendRedirect("/login");
            return;
        }
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext sc = filterConfig.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        userService = context.getBean(UserService.class);
    }

    @Override
    public void destroy() {
    }
}
