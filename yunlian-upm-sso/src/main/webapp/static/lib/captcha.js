/*jshint node:true */
/*global $*/
exports.init = function(config) {
    var img = document.getElementById(config.imgId);
    function renderImg() {
        if(img){
            var src = img.src.split('?')[0];
            img.src = src+'?captchaId=' + new Date().getTime();
        }
    }
    renderImg();
    $(img).click(renderImg);
};
