<#compress><!doctype html>
<html>
    <head>
        <#include "header.inc" >
        <style>
            .dropdown-menu { margin-top:0; }
        </style>
    </head>
    <body class="theme-cos">
        <div id="doc">
            <div id="bd" class="clearfix">
                <div id="l-hd">
                    <div class="l-hd-left">
                        <div class="l-hd-main">
                           <span class="logo"></span>
                            <h1>
                                <a href="/" title="壹化云链员工用户中心"></a>
                            </h1>
                        </div>
                    </div>
                    <ul class="user-info">
                        <#if _currentUser??>
                        <li>Hi，<a class="user-name" href="#" title="个人中心"
                                >${_currentUser.name!""}</a>
                        </li>
                        </#if>
                        <li class="icon">
                            <a href="/logout" title="退出"
                                ><i class="fa fa-power-off fa fa-white"></i></a>
                        </li>
                    </ul>
                </div>

                <div id="wrapper" class="with-sidebar-icon">

                    <#-- wait for react sidebar
                    <#if _currentUser?? && __menus__json??>
                    <div id="sidebar"></div>
                    <script src="/static/entrance/sidebar-bundle.js"></script>
                    <script>
                        React.renderComponent(require('app/sidebar')({
                            menus: ${__menus__json}
                        }), document.getElementById('sidebar'));
                    </script>
                    </#if>
                    -->

                    <#if _currentUser??>
                    <div id="main-nav-bg"></div>
                    <div id="main-nav" class="main-nav-fixed">
                        <div class="navigation">
                            <#include 'sidebar.ftl' encoding="UTF-8">
                        </div>
                    </div>
                    </#if>

                    <div id="l-bd" class="right-all">
                        <div class="container-fluid">${body}</div>
                    </div>
                </div>
                <div id="l-ft">
                    <p>
                        <#if _timeCost??>
                        <span class="ft-time">页面响应时间：${_timeCost} ms</span>
                        </#if>
                        ©${.now?string('yyyy')} sinochem
                        <#if _hostname??>
                        <span class="ft-time">服务器：${_hostname}</span>
                        </#if>
                    </p>
                </div>

            </div>
        </div>
    </body>
    <#include "footer.inc" >
</html></#compress>
