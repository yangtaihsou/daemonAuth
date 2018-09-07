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
        user: {
            id: null,
            userPin: "",
            userName: "",
            email: "",
            tel: "",
            isChanged: false,
            roleList: []
            //  disabled:true
        },
        oldRoleList: []
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
            $.get("/users/info/" + id, function (r) {
                vm.user.id = r.user.id;
                vm.user.userPin = r.user.userPin;
                console.log(vm.user.userPin)
                vm.user.userName = r.user.userName;
                vm.user.email = r.user.email;
                vm.user.tel = r.user.tel;
                vm.user.roleList = r.roleIds;
                vm.oldRoleList = r.roleIds;
                vm.user.readonly = true;
                //加载菜单树
                vm.loadTree(true);
            });
        },
        loadTree: function (isUpdate) {
            $.get("/tree/roleTree", function (r) {
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.roleList);
                ztree.expandAll(false);
                if (isUpdate) {
                    var menuIds = vm.user.roleList;
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
            var url = (vm.user.id == null || vm.user.id == '') ? "/users/batchSave" : "/users/batchUpdate";

            //获取选择的菜单
            var nodes = ztree.getCheckedNodes(true);
            var roleIdList = new Array();
            for (var i = 0; i < nodes.length; i++) {
                roleIdList.push(nodes[i].code + ":" + nodes[i].name);
            }
            vm.user.roleList = roleIdList;
            if (JSON.stringify(vm.oldRoleList) != JSON.stringify(roleIdList)) {
                vm.user.isChanged = true;
            }

            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
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