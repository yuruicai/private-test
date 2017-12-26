/**
 * Created by likaihua on 2015-12-30.
 */
(function($){

  var createDom = function(data){
    var head = $('head');
    var body = $('body');
    body = body.length > 0 ? body[0]:$('html');
    var styleText = '<style>body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,p,blockquote,table,th,td,iframe,article,aside,canvas,details,figcaption,figure,footer,header,hgroup,menu,nav,section,summary,time,mark,audio,video,span,p{margin:0;padding:0;font-family:"Helvetica Neue",tahoma,"PingHei","Hiragino Sans GB",stheiti,"WenQuanYi Micro Hei","Microsoft Yahei",sans-serif;}.clearfix{zoom:1;}.clearfix:after{content:".";display:block;height:0;clear:both;line-height:0;visibility:hidden;}li{list-style:none;vertical-align:center;}a{cursor:pointer;outline:none;}a:hover{text-decoration:none;}a:focus{text-decoration:none;}a:visited,a:active{text-decoration:none;}img{border:0;border:none;}.home{position:fixed;width:40px;height:40px;right:10px;bottom:10px;background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAABSCAYAAAAmR5bKAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTExIDc5LjE1ODMyNSwgMjAxNS8wOS8xMC0wMToxMDoyMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RTZFREQ4RjlBN0IzMTFFNTg1NkNCNUJFNTc1RTQyRjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RTZFREQ4RkFBN0IzMTFFNTg1NkNCNUJFNTc1RTQyRjYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpFNkVERDhGN0E3QjMxMUU1ODU2Q0I1QkU1NzVFNDJGNiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpFNkVERDhGOEE3QjMxMUU1ODU2Q0I1QkU1NzVFNDJGNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pi2DhaAAAAG4SURBVHja7JnrTQMxDMdzp1uhG8DnIrEBoO7ABnxnDuZgh0o3AnDiM0zAECFGpDpFedi5RDXobynqqc3j58fZrjLYh8udMeboxpXRJYsbh1EpnPllOo5K4U6Qo1EuAAQgAAsysWY9PhtzcZ2f8/FizNN93fzNFiwdFs6RzkcMAhCAANQOSEm1JJ+v9fMzMriW38LFqMWoxQAEIABRi1GLEYMABOB/BlwU870T4IEeFMJRtr+jlv/Ljb1WEw72Rn8M0k3TmxtW2SCmHVmQHrRe5iwEiGYBgAD8y4ATe+bsXvbbgffdWuj3OZEowrXVgP6A9UF+8xhQDN6DptZVu9hv5DeOAdCnHxwlZ9swBkON15tLDg2VaR6DpU1TCuQUbAaYC/TQKo1jcBKpE7Mi56DYOqabZYApmFi6Sa2JZYKuFpSA5xRpAiixshCkr4tzVs65mKEIGlYAJmNRUhFmq9yCpUrUrN0qfd8otdRZMNZSldqssGpUNAx9XMzpoJs1rL1isst/koZvZ9+GNVZvpWkn7A83uZjjklSjunHf8ZzxdZ52i5OgBUqgmwEgABmAuGmqlJ+bpm8BBgAACt1ZbW2qRQAAAABJRU5ErkJggg==);background-position:0 0;cursor:pointer;    transition: all 0.3s ease 0s;}.home:hover{background-position:0-42px;}.panel-fengjr{position:fixed;right:5px;bottom:-1000px;display:inline-block;padding:10px;background-color:rgba(54,54,56,0.8);z-index:9;overflow:hidden;}ul.fengjr-icon-list{width:792px;}ul.fengjr-icon-list li{position:relative;width:120px;height:120px;margin:5px;float:left;cursor:pointer;border:1px solid rgba(255,255,255,0);}ul.fengjr-icon-list li:hover{border:1px solid rgba(255,255,255,1);}ul.fengjr-icon-list li a{display:block;}</style>';
    head.append(styleText);
    $('<div class="home"></div>').appendTo(body);
    $('<div class="panel-fengjr"></div>').appendTo(body);
    $('<ul class="fengjr-icon-list clearfix"></ul>').appendTo($('.panel-fengjr'));
    for(var i=0;i<data.length;i++){
      var html = '<li><a href="'+data[i].url+'" target="_blank"><img src="'+data[i].image1+'" alt=""/></a></li>';
      $('.fengjr-icon-list').append(html);
    }
  };
  var bindEvents = function(){
    $('.home').click(function(e){
      e.stopPropagation();
      $('.panel-fengjr').animate({
        bottom:5
      },100);
    });
    $(document).click(function(e){
      var target  = $(e.target);
      if(target.closest(".panel-fengjr").length == 0){
        $('.panel-fengjr').animate({
          bottom:-1000
        },100);
      }
    });
  };

  $.getJSON( "/upmApps", function(data) {
    createDom(data);
    bindEvents();
  });
})(jQuery);