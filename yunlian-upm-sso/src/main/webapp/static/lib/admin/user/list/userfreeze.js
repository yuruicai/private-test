var jquery = window.jQuery || require('jquery');

var userstatusstore = require('./userstatusstore');
var actions = require('./actions');

module.exports = function() {
    this.previousStatus = userstatusstore.getState(this.id);
    if (!window.confirm('确定要进行此操作？')) {
        return;
    }
    var ctx = this;
    var url = '/admin/user/' + (this.previousStatus === 'active' ? 'freeze' : 'recovery');
    jquery.get(url, { id: this.id }, function(/*data*/) {
        actions.toggleUserFreeze(ctx.id);
        updateView(ctx);
    });
};

function updateView(ctx) {
    var $el = jquery(ctx.el);
    var status = userstatusstore.getState(ctx.id);
    var previousStatus = ctx.previousStatus;

    var statusText = ctx.statusMap[status];
    $el.parents('tr').find('.userstatus-describe').text(statusText);
    var toggleText = ctx.statusMap[previousStatus];
    if (status === 'freeze') {
        toggleText = '解冻';
    }
    $el.text(toggleText);
}
