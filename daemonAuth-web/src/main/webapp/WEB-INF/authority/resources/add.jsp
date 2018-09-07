<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<form class="form-inline" role="form" id="resourcesForm">
    <table class="table   table-hover ">
        <tr>
            <td class="col-lg-1"> 资源码</td>
            <td class="col-lg-1"><input type="input" name="resourceCode" class="form-control"></td>
        </tr>
        <tr>
            <td class="col-lg-1"> 资源名字</td>
            <td class="col-lg-1"><input type="input" name="resourceName" class="form-control"></td>
        </tr>

        <tr>
            <td class="col-lg-1">节点类型</td>
            <td class="col-lg-1"><input type="radio" name="nodeType" class="form-control" value="1"
                                        method="chooseNodetype"/>根节点
                <input type="radio" name="nodeType" class="form-control" value="2" method="chooseNodetype"/>枝节点
                <input type="radio" name="nodeType" class="form-control" value="3" method="chooseNodetype"/>叶子节点
                <input type="radio" name="nodeType" class="form-control" value="4" method="chooseNodetype"/>资源节点
            </td>
        </tr>
        <tr style="display: none" id="parentCode">
            <td class="col-lg-1"> 上级节点</td>
            <td class="col-lg-1"><input type="button" name="parentCodeChoose" class="btn  btn-info" value="选择上级"
                                        requestUrl="/tree/resourcesBranch"
                                        method="openTree" handler="resoucesBranchTree"/>
                <input type="hidden" name="parentCode" value=""/>
            </td>
        </tr>
        <%-- <tr >
             <td class="col-lg-1"  >是否展示 </td>
             <td class="col-lg-1">    <input type="radio" name="display"   class="form-control" value="true"  />是
                 <input type="radio" name="display"   class="form-control" value="false"  />否 </td>
         </tr>--%>
        <tr style="display: none" id="resourceUrl">
            <td class="col-lg-1">资源url</td>
            <td class="col-lg-1"><input type="input" name="resourceUrl" class="form-control" placeholder="请填写"></td>
        </tr>
        <tr id="resourceIcon">
            <td class="col-lg-1">资源icon</td>
            <td class="col-lg-1"><input type="input" name="resourceIcon" class="form-control" placeholder="请填写(如无可不填写)">
            </td>
        </tr>
        <tr>
            <td class="col-lg-1" colspan="2">
                <input type="button" class="btn  btn-warning" value="保存" form="resourcesForm" action='post'
                       requestUrl='/resources/save' method="formRequest"/>
            </td>
        </tr>
    </table>
</form>
<jsp:include page="../tree_dialog.jsp"></jsp:include>
