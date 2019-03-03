package com.jinmy.onlinejudge.entity;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

@Entity
@Data
@Log4j2
public class CompileError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long solution;
    @Column(columnDefinition = "LONGTEXT")
    private String info;

    protected CompileError() {
    }

    @Override
    public String toString() {
        return "CompileError{" +
                "info='" + info + '\'' +
                ", solution_id=" + solution +
                '}';
    }

    public CompileError(Long solution_id, String info) {
        this.info = info;
        this.solution = solution_id;
    }
}
