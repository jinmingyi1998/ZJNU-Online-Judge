package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/contest")
public class ContestController {
    @Autowired
    ContestService contestService;
    @Autowired
    HttpSession session;

    @GetMapping
    public ModelAndView contestPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "title", defaultValue = "") String title) {
        ModelAndView m = new ModelAndView("contest/contests");
        page = Math.max(0, page);
        Page<Contest> contests = contestService.getContestPage(page, title);
        m.addObject("contests", contests);
        return m;
    }

    @Transactional
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
}
