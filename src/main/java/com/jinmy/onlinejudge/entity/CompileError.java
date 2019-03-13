package com.jinmy.onlinejudge.entity;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Data
@Log4j2
public class CompileError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Solution solution;
    @Column(columnDefinition = "LONGTEXT")
    private String info;

    public CompileError() {
    }

    public CompileError(Solution solution, String info) {
        this.solution = solution;
        this.info = info;
    }

    @Override
    public String toString() {
        return "CompileError{" +
                "solution=" + solution +
                ", info='" + info + '\'' +
                '}';
    }
}
