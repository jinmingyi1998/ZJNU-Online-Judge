package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.util.JudgeQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JudgeService {
    @Autowired
    JudgeQueue judgeQueue;
    @Autowired
    SolutionService solutionService;
    public void submit(Solution solution) {
        solution = solutionService.insertSolution(solution);
        solutionService.addSubmit(solution);
        try {
            if (solution.getContest().getPattern().equals("acm"))
                judge(solution);
        } catch (Exception e) {
            judge(solution);
        }
    }

    public void judge(Solution solution) {
        judgeQueue.solve(solution);
    }
}
