package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class MainController {
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @Autowired
    UserService userService;

    @GetMapping("/404")
    public ModelAndView notFound(){
        return new ModelAndView("404");
    }

    @GetMapping("/standing")
    public ModelAndView userStanding(@RequestParam(value = "page",defaultValue = "0") int page, HttpSession session) {
        ModelAndView m = new ModelAndView("Standing");
        page=Math.max(0,page);
        page=Math.min(9,page);
        List<User> userList = userService.userStanding();
        for(int i=0;i<userList.size();i++)
        {
            userList.get(i).setStanding(i+1);
        }
        Page<User>userPage= new PageImpl<>(userList, PageRequest.of(page, 50), userList.size());
        try {
            @NotNull User user = (User) session.getAttribute("currentUser");
            user.setStanding(-1);
            for(int i=0;i<userList.size();i++)
            {
                if(userList.get(i).getId()==user.getId()){
                    user.setStanding(i+1);
                    break;
                }
            }
            m.addObject("user", user);
        } catch (Exception e) {
        }
        m.addObject("userPage", userPage);
        return m;
    }
}
