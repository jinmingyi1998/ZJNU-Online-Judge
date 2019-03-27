package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Tag;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.entity.UserProblem;
import com.jinmy.onlinejudge.service.SolutionService;
import com.jinmy.onlinejudge.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserSpaceController {
    @Autowired
    UserService userService;
    @Autowired
    SolutionService solutionService;

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


    @GetMapping("/rest/pie/{uid}")
    public PieGraph getRadarGraph(@PathVariable(value = "uid") Long uid, HttpServletResponse response) {
        try {
            @NotNull User user = userService.getUserById(uid);
            PieGraph pieGraph = new PieGraph();
            return pieGraph;
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    @Data
    class Graph {
        Radargraph radarGraph = new Radargraph();
        PieGraph pieGraph = new PieGraph();
        int ratio = 0;
        int solve = 0;
        int submit = 0;

        public Graph(User user) {
            solve = user.getSolve();
            submit = user.getSubmit();
            ratio = (int) (1.0 * user.getSolve() / user.getSubmit() * 100);
            List<UserProblem> userProblems = solutionService.getUserSolvedProblem(user);
            pieGraph.prime = pieGraph.medium = pieGraph.advance = 0;
            for (UserProblem up : userProblems) {
                for (Tag t : up.getProblem().getTags()) {
                    if (t.getId() == 1l) {
                        pieGraph.prime++;
                    } else if (t.getId() == 2l) {
                        pieGraph.medium++;
                    } else if (t.getId() == 3l) {
                        pieGraph.advance++;
                    } else if (t.getName().equals("数据结构")) {
                        radarGraph.ds++;
                    } else if (t.getName().equals("动态规划")) {
                        radarGraph.dp++;
                    } else if (t.getName().equals("搜索")) {
                        radarGraph.search++;
                    } else if (t.getName().equals("数论")) {
                        radarGraph.math++;
                    } else if (t.getName().equals("图论")) {
                        radarGraph.graph++;
                    } else if (t.getName().equals("计算几何")) {
                        radarGraph.geometry++;
                    } else if (t.getName().equals("字符串")) {
                        radarGraph.string++;
                    }
                }
            }
        }
    }


    @Data
    class Radargraph {
        int ds;
        int string;
        int math;
        int search;
        int graph;
        int dp;
        int geometry;
    }

    @Data
    class PieGraph {
        int prime = 0;
        int medium = 0;
        int advance = 0;
    }

}
