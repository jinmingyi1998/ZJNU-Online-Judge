package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Team;
import com.jinmy.onlinejudge.entity.TeamRole;
import com.jinmy.onlinejudge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRoleRepository extends JpaRepository<TeamRole, Long> {
    List<TeamRole> findAllByUser(User u);
    List<TeamRole>findAllByTeam(Team team);
}
