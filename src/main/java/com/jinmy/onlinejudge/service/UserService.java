/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.User;
import com.jinmy.onlinejudge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    User saveOrUpdateUser(User user) {
        return userRepository.save(user);
    }

    //TODO update register to return string
    @Transactional
    public User registerUser(User user) {
        if (user.userValidator() == false) {
            return null;
        }
        if (userRepository.findByUsernameIs(user.getName()).isPresent()) {
            return null;
        } else {
            return userRepository.save(user);
        }
    }

    public User loginUser(User user) {
        Optional<User> opt_user = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        return opt_user.isPresent() ? opt_user.get() : null;
    }

    @Transactional
    void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<User> SearchUser(String name, Pageable pageable) {
        return userRepository.findByNameLike(name, pageable);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    /**
     * @param username
     * @return return user or null if not exist
     */
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsernameIs(username);
        return user.isPresent() ? user.get() : null;
    }

    /**
     * @param id user's id
     * @return true if the user exist
     */
    public boolean isExist(Long id) {
        return getUserById(id) != null;
    }
}
