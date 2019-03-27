package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAll();

    Optional<Tag> findByName(String name);
}
