package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Contest;
import com.jinmy.onlinejudge.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ContestService {
    private final int PAGE_SIZE = 30;
    @Autowired
    ContestRepository contestRepository;

    public Page<Contest> getContestPage(int page, String title) {
        if (title.length() == 0)
            return contestRepository.findAll(PageRequest.of(page, PAGE_SIZE, new Sort(Sort.Direction.DESC, "startTime")));
        return contestRepository.findByTitleLike(PageRequest.of(page, PAGE_SIZE, new Sort(Sort.Direction.DESC, "startTime")), title);
    }
}
