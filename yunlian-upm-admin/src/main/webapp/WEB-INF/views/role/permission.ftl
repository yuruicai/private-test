<style type="text/css" xmlns="http://www.w3.org/1999/html">
    .margin-type {
        margin-right: 10px;
    }

    .height-input-type {
        margin-bottom: 0;
        height: 30px;
    }
</style>
<link rel="stylesheet" type="text/css" href="/static/css/default.css">
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css">
<link rel="stylesheet" href="/static/css/combo.select.css">
<link rel="stylesheet" href="/static/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" type="text/css">

<script type="text/javascript" src="/static/jquery.combo.select.js"></script>
<script type="text/javascript" src="/static/js/role/permission.js"></script>

<#--<script type="text/javascript" src="/static/jquery-ztree/3.5.12/js/jquery-1.4.4.min.js"></script>-->
<script type="text/javascript" src="/static/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/jquery-ztree/3.5.12/js/jquery.ztree.exedit-3.5.js"></script>


<div id="role-permissions-conf">
    <div class="single-roleconf span4 user-column">
        <ul class="nav nav-tabs">
            <li class="active">
                <a href="javascript:void(0)">选择需要配置的角色</a>
            </li>
        </ul>
        <div class="sign-box shadow-box">
            <div class="role-search">
                角色搜索：
                <select class="js-select height-type">
                </select>
                <input type="hidden" name="roleId" id="roleId" value="${roleId!''}"/>
            </div>
            <form id="roleCreateForm" class="form-horizontal">
                <ul class="user-role-list form-inline">
                    <li class="alert alert-info" style="list-style-type: none">
                        <span class="label label-info">角色名称:</span>
                        <span id="name">${name!''}</span>
                    </li>
                    <li class="alert alert-info" style="list-style-type: none">
                        <span class="label label-info">角色编码:</span>
                        <span id="code">${code!''}</span>
                    </li>
                    <li class="alert alert-info" style="list-style-type: none">
                        <span class="label label-info">角色说明:</span>
                        <span id="comment">${comment!''}</span>
                    </li>
                    <li class="alert alert-info" style="list-style-type: none">
                        <span class="label label-info">应用名称:</span>
                        <span >${appName!''}</span>
                    </li>
                </ul>
            </form>
        </div>

    </div>

    <div class="permissionList user-column">
        <ul class="nav nav-tabs">
            <li class="active">
                <a href="javascript:void(0)">选择权限</a>
            </li>
        </ul>
        <div class="role-save-btn">
            <input type="button" class="btn btn-primary" value="保存" onclick="savePermissions()">
            <input type="button" class="btn btn-primary" value="全部收起" onclick="colltree()">
            <input type="button" class="btn btn-primary" value="全部展开" onclick="expantree()">
        </div>
        <div class="zTreeDemoBackground left">
            <ul id="treePermissions" class="ztree"
                style="width: 500px;height: 600px;background:#ffffff;border: none;overflow:auto"></ul>
        </div>
    </div>
</div>