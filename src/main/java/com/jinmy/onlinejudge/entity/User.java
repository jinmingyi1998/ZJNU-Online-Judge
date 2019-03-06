package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

enum Privilege {
    admin("admin"), teacher("teacher"), student("student");
    private String name;

    Privilege(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }
}

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Log4j2
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "username cannot be empty")
    @Size(min = 4, max = 30)
    @Column(nullable = false, length = 30, unique = true)
    private String username;
    @NotEmpty(message = "name cannot be empty")
    @Size(min = 2, max = 30)
    @Column(nullable = false, length = 30)
    private String name;
    @NotEmpty(message = "email cannot be empty")
    @Size(min = 4, max = 50)
    @Column(nullable = false, length = 50)
    @Email(message = "email format error")
    private String email;
    @NotEmpty(message = "username cannot be empty")
    @Size(min = 4, max = 30)
    @Column(nullable = false, length = 30)
    private String password;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer submit;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer solve;
    @Column(nullable = false)
    private Boolean banned;
    @CreatedDate
    @Column(nullable = false)
    private Instant registertime;
    @Column(length = 200)
    private String school;
    @Column(length = 200)
    private String cls;//class
    @Column
    private Privilege privilege;

    protected User() {
    }

    public User(@NotEmpty(message = "username cannot be empty") @Size(min = 4, max = 30) String username, @NotEmpty(message = "name cannot be empty") @Size(min = 2, max = 30) String name, @NotEmpty(message = "email cannot be empty") @Size(min = 4, max = 50) @Email(message = "email format error") String email, @NotEmpty(message = "username cannot be empty") @Size(min = 4, max = 30) String password, Integer submit, Integer solve, Boolean banned, Instant registertime, String school, String cls) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.submit = submit;
        this.solve = solve;
        this.banned = banned;
        this.registertime = registertime;
        this.school = school;
        this.cls = cls;
    }

    public User(@NotEmpty(message = "username cannot be empty") @Size(min = 4, max = 30) String username, @NotEmpty(message = "name cannot be empty") @Size(min = 2, max = 30) String name, @NotEmpty(message = "email cannot be empty") @Size(min = 4, max = 50) @Email(message = "email format error") String email, @NotEmpty(message = "username cannot be empty") @Size(min = 4, max = 30) String password, String school, String cls) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.school = school;
        this.cls = cls;
        registertime = Instant.now();
        banned = false;
        solve = 0;
        submit = 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", submit=" + submit +
                ", solve=" + solve +
                ", banned=" + banned +
                ", registertime=" + registertime +
                ", school='" + school + '\'' +
                ", cls='" + cls + '\'' +
                ", privilege=" + privilege +
                '}';
    }

    public boolean userValidator() {
        String pattern = "^\\w{6,15}$";
        String pattern2 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        if (!this.email.matches(pattern2)) {
            System.err.println("email not match");
            return false;
        }
        if (!this.username.matches(pattern)) {
            System.err.println("username not match");
            return false;
        }
        if (!this.password.matches(pattern)) {
            System.err.println("password not match");
            return false;
        }
        return true;
    }
}
