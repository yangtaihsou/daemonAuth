var Authority = (function () {
    var instance;
    var _ = function () {
        var me = this;
        // 事件对象

        me.handler = {
            callbacks: {
                queryResources: function (data) {
                    $('#content').html(data);
                },
                toEditResources: function (data) {
                    $('#content').html(data);
                },
                toDeleteResources: function (data, event) {
                    if (data.status == true) {
                        alert("删除成功");
                        me.closeTree.request();
                    } else {
                        alert(data.reason);
                    }
                    $(event.target).parent().parent().remove();
                },
                queryRoles: function (data) {
                    $('#content').html(data);
                },
                queryUsers: function (data) {
                    $('#content').html(data);
                },
                toEditRoles: function (data) {
                    $('#content').html(data);
                },
                queryRolesResources: function (data) {
                    $('#content').html(data);
                },
                toEditRolesResources: function (data) {
                    $('#content').html(data);
                },
                queryUsersRoles: function (data) {
                    $('#content').html(data);
                },
                toEditUsersRoles: function (data) {
                    $('#content').html(data);
                },
                toEditUsers: function (data) {
                    $('#content').html(data);
                },
                saveRoleResourcesTree: function (data) {
                    if (data.status == true) {
                        alert("保存成功");
                        me.closeTree.request();
                    } else {
                        alert(data.reason);
                    }
                },
                saveUsersRolesTree: function (data) {
                    if (data.status == true) {
                        alert("保存成功");
                        me.closeTree.request();
                    } else {
                        alert(data.reason);
                    }
                },
                updateEnable: function (data, event) {
                    if (data.status == true) {
                        var eventTarget = $(event['target']);
                        alert(me.showEnableTip[$(eventTarget).html()].tip);
                        $(eventTarget).attr("class", me.showEnableTip[$(eventTarget).html()].class);//修改样式
                        $(eventTarget).html(me.showEnableTip[$(eventTarget).html()].text);//修改文字展示
                    } else {
                        alert(data.reason);
                    }
                },
                updateDisplay: function (data) {
                    if (data.status == true) {
                        alert("保存成功");
                        me.closeTree.request();
                    } else {
                        alert(data.reason);
                    }
                }
            },
            callPara: {
                queryResources: function () {
                    var paraData = $('#queryResourcesForm').serialize();
                    return paraData;
                },
                toEditResources: function () {
                    return "";
                },
                toDeleteResources: function () {
                    return "";
                },
                queryRoles: function () {
                    var paraData = $('#queryRolesForm').serialize();
                    return paraData;
                },
                queryUsers: function () {
                    var paraData = $('#queryUsersForm').serialize();
                    return paraData;
                },
                toEditRoles: function () {
                    return "";
                },
                queryRolesResources: function () {
                    var paraData = $('#queryRolesResourcesForm').serialize();
                    return paraData;
                },
                toEditRolesResources: function () {
                    return "";
                },
                queryUsersRoles: function () {
                    var paraData = $('#queryUsersRolesForm').serialize();
                    return paraData;
                },
                toEditUsersRoles: function () {
                    return "";
                },
                toEditUsers: function (data) {
                    return "";
                },
                resoucesBranchTree: function () {
                    var paraData = {};
                    var data = $('input[name="parentCode"]').val();
                    paraData.data = "parentCode=" + data;
                    paraData.cascadeCheck = false;
                    return paraData;
                },
                roleResoucesTree: function (event) {
                    //$('input[name="saveTree"]').
                    $('input[name="saveTree"]').attr("handler", "saveRoleResourcesTree");
                    $('input[name="saveTree"]').attr("requestUrl", "/rolesResources/saveBatch");
                    $('input[name="saveTree"]').attr("method", "submit");
                    $('input[name="saveTree"]').attr("action", "post");
                    $('input[name="saveTree"]').attr("roleCode", event["roleCode"].value);
                    var paraData = {};
                    paraData.cascadeCheck = false;
                    paraData.onlyLeafCheck = true;
                    paraData.data = "roleCode=" + event["roleCode"].value;
                    return paraData;
                },
                saveRoleResourcesTree: function (event) {
                    var paraData = '';
                    var roleCode = event["roleCode"].value;
                    var resoucesCode = me.getTreeChecked();
                    paraData = "roleCode=" + roleCode + "&resourceCode=" + resoucesCode;
                    return paraData;
                },
                roleTree: function () {
                    $('input[name="saveTree"]').attr("method", "saveRoleTreeChecked");
                    var paraData = {};
                    /* var data = $('input[name="parentCode"]').val();
                     paraData.data =  "parentCode="+data;*/
                    return paraData;
                },
                userResourcesTree: function (event) {
                    $('input[name="saveTree"]').attr("handler", "saveUsersRolesTree");
                    $('input[name="saveTree"]').attr("requestUrl", "/usersRoles/saveBatch");
                    $('input[name="saveTree"]').attr("method", "submit");
                    $('input[name="saveTree"]').attr("action", "post");
                    $('input[name="saveTree"]').attr("userPin", event["userPin"].value);
                    var paraData = {};
                    paraData.cascadeCheck = false;
                    paraData.onlyLeafCheck = true;
                    paraData.data = "userPin=" + event["userPin"].value;
                    return paraData;
                },
                saveUsersRolesTree: function (event) {
                    var paraData = '';
                    var userPin = event["userPin"].value;
                    var roleCode = me.getTreeChecked();
                    paraData = "userPin=" + userPin + "&roleCode=" + roleCode;
                    return paraData;
                },
                updateEnable: function (event) {
                    return "";
                },
                updateDisplay: function () {
                    var paraData = $('#updateDisplayForm').serialize();
                    return paraData;
                }
            }
        }
        me.showEnableTip = {
            "生效": {"text": "失效", "class": "btn btn-info", "tip": "生效成功"},
            "失效": {"text": "生效", "class": "btn btn-warning", "tip": "失效成功"}
        }
        me.createForm = function () {
            var form = jQuery('<form   name="roleResoucesTreeForm"></form>');
            var oldElement = $('input[name="saveTree"]');
            var newElement = jQuery(oldElement).clone();
            jQuery(newElement).appendTo(form);
            return form;
        }
        me.formRequest = {//数据来源于form
            this_me: this,
            event: '',
            callbackUrl: '',
            content: 'content',
            request: function (event) {
                this.event = event;
                console.log(event['form']);
                var paraData = $('#' + event['form'].value).serialize();
                var action = event["action"].value;
                if (action == 'new') {
                    window.open(this.requestUrl);
                } else {
                    me.ajax(action, event['requestUrl'].value, paraData, this.callback, this);
                }
            },
            callback: function (data) {
                var status = data.status;
                if (status == true) {
                    alert("成功");
                } else {
                    alert(data.reason);
                }
            }
        },

            me.submit = {
                request: function (event) {
                    var action = event["action"].value;
                    if (action == "delete") {
                        if (!window.confirm("要删除吗？")) {
                            return;
                        }
                    }
                    var handler = event['handler'].value;
                    var paraData = me.handler.callPara[handler](event);
                    me.ajax(action, event['requestUrl'].value, paraData, me.handler.callbacks[handler], this, event);
                }
            }
        me.ajax = function (type, url, paraData, fnMethod, fnObj, event) {
            $.ajax({
                type: type,
                url: url,
                data: paraData,
                cache: false,
                async: true,
                success: function (result) {
                    fnMethod.call(fnObj, result, event);
                }
            })
        }

        me.ajaxRequestForcontent = function (url, paraData, domId) {

            $.ajax({
                type: "get",
                url: url,
                async: true,
                data: paraData + "&_t=" + new Date().getTime(),
                complete: function (data) {
                    $('.theme-popover-mask').fadeOut(1);
                    $('#' + domId).html(data.responseText);
                }
            })
        }

        me.openTree = {
            request: function (event) {
                var handler = event['handler'].value;
                var paraData = me.handler.callPara[handler](event);
                me.ajaxTree(event['requestUrl'].value, paraData);
                //   $('#dlg').dialog('open');//使用自带的弹出框报错（需要对页签再次点击才能生效），原因是这种异步加载资源造成的。
                $('.theme-popover-mask').show();
                $('.theme-popover-mask').height($(document).height());
                $('.theme-popover').slideDown(200);
            }
        }


        me.ajaxTree = function (url, para) {
            $('#ttt').tree({
                method: 'get',
                url: url + "?" + para.data + "&_t=" + new Date().getTime(),
                //data:para,
                cascadeCheck: para.cascadeCheck,
                onlyLeafCheck: para.onlyLeafCheck,
                animate: true,
                checkbox: true
            })
        }

        me.closeTree = {
            request: function () {
                $('.theme-popover-mask').hide();
                $('.theme-popover').slideUp(200);
            }
        }
        me.chooseNodetype = {//选择不同类型的节点
            request: function (event) {
                var value = event['value'].value;
                console.log(value);
                if (value == '1') {
                    $('#resourceUrl').hide();
                    $('input[name="resourceUrl"]').val('');
                    $('#parentCode').hide();
                    $('input[name="parentCode"]').val('');
                } else if (value == '2') {
                    $('#parentCode').show();
                    $('#resourceUrl').hide();
                    $('input[name="resourceUrl"]').val('');
                } else if (value == '3') {
                    $('#resourceUrl').show();
                    $('#parentCode').show();
                    $('input[name="parentCode"]').val('');
                } else if (value == '4') {
                    $('#resourceUrl').show();
                    $('#parentCode').show();
                    $('input[name="parentCode"]').val('');
                }
            }
        }
        me.saveRoleTreeChecked = {
            request: function (event) {
                $('input[name="roleCode"]').val(me.getTreeChecked());
                me.closeTree.request();
            }
        }

        me.saveTreeChecked = {
            request: function (event) {
                $('input[name="parentCode"]').val(me.getTreeChecked());
                me.closeTree.request();
            }
        }

        me.getTreeChecked = function () {
            var nodes = $('#ttt').tree('getChecked');
            var s = '';
            for (var i = 0; i < nodes.length; i++) {
                if (s != '') s += ',';
                s += nodes[i].id;
            }
            console.log(s);
            return s;
        }

        var map = new Map();
        me.initTopMenusHTML = function () {
            map.put("resourcesList", '   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/resources/list.do">资源列表</a></li>' +
                '<li><a href="javascript:void(0)"  src="/resources/toAdd.do">资源添加</a></li>');
            map.put("rolesList", '   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/roles/list.do">角色列表</a></li>' +
                '<li><a href="javascript:void(0)"  src="/roles/toAdd.do">角色添加</a></li>');
            map.put("usersList", '   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/users/list.do">用户列表</a></li>' +
                '<li><a href="javascript:void(0)"  src="/users/toAdd.do">用户添加</a></li>');
            map.put("rolesResourcesList", '   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/rolesResources/list.do">资源角色列表</a></li>');
            map.put("usersRolesList", '   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/usersRoles/list.do">用户角色列表</a></li>');
        }
        me.topMenu = {
            html: '<a class="blog-nav-item " href="javascript:void(0)" id="resourcesList">资源管理</a>' +
                '<a class="blog-nav-item " href="javascript:void(0)" id="rolesList">角色管理</a>' +
                '<a class="blog-nav-item " href="javascript:void(0)" id="usersList">用户管理</a>' +
                '<a class="blog-nav-item " href="javascript:void(0)" id="rolesResourcesList">资源角色管理</a>' +
                '<a class="blog-nav-item " href="javascript:void(0)" id="usersRolesList">用户角色管理</a>'
        }
        me.getHTML = function (key) {
            return map.get(key);
        }
        me.loadDefaultSidebar = function () {//加载菜单
            me.initTopMenusHTML();
            $('#topSidebar').html(me.topMenu.html);//top menu
            $('#leftSidebar').html(me.getHTML('resourcesList'));//左侧菜单
        }
        me.loadDefaultPage = function () {//加载默认显示的页面
            var a = $('#topSidebar').find("a")[0];
            $(a).click();
            var li = $('#leftSidebar').find("li")[0];
            $(li).click();
        }
    }
    return {
        getInstance: function () {
            if (!instance) {
                instance = new _();
            }
            return instance;
        }
    }


})()

var Event = function () {
    this.target;
    this.obj = {};
    this.setSource = function (objs) {
        this.obj = objs.attributes;
        this.target = objs;
        this.obj.target = objs;
    }
    this.getSource = function () {
        return this.obj;
    }
    this.getTarget = function () {
        return this.target;
    }
}