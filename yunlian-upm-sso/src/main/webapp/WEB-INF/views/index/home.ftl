<style>
    .welcome {
        margin:0 auto;
        font-size: 30px;
        color: #3361A4;
        text-align:center; 	/* 文字等内容居中 */
    }
    .famoussite-mainlink{
        color: rgb(51, 101, 151);
        cursor: auto;
        display: inline;
        font-family: 'Microsoft YaHei', 微软雅黑;
        font-size: 18px;
        font-style: normal;
        font-variant: normal;
        font-weight: normal;
        height: auto;
        line-height: 30px;
        outline-color: rgb(51, 101, 151);
        outline-style: none;
        outline-width: 0px;
        padding-bottom: 1px;
        padding-left: 24px;
        padding-right: 4px;
        padding-top: 1px;
        text-decoration: none;
        width: auto;
        zoom: 1;
    }
    .linkTable{
        border:none;
        margin:none;
        line-height:20px;
        padding:0px;
        margin:0 auto;
    }
</style>
<div class="row">
    <div class="welcome">欢迎登录SSO系统!</div>
    <hr><br>
    <div  class="divCenter">
    <#if applications??>
        <table id="linkTable"class="linkTable">
            <#list applications as item>
                <#if ((item_index+1)%6==1)>
                <tr>
                </#if>
                <td>
                    <span><a  id="menu-${item.id}"  href="${item.url}" class="famoussite-mainlink">${item.name}</a></span>
                </td>
                <#if ((item_index+2)%6==1)>
                </tr>
                </#if>
            </#list>
        </table>
    </#if>
    </div>
</div>
