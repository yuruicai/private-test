<div id="sign-up">
    <h1>创建应用</h1>

    <div class="sign-box shadow-box">
        <form id="applicationCreateForm" class="form-horizontal" method="post" action="/app/save">

            <div class="control-group">
                <label class="control-label"><i class="required"></i>应用名称</label>

                <div class="controls">
                    <input name="name" id="name" type="text" size="50" data-rules="required" value="<#if application??>${application.name!""}</#if>"/>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label"><i class="required"></i>应用appkey</label>

                <div class="controls">
                    <input name="appkey" id="appkey" type="text" size="50" data-rules="required" value="<#if application??>${application.appkey!""}</#if>"/>
                </div>
            </div>

            <#--<div class="control-group">-->
                <#--<label class="control-label"><i class="required"></i>secret</label>-->

                <#--<div class="controls">-->
                    <#--<input name="secret" id="secret" type="text" size="50" data-rules="required" value="<#if application??>${application.secret!""}</#if>"/>-->
                <#--</div>-->
            <#--</div>-->

            <div class="control-group">
                <label class="control-label"><i class="required"></i>url</label>

                <div class="controls">
                    <input name="url" id="url" type="text" size="50" data-rules="required" value="<#if application??>${application.url!""}</#if>"/>
                </div>
            </div>
            <#if application??>
                <input type="text" 　name="id" id="id" value="<#if application??>${application.id!""}</#if>" data-rules="required" />
            </#if>

            <div class="control-group">
                <div class="controls">
                    <input type="button" id="saveButton" style="margin: 0 auto 0 10px;" class="btn btn-primary" value="保存">
                    <input type="button" id="cancleButton" style="margin: 0 auto 0 10px;" class="btn btn-primary" onclick="javascript:window.location.href = '/app/list'" value="取消" >
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript">
   $('#saveButton').click(function () {
       var id = $("#id").val();
       var name = $("#name").val();
       var appkey = $("#appkey").val();
       var secret = $("#secret").val();
       var url = $("#url").val();
       if(name == null || name.trim()=='' ){
           layer.alert("应用名称不能为空");
           return;
       }
       if(name.length > 30){
           layer.alert("应用名称长度不能大于30字符");
           return;
       }
       if(appkey == null || appkey.trim()=='' ){
           layer.alert("appkey不能为空");
           return;
       }
       if(appkey.length > 30){
           layer.alert("appkey长度不能大于30个字符");
           return;
       }
       if(url == null || url.trim()=='' ){
           layer.alert("url不能为空");
           return;
       }

       var strRegex = "((http|ftp|https)://)(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})*(/[a-zA-Z0-9\&%_\./-~-]*)?";
       var re=new RegExp(strRegex);
       if (!re.test(url)) {
           alert("url不符合格式")
           return false;
       }
       if(url.length > 200){
           layer.alert("url长度不能大于200个字符");
           return;
       }


       $.get("/app/queryByAppkey.ajax", {"appkey" : appkey}, function (result) {
           if(result.data != null){
               layer.alert("相同appkey的应用已经存在");
               return;
           }

           $("#applicationCreateForm").submit();
       }, "json");
   })



</script>
