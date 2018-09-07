package com.daemonauth.controller;

import com.daemonauth.util.interceptor.context.LoginContext;
import com.daemonauth.domain.ErpSysUser;
import com.daemonauth.domain.Menu;
import com.daemonauth.service.common.AuthorityDBLoad;
import com.daemonauth.util.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Package .controller
 * @Description: 页面视图控制器
 * @Author
 * @Date 2017/7/11
 * @Time 10:27
 * @Version V1.0
 */
@Controller
public class PageController {

    private static List<Menu> commonMenuList = new ArrayList<Menu>();
    private static List<Menu> adminMenuList = new ArrayList<Menu>();//超级用户才能看到所用菜单

    static {
        Menu menu1 = new Menu();
        menu1.setIcon("fa fa-calendar");
        menu1.setMenuId(1);
        menu1.setName("资源管理");
        menu1.setOrderNum(1);
        menu1.setUrl("/sys/resource_mng.html");
        Menu menu2 = new Menu();
        menu2.setIcon("fa fa-tasks");
        menu2.setMenuId(2);
        menu2.setName("角色管理");
        menu2.setOrderNum(2);
        menu2.setUrl("/sys/role_mng.html");
        Menu menu3 = new Menu();
        menu3.setIcon("fa fa-tasks");
        menu3.setMenuId(3);
        menu3.setName("用户管理");
        menu3.setOrderNum(3);
        menu3.setUrl("/sys/user_mng.html");
        Menu menu4 = new Menu();
        menu4.setIcon("fa fa-tasks");
        menu4.setMenuId(4);
        menu4.setName("本系统用户管理");
        menu4.setOrderNum(4);
        menu4.setUrl("/sys/erpsysuser_mng.html");
        commonMenuList.add(menu1);
        commonMenuList.add(menu2);
        commonMenuList.add(menu3);
        adminMenuList.add(menu1);
        adminMenuList.add(menu2);
        adminMenuList.add(menu3);
        adminMenuList.add(menu4);
    }

    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    @RequestMapping("sys/{url}.html")
    public String sysPage(@PathVariable("url") String url) {
        return url + ".html";
    }

    @ResponseBody
    @RequestMapping(value = "/userInfo")
    public ErpSysUser userInfo() {
        LoginContext loginContext = LoginContext.getLoginContext();
        ErpSysUser erpSysUser = new ErpSysUser();
        erpSysUser.setUserErp(loginContext.getPin());
        erpSysUser.setUsername(loginContext.getNick());
        return erpSysUser;
    }

    @ResponseBody
    @RequestMapping(value = "/menu")
    public R menu() {
        String userErp = LoginContext.getLoginContext().getPin();
        if (userErp.equals("bjyangkuan")) {
            return R.ok().put("menuList", adminMenuList).put("code", 0);
        }
        ErpSysUser erpSysUser = authorityDataConfig.erpSysUserMap.get(userErp);
        if (erpSysUser == null || erpSysUser.getUserType() != 0) {
            return R.ok().put("menuList", commonMenuList).put("code", 0);
        } else {
            return R.ok().put("menuList", adminMenuList).put("code", 0);
        }
    }

}