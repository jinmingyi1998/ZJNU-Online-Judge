/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.*;
import com.jinmy.onlinejudge.service.*;
import com.jinmy.onlinejudge.util.Rank;
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
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/contest")
public class ContestController {
    @Autowired
    ContestService contestService;
    @Autowired
    HttpSession session;
    @Autowired
    ProblemService problemService;
    @Autowired
    SolutionService solutionService;
    @Autowired
    UserService userService;
    @Autowired
    JudgeService judgeService;

    @GetMapping
    public ModelAndView contestPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "title", defaultValue = "") String title) {
        ModelAndView m = new ModelAndView("contest/contests");
        page = Math.max(0, page);
        Page<Contest> contests = contestService.getContestPage(page, title);
        m.addObject("contests", contests);
        return m;
    }

    @GetMapping("/{id}")
    public ModelAndView contestInfo(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        ModelAndView m = new ModelAndView("contest/contestinfo");
        try {
            @NotNull Contest contest = contestService.getContestById(id);
            if (contest.getPrivilege().equals("private")) {
                if (session.getAttribute("contest" + contest.getId()) == null) {
                    return null;
                }
            }
            m.addObject("contest", contest);
            m.addObject("problems", contest.getProblems());
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

    @GetMapping("/rest/{cid}/problem/{pid}")
    public Problem getProblemByContestTempId(@PathVariable(value = "cid") Long cid,
                                             @PathVariable("pid") Long pid) {
        try {
            @NotNull Contest contest = contestService.getContestById(cid);
            for (ContestProblem cp : contest.getProblems()) {
                if (cp.getTempId() == pid) {
                    Problem p = cp.getProblem();
                    p.setId(cp.getTempId());
                    p.setTitle(cp.getTempTitle());
                    return p;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    @GetMapping("/rest/status/{cid}")
    public List<Solution> getUserStatus(@PathVariable("cid") Long cid, HttpServletResponse response) {
        try {
            @NotNull Contest contest = contestService.getContestById(cid);
            @NotNull User user = (User) session.getAttribute("currentUser");
            @NotNull List<Solution> solutions = solutionService.getSolutionsOfUserInContest(user, contest);
            for (Solution s : solutions) {
                for (ContestProblem cp : contest.getProblems()) {
                    if (cp.getProblem().getId() == s.getProblem().getId()) {
                        s.getProblem().setId(cp.getTempId());
                        break;
                    }
                }
            }
            return solutions;
        } catch (Exception e) {
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @PostMapping("/status/view/{id}")
    public Solution restfulShowSourceCodeInContest(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        try {
            Solution solution = solutionService.getSolutionById(id);
            Contest contest = contestService.getContestById(solution.getContest().getId());
            for (ContestProblem cp : contest.getProblems()) {
                if (cp.getProblem().getId() == solution.getProblem().getId()) {
                    solution.getProblem().setId(cp.getTempId());
                    break;
                }
            }
            User user = (User) session.getAttribute("currentUser");
            if (user != null && user.getId() == solution.getUser().getId()) {
                return solution;
            }
        } catch (Exception e) {
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @PostMapping("/status/share/{id}")
    public boolean setShare(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            User user = (User) session.getAttribute("currentUser");
            if (userService.isExist(user.getId())) {
                Solution solution = solutionService.getSolutionById(id);
                if (solution.getUser().equals(user)) {
                    solution.setShare(!solution.getShare());
                    solutionService.updateSolution(solution);
                    return solution.getShare();
                }
            }
        } catch (Exception e) {
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }


    @GetMapping("/rest/rank/{cid}")
    public Rank getRankOfContest(@PathVariable Long cid, HttpServletResponse response) {
        try {
            @NotNull Contest contest = contestService.getContestById(cid);
            @NotNull Rank rank = new Rank(contest);
            @NotNull List<Solution> solutions = solutionService.getSolutionsInContest(contest);
            rank.init(solutions);
            return rank;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/rest/{cid}/comments")
    public List<ContestComment> getCommentsOfContest(@PathVariable Long cid) {
        try {
            @NotNull Contest contest = contestService.getContestById(cid);
            return contest.getContestComments();
        } catch (Exception e) {
        }
        return null;
    }

    @PostMapping("/{cid}/submit/{pid}")
    public String submitProblemInContest(@PathVariable("pid") Long pid,
                                         @PathVariable("cid") Long cid,
                                         HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(value = "language", defaultValue = "cpp") String language,
                                         @RequestParam(value = "source") String source,
                                         @RequestParam(value = "share", defaultValue = "false") boolean share) {
        log.info("Submit:" + Date.from(Instant.now()));
        try {
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
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {// user doesn't login
                log.error("User not exist");
                return "Please Login";
            }
            if (!userService.isExist(user.getId())) {// user not found
                log.error("User not found");
                return "Please Login";
            }
            Contest contest = contestService.getContestById(cid);

            Problem problem = null;
            for (int i = 0; i < contest.getProblems().size(); i++) {
                if (contest.getProblems().get(i).getTempId() == pid) {
                    problem = contest.getProblems().get(i).getProblem();
                }
            }
            if (problem == null) {
                return "Problem Not Exist";
            }
            Solution solution = new Solution(user, problem, language, source, request.getRemoteAddr(), share);
            solution.setContest(contestService.getContestById(cid));
            solution = solutionService.insertSolution(solution);
            judgeService.submit(solution);
            return "success";
        } catch (Exception e) {
            try {
                response.sendRedirect("/404");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
