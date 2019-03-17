/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Comment {

    @Id
    private Long id;
    @Column(nullable = true)
    private Long fatherId;
    @Column(nullable = false)
    private Instant postTime;
    @ManyToOne
    private User user;
    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @JsonManagedReference
    public String getNormalPostTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(postTime));
    }
}



