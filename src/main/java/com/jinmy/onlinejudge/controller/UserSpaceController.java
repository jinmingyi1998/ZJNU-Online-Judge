package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Tag;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.entity.UserProblem;
import com.jinmy.onlinejudge.service.SolutionService;
import com.jinmy.onlinejudge.service.TagService;
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
    public Graph getGraph(@PathVariable(value = "uid") Long uid, HttpServletResponse response) {
        try {
            @NotNull User user = userService.getUserById(uid);
            Graph graph=new Graph(user);
            return graph;
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    @Autowired
    private TagService tagService;

    @Data
    class Graph {
        Radargraph radar = new Radargraph();
        PieGraph pie = new PieGraph();
        int ratio = 0;
        int solve = 0;
        int submit = 0;

        public Graph(User user) {
            solve = user.getSolve();
            submit = user.getSubmit();
            ratio = (int) (1.0 * user.getSolve() / user.getSubmit() * 100);
            List<UserProblem> userProblems = solutionService.getUserSolvedProblem(user);
            pie.prime = pie.medium = pie.advance = 0;
            for (UserProblem up : userProblems) {
                for (Tag t : up.getProblem().getTags()) {
                    if (t.getId() == 1l) {
                        pie.prime++;
                    } else if (t.getId() == 2l) {
                        pie.medium++;
                    } else if (t.getId() == 3l) {
                        pie.advance++;
                    } else if (t.getName().equals("数据结构")) {
                        radar.ds+=up.getProblem().getScore();
                    } else if (t.getName().equals("动态规划")) {
                        radar.dp+=up.getProblem().getScore();
                    } else if (t.getName().equals("搜索")) {
                        radar.search+=up.getProblem().getScore();
                    } else if (t.getName().equals("数论")) {
                        radar.math+=up.getProblem().getScore();
                    } else if (t.getName().equals("图论")) {
                        radar.graph+=up.getProblem().getScore();
                    } else if (t.getName().equals("计算几何")) {
                        radar.geometry+=up.getProblem().getScore();
                    } else if (t.getName().equals("字符串")) {
                        radar.string+=up.getProblem().getScore();
                    }
                }
            }
            radar.ds= (int) (100.0* radar.ds/tagService.getTagByName("数据结构").getScore());
            radar.dp= (int) (100.0* radar.dp/tagService.getTagByName("动态规划").getScore());
            radar.geometry= (int) (100.0* radar.geometry/tagService.getTagByName("计算几何").getScore());
            radar.string= (int) (100.0* radar.string/tagService.getTagByName("字符串").getScore());
            radar.graph= (int) (100.0* radar.graph/tagService.getTagByName("图论").getScore());
            radar.search= (int) (100.0* radar.search/tagService.getTagByName("搜索").getScore());
            radar.math= (int) (100.0* radar.math/tagService.getTagByName("数论").getScore());
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
