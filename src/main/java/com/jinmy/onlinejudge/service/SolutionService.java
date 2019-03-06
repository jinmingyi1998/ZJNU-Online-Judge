package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.CompileError;
import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.CompileErrorRepository;
import com.jinmy.onlinejudge.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SolutionService {
    public static final String[] STATUS = {"Accepted", "Wrong Answer", "Compile Error",
            "Time Limit Exceed", "Memory Limit Exceed",
            "Runtime Error", "System Error"};
    @Autowired
    UserService userService;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private CompileErrorRepository compileErrorRepository;

    /**
     * @param id solution id
     * @return a solution or null(not found)
     */
    public Solution getSolutionById(Long id) {
        Optional<Solution> solution = solutionRepository.findById(id);
        if (solution.isPresent()) {
            Solution s = solution.get();
//            Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s);
//            if (ce.isPresent()) {
//                s.setCe(ce.get());
//            }
            return s;
        }
        return null;
    }

    @Transactional
    public Solution insertSolution(Solution solution) {
        return solutionRepository.save(solution);
    }

    public List<Solution> allSolutionFilterByUser(List<Solution> solutionList, User user) {
        if (solutionList == null) {
            solutionList = solutionRepository.findAllByUser(user);
//            for (Solution s : solutionList) {
//                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s);
//                if (ce.isPresent()) {
//                    s.setCe(ce.get());
//                }
//            }
        }
        for (int j = solutionList.size() - 1; j >= 0; j--) {
            if (solutionList.get(j).getUser().getId() != user.getId())
                solutionList.remove(j);
        }
        return solutionList;
    }

    public List<Solution> allSolutionFilterByResult(List<Solution> solutionList, String result) {
        for (int i = 0; i < SolutionService.STATUS.length; i++) {
            if (result.equals(SolutionService.STATUS[i])) {
                if (solutionList == null) {
                    solutionList = solutionRepository.findAllByResult(result);
//                    for (Solution s : solutionList) {
//                        Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
//                        if (ce.isPresent()) {
//                            s.setCe(ce.get());
//                        }
//                    }
                } else {
                    for (int j = solutionList.size() - 1; j >= 0; j--) {
                        if (!solutionList.get(j).getResult().equals(result))
                            solutionList.remove(j);
                    }
                }
                return solutionList;
            }
        }
        return solutionList;
    }

    public List<Solution> allSolutionFilterByProblem(List<Solution> solutionList, Problem problem) {
        if (null == solutionList) {
            solutionList = solutionRepository.findAllByProblem(problem);
//            for (Solution s : solutionList) {
//                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
//                if (ce.isPresent()) {
//                    s.setCe(ce.get());
//                }
//            }
        } else {
            for (int i = solutionList.size() - 1; i >= 0; i--) {
                if (solutionList.get(i).getProblem().getId() != problem.getId())
                    solutionList.remove(i);
            }
        }
        return solutionList;
    }

    /**
     * get a page by user id
     *
     * @param page
     * @param size
     * @param id   user id
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
}
