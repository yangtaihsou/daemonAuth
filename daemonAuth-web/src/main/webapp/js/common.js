//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};
T.p = url;

//全局配置
$.ajaxSetup({
    dataType: "json",
    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    cache: false
});

//重写alert
window.alert = function (msg, callback) {
    parent.layer.alert(msg, function (index) {
        parent.layer.close(index);
        if (typeof(callback) === "function") {
            callback("ok");
        }
    });
}

//重写confirm式样框
window.confirm = function (msg, callback) {
    parent.layer.confirm(msg, {btn: ['确定', '取消']},
        function () {//确定事件
            if (typeof(callback) === "function") {
                callback("ok");
            }
        });
}

window.addTab = function (opt) {
    if (self.frameElement && self.frameElement.tagName == "IFRAME") {
        parent.$('#container').addtabs({iframeHeight: opt.height}).add({
            'id': opt.id,
            "url": opt.url,
            title: opt.title
        });
    } else {
        open(opt);
    }

};

window.closeTab = function (opt) {
    parent.$.addtabs.close(opt);
};

window.open = function (options) {
    if (!options.width) {
        options.width = '800'
    }
    parent.layer.open({
        type: 2,
        title: options.title,
        shadeClose: true,
        shade: 0.8,
        area: [options.width + 'px', options.height + 'px'],
        content: options.url
    });
};

/**
 * 跳转更新页
 * @param url
 * @param id
 */
function toUpdatePage(url, id) {

    if (id == null) {
        id = getSelectedRow();
    }

    if (id == null) {
        return;
    }

    location.href = url + id;
}

/**
 * 确认对话框ajax请求
 * @param opt
 */
function confirmAjax(opt) {

    if (!opt.data) {
        opt.data = getSelectedRows();
    }

    if (!opt.data) {
        return;
    }

    if (!opt.type) {
        opt.type = "POST";
    }
    confirm(opt.msg, function () {
        $.ajax({
            type: opt.type,
            url: opt.url,
            data: opt.data,
            success: function (r) {
                if (r.status) {
                    alert('操作成功', function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                    });
                } else {
                    alert(r.msg);
                }
            }
        });
    });
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }

    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    return grid.getGridParam("selarrrow");
}

//获取行数据
function getSelectRowData(rowId) {
    if (!rowId) {
        return;
    }
    return $("#jqGrid").jqGrid('getRowData', rowId);
}

//获取当月，格式：201702
function getCurMonth() {
    var curDate = new Date();
    var year = curDate.getFullYear();
    var month = curDate.getMonth() + 1;
    var ym = "" + year;
    if (month < 10) {
        ym += "0";
    }
    ym += month;
    return ym;
}

//获取当天，格式：20170227
function getCurDay() {
    var curDate = new Date();
    var day = curDate.getDate();
    var ym = getCurMonth();
    if (day < 10) {
        ym += "0";
    }
    ym += day;
    return ym;
}

window.grid = function (opt) {
    $("#" + opt.id).jqGrid({
        url: opt.url,
        datatype: "json",
        colModel: opt.colModel,
        viewrecords: true,
        height: 400,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: false,
        pager: "#" + opt.id + "Pager",
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
            $("#" + opt.id).closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
}
