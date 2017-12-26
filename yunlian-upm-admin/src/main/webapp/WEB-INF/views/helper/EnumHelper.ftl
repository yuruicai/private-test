<#macro ENUM_TO_OBJ arr>
    <#compress>
    <#if arr??>
    {
    <#list arr.values() as obj>
        <@ENUM_ITEM obj /><#if obj_has_next>,</#if>
    </#list>
    }
    </#if>
    </#compress>
</#macro>

<#macro ENUM_ITEM obj>
    <#compress>
    ${obj.getIndex()} : { name: '${obj.toString()?lower_case}', text: '${obj.getName()}' }
    </#compress>
</#macro>
