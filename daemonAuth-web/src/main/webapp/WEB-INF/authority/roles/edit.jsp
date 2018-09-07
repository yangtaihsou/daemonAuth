<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<form class="form-inline" role="form" id="rolesForm">
    <table class="table   table-hover">


        <tr>
            <td class="col-lg-1">角色编码</td>
            <td><input type="text" name="roleCode" value="${roles.roleCode}" class="form-control"/>
            </td>

        </tr>
        <tr>
            <td class="col-lg-1">角色名字</td>
            <td><input type="text" name="roleName" value="${roles.roleName}" class="form-control"/>
            </td>

        </tr>
        <tr>
            <td class="col-lg-1">系统编码</td>
            <td>
                <select class="form-control-withoutpadding" name="systemCode">
                    <option value="">请选择系统编码</option>
                    <c:forEach var="item" items="${rootCode}">
                        <option value="${item.key}"
                                <c:if test="${roles.systemCode==item.key}"> selected="selected" </c:if>
                        >${item.value}</option>
                    </c:forEach>
                </select>
            </td>

        </tr>

        <tr>
            <td class="col-lg-1" colspan="2">
                <input type="button" class="btn  btn-warning" value="保存" form="rolesForm" action='post'
                       requestUrl='/roles/edit' method="formRequest"/>
            </td>
            <input type="hidden" value="${roles.id}" name="id">
        </tr>
    </table>
</form>