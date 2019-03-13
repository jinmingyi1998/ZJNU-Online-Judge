package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    private Instant endTime;

    @Column(nullable = false)
    private Instant createTime;
    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<ContestComment> contestComments;
    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<ContestProblem> problems;
    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Solution> solutions;

    public Contest(String title, String description, String privilege, String password, Instant startTime, Instant endTime, Instant createTime, List<ContestProblem> contestProblems, List<ContestComment> contestComments) {
        this.title = title;
        this.description = description;
        this.privilege = privilege;
        this.password = password;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createTime = createTime;
    }

    public String getNormalStartTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date.from(startTime));
    }

    public String getNormalEndTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date.from(endTime));
    }

    public void setStartTime(String startTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(startTime, dtf);
        this.startTime = Instant.from(localDateTime.atZone(ZoneId.systemDefault()));
    }

    public void setEndTime(String startTime, String lastTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(startTime, dtf);
        this.startTime = Instant.from(localDateTime.atZone(ZoneId.systemDefault()));
        String ps = "PT" + lastTime.split(":")[0] + "H" + lastTime.split(":")[1] + "M";
        System.out.println(ps);
        Duration duration = Duration.parse(ps);
        this.endTime = this.startTime.plus(duration);
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
                ", lastTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}