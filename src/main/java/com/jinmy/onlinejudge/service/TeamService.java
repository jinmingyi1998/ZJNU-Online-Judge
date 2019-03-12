package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Team;
import com.jinmy.onlinejudge.entity.TeamRole;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.TeamRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    @Autowired
    TeamRoleRepository teamRoleRepository;

    public List<Team> getTeamsOfUser(User u) {
        List<TeamRole> teamRoles = teamRoleRepository.findAllByUser(u);
        List<Team> teams = new ArrayList<>();
        for (TeamRole t : teamRoles) {
            teams.add(t.getTeam());
        }
        return teams;
    }
}
