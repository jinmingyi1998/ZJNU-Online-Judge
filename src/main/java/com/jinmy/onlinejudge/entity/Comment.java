package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Comment {

    @Id
    private Long id;
    @Column(nullable = true)
    private Long fatherId;
    @Column(nullable = false)
    private Instant postTime;
    @ManyToOne
    private User user;
    @Column(columnDefinition = "LONGTEXT")
    private String text;
}

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Entity
class ProblemComment extends Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Problem problem;

    public ProblemComment(Problem problem) {
        this.problem = problem;
    }

    public ProblemComment() {

    }

    @Override
    public String toString() {
        return super.toString() + "ProblemComment{" +
                "problem=" + problem +
                '}';
    }
}

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Entity
class ContestComment extends Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Contest contest;

    public ContestComment() {
    }

    public ContestComment(Contest contest) {

        this.contest = contest;
    }

    @Override
    public String toString() {
        return "ContestComment{" +
                "id=" + id +
                ", contest=" + contest +
                '}';
    }
}

