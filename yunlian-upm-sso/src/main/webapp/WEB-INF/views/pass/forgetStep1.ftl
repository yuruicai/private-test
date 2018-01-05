<div id="setPassword">
    <h3 class="alert alert-warning">
        第一步，确认账号
    </h3>

    <div class="shadow-box password-box">
        <form method="post" class="form-horizontal" action="/pass/forget">
            <div class="control-group">
                <label class="control-label">手机号</label>

                <div class="controls">
                    <input type="text" size="20" name="mobile" value="${mobile!""}">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">验证码</label>

                <div class="controls">
                    <input type="text" size="20" name="captcha" value=""/>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <img id="imageCapto" alt="验证码" src="/generatImage"
                    onclick="(function(img) {var src = img.src.split('?')[0];img.src = src+'?captchaId='+new Date().getTime();})(this);">
                    <a href="javascript:(function(img) {var src = img.src.split('?')[0];img.src = src+'?captchaId='+new Date().getTime();})(document.getElementById('imageCapto'));">看不清楚？点击换一张</a>
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
