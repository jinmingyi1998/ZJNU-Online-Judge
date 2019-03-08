package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.util.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {
    @Autowired
    UserService userService;
    @Autowired
    ProblemService problemService;
    @Autowired
    ContestService contestService;
    @Autowired
    SolutionService solutionService;

    public Rank getRank(Long contest_id) {
        try {
            Contest contest = contestService.getContestById(contest_id);
            Rank rk = new Rank(contest);
            List<Solution> solutions = contest.getSolutions();
            rk.init(solutions);
            return rk;
        } catch (Exception e) {
        }
        return null;
    }
}
