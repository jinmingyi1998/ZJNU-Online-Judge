/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.TagRepository;
import com.jinmy.onlinejudge.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/problems")
@RestController
public class ProblemController {
    private final int PAGE_SIZE = 30;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private HttpSession session;
    @Autowired
    private JudgeService judgeService;
    @Autowired
    private UserService userService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserAuthorityService userAuthorityService;

    @GetMapping("/{id}")
    public ModelAndView showproblem(@PathVariable Long id, HttpServletResponse response) {
        Problem problem = problemService.getProblemById(id);
        ModelAndView m = new ModelAndView("problem/showproblem");
        if (problem == null || problem.getActive() == false) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } else
            m.addObject("problem", problemService.getProblemById(id));
        List<Solution> solutionList = new ArrayList<Solution>();
        if (session.getAttribute("currentUser") != null) {
            User user = (User) session.getAttribute("currentUser");
            solutionList = solutionService.getSolutionPage(0, 5, user).getContent();
        }
        m.addObject("solutions", solutionList);
        return m;
    }

    @PostMapping("/submit/{id}")
    public String submitProblem(@PathVariable("id") Long id,
                                HttpServletRequest request,
                                @RequestParam(value = "language", defaultValue = "cpp") String language,
                                @RequestParam(value = "source") String source,
                                @RequestParam(value = "share", defaultValue = "false") boolean share) {
        log.info("Submit:" + Date.from(Instant.now()));
        if (session.getAttribute("last_submit") != null) {
            Instant instant = (Instant) session.getAttribute("last_submit");
            if (Instant.now().minusSeconds(10).compareTo(instant) < 0) {
                return "Don't submit within 10 seconds";
            } else if (source.length() > 20000) {
                return "Source code too long";
            } else if (source.length() < 2) {
                return "Source code too short";
            }
        }
        session.setAttribute("last_submit", Instant.now());
        @NotNull User user = (User) session.getAttribute("currentUser");
        if (!userAuthorityService.isLogin(user)) {// user doesn't login
            log.error("User not exist");
            return "Please Login";
        }
        Problem problem = problemService.getProblemById(id);
        if (problem == null || problem.getActive() == false) {
            return "Problem Not Exist";
        }
        Solution solution = new Solution(user, problem, language, source, request.getRemoteAddr(), share);
        solution = solutionService.insertSolution(solution);
        judgeService.submit(solution);
        return "success";
    }

    @GetMapping
    public ModelAndView showProblemList(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "problem", defaultValue = "") String search) {
        ModelAndView m = new ModelAndView("problem/problemlist");
        page = Math.max(page, 0);
        Page<Problem> problemPage = problemService.getProblemPage(page, PAGE_SIZE, search);
        m.addObject("problems", problemPage);
        m.addObject("tags", tagRepository.findAll());
        return m;
    }

    @GetMapping("/status/top/{pid}")
    public List<Solution> getTopOfProblem(@PathVariable Long pid,HttpServletResponse response) {
        try{
            @NotNull Problem problem=problemService.getProblemById(pid);
            List <Solution> solutions=solutionService.getTop50OfProblem(problem);
            return solutions;
        }catch (Exception e){}
        try {
            response.sendRedirect("/404");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/tags")
    public ModelAndView showProblemListByTag(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "tag", defaultValue = "") String tagname) {
        ModelAndView m = new ModelAndView("problem/tagproblems");
        page = Math.max(page, 0);
        Page<Problem> problemPage = problemService.getTag(page, PAGE_SIZE, tagname);
        m.addObject("problems", problemPage);
        m.addObject("tag", tagRepository.findByName(tagname).get());
        m.addObject("tags", tagRepository.findAll());
        return m;
    }
}
