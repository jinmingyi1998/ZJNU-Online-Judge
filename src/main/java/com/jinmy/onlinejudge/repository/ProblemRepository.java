package com.jinmy.onlinejudge.repository;


import com.jinmy.onlinejudge.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem,Long> {

    void deleteById(Long id);
    Problem save(Problem problem);
    List<Problem> findAll();
    Optional<Problem> findById(Long id);
    Optional<Problem> findByTitleEquals(String s);

    @Override
    Page<Problem> findAll(Pageable pageable);

    Page<Problem> findAllByIdOrTitleLike(Pageable pageable,Long id,String title);
    Page<Problem> findAllByTitleLike(Pageable pageable,String title);
}
