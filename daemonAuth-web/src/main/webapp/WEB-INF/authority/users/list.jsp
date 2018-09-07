<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<form class="form-inline" role="form" id="queryUsersForm">
    <table class="table  table-hover" style="width: 60%">
        <tr>
            <td><input type="text" name="userPin" placeholder="用户pin" class="form-control"
                       value="${parameterMap.userPin[0]}"/></td>
            <td><input type="text" name="userName" placeholder="用户名字" class="form-control"
                       value="${parameterMap.userName[0]}"/></td>
            <td>
                <input type="radio" name="enable" class="form-control" value="true" <c:if
                        test="${parameterMap.enable[0]==true}"> checked="" </c:if>  />有效
                <input type="radio" name="enable" class="form-control" value="false" <c:if
                        test="${parameterMap.enable[0]==false}"> checked="" </c:if>   />无效
            </td>
            <td>
                <input type="button" class="btn  btn-warning" value="查询" action='get'
                       requestUrl='/users/list' method="submit" handler="queryUsers"/>
            </td>
        </tr>
    </table>
</form>
共<c:out value="${count}"/>条数据
<br>
<table class="table table-bordered table-hover">
    <thead>
    <tr>

        <th>用户pin</th>
        <th>用户名字</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    </tr>
    </thead>
    <tbody>

    <c:forEach var="result" items="${usersList}" varStatus="status">
        <tr>

            <td class="col-lg-1">${result.userPin} </td>
            <td class="col-lg-1">${result.userName} </td>
            <td class="col-lg-1">
                <c:if test="${result.enable!=true}">
                    <button type="button" class="btn btn-warning" action='put'
                            requestUrl='/users/updateEnable?id=${result.id}&enable=true' method="submit"
                            handler="updateEnable">生效
                    </button>
                </c:if>
                <c:if test="${result.enable==true}">
                    <button type="button" class="btn btn-info" action='put'
                            requestUrl='/users/updateEnable?id=${result.id}&enable=false' method="submit"
                            handler="updateEnable">失效
                    </button>
                </c:if>
            </td>


            <td class="col-lg-1">
                <input type="button" class="btn  btn-warning" value="编辑" action='get'
                       requestUrl='/users/toEdit?id=${result.id}' method="submit" handler="toEditUsers"/>
                <input type="button" class="btn  btn-info" value="选择角色" userPin="${result.userPin}"
                       requesturl="/tree/roles" method="openTree" handler="userResourcesTree">
                <input type="hidden" name="roleCode">
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<jsp:include page="../../common/page.jsp"></jsp:include>

<jsp:include page="../tree_dialog.jsp"></jsp:include>