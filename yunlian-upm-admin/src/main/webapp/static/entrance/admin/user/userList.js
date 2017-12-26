/**
 * Created by zhanggm on 2015/11/19.
 */

$('#allids').on("click", function(){
    var flag = this.checked;
    $('input[name="checkboxids"]').each(function(){
        this.checked = flag;
    });
});

$('input[name="checkboxids"]').on("click",function(){
    $('#allids').attr("checked", $('input[name="checkboxids"]').length === $('input[name="checkboxids"]:checked').length ? true : false);
});

$('.queryMobile').on('click', function(){
    $('.queryMobile').text('查看手机');
    curObj = $(this);
    $.get('/user/mobile', { id: curObj.attr('data-id') }, function(data/*, textStatus, jqXHR*/) {
        var msg = data.result.msg;
        if (msg === null) {
            msg = '还没有手机号';
            return false;
        }
        curObj.parent().html(msg);
        //$(self.el).replaceWith('<span>' + msg + '</span>');
    });
    curObj.unbind();
});

$('.restPwd').on('click', function(){
    curObj = $(this);
    $.get('/user/resetPass', { id: curObj.attr('data-id') }, function(data/*, textStatus, jqXHR*/) {
        var msg = data.result.msg;
        if (msg === null) {
            msg = '重置失败';
            return false;
        }
        curObj.html(msg);
        //$(self.el).replaceWith('<span>' + msg + '</span>');
    });
});

$('.userStatusChange').on('click', function(){

    var url = '';
    curObj = $(this);

    var flag = curObj.attr('flag');
    if('freeze' == flag){
        url = '/user/freeze';
    }

    if('recovery' == flag){
        url = '/user/recovery';
    }

    $.get(url, { id: curObj.attr('data-id') }, function(data/*, textStatus, jqXHR*/) {
        var msg = data.result.msg;
        if (msg === null) {
            msg = '冻结失败';
            return false;
        }
        window.location.reload();
        //$(self.el).replaceWith('<span>' + msg + '</span>');
    });
});













$('#batchresetpassword').on('click', function(){
    var uri = '/admin/user/resetPassBySys';
    if (!confirm('确定要批量重置密码?')) {
        return;
    }
    var failMsg = '';
    var failed = 0;
    $('input[name="checkboxids"]:checked').each(function(){
        $.get(uri, { id: $(this).val() })
            .fail(function() {
                failMsg = failMsg + ',' + $(this).parents('tr').find('td').eq(2).text();
            });
    });

    if(failed >0){
        window.alert(failMsg + "重置密码失败");
    }else{
        window.alert("批量重置密码全部成功");
    }

});
