<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<link href="/static/css/user.css" rel="stylesheet">

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
    <#if user??>
    <form class="form-horizontal" action="/admin/user/editVirtualUser" method="post">
        <input type="hidden" id="id" name="id" value="${user.id!''}"/>
        <div class="control-group">
            <label class="control-label" for="loginName">登录名</label>

            <div class="controls">
                <input type="text" id="loginName" name="loginName" value="${user.loginName!''}" readonly data-params="required"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="name">显示名</label>
            <div class="controls">
                <input type="text" id="name" name="name" value="${user.name!''}" data-params="required"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" >责任人</label>
            <div id="ownerIdComponent" class="controls">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="comment">备注</label>
            <div class="controls">
                <textarea id="comment"  name="comment" data-params="{maxlength:32}" placeholder="最多输入32个字符">${user.comment!''}</textarea>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <input type="submit" class="btn btn-primary" value="保存"/>
            </div>
        </div>
    </form>
    </#if>
</div>