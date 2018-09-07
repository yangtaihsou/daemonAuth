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
    <script src="/static/js/baoxian_erp.js?t=2343240234"></script>
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
    function initTabResource(main){
        var topMenuHtml = "";
        <c:forEach var="topLab" items="${tabs[systemCode]}" varStatus="status">
        topMenuHtml = topMenuHtml+  '<c:out value="${topLab}" escapeXml="false"/>';
        </c:forEach>
        main.topMenu.html=topMenuHtml;

        <c:forEach var="item" items="${tabs}" varStatus="status">
        var subTabHtml = "";
        <c:forEach var="subTab" items="${tabs[item.key]}" varStatus="status">
        subTabHtml = subTabHtml+'<c:out value="${subTab}" escapeXml="false"/>';
        </c:forEach>
        main.map.put("<c:out value="${item.key}"/>",subTabHtml);
        </c:forEach>
    }
    jQuery(document).ready(function () {

        var view = baoxianErp.getInstance();
        initTabResource(view);

        $('input[type=file]').live('change',function(){
            view[$(this).attr('method')](this);
        })

        $('select').live('blur',function(){
            var method = $(this).attr('method');
            if(typeof(method) != 'undefined'){
                view[method].request(this);
            }
        })

        $('input[tag]').live('blur',function(){
            var method = $(this).attr('method');
            if(typeof(method) != 'undefined'){
                view[method].request(this);
            }
        })


        $(':input[tag]').live('click',function(){
            var method = $(this).attr('method');
            if(typeof(method) != 'undefined'){
                if($(this).is('button')){
                   // $(this).attr('disabled','');
                }
                view[method].request(this);
            }
        })

        view.loadDefaultSidebar();
        $('#topSidebar>a').live('click',function () {//点击top
            $(this).addClass("blog-active").siblings().removeClass("blog-active");
            var leftId = $(this).attr('id');
            $('#leftSidebar').html(view.getHTML(leftId));
        });

    /*    $('#leftSidebar>li').live('click',function () {//点击左侧菜单
            $(this).css('background-color','#f8f8f9').siblings().css('background-color','');
            var a = $(this).find("a")[0];
            var requestUrl = $(a).attr('src');
            if(typeof(requestUrl) != 'undefined'){
                $('.theme-popover-mask').fadeIn(100);
                view.ajaxRequestForcontent(requestUrl,'','content');
            }
        });*/

        $('li[role="leaf"]').live('click',function () {//点击叶子节点

            $(this).css('background-color','#f8f8f9').siblings().css('background-color','');
            var a = $(this).find("a")[0];
            var requestUrl = $(a).attr('src');
            if(typeof(requestUrl) != 'undefined'){
                $('.theme-popover-mask').fadeIn(100);
                view.ajaxRequestForcontent(requestUrl,'','content');
            }
        });

        $('li[role="node"]').live('click',function (event) {//点击枝节点
            var attributes = event.target.attributes;
            if(typeof( attributes["role"]) != 'undefined'||typeof( attributes["src"]) != 'undefined'){//点击了枝节点下的叶子节点
                console.log(event.target.attributes);
                return;
            }
            $(this).css('background-color','#f8f8f9').siblings().css('background-color','');
            var a = $(this).find("a")[0];
            var isShow = $(a).attr('show');
            if(typeof(isShow) == 'undefined'){
                $(a).attr('show',"");
            }else{
                $(a).removeAttr('show');
                $(this).html($(a).prop('outerHTML'));
                return;
            }
            var id = $(a).attr('id');
            $(this).html($(a).prop('outerHTML')+"<ul>"+view.getHTML(id)+"</ul>");

        });

        $('#pageStr li>a').live('click',function(){ //翻页
            var requestUrl = $(this).attr('src');
            if(!requestUrl) return;
            view.ajaxRequestForcontent(requestUrl,'','content');
        } )
        view.loadDefaultPage();
    });


</script>