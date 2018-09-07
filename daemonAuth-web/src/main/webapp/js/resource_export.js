/**
 * Created by guojianwei on 2017/7/11.
 */

var setting = {
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
var ztree;

var id = T.p("id");
var vm = new Vue({
    el: '#rrapp',
    data: {
        title: "新增",
        resources: {
            resourceCode: "",
            resourceName: "",
            nodeType: null,
            parentName: "",
            resourceUrl: "",
            resourceIcon: "",
            id: null,
            parentCode: "",
            parentName: ""
        }
    },
    created: function () {


        console.log(123);
        this.loadTree(false);
    },
    methods: {


        loadTree: function (isUpdate) {
            $.get("/tree/parentTree", function (r) {

                ztree = $.fn.zTree.init($("#menuTree"), setting, r.treeList);
                if (isUpdate) {
                    var node = ztree.getNodeByParam("code", vm.resources.parentCode);
                    ztree.selectNode(node);
                    vm.resources.parentName = node.name;
                }

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
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.resources.parentCode = node[0].code;
                    vm.resources.parentName = node[0].name;
                    layer.close(index);
                }
            });
        },
        saveOrUpdate: function (event) {
            var url = "/resources/batchExport";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                data: vm.resources,
                success: function (r) {
                    if (r.status) {
                        alert('操作成功', function (index) {
                            //vm.back();
                        });
                    } else {
                        alert(r.reason);
                    }
                }
            });
        },
        back: function (event) {
            history.go(-1);
        }
    }
});