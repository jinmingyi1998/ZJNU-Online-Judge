package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.ContestProblem;
import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Tag;
import com.jinmy.onlinejudge.repository.ContestProblemRepository;
import com.jinmy.onlinejudge.service.ContestService;
import com.jinmy.onlinejudge.service.ProblemService;
import com.jinmy.onlinejudge.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    ContestProblemRepository contestProblemRepository;

    @GetMapping
    public ModelAndView index(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "problem", defaultValue = "") String search) {
        ModelAndView m = new ModelAndView("admin/admin");
        page = Math.max(page, 0);
        Page<Problem> problemPage = problemService.getAdminProblemPage(page, 40, search);
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
        ModelAndView m = new ModelAndView("admin/insertContest");
        return m;
    }

    @PostMapping("/contest/insert")
    public String insertContestAction(@RequestParam(value = "title") String title,
                                      @RequestParam(value = "description") String description,
                                      @RequestParam(value = "privilege", defaultValue = "public") String privilege,
                                      @RequestParam(value = "password", defaultValue = "") String password,
                                      @RequestParam(value = "startTime") String startTime,
                                      @RequestParam(value = "lastTime") String lastTime,
                                      @RequestParam(value = "list[]") String[] ids) {
        Contest contest = new Contest();
        contest.setTitle(title);
        contest.setCreateTime(Instant.now());
        contest.setDescription(description);
        contest.setPrivilege(privilege);
        contest.setStartTime(startTime);
        contest.setEndTime(startTime, lastTime);
        contest.setPassword(password);
        contest = contestService.insertContest(contest);
        List<ContestProblem> contestProblems = new ArrayList<>();
        Long _cnt = 1L;
        for (int i = 0; i < ids.length; i++) {
            String[] _s = ids[i].split("&", 2);
            Long id = Long.parseLong(_s[0]);
            Problem p = problemService.getProblemById(id);
            if (p != null) {
                contestProblems.add(new ContestProblem(p, _s[1], _cnt++));
            }
        }
        for (ContestProblem cp : contestProblems) {
            cp.setContest(contest);
            contestProblemRepository.save(cp);
        }
        return "success";
    }

    @GetMapping("/contest/get-problem-name/{id}")
    public String getProblemName(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        Problem problem = problemService.getProblemById(id);
        try {
            return problem.getTitle();
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