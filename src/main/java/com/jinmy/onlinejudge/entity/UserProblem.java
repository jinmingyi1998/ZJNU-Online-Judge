package com.jinmy.onlinejudge.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Slf4j
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "problem_id"}))
public class UserProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    User user;
    @ManyToOne
    Problem problem;

    public UserProblem() {
    }

    public UserProblem(User user, Problem problem) {
        this.user = user;
        this.problem = problem;
    }
}
