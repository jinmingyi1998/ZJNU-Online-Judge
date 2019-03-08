package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestProblemRepository extends JpaRepository<ContestProblem, Long> {
    ContestProblem save(ContestProblem contestProblem);
}
