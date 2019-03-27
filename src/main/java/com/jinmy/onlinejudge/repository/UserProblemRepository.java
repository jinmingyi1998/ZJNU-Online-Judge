package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.entity.UserProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProblemRepository extends JpaRepository<UserProblem, Long> {
    @Override
    <S extends UserProblem> S save(S entity);

    Long countAllByUser(User user);

    Long countAllByProblem(Problem problem);

    Optional<UserProblem> findByUserAndProblem(User user, Problem problem);

    List<UserProblem>findAllByUser(User user);
    List<UserProblem>findAllByProblem(Problem problem);
}
