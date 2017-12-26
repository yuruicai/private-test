<div class="ten columns">

<#if ipTimesLimitInfo ??>
    <h3>IP次数限制(过期时间：${ipTimesLimitExpireTime?datetime}) </h3>
    <form method="post" action="/admin/timeslimit/save">
        <table id="audit-loginIp-table" class="table table-striped table-hover">
            <input type="hidden" name="type" value="1">
            <input type="hidden" name="ip" value="${ip!''}">
            <tbody>
            <tr>
                <td>成功登录次数</td>
                <td><input name="successLoginTimes" value="${ipTimesLimitInfo.successLoginTimes}"></td>
            </tr>
            <tr>
                <td>失败登录次数</td>
                <td><input name="failLoginTimes"  value="${ipTimesLimitInfo.failLoginTimes}"></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="提交"></td>
            </tr>
            </tbody>
        </table>
    </form>
</#if>
<#if usernameTimesLimitInfo ??>
    <h3>用户名次数限制(过期时间：${usernameTimesLimitExpireTime?datetime}) </h3>
    <form method="post" action="/admin/timeslimit/save">
        <table id="audit-loginIp-table" class="table table-striped table-hover">
            <input type="hidden" name="type" value="2">
            <input type="hidden" name="username" value="${username!''}">
            <tbody>
            <tr>
                <td>成功登录次数</td>
                <td><input name="successLoginTimes" value="${usernameTimesLimitInfo.successLoginTimes}"></td>
            </tr>
            <tr>
                <td>失败登录次数</td>
                <td><input name="failLoginTimes"  value="${usernameTimesLimitInfo.failLoginTimes}"></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" value="提交"></td>
            </tr>
            </tbody>
        </table>
    </form>
</#if>
</div>