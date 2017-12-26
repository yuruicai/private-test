<title>${title!"壹化云链"}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">

<link href="${base}/static/css/cosui.min.css" rel="stylesheet">
<link href="${base}/static/css/sso.css" rel="stylesheet">


<#if isEmbedPage?? && isEmbedPage == true>
    <link href="${base}/static/css/embed.css" rel="stylesheet">
</#if>
<#assign cos_siteKey = 'sso'/>
<#assign cos_yuiVersion = '3.13.0'/>
<#assign cos_useUI = true/>
<#assign cos_gaAccount = 'UA-28174807-12'/>
<script src="${base}/static/jquery-2.0.3.min.js" type="text/javascript"></script>
<script src="${base}/static/lib/bootstrap-dropdown.js" type="text/javascript"></script>
<script src="${base}/static/entrance/common/common-bundle.js"></script>
<script>require('common');</script>

