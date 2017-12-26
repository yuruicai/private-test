<script type="text/javascript" src="/static/js/role/roleEdit.js"></script>
<div id="sign-up">
    <h1>修改角色</h1>

    <div class="sign-box shadow-box">
        <form id="roleCreateForm" action="save" class="form-horizontal" method="post">

        <#if role.id?? >
            <input id="roleId" name="id" type="hidden" size="50" value="${role.id!""}"/>
        </#if>

            <div class="control-group">
                <label class="control-label"><i class="required"></i>角色名</label>

                <div class="controls">
                    <input id="name" name="name" type="text" maxlength="30" data-rules="required" value="${role.name!""}"/>
                    <span class="help-inline">推荐按照部门+岗位+用途（可选）来命名角色</span>
                </div>
            </div>
             <input id="code" name="code" type="hidden" size="50" data-rules="required" value="${role.code!""}"/>
            <div class="control-group">
                <label class="control-label">角色说明</label>

                <div class="controls">
                    <input name="comment" id ="comment" type="text"  maxlength="100" value="${role.comment!""}"/>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label">应用</label>

                <div class="controls">
                    <input type="text" class="currentAppName" name="applicationName" id="applicationName"
                           resource="${role.id!''}"
                           value="${role.applicationName!''}" readonly="true"/>
                </div>
            </div>

        <#if roleOrgVisible?? && roleOrgVisible>
            <div class="control-group">
                <label class="control-label"><i class="required"></i>所属部门：</label>

                <div class="controls">
                    <span>
                        <input class="org-list" type="text" value="${role.orgName!""}" data-rules="required"
                               autocomplete="off"/>
                        <input id="orgId" type="hidden" name="orgId" value="${role.orgId!""}" data-rules="required">
                    </span>
                </div>
            </div>
        </#if>

            <div class="control-group">
                <input type="hidden" name="isNew" value="${role.isNew!""}">
                <input type="hidden" name="roleType" value="${role.roleType!'1'}"/>
                <input type="hidden" name="status" value="${role.status!'0'}"/>
                <input type="hidden" class="currentAppId" name="applicationId" id="applicationId"
                       value="${role.applicationId!'0'}"
                       resource="${role.id!''}"
                       value="${role.applicationId!''}" readonly="true"/>

                <div class="controls">
                    <label style="line-height:40px;">
                        <input type="button" id="createRole" class="btn btn-primary" value="保存"/>
                        <a href="/role/list?applicationId=<#if role.id??>${role.applicationId!''}<#else>${appId!''}</#if>"
                           class="btn btn-primary">角色列表</a>
                        <a href="javascript:void(0);" id="permissionBtn"
                           class="btn btn-primary">角色配置权限</a>
                    </label>
                </div>
            </div>
        </form>
    </div>
</div>
