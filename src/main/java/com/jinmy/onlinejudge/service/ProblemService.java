package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.repository.ProblemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProblemService {
    @Autowired
    private ProblemRepository problemRepository;

    public Problem getProblemById(Long id) {
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isPresent())
            return problem.get();
        return null;
    }

    @Transactional
    public List<Problem> getProblemList() {
        List<Problem> problemList = problemRepository.findAll();
        return problemList;
    }

    @Transactional
    public Problem updateProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    @Transactional
    public Problem insertProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    public Page<Problem> getProblemPage(int page, int size, String search) {
        Sort _sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, _sort);
        try {
            if (search.length() > 0) {
                try {
                    return problemRepository.findAllByIdOrTitleLike(pageable, Long.parseLong(search), search);
                } catch (NumberFormatException e) {
                    return problemRepository.findAllByTitleLike(pageable, "%" + search + "%");
                }
            }
        } catch (Exception e) {
            return problemRepository.findAll(pageable);
        }
        return problemRepository.findAll(pageable);
    }

    public void delete(Long id) {
        problemRepository.deleteById(id);
    }
}