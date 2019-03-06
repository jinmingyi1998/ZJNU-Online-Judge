/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.CompileErrorRepository;
import com.jinmy.onlinejudge.service.ProblemService;
import com.jinmy.onlinejudge.service.SolutionService;
import com.jinmy.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/status")
public class StatusController {
    private static final int PAGE_SIZE = 25;
    @Autowired
    SolutionService solutionService;
    @Autowired
    UserService userService;
    @Autowired
    HttpSession session;
    @Autowired
    ProblemService problemService;
    @Autowired
    CompileErrorRepository compileErrorRepository;

    @GetMapping
    public ModelAndView showStatusList(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "username", defaultValue = "") String username,
                                       @RequestParam(value = "problem", defaultValue = "-1") Long pid,
                                       @RequestParam(value = "result", defaultValue = "") String result) {
        ModelAndView m = new ModelAndView("problem/status");
        page = Math.max(0, page);
        // receive username
        Page<Solution> solutions = solutionService.getStatus(userService.getUserByUsername(username),
                problemService.getProblemById(pid), result, page);
        m.addObject("solutions", solutions);
        return m;
    }

    @GetMapping("/view/{id}")
    public ModelAndView showSourceCode(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        ModelAndView m = new ModelAndView("problem/sourcecode");
        Solution solution = solutionService.getSolutionById(id);
        User user = (User) session.getAttribute("currentUser");
        if (solution != null && solution.getShare()) {
            m.addObject("solution", solution);
            return m;
        }
        if (user != null && user.getId() == solution.getUser().getId()) {
            m.addObject("solution", solution);
            return m;
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/view/{id}")
    public Solution restfulShowSourceCode(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        Solution solution = solutionService.getSolutionById(id);
        try {
            User user = (User) session.getAttribute("currentUser");
            if (solution != null && solution.getShare()) {
                return solution;
            }
            if (user != null && user.getId() == solution.getUser().getId()) {
                return solution;
            }
            solution.setSource("This Source Code Is Not Shared!");
            solution.setCe(null);
            return solution;
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping("/share/{id}")
    public boolean setShare(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            User user = (User) session.getAttribute("currentUser");
            if (userService.isExist(user.getId())) {
                Solution solution = solutionService.getSolutionById(id);
                solution.setShare(!solution.getShare());
                solutionService.updateSolution(solution);
                return solution.getShare();
            }
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }
}
