/**
 * @fileoverview 通用js
 */
M.add('upm-core', function (Y) {
    Y.namespace('mt.upm');
    var win = Y.config.win;

    Y.namespace("mt.upm").Core = {
        init: function (userId) {
            this.userId = userId;
            this.buildSelect();
            this.onSelect();
        },
        onSelect: function () {
            var _this = this;
            var forms = Y.all(".autoSelectApp");
            forms.each(function (selectForm) {
                selectForm.on('change', function () {
                    var appId = this.get("value");
                    var appName = this.get("selectedOptions").get(0)[0]._node.text;
                    _this.appChange(appId, appName);
                    var query = win.location.search;
                    var host = win.location.host;
                    var uri = win.location.pathname;
                    var href = '';
                    if (query.indexOf("applicationId") !== -1) {
                        var newQuery = query.replace(/applicationId=\d*/, "applicationId=" + appId);
                        href = "http://" + host + uri + newQuery;
                    } else {
                        href = "http://" + host + uri + query;
                    }
                    win.location.href = href;
                });
            });
        },
        buildSelect: function () {
            var _this = this;
            Y.mt.io.get('/apps.ajax', {userId: this.userId}, function (response) {
                var forms = Y.all(".autoSelectApp");
                var appId = Y.mt.cookie.get("appId");
                var appName = Y.mt.cookie.get("appName");
                Y.NodeList.each(forms, function (selectForm) {
                        if (!selectForm) return;
                        var options = Y.Node.getDOMNode(selectForm).options;
                        Y.Array.each(response.data, function (item) {
                            var option = new Option(item.name, item.id);
                            if (appId === '' || appId === null || appId === 0) {
                                appId = item.id;
                                appName = item.name;
                            }
                            if (option.value === appId) {
                                option.selected = true;
                            }
                            options.add(option);
                        });
                    },
                    function () {
                        new Y.mt.widget.HeaderTip("error", "操作失败，请重试");
                    });
                _this.appChange(appId, appName);
            });
        },
        appChange: function (appId, appName) {
            Y.mt.cookie.remove("appId");
            Y.mt.cookie.remove("appName");
            Y.mt.cookie.set("appId", appId, null, '/');
            Y.mt.cookie.set("appName", appName, null, '/');
            Y.all(".currentAppId").each(function (item) {
                /*jshint eqeqeq:false, -W041:false*/
                if (item.getAttribute("readonly") != null && item.getAttribute("readonly") != "true") {
                    item.set("value", appId);
                } else {
                    if (item.get("value") == null || item.get("value") == 0 || item.get("value") == '' || item.getAttribute("resource") == null || item.getAttribute("resource") == '') {
                        item.set("value", appId);
                    }
                    // ignore
                }
            });
            Y.all(".appIdHref").each(function (item) {
                /*jshint eqeqeq:false, -W041:false*/
                var href = item.getAttribute("href");
                if (href != null && href != "") {
                    if (href.indexOf("applicationId") !== -1) {
                        var newHref = href.replace(/applicationId=\d*/, "applicationId=" + appId);
                        item.set("href", newHref);
                    } else if (href.indexOf("?") !== -1) {
                        item.set("href", href + "&applicationId=" + appId);
                    } else {
                        item.set("href", href + "?applicationId=" + appId);
                    }
                }
            });
            Y.all(".currentAppName").each(function (item) {
                if (item.getAttribute("readonly") !== "true") {
                    item.set("value", appName);
                } else {
                    /*jshint eqeqeq:false, -W041:false*/
                    if (item.get("value") == 0 || item.get("value") == '' || item.getAttribute("resource") == null || item.getAttribute("resource") == '') {
                        item.set("value", appName);
                    }
                    // ignore
                }
            });

        }
    };

}, '1.0.0', { requires: [
    'w-base',
    'mt-cookie',
    'node',
    'mt-io'
]});
