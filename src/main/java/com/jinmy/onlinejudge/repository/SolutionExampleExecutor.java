package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Solution;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface SolutionExampleExecutor extends JpaRepository<Solution, Long>, QueryByExampleExecutor<Solution> {
    @Override
    <S extends Solution> Page<S> findAll(Example<S> example, Pageable pageable);
}
