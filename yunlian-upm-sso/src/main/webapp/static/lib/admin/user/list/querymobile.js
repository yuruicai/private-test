var jquery = window.jQuery || require('jquery');

module.exports = querymobile;

function querymobile() {
    var self = this;
    jquery.get('/admin/user/mobile', { id: this.id }, function(data/*, textStatus, jqXHR*/) {
        var msg = data.result.msg;
        if (msg === null) {
            msg = '还没有手机号';
        }
        jquery(self.el).replaceWith('<span>' + msg + '</span>');
    });
}

