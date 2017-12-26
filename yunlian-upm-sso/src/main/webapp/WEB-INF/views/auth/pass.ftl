<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <#if supportPost?? && supportPost == true >
    <script type='text/javascript'>
        window.onload = function () {
            var form = document.getElementById('passForm');
            form.submit();
        };
        setTimeout( function(){
            var form = document.getElementById('passForm');
            form.submit();
        }, 5000);
    </script>
    <#else>
    <script language="JavaScript">
        setTimeout( function(){
            window.location.href= decodeURIComponent("${service?url}");
        }, 5000);

        window.onload = function () {
            window.location.href= decodeURIComponent("${service?url}");
        };
    </script>
    </#if>
</head>
<body>
<#if supportPost?? && supportPost == true >
<form id="passForm" method="post" action="${service!''}">
    <input type="hidden" name="SID" value="${sid!''}"/>
    <input type="hidden" name="time" value="${time!''}"/>
    <input type="hidden" name="sign" value="${sign!''}"/>
    <input type="submit" name="sub" style="display:none;">
</form>
</#if>
<#if ssoskt?? && (ssoskt?length gt 0) && isSetCookie?? && isSetCookie >
<div style = "display:none">
    <img src="http://crosset.sankuai.info/index.php?ssoskt=${ssoskt?url}" />
    <img src="http://crosset.fengjr.com/index.php?ssoskt=${ssoskt?url}" />
</div>
</#if>
</body>
</html>
