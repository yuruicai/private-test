//初始化表头上的应用信息
$(function () {
    //获取应用列表
    var url = '/apps.ajax';
    $.get(url, function (response) {
        if (response != null && response != '') {
            var result = eval(response);
            if (result != undefined && result != null && result.data != undefined && result.data != null && result.data.length > 0) {
                $.each(result.data, function (index, item) {
                    var appId = $.cookie('appId');
                    var appName = $.cookie('appName');
                    if (appId != undefined && appId != null && appId == item.id) {
                        $('#readAppList').append('<option selected="true" value ="' + item.id + '">' + item.name + '</option>');
                    } else {
                        $('#readAppList').append('<option value ="' + item.id + '">' + item.name + '</option>');
                    }
                });
                if($.cookie('appId')==undefined||$.cookie('appId')==null||$.cookie('appId')=='null'){
                    $.cookie('appId', result.data[0].id);
                    $.cookie('appName', result.data[0].name);
                }
            }
        }
    });
});


//选择应用
// 查询应用下的角色信息
$('#readAppList').change(function () {
    var forms = $('.autoSelectApp');

    $.each(forms, function (index, item) {
        var appId = $("#readAppList  option:selected").val();
        var appName = $("#readAppList  option:selected").text();
        updataCookie(appId, appName);

        //更新url信息
        var query = window.location.search;
        var host = window.location.host;
        var uri = window.location.pathname;
        var href = '';
        if (query.indexOf("applicationId") !== -1) {
            var newQuery = query.replace(/applicationId=\d*/, 'applicationId=' + appId);
            href = 'http://' + host + uri + newQuery;
        } else {
            href = 'http://' + host + uri + query;
        }
        window.location.href = href;
    });
});

//更新cookie中的值：appId,appName
function updataCookie(appId, appName) {
    //更新cookie值
    $.cookie('appId', appId, {path: '/'});
    $.cookie('appName', appName, {path: '/'});

    //更新页面中的含有currentAppId class的元素的值
    var currentAppIds = $('.currentAppId');
    if (currentAppIds != undefined && currentAppIds != null) {
        $.each(currentAppIds, function (index, item) {
            $(item).val(appId);
        });
    }

    var appIdHrefs = $('.appIdHref');
    if (appIdHrefs != undefined && appIdHrefs != null) {
        $.each(appIdHrefs, function (index, item) {
            var href = $(item).data('href');
            if (href != undefined && href != null && href != "") {
                if (href.indexOf('applicationId') !== -1) {
                    var newHref = href.replace(/applicationId=\d*/, 'applicationId=' + appId);
                    item.set('href', newHref);
                } else if (href.indexOf('?') !== -1) {
                    item.set('href', href + "&applicationId=" + appId);
                } else {
                    item.set('href', href + "?applicationId=" + appId);
                }
            }
        });

        var currentAppNames = $('.currentAppName');
        if (currentAppNames != undefined && currentAppNames != null) {
            $.each(currentAppNames, function (index, item) {
                $(item).val(appName);
            });
        }
    }

}