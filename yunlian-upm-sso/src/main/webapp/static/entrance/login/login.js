/*jshint node:true*/

function init(config) {
    var image = config.image;
    var ndOs = document.getElementById('os');
    var ndScreen = document.getElementById('screen');
    var ndRecheck = document.getElementById('recheckPhone');
    var xhr;
    var reCheckUrl = '/auth/refreshCode';
    // 重发手机校验码
    if (ndRecheck) {
        if(config.verify && config.verify.type === "mobile"){
            ndRecheck.setAttribute('disabled', 'disabled');
            setTimer();
        }
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
        };
    }
    // 左侧图片
    if (image === "") {
        image = '/static/images/topic' + Math.ceil(Math.random() * 7) + '.png';
    }
    document.getElementById('banner-img').src = image;
    if (ndOs) {
        ndOs.setAttribute("value", navigator.platform);
    }
    if (ndScreen) {
        ndScreen.setAttribute("value", window.screen.width + "x" + window.screen.height);
    }
    function setTimer() {
        var count = 60;
        var timer = window.setInterval(function () {
            count--;
            if (count > 0) {
                ndRecheck.setAttribute('value', count + '秒后重新获取短信');
            } else {
                window.clearInterval(timer);
                ndRecheck.setAttribute('value', "重新获取短信");
                ndRecheck.removeAttribute('disabled');
            }
        }, 1000);

    }

    var qrcodeConfig = config.qrcode;
    if (qrcodeConfig) {
        if (typeof qrcodeConfig === 'string') {
            qrcodeConfig = { text: qrcodeConfig };
        }
        if (config.appkey) {
            qrcodeConfig.appkey = config.appkey;
        }
        if (config.service) {
            qrcodeConfig.service = config.service;
        }
        qrcodeConfig.width = 150;
        qrcodeConfig.height = 150;
        qrcodeConfig.title = '请使用新版大象app扫描登录';
        require('../../lib/qrcode')('qr-code', qrcodeConfig);
    }

    if (config.isNeedCaptcha) {
        require('../../lib/captcha').init(config.captcha || {});
    }

    if (config.login) {
        require('../../lib/login-toggle').init(config.login);
    }

    if (config.verify) {
        require('../../lib/login-verify-toggle').init(config.verify);
    }
}

module.exports = {
    init: init
};
