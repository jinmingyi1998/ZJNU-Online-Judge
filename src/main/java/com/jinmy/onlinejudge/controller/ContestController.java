package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/contest")
public class ContestController {
    @Autowired
    ContestService contestService;

    @GetMapping("/")
    public ModelAndView contestPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "title", defaultValue = "") String title) {
        ModelAndView m = new ModelAndView("contest/contests");
        page = Math.max(0, page);
        Page<Contest> contests = contestService.getContestPage(page, title);
        m.addObject("contests", contests);
        return m;
    }
}
