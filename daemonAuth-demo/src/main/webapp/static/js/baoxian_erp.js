var baoxianErp=(function(){
    var instance;
    var Erp=function(){
        var me = this;
        me.map = new Map();
     /*   me.initTopMenusHTML = function(){//填充每个topmenu的html
            map.put("productList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/product/list.do">产品列表</a></li>'+
                ' <li><a href="javascript:void(0)"  src="/product/toAdd.do">产品新增</a></li>');
            map.put("priceList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/productPrice/list.do">价格列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/productPrice/toAdd.do">价格导入</a></li>');
            map.put("broadcastAlbumList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/broadcastAlbum/list.do">轮播图片列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/broadcastAlbum/toAdd.do">轮播图片上传</a></li>'+
                '<li><a href="javascript:void(0)"  src="/broadcastAlbum/toEdit.do">轮播图片编辑</a></li>');
            map.put("orderList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/order/list.do">订单列表</a></li>');
            map.put("saleList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/sale/list.do">库存列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/sale/toAdd.do">库存管理</a></li>');
            map.put("jobCodeList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/jobcode/list.do">职业列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/jobcode/toAdd.do">职业代码导入</a></li>');
            map.put("healthToldList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/healthtold/list.do">健康告知列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/healthtold/toAdd.do">健康告知新增</a></li>');


            map.put("companyList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/insurancecompany/list.do">公司列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/insurancecompany/toAdd.do">公司新增</a></li>');

            map.put("travelDestList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/traveldest/list.do">旅游国家列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/traveldest/toAdd.do">旅游列表导入</a></li>');
            map.put("resourcesList",'   <li class="active" style="background-color: #f9f9f9" ><a href="javascript:void(0)" src="/resources/list.do">资源列表</a></li>'+
                '<li><a href="javascript:void(0)"  src="/resources/toAdd.do">资源添加</a></li>');

        }*/
        me.loadDefaultSidebar=function(){//加载菜单
            //me.initTopMenusHTML();
            $('#topSidebar').html(me.topMenu.html);//top menu
            $('#leftSidebar').html(me.getHTML('productList'));//左侧菜单
        }
        me.loadDefaultPage=function(){//加载默认显示的页面
            var a = $('#topSidebar').find("a")[0];
            $(a).click();
            var li = $('#leftSidebar').find("li")[0];
            $(li).click();
        }
        me.ajaxRequestForcontent =function (url,paraData,domId){

            $.ajax({
                type : "get",
                url :url,
                async:true,
                data:paraData+"&_t="+new Date().getTime(),
                complete : function(data){
                    $('.theme-popover-mask').fadeOut(1);
                    $('#'+domId).html(data.responseText);
                }
            })
        }

        me.topMenu={
            html:
                '<a class="blog-nav-item " href="javascript:void(0)" id="productList">产品管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="priceList">价格管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="companyList">保险公司管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="broadcastAlbumList">轮播相册管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="orderList">订单管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="saleList">库存管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="jobCodeList">职业代码管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="healthToldList">健康告知管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="travelDestList">旅游目的地管理</a>'
                    +'<a class="blog-nav-item " href="javascript:void(0)" id="resourcesList">旅游目的地管理</a>'
        }

        me.addDynamicTable = function(id){
            var trObj = $('#'+id+'Div tr:last-child');//从隐藏域里得到
            $('#'+id+'  table').append("<tr>"+$(trObj).html()+"</tr>");
            this.reloadIndex(id);
        }


        me.reloadIndex =function( id){//重新生成动态表格的下标

            var index = 0;
            var trCount = $('#'+id).find('tr').length;//动态表格的行数
            $('#'+id).find('tr').each(function(){//循环动态表格行
                var trObjEach = this;
                $(trObjEach).find(":input").each(function(){//循环动态表格具体行里所有的组件
                    var name = $(this).attr("name");
                    if(typeof(name) == 'undefined'){
                        return;
                    }
                    if(name.indexOf('.')<0){
                        return;
                    }
                    var nameArr = name.split(".");
                    var nameClass = nameArr[0];
                    if(nameClass.split("[").length!=0){//用于编辑状态下，页面加载时重新计算下标（加载进来时，下标是1开始的）
                        nameClass =  nameClass.split("[")[0];
                    }
                    if(nameArr.length>=2){
                        name = nameClass+"["+index+"]."+nameArr[1];
                        $(this).attr("name",name);
                    }
                    //如果有id，则只要生成不一样即可
                    var id = $(this).attr("id");
                    if(typeof(id) != 'undefined'){
                        $(this).attr("id",id+index);
                    }

                    var tag = $(this).attr("tag");
                    if(typeof(tag) != 'undefined'){
                        if(tag=="optionSort"){
                            var optionSelectedValue = $(this).val();
                          // var defaultText =$(this+' option[value=""]').text();
                            var defaultText =$(this).find(' option[value=""]').text();
                            $(this).html("");
                            $(this).append("<option value=''>"+defaultText+"</option>");
                            for(var optionIndex=1;optionIndex<trCount+1;optionIndex++){
                                var selected;
                                if(optionIndex==optionSelectedValue) {
                                    selected="selected";
                                } else{
                                    selected=""
                                }
                                $(this).append("<option value='"+optionIndex+"' "+selected+">"+optionIndex+"</option>");
                            }
                        }
                    }
                });
                index ++;
            })
        }



        me.getHTML = function(key){
            return me.map.get(key);
        }
        me.dynamicTableClick={//点击按钮增加动态表格
            request:function(obj){
                me.addDynamicTable( $(obj).attr("tag"));
            }
        }

        me.delDynamicTable={//点击按钮删除动态表格
            request:function(obj){
                $(obj).parent().parent().remove();
                me.reloadIndex( $(obj).attr("tag"));
            }
        }

        me.limitShow={//是否需要选择时，div的展示与隐藏
            request:function(obj){
                var checkedVal = $(obj).val();
                var div = $(obj).attr('div');
                if(checkedVal=='true'){
                    $('#'+div).show();
                }else{
                    $('#'+div).hide();
                    DocumentElementMap.clear(div);

                }
            }
        }

        me.checkAllLimitAreas={
            request:function(obj){
                if($(obj).attr("checked")=='checked'){
                 $( "input[name='limitAreas']" ).check();
                }else{
                    $( "input[name='limitAreas']" ).uncheck();
                }
            }
        }

        /*导入价格*/
        me.toImportPrices ={
            request: function(paraData){
                return me.ajaxRequestForcontent("/productPrice/toAdd.do",paraData,'content');
            }
        }

        me.queryOrderDetail = {
            request:function(obj){
                var value = $(obj).attr("value");
                var paraData = "orderId="+value;
                return me.ajaxRequestForcontent("/order/detail.do",paraData,'content');
            }
        }

        me.queryHealthToldList={
            request: function(paraData){
                var productCode = $(':input[name=productCode]').val();
                paraData =  "productCode="+productCode;
                return me.ajaxRequestForcontent("/healthtold/list.do",paraData,'content');
            }
        }
        me.queryPriceList ={
            request: function(paraData){
                paraData =  $('#queryPriceListForm').serialize();
                return me.ajaxRequestForcontent("/productPrice/list.do",paraData,'content');
            }
        }

        me.queryJobcodeList ={
            request: function(paraData){
                var groupName = $(':input[name=groupName]').val();
                paraData =  "groupName="+groupName;
                return me.ajaxRequestForcontent("/jobcode/list.do",paraData,'content');
            }
        }



        me.queryOrderList ={
            request: function(paraData){
                var pin = $(':input[name=pin]').val();
                paraData =  "pin="+pin;
                return me.ajaxRequestForcontent("/order/list.do",paraData,'content');
            }
        }



        me.testProductCode={
            request: function(){
                var cooperateCompanyCode = $(':input[name=cooperateCompanyCode]').val();
                var productCode =  $('input[name=productCode]')[0].value;
                if(productCode==""||productCode==""){
                    alert("请输入产品编码");
                    return false;
                }
                if(cooperateCompanyCode==""||cooperateCompanyCode==""){
                    alert("请输入店铺");
                    return false;
                }
                me.getAjaxCallBack('/product/existTest.do?productCode='+productCode+"&cooperateCompanyCode="+cooperateCompanyCode,this.callback);
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("可以使用");
                } else {
                    alert(data.reason);
                }
            }
        }

        me.onSaleStatus={

            showTip:{
                "下架隐藏":{"text":"上架展示","class":"btn btn-warning","tip":"下架成功"},
                "上架展示":{"text":"下架隐藏","class":"btn btn-info","tip":"上架成功"}
            },
            request: function(obj){
                var tag = $(obj).attr("tag");
                var value = $(obj).attr('value');
                var id =  $(obj).attr('key');
                this.obj = obj;
                var paraData = tag+"="+value+"&id="+id;
                me.postAjaxCallBack(paraData,'/product/onSaleStatus.do',this.callback,this);
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert(this.showTip[$(this.obj).html()].tip);
                    $(this.obj).attr("class",this.showTip[$(this.obj).html()].class);//修改样式
                    $(this.obj).html(this.showTip[$(this.obj).html()].text);//修改文字展示
                    console.log($(this.obj).html());
                } else {
                    alert(data.reason);
                }
            }
        }
        me.onlineStatus={
               showTip:{
             "上线销售":{"text":"下线停售","class":"btn btn-warning","tip":"上线成功"},
             "下线停售":{"text":"上线销售","class":"btn btn-info","tip":"下线成功"}
             },
            request: function(obj){
                var tag = $(obj).attr("tag");
                var value = $(obj).attr('value');
                var id =  $(obj).attr('key');
                this.obj = obj;
                var paraData = tag+"="+value+"&id="+id;
                me.postAjaxCallBack(paraData,'/product/onlineStatus.do',this.callback,this);
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert(this.showTip[$(this.obj).html()].tip);
                    $(this.obj).attr("class",this.showTip[$(this.obj).html()].class);//修改样式
                    $(this.obj).html(this.showTip[$(this.obj).html()].text);//修改文字展示
                    console.log($(this.obj).html());
                } else {
                    alert(data.reason);
                }
            }
        }

        me.addInsurancecompany={
            request: function(){
                var paraData = $('#insurancecompanyAddForm').serialize();
                me.postAjaxCallBack(paraData,'/insurancecompany/add.do',this.callback,"");
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("增加公司成功");
                    me.ajaxRequestForcontent('/insurancecompany/list.do','','content');
                } else {
                    alert(data.reason);
                }
            }
        }

        me.productEditSubmit={
            request: function(){
                var paraData = $('#productEditForm').serialize();
                me.postAjaxCallBack(paraData,'/product/modify.do',this.callback,"");
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("修改产品成功");
                    me.ajaxRequestForcontent('/product/list.do','','content');
                } else {
                    alert(data.reason);
                }
            }
        }

        me.healthToldAdd={
            request: function(){
                var paraData = $('#healthToldAddForm').serialize();
                me.postAjaxCallBack(paraData,'/healthtold/add.do',this.callback,"");
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("增加健康告知成功");
                    me.ajaxRequestForcontent('/healthtold/list.do','','content');
                } else {
                    alert(data.reason);
                }
            }
        }

        me.broadcastAlbumAdd={
            request: function(){
                var paraData = $('#broadcastAlbumAddForm').serialize();
                me.postAjaxCallBack(paraData,'/broadcastAlbum/add.do',this.callback,"");
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("增加图片成功");
                    me.ajaxRequestForcontent('/broadcastAlbum/list.do','','content');
                } else {
                    alert(data.reason);
                }
            }
        }

        me.call={
            callbacks:{
            'queryProductName':function(data){
                $('div[name="productName"]').html(data.reason);
                $('button[name="add"]').attr("disabled","disabled");
                if(data.reason!=''&&data.reason!=null){
                    $('button[name="add"]').removeAttr("disabled");
                }
            },
                'queryProductCategory':function(data){
                    if(data.content!=''&&data.content!=null){
                        $(':input[name="second"]').html(data.content);
                    }
                },
                'queryProductAgeCondition':function(data){
                    if(data.status==false){
                        alert(data.reason);
                    }else if(data.count>20){
                        if(confirm("信息大于20行，要展示吗")){
                            $('#ageSelectionsTable').html(data.content);
                        }
                    }else{
                        $('#ageSelectionsTable').html(data.content);
                    }
                },
                'queryProductTimeCondition':function(data){
                    if(data.status==false){
                        alert(data.reason);
                    }else if(data.count>20){
                        if(confirm("信息大于20行，要展示吗")){
                            $('#timeSelectionsTable').html(data.content);
                        }
                    }else{
                        $('#timeSelectionsTable').html(data.content);
                    }
                },
                'queryProductInsureAmountCondition':function(data){
                    if(data.status==false){
                        alert(data.reason);
                    }else if(data.count>20){
                        if(confirm("信息大于20行，要展示吗")){
                            $('#insureAmountSelectionsTable').html(data.content);
                        }
                    }else{
                        $('#insureAmountSelectionsTable').html(data.content);
                    }
                },
                'queryProdudctDetail':function(data){
                    if(data.status==false){
                        alert(data.reason);
                    }else{
                        var ue = UE.getEditor('editor');
                        ue.ready(function() {
                            ue.setContent(data.content);
                        });
                    }
                }

            },
            callPara:{
                'queryProductName':function(){
                    var productCode = $('input[name="productCode"]').val();
                    return "key="+productCode;
                },
                'queryProductCategory':function(){
                    var first = $(':input[name="first"]').val();
                    return "first="+first;
                },
                'queryProductAgeCondition':function(){
                    var productCode = $('input[name="productCode"]').val();
                    return "productCode="+productCode;
                },
                'queryProductTimeCondition':function(){
                    var productCode = $('input[name="productCode"]').val();
                    return "productCode="+productCode;
                },
                'queryProductInsureAmountCondition':function(){
                    var productCode = $('input[name="productCode"]').val();
                    return "productCode="+productCode;
                },
                'queryProdudctDetail':function(){
                    var id = $(':input[name="id"]').val();
                    var productCode = $(':input[name="productCode"]').val();
                    var detailType = $(':input[name="detailType"]').val();
                    return "detailType="+detailType+"&productCode="+productCode+"&id="+id;
                }
            }
        }


        me.modelAndUpdate={
            this_me:this,
            requestUrl:'',
            callbackUrl:'',
            content:'content',
            request: function(obj){
                var formName = $(obj).attr("form");
                this.requestUrl = $(obj).attr("requestUrl");
                this.callbackUrl = $(obj).attr("callbackUrl");

                var paraData = $('#'+formName).serialize();
                var action = $(obj).attr("action");
                if(action=='post'){
                    me.postAjaxCallBack(paraData, this.requestUrl, this.callback,"");
                }
                if(action=='get'){
                    me.getAjaxCallBack(this.requestUrl, this.callback,this);
                }
                if(action=='direct'){
                    me.ajaxRequestForcontent(this.requestUrl,'',this.content);
                }
                if(action=='new'){
                 window.open(this.requestUrl);
                }
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("成功");
                    me.ajaxRequestForcontent( me.modelAndUpdate.callbackUrl,'',me.modelAndUpdate.content);//这里this很容易变成执行了callback的对象，所以使用call的时候注意。
                } else {
                    alert(data.reason);
                }
            }
        }

        me.queryWithBeforeAndCallBack={
            this_me:this,
            requestUrl:'',
            callbackUrl:'',
            content:'content',
            request: function(obj){
                this.requestUrl = $(obj).attr("requestUrl");
                this.callbackUrl = $(obj).attr("callbackUrl");
                var checkMethod = $(obj).attr("checkMethod");
                var paraData = me.call.callPara[checkMethod]();
                me.getAjaxCallBack(this.requestUrl+"?"+paraData, me.call.callbacks[checkMethod],this);
            }
        }


        me.addSale={
            request: function(){
                var paraData = $('#saleAddForm').serialize();
                me.postAjaxCallBack(paraData,'/sale/add.do',this.callback,"");
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("设置库存成功");
                    me.ajaxRequestForcontent('/sale/list.do','','content');
                } else {
                    alert(data.reason);
                }
            }
        }

        me.editProduct={
            request: function(obj){//编辑产品
                var key =$(obj).attr('key');
                var paraData = "id="+key;
                return me.ajaxRequestForcontent("/product/toEdit.do",paraData,'content');
            }
        }

        me.copyProduct={
            request: function(obj){//复制产品
                var key =$(obj).attr('key');
                var paraData = "id="+key;
                return me.ajaxRequestForcontent("/product/toCopy.do",paraData,'content');
            }
        }
        me.priceAdd={
            request: function(){//新增价格
                var paraData = $('#priceAddForm').serialize();
                me.postAjaxCallBack(paraData,'/productPrice/add.do',this.callback,"productList");
            },
            callback:function(data){
                var status = data.status;
                if (status == true) {
                    alert("新增价格成功");
                    me.ajaxRequestForcontent('/product/list.do','','content');
                } else {
                    alert(data.reason);
                }
            }
        }

        me.query={
            request: function(obj){
                var formName = $(obj).attr("form");
                var requestUrl = $(obj).attr("requestUrl");
                var paraData = $('#'+formName).serialize();
                me.ajaxRequestForcontent(requestUrl,paraData,"content");
            }
        }

        me.productAddBtn={
            request: function(){//新增产品信息
                var paraData = $('#productAddForm').serialize();
                me.postAjaxCallBack(paraData,'/product/add.do',this.callback,"productList");
            },
            callback:function(data,domId){
                var status = data.status;
                if (status == true) {
                    alert("新增产品成功");
                    me.ajaxRequestForcontent('/product/list.do','','content');
                } else {
                    alert(data.reason);
                }
            }
        }

        me.postAjaxCallBack = function (paraData,url,fn,fnP1){
            $.ajax({
                type: "post",
                url: url,
                cache: false,
                async:true,
                data:paraData+"&_t="+new Date().getTime(),
                success: function (result) {
                    fn.call(fnP1,result,fnP1);//回调
                }
            })
        }

        me.getAjaxCallBack = function (url,fn,fnP1){
            $.ajax({
                type: "get",
                url: url+"&_t="+new Date().getTime(),
                cache: false,
                async:true,
                success: function (result) {
                    fn.call(null,result,fnP1);//回调
                }
            })
        }

        me.button = {
        }
        me.uploadProductImg=function(fileObj){//上传产品介绍图片的方法
            var hiddenObj = $(fileObj).parent().parent().find('input[type=hidden]');
            var imgObj = $(fileObj).parent().parent().find('img');
            var fileNameDisplay = $(fileObj).parent().parent().find('span[name=fileNameDisplay]');
            var para = {
                url:"/common/ajaxProductImgfileupload.do",
                fileElementId: $(fileObj).attr("id"),
                before:function(){
                    $(imgObj).show();
                    $(fileNameDisplay).text("上传中。。");
                },
                complete:function(result){
                    if(typeof(result) != 'undefined')
                    {
                        $(imgObj).hide();
                        result =  $(result).html();
                        result = $.parseJSON(result)
                        var status = result.status;
                        if (status == true) {
                            hiddenObj.val(result.reason);
                            $(fileNameDisplay).text("上传成功");
                        }else{
                            alert(result.reason);
                            $(fileNameDisplay).text("上传失败，请重试");
                        }
                    }
                },
                error:"上传产品照片失败，请联系开发人员"
            }
            me.uploadFile(para)
        }

        me.uploadPrice=function(fileObj){//上传费率价格文件的方法
            var cooperateCompanyCode = $('select[name=cooperateCompanyCode]').val();
            var priceFileType = $(':input[name=priceFileType]').val();
            if(cooperateCompanyCode==""||priceFileType==""){
                alert("下拉框的选择不能是空");
                return false;
            }
            var imgObj = $(fileObj).parent().parent().find('img');//必须放在前面，不然complete方法里是另一个对象
            var fileNameDisplay = $(fileObj).parent().parent().find('span[name=fileNameDisplay]');
            var para = {
                url:"/common/ajaxPricefileupload.do?cooperateCompanyCode="+cooperateCompanyCode+"&priceFileType="+priceFileType,
                fileElementId: $(fileObj).attr("id"),
                before:function(){
                    $(imgObj).show();
                    $(fileNameDisplay).text("上传中。。");
                },
                complete:function(result){
                    if(typeof(result) != 'undefined'){
                        $(imgObj).hide();
                        result =  $(result).html();
                        result = $.parseJSON(result)
                        var status = result.status;
                        if (status == true) {
                            $(fileNameDisplay).text("上传成功");
                            var html = result.reason;
                            $("#priceAddForm div").html(html);
                            $("#priceAddForm div").html(  $("#priceAddForm div").text());
                        }else{
                            alert(result.reason);
                            $(fileNameDisplay).text("上传失败，请重试");
                        }
                    }
                },
                error:"上传价格文件失败，请联系开发人员"
            }
            me.uploadFile(para)
        }

        me.uploadJobCode=function(fileObj){//上传职业表的方法
            var jobRiskType = $(':input[name=jobRiskType]').val();
            if(jobRiskType==""){
                alert("下拉框的选择不能是空");
                return false;
            }
            var imgObj = $(fileObj).parent().parent().find('img');//必须放在前面，不然complete方法里是另一个对象
            var fileNameDisplay = $(fileObj).parent().parent().find('span[name=fileNameDisplay]');
            var para = {
                url:"/common/ajaxJobriskfileupload.do?jobRiskType="+jobRiskType,
                fileElementId: $(fileObj).attr("id"),
                before:function(){
                    $(imgObj).show();
                    $(fileNameDisplay).text("上传中。。");
                },
                complete:function(result){
                    if(typeof(result) != 'undefined'){
                        $(imgObj).hide();
                        result =  $(result).html();
                        result = $.parseJSON(result)
                        var status = result.status;
                        if (status == true) {
                            $(fileNameDisplay).text("上传成功");
                            var html = result.reason;
                            $("#jobcodeAddForm div").html(html);
                            html =  $("#jobcodeAddForm div").text();
                            $("#jobcodeAddForm div").html(html );
                        }else{
                            alert(result.reason);
                            $(fileNameDisplay).text("上传失败，请重试");
                        }
                    }
                },
                error:"上传价格文件失败，请联系开发人员"
            }
            me.uploadFile(para)
        }

        me.uploadTraveldest=function(fileObj){//上传旅游国家表的方法

            var imgObj = $(fileObj).parent().parent().find('img');//必须放在前面，不然complete方法里是另一个对象
            var fileNameDisplay = $(fileObj).parent().parent().find('span[name=fileNameDisplay]');
            var para = {
                url:"/common/ajaxTravelDestfileupload.do",
                fileElementId: $(fileObj).attr("id"),
                before:function(){
                    $(imgObj).show();
                    $(fileNameDisplay).text("上传中。。");
                },
                complete:function(result){
                    if(typeof(result) != 'undefined'){
                        $(imgObj).hide();
                        result =  $(result).html();
                        result = $.parseJSON(result)
                        var status = result.status;
                        if (status == true) {
                            $(fileNameDisplay).text("上传成功");
                            var html = result.reason;
                            $("#traveldestAddForm div").html(html);
                            html =  $("#traveldestAddForm div").text();
                            $("#traveldestAddForm div").html(html );
                        }else{
                            alert(result.reason);
                            $(fileNameDisplay).text("上传失败，请重试");
                        }
                    }
                },
                error:"上传旅游国家文件失败，请联系开发人员"
            }
            me.uploadFile(para)
        }

        me.uploadBroadcastAlbum = function(){

        }

        me.uploadFile = function(para){//ajaxfileload
            para['before']();
            $.ajaxFileUpload({
                url:para['url'],
                secureuri:false,
                cache:false,
                fileElementId:para['fileElementId'],
                dataType:'text',//json格式，js报错
                success: function (result)
                {
                    para['complete'](result);
                },
                error: function ()
                {
                    para['error'];
                }
            })
        }
        me.loadDynamicTable = function(){
            $('#ageSelectionsTable').html('<c:out value="${ageSelectionsTable}" escapeXml="false" />');
            $('#insureAmountSelectionsTable').html('<c:out value="${insureAmountSelectionsTable}" escapeXml="false" />');
            $('#timeSelectionsTable').html('<c:out value="${timeSelectionsTable}" escapeXml="false" />');
            $('#productCategoryCodeTable').html('<c:out value="${productCategoryCodeTable}" escapeXml="false" />');
            $('#productPictureUrlTable').html('<c:out value="${productPictureUrlTable}" escapeXml="false" />');
            $('#hiddenDivTable').html('<c:out value="${hiddenDivTable}" escapeXml="false" />');

            $('#buyNoticeUrlTable').html('<c:out value="${buyNoticeUrlTable}" escapeXml="false" />');
            $('#claimTipsUrlTable').html('<c:out value="${claimTipsUrlTable}" escapeXml="false" />');
            $('#faqsUrlTable').html('<c:out value="${faqsUrlTable}" escapeXml="false" />');
            $('#productIntroductionUrlTable').html('<c:out value="${productIntroductionUrlTable}" escapeXml="false" />');
            $('#insurePrivilegeSelectionsTable').html('<c:out value="${insurePrivilegeSelectionsTable}" escapeXml="false" />');


            me.reloadIndex( "ageSelectionsTable");
            me.reloadIndex( "insureAmountSelectionsTable");
            me.reloadIndex( "timeSelectionsTable");
            me.reloadIndex( "productCategoryCodeTable");
            me.reloadIndex( "productPictureUrlTable");
            me.reloadIndex( "insurePrivilegeSelectionsTable");


            me.reloadIndex( "buyNoticeUrlTable");
            me.reloadIndex( "claimTipsUrlTable");
            me.reloadIndex( "faqsUrlTable");
            me.reloadIndex( "productIntroductionUrlTable");
        }
    }
    return {
        getInstance:function(){//子页面都是导到index.jsp，所以对象可以只初始化一次
            if(!instance){
                instance = new Erp();
            }
            return instance;
        }
    }
})( )
/*console.log(docElementArr.eq(index).is("input:checkbox"));
console.log(docElementArr.eq(index).is("input:text"));
console.log(docElementArr.eq(index).is("select"));
var docElementArr = $(obj+" :input");
var docElementCount = docElementArr.size();
if(docElementCount>0){
    for(var index= 0;index<docElementCount;index++){

    }*/


var DocumentElementMap= {
    elment:{
        'text':'input:text',
        'checkbox':'input:checkbox',
        'select':'select',
        'button':'button',
        'radio':'input:radio',
        'hidden':'input:hidden'
    },
    docElementArr:{},
    docElementCount:0,
    clear:function(contextObj){
        this.docElementArr = $("#"+contextObj+" :input");
        this.docElementCount = this.docElementArr.size();
        for(var index= 0;index<this.docElementCount;index++){
            var contextElement = this.docElementArr.eq(index);
            for(var e in this.elmentClear){
                if(contextElement.is(e)){
                    if(typeof(this.elmentClear[e])=="function"){
                        this.elmentClear[e](contextElement);
                    }
                }
            }
            }
        },
    elmentClear:{
        'input:text':function(obj){
            obj.val('');
        },
        'input:checkbox':function(obj){
            obj.removeAttr("checked");
        },
        'select':function(obj){
            obj.get(0).selectedIndex=0;
        },
        'button':function(obj){

        },
        'input:radio':function(obj){
            obj.removeAttr("checked");
        },
         'input:hidden':function(obj){

         }
    }

}


