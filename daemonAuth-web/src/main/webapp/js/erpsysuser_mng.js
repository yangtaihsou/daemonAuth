/**
 * Created by guojianwei on 2017/7/11.
 */
/**
 * Created by guojianwei on 2017/7/11.
 */
$(function () {
    $("#jqGrid").jqGrid({
        url: '/erpSysUser/pageQuery',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', width: 80},
            {label: 'erp', name: 'userErp', width: 80},
            {label: '用户名字', name: 'username', width: 80},
            {label: '用户类型', name: 'userType', formatter: 'select', formatoptions: {value: dics.userType}, width: 80},
            {label: '状态', name: 'enable', formatter: 'select', formatoptions: {value: dics.status}, width: 80},
            {label: '系统编码', name: 'sysCodes', width: 80},
            {label: '创建时间', name: 'createDate', width: 80},
            {label: '修改时间', name: 'updateTime', width: 80},
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
                    //btns.push('<a class="btn btn-xs btn-success" href="javascript:void(0);" onclick="vm.delete('+options.rowId+')">删除</a>');
                    return btns.join(' ');
                }
            }
        ],
        viewrecords: true,
        height: 400,
        rowNum: 10,
        rowList: [10, 30, 50],
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
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        params: {
            nodeType: ""
        },
        resources: []
    },
    created: function () {
        this.loadDic();
    },
    methods: {

        loadDic: function () {
            $.get("/roles/resourceDic", function (r) {
                vm.resources = r.resourcesRootCode;
            });
        },
        query: function () {
            $("#jqGrid").jqGrid('setGridParam', {
                postData: vm.params,
                page: 1
            }).trigger("reloadGrid");
        },
        create: function () {


            var opt = {
                url: "/sys/erpsysuser_add.html#800",
                id: 'erpsysuser_add',
                title: '创建本系统用户',
                height: 800
            };

            addTab(opt);

            //window.location = "/sys/role_add.html?id="+id;
        },
        update: function (id) {


            var opt = {
                url: "/sys/erpsysuser_add.html?id=" + id + "#800",
                id: 'erpsysuser_add' + id,
                title: id + '.本系统用户维护',
                height: 800
            };

            addTab(opt);

            //window.location = "/sys/role_add.html?id="+id;
        },
        changeStatus: function (id, status) {
            $.ajax({
                type: "PUT",
                url: '/erpSysUser/updateEnable?id=' + id + '&enable=' + status,
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
        delete: function (v_id) {

            var opt = {
                url: "/erpSysUser/delete?id=" + v_id,
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
        }
    }
});