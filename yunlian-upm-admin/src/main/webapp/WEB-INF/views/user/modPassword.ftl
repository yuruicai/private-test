<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<!--<link href="/static/css/user.css" rel="stylesheet">-->

<#assign needsSendSms = mobile?? && (mobile?length gt 0) && (mobile?lower_case != "null") />

<div class="password-mod shadow-box">
    <div class="control-group">
        <div class="controls">
            <div id="tips" style="display: block;">
            <#if tips??>
                <div style="color: #ff0000;">${tips}</div>
            </#if>
            </div>
        </div>
    </div>
    <form class="form-horizontal" action="/user/modPassword" method="post" id="updatePassForm">
        <dl class="dl-horizontal">
            <dt>用户名</dt>
            <dd>${username!""}</dd>
        </dl>

        <div class="control-group">
            <label class="control-label" for="inputPassword">旧密码</label>

            <div class="controls">
                <input type="password" id="inputPassword" name="oldPassword" value=""/>
            </div>
        </div>
        <div class="control-group">
            <!-- 15位以上RTX登录不了 -->
            <label for="fieldNewPassword1" class="control-label">新密码</label>
            <div class="controls">
                <input id="fieldNewPassword1" type="password" name="newPassword1" value="" onBlur="checkPwd(this);" />
                <p class="help-block text-muted">
                    8~15位，必须包含一位数字、大写字母、小写字母、不可与用户名过于相似</br>
                </p>
            </div>
        </div>
        <div class="control-group">
            <label for="fieldNewPassword2" class="control-label">确认新密码</label>
            <div class="controls">
                <input id="fieldNewPassword2" type="password" name="newPassword2" value="" onBlur="checkPwdDiff();"/>
            </div>
        </div>
        <div id="tipDiv" style="display: none;color: #005599" class="control-group">
        </div>
        <div class="control-group">
            <div class="controls">
                <div id="tip" style="display: block;">
                    <#if errMsg??>
                    <div style="color: #ff0000;">${errMsg}</div>
                    </#if>
                </div>
                <span style="padding-left: 40px;">
                    <input type="submit" class="btn btn-warning" value="修改"/>
                    <a href="javascript:history.back()" class="btn btn-success" style="margin-left: 20px;">返回</a>
                </span>
            </div>
        </div>

    </form>
</div>
<script src="/static/layer/layer.js"></script>
<script src="/static/jquery.base64.min.js"></script>
<script>


    function checkPwd(user){
        var uname=user.value;
        var tip = "";
        if(!uname){
            layer.msg('新密码不能为空!', function(){
                user.focus();
                return false;
            });
        }
        if(uname.length<8){

            layer.msg('密码长度不能小于8位!', function(){
                user.focus();
                return false;
            });
        }else if(uname.length>15){
            layer.msg('密码长度不能大于15位!', function(){
                user.focus();
                return false;
            });
        }
        var re =new RegExp("[A-Z]");
        if(re.test(uname)){
            re = new RegExp("[a-z]");
            if(re.test(uname)){
                re = new RegExp("[0-9]");
                if(!re.test(uname)){
                    layer.msg('新密码必须包含数字，请检查确认!', function(){
                        user.focus();
                        return false;
                    });
                }
            }else{
                layer.msg('新密码必须包含一个小写字母，请检查确认!', function(){
                    user.focus();
                    return false;
                });
            }

        }else{
            layer.msg('新密码必须包含一个大写字母，请检查确认!', function(){
                user.focus();
                return false;
            });
        }


    }
    function checkPwdDiff(){
        var fieldNewPassword1 = $('#fieldNewPassword1').val();
        var fieldNewPassword2 = $('#fieldNewPassword2').val();
        if(fieldNewPassword1!=fieldNewPassword2){
            layer.alert("两次输入的密码不一致");

        }
    }

    $('#updatePassForm').on('submit', function(e){
        e.preventDefault();
        var inputPassword = $('#inputPassword').val();
        var fieldNewPassword1 = $('#fieldNewPassword1').val();
        var fieldNewPassword2 = $('#fieldNewPassword2').val();
        $("#inputPassword").val($.base64.encode(inputPassword));
        $("#fieldNewPassword1").val($.base64.encode(fieldNewPassword1));
        $("#fieldNewPassword2").val($.base64.encode(fieldNewPassword2));
        this.submit();
    });
</script>

