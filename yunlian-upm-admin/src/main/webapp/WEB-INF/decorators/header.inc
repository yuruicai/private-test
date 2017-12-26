<head>
<title>权限管理系统</title>
<script src="${base}/static/jquery-2.0.3.min.js" type="text/javascript"></script>
<script src="${base}/static/jquery.cookie.js" type="text/javascript"></script>
<script src="${base}/static/entrance/common/common-bundle.js"></script>
<script src="${base}/static/js/common/header.js"></script>
<script src="/static/layer/layer.js"></script>
<link href="/static/css/cosui.min.css" rel="stylesheet" />
<link href="/static/css/upma.css" rel="stylesheet">
<link href="/static/css/bootstrap.min.css" rel="stylesheet">
${head}
<script type="text/javascript">
        var successMsg =null;
        var errorMsg =null;
        <#if errMsg??>
          errorMsg='${errMsg!''}';
        </#if>
        <#if msg??>
          successMsg='${msg!''}';
        </#if>
</script>
</head>
