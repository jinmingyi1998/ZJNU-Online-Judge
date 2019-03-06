package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Slf4j
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String text;
    @ManyToOne
    private Problem problem;
    protected Article() {
    }

    public Article(String text, Problem problem) {
        this.text = text;
        this.problem = problem;
    }
}
