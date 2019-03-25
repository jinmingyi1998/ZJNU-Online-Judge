package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAll();

    Optional<Video> findById(Long id);

    List<Video> findAllByNameLikeOrTagLike(String name, String tag);

    List<Video> findAllByName(String name);

    Video save(Video video);

}
