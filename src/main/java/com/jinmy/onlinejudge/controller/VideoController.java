package com.jinmy.onlinejudge.controller;

import com.jinmy.onlinejudge.entity.Video;
import com.jinmy.onlinejudge.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    VideoRepository videoRepository;

    @GetMapping
    public ModelAndView videoIndex() {
        ModelAndView m = new ModelAndView("videos/index");
        return m;
    }

    @GetMapping("/api/list")
    public List<Video> getVideos() {
        List<Video> videoList = videoRepository.findAll();
        return videoList;
    }

    @GetMapping("/{id}")
    public ModelAndView getVideoById(@PathVariable Long id, HttpServletResponse response) {
        ModelAndView m = new ModelAndView("videos/player");
        Optional<Video> video = videoRepository.findById(id);
        if (video.isPresent()) {
            m.addObject("video", video.get());
            return m;
        } else {
            try {
                response.sendRedirect("/404");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
