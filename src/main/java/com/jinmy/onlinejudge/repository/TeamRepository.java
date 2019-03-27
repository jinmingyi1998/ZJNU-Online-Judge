package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByNameLike(String name);
    Optional<Team>findByName(String name);

    Optional<Team> findById(Long aLong);

    List<Team> findAll();
}
