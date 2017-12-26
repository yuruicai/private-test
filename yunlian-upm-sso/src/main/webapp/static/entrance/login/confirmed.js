/*jshint node:true*/
/*global $*/

exports.init = function() {
    bindUI();
};

function bindUI() {
    $('.J-qrcode-confirm').on('submit', function(e) {
        e.preventDefault();
        var data = $(e.target).serialize();
        $.post('/qrcode/confirmed', data, function(data) {
            if (data.code === 201) {
                window.location.href = 'mtxm://close';
            } else {
                window.location.reload();
            }
        });
    });
}


