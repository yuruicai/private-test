
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


<#macro list arr space="">
<#compress>
<#if arr?? && arr?is_sequence>
[
    <#list arr as item>
    <#if item?is_string>
    ${item}
    <#elseif item?is_hash>
    {
    <#list item?keys as key>
        ${key}: "${item[key]}"<#if key_has_next>,</#if>
    </#list>
    }</#if><#if item_has_next>,
    </#if></#list>
]
</#if>
</#compress>
</#macro>

<#function hash obj space="">
<#if obj?? && obj?is_hash_ex>
    <#local
        origSpace = space
        out = "{",
        keys = obj?keys,
        space = origSpace + "    "
    >
    <#list keys as key>
        <#local val = obj[key]>
        <#if val?is_hash_ex>
            <#local val = hash(val, space)>
        <#elseif val?is_sequence>
            <#-- @TODO -->
        <#elseif val?is_string>
            <#local val = '"' + val?replace('"', '\\"') + '"'>
        </#if>
        <#local out = out + "\n" + space + key + ":" + val>
        <#if key_has_next>
            <#local out = out + ",">
        </#if>
    </#list>
    <#local out = out + "\n" + origSpace + "}">
</#if>
<#return out>
</#function>

