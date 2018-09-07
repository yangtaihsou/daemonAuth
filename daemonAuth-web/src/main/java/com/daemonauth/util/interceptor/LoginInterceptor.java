package com.daemonauth.util.interceptor;

import com.daemonauth.domain.ErpSysUser;
import com.daemonauth.service.common.AuthorityDBLoad;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User:
 * Date: 15-3-30
 * Time: 下午5:47
 */
public class LoginInterceptor implements HandlerInterceptor {

    private final static Log log = LogFactory.getLog(LoginInterceptor.class);


    @Resource(name = "authorityDataConfig")
    private AuthorityDBLoad authorityDataConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        //静态资源文件
        if (uri.contains("static")) {
            return true;
        }
        if (uri.contains("rest")) {
            return true;
        }
        if (uri.contains("statics")) {
            return true;
        }
        if (uri.contains("js")) {
            return true;
        }
        if (uri.contains("/query")) {
            return true;
        }
        if (uri.contains("/authority")) {
            return true;
        }
        if (uri.contains("/500.html")) {
            return true;
        }

        String userErp = "";// LoginContext.getLoginContext().getPin();
        if (userErp.equals("bjyangkuan")) {
            return true;
        }
        ErpSysUser erpSysUser = authorityDataConfig.erpSysUserMap.get(userErp);

        if (erpSysUser == null) {
            //throw new Exception("您没有权限访问，请联系所对应运营系统的负责人，如黄金、券商、信保、保险等系统研发负责人");
            response.sendRedirect("/500.html");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
