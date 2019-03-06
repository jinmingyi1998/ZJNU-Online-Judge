package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Entity
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 255)
    private String description;
    @Column(nullable = false)
    private String privilege;
    @Column(length = 200)
    private String password;
    @Column(nullable = false)
    private Instant startTime;
    @Column(nullable = false)
    private Instant lastTime;
    @Column(nullable = false)
    private Instant createTime;
    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<ContestComment> contestComments;
    @OneToMany(mappedBy = "contest")
    private List<ContestProblem> problems;

    public Contest(String title, String description, String privilege, String password, Instant startTime, Instant lastTime, Instant createTime, List<ContestProblem> contestProblems, List<ContestComment> contestComments) {
        this.title = title;
        this.description = description;
        this.privilege = privilege;
        this.password = password;
        this.startTime = startTime;
        this.lastTime = lastTime;
        this.createTime = createTime;
    }

    public Contest() {

    }

    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", privilege='" + privilege + '\'' +
                ", password='" + password + '\'' +
                ", startTime=" + startTime +
                ", lastTime=" + lastTime +
                ", createTime=" + createTime +
                '}';
    }
}