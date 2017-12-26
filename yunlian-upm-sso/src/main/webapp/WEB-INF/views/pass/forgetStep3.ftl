<div class="shadow-box resetPassword">
    <h3 class="alert alert-warning">
        第三步，重置密码
    </h3>

    <div class="">
        <form class="form-horizontal" action="/pass/reset" method="post">
            <dl class="dl-horizontal">
                <dt>用户名</dt>
                <dd>${username!""}</dd>
            </dl>
            <dl class="dl-horizontal">
                <dt>手机</dt>
                <dd>${mobile!""}</dd>
            </dl>
            <div class="control-group">
                <label class="control-label"
                    for="inputPassword">新密码</label>

                <div class="controls">
                    <input type="password" id="inputPassword" name="password1" value=""/> 最少八位，必须包含一位数字、大写字母、小写字母、不可与用户名过于相似
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="inputPassword">确认新密码</label>

                <div class="controls">
                    <input type="password" id="inputPassword2" name="password2" value=""/>
                </div>
            </div>
            <input type="hidden" name="checkCode" value="${checkCode!""}">

            <div class="control-group">
                <div class="controls">
                    <input type="hidden" name="mobile" value="${mobile!""}"/>

                    <div id="tip" style="display: block;">
                        <#if errMsg??>
                        <label style="color: #ff0000;">${errMsg}</label>
                        </#if>
                    </div>
                    <input type="submit" class="btn btn-success" value="修改"/>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="/static/js/jquery.base64.min.js"></script>
<script src="/static/entrance/pass/reset-bundle.js"></script>
<script>
    require('main').init({
        username: '${username!""}',
        fields: [
            { label: '新密码', name: 'password1', validateUrl: '/pass/verifyPassFormat' },
            { label: '确认新密码', name: 'password2', validateUrl: '/pass/verifyPassFormat', needsSameAs: 'password1' }
        ]
    });
</script>
