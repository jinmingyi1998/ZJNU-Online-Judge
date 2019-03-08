/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.ContestComment;
import com.jinmy.onlinejudge.entity.ContestProblem;
import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

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

    @GetMapping("/rest/contest/{cid}/problem/{pid}")
    public Problem getProblemByContestTempId(@PathVariable(value = "cid") Long cid,
                                             @PathVariable("pid") Long pid) {
        try {
            @NotNull Contest contest = contestService.getContestById(cid);
            for (ContestProblem cp : contest.getProblems()) {
                if (cp.getTempId() == pid) {
                    return cp.getProblem();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    @GetMapping("/rest/contest/{cid}/comments")
    public List<ContestComment>getCommentsOfContest(@PathVariable Long cid){
        try
        {
            @NotNull Contest contest=contestService.getContestById(cid);
            return contest.getContestComments();
        }catch (Exception e){}
        return null;
    }

}
