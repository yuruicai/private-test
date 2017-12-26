${callback}([
<#list cities as city>
{"id": "${city.id}", "name": "${city.name?js_string}"}<#if city_index+1 < cities?size>,</#if>
</#list>
]);