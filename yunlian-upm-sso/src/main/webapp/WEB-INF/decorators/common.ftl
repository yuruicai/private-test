<#compress><!doctype html>
<html>
    <head>
        <#include "header.inc" >
        <style>
            .dropdown-menu { margin-top:0; }
            #logo {
                max-width: 380px;
            }
        </style>
    </head>
    <body class="theme-cos">
        <div id="doc" style="height: 100%">
            <div id="l-hd" style="height: 8%">
                <div class="l-hd-left">
                    <div class="l-hd-main">
                        <h1>
                            <a href="/" title="壹化云链员工用户中心">
                                <img id="logo" src="/static/img/logo.png">
                            </a>
                        </h1>
                    </div>
                    <div class="navbar navbar-inverse">
                        <div class="navbar-inner">
                            <ul id="menus" class="nav">
                                <#if __menus__??>
                                <#list __menus__ as m>
                                <li class="dropdown">
                                    <a href="javascript:void(0)" class="dropdown-toggle"
                                        data-toggle="dropdown">${m.title}<b class="caret"></b></a>
                                    <ul class="dropdown-menu" role="menu">
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
                        <#if currentUserName??>
                        <li>Hi，<a class="user-name" href="#" title="个人中心">${currentUserName!"xxx"}</a></li>
                        </#if>
                        <li class="icon"><a href="/logout" title="退出"><i
                                    class="fa fa-power-off  fa fa-white"></i></a></li>
                    </ul>
                </div>
            </div>
            <div id="wrapper" style="height:86%;overflow-x:hidden;overflow-y:scroll">
                <div id="l-bd">
                    <div class="container-fluid">
                        <div class="row-fluid">${body}</div>
                    </div>
                </div>
            </div>
            <div id="l-ft">
                <p> ©${.now?string.yyyy} sinochem.com </p>
            </div>
        </div>
        <#include "footer.inc" >
        <script type="text/javascript" src="/static/jquery.menu.js"></script>
    </body>
</html></#compress>
