/**
 * Created by guojianwei on 2017/7/12.
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
    },
    check: {
        enable: true,
        nocheckInherit: true
    }
};
var ztree;

var id = T.p("id");
var vm = new Vue({
    el: '#rrapp',
    data: {
        title: "新增",
        roles: {
            id: null,
            roleCode: "",
            roleName: "",
            systemCode: "",
            isChanged: false,
            resourceList: []
        },
        oldResourceList: [],
        resources: []
    },
    created: function () {

        this.loadDic();

        if (id != null) {
            this.title = "修改";
            this.getInfo(id)
        }


    },
    methods: {

        selectVal: function (ele) {

            var systemCode = ele.target.value;
            vm.loadTree(true, systemCode);
            $("#authorization").show();
        },
        loadDic: function () {
            $.get("/roles/resourceDic", function (r) {
                vm.resources = r.resourcesRootCode;
            });
        },
        getInfo: function (id) {
            $.get("/roles/info/" + id, function (r) {
                vm.roles.id = r.roles.id;
                vm.roles.roleCode = r.roles.roleCode;
                vm.roles.roleName = r.roles.roleName;
                vm.roles.systemCode = r.roles.systemCode;
                vm.oldResourceList = r.resourceIds;
                vm.roles.resourceList = r.resourceIds;
                //加载菜单树
                vm.loadTree(true, r.roles.systemCode);
            });
        },
        loadTree: function (isUpdate, systemCode) {
            $.get("/tree/resourceTree?parentCode=" + systemCode, function (r) {
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.treeList);
                ztree.expandAll(false);
                if (isUpdate) {
                    $("#authorization").show();
                    var menuIds = vm.roles.resourceList;
                    for (var i = 0; i < menuIds.length; i++) {
                        var codeName = menuIds[i].split(':');
                        var node = ztree.getNodeByParam("code", codeName[0]);
                        if (node != null) {
                            ztree.checkNode(node, true, false);
                            ztree.selectNode(node);//显示当前的节点
                        }

                    }

                }

            })
        },
        saveOrUpdate: function (event) {
            var url = (vm.roles.id == null || vm.roles.id == '') ? "/roles/batchSave" : "/roles/batchUpdate";

            //获取选择的菜单
            var nodes = ztree.getCheckedNodes(true);
            var resourceIdList = new Array();
            for (var i = 0; i < nodes.length; i++) {
                resourceIdList.push(nodes[i].code + ":" + nodes[i].name);
            }
            vm.roles.resourceList = resourceIdList;

            if (JSON.stringify(vm.oldResourceList) != JSON.stringify(resourceIdList)) {
                vm.roles.isChanged = true;
            }

            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(vm.roles),
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