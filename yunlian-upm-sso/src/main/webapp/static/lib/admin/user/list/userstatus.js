var jquery = window.jQuery || require('jquery');

var userstatusstore = require('./userstatusstore');
var actions = require('./actions');

module.exports = function() {
    if (!window.confirm('确定要进行此操作？')) {
        return;
    }
    this.previousStatus = userstatusstore.getState(this.id);
    var ctx = this;
    var url = '/admin/user/' + (this.previousStatus === 'active' ? 'disable' : 'enable');
    jquery.get(url, { id: this.id }, function(/*data*/) {
        actions.toggleUserStatus(ctx.id);
        updateView(ctx);
    });
};

function updateView(ctx) {
    var $el = jquery(ctx.el);
    var status = userstatusstore.getState(ctx.id);
    var statusText = ctx.statusMap[status];
    $el.parents('tr').find('.userstatus-describe').text(statusText);
    var previousStatus = ctx.previousStatus;
    var toggleText = ctx.statusMap[previousStatus];
    $el.text(toggleText);
}
