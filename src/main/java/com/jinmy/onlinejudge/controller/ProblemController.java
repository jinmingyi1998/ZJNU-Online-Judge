package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.JudgeService;
import com.jinmy.onlinejudge.service.ProblemService;
import com.jinmy.onlinejudge.service.SolutionService;
import com.jinmy.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @GetMapping("/showproblem")
    public ModelAndView showproblem(@RequestParam("id") Long id, HttpServletResponse response) {
        Problem problem = problemService.getProblemById(id);
        ModelAndView m = new ModelAndView("problem/showproblem");
        if (problem == null) {
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
    public String submitProblem(Model m, @PathVariable("id") Long id,
                                HttpServletResponse response, HttpServletRequest request,
                                @RequestParam(value = "language", defaultValue = "cpp") String language,
                                @RequestParam(value = "source") String source, @RequestParam(value = "share", defaultValue = "false") boolean share) {
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
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {// user doesn't login
            log.error("User not exist");
            return "Please Login";
        }
        if (!userService.isExist(user.getId())) {// user not found
            log.error("User not found");
            return "Please Login";
        }
        Problem problem = problemService.getProblemById(id);
        if (problem == null) {
            return "Problem Not Exist";
        }
        Solution solution = new Solution(user, problem, language, source, request.getRemoteAddr(), share);
        solution = solutionService.insertSolution(solution);
        judgeService.submit(solution);
        return "success";
    }

    @GetMapping("/problems")
    public ModelAndView showProblemList(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "search", defaultValue = "") String search) {
        if (search.length() > 0) {
            session.setAttribute("problem-search", search);
        }
        String _search;
        try {
            _search = (String) session.getAttribute("search");
        } catch (Exception e) {
            _search = "";
        }
        ModelAndView m = new ModelAndView("problem/problemlist");
        page = Math.max(page, 0);
        Page<Problem> problemPage = problemService.getProblemPage(page, PAGE_SIZE, _search);
        if (page >= problemPage.getTotalPages() && problemPage.getTotalPages() > 0) {
            page = problemPage.getTotalPages() - 1;
            problemPage = problemService.getProblemPage(page, PAGE_SIZE, search);
        }
        m.addObject("problems", problemPage);
        return m;
    }
}
