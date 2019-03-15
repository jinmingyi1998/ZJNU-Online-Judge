package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserSpaceController {
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ModelAndView userSpace(HttpServletResponse response, @PathVariable Long uid) {
        try {
            ModelAndView m = new ModelAndView("user/userspace");
            @NotNull User user = userService.getUserById(uid);
            m.addObject("user", user);
            return m;
        } catch (Exception e) {
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
