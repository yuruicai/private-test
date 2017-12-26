<style type="text/css">
    #user-box th {width: 100px;text-align:right;}
    #user-box td {position:relative;left: 10px;}
</style>
<div id="user-list">
    <div class="box span3">
    <div class="box-header">个人信息</div>
    <div class="box-content">
        <table id="user-box">
            <tr>
                <th>姓名：</th>
                <td>${user.name}</td>
            </tr>
            <tr>
                <th>员工号：</th>
                <td>${user.code}</td>
            </tr>
            <tr>
                <th>登录名：</th>
                <td>${user.loginName}</td>
            </tr>
            <tr>
                <th>公司邮箱：</th>
                <td>${user.email}</td>
            </tr>
            <tr>
                <th>手机号：</th>
                <td>${user.decryptMobile}</td>
            </tr>
            <#--<tr>-->
                <#--<th>身份证号：</th>-->
                <#--<td>${user.idCode!''}</td>-->
            <#--</tr>-->
        </table>
    </div>
</div>
    </div>

<script>
    $(function() {
        $('.span3').css({"width":"100%","height":"500px"});
    });
</script>

