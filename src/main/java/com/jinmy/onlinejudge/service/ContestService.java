package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.ContestComment;
import com.jinmy.onlinejudge.repository.ContestCommentRepository;
import com.jinmy.onlinejudge.repository.ContestProblemRepository;
import com.jinmy.onlinejudge.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ContestService {
    private final int PAGE_SIZE = 30;
    @Autowired
    ContestRepository contestRepository;
    @Autowired
    ContestProblemRepository contestProblemRepository;
    @Autowired
    SolutionService solutionService;
    @Autowired
    ContestCommentRepository contestCommentRepository;

    public Page<Contest> getContestPage(int page, String title) {
        if (title.length() == 0)
            return contestRepository.findAll(PageRequest.of(page, PAGE_SIZE, new Sort(Sort.Direction.DESC, "startTime")));
        return contestRepository.findByTitleLike(PageRequest.of(page, PAGE_SIZE, new Sort(Sort.Direction.DESC, "startTime")), "%" + title + "%");
    }

    public Contest insertContest(Contest contest) {
        return contestRepository.save(contest);
    }

    @Transactional
    public Contest getContestById(Long id) {
        Optional<Contest> contest = contestRepository.findById(id);
        if (contest.isPresent()) {// initialize the contest
            //contest.get().setSolutions(solutionService.getSolutionsInContest(contest.get()));
            contest.get().setProblems(contestProblemRepository.findAllByContest(contest.get()));
            return contest.get();
        } else return null;
    }

    public List<ContestComment> getCommentsOfContest(Contest c) {
        List<ContestComment> contestComments = contestCommentRepository.findAllByContest(c);
        return contestComments;
    }
}
