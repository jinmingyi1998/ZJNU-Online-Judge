package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Team;
import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping
    public ModelAndView getTeamListOfMe(HttpServletResponse response) {
        try {
            ModelAndView m = new ModelAndView("team/teamindex");
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
}
