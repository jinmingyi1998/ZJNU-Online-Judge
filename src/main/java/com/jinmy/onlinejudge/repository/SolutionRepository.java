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
import org.springframework.data.domain.Example;
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

    List<Solution> findAllByContestOrderByIdAsc(Contest contest);

    Page<Solution> findAllByUser(Pageable pageable, User user);

    List<Solution>findTop5ByUserAndProblemOrderByIdDesc(User user,Problem problem);

    List<Solution> findAllByUser(User user, Sort sort);


    Page<Solution> findAllByProblem(Pageable pageable,Problem problem);

    Page<Solution> findAllByResult(Pageable pageable, String result);
    Page<Solution> findAllByProblemAndResult(Pageable pageable, Problem problem,String result);


    //Top 50
    List<Solution> findFirst50ByResultAndProblemOrderByTimeAsc(String res,Problem problem);


    @Override
    <S extends Solution> Page<S> findAll(Example<S> example, Pageable pageable);

    Page<Solution> findAllByUserAndResult(Pageable pageable, User user, String result);

    Page<Solution> findAllByUserAndProblemAndResult(Pageable pageable, User user, Problem problem, String result);

    Page<Solution> findAllByUserAndProblem(Pageable pageable, User user, Problem problem);


    //   Page<Solution> findAllByUserAndProblemAndResult( Pageable pageable, User user, Problem problem, String result);
}

