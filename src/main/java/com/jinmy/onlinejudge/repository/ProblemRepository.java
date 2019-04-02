package com.jinmy.onlinejudge.repository;


import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    void deleteById(Long id);

    Page<Problem>findAllByTagsOrderByIdAsc(Tag tag,Pageable pageable);

    Problem save(Problem problem);

    List<Problem> findAll();

    Optional<Problem> findById(Long id);

    Optional<Problem> findByTitleEquals(String s);

    @Override
    Page<Problem> findAll(Pageable pageable);

    Page<Problem> findAllByActive(Pageable pageable, Boolean active);

    @Override
    <S extends Problem> Page<S> findAll(Example<S> example, Pageable pageable);

    Page<Problem> findAllByActiveAndIdOrTitleLike(Pageable pageable, Boolean active, Long id, String title);

    Page<Problem> findAllByActiveAndTitleLike(Pageable pageable, Boolean active, String title);
    Page<Problem> findAllByIdOrTitleLike(Pageable pageable, Long id, String title);

    Page<Problem> findAllByTitleLike(Pageable pageable, String title);
}
