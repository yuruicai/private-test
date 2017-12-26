<style type="text/css">
    .margin-type {
        margin-right: 10px;
    }
    #selectedRoles {
        height:10px;
        margin: 30px 0 20px 10px;
        color: #E20D0D;
        font-weight: 900;
    }
</style>
<link rel="stylesheet" type="text/css" href="/static/css/default.css">
<link rel="stylesheet" href="/static/css/combo.select.css">
<script type="text/javascript" src="/static/jquery.combo.select.js"></script>
<script src="/static/userPermission/userRoles.js"></script>
<#--<div id="user-singleconf" id="pre-mask-loading">-->
    <#--<div class="single-userconf user-column">-->
        <#--<div id="breadcrumbs" class="breadcrumbs">-->
            <#--<ul class="breadcrumb">-->
                <#--<li class="active">需要配置的用户</li>-->
            <#--</ul><!--.breadcrumb&ndash;&gt;-->
        <#--</div><!--#breadcrumbs&ndash;&gt;-->
<div id="role-permissions-conf">
    <div class="single-roleconf span4 user-column">
        <ul style="border-bottom: 1px solid #ddd">
            <li class="active" style="list-style-type: none">
                <h4>选择需要配置的角色</h4>
            </li>
        </ul>
        <div class="user-list">
            【用户搜索】
            <select class="js-select" ></select>
            <input id="user-id" type="hidden" name="user-id" value="${userId!''}"/>
            <div id="roleList" style="display:none;margin-top: 40px;"><p>【用户拥有的角色列表】</p></div>

            <ul class="user-role-list form-inline unstyled"></ul>
        </div>
    </div><div class="single-roleconf span4 user-column" style="width: 60%;">
        <ul style="border-bottom: 1px solid #ddd">
        <li class="active" style="list-style-type: none">
            <h4>选择的角色</h4>
        </li>
        </ul>
        <div id="selectedRoles" style="height:auto !important;"></div>
        <div class="role-save-btn">
            <input type="button" class="btn btn-primary" value="保存" onclick="saveUserRoles()" />
        </div>
        <ul class="nav nav-tabs">
            <li class="active">
                <a href="javascript:void('0');">新增关联角色</a>
            </li>
        </ul>
        <div class="role-search">
            搜索：<input class="f-text" type="text" style="height:30px" value="" onkeyup="serachRoles()" name="role-name" id="role-name" placeholder="角色名"/>
        </div>
        <!-- 当前用户可配置角色列表 -->
        <table class="table table-striped table-bordered table-hover form-inline" id="roleTable" width="200px">
            <thead>
            <tr>
                <th width="50px">
                    <label class="checkbox">
                        <input type="checkbox" class="checkbox-all" value="1"  onchange="selectAll()">选择
                    </label>
                </th>
                <th width="150px">角色名</th>
            </tr></thead>
            <tbody>
        <#list roles as role>
            <#if role.code!='UPM_APPM'>
            <tr class="isMatched role-item-tr">
                <td>
                    <label class="checkbox">
                        <input id="checkbox_${role.id!''}" class="checkbox" type="checkbox" name="checkbox" value="${role.id!''}" data-id="${role.id!''}" data-name="${role.name!''}" onclick="selectRole(this)"/>
                    </label>
                </td>
                <td id="role-item-name" class="row-trigger">${role.name!''}</td>
            </tr>
            </#if>

        </#list>
            </tbody>
        </table>
    </div>
</div>
<script>

</script>
