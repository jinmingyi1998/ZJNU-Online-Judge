package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.entity.ContestComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestCommentRepository extends JpaRepository<ContestComment, Long> {
    @Override
    <S extends ContestComment> S save(S entity);

    List<ContestComment> findAllByContest(Contest contest);
}
