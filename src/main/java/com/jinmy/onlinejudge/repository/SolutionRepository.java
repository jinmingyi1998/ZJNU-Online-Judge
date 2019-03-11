/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Solution;
import com.jinmy.onlinejudge.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long>, QueryByExampleExecutor<Solution> {

    Solution save(Solution solution);

    Optional<Solution> findById(Long id);

    List<Solution> findAll();

    @Override
    Page<Solution> findAll(Pageable pageable);

    List<Solution> findAllByUserAndContest(User u, Contest contest);

    List<Solution> findAllByContest(Contest contest, Sort sort);

    Page<Solution> findAllByUser(Pageable pageable, User user);

    List<Solution> findAllByResult(String result);

    List<Solution> findAllByProblem(Problem problem);

    Page<Solution> findAllByResult(Pageable pageable, String result);

    //   Page<Solution> findAllByUserAndProblemAndResult( Pageable pageable, User user, Problem problem, String result);
}

