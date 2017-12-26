<#setting  number_format="#">

<#if __menus??>
<#assign
    currentParentMenuId = (_currentParentMenuId!0)
    currentMenuId = (_currentMenuId!0)
/>
<ul class="nav">
    <#list __menus__ as menu>
        <#if currentParentMenuId == menu.id>
        <#assign menuCls = ''>
        <#else>
        <#assign menuCls = 'in'>
        </#if>
        <li>
            <#if menu.menus ?? && menu.menus?size gt 0>
            <a href="javascript:void(0)"
                ><i class="fa fa-<#if menu.title='关键指标'>bar-chart-o<#else>star-half-o</#if>"
                    ></i><span>${menu.title}</span><i
                    class="fa fa-angle-<#if currentParentMenuId == menu.id>up<#else>down</#if> angle-down"></i></a>
            <ul class="nav ${menuCls}">
                <#list menu.menus as subMenu>
                    <#if currentMenuId == subMenu.id>
                    <#assign subMenuCls = ''>
                    <#else>
                    <#assign subMenuCls = 'in'>
                    </#if>
                    <li>
                        <a id="menu-${subMenu.id}" class="${subMenuCls}" href="${subMenu.url}"
                            ><span>${subMenu.title}</span></a>
                    </li>
                </#list>
            </ul>
            <#else>
            <a id="menu-${menu.id}" class="${subMenuCls}" href="${menu.url}"
                ><span>${menu.title}</span></a>
            </#if>
        </li>
    </#list>
</ul>
</#if>

