/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Log4j2
@Data
@Entity
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, unique = true, nullable = false)
    private String title;
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;
    @Column(columnDefinition = "LONGTEXT")
    private String input;
    @Column(columnDefinition = "LONGTEXT")
    private String output;
    @Column(columnDefinition = "LONGTEXT")
    private String sampleInput;
    @Column(columnDefinition = "LONGTEXT")
    private String sampleOutput;
    @Column(columnDefinition = "LONGTEXT")
    private String hint;
    @Column(columnDefinition = "LONGTEXT")
    private String source;
    @Column(nullable = false, columnDefinition = "int default 1000")
    private Integer timeLimit;
    @Column(nullable = false, columnDefinition = "int default 65536")
    private Integer memoryLimit;
    @Column(nullable = false)
    private Boolean active = false;
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer submit = 0;
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer accepted = 0;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;
    @OneToMany(mappedBy = "problem")
    private List<ProblemComment> problemComments;
    @OneToMany(mappedBy = "problem")
    private List<Solution> solutions;

    protected Problem() {
    }

    public Problem(String title, String description, String input, String output, String sampleInput, String sampleOutput, String hint, String source, Integer timeLimit, Integer memoryLimit) {
        this.title = title;
        this.description = description;
        this.input = input;
        this.output = output;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.hint = hint;
        this.source = source;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        accepted = 0;
        submit = 0;
        active = false;
    }

    public Problem(String title, String description, String input, String output, String sampleInput, String sampleOutput, String hint, String source, Integer timeLimit, Integer memoryLimit, Boolean active) {
        this.title = title;
        this.description = description;
        this.input = input;
        this.output = output;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.hint = hint;
        this.source = source;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.active = active;
        accepted = 0;
        submit = 0;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", sampleInput='" + sampleInput + '\'' +
                ", sampleOutput='" + sampleOutput + '\'' +
                ", hint='" + hint + '\'' +
                ", source='" + source + '\'' +
                ", timeLimit=" + timeLimit +
                ", memoryLimit=" + memoryLimit +
                ", active=" + active +
                ", submit=" + submit +
                ", accepted=" + accepted +
                '}';
    }

    public String getRatio() {
        if (this.submit == 0) return "0%";
        return this.accepted * 1.0 / this.submit * 100.0 + "%";
    }
}
