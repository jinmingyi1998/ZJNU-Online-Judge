package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    @Override
    Page<Contest> findAll(Pageable pageable);

    @Override
    List<Contest> findAll();

    @Override
    Optional<Contest> findById(Long aLong);

    Page<Contest> findByTitleLike(Pageable pageable, String title);

    Contest save(Contest contest);
}
