/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.*;
import com.jinmy.onlinejudge.repository.CompileErrorRepository;
import com.jinmy.onlinejudge.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SolutionService {
    public static final String[] STATUS = {"Accepted", "Wrong Answer", "Compile Error",
            "Time Limit Exceed", "Memory Limit Exceed",
            "Runtime Error", "System Error"};
    private final int PAGE_SIZE = 30;
    @Autowired
    UserService userService;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private CompileErrorRepository compileErrorRepository;
    @Autowired
    private ContestService contestService;

    /**
     * @param id solution id
     * @return a solution or null(not found)
     */
    public Solution getSolutionById(Long id) {
        Optional<Solution> solution = solutionRepository.findById(id);
        if (solution.isPresent()) {
            Solution s = solution.get();
            return s;
        }
        return null;
    }

    @Transactional
    public Solution insertSolution(Solution solution) {
        return solutionRepository.save(solution);
    }


    public Page<Solution> getStatus(User user, Problem problem, String result, int page) {
        Solution s = new Solution();
        s.setUser(user);
        s.setProblem(problem);
        s.setResult(result);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Solution> example = Example.of(s, exampleMatcher);
        return solutionRepository.findAll(example, PageRequest.of(page, PAGE_SIZE, new Sort(Sort.Direction.DESC, "id")));
        //return solutionExampleExecutor.findAll(example, );
    }

    /**
     * get a page by user id
     *
     * @param page
     * @param size
     * @param user
     * @return a page
     */
    public Page<Solution> getSolutionPage(int page, int size, User user) {
        Sort _sort = new Sort(Sort.Direction.DESC, "submitTime");
        Page<Solution> solutionPage = solutionRepository.findAllByUser(new PageRequest(page, size, _sort), user);
        for (Solution s : solutionPage.getContent()) {
            if (s.getResult().equals("Compile Error")) {
                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s);
                if (ce.isPresent()) s.setCe(ce.get());
            }
        }
        return solutionPage;
    }

    public List<Solution> getSolutionsOfUser(User user) {
        return solutionRepository.findAllByUser(user, new Sort(Sort.Direction.DESC, "id"));
    }

    /**
     * get a page with sort
     *
     * @param page
     * @param size
     * @return
     */
    public Page<Solution> getSolutionPage(int page, int size) {
        Sort _sort = new Sort(Sort.Direction.DESC, "id");
        Page<Solution> solutionPage = solutionRepository.findAll(new PageRequest(page, size, _sort));
//        for (Solution s : solutionPage.getContent()) {
//            if (s.getResult().equals("Compile Error")) {
//                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
//                if (ce.isPresent()) s.setCe(ce.get());
//            }
//        }
        return solutionPage;
    }

    @Transactional
    public Solution updateSolution(Solution solution) {
        return solutionRepository.save(solution);
    }


    public List<Solution> getSolutionsOfUserInContest(User u, Contest c) {
        List<Solution> solutions = solutionRepository.findAllByUserAndContest(u, c);
        return solutions;
    }

    public List<Solution> getSolutionsInContest(Contest contest) {
        return solutionRepository.findAllByContest(contest, new Sort(Sort.Direction.ASC, "submitTime"));
    }
}
