<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<form class="form-inline" role="form" id="queryResourcesForm">
    <table class="table  table-hover" style="width: 100%">
        <tr>
            <td>
                <input type="text" name="resourceCode" placeholder="资源编码" class="form-control"
                       value="${parameterMap.resourceCode[0]}"/></td>
            <td><input type="text" name="parentCode" placeholder="父资源编码" class="form-control"
                       value="${parameterMap.parentCode[0]}"/></td>
            <td>
                <select class="form-control-withoutpadding" name="nodeType">
                    <option value="">请选择节点类型</option>

                    <option value="1"
                            <c:if test="${1==parameterMap.nodeType[0]}">selected </c:if> >根节点
                    </option>
                    <option value="2"
                            <c:if test="${2==parameterMap.nodeType[0]}">selected </c:if> >枝节点
                    </option>
                    <option value="3"
                            <c:if test="${3==parameterMap.nodeType[0]}">selected </c:if> >叶子节点
                    </option>
                    <option value="4"
                            <c:if test="${4==parameterMap.nodeType[0]}">selected </c:if> >资源节点
                    </option>

                </select>
            </td>
            <td>
                <input type="radio" name="enable" class="form-control" value="true" <c:if
                        test="${parameterMap.enable[0]==true}"> checked="" </c:if>  />有效
                <input type="radio" name="enable" class="form-control" value="false" <c:if
                        test="${parameterMap.enable[0]==false}"> checked="" </c:if>   />无效
            </td>

            <td>
                <input type="button" class="btn  btn-warning" value="查询" action='get'
                       requestUrl='/resources/list' method="submit" handler="queryResources"/>
            </td>
            <td>填写枝节点的父资源编码进行查询，才能设置顺序<br>
                填写叶子的父资源编码时，节点类型设为叶子节点或枝节点，设置顺序。

            </td>
            <td>
                <c:if test="${editDisplay}">
                    <input type="button" class="btn  btn-warning" value="保存展示顺序" action='post'
                           requestUrl='/resources/updateDisplay' method="submit" handler="updateDisplay"/>
                </c:if>
            </td>

        </tr>
    </table>
</form>
共<c:out value="${count}"/>条数据
<br>

<form id="updateDisplayForm">
    <table class="table table-bordered table-hover">
        <thead>
        <tr>

            <th>ID</th>
            <th>资源编码</th>
            <th>资源名字</th>
            <th>资源url</th>
            <th>节点类型</th>
            <th>上级节点</th>
            <%--        <th >是否展示</th>--%>

            <th>状态</th>
            <c:if test="${editDisplay}">

                <th>展示顺序</th>

            </c:if>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="result" items="${resourcesList}" varStatus="status">
            <tr>
                <td class="col-lg-1">${result.id}</td>
                <td class="col-lg-1"> ${result.resourceCode}</td>
                <td class="col-lg-1"> ${result.resourceName}</td>
                <td class="col-lg-1"> ${result.resourceUrl}</td>
                <td class="col-lg-1">
                    <c:if test="${result.nodeType==1}">根节点 </c:if>
                    <c:if test="${result.nodeType==2}">枝节点 </c:if>
                    <c:if test="${result.nodeType==3}">叶子节点 </c:if>
                    <c:if test="${result.nodeType==4}">资源节点 </c:if>
                </td>
                <td class="col-lg-1">${result.parentCode} </td>
                    <%--
                                <td class="col-lg-1"> <c:if test="${result.display==true}">是 </c:if>
                                    <c:if test="${result.display==false}">否 </c:if></td>--%>
                <td class="col-lg-1">
                    <c:if test="${result.enable!=true}">
                        <button type="button" class="btn btn-warning" action='put'
                                requestUrl='/resources/updateEnable?id=${result.id}&enable=true' method="submit"
                                handler="updateEnable">生效
                        </button>
                    </c:if>
                    <c:if test="${result.enable==true}">
                        <button type="button" class="btn btn-info" action='put'
                                requestUrl='/resources/updateEnable?id=${result.id}&enable=false' method="submit"
                                handler="updateEnable">失效
                        </button>
                    </c:if>
                </td>
                <c:if test="${editDisplay}">
                    <td class="col-lg-1">
                        <input type="hidden" name="resourceList[${status.index}].id" value="${result.id}">
                        <select class="form-control" name="resourceList[${status.index}].displayIndex">
                            <c:forEach var="x" begin="1" end="${displayCount}" step="1">
                                <option value="${x}"
                                        <c:if test="${result.displayIndex==x}">selected="" </c:if>
                                >${x}</option>
                            </c:forEach>
                        </select>
                    </td>
                </c:if>
                <td class="col-lg-1">
                    <input type="button" class="btn  btn-warning" value="编辑" action='get'
                           requestUrl='/resources/toEdit?id=${result.id}' method="submit" handler="toEditResources"/>
                    <input type="button" class="btn  btn-primary" value="删除" action='delete'
                           requestUrl='/resources/delete?id=${result.id}' method="submit" handler="toDeleteResources"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</form>
<jsp:include page="../../common/page.jsp"></jsp:include>
