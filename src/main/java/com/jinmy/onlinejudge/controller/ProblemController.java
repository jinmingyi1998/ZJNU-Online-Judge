/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.*;
import com.jinmy.onlinejudge.repository.ArticleRepository;
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
        List<Solution> solutionList = new ArrayList<>();
        if (session.getAttribute("currentUser") != null) {
            User user = (User) session.getAttribute("currentUser");
            solutionList = solutionService.getTop5ProblemSolution(user, problem);
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
        try {
            User user = (User) session.getAttribute("currentUser");
            if (!userAuthorityService.isLogin(user)) {// user doesn't login
                log.error("User not exist");
                return "Please Login";
            }
            Problem problem = problemService.getProblemById(id);
            if (problem == null || problem.getActive() == false) {
                return "Problem Not Exist";
            }
            //null检验完成

            Solution solution = new Solution(user, problem, language, source, request.getRemoteAddr(), share);

            judgeService.submit(solution);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
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
    public List<Solution> getTopOfProblem(@PathVariable Long pid, HttpServletResponse response) {
        try {
            @NotNull Problem problem = problemService.getProblemById(pid);
            List<Solution> solutions = solutionService.getTop50OfProblem(problem);
            return solutions;
        } catch (Exception e) {
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/tags")
    public ModelAndView showProblemListByTag(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "tag", defaultValue = "") String tagname, HttpServletResponse response) {
        ModelAndView m = new ModelAndView("problem/tagproblems");
        try {
            page = Math.max(page, 0);
            Page<Problem> problemPage = problemService.getByTagName(page, PAGE_SIZE, tagname);
            Tag tag = tagRepository.findByName(tagname).get();
            tag.setProblems(problemPage.getContent());
            m.addObject("problems", problemPage);
            m.addObject("tag", tag);
            m.addObject("tags", tagRepository.findAll());
            return m;
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    @GetMapping("/rest/usersolved/{uid}/{pid}")
    public String userHasSolvedProblem(@PathVariable("uid") Long uid, @PathVariable(value = "pid") Long pid) {
        User user = userService.getUserById(uid);
        Problem problem = problemService.getProblemById(pid);
        try {
            solutionService.getUserProblem(user, problem);
            return "success";
        } catch (NullPointerException e) {
            return "false";
        }
    }

    @GetMapping("/rest/{pid}")
    public Problem getRestProblem(@PathVariable("pid") Long id, HttpServletResponse response) {
        try {
            @NotNull Problem problem = problemService.getProblemById(id);
            return problem;
        } catch (NullPointerException e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

    @GetMapping("/article/{pid}")
    public ModelAndView problemArticle(@PathVariable(value = "pid") Long pid, HttpServletResponse response) {
        ModelAndView m = new ModelAndView("problem/article");
        try {
            @NotNull Problem problem = problemService.getProblemById(pid);
            List<Article> articles = problemService.getArticlesOfProblem(problem);
            m.addObject("articles", articles);
            m.addObject("problem", problem);
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        return m;
    }

    @GetMapping("/rest/article/{pid}")
    public List<Article> getRestArticleById(@PathVariable("pid")Long pid)
    {
        @NotNull Problem problem = problemService.getProblemById(pid);
        List<Article> articles = problemService.getArticlesOfProblem(problem);
        return articles;
    }

    @Autowired
    ArticleRepository articleRepository;

    @PostMapping("/article/{pid}")
    public ModelAndView postArticle(@PathVariable("pid") Long pid, HttpServletResponse response, Article article) {
        if (!userAuthorityService.isLogin()) {
            try {
                response.sendRedirect("/404");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        User user = (User) session.getAttribute("currentUser");
        Problem problem = problemService.getProblemById(pid);
        article.setProblem(problem);
        article.setAuthor(user);
        article.setPostTime(Instant.now());
        articleRepository.save(article);
        try {
            response.sendRedirect("/problems/article/"+pid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return problemArticle(pid, response);
    }
}
