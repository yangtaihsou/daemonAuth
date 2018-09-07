<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>权限管理系统</title>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <script src="/static/js/jquery.min.js"></script>
    <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/extend.css" rel="stylesheet">
    <%--    <link href="/static/css/modern.css"   rel="stylesheet">--%>
    <link href="/static/bootstrap/blog.css" rel="stylesheet">
    <script src="/static/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/js/jquery.extend.js"></script>
    <script src="/static/js/authority.js"></script>
    <script src="/static/js/map.js"></script>
    <script src="/static/js/plugin/dropdown.js"></script>

    <script type="text/javascript" src="/static/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/easyui/themes/default/easyui.css">

    <link href="/static/bootstrap/css/modal.css" rel="stylesheet">
</head>

<body>


<div class="blog-masthead">
    <div class="blog_container ">
        <nav class="blog-nav" id="topSidebar">
        </nav>
    </div>
</div>

<div class="container-fluid">

    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar" id="leftSidebar">
            </ul>

        </div>
        <br>
        <div class="col-xs-12 col-sm-9" id="content">


        </div>


    </div>
</div>


</body>
</html>
<script>

    $(document).ready(function () {
        var authority = Authority.getInstance();
        authority.loadDefaultSidebar();
        $('#topSidebar>a').live('click', function () {//点击top
            $(this).addClass("blog-active").siblings().removeClass("blog-active");
            var leftId = $(this).attr('id');
            $('#leftSidebar').html(authority.getHTML(leftId));
        });

        $('#leftSidebar>li').live('click', function () {//点击左侧菜单
            $(this).css('background-color', '#f8f8f9').siblings().css('background-color', '');
            var a = $(this).find("a")[0];
            var requestUrl = $(a).attr('src');
            if (typeof(requestUrl) != 'undefined') {
                $('.theme-popover-mask').fadeIn(100);
                authority.ajaxRequestForcontent(requestUrl, '', 'content');
            }
        });
        $('#pageStr li>a').live('click', function () { //翻页
            var requestUrl = $(this).attr('src');
            if (!requestUrl) return;
            authority.ajaxRequestForcontent(requestUrl, '', 'content');
            authority.ajaxRequestForcontent(requestUrl, '', 'content');
        })
        authority.loadDefaultPage();

        $(':input[method]').live('click', function () {
            var method = $(this).attr('method');
            if (typeof(method) != 'undefined') {
                var event = new Event();
                event.setSource(this);
                authority[method].request(event.getSource());
            }
        })
        $('a[method]').live('click', function () {
            var method = $(this).attr('method');
            if (typeof(method) != 'undefined') {
                var event = new Event();
                event.setSource(this);
                authority[method].request(event.getSource());
            }
        })

        $('#pageStr li>a').live('click', function () { //翻页
            var requestUrl = $(this).attr('src');
            if (!requestUrl) return;
            authority.ajaxRequestForcontent(requestUrl, '', 'content');
        })
    });


    //禁止后退键 作用于Firefox、Opera
    document.onkeypress = $.banBackSpace;
    //禁止后退键  作用于IE、Chrome
    document.onkeydown = $.banBackSpace;

</script>