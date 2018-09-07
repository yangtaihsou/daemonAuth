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
            parentName: "",
            resourceId: ""
        }
    },
    created: function () {
        if (id != null) {
            this.title = "修改";
            this.getInfo(id)
        } else {

            this.loadTree(false);
        }


    },
    methods: {

        getInfo: function (id) {
            $.get("/resources/info/" + id, function (r) {
                vm.resources.resourceCode = r.resources.resourceCode;
                vm.resources.resourceName = r.resources.resourceName;
                vm.resources.nodeType = r.resources.nodeType;
                vm.resources.parentName = r.resources.parentName;
                vm.resources.resourceUrl = r.resources.resourceUrl;
                vm.resources.resourceIcon = r.resources.resourceIcon;
                vm.resources.id = r.resources.id;
                vm.resources.parentCode = r.resources.parentCode;
                vm.resources.resourceId = r.resources.resourceId;
                //加载菜单树
                vm.loadTree(true);
            });
        },
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
            var url = (vm.resources.id == null || vm.resources.id == '') ? "/resources/save" : "/resources/edit";
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