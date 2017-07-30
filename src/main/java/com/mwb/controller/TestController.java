package com.mwb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created by fangchen.chai on 2017/4/3.
 */
@Controller
public class TestController {

    @RequestMapping(value = "/login")
    public String loginHtml() {

        System.out.println("2111");
        return "login";
    }

}
