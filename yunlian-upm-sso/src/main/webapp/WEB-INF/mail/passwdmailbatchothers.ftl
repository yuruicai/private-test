<p style="line-height: 200%">

<table>
    <tr>
        <td>ID</td>
        <td>登录名</td>
        <td>姓名</td>
    </tr>
<#list userList as userBean>
    <tr>
        <td>${userBean.id}</td>
        <td>${userBean.loginName}</td>
        <td>${userBean.name}</td>
    </tr>
</#list>
</table>
<br/>

</p>
-------------------------------------------<br/>
开发负责人：UPM &lt;upm@fengjr.com&gt;<br/>
内容负责人：MIS &lt;mis@fengjr.com&gt;<br/>

