/**
 * Created by guojianwei on 2017/7/11.
 */
$(function () {
    $("#jqGrid").jqGrid({
        url: '/resources/pageQuery',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', width: 20},
            {label: '资源编码', name: 'resourceCode', width: 60},
            {label: '资源名字', name: 'resourceName', width: 80},
            {label: '资源url', name: 'resourceUrl', width: 80},
            {label: '资源图标', name: 'resourceIcon', width: 40},
            {label: '节点类型', name: 'nodeType', formatter: 'select', formatoptions: {value: dics.nodeType}, width: 40},
            {label: '上级节点', name: 'parentCode', width: 60},
            {label: '状态', name: 'enable', formatter: 'select', formatoptions: {value: dics.status}, width: 20},
            {
                label: '自己父节点下的顺序', name: 'displayIndex', width: 60,
                formatter: function (cellvalue, options, rowObject) {
                    var records = $("#jqGrid").jqGrid('getGridParam', 'records');
                    //var rowNum = $("#jqGrid").jqGrid('getGridParam','rowNum');
                    var selectNodeType = vm.params.selectNodeType;
                    if (selectNodeType != null && (selectNodeType == 1 || selectNodeType == 2)) {//父节点为根节点或者枝节点
                        $('#saveDisplayIndex').show();
                        if (records > 0) {
                            var rowIndex = vm.resources.index++;
                            var selects = [];
                            selects.push('<select   class="form-control" name="resourceList[' + rowIndex + '].displayIndex"style="width: 50">');
                            selects.push('<option value="">请选择</option>');
                            for (var index = 1; index < records + 1; index++) {
                                if (cellvalue != null && cellvalue == index) {
                                    selects.push('<option value="' + index + '" selected>' + index + '</option>');
                                } else {
                                    selects.push('<option value="' + index + '">' + index + '</option>');
                                }
                            }
                            selects.push('</select>');
                        }
                        selects.push('<input type="hidden" name="resourceList[' + rowIndex + '].id" value="' + rowObject['id'] + '"');
                        return selects.join("");
                    } else {
                        $('#saveDisplayIndex').hide();
                        return cellvalue == null ? "" : cellvalue;
                    }
                }
            },
            {
                label: '操作', name: 'enable', width: 80,
                formatter: function (cellvalue, options, rowObject) {
                    var btns = [];
                    btns.push('<a class="btn btn-xs btn-success" href="javascript:void(0);" onclick="vm.update(' + options.rowId + ')">修改</a>');
                    if (cellvalue) {
                        btns.push('<a class="btn btn-xs btn-success" href="javascript:void(0);" onclick="vm.changeStatus(' + options.rowId + ',' + !cellvalue + ')">无效</a>');
                    } else {
                        btns.push('<a class="btn btn-xs btn-warning" href="javascript:void(0);" onclick="vm.changeStatus(' + options.rowId + ',' + !cellvalue + ')">生效</a>');
                    }
                    btns.push('<a class="btn btn-xs btn-success" href="javascript:void(0);" onclick="vm.loadSourceTree(\'' + rowObject.resourceCode + '\')">预览上下级</a>');
                    return btns.join(' ');
                }
            }
        ],
        viewrecords: true,
        height: 800,
        rowNum: 30,
        rowList: [10, 30, 50, 100],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "pageNo",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    $('#saveDisplayIndex').click(function () {
        var paraData = $('#updateDisplayForm').serialize();
        console.log(paraData);

        $.ajax({
            type: 'post',
            url: '/resources/updateDisplay',
            data: paraData,
            cache: false,
            async: true,
            success: function (result) {
                //layer.alert(result.reason);
                $('#saveDisplayIndexMsg').text(result.reason);
            }
        })

    })
});

var resource_mng_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "code",
            pIdKey: "parentCode",
            rootPId: ""
        },
        key: {
            url: "nourl"
        }
    }
};
var resource_mng_ztree;

var resource_upAndDown_ztree;//根据某个资源码，展示上下级关系
var vm = new Vue({
    el: '#rrapp',
    data: {
        params: {
            nodeType: "",
            parentCode: "",
            parentName: "",
            selectNodeType: ""
        },
        resources: {
            id: null,
            displayIndex: "",
            index: 0
        }
    },
    created: function () {
        this.loadTree(false);
    },

    methods: {
        loadTree: function (isUpdate) {
            $.get("/tree/parentTree", function (r) {
                resource_mng_ztree = $.fn.zTree.init($("#menuTree"), resource_mng_setting, r.treeList);
            })
        },
        menuTree: function () {
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择菜单",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#menuLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = resource_mng_ztree.getSelectedNodes();

                    //选择上级菜单
                    vm.params.parentCode = node[0].code;
                    vm.params.parentName = node[0].name;
                    vm.params.selectNodeType = node[0].nodeType;

                    layer.close(index);
                }
            });
        },
        query: function () {
            vm.resources.index = 0;
            $("#jqGrid").jqGrid('setGridParam', {
                postData: vm.params,
                page: 1
            }).trigger("reloadGrid");
        },
        create: function () {
            var opt = {
                url: "/sys/resource_add.html#800",
                id: 'resource_add',
                title: '创建资源',
                height: 800
            };
            addTab(opt);
        },
        batchExport: function () {
            var opt = {
                url: "/sys/resource_export.html#800",
                id: 'resource_export',
                title: '批量导入资源',
                height: 800
            };
            addTab(opt);
        },
        changeStatus: function (id, status) {
            $.ajax({
                type: "PUT",
                url: '/resources/updateEnable?id=' + id + '&enable=' + status,
                success: function (r) {
                    if (r.status) {
                        alert('操作成功', function (index) {
                            $("#jqGrid").jqGrid().trigger("reloadGrid");
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        update: function (id) {
            var opt = {
                url: "/sys/resource_add.html?id=" + id + "#800",
                id: 'resource_add' + id,
                title: id + '.资源修改',
                height: 800
            };

            addTab(opt);

            //window.location = "/sys/resource_add.html?id="+id;
        },
        delete: function (v_id) {

            var opt = {
                url: "/resources/delete?id=" + v_id,
                msg: '确认要删除此记录吗？',
                type: "DELETE",
                data: {}
            };
            confirmAjax(opt);
        },
        detail: function (id) {
            var opt = {
                url: "/ap/loanrecords_detail.html?id=" + id + "#1200",
                id: 'loanrecords_detail' + id,
                title: id + '.贷款详情',
                height: 800
            };

            if (T.p('from') == 'erp') {
                window.location = opt.url;
                parent.$("#loanIframe").css("height", "1200px");
            }
            addTab(opt);
        },
        loadSourceTree: function (sourceCode) {
            $.get("/tree/upAndDownTree?sourceCode=" + sourceCode, function (r) {
                resource_upAndDown_ztree = $.fn.zTree.init($("#sourceMenuTree"), resource_mng_setting, r.treeList);

                layer.open({
                    type: 1,
                    offset: '50px',
                    skin: 'layui-layer-molv',
                    title: "路径预览",
                    area: ['300px', '450px'],
                    shade: 0,
                    shadeClose: false,
                    content: jQuery("#sourceMenuLayer")
                });
                var sourceNode = resource_upAndDown_ztree.getNodeByParam("code", sourceCode, null);

                resource_upAndDown_ztree.selectNode(sourceNode);//显示当前的节点
                resource_upAndDown_ztree.expandNode(sourceNode, true, true, true);//展开当前节点下面的子节点

            })
        }
    }
});