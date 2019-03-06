package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ProblemService problemService;
    @Autowired
    HttpSession session;

    @GetMapping
    public ModelAndView index(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "search", defaultValue = "") String search) {
        if (search.length() > 0) {
            session.setAttribute("problem-search", search);
        }
        String _search;
        try {
            _search = (String) session.getAttribute("search");
        } catch (Exception e) {
            _search = "";
        }
        Page<Problem> problemPage = problemService.getProblemPage(page, 20, _search);
        ModelAndView m = new ModelAndView("admin/admin");
        m.addObject("problems", problemPage);
        return m;
    }

    @GetMapping("/insert")
    public ModelAndView insertProblem() {
        ModelAndView m = new ModelAndView("admin/insert");
        return m;
    }

    @PostMapping("/insert")
    public ModelAndView insertProblemAction(Problem problem) {
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
}
