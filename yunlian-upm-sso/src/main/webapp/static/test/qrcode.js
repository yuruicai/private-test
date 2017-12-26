/*jshint node:true*/
/*global describe, it, before, after, jQuery, QRCode, sinon*/
var qrcode = require('../lib/qrcode');
var assert = require('assert');

global.sinon = require('sinon');
require('sinon/lib/sinon/util/event.js');
require('sinon/lib/sinon/util/fake_server.js');
require('sinon/lib/sinon/util/fake_xml_http_request.js');

describe('qrcode is ok', function() {

    it('it works', function() {
        assert.equal(typeof qrcode, 'function');
    });

    before(function() {
        jQuery('<div id="qr-code"></div>').appendTo('body');
    });
    after(function() {
        jQuery('#qr-code').remove();
    });

    it('.qrcode 为 new QRCode', function() {
        var app = qrcode('qr-code', 'test');
        assert.ok(app.qrcode instanceof QRCode);
    });

    it('login() 初始调用', function() {
        var clock = sinon.useFakeTimers();
        var app = qrcode('qr-code', 'test');
        sinon.spy(app, 'login');

        clock.tick(app.defaultLoginDelay);
        assert(app.login.called);

        app.login.restore();
        clock.restore();
    });

    it('login() 初始调用时间 opts.loginDelay', function() {
        var clock = sinon.useFakeTimers();
        var app = qrcode('qr-code', {
            text: 'test',
            loginDelay: 2000
        });
        sinon.spy(app, 'login');

        clock.tick(1000);
        assert(app.login.notCalled);

        clock.tick(2000);
        assert(app.login.called);

        app.login.restore();
        clock.restore();
    });

});

describe('login()', function() {

    before(function() {
        jQuery('<div id="qr-code"></div>').appendTo('body');
    });
    after(function() {
        jQuery('#qr-code').remove();
    });

    var xhr;
    var requests;
    before(function() {
        xhr = sinon.useFakeXMLHttpRequest();
        requests = [];
        xhr.onCreate = function(req) {
            requests.push(req);
        };
    });
    after(function() {
        xhr.restore();
    });

    function respond(idx, code) {
        if (arguments.length === 1) {
            code = idx;
            idx = requests.length - 1;
        }
        requests[idx].respond(200, { 'Content-Type': 'application/json' }, '{"code":' + code + '}');
    }

    var app;
    before(function() {
        app = qrcode('qr-code', 'test');
    });

    it('会请求 /qrcode/login', function() {
        app.login();
        assert.equal(requests.length, 1);
    });

    it('遇到 200 ，继续轮询', function() {
        var clock = sinon.useFakeTimers();
        app.login();
        respond(1, 200);

        sinon.spy(app, 'login');

        clock.tick(app.defaultLoginDelay);
        assert(app.login.called);
        app.login.restore();
        clock.restore();
    });

    it('遇到 201, 跳转到首页', function() {
        app.login();
        var stub = sinon.stub(app, 'redirect', function() {});
        requests[requests.length - 1].respond(200, { 'Content-Type': 'application/json' }, '{"code":201}');

        assert(stub.called);
        assert(stub.calledWith('/'));

        stub.restore();
    });

    it('遇到 404, 请求新的 uuid', function() {
        var stub = sinon.stub(app, 'getuuid', function() {});
        app.login();
        respond(4, 404);

        assert(stub.called);

        stub.restore();
    });

    it('遇到 408 ，继续轮询', function() {
        var clock = sinon.useFakeTimers();
        app.login();
        respond(5, 408);

        sinon.spy(app, 'login');

        clock.tick(app.defaultLoginDelay);
        assert(app.login.called);
        app.login.restore();
        clock.restore();
    });

    it('遇到 500 , 重试轮询', function() {
        var clock = sinon.useFakeTimers();
        app.login();
        respond(7, 500);

        sinon.stub(app, 'login', function() {}).withArgs({ retry: 5 });
        clock.tick(app.defaultLoginDelay);
        app.login.restore();
        clock.restore();
    });

    it('遇到 500 , 重试轮询 5 次后延迟', function() {
        var clock = sinon.useFakeTimers();
        var app1 = qrcode('qr-code', 'test');
        app1.login();
        respond(500);
        clock.tick(app1.defaultLoginDelay);
        respond(500);
        clock.tick(app1.defaultLoginDelay);
        respond(500);
        clock.tick(app1.defaultLoginDelay);
        respond(500);
        clock.tick(app1.defaultLoginDelay);
        respond(500);
        clock.tick(app1.defaultLoginDelay);
        respond(500);
        sinon.spy(app1, 'login');
        clock.tick(app1.defaultLoginDelay);
        assert(app1.login.notCalled);
        clock.tick(app1.defaultErrorDelay - app1.defaultLoginDelay);
        assert(app1.login.called);
        app1.login.restore();
        clock.restore();
    });

    it('遇到 500, 再遇到 200, 延迟重试取消', function() {
        var clock = sinon.useFakeTimers();
        var app2 = qrcode('qr-code', 'test');
        app2.login();
        respond(500);
        clock.tick(app2.defaultLoginDelay);
        respond(200);
        var stub = sinon.stub(app2, 'login', function() {});
        clock.tick(app2.defaultLoginDelay);
        assert(stub.calledWith(undefined));
        stub.restore();
        clock.restore();
    });

});




