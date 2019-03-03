package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.CompileErrorRepository;
import com.jinmy.onlinejudge.service.SolutionService;
import com.jinmy.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

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
    CompileErrorRepository compileErrorRepository;

    @GetMapping
    public ModelAndView showStatusList(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "username", defaultValue = "") String username,
                                       @RequestParam(value = "problem", defaultValue = "-1") Long pid,
                                       @RequestParam(value = "result", defaultValue = "") String result) {
        ModelAndView m = new ModelAndView("problem/status");
        page = Math.max(0, page);
        List<Solution> solutionList = null;
        // receive username
        if (username.length() > 0) {
            session.setAttribute("status-username", username);
        }
        if ((username = (String) session.getAttribute("status-username")) != null) {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                solutionList = solutionService.allSolutionFilterByUser(solutionList, user.getId());
            }
        }
        //receive result
        if (result.length() > 0) {
            session.setAttribute("status-result", result);
        }
        if ((result = (String) session.getAttribute("status-result")) != null) {
            solutionList = solutionService.allSolutionFilterByResult(solutionList, result);
        }
        //recieve problem
        if (pid >= 0) {
            session.setAttribute("status-pid", pid);
        }
        if ((pid = (Long) session.getAttribute("status-pid")) != null) {
            solutionList = solutionService.allSolutionFilterByProblem(solutionList, pid);
        }
        if (solutionList == null) {
            Page<Solution> solutionPage = solutionService.getSolutionPage(page, PAGE_SIZE);
            m.addObject("solutions", solutionPage);
        } else {
            int _page = solutionList.size() / PAGE_SIZE;
            if (solutionList.size() % PAGE_SIZE != 0)
                _page++;
            page = Math.min(page, _page);
            Page<Solution> solutionPage = new PageImpl<>(solutionList, new PageRequest(page, PAGE_SIZE, Sort.Direction.DESC, "id"), solutionList.size());
            m.addObject("solutions", solutionPage);
        }
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
        if (user != null && user.getId() == solution.getUserId()) {
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
        User user = (User) session.getAttribute("currentUser");
        if (solution != null && solution.getShare()) {
            return solution;
        }
        if (user != null && user.getId() == solution.getUserId()) {
            return solution;
        }
        solution.setSource("This Source Code Is Not Shared!");
        solution.setCe("");
        solution.setUsername(userService.getUserById(solution.getUserId()).getUsername());
        return solution;
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
