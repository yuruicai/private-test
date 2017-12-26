<#compress><!DOCTYPE html>
<html>
    <head>
        <title>壹化云链统一登录中心</title>
        <meta http-equiv=content-type content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <link href="/static/css/login.css" rel="stylesheet">
        <script src="/static/jquery-2.0.3.min.js" type="text/javascript"></script>
    </head>
    <style>

    </style>
    <#import "../helper/json.ftl" as json />



    <body id="login">
        <div id="doc">
            <div id="l-hd" >
                <a class="" href="/"><h1 style="position: relative;top: 14px;"></h1></a>
            </div>
            <div id="l-bd" class="cf">
                <div class="promotion-banner">
                    <img id="banner-img" alt="壹化云链价值观" src=""/>
                </div>
                <div class="login-section-bg">
                    <div class="login-section">
                        <div class="login-container login-form-container" >
                            <#assign
                            showErrorTip = error?? || freezeLimit??
                            isNeedCaptcha = needCaptcha?? && needCaptcha == "true"
                            />
                            <#if showErrorTip>
                            <div id="form-tip" class="tip-error">
                                <#if error??>
                                ${error}
                                <#elseif freezeLimit?? && freezeLimit >=0>
                                多次重试将临时冻结账号，剩余重试次数${freezeLimit}，<a tabindex="-1" class="forget-password"
                                                                   href="/pass/forget">忘记密码？</a>
                            </#if>
                        </div>
                    </#if>

                    <form id="loginForm" class="login-form" action="/login" method="post">
                        <div class="form-field">
                            <i class="icon icon-user"></i>
                            <input type="text" id="login-username"
                                   name="username" placeholder="账号" value="">
                        </div>
                        <div class="form-field">
                            <i class="icon icon-password"></i>
                            <input type="password" id="login-password"
                                   name="password" placeholder="密码" value="">
                        </div>

                        <#if isNeedCaptcha>
                            <div class="form-field cf">
                                <input class="ipt-captcha" type="text"
                                       name="captcha" placeholder="验证码" value="">

                                <div class="captcha-field">
                                    <img id="imageCaptcha" alt="点击更新验证码"
                                         title="点击更新验证码" src="/api/generatImage" onclick="updImg();">
                                </div>
                            </div>
                        </#if>

                        <div class="form-field form-field-submit">
                            <input type="submit" class="btn" name="commit" value="登录">
                        </div>
                        <div class="form-field form-field-other cf">
                            <a tabindex="-1" class="forget-password" href="/pass/forget">忘记密码？</a>
                            <input type="hidden" name="service" value="${service!''}"/>
                            <input type="hidden" name="key" value="${key!''}"/>
                            <input type="hidden" name="appkey" value="${appkey!''}"/>
                            <input id="os" type="hidden" name="os" value=""/>
                            <input id="screen" type="hidden" name="screen" value=""/>
                            <input type="hidden" name="isRemembered" value="false"/>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>
    <div id="l-ft">
        <p>
            ©${.now?string.yyyy} fengjr.com
            <a class="feedback" href="mailto:upm@fengjr.com">问题反馈</a>
        </p>
    </div>
</div>
<#--<script src="/static/node_modules/qrcodejs/qrcode.js"></script>-->
<script src="/static/js/jquery.base64.min.js"></script>
<script src="/static/entrance/login/login-bundle.js"></script>
<script>
    require('main').init({
        appkey: '${(appkey!'')?xhtml}'
        , service: '${(service!'')?url?xhtml}'
        , image: '${image!''}'
        , isNeedCaptcha: ${isNeedCaptcha?string('true', 'false')}
        <#if isNeedCaptcha>
            , captcha: { imgId: 'imageCaptcha' }
        </#if>
    });
    function updImg(){
//        var img = $("#banner-img").src;
//        alert(img);
        require('main').init({
            image: '${image!''}',
            isNeedCaptcha: true,
            captcha: { imgId: 'imageCaptcha' }
        });

    }

    $('#loginForm').on('submit', function(e){
        e.preventDefault();
        var password = $('#login-password').val();
        $("#login-password").val($.base64.encode(password));
        this.submit();
    });
</script>
</body>
</html></#compress>
