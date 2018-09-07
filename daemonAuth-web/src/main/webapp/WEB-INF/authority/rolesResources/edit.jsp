<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<form class="form-inline" role="form" id="rolesResourcesForm">
    <table class="table   table-hover">


        <tr>
            <td class="col-lg-1">资源码</td>
            <td><input type="text" name="resourceCode" value="${rolesResources.resourceCode}" class="form-control"/>
            </td>

        </tr>
        <tr>
            <td class="col-lg-1"></td>
            <td><input type="text" name="resourceName" value="${rolesResources.resourceName}" class="form-control"/>
            </td>

        </tr>
        <tr>
            <td class="col-lg-1">角色编码</td>
            <td><input type="text" name="roleCode" value="${rolesResources.roleCode}" class="form-control"/>
            </td>

        </tr>
        <tr>
            <td class="col-lg-1">角色名字</td>
            <td><input type="text" name="roleName" value="${rolesResources.roleName}" class="form-control"/>
            </td>

        </tr>

        <tr>
            <td class="col-lg-1" colspan="2">
                <input type="button" class="btn  btn-warning" value="保存" form="rolesResourcesForm" action='post'
                       requestUrl='/rolesResources/edit' method="formRequest"/>
            </td>
            <input type="hidden" value="${rolesResources.id}" name="id">
        </tr>
    </table>
</form>