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
            <div id="l-hd">
                <div class="l-hd-left">
                    <div class="l-hd-main">
                        <span class="logo"></span>
                        <h1>
                            <a href="/" title="壹化云链员工用户中心"
                                ><#-- TODO logo --><#--<img src="/static/img/logo.png">--></a>
                        </h1>
                    </div>
                    <div class="navbar navbar-inverse">
                        <div class="navbar-inner">
                            <ul id="menus" class="nav">
                                <#if __menus__??>
                                <#list __menus__ as m>
                                <li class="dropdown">
                                    <#if m.menus ?? && m.menus?size &gt; 0>
                                    <a href="javascript:void(0)" class="dropdown-toggle"
                                        data-toggle="dropdown"
                                        >${m.title}<b class="caret"></b></a>
                                    <#else>
                                    <a id="menu-${m.id}" class="menu-list"
                                        href="${m.url}"><span>${m.title}</span></a>
                                    </#if>
                                    <ul class="dropdown-menu">
                                        <#if m.menus??>
                                        <#list m.menus as i>
                                        <li>
                                            <a id="menu-${i.id}" class="menu-list"
                                                href="${i.url}"><span>${i.title}</span></a>
                                        </li>
                                        </#list>
                                        </#if>
                                    </ul>
                                </li>
                                </#list>
                                </#if>
                            </ul>
                        </div>
                    </div>

                    <ul class="user-info">
                        <#if _currentUser??>
                        <li>Hi，<a class="user-name"
                                title="个人中心">${_currentUser.name!"xxx"}</a>
                        </li>
                        </#if>
                        <li class="icon"><a href="/logout" title="退出"><i
                                    class="fa fa-power-off  fa fa-white"></i></a></li>
                    </ul>
                </div>
            </div>
            <div id="wrapper">
                <div id="l-bd">
                    <div class="container-fluid">
                        <div class="row-fluid">${body}</div>
                    </div>
                </div>
            </div>
            <div id="l-ft">
                &copy;${.now?string('yyyy')} fengjr.com
            </div>
        </div>
    </body>
    <#include "footer.inc" >
</html></#compress>
