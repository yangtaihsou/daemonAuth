var DaemonAuthority = (function () {
    var instance;
    var _ = function () {
        var me = this;
        me.urlOfPinMap =new Map();//存放当前登录用户的所有url


        me.auth = function(para){
            me.loadMenu(para);
            me.loadAuthUrlMap(para['uriLoadUrl']);
        }

        me.loadMenu = function(para){//加载当前用户的menu的HTML
           var loadMenuUrl=para['menuLoadUrl'];
          var  menuId=para['menuId'];
           var methods = para['loadAfterMenuMethod'];
            $.ajax({
                type: "GET",
                url: loadMenuUrl,
                success: function(r){
                    if(r.code == 0){
                        // console.log(r.menu);
                        $('#'+menuId).append(r.menu);
                        //menu_resourceClick();
                        for(var index in methods) {
                            //console.log(index);
                            methods[index]();
                        }
                    }else{
                        alert(r.msg);
                    }
                }
            });
        }
        me.loadAuthUrlMap = function(loadAuthUrl){

            $.ajax({
                type: "GET",
                url: loadAuthUrl,
                success: function(r){
                    if(r.code == 0){
                        for(var index in r.uriResourceList){
                            //  console.log(r.buttonResourceList[index]);
                            me.urlOfPinMap.put(r.uriResourceList[index],'');
                        }
                    }else{
                        alert(r.msg);
                    }
                }
            });
        }
        me.ObserverDomChange = function(domNodeId){//监听页面dom指定节点区域变动（ajax异步完成如果操作了dom，可以监听到）
            //可以监听到属性、文本内容、节点插入删除、子节点变化等事件。可是该事件 W3C 已废弃，虽然一些浏览器仍然支持，但不建议使用。
            //参考文档http://www.cnblogs.com/snandy/archive/2016/04/10/5362824.html
            var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
            var text = document.querySelector("#"+domNodeId);
            var observer = new MutationObserver(function(mutations) {
                me.checkElementAuth();
            });
            observer.observe(text, {
                childList:true,//子元素的变动
                subtree: true//所有下属节点（包括子节点和子节点的子节点）的变动
            });
        }
        me.checkElementAuth=function(){//检查当前页面的按钮是否展示。只检查添加了自定义属性authurl的控件
            $(":input").each(function(){//包括button select input radio等各种控件(不包括超链接控件)
                var requestUrl = $(this).attr("authurl");
                if(requestUrl){
                    if(requestUrl&&me.urlOfPinMap.get(requestUrl)!=''){
                        //console.log(':input hide url:'+requestUrl);
                        $(this).hide();
                    }
                    //console.log(requestUrl);
                }
            });

            $("a").each(function(){
                var requestUrl = $(this).attr("authurl");
                if(requestUrl&&me.urlOfPinMap.get(requestUrl)!=''){
                    //console.log('a hide url:'+requestUrl);
                    $(this).hide();
                }

            });
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