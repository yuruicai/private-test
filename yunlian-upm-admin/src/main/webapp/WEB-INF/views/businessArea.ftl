${callback}([
<#list businessAreaList as businessArea>
{"id": "${businessArea.id}", "name": "${businessArea.name}", "parentId":
    <#if businessArea.district == 0>
        "0"
    <#else>
        "${businessArea.district}"
    </#if>
}<#if businessArea_index+1 < businessAreaList?size>,</#if>
</#list>
]);