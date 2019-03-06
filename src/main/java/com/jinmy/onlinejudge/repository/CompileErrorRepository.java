package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.CompileError;
import com.jinmy.onlinejudge.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompileErrorRepository extends JpaRepository<CompileError, Long> {
    CompileError save(CompileError ce);

    Optional<CompileError> findCompileErrorBySolution(Solution solution);
}
