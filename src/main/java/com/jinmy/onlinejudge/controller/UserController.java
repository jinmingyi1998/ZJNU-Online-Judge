package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("user/register");
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        User u = new User(user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getSchool(), user.getCls());
        try {
            User t_user = userService.registerUser(u);
            if (t_user != null)
                return "success";
            else return "false";
        } catch (Exception e) {
            return "false";
        }
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("user/login");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.invalidate();
        return login();
    }

    @PostMapping("/login")
    public String loginUser(User user, HttpSession session, Model m) {
        User t_user = userService.loginUser(user);
        if (t_user != null) {
            session.setMaxInactiveInterval(30 * 60);
            session.setAttribute("currentUser", t_user);
            session.setAttribute("loginTime", new Date());
            return "success";
        }
        return "Username or password is wrong.用户名或密码错误。";
    }
}
