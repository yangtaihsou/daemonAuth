package com.daemonauth.util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class PageUtil {

    public static String getPageStr(String url, int pagesize, int pages, int count) {
        if (count < 1 || count < pagesize) {
            return "";
        }
        if (pages < 1)
            pages = 1;

        if (url.indexOf("?") < 0) {
            url = url + "?";
        }
        int totalpage = 0;
        totalpage = ((count % pagesize == 0) ? (count / pagesize) : (count / pagesize + 1));
        if (totalpage == 0) {
            totalpage = 1;
        }
        if (pages > totalpage) {
            pages = totalpage;
        } else if (pages < 1) {
            pages = 1;
        }

        StringBuffer sb = new StringBuffer();
        if (pages == 1) {
            sb.append("<div class=\"text-center\"><ul class=\"pagination\"><li class=\"disabled\"><a  href=\"javascript:void(0)\" " +
                    "src=\"" + url + "&p=1\" >首页</a></li>");
        } else {
            sb.append("<div class=\"text-center\"><ul class=\"pagination\"><li ><a  href=\"javascript:void(0)\" " +
                    "src=\"" + url + "&p=1\" >首页</a></li>");
        }
        int pagesFrom = 1, pagesTo = totalpage;
        if (pages <= 5 && totalpage - pages > 5) {
            pagesTo = totalpage >= 10 ? 10 : totalpage;
        } else if (pages > 5 && totalpage - pages > 5) {
            pagesFrom = pages - 5;
            pagesTo = pages + 5;
        } else if (pages > 5 && totalpage - pages <= 5) {
            pagesFrom = totalpage > 10 ? totalpage - 5 : 1;
        }

        for (int ik = pagesFrom; ik <= pagesTo; ik++) {
            if (ik == pages) {
                sb.append("<li class=\"active\"><a href=\"javascript:void(0)\">" + ik + "<span class=\"sr-only\">(current)</span></a></li> ");
            } else {
                sb.append("<li><a href=\"javascript:void(0)\" src=\"" + url + "&p=" + ik + "\">&nbsp;" + ik + "</a></li> ");
            }
        }
        if (totalpage == pages) {
            sb.append("<li  class=\"disabled\"><a  href=\"javascript:void(0)\" src=\"" + url + "&p=" + totalpage + "\" class=\"pre\">末页</a></li>");
        } else {
            sb.append("<li><a href=\"javascript:void(0)\"  src=\"" + url + "&p=" + totalpage + "\" class=\"pre\">末页</a></li>");
        }
        sb.append("</ul></div>");
        return sb.toString();
    }

    public static String joinParameter(HttpServletRequest request) {
        return joinParameter(request, new String[]{"_t", "p"}, true);
    }

    public static String joinParameter(HttpServletRequest request, String[] excludes, boolean attachURI) {
        String uri = request.getRequestURI();
        StringBuffer url = new StringBuffer();

        if (uri.toString().indexOf("?") < 0) {
            url.append(uri).append("?");
        }
        Enumeration<String> enums = request.getParameterNames();
        List<String> paras = new ArrayList<String>();

        if (enums != null) {
            while (enums.hasMoreElements()) {
                String para = enums.nextElement();

                if (isContain(excludes, para))
                    continue;
                paras.add(para);
            }
            Collections.sort(paras, String.CASE_INSENSITIVE_ORDER);
            for (int m = 0; m < paras.size(); m++) {

                String para = paras.get(m);
                String[] values = request.getParameterValues(para);
                for (int k = 0; values != null && k < values.length; k++) {
                    String value = values[k];
                    if (!isBlankStr(value)) {
                        url.append(para).append("=").append(value).append("&");
                    }
                }
            }
        }

        if (url.length() > 0 && url.charAt(url.length() - 1) == '&')
            url.deleteCharAt(url.length() - 1);
        return url.toString();

    }

    public static boolean isContain(String[] stringArray, String checkstr) {

        if (stringArray == null || isBlankStr(checkstr)) {
            return false;
        }
        for (int i = 0; i < stringArray.length; i++) {
            if (checkstr.equals(stringArray[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlankStr(String s) {
        return s == null || s.trim().length() == 0;
    }

}
