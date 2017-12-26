${callback}([
<#list categories as category>
{"id": "${category.id}", "name": "${category.name?js_string}", "parentId": "${category.parentId}"}<#if category_index+1 < categories?size>,</#if>
</#list>
]);