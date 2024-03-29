/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jinmy.onlinejudge.service;

import com.jinmy.onlinejudge.entity.Article;
import com.jinmy.onlinejudge.entity.Problem;
import com.jinmy.onlinejudge.entity.Tag;
import com.jinmy.onlinejudge.repository.ArticleRepository;
import com.jinmy.onlinejudge.repository.ProblemRepository;
import com.jinmy.onlinejudge.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProblemService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private TagRepository tagRepository;

    /**
     * @param id
     * @return return problem or null if not exist
     */
    public Problem getProblemById(Long id) {
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isPresent())
            return problem.get();
        return null;
    }

    public boolean isExist(Long id) {
        return problemRepository.findById(id).isPresent();
    }

    @Transactional
    public List<Problem> getProblemList() {
        List<Problem> problemList = problemRepository.findAll();
        return problemList;
    }

    @Transactional
    public Problem updateProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    @Transactional
    public Problem insertProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    public Page<Problem> getByTagName(int page, int size, String tagname) {
        try {
            Tag t = tagRepository.findByName(tagname).get();
            Page<Problem> problems = problemRepository.findAllByTagsOrderByIdAsc(t, PageRequest.of(page, size));
            return problems;
        } catch (Exception e) {
        }
        return null;
    }

    public Page<Problem> getProblemPage(int page, int size, String search) {
        Sort _sort = new Sort(Sort.Direction.ASC, "id");
        Page<Problem> problemPage = null;
        try {
            if (search.length() > 0) {
                try {
                    problemPage = problemRepository.findAllByActiveAndIdOrTitleLike(PageRequest.of(page, size, _sort), true, Long.parseLong(search), "%" + search + "%");
                } catch (NumberFormatException e) {
                    problemPage = problemRepository.findAllByActiveAndTitleLike(PageRequest.of(page, size, _sort), true, "%" + search + "%");
                }
            } else
                problemPage = problemRepository.findAllByActive(PageRequest.of(page, size, _sort), true);
        } catch (Exception e) {
            problemPage = problemRepository.findAllByActive(PageRequest.of(page, size, _sort), true);
        }
        return problemPage;
    }

    public Page<Problem> getAdminProblemPage(int page, int size, String search) {
        Sort _sort = new Sort(Sort.Direction.ASC, "id");
        try {
            if (search.length() > 0) {
                try {
                    return problemRepository.findAllByIdOrTitleLike(PageRequest.of(page, size, _sort), Long.parseLong(search), "%" + search + "%");
                } catch (NumberFormatException e) {
                    return problemRepository.findAllByTitleLike(PageRequest.of(page, size, _sort), "%" + search + "%");
                }
            }
        } catch (Exception e) {
        }
        return problemRepository.findAll(PageRequest.of(page, size, _sort));
    }

    public void delete(Long id) {
        problemRepository.deleteById(id);
    }

    public List<Article> getArticlesOfProblem(Problem problem) {
        List<Article> articles = articleRepository.findAllByProblemOrderByIdDesc(problem);
        return articles;
    }
}