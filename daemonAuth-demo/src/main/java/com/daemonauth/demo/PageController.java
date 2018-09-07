package com.daemonauth.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class PageController {


	@RequestMapping("sys/{url}.html")
	public String sysPage(@PathVariable("url") String url,Model model){

		return url + ".html";
	}

    @RequestMapping(value = "/jsp/index", method = RequestMethod.GET)
    public String index(String pin,HttpServletRequest request) {

        return "/index.jsp";
    }

    @RequestMapping(value = "/jsp/page", method = RequestMethod.GET)
    public String page(String pin,HttpServletRequest request) {

        return "/page.jsp";
    }
    @Resource(name = "authCheck")
    AuthorityCheckService authCheck;
    /**
     * 首页页签视图
     */
    @ResponseBody
    @RequestMapping("/page/menu")
    public R menu(){
        String html = authCheck.setUpMenuHtml( "bjyangkuan");
        return R.ok().put("menu",html);
    }

    /**
     * 加载用户拥有的资源(带url)列表
     */
    @ResponseBody
    @RequestMapping("/page/uriResourceList")
    public R uriResourceList(){
        List<String>  list = authCheck.loadUrlResourceList("bjyangkuan");
        return R.ok().put("uriResourceList",list);
    }
}
