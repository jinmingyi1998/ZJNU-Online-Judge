package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
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

    List<Solution> findAllByUser(User u);

    Page<Solution> findAllByUser(Pageable pageable, User user);

    List<Solution> findAllByResult(String result);

    List<Solution> findAllByProblem(Problem problem);

    Page<Solution> findAllByResult(Pageable pageable, String result);
}
