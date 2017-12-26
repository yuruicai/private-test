<div id="sign-up">
    <h1>编辑应用</h1>

    <div class="sign-box shadow-box">
        <form id="applicationCreateForm" class="form-horizontal" method="post" action="/app/save">
            <input  name="id" id="id" type="hidden" size="50" data-rules="required" value="<#if application??>${application.id!""}</#if>"/>
            <#--<input type ="hidden" name="id" value="<#if application??>${application.id!""}</#if>">-->
            <div class="control-group">
                <label class="control-label"><i class="required"></i>应用名称</label>

                <div class="controls">
                    <input name="name" id="name" type="text" size="50" data-rules="required" value="<#if application??>${application.name!""}</#if>"/>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label"><i class="required"></i>appkey</label>

                <div class="controls">
                    <input name="appkey" id="appkey" type="text" size="50" data-rules="required" value="<#if application??>${application.appkey!""}</#if>"/>
                </div>
            </div>

            <#--<div class="control-group">-->
                <#--<label class="control-label"><i class="required"></i>secret</label>-->

                <#--<div class="controls">-->
                    <#--<input disabled="disabled" name="secret" id="secret" type="text" size="50" data-rules="required" value="<#if application??>${application.secret!""}</#if>"/>-->
                <#--</div>-->
            <#--</div>-->

            <div class="control-group">
                <label class="control-label"><i class="required"></i>url</label>

                <div class="controls">
                    <input name="url" id="url" type="text" size="50" data-rules="required" value="<#if application??>${application.url!""}</#if>"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i class="required"></i>是否接入权限</label>
                <div class="controls">
                    <input type="radio" name="useUpm" value="1" <#if !application?? || (application?? && application.useUpm == 1)>checked</#if>/> 是
                    <input type="radio" name="useUpm" value="0" <#if application?? && application.useUpm == 0>checked</#if>/> 否
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">应用图标1</label>

                <div class="controls">
                    <#if application?? && application.image1?? && application.image1 != ''>
                        <span class="image1">${application.image1}<a href="#" class="btn btn-primary">删除</a></span>
                    <#else>
                    <input type="file" name="image" id="image1" />
                    </#if>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">应用图标2</label>
                <div class="controls">
                    <#if application?? && application.image2?? && application.image2 != ''>
                        <span class="image2">${application.image2}<a href="#" class="btn btn-primary">删除</a></span>
                    <#else>
                    <input type="file" name="image" id="image2" />
                    </#if>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input type="button" id="saveButton" style="margin: 0 auto 0 10px;" class="btn btn-primary" value="保存"><input type="button" id="cancleButton" style="margin: 0 auto 0 10px;" class="btn btn-primary" onclick="javascript:window.location.href = '/app/list'" value="取消" >
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" src="/static/jquery.ajaxfileupload.js"></script>
<script type="text/javascript">
   $('#saveButton').click(function () {
       var id = $("#id").val();
       var name = $("#name").val();
       var appkey = $("#appkey").val();
       var secret = $("#secret").val();
       var url = $("#url").val();
       if(name == null || name.trim()=='' ){
           alert("应用名称不能为空");
           return;
       }
       if(name.length > 30){
           alert("应用名称长度不能大于30字符");
           return;
       }
       if(appkey == null || appkey.trim()=='' ){
           alert("appkey不能为空");
           return;
       }
       if(appkey.length > 30){
           alert("appkey长度不能大于30字符");
           return;
       }
       if(url == null || url.trim()=='' ){
           alert("url不能为空");
           return;
       }
       if(url.length > 200){
           alert("url长度不能大于200字符");
           return;
       }
       var strRegex = "((http|ftp|https)://)(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})*(/[a-zA-Z0-9\&%_\./-~-]*)?";
       var re=new RegExp(strRegex);
       if (!re.test(url)) {
           alert("url不符合格式")
           return false;
       }
       $.get("/app/queryByAppkey.ajax", {"appkey" : appkey}, function (result) {
           if(result.data != null && result.data != id){
               layer.alert("相同appkey的应用已经存在");
               return;
           }

           $("#applicationCreateForm").submit();
       }, "json");
   })
   $(document).ready(function() {
       var interval;

       function applyAjaxFileUpload(element) {
           var id = $("#id").val();
           $(element).AjaxFileUpload({
               action: "/app/uploadImage?appId=" + id+"&imageId="+ $(element).attr("id"),
               onChange: function(filename) {
                   // Create a span element to notify the user of an upload in progress
                   var $span = $("<span />")
                           .attr("class", $(this).attr("id"))
                           .text("Uploading")
                           .insertAfter($(this));

                   $(this).remove();

                   interval = window.setInterval(function() {
                       var text = $span.text();
                       if (text.length < 13) {
                           $span.text(text + ".");
                       } else {
                           $span.text("Uploading");
                       }
                   }, 200);
               },
               onSubmit: function(filename) {
                   return true;
               },
               onComplete: function(filename, response) {
                   window.clearInterval(interval);
                   if (typeof(response.error) === "string") {
                       $span.replaceWith($fileInput);

                       applyAjaxFileUpload($fileInput);

                       alert(response.error);

                       return;
                   }

                   var $span = $("span." + $(this).attr("id")).text(response.data + " "),
                           $fileInput = $("<input />")
                                   .attr({
                                       type: "file",
                                       name: $(this).attr("name"),
                                       id: $(this).attr("id")
                                   });


                   $("<a />")
                           .attr("href", "#")
                           .attr("class", "btn btn-primary")
                           .text("删除")
                           .bind("click", function(e) {
                               $span.replaceWith($fileInput);

                               applyAjaxFileUpload($fileInput);
                           })
                           .appendTo($span);
               }
           });
       }

       applyAjaxFileUpload("#image1");
       applyAjaxFileUpload("#image2");

        <#if application?? && application.image1?? && application.image1 != ''>
            $('.image1 a').click(function(){
                $image1Input = $("<input />")
                        .attr({
                            type: "file",
                            name: "image",
                            id: "image1"
                        });
                $(this).parent().replaceWith($image1Input);
                applyAjaxFileUpload($image1Input);
            });
        </#if>

   <#if application?? && application.image2?? && application.image2 != ''>
       $('.image2 a').click(function(){
           $image2Input = $("<input />")
                   .attr({
                       type: "file",
                       name: "image",
                       id: "image2"
                   });
           $(this).parent().replaceWith($image2Input);
           applyAjaxFileUpload($image2Input);
       });
   </#if>
   });


</script>
