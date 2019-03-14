package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.util.JudgeQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JudgeService {
    @Autowired
    JudgeQueue judgeQueue;

    public void submit(Solution solution) {
        if (solution.getContest().getPattern().equals("acm"))
            judge(solution);
    }

    public void judge(Solution solution) {
        judgeQueue.solve(solution);
    }
}
