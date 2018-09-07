<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<form class="form-inline" role="form" id="usersRolesForm">
    <table class="table   table-hover">


        <tr>
            <td class="col-lg-1">用户pin</td>
            <td><input type="text" name="pin" class="form-control"/>
            </td>

        </tr>
        <tr>
            <td class="col-lg-1">角色</td>
            <td><input type="button" class="btn  btn-info" value="选择角色" requestUrl="/tree/roles"
                       method="openTree" handler="roleTree"/>
                <input type="hidden" name="roleCode">
            </td>

        </tr>


        <tr>
            <td class="col-lg-1" colspan="2">
                <input type="button" class="btn  btn-warning" value="保存" form="usersRolesForm" action='post'
                       requestUrl='/usersRoles/saveBatch' method="formRequest"/>
            </td>
        </tr>
    </table>
</form>


<jsp:include page="../tree_dialog.jsp"></jsp:include>