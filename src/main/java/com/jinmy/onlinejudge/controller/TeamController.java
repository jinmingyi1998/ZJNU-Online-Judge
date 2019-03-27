/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Team;
import com.jinmy.onlinejudge.entity.TeamRole;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.TeamService;
import com.jinmy.onlinejudge.service.UserAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    HttpSession session;
    @Autowired
    TeamService teamService;
    @Autowired
    UserAuthorityService userAuthorityService;

    @GetMapping
    public ModelAndView getTeamListOfMe(HttpServletResponse response) {
        try {
            ModelAndView m = new ModelAndView("team/teamindex");
            if (!userAuthorityService.isLogin()) {
                response.sendRedirect("/login");
                return null;
            }
            User u = (User) session.getAttribute("currentUser");
            List<Team> teams = teamService.getTeamsOfUser(u);
            m.addObject("teams", teams);
            return m;
        } catch (Exception e) {
            log.error("Exception!");
        }
        try {
            response.sendRedirect("/404");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/create")
    public ModelAndView createTeam() {
        return new ModelAndView("team/team_create");
    }

    @PostMapping("/rest/create")
    public String createTeam(Team team) {
        try {
            if (!team.validator()) return "Format Wrong";
            Team t = teamService.insertTeam(team);
            User u = (User) session.getAttribute("currentUser");
            TeamRole teamRole = new TeamRole(t, u, TeamRole.Role.admin);
            teamService.joinTeam(teamRole);
            return "success";
        } catch (Exception e) {
        }
        return "false";
    }

    @GetMapping("/rest/checkname")
    public String checkName(@RequestParam String name) {
        if (name.length() > 30)
            return "Name Too Long";
        if (teamService.teamNameExist(name)) {
            return "Name exist";
        }
        return "success";
    }

    @GetMapping("/{tid}")
    public ModelAndView teamInfo(@PathVariable(value = "tid") Long id, HttpServletResponse response) {
        Team team = null;
        try {
            team = teamService.getTeamById(id);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            try {
                response.sendRedirect("/404");
                return null;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        List<TeamRole> teamRoles = teamService.getUsersOfTeam(team);
        ModelAndView m = new ModelAndView("team/teaminfo");
        m.addObject("team", team);
        m.addObject("members", teamRoles);
        return m;
    }

}
