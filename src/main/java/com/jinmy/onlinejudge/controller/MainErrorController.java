package com.jinmy.onlinejudge.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
@Controller
public class MainErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView errorPage(HttpServletRequest request){
        Integer errorCode= (Integer) request.getAttribute("javax.servlet.error.status_code");
        ModelAndView m = new ModelAndView("404");
        m.addObject("code",errorCode);
        return m;
    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
