package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@Service
public class UserAuthorityService {
    @Autowired
    HttpSession session;
    @Autowired
    UserService userService;

    public boolean isLogin(User user) {
        try {
            @NotNull User u = (User) session.getAttribute("currentUser");
            if (userService.isExist(u.getId()) && u.getId() == user.getId())
                return true;
        } catch (NullPointerException e) {
        }
        return false;
    }

    public boolean isLogin() {
        try {
            @NotNull User u = (User) session.getAttribute("currentUser");
            if (userService.isExist(u.getId()))
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isAdmin(User user) {
        User u = userService.getUserById(user.getId());
        if (u.getPrivilege() == User.Privilege.admin) return true;
        return false;
    }

    public boolean isTeacher(User user) {
        User u = userService.getUserById(user.getId());
        if (u.getPrivilege() == User.Privilege.admin ||
                u.getPrivilege() == User.Privilege.teacher)
            return true;
        return false;
    }


}
