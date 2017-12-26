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

