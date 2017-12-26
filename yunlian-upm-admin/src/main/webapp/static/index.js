
(function($){
  var switchBg = function(){
    var bgEle = $('img.bg');
    var bgArr = ['/static/images/bg1.jpg','/static/images/bg2.jpg','/static/images/bg3.jpg','/static/images/bg4.jpg'];
    var bgIndex = $.cookie('bgIndex');
    bgIndex = bgIndex | bgArr.indexOf(bgEle.attr('src'));
    if(bgIndex == bgArr.length-1){
      bgIndex = 0;
    }else{
      bgIndex++;
    }
    $.cookie('bgIndex',bgIndex);
    bgEle.attr('src',bgArr[bgIndex]);
    setInterval(function(){
      if(bgIndex == bgArr.length-1){
        bgIndex = 0;
      }else{
        bgIndex++;
      }
      $.cookie('bgIndex',bgIndex);
      bgEle.attr('src',bgArr[bgIndex]);
    },60000);
  };
  var iconsToCenter = function(){
    var iconList = $('ul.icon-list');
    iconList.css({
      marginTop:-(iconList.height()/2),
      marginLeft:-(iconList.width()/2)
    });
  };
  iconsToCenter();
  switchBg();
})(jQuery);