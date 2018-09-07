package com.daemonauth.demo;


import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorityInterceptor implements HandlerInterceptor {
    private final static Log log = LogFactory.getLog(AuthorityInterceptor.class);

    @Resource(name = "authCheck")
    AuthorityCheckService authCheck;

    private String excludePath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String pin = "bjyangkuan";
        log.info("用户登录pin:" + pin);


        String uri = request.getRequestURI();

        if("/".equals(uri) ||"其他url".equals(uri)){
            return true;
        }

        if(excludePath!=null&&!excludePath.equals("")){//指定排除url,一般为静态资源
            String[] excludePathArr = excludePath.split(",");
            for(String excludePathStr : excludePathArr){
                if (uri.contains(excludePathStr)) {
                    return true;
                }
            }
        }
        if (Strings.isNullOrEmpty(authCheck.loadResourceUrl(pin, uri))) {
            //TODO 也可以在这里对uri进行特殊处理，再次loadResourceUrl
                 String accept = request.getHeader("Accept");
                if(accept.contains("application/json")){//ajax异步请求
                    log.info("您没有权限访问，请联系管理员，对您的京东账号进行配置");
                    throw new RuntimeException("您没有权限访问，请联系管理员，对您的京东账号进行配置");
                }else{//直接请求页面
                    response.sendRedirect("/501.html");
                    return false;
                }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
/*        String uri = request.getRequestURI();
        //     if (uri.equals("/")) {
        String pin = LoginContext.getLoginContext().getPin();
        Map<String,Resources> resourcesByPinAndSysteCodeMap = authCheck.loadResourceUrl(pin,authCheck.getSystemCode());
        if (resourcesByPinAndSysteCodeMap!=null&&
                resourcesByPinAndSysteCodeMap.get(uri) != null) {
            log.info(Thread.currentThread().getName() + "=" + request.getRequestURI());
            request.setAttribute("tabs", authCheck.loadTabs(pin).asMap());

            request.setAttribute("systemCode", authCheck.getSystemCode());

            request.setAttribute("resourcesByUrlMap", resourcesByPinAndSysteCodeMap);

            request.setAttribute("profiles", profiles);

            request.setAttribute("uri", uri);
        }*/
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public AuthorityCheckService getAuthCheck() {
        return authCheck;
    }

    public void setAuthCheck(AuthorityCheckService authCheck) {
        this.authCheck = authCheck;
    }

    public String getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(String excludePath) {
        this.excludePath = excludePath;
    }
}
