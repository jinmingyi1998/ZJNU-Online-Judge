package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Tag;
import com.jinmy.onlinejudge.service.ContestService;
import com.jinmy.onlinejudge.service.ProblemService;
import com.jinmy.onlinejudge.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ProblemService problemService;
    @Autowired
    HttpSession session;
    @Autowired
    ContestService contestService;
    @Autowired
    TagService tagService;

    @GetMapping
    public ModelAndView index(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "search", defaultValue = "") String search) {
        ModelAndView m = new ModelAndView("admin/admin");
        page = Math.max(page, 0);
        Page<Problem> problemPage = problemService.getProblemPage(page, 40, search);
        m.addObject("problems", problemPage);
        return m;
    }

    @GetMapping("/insert")
    public ModelAndView insertProblem() {
        ModelAndView m = new ModelAndView("admin/insert");
        return m;
    }

    @PostMapping("/insert")
    public ModelAndView insertProblemAction(Problem problem, @RequestParam(value = "tag", defaultValue = "") String tag) {
        String[] tags = tag.split(",");
        ArrayList<Tag> t = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            Tag _tag = tagService.getTagByName(tags[i]);
            if (_tag != null) {
                t.add(_tag);
            }
        }
        problem.setTags(t);
        System.out.println(problem);
        problemService.insertProblem(problem);
        return insertProblem();
    }

    @GetMapping("/edit/{pid}")
    public ModelAndView editProblem(@PathVariable(value = "pid") Long id, HttpServletResponse response) {
        Problem problem = problemService.getProblemById(id);
        if (problem == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        ModelAndView m = new ModelAndView("admin/edit");
        m.addObject("problem", problem);
        return m;
    }

    @PutMapping("/edit/{pid}")
    public ModelAndView editProblemAction(Problem problem, @PathVariable(value = "pid") Long id, HttpServletResponse response) {
        problem.setId(id);
        problemService.updateProblem(problem);
        return editProblem(id, response);
    }

    @DeleteMapping("/delete/{pid}")
    public void deleteProblem(@PathVariable(value = "pid") Long id) {
        problemService.delete(id);
    }

    @GetMapping("/contest")
    public ModelAndView contests(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "title", defaultValue = "") String title) {
        ModelAndView m = new ModelAndView("admin/contests");
        page = Math.max(0, page);
        Page<Contest> contests = contestService.getContestPage(page, title);
        m.addObject("contests", contests);
        return m;
    }

    @GetMapping("/contest/insert")
    public ModelAndView insertContest() {
        ModelAndView m = new ModelAndView("admin/insert_contest");
        return m;
    }
}