package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserSpaceController {
    @Autowired
    UserService userService;

    @GetMapping("/{uid}")
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

    @PostMapping("/{uid}")
    public String userUpdate(HttpServletResponse response, @PathVariable Long uid, User user, @RequestParam(value = "new_password") String password) {
        try {
            @NotNull User init_user = userService.getUserById(uid);
            if (password.equals(init_user.getPassword())) {
                if (!user.userValidator()) {
                    return "Format Wrong";
                }
                user.setSubmit(init_user.getSubmit());
                user.setSolve(init_user.getSolve());
                user.setPrivilege(init_user.getPrivilege());
                user.setBanned(init_user.getBanned());
                user.setRegistertime(init_user.getRegistertime());
                userService.saveOrUpdateUser(user);
                return "success";
            } else {
                return "Password Wrong";
            }
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return "";
    }

    @GetMapping("/rest/user/{id}")
    public User getUser(@PathVariable Long id) {
        @NotNull User user = userService.getUserById(id);
        return user;
    }

}
