<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>系统</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="/static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/extend.css"   rel="stylesheet">
    <link href="/static/bootstrap/blog.css" rel="stylesheet">
    <script src="/static/js/jquery.min.js"></script>
    <script src="/static/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/js/DaemonAuthority.js?t=2343240234"></script>
    <script src="/static/js/map.js"></script>
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

    jQuery(document).ready(function () {
        loadAuth();
    });

    /**
     * 加载权限相关
     */
    function loadAuth() {
        var authority = new DaemonAuthority.getInstance();
        var authOptions = {//自定义属性
            'domListenNodeId': 'content_xxx',//指定监听变化的节点
            'menuLoadUrl': "/page/menu",//查询页签
            'menuId': "topSidebar",//append页签的id
            'uriLoadUrl': "/page/uriResourceList",//查询用户拥有url的请求地址
            'loadAfterMenuMethod':{//页签加载完毕，执行的方法集合
                '0':menu_resourceClick,//绑定menu页签监听事件
                '1':menu_resourceClick
            }
        };
        authority.auth(authOptions);
    }

    function menu_resourceClick(){}
</script>