package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestProblemRepository extends JpaRepository<ContestProblem, Long> {
    ContestProblem save(ContestProblem contestProblem);

    List<ContestProblem> findAllByContest(Contest contest);

}
