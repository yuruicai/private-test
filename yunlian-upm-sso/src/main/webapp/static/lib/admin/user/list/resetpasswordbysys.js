var jquery = window.jQuery || require('jquery');

module.exports = function() {
    var uri = '/admin/user/resetPassBySys';
    if (!window.confirm('确定要重置密码?')) {
        return;
    }
    jquery.get(uri, { id: this.id })
        .done(function(data) {
            window.alert(data.result.msg);
        })
        .fail(function() {
            window.alert('发生错误，操作失败!');
        });
};
