package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

enum Role {
    admin("Administrator"), manager("Manager"), member("Member");

    String name;

    Role(String s) {
        name = s;
    }
}

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Slf4j
@Entity
public class TeamRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Team team;
    @ManyToOne
    private User user;
    private Role role;
    public TeamRole(Team team, User user, Role role) {
        this.team = team;
        this.user = user;
        this.role = role;
    }
    public TeamRole() {

    }

    @Override
    public String toString() {
        return "TeamRole{" +
                "id=" + id +
                ", team=" + team +
                ", user=" + user +
                ", role=" + role +
                '}';
    }
}
