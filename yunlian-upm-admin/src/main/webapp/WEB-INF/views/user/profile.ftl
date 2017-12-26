<style>
    #user-box{
        line-height:30px;
    }
    #user-box th {
        width: 100px;
        float: right;
        text-align: right;
        margin-right: 10px;
    }
</style>

<div id="user-list">
    <div class="box span3" style="width: 100%;height: 500px;">
    <div class="box-header">个人信息</div>
    <div class="box-content">
        <table id="user-box">
            <tr>
                <th>姓名：</th>
                <td>${user.name!''}</td>
            </tr>
            <tr>
                <th>员工号：</th>
                <td>${user.code!''}</td>
            </tr>
            <tr>
                <th>登录名：</th>
                <td>${user.loginName!''}</td>
            </tr>
            <tr>
                <th>公司邮箱：</th>
                <td>${user.email!''}</td>
            </tr>
            <tr>
                <th>密码：</th>
                <td><a href="/user/modPassword">修改密码</a></td>
            </tr>
            <tr>
                <th>手机号：</th>
                <td>${user.decryptMobile!''}</td>
            </tr>

        </table>
    </div>
</div>
    </div>
