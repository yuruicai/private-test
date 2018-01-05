require=(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
/*jshint node:true */
/*global $*/
exports.init = function(config) {
    var img = document.getElementById(config.imgId);
    function renderImg() {
        if(img){
            var src = img.src.split('?')[0];
            img.src = src+'?captchaId=' + new Date().getTime();
        }
    }
    renderImg();
    $(img).click(renderImg);
};

},{}],2:[function(require,module,exports){
exports.init = function init(config) {
    this.config = config;
    this.type = this.config.type;
    this.$toggle = $(config.toggle);
    this.bindUI();
};

exports.bindUI = bindUI;
exports.onToggle = onToggle;

function bindUI() {
    var self = this;
    this.$toggle.click(function(e) {
        self.onToggle(e);
    });
}

function onToggle(e) {
    var $el = $(e.currentTarget);
    var type = $el.data('type');

    var othertypes = [];
    $.each(this.config.types, function(i, item) {
        if (item !== type) {
            othertypes.push(item);
        }
    });

    var $section = $el.parents('.login-section');
    var $tabs = $section.find('.login-tabs');

    $tabs.find('.login-toggle').removeClass('is-active');
    $tabs.find('.login-toggle-' + type).addClass('is-active');

    $tabs.removeClass('login-type-' + othertypes.join('login-type-'));
    $tabs.addClass('login-type-' + type);

    $section.find('.login-container').hide();
    $section.find('.login-' + type + '-container').show();
    if (type === 'form') {
        $section.find('input[type="text"]').focus();
    }
}


},{}],3:[function(require,module,exports){
exports.init = function init(config) {
    this.config = config;
    this.type = this.config.type;
    this.$toggle = $(config.toggle);
    this.bindUI();
};

exports.bindUI = bindUI;
exports.onToggle = onToggle;

function bindUI() {
    var self = this;
    this.$toggle.click(function(e) {
        self.onToggle(e);
    });
}

function onToggle(e) {
    var $el = $(e.currentTarget);
    var type = $el.data('type');

    var othertypes = [];
    $.each(this.config.types, function(i, item) {
        if (item !== type) {
            othertypes.push(item);
        }
    });

    var $section = $el.parents('.login-section');
    var $tabs = $section.find('.login-tabs');

    $tabs.find('.login-toggle').removeClass('is-active');
    $tabs.find('.login-toggle-' + type).addClass('is-active');

    $tabs.removeClass('login-type-' + othertypes.join('login-type-'));
    $tabs.addClass('login-type-' + type);

    $section.find('.login-container').hide();

    $section.find('.login-' + type + '-container').show();
    $section.find('input[type="text"]').focus();

}


},{}],4:[function(require,module,exports){
/*jshint node:true, expr:true, camelcase:false*/
/*global $*/
/**
 * info, warn, error
 */
module.exports = message;

function message(opts) {
    return new Message(opts);
}

function Message(opts) {
    opts || (opts = {});
    this.css_prefix = opts.css_prefix;
    this.parentEl = opts.parentEl;
    if (typeof this.parentEl === 'string') {
        this.parentEl = $(this.parentEl);
    }
    this.container = null;
}

Message.prototype.css_prefix = 'message';

Message.prototype.show = function(msg, type) {
    if (!this.container) {
        this.container = $('<div class="' + this.css_prefix + '"></div>');
        this.container.appendTo(this.parentEl);
    }
    this.container.removeClass();
    this.container.addClass(this.css_prefix + ' ' + this.css_prefix + '-' + type);
    this.container.html(msg);
    return this;
};

Message.prototype.warn = function(msg) {
    return this.show(msg, 'warn');
};

Message.prototype.error = function(msg) {
    return this.show(msg, 'error');
};

Message.prototype.info = function(msg) {
    return this.show(msg, 'info');
};


},{}],5:[function(require,module,exports){
/*jshint node:true, expr:true,unused:false,camelcase:false*/
/*global QRCode,jQuery,$*/

var host = window.location.protocol + '//' + window.location.host;

function qrcode(domId, opts) {
    if (typeof opts === 'string') {
        opts = { text: opts };
    }
    opts || (opts = {});

    var ctx = {};
    ctx.appkey = opts.appkey || '';
    ctx.service = opts.service || '';

    var uuid = opts.text;
    ctx.uuid = uuid;
    ctx.text = host + '/qrcode/' + uuid;
    ctx.title = opts.title;

    opts.text= ctx.text;
    opts.correctLevel = QRCode.CorrectLevel.L;
    var qr =  new QRCode(domId, opts);
    ctx.qrcode = qr;

    ctx.login = login;
    ctx.remake = remake;
    ctx.getuuid = getuuid;
    ctx.redirect = redirect;
    ctx.invalid = invalid;
    ctx.uiSetTitle = uiSetTitle;
    ctx.loginSuccessTip = loginSuccessTip;

    ctx.message = require('./message')({
        parentEl: $('#' + domId).parent(),
        css_prefix: 'qrcode-message'
    });

    ctx.defaultLoginDelay = 3000;
    ctx.defaultErrorDelay = 5000;
    ctx.defaultErrorRetry = 5;
    ctx.defaultMaxGetuuid = 10;

    ctx.loginDelay = opts.loginDelay || ctx.defaultLoginDelay;

    ctx.loginTimer = window.setTimeout(function() {
        ctx.login();
    }, ctx.loginDelay);

    ctx.uiSetTitle();

    return ctx;
}

var loginUrl = '/qrcode/login';
var encode = window.encodeURIComponent;

function login(opts) {
    opts || (opts = {});
    var self = this;
    opts.retry || (opts.retry = self.defaultErrorRetry);
    function _login(_opts) {
        self.loginTimer = setTimeout(function() {
            self.login(_opts);
        }, opts.delay ? opts.delay : self.loginDelay);
    }
    return jQuery.post(host + loginUrl, { uuid: this.uuid }, function(data) {
        if (data.code !== 500) {
            // reset
            opts.retry = self.defaultErrorRetry;
        }
        var loginConfig;
        switch (data.code) {
            case 200:
                //after login successfully, show a tip
                self.loginSuccessTip();
                _login();
                break;
            case 201:
                self.redirect('/auth?' +
                   'appkey=' + encode(self.appkey) + '&service=' + self.service);
                break;
            case 404:
                if (!self.hasOwnProperty('getuuidCount')) {
                    self.getuuidCount = 0;
                }
                if (self.getuuidCount > self.defaultMaxGetuuid) {
                    self.invalid();
                    return;
                }

                self.getuuidCount++;
                self.getuuid(function() {
                    var text;
                    if(arguments.length > 0) {
                        text = host + '/qrcode/' + arguments[0];
                    }
                    self.remake(text);
                    self.login();
                });
                break;
            case 408:
                _login();
                break;
            case 500:
                opts.retry--;
                loginConfig = {
                    retry: opts.retry
                };
                if (opts.retry === 0) {
                    loginConfig.delay = self.defaultErrorDelay;
                }
                _login(loginConfig);
                break;
            default:
                _login();
                break;
        }
    });
}

function remake(text) {
    if (arguments.length === 0) {
        text = this.text;
    }
    this.qrcode.makeCode(text);
}

function uiSetTitle() {
    this.qrcode._el.title = this.title ? this.title : '';
}

var uuidUrl = '/qrcode/getuuid';

function getuuid(cb) {
    var self = this;
    return jQuery.get(host + uuidUrl, function(data) {
        self.uuid = data.uuid;
        cb(self.uuid);
    });
}

function redirect(url) {
    window.location.href = host + url;
}

function invalid() {
    var self = this;
    // self.qrcode.clear();
    this.message.warn('<span class="qrcode-invalid">失效了</span><br><button class="btn J-qrcode-reload">刷新</button>');
    this.message.container.find('.J-qrcode-reload').on('click', function() {
        self.redirect('/login');
    });
}

function loginSuccessTip() {
    var self = this,
        txt = '<div class="success-tip"><div class="success-flag"></div></div>' +
            '<div class="success-txt"><div class="success-txt-top">扫码成功</div><div class="success-txt-bottom">请在手机点击确认!</div></div>' +
            '<div style="clear: both;"></div>';
    // self.qrcode.clear();
    this.message.warn(txt);
}

module.exports = qrcode;


},{"./message":4}],"main":[function(require,module,exports){
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

},{"../../lib/captcha":1,"../../lib/login-toggle":2,"../../lib/login-verify-toggle":3,"../../lib/qrcode":5}]},{},[]);
