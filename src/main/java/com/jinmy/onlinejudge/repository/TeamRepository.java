package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);

    @Override
    Optional<Team> findById(Long aLong);
}
