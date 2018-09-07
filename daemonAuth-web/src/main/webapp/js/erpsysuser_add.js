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
        erpSysUser: {
            userErp: "",
            username: "",
            userType: null,
            operateErp: "",
            enable: null,
            createDate: null,
            sysCodes: "",
            sysCodesList: [],
            id: null,
            updateTime: null
        },
        resources: []
    },
    created: function () {

        if (id != null) {
            this.title = "修改";
            this.getInfo(id)
        } else {

            this.loadDic();
            /*  this.loadTree(false);*/
        }


    },
    methods: {

        selectVal: function () {
            console.log(vm.erpSysUser.sysCodesList);
        },
        getInfo: function (id) {
            $.get("/erpSysUser/info/" + id, function (r) {
                vm.erpSysUser.userErp = r.erpSysUser.userErp;
                vm.erpSysUser.username = r.erpSysUser.username;
                vm.erpSysUser.userType = r.erpSysUser.userType;
                vm.erpSysUser.id = r.erpSysUser.id;
                vm.erpSysUser.sysCodes = r.erpSysUser.sysCodes;
                // console.log( vm.erpSysUser.sysCodes );

                //加载菜单树

                //    vm.loadTree(true);

                vm.loadDic();
            });
        },

        loadDic: function () {
            $.get("/roles/resourceDic", function (r) {

                var sysCodes = vm.erpSysUser.sysCodes;
                console.log(r);
                $(r.resourcesRootCode).each(function (text, ele) {
                    if (vm.erpSysUser.sysCodes.indexOf(ele.resourceCode) >= 0) {
                        ele.checked = true;
                    }
                    //ele.value=ele.resourceCode;
                });
                vm.resources = r.resourcesRootCode;

                console.log(vm.resources);


            });
        },
        saveOrUpdate: function (event) {
            var url = (vm.erpSysUser.id == null || vm.erpSysUser.id == '') ? "/erpSysUser/save" : "/erpSysUser/edit";
            //vm.erpSysUser.sysCodes = vm.erpSysUser.sysCodesList.join(",");
            console.log(vm.resources);
            vm.erpSysUser.sysCodes = "";
            $(vm.resources).each(function (text, ele) {
                if (ele.checked) {
                    vm.erpSysUser.sysCodes = vm.erpSysUser.sysCodes + ele.resourceCode + ",";
                }
            });
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                data: vm.erpSysUser,
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
