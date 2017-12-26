<link rel="stylesheet" href="/static/css/combo.select.css">
<script type="text/javascript" src="/static/jquery.combo.select.js"></script>
<style>
    .labelContent {
        float: left;
        width: 100px;
        padding-top: 5px;
    }
</style>
<div id="app-manager-list">
    <br/>
    <div class="labelContent">应用名称：</div><div style="font-weight: 900;color:red;font-size:25px;">${applicationBean.name!""}</div>
    <br/>
    <div><span class="labelContent">Appkey：</span>${applicationBean.appKey!""}</div>
    <br/>
    <div >
        <div class="labelContent">应用默认角色：</div>
        <div style="float: left"><select class="js-select-roles"　><select></div>
        <div style="float: left">&nbsp;&nbsp;<a id="changeRole" class="btn btn-primary"><#if applicationBean.roleId??>修改默认角色<#else>添加默认角色</#if></a></div>
        <input type="hidden" id="roleId" value="<#if applicationBean.roleId?? && applicationBean.roleId ??>${applicationBean.roleId!""}</#if>"/>
    </div>
    <input id="appId" value="${applicationBean.id!''}" type="hidden"/>
    <div style="clear:both;">
        <div class="labelContent">应用管理员：</div>
        <div style="float: left"><select class="js-select-user" ><select></div>
        <div style="float: left">&nbsp;&nbsp;<a id="addAppManager" class="btn btn-primary">添加</a></div>
        <input type="hidden" id="userId" name="userId"/>
    </div>

    <div style="clear:both;"></div>
    <table id="app-manager-table" class="table table-striped table-hover">
        <thead>
        <tr>
            <th data-params="{name:'login'}" style="width: 30" id="login">用户名</th>
            <th data-params="{name:'name'}"  style="width: 30" id="name">姓名</th>
            <th style="width: 30">操作</th>
<#--            <th data-params="{name:'contextAttrType'}" style="width: 30">应用ID类型</th>
            <th data-params="{name:'appkey'}" style="width: 30">应用ID类型值</th>-->
        </tr>
        </thead>
        <tbody>
        <#list managers as manager>
        <tr>
            <td>${manager.login!""}</td>
            <td>${manager.name!""}</td>
            <td><a class="deleteAppManager" data-href="/app/deleteAppManager"  data-id="${manager.id!""}" href="javascript:void(0)">删除</a></td>
        </tr>
        </#list>
        </tbody>
    </table>

</div>

<script src="/static/datatables/jquery.dataTables.min.js"></script>
<script src="/static/datatables/jquery.dataTables.bootstrap.js"></script>
<script src="/static/applicationmanagerlist/managerlist.js" ></script>





