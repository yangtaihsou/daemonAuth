package com.daemonauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User:
 * Date: 15-1-8
 * Time: 下午3:02
 */
@Controller
public class AuthorityController {

    private final static Logger log = LoggerFactory.getLogger(AuthorityController.class);

    @RequestMapping(value = "/work", method = RequestMethod.GET)
    public String index() {
        return "/authority/index.jsp";
    }
}
