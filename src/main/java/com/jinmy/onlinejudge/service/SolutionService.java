package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.CompileError;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.CompileErrorRepository;
import com.jinmy.onlinejudge.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SolutionService {
    @Autowired
    UserService userService;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private CompileErrorRepository compileErrorRepository;
    public static final String[] STATUS = {"Accepted", "Wrong Answer", "Compile Error",
            "Time Limit Exceed", "Memory Limit Exceed",
            "Runtime Error", "System Error"};

    /**
     * @param id solution id
     * @return a solution or null(not found)
     */
    public Solution getSolutionById(Long id) {
        Optional<Solution> solution = solutionRepository.findById(id);
        if (solution.isPresent()) {
            Solution s = solution.get();
            Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(id);
            if (ce.isPresent()) {
                s.setCe(ce.get().getInfo());
            }
            s.setUsername(userService.getUserById(s.getUserId()).getUsername());
            return s;
        }
        return null;
    }

    @Transactional
    public Solution insertSolution(Solution solution) {
        return solutionRepository.save(solution);
    }

    public List<Solution> allSolutionFilterByUser(List<Solution> solutionList, Long id) {
        if (solutionList == null) {
            solutionList = solutionRepository.findAllByUserId(id);
            for (Solution s : solutionList) {
                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
                if (ce.isPresent()) {
                    s.setCe(ce.get().getInfo());
                }
                s.setUsername(userService.getUserById(s.getUserId()).getUsername());
            }
        }
        for (int j = solutionList.size() - 1; j >= 0; j--) {
            if (solutionList.get(j).getUserId() != id)
                solutionList.remove(j);
        }
        return solutionList;
    }

    public List<Solution> allSolutionFilterByResult(List<Solution> solutionList, String result) {
        for (int i = 0; i < SolutionService.STATUS.length; i++) {
            if (result.equals(SolutionService.STATUS[i])) {
                if (solutionList == null) {
                    solutionList = solutionRepository.findAllByResult(result);
                    for (Solution s : solutionList) {
                        Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
                        if (ce.isPresent()) {
                            s.setCe(ce.get().getInfo());
                        }
                        s.setUsername(userService.getUserById(s.getUserId()).getUsername());
                    }
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

    public List<Solution> allSolutionFilterByProblem(List<Solution> solutionList, Long pid) {
        if (null == solutionList) {
            solutionList = solutionRepository.findAllByProblemId(pid);
            for (Solution s : solutionList) {
                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
                if (ce.isPresent()) {
                    s.setCe(ce.get().getInfo());
                }
                s.setUsername(userService.getUserById(s.getUserId()).getUsername());
            }
        } else {
            for (int i = solutionList.size() - 1; i >= 0; i--) {
                if (solutionList.get(i).getProblemId() != pid)
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
    public Page<Solution> getSolutionPage(int page, int size, Long id) {
        Sort _sort = new Sort(Sort.Direction.DESC, "submitTime");
        Page<Solution> solutionPage = solutionRepository.findAllByUserId(new PageRequest(page, size, _sort), id);
        for (Solution s : solutionPage.getContent()) {
            if (s.getResult().equals("Compile Error")) {
                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
                if (ce.isPresent()) s.setCe(ce.get().getInfo());
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
        for (Solution s : solutionPage.getContent()) {
            if (s.getResult().equals("Compile Error")) {
                Optional<CompileError> ce = compileErrorRepository.findCompileErrorBySolution(s.getId());
                if (ce.isPresent()) s.setCe(ce.get().getInfo());
            }
            s.setUsername(userService.getUserById(s.getUserId()).getUsername());
        }
        return solutionPage;
    }

    @Transactional
    public Solution updateSolution(Solution solution) {
        return solutionRepository.save(solution);
    }
}
