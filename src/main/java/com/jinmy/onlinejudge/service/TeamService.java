package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Team;
import com.jinmy.onlinejudge.entity.TeamRole;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.TeamRepository;
import com.jinmy.onlinejudge.repository.TeamRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    TeamRoleRepository teamRoleRepository;
    @Autowired
    TeamRepository teamRepository;

    public List<Team> getTeamsOfUser(User u) {
        List<TeamRole> teamRoles = teamRoleRepository.findAllByUser(u);
        List<Team> teams = new ArrayList<>();
        for (TeamRole t : teamRoles) {
            teams.add(t.getTeam());
        }
        return teams;
    }

    public List<TeamRole> getUsersOfTeam(Team t) {
        return teamRoleRepository.findAllByTeam(t);
    }

    @Transactional
    public Team getTeamById(Long id) throws NullPointerException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) return team.get();
        throw new NullPointerException("team not found");
    }

    public boolean teamNameExist(String name) {
        return teamRepository.findByName(name).isPresent();
    }

    public Team insertTeam(Team team) {
        return teamRepository.save(team);
    }

    public TeamRole joinTeam(TeamRole t) {
        return teamRoleRepository.save(t);
    }

    public List<Team> searchTeam(String search_by, String text) {
        List<Team> teams = new ArrayList<>();
        if (search_by.equals("name")) {
            teams = teamRepository.findAllByNameLike("%" + text + "%");
        } else if (search_by.equals("id")) {
            try {
                Optional<Team> team = teamRepository.findById(Long.parseLong(text));
                if (team.isPresent()) teams.add(team.get());
            } catch (Exception e) {
                return teams;
            }
        } else {
            teams = teamRepository.findAll();
        }
        return teams;
    }
}
