<style type="text/css">
    .ztree li span.button.add {
        margin-left: 2px;
        margin-right: -1px;
        background-position: -144px 0;
        vertical-align: top;
        *vertical-align: middle;
        text-decoration: none

    }
</style>

<div id="menu-operation-list" id="pre-mask-loading">
    <div class="menulist user-column span5">
        <a id="hide-all" class="btn btn-primary" onclick="colltree()" href="javascript:void(0);">全部收起</a>
        <a id="show-all" class="btn btn-primary" onclick="expantree()" href="javascript:void(0);">全部展开</a>
        <ul class="nav nav-tabs">
            <li class="active">
                <a href="javascript:void('0');">菜单列表</a>
            </li>
        </ul>
        <ul>
            <div class="content_wrap">
                <div class="zTreeDemoBackground left">
                    <ul id="treeDemo" class="ztree"
                        style="width: 500px;height: 600px; background:#ffffff;border: none;overflow:auto"></ul>
                </div>
            </div>

        </ul>
    </div>

    <div class="operationlist user-column span7">
        <ul class="nav nav-tabs">
            <li class="active">
                <a href="javascript:void('0');">编辑菜单</a>
            </li>
        </ul>
        <div id="menuInfo" style="margin:5px;">
            <form id="menu-form" class="form-horizontal" action="save" method="post" class="form-inline" accept-charset="utf-8">
                <div class="control-group">
                    <label class="control-label" ><i class="required"></i>菜单名称：</label>
                    <div class="controls">
                        <input type="text"  name="title" id="title" placeholder="菜单名称" value=""/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" >菜单URL：</label>
                    <div class="controls">
                        <input type="text" name="url" placeholder="菜单URL" id="url" value="">
                    </div>
                    <input type="hidden" name="id" id="id" value=""/>
                    <input type="hidden" name="typetree" id="typetree" value=""/>
                    <input type="hidden" name="pId" id="pId" value=""/>
                </div>
                <div class="control-group">
                    <label class="control-label" ><i class="required"></i>显示类型：</label>
                    <div class="controls">
                        <input type="radio" name="showType" id="showType"  checked value="1"/>在原页面打开&nbsp;&nbsp;&nbsp;
                        <input type="radio" name="showType" id="showType" value="2"/>在新页面打开
                    </div>
                </div>
                <div class="control-group" >
                    <label class="control-label"> <i class="required"></i>是否显示：</label>
                    <div class="controls">
                        <input type="radio" name="isShow" id="isShow"  checked value="1"/>是&nbsp;&nbsp;&nbsp;
                        <input type="radio" name="isShow" id="isShow" value="0"/>否
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">权限信息：</label>
                    <div class="controls">
                        <input type="text" name="permission" placeholder="权限信息" id="permission" value=""/>
                    </div>
                </div>
                <div>
                    <input type="hidden" name="id" value=""/>
                    <input type="hidden" name="parentId" value=""/>
                </div>
                <div class="controls">
                    <input type="button" class="btn btn-primary save" value="保存修改" onclick="save()">
                </div>
            </form>
        </div>

    </div>
    <link rel="stylesheet" href="/static/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <link href="/static/css/font-awesome.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="/static/menulist/menulist.js"></script>
    <script type="text/javascript" src="/static/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="/static/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript" src="/static/jquery-ztree/3.5.12/js/jquery.ztree.exedit-3.5.js"></script>
