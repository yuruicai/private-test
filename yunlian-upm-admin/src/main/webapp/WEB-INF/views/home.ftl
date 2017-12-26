<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>壹化云链—权限管理系统</title>
    <link rel="stylesheet" href="/static/css/index.css">
</head>
<body>
<img src="/static/images/bg1.jpg" alt="" class="bg"/>
<div class="wrap">
    <div class="head">
        <div class="title clearfix">
            <div class="logo">
                <img src="/static/images/logo.png" style="width: 150px;" alt="壹化云链"/>
            </div>
            <div class="name">后台管理中心</div>
        </div>
        <div class="userinfo clearfix">
            <div class="user">
                Hi,
                <span class="username">${_currentUser.name}</span>
            </div>
            <div class="icon"></div>
            <a href="/logout?path=/home"><div class="exit"></div></a>
        </div>
    </div>
    <ul class="icon-list clearfix">
        <#list applications as a>
        <li>
            <a href="${a.url}" target="_blank">
                <img src="${a.image1!''}" alt=""/>
                <#--<img src="${a.image2}" alt="" class="back"/>-->
            </a>
        </li>
        </#list>
    </ul>
</div>
<script src="static/jquery-2.0.3.min.js" charset="utf-8"></script>
<script src="static/jquery.cookie.js" charset="utf-8"></script>
<script src="static/index.js" charset="utf-8"></script>
</body>
</html>
