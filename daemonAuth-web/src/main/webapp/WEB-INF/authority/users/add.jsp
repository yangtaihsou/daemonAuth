<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<form class="form-inline" role="form" id="usersForm">
    <table class="table   table-hover">


        <tr>
            <td class="col-lg-1">用户pin</td>
            <td><input type="text" name="userPin" class="form-control"/>
            </td>

        </tr>
        <tr>
            <td class="col-lg-1">用户名字</td>
            <td><input type="text" name="userName" class="form-control"/>
            </td>

        </tr>


        <tr>
            <td class="col-lg-1" colspan="2">
                <input type="button" class="btn  btn-warning" value="提交" form="usersForm" action='post'
                       requestUrl='/users/save' method="formRequest"/>
            </td>
        </tr>
    </table>
</form>