package com.daemonauth.util;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * User:
 * Date: 14-11-25
 * Time: 上午11:22
 */
public class Constant {

    public static final int pageSize = 20;

    public static void loadRequestParameterMapToModel(Model model, HttpServletRequest request) {
        model.addAttribute("parameterMap", request.getParameterMap());
    }


}
