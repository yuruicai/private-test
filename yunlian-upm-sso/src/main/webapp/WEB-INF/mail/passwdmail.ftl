FromMail:System@fengjr.com
FromName:统一权限管理系统
To:${(receiver)!''}
Subject:${cityName!""}新员工<#if waimai??>${waimai!""}</#if>：${name!""} 登录名：${login!""}

<p style="line-height: 200%">

    组织节点：${fullOrgName!""}<br/>
    行政单位：${cityName!""} <br/>
    职位：${title!""} <br/>
    新员工：${name!""} <br/>
    登录名：${login!""} <br/>
    MIS UID：<span style="color: red;">${id!""}</span> <br/>
    工号：${code!""} <br/>
<#if passwd?? >
    密码：${passwd!""} <br/>
</#if>

    开发负责人：UPM &lt;upm@fengjr.com&gt;<br />
    内容负责人：MIS &lt;mis@fengjr.com&gt;<br />

</p>