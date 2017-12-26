<!doctype html>
<html>
<style>
    #logo {
        position: relative;
        top: 0px;
        left: -77px;
        max-width: 280px;
    }
</style>
<#include "header.inc" >
<body class="theme-cos">
<div id="doc" style="height: 100%">
    <div id="l-hd" style="height: 8%">
        <div class="l-hd-left">
            <div class="l-hd-main">
                <span class="logo"></span>

                <h1>
                    <a href="/" title="壹化云链统一权限管理系统">
                        <img id="logo" src="/static/img/logo.png">
                    </a>
                </h1>
            </div>
        </div>
        <ul class="user-info">
        <#if curUserName??>
            <li>Hi，<a class="user-name" href="/user/profile" title="个人中心">${curUserName!"xxx"}</a></li>
        </#if>
            <li class="icon"><a href="/logout" title="退出"><i
                    class="fa fa-power-off  fa fa-white"></i></a></li>
        </ul>
    </div>
    <div id="wrapper" style="height:86%;overflow-x:hidden;overflow-y:scroll" class="with-sidebar-icon">
        <div id="l-bd">
            <div class="container-fluid">
                <div class="row-fluid">${body}</div>
            </div>
        </div>
    </div>
    <div id="l-ft">
        @ 2015 壹化云链
    </div>
</div>
<#--<script type="text/javascript"-->
<#--src="http://task.sankuai.com/s/d41d8cd98f00b204e9800998ecf8427e/zh_CNwl4cgq-1988229788/6157/63/1.4.0/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?collectorId=ae8a6fa9"></script>-->
<#if __user__??>
<script type="text/javascript">
</script>
<script type="text/javascript" src="/static/js/common/common.js"></script>
</#if>
</body>
<#include "footer.inc" >
</html>
