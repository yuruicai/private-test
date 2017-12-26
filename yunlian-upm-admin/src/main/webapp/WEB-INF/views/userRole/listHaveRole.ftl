<#include "/helper/includeHelper.ftl">
<div id="role-user-list">
    <br/>

    <div style="text-align: center;">角色名：${role.name!""}</div>
    <hr/>
    <table id="role-user-table" class="table table-striped table-hover">
        <thead>
        <tr>
            <th>用户名</th>
            <th>登陆名</th>
            <#--<th>部门</th>-->
            <#--<th>所在组织</th>-->
            <#--<th>岗位</th>-->
            <th>邮箱</th>
            <#--<th>用户类型</th>-->
        </tr>
        </thead>
        <tbody>
        <#list userList as userBean>
        <tr>
            <td>${userBean.name!""}</td>
            <td>${userBean.loginName!""}</td>
            <#--<td>${userBean.departmentName!""}</td>-->
            <#--<td>${userBean.orgName!""}</td>-->
            <#--<td>${userBean.posName!""}</td>-->
            <td>${userBean.email!""}</td>
            <#--<td>${userBean.typeName!''}</td>-->
        </tr>
        </#list>
        </tbody>
    </table>
    <script type="text/javascript" src="/static/datatables/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/static/datatables/jquery.dataTables.bootstrap.js"></script>
    <script type="text/javascript">

        $('#role-user-table').dataTable({
            "aaSorting": [[10, "desc"]],
            "bFilter": false,
            "aLengthMenu": [[100, 50, 20], [100, 50, 20]],
            "sPaginationType": "four_button", //分页，首页、上一页、下一页、尾页
            "iDisplayLength": 20,
            "aoColumnDefs": [
                {
                    sDefaultContent: '',
                    aTargets: ['_all']
                },
                {
                    "bSortable": false,
                    "aTargets": ['_all']
                }
            ],
            "oLanguage": {
                "sLengthMenu": "显示 _MENU_ 条记录",
                "sInfo": "显示第 _START_ - _END_ 条记录，共 _TOTAL_ 条",
                "sSearch": "搜索:",
                "sInfoEmpty": "没有符合条件的记录",
                "sProcessing": "正在加载中...",
                "sZeroRecords": "没有符合条件的记录",
                "searching": false,
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": " 上页 ",
                    "sNext": " 下页 ",
                    "sLast": " 尾页 "
                }
            },
        });
    </script>
</div>
