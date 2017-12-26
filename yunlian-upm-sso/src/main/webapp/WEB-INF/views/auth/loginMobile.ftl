<!doctype html>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <link href="/static/css/login-mobile.css" rel="stylesheet">
    <title>壹化云链统一登录中心</title>
</head>
<body id="loginMobile">
<div id="doc">
    <div id="l-hd">
        <a class="logo" href="javascript:void(0);" title="壹化云链统一登录中心"><img src="/static/images/logo-200-130-2.png" width="100px" height="65px"></a>
    </div>
    <div id="l-bd" class="cf">
        <div id="mobile-tip" class="tip-error">
            <#if error??>
                ${error}
            <#elseif needMobile??>
                为加强账户安全，公司现随机试点推行短信验证，请输入收到的校验码
            </#if>
        </div>
        <#if needMobile??>
            <form class="login-form login-form-mobile-check" action="/auth/mobileCheck" method="post">
                <div class="form-field">
                    <label class="form-field-label">手机号码：</label>

                    <div class="form-field-controls">
                        <span class="phone-number">${mobile!''}</span>
                    </div>
                </div>
                <div class="form-field">
                    <label class="form-field-label">校验码：</label>

                    <div class="form-field-controls">
                        <input type="text" name="code" class="phone-check" placeholder="短信验证码"
                               value=""/>
                        <input id="recheckPhone" type="button" class="btn-recheck"
                               disabled="disabled" value="60秒后重新获取短信"/>
                    </div>
                </div>
                <div class="form-field form-field-submit">
                    <input type="hidden" name="appkey" value="${appkey!''}"/>
                    <input type="hidden" name="key" value="${key!''}"/>
                    <input type="hidden" name="service" value="${service!''}"/>
                    <input type="hidden" name="mobile" value="${mobile!''}"/>
                    <input type="submit" class="btn" name="commit" value="确定">
                </div>
            </form>
        <#else>
        <div id="login-form">
            <form method="POST" action="/login">
                <div class="form-field form-field-input">
                    <input id="username" type="text" class="login-eng" name="username" placeholder="MIS账号" value=""/>
                    <i class="icon icon-user"></i>
                </div>
                <div class="form-field form-field-input">
                    <input id="password" type="password" class="login-eng" name="password" placeholder="密码" value=""/>
                    <i class="icon icon-password"></i>
                </div>
                <#if needCaptcha?? && needCaptcha == "true">
                    <div class="form-field">
                        <input type="text" class="input-captcha" name="captcha" value="" autocomplete="off" placeholder="验证码"/>
                        <img id="imageCapto" class="img-captcha" alt="验证码" src="/generatImage"
                             onclick="(function(img) {var src = img.src.split('?')[0];img.src = src+'?p='+new Date().getTime();})(this);">
                    </div>
                </#if>
                <div>
                    <input type="hidden" name="service" value="${service!''}"/>
                    <input type="hidden" name="key" value="${key!''}"/>
                    <input type="hidden" name="appkey" value="${appkey!''}"/>
                    <input id="os" type="hidden" name="os" value=""/>
                    <input id="screen" type="hidden" name="screen" value=""/>
                    <input type="hidden" name="isRemembered" value="false"/>
                    <input type="submit" class="btn" name="submit" value="登 录" tabindex="4" />
                </div>
            </form>
            <div class="forget-password">
                <a href="/pass/forget">忘记密码？</a>
            </div>
        </#if>
        </div>
        </div>
    </div>
</div>
</body>
<script>
    var username = document.getElementById('username');
    //模拟click事件
    function simulateClick() {
        if (!document.createEvent) {
            return;
        }
        var evt = document.createEvent("MouseEvents");
        evt.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
        if (username) {
            username.dispatchEvent(evt);
        }
    }
    try {
        simulateClick();
        document.getElementById('os').setAttribute("value", navigator.platform)
        document.getElementById('screen').setAttribute("value", window.screen.width + "x" + window.screen.height)
    } catch (e) {
    }
    if (username) {
        username.focus();
    }

    var ndRecheck = document.getElementById('recheckPhone');
    var xhr;
    var reCheckUrl = '/auth/refreshCode';
    // 重发手机校验码
    if (ndRecheck) {
        setTimer();
        ndRecheck.onclick = function () {
            ndRecheck.setAttribute('disabled', 'disabled');
            setTimer();
            if (window.XMLHttpRequest) {
                xhr = new window.XMLHttpRequest();
            } else if (window.ActiveXObject) {
                xhr = new window.ActiveXObject("Microsoft.XMLHTTP");
            }
            if (xhr) {
                xhr.open('get', reCheckUrl, false);
                xhr.send(null);
            }

        }
    }
    function setTimer() {
        var count = 60;
        var timer = window.setInterval(function () {
            count--;
            if (count > 0) {
                ndRecheck.setAttribute('value', count + '秒后重新获取短信');
            } else {
                window.clearInterval(timer);
                ndRecheck.setAttribute('value', "重新获取短信")
                ndRecheck.removeAttribute('disabled');
            }
        }, 1000);
    }
</script>
</html>
