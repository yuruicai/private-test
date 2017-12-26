<#import "../helper/json.ftl" as json>
<#assign UserStatus = statics["com.sinochem.yunlian.upm.admin.constant.UserStatus"]>
<#assign UserType = statics["com.sinochem.yunlian.upm.admin.constant.UserType"]>

<style type="text/css">
    #addUser {border:1px solid #369;width:300px;height:300px;background:#e2ecf5;z-index:1000;position:fixed;display:none;top: 50%;left: 50%;margin-left: -150px;margin-top: -150px;}
    #addUser h4 {height:20px;background:#369;color:#fff;padding:5px 0 0 5px;}
    #addUser .bd-top-box {padding: 0;}
    #userForm {margin: 0;padding: 0;}
    .button {margin-top: 20px;position: absolute;}
    #close {float: right;margin-right: 10px;cursor:pointer;}
    #addUserContent {margin-top: 30px;}
    #adduserH4 {margin: 0 auto 10px auto;}
    .cont {
        margin-left: 35px;
    }
    #addUser  input {width:120px;position:relative; margin-left:20px;}
    #addUser  select {width:120px;position:relative; margin-left:20px;}
    #addUser label {position: relative;left: 10px;width: 80px;}
    #addUser input.myinp {border:1px solid #ccc;height:16px;}
    #addUser input.sub {width:80px;margin-left:18px;}
    #queryForm input {width: 120px;height: 30px;margin-right: 20px;}
    #queryForm label {}

    #updateUser {border:1px solid #369;width:300px;height:300px;background:#e2ecf5;z-index:1000;position:fixed;display:none;top: 50%;left: 50%;margin-left: -150px;margin-top: -150px;}
    #updateUser h4 {height:20px;background:#369;color:#fff;padding:5px 0 0 5px;}
    #updateUser .bd-top-box {padding: 0;}
    #userUpdateForm {margin: 0;padding: 0;}
    #closeUpdate {float: right;margin-right: 10px;cursor:pointer;}
    #updateUserContent {margin-top: 30px;}
    #updateuserH4 {margin: 0 auto 10px auto;}
    #updateUser  input {width:120px;position:relative; margin-left:20px;}
    #updateUser  select {width:120px;position:relative; margin-left:20px;}
    #updateUser label {position: relative;left: 10px;width: 80px;}
    #updateUser input.myinp {border:1px solid #ccc;height:16px;}
    #updateUser input.sub {width:80px;margin-left:18px;}
    .bitianTip {
        color: red;
        position: relative;
        top: 5px;
    }
</style>

<div id="user-list">
    <form id="queryForm" class="form-search form-inline bd-top-box" method="get" action="/user/list">
        <label>姓名：</label>
        <input type="text" class="span2" name="name" value="${userListBean.name!""}" placeholder="模糊查询"/>
        <label>登录名：</label>
        <input type="text" class="span2"  name="loginName" value="${userListBean.loginName!""}" placeholder="模糊查询"/>
        <label>手机：</label>
        <input type="text" class="span2"  name="mobile" value="${userListBean.mobile!""}" placeholder="输入正确手机号"/>
        <input type="submit" class="btn btn-primary" style="width: 50px;" value="查找"/>
    </form>

    <table id="user-table" class="table table-striped table-hover table-condensed">
        <thead>
        <tr>
            <th data-params="{name:'checkbox'}" style="width:20px;"><input type="checkbox" id="allids"></th>
            <th data-params="{name:'name'}" style="width:80px;">姓名</th>
            <th data-params="{name:'loginName'}" style="width:80px;">登录名</th>
            <th data-params="{name:'email'}">邮箱</th>
            <th data-params="{name:'mobile', unsortable:true}">手机</th>
            <th data-params="{name:'type', unsortable:true}">用户类型</th>
            <th data-params="{name:'status', unsortable:true}" style="width:60px;">用户状态</th>
            <#--<th data-params="{name:'code'}" >员工号</th>-->
            <th data-params="{name:'createTime'}">创建时间</th>
            <th data-params="{name:'updateTime'}">更新时间</th>
        <#--<th data-params="{name:'责任人'}">责任人</th>
        <th data-params="{name:'备注'}">备注</th>-->
            <th data-params="{name:'opration', unsortable:true}">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list userList as userBean>
        <tr>
            <td class="row-trigger"><input type="checkbox" name="checkboxids" value="${userBean.id!''}"> </td>
            <td class="row-trigger"><a href="/user/view?id=${userBean.id!""}">${userBean.name!""}</a></td>
            <td>${userBean.loginName!""}</td>
            <td>${userBean.email!""}</td>
            <td>
                <a role="button" aria-label="querymobile"
                   class='queryMobile' data-id="${userBean.id!''}" href="javascript:return false;">查看手机</a>
            </td>
            <td>${userBean.typeName!''}</td>
            <td class="userstatus-describe">${userBean.statusName!''}</td>
            <#--<td>${userBean.code!""}</td>-->
            <td>
                <#if userBean.createTime??>
                ${userBean.createTime?string("yyyy-MM-dd HH:mm:ss")!""}
                </#if>
            </td>
            <td>
                <#if userBean.updateTime??>
                ${userBean.updateTime?string("yyyy-MM-dd HH:mm:ss")!""}
                </#if>
            </td>
        <#--<td>
            <#if userBean.ownerName??>
            ${userBean.ownerName!""}
            </#if>
        </td>
        <td>
            <#if userBean.comment??>
            ${userBean.comment!""}
            </#if>
        </td>-->
            <td>
                <a class="btn btn-small btn-primary userUpdateBtn" role="button" aria-label="userUpdate"
                   data-id="${userBean.id!''}" data-name="${userBean.name!''}" data-code="${userBean.code!''}" data-loginname="${userBean.loginName!''}" data-email="${userBean.email!''}" data-mobile="${userBean.decryptMobile!''}"  data-type="${userBean.type!''}" href="javascript:void(0);">修改</a>
                <#if userBean.status == UserStatus.ACTIVE.getIndex()>
                    <a class="btn btn-small btn-danger userDisableBtn" role="button" aria-label="userstatus"
                       data-id="${userBean.id!''}" href="javascript:void(0);">停用</a>
                <#elseif userBean.status == UserStatus.DELETE.getIndex() || userBean.status == UserStatus.INACTIVE.getIndex()>
                    <a class="btn btn-small btn-success userEnableBtn" role="button" aria-label="userstatus"
                       data-id="${userBean.id!''}" href="javascript:void(0)">启用</a>
                </#if>
                <#if userBean.status == UserStatus.ACTIVE.getIndex()>
                    <a class="btn btn-small btn-info restPwd" role='button' aria-label='resetpasswordbysys'
                       data-id="${userBean.id!''}" href="javascript:void(0)">重置密码</a>
                </#if>
            </td>
        </tr>
        </#list>

        <div id="addUser" style="height: 300px">
            <form id="userForm" class="form-search form-inline bd-top-box"  method="post" action="/user/addUser">
                <h4 id="adduserH4"><span>添加用户</span><span id="close">关闭</span></h4>
                <div id="addUserContent">
                    <span class="cont"><label style="display: inline; width:65px;">登录名<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" maxlength="20"  id="loginName" name="loginName" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'"  /></span><br/>
                    <span class="cont"><label style="display: inline;width:65px;">姓&nbsp;&nbsp;&nbsp;&nbsp;名<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input maxlength="10" type="text" id="name" name="name" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'" onblur="this.style.border='1px solid #ccc'" /></span><br/>
                    <span class="cont"><label style="display: inline;width:65px;">手机号<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" maxlength="30" id="mobile" name="mobile" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'" /></span><br/>
                    <span class="cont"><label style="display: inline;width:65px;">邮&nbsp;&nbsp;&nbsp;&nbsp;箱<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input maxlength="50" type="text" name="email" id="email" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'" onblur="this.style.border='1px solid #ccc'" /></span><br/>
                    <span class="cont"><label style="display: inline;width:65px;">用户类型<span class="bitianTip">*</span></label>
                        <select name="type" id="type">
                            <#list UserType.values() as t>
                            <option value="${t.getIndex()}" <#if t_index == 0>checked</#if>>${t.getName()}</option>
                            </#list>
                        </select>
                    </span><br/>
                    <#--<span class="cont"><label style="display: inline;">员工号&nbsp;</label><input type="text" maxlength="30" id="code" name="code" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'"  /></span><br/>-->
                    <span class="cont button"><input onclick="addUser()"  value="添加用户" class="btn btn-primary sub" /><input type="reset" value="重置" class="btn btn-primary sub" /><br/>
                </div>
            </form>
        </div>
        <div id="updateUser" style="height: 300px"  >
            <form id="userUpdateForm" class="form-search form-inline bd-top-box"  method="post" action="/user/updateUser">
                <h4 id="updateuserH4"><span>修改用户</span><span id="closeUpdate">关闭</span></h4>
                <div id="updateUserContent">
                    <span class="cont"><label style="display: inline;">登录名<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" maxlength="20"  id="loginNameUpdate" name="loginName" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'"/></span><br/>
                    <span class="cont"><label style="display: inline;">姓&nbsp;&nbsp;&nbsp;&nbsp;名<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input maxlength="10" type="text" id="nameUpdate" name="name" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'" onblur="this.style.border='1px solid #ccc'" /></span><br/>
                    <span class="cont"><label style="display: inline;">手机号<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" maxlength="30" id="mobileUpdate" name="mobile" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'" /></span><br/>
                    <span class="cont"><label style="display: inline;">邮&nbsp;&nbsp;&nbsp;&nbsp;箱<span class="bitianTip">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label><input maxlength="50" type="text"  id="emailUpdate" name="email" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'" onblur="this.style.border='1px solid #ccc'" /></span><br/>
                    <#--<span class="cont"><label style="display: inline;">员工号</label><input type="text" maxlength="30" id="codeUpdate" name="code" class="myinp"登录 onfoucs="this.style.border='1px solid #f60'"  /></span><br/>-->
                    <span class="cont"><label style="display: inline;">用户类型<span class="bitianTip">*</span></label>
                        <select name="type" id="typeUpdate">
                        <#list UserType.values() as t>
                            <option value="${t.getIndex()}" <#if t_index == 0>checked</#if>>${t.getName()}</option>
                        </#list>
                        </select>
                    </span><br/>
                    <span class="cont button"><input  onclick="updateUser()" value="修改用户" class="btn btn-primary sub" /><input type="reset" value="重置" class="btn btn-primary sub" /><br/>
                </div>
                <input type="hidden" id="id" name="id" ">
            </form>
        </div>
        </tbody>
    </table>
</div>
<script src="/static/datatables/jquery.dataTables.min.js"></script>
<script src="/static/datatables/jquery.dataTables.bootstrap.js"></script>
<script src="/static/layer/layer.js"></script>
<script src="/static/layer/layerUtil.js"></script>
<script src="/static/entrance/admin/user/userList.js"></script>
<script>
    function checkUNExist(formId){
        var id;
        var loginName;
        var mobile;
        var code;
        var result = true;
        if(formId == "userForm"){
            loginName = $('#loginName').val();
            mobile =$('#mobile').val();
            code = $('#code').val();
        }else{
            id  = $('#id').val();
            loginName = $('#loginNameUpdate').val();
            mobile =$('#mobileUpdate').val();
            code = $('#codeUpdate').val();
        }
        $.ajax({
            type: 'POST',
            url: '/user/addUserCheck',
            data: {
                "id": id,
                "loginName":loginName,
                "mobile": mobile,
                "code": code
            },
            async: false,
            success:function(data){
                if(data.success == false){
                    layer.msg(data.error[0].message, function(){
                    });
                    result = false;
                }
            }
        });
        return result;
    }


    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }

    /** 初始化加载结束 */

    function getUrlVars(){
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?')+1).split('&');
        for(var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
    }

    $(function() {
        var userAdd = document.getElementById("addUser");
        var mClose = document.getElementById("close");
        var userUpdate = document.getElementById("updateUser");
        var mCloseUpdate = document.getElementById("closeUpdate");

        $('.userUpdateBtn').click(function(){
            $("#loginNameUpdate").val($(this).data("loginname"));
            $("#nameUpdate").val($(this).data("name"));
            $("#mobileUpdate").val($(this).data("mobile"));
            $("#emailUpdate").val($(this).data("email"));
            $("#typeUpdate").val($(this).data("type"));
            $("#id").val($(this).data("id"));
            //$("#codeUpdate").val($(this).data("code"));
            $('#updateUser').show();

            mybgUpdate = document.createElement("div");
            mybgUpdate.setAttribute("id","mybgUpdate");
            mybgUpdate.style.background = "#000";
            mybgUpdate.style.width = "100%";
            mybgUpdate.style.height = "100%";
            mybgUpdate.style.position = "absolute";
            mybgUpdate.style.top = "0";
            mybgUpdate.style.left = "0";
            mybgUpdate.style.zIndex = "500";
            mybgUpdate.style.opacity = "0.3";
            mybgUpdate.style.filter = "Alpha(opacity=30)";
            document.body.appendChild(mybgUpdate);

            document.body.style.overflow = "hidden";
        });
        $('.userDisableBtn').click(function(){

            var id = this.getAttribute("data-id");
            layer.confirm('确定要停用该用户？', {
                        btn: ['是','否']
                    }, function(){
                        $.ajax({
                            type: 'POST',
                            url: '/user/disable',
                            data: {"id":id},
                            success:function(data){
                                if(data.status == 200){
                                    location.reload();
                                }
                            }
                        });
                    }, function(){}
            );
        });

        $('.userEnableBtn').click(function(){
            var id = this.getAttribute("data-id");
            layer.confirm('确定要启用该用户？', {
                btn: ['是','否']
            }, function(){
                $.ajax({
                    type: 'POST',
                    url: '/user/enable',
                    data: {"id":id},
                    success:function(data){
                        if(data.status == 200){
                            location.reload();
                        }
                    }
                });
            }, function(){});
        });

        $('#delUserBtn').click(function(){

            var checkedCount = $("#user-table input:checkbox:checked").size();

            if(checkedCount != 1){
                alertError('只能选中一条记录进行删除，请检查！');
                return false;
            }

            var id = $("#user-table input:checkbox:checked")[0].value;
            $.ajax({
                type: 'POST',
                url: '/user/disable',
                data: {"id":id},
                success:function(data){
                    if(data.status == 200){
                        alertSuccess("用户已停用");
                        location.reload();
                    }
                }
            });
        });

        mClose.onclick = function()
        {
            userAdd.style.display = "none";
            mybg.style.display = "none";
        }
        mCloseUpdate.onclick = function()
        {
            userUpdate.style.display = "none";
            mybgUpdate.style.display = "none";
        }
        var params = getUrlVars();
        if(params.join("").indexOf('errorInfo') > -1){
            var errorInfo = decodeURI(params['errorInfo']);
            alertError(errorInfo);
        }

        $('.table').dataTable({
            "aaSorting": [[ 10, "desc" ]]  ,
            "aLengthMenu": [[10,50, 100], [10,50,100]],
            "sPaginationType": "four_button", //分页，首页、上一页、下一页、尾页
            "bFilter": false, //过滤功能
            "iDisplayLength": 10,
            "aoColumnDefs":[
                {
                    sDefaultContent: '',
                    aTargets: ['_all']
                },
                {
                    "bSortable": false,
                    "aTargets": ['_all']
                }
            ],
            "oLanguage": {
                "sLengthMenu": "显示 _MENU_ 条记录",
                "sInfo": "显示第 _START_ - _END_ 条记录，共 _TOTAL_ 条",
                "sInfoEmpty": "没有符合条件的记录",
                "sSearch": "查找:",
                "sProcessing": "正在加载中...",
                "sZeroRecords": "没有符合条件的记录",
                "oPaginate" : {
                    "sFirst" : "首页",
                    "sPrevious" : " 上页 ",
                    "sNext" : " 下页 ",
                    "sLast" : " 尾页 "
                }
            },
        });
        $('#user-table_length').after('<input type="button" style="margin-top: 12px" class="btn btn-primary demo0" id="addUserBtn" value="添加用户"/>');

        $('#addUserBtn').click(function(){
            $('#addUser').show();

            mybg = document.createElement("div");
            mybg.setAttribute("id","mybg");
            mybg.style.background = "#000";
            mybg.style.width = "100%";
            mybg.style.height = "100%";
            mybg.style.position = "absolute";
            mybg.style.top = "0";
            mybg.style.left = "0";
            mybg.style.zIndex = "500";
            mybg.style.opacity = "0.3";
            mybg.style.filter = "Alpha(opacity=30)";
            document.body.appendChild(mybg);

            document.body.style.overflow = "hidden";
        });

    });
    function addUser() {
        if($('#loginName').val().trim() == ''){
            alertError("登录名不能为空");
            return false;
        }
        if($('#loginName').val().length>20){
            alertError("登录名长度不能大于20");
            return false;
        }

        if($('#name').val().trim()  == ''){
            alertError("姓名不能为空");
            return false;
        }
        if($('#name').val().length>10){
            alertError("姓名长度不能大于10");
            return false;
        }

        if($('#mobile').val().trim()  == ''){
            alertError("手机号不能为空");
            return false;
        }

        if($('#email').val().trim()  == ''){
            alertError("邮箱不能为空");
            return false;
        }

        if($('#email').val().length>50){
            alertError("邮箱长度不能大于50");
            return false;
        }

        var telReg = !!$('#mobile').val().match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/);
        if(telReg == false){
            alertError("手机号输入不合规范，请检查");
            return false;
        }

        if ((!!$('#email').val().match(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/)) == false) {
            alertError("邮箱格式不正确");
            return false;
        }

        if(checkUNExist("userForm") == false){
            return false;
        }

        var url = '/user/addUser';
        var data = {'loginName':$('#loginName').val(),'name': $('#name').val(),'mobile':$('#mobile').val(),'email':$('#email').val(),'type':$("#type").val()};
        $.ajax({
            type: 'post',
            traditional: true,
            url: url,
            data: data,
            success: function (data) {
                if(eval(data).status==200){
                    var userAdd = document.getElementById("addUser");
                    userAdd.style.display = "none";
                    alertSuccess(eval(data).result.msg,reload);
                }else{
                    alertError(eval(data).result.msg);
                }
            },
            error: function (data) {
                alertError("系统繁忙，请稍后重试");
            }
        });
    }
    function updateUser() {
        if($('#loginNameUpdate').val().trim()  == ''){
            alertError("登录名不能为空");
            return false;
        }
        if($('#loginNameUpdate').val().length>20){
            alertError("登录名长度不能大于20");
            return false;
        }

        if($('#nameUpdate').val().trim()  == ''){
            alertError("姓名不能为空");
            return false;
        }
        if($('#nameUpdate').val().length>10){
            alertError("姓名长度不能大于10");
            return false;
        }

        if($('#mobileUpdate').val().trim()  == ''){
            alertError("手机号不能为空");
            return false;
        }

        if($('#emailUpdate').val().trim()  == ''){
            alertError("邮箱不能为空");
            return false;
        }
        if($('#emailUpdate').val().length>50){
            alertError("邮箱长度不能大于50");
            return false;
        }

        var telReg = !!$('#mobileUpdate').val().match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/);
        if(telReg == false){
            alertError("手机号输入不合规范，请检查");
            return false;
        }

        if(checkUNExist("userUpdateForm") == false){
            return false;
        }

        if ((!!$('#emailUpdate').val().match(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/)) == false) {
            alertError("邮箱格式不正确");
            return false;
        }

        var url = '/user/updateUser';
        var data = {'id': $('#id').val(),'loginName':$('#loginNameUpdate').val(),'name': $('#nameUpdate').val(),'mobile':$('#mobileUpdate').val(),'email':$('#emailUpdate').val(),'type':$("#typeUpdate").val()};
        $.ajax({
            type: 'post',
            traditional: true,
            url: url,
            data: data,
            success: function (data) {
                if(eval(data).status==200){
                    var userUpdate = document.getElementById("updateUser");
                    userUpdate.style.display = "none";
                    alertSuccess(eval(data).result.msg,reload);
                }else{
                    alertError(eval(data).result.msg);
                }
            },
            error: function (data) {
                alertError("系统繁忙，请稍后重试");
            }
        });
    }
    var reload =function(index){
        location.reload();
    }
</script>