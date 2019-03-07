package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Tag;
import com.jinmy.onlinejudge.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public boolean checkNameExist(String name) {
        Optional<Tag> t = tagRepository.findByName(name);
        return t.isPresent();
    }

    public Tag getTagByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        return tag.isPresent() ? tag.get() : null;
    }
}
