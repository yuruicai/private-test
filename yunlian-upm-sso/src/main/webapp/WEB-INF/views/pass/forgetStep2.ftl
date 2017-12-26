<div id="setPassword">
    <h3 class="alert alert-warning">
        第二步，安全验证
    </h3>

    <div class="shadow-box password-box">
        <form class="form-horizontal" method="get" action="/pass/reset">
            <div class="control-group">
                <label class="control-label">短信验证码</label>

                <div class="controls">
                    <input type="hidden" name="mobile" value="${mobile!""}">
                    <input type="text" size="20" name="smsCode" value="">
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <div id="tip" style="display: block;">
                    <#if errMsg??>
                        <label style="color: #ff0000;">${errMsg}</label>
                    </#if>
                    </div>
                    <input type="submit" class="btn btn-primary" value="下一步">
                </div>
            </div>
        </form>
    </div>
</div>
