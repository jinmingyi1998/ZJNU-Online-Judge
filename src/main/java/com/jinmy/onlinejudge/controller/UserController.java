package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        User u=new User(user.getUsername(),user.getName(),user.getEmail(),user.getPassword(),user.getSchool(),user.getCls());
        User t_user = userService.registerUser(u);
        System.out.println(t_user);
        if (t_user != null)
            return "redirect:/";
        else
            return register();
    }

    @GetMapping("/login")
    public String login(Model m) {
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginUser(User user, HttpSession session,Model m) {
        User t_user = userService.loginUser(user);
        if (t_user != null) {
            session.setMaxInactiveInterval(30 * 60);
            session.setAttribute("currentUser", t_user);
            session.setAttribute("loginTime", new Date());
            String last_page= (String) session.getAttribute("last_page");
            if (last_page==null)
                return "redirect:/";
            return "redirect:/";
        }
        m.addAttribute("message","用户名或密码错误");
        return login(m);
    }
}
