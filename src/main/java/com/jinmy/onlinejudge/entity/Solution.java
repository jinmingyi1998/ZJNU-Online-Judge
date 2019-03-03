package com.jinmy.onlinejudge.entity;

import com.jinmy.onlinejudge.repository.CompileErrorRepository;
import com.jinmy.onlinejudge.service.UserService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.swing.text.DateFormatter;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

@Data
@Log4j2
@Entity
public class Solution {
//    @Autowired
//    UserService userService;
//    @Autowired
//    CompileErrorRepository compileErrorRepository;

    protected Solution() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * user id
     */
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long problemId;

    /**
     * C: c
     * C++11: cpp
     * Java: java
     * Python2: py2
     * Python3: py3
     */
    @Column(nullable = false)
    private String language;

    @Size(max = 32768, message = "Code too Long!")
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String source;

    @Column(nullable = false)
    private Instant submitTime;

    @Column(nullable = false)
    private String ip;

    @Column(columnDefinition = "bigint default NULL")
    private Long contest;

    @Column(nullable = false, columnDefinition = "integer default -1")
    private Integer time;

    @Column(nullable = false, columnDefinition = "integer default -1")
    private Integer memory;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer length;

    @Column(nullable = false, columnDefinition = "varchar(50) default 'WA'", length = 50)
    private String result;
    @Column
    private Boolean share;

    public String getNormalLanguage() {
        switch (language) {
            case "c":
                return "C";
            case "cpp":
                return "C++";
            case "java":
                return "Java";
            case "py2":
                return "Python2";
            case "py3":
                return "Python3";
        }
        return "";
    }

    public String getNormalSubmitTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(submitTime));
    }

    private String ce;
    private String username;



    public Solution(Long userId, Long problemId, String language, @Size(max = 10000, message = "Code too Long!") String source, String ip,Boolean share) {
        this.userId = userId;
        this.problemId = problemId;
        this.language = language;
        this.source = source;
        this.ip = ip;
        this.share=share;
        length = source.length();
        submitTime = Instant.now();
        time = 0;
        memory = 0;
        result = "WA";
    }

    public Solution(Long userId, Long problemId, String language, @Size(max = 10000, message = "Code too Long!") String source, String ip, Long contest) {
        this.userId = userId;
        this.problemId = problemId;
        this.language = language;
        this.source = source;
        this.ip = ip;
        this.contest = contest;
        this.share=false;
        length = source.length();
        submitTime = Instant.now();
        time = 0;
        memory = 0;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id=" + id +
                ", userId=" + userId +
                ", problemId=" + problemId +
                ", language='" + language + '\'' +
                ", source='" + source + '\'' +
                ", submitTime=" + submitTime +
                ", ip='" + ip + '\'' +
                ", contest=" + contest +
                ", time=" + time +
                ", memory=" + memory +
                ", length=" + length +
                ", result='" + result + '\'' +
                ", share=" + share +
                ", ce='" + ce + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
