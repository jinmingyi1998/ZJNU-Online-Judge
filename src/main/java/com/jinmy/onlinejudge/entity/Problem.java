package com.jinmy.onlinejudge.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Type;

import javax.persistence.*;
@Log4j2
@Data
@Entity
public class Problem {
    protected Problem() {
    }
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
    private Boolean active=false;
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer submit=0;
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer accepted=0;

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
        accepted=0;
        submit=0;
        active=false;
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
        accepted=0;
        submit=0;
    }

    public String getRatio() {
        if (this.submit == 0) return "0%";
        return this.accepted * 1.0 / this.submit * 100.0 + "%";
    }
}
