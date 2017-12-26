<#setting  number_format="#">
<#if __menus??>

<ul id="upm-menus" class="nav">
    <#assign currentParentMenuId = ((_currentParentMenuId!""))/>
    <#assign currentMenuId = ((_currentMenuId!""))/>
    <#list __menus__ as menu>
        <li class="dropdown-collapse">
            <#if menu.menus ?? && menu.menus?size &gt; 0>
                <a class="dropdown-collapse" href="javascript:void(0)" onclick="clickFirstMenu(this)"
                   data-toggle="dropdown-collapse">
                    <i class="fa fa-bar-chart-o"></i>
                    <span>${menu.title}</span>
                    <i class="fa fa-angle-<#if currentParentMenuId == menu.id>up<#else>down</#if> angle-down"></i>
                </a>
            <#else>\
                <a id="menu-${menu.id}" class="menu-list"
                   href="javascript:void(0)"
                   data-url="${menu.url}" data-appId="${appId!''}"><span>${menu.title}</span></a>
            </#if>
            <ul class="nav <#if currentParentMenuId == menu.id><#else>in</#if> ${currentParentMenuId} ${menu.id}">
            <#if menu.menus ?? && menu.menus?size &gt; 0>
                <#list menu.menus as subMenu>
                    <li>
                        <a id="menu-${subMenu.id}"
                           class="menu-list"
                           href="javascript:void(0)"
                           data-url="${subMenu.url}" onclick="selectMenu(this)"
                           data-appId="${appId!''}"><span>${subMenu.title}</span></a>
                    </li>
                </#list>
             </#if>
            </ul>
        </li>
    </#list>

</ul>
</#if>