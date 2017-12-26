<#include "/helper/includeHelper.ftl">
<#--<link rel="stylesheet" href="/static/css/font-awesome.min.css">-->
<#--<link rel="stylesheet" href="/static/css/style.css">-->
<script type="text/javascript" src="/static/js/role/roleList.js"></script>
<div id="role-list">
    <form id="roleForm" class="form-search form-inline bd-top-box" method="get" action="">
        <label>角色名</label><input type="text" name="name"
                                 value="${roleListBean.name!""}" placeholder="支持角色名模糊查询"/>
        <#--<label>角色代码</label><input type="text" name="code"-->
                                  <#--value="${roleListBean.code!""}" placeholder="支持角色代码模糊查询"/>-->
        <input type="hidden" name="applicationId"
               value="${roleListBean.applicationId!''}"/>
        <input type="submit" class="btn btn-primary" value="查找"/>

        <label><a href="/role/create" class="btn btn-primary">创建角色</a></label>
    </form>
    <table id="role-table" class="table table-striped table-bordered table-hover table-full-width table-condensed" style="table-layout:fixed;">
        <thead>
        <tr>
            <#--<th style="word-wrap:break-word;">角色代码</th>-->
            <th style="word-wrap:break-word;">角色名</th>
            <th style="word-wrap:break-word;">备注</th>
            <#--<th style="word-wrap:break-word;">所属应用</th>-->
            <th style="word-wrap:break-word;">创建时间</th>
            <th style="word-wrap:break-word;">更新时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#list list as roleBean>
        <tr>
            <input id="id${roleBean_index}" type="hidden" value="${roleBean.id!""}">
            <#--<td style="word-wrap:break-word;">${roleBean.code!""}</td>-->
            <td style="word-wrap:break-word;">${roleBean.name!""}</td>
            <td style="word-wrap:break-word;">${roleBean.comment!""}</td>
            <#--<td style="word-wrap:break-word;">${roleBean.applicationName!''}</td>-->
            <td style="word-wrap:break-word;">${roleBean.createTime?string("yyyy-MM-dd HH:mm:ss")!""}</td>
            <td style="word-wrap:break-word;">${roleBean.updateTime?string("yyyy-MM-dd HH:mm:ss")!""}</td>
            <td>
                <a class="btn btn-small btn-primary" href='/role/permission?curRoleId=${roleBean.id!""}&appId=${roleBean.applicationId!""}'>角色配置权限</a>
                <a class="btn btn-small btn-primary" href="/userRole/listHaveRole?roleId=${roleBean.id!""}">配置角色的用户</a>
                <#if roleBean.code??&&roleBean.code=='UPM_SUPER'>
                    <a class="editrole btn btn-small btn-warning" onclick="checkSuperRole()">修改</a>
                <#else>
                    <a class='editrole btn btn-small btn-warning' href='edit?id=${roleBean.id!""}'>修改</a>
                </#if>
                <a class="delete btn btn-small btn-danger"  data-href="/role/delete" data-id="${roleBean.id!0}"
                   data-name="${roleBean.name!""}"
                   href="javascript:void(0)">删除</a>
            </td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
<script type="text/javascript" src="/static/datatables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/static/datatables/jquery.dataTables.bootstrap.js"></script>
