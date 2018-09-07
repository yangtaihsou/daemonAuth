<%@ page language="java" pageEncoding="utf-8" %>
<style>
    .tree-container-div {
        overflow: auto !important;
        height: 560px;
    }
</style>
<div id="dlg" class="easyui-dialog" title="请选择"
     data-options="iconCls:'icon-save',resizable:true,autoOpen:false,closed:true"
     style="width:400px;height:300px;padding:10px">

</div>

<div class="theme-popover tree-container-div">
    <div class="theme-poptit">
        <a href="javascript:void(0);" title="关闭" class="close" method="closeTree">×</a>
        <h3>请选择</h3>
    </div>


    <ul id="ttt" class="easyui-tree"></ul>
    <div class="theme-popbod dform">
        <form class="theme-signin" name="loginform" action="" method="post">
            <%--  <ol>
                      <li><input class="btn btn-primary" type="submit" name="submit" value=" 保存 " /></li>
              </ol>--%>
            <input class="btn btn-primary" type="button" value=" 保存" name="saveTree" method="saveTreeChecked"/>
        </form>
    </div>
</div>
<div class="theme-popover-mask"></div>