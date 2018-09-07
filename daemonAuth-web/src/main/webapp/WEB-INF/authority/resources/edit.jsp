<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<form class="form-inline" role="form" id="resourcesForm">
    <table class="table   table-hover">
        <tr>
            <td class="col-lg-1"> 资源码</td>
            <td class="col-lg-1"><input type="input" name="resourceCode" value="${resources.resourceCode}"
                                        class="form-control"></td>
        </tr>
        <tr>
            <td class="col-lg-1"> 资源名字</td>
            <td class="col-lg-1"><input type="input" name="resourceName" value="${resources.resourceName}"
                                        class="form-control"></td>
        </tr>


        <tr>
            <td class="col-lg-1">节点类型</td>
            <td class="col-lg-1">
                <input type="radio" name="nodeType" class="form-control" value="1" method="chooseNodetype"  <c:if
                        test="${resources.nodeType==1}"> checked="" </c:if>/>根节点
                <input type="radio" name="nodeType" class="form-control" value="2" method="chooseNodetype" <c:if
                        test="${resources.nodeType==2}"> checked="" </c:if> />枝节点
                <input type="radio" name="nodeType" class="form-control" value="3" method="chooseNodetype" <c:if
                        test="${resources.nodeType==3}"> checked="" </c:if> />叶子节点

                <input type="radio" name="nodeType" class="form-control" value="4" method="chooseNodetype" <c:if
                        test="${resources.nodeType==4}"> checked="" </c:if> />资源节点
            </td>
        </tr>
        <tr <c:if test="${resources.nodeType==1}"> style="display: none" </c:if> id="parentCode">
            <td class="col-lg-1"> 上级节点</td>
            <td class="col-lg-1">
                <input type="button" name="parentCodeChoose" class="btn  btn-info" value="选择上级"
                       requestUrl="/tree/resourcesBranch" method="openTree" handler="resoucesBranchTree"/>
                ${resources.parentCode}
                <input type="hidden" name="parentCode" value="${resources.parentCode}"/>
            </td>
        </tr>
        <%--    <tr >
                <td class="col-lg-1"  >是否展示 </td>
                <td class="col-lg-1">    <input type="radio" name="display"   class="form-control" value="true"  <c:if test="${resources.display==true}">  checked="" </c:if>/>是
                    <input type="radio" name="display"   class="form-control" value="false"  <c:if test="${resources.display==false}">  checked="" </c:if>/>否 </td>
            </tr>--%>
        <tr <c:if test="${resources.nodeType!=3&&resources.nodeType!=4}"> style="display: none" </c:if>
                id="resourceUrl">
            <td class="col-lg-1">资源url</td>
            <td class="col-lg-1"><input type="input" name="resourceUrl" class="form-control" placeholder="请填写"
                                        value="${resources.resourceUrl}"></td>
        </tr>
        <tr id="resourceIcon">
            <td class="col-lg-1">资源icon</td>
            <td class="col-lg-1"><input type="input" name="resourceIcon" class="form-control" placeholder="请填写(如无可不填写)"
                                        value="${resources.resourceIcon}"></td>
        </tr>

        <tr>
            <td class="col-lg-1" colspan="2">
                <input type="button" class="btn  btn-warning" value="更新" form="resourcesForm" action='post'
                       requestUrl='/resources/edit' method="formRequest"/>
            </td>
            <input type="hidden" value="${resources.id}" name="id">
        </tr>
    </table>
</form>
<jsp:include page="../tree_dialog.jsp"></jsp:include>
