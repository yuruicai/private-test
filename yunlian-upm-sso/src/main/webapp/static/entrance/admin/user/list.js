var jquery = window.jQuery || require('jquery');

var userlist = require('../../../lib/admin/user/list');
var aria = require('../../../lib/aria');
var sc = require('../../../lib/simplecomponent');
// ENUM
var statusMap = null;

module.exports = {
    init: init
};

function init(config) {
    userlist.initStore(config.users);
    statusMap = Object.keys(config.status).reduce(function(prev, index) {
        var item = config.status[index];
        prev[item.name] = item.text;
        return prev;
    }, {});
    scanRoleButton(config.container);
    batchResetPassword();
}

function scanRoleButton(container) {
    jquery(container).on('click', '[role="button"]', onclick);
}

function onclick(e) {
    var $el = $(e.currentTarget);
    var label = $el.attr('aria-label');
    var action = userlist[label];
    if (typeof action !== 'function') {
        return;
    }
    var ctx = aria(e.currentTarget);
    ctx.username = $el.parents('tr').find('td').eq(2).text();
    ctx.statusMap = statusMap;
    ctx.setState = sc.setState;
    ctx.id = $el.data('id');
    ctx.el = e.currentTarget;
    action.call(ctx);
}

function batchResetPassword(){

    jquery('#allids').on("click", function(){
        var flag = this.checked;
        jquery('input[name="checkboxids"]').each(function(){
            this.checked = flag;
        });
    });

    jquery('input[name="checkboxids"]').on("click",function(){
        jquery('#allids').attr("checked", jquery('input[name="checkboxids"]').length === jquery('input[name="checkboxids"]:checked').length ? true : false);
    });

    jquery('#batchresetpassword').on('click', function(){
        var uri = '/admin/user/resetPassBySys';
        if (!window.confirm('确定要批量重置密码?')) {
            return;
        }
        var failMsg = '';
        var failed = 0;
        jquery('input[name="checkboxids"]:checked').each(function(){
            jquery.get(uri, { id: jquery(this).val() })
                .fail(function() {
                    failMsg = failMsg + ',' + jquery(this).parents('tr').find('td').eq(2).text();
                });
        });

        if(failed >0){
            window.alert(failMsg + "重置密码失败");
        }else{
            window.alert("批量重置密码全部成功");
        }

    });
}