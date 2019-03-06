package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Data
@Slf4j
public class ContestProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Problem problem;
    @ManyToOne
    private Contest contest;
    @Column
    private Long tempId;
    @Column
    private String tempTitle;

    public ContestProblem(Problem problem, Contest contest, Long tempId, String tempTitle) {
        this.problem = problem;
        this.contest = contest;
        this.tempId = tempId;
        this.tempTitle = tempTitle;
    }

    public ContestProblem() {

    }

    @Override
    public String toString() {
        return "ContestProblem{" +
                "id=" + id +
                ", problem=" + problem +
                ", contest=" + contest +
                ", tempId=" + tempId +
                ", tempTitle='" + tempTitle + '\'' +
                '}';
    }
}
