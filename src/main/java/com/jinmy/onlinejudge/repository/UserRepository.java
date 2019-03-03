package com.jinmy.onlinejudge.repository;

import com.jinmy.onlinejudge.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIs(String username);

    Page<User> findByNameLike(String name, Pageable pageable);

    Optional<User> findByUsernameAndPassword(String username,String password);

    void deleteById(Long id);

    User save(User user);

    Optional<User> findById(Long id);
}
