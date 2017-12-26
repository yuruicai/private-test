
$(function() {
    $('#recheckPhone').click(function() {
        sendsms();
    });
});

function setTimer() {
    var ndRecheck = $('#recheckPhone');
    var count = 60;
    var timer = window.setInterval(function () {
        count--;
        if (count > 0) {
            ndRecheck.val(count + '秒后重新获取短信');
        } else {
            window.clearInterval(timer);
            ndRecheck.val( "重新获取短信");
            ndRecheck.attr('disabled',false);
        }
    }, 1000);
}

function sendsms(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/user/sendSmsCode",
        dataType: 'json',
        success: function() {
            $('#recheckPhone').attr("disabled",true);
            setTimer();
        }
    });
}
