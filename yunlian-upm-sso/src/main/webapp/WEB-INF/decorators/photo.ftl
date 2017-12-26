<!doctype html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">

    <link href="${base}/static/node_modules/mtfe_cos-ui/cos-ui.css" rel="stylesheet">
    <link href="${base}/static/css/sso.css" rel="stylesheet">

<#if isEmbedPage?? && isEmbedPage == true>
    <link href="${base}/static/css/embed.css" rel="stylesheet">
</#if>
<#assign cos_siteKey = 'sso'/>
<#assign cos_yuiVersion = '3.13.0'/>
<#assign cos_useUI = true/>
<#assign cos_gaAccount = 'UA-28174807-12'/>
    <script src="${base}/static/node_modules/jquery/dist/jquery.js"></script>
    <script src="${base}/static/entrance/common/common-bundle.js"></script>
</head>
<body class="theme-cos">
<div id="doc">
    <div id="wrapper">
        <div id="l-bd">
            <div class="container-fluid">
                <div class="row-fluid">${body}</div>
            </div>
        </div>
    </div>
</div>
</body>
<#include "footer.inc" >
</html>
