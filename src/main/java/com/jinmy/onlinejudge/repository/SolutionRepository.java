package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Solution save(Solution solution);

    Optional<Solution> findById(Long id);

    List<Solution> findAll();

    @Override
    Page<Solution> findAll(Pageable pageable);

    List<Solution> findAllByUserId(Long uid);

    Page<Solution> findAllByUserId(Pageable pageable, Long uid);

    List<Solution> findAllByResult(String result);
    List<Solution> findAllByProblemId(Long pid);

    Page<Solution> findAllByResult(Pageable pageable, String result);
}
