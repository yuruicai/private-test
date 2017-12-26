var zNodes;
var setting = {
    view: {
        addHoverDom: addHoverDom,
        removeHoverDom: removeHoverDom,
        selectedMulti: false
    },
    edit: {
        enable: true,
        editNameSelectAll: true,
        showRemoveBtn: showRemoveBtn,
        showRenameBtn: showRenameBtn
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeDrag: beforeDrag,
        beforeDrop: beforeDrop,
        beforeRemove: beforeRemove,
        onClick: zTreeOnClick
    }
};
var dragId;
var log, className = "dark";
function zTreeOnClick(event, treeId, treeNode) {
    initEditPage();
    $("#title").val(treeNode.name);
    $("#url").val(treeNode.url);
    $("#pId").val(treeNode.pId);
    $("#id").val(treeNode.id);
    $("#permission").val(treeNode.permission);
    $("#typetree").val(treeNode.isParent);
    var c =$("input[name='isShow']");
    if(c[0].value==treeNode.isShow+''){ c[0].checked=true;}
    if(c[1].value==treeNode.isShow+''){ c[1].checked=true;;}
    var showType =$("input[name='showType']");
    if(showType[0].value==treeNode.showType+''){ showType[0].checked=true;}
    if(showType[1].value==treeNode.showType+''){ showType[1].checked=true;;}
}
;
function beforeDrag(treeId, treeNodes) {
    for (var i = 0, l = treeNodes.length; i < l; i++) {
        dragId = treeNodes[i].pId;
        if (treeNodes[i].drag === false) {
            return false;
        }
    }
    return true;
}
function beforeDrop(treeId, treeNodes, targetNode, moveType) {
    //  alert(treeNodes[0].id+targetNode.id+moveType)
    if (moveType == "inner") {
        return false
    }
    if (targetNode.pId == dragId) {
        var data = {id: treeNodes[0].id, targetId: targetNode.id, moveType: moveType};
        var confirmVal = false;
        $.ajax({
            async: false,
            type: "post",
            data: data,
            url: "/menu/moveMenu/ ",
            success: function (json) {
                if (json.status == "200") {
                    confirmVal = true;
                } else {
                }
            },
            error: function () {
            }
        });
        return confirmVal;
    } else {
        alertError("只能进行同级排序！");
        return false;
    }
}
function beforeRemove(treeId, treeNode) {
    if(treeNode.id==-1){
        layer.confirm("确定删除" +  treeNode.name  + "吗？", {icon: 3, title:'提示'}, function(){
            alertSuccess("删除成功",reloadMenu);
        });
        return false;
    }
    className = (className === "dark" ? "" : "dark");
    showLog("[ " + getTime() + " beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    var data = {id: treeNode.id}
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.selectNode(treeNode);
    layer.confirm("确定删除" +  treeNode.name  + "吗？", {icon: 3, title:'提示'}, function(){
        $.ajax({
            async: false,
            type: "post",
            data: data,
            url: "/menu/delete?date=" + getTime,
            success: function (json) {
                if (json == "success") {
                    alertSuccess("删除成功",reloadMenu)
                } else {
                    alertError('操作失败');
                }
            },
            error: function () {
                alertError('系统繁忙，请稍后重试!');
            }
        });
    });
    return false;
}
var reloadMenu =function(index){
    $.ajax({
        async: false,
        type: "get",
        url: "/menu/getMenu.ajax",
        success: function (json) {
            zNodes = JSON.parse(json);
        },
        error: function () {
        }
    });
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.expandAll(true);
    initEditPage();
    layer.close(index);
}
function onRemove(e, treeId, treeNode) {
    showLog("[ " + getTime() + " onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
}
function showRemoveBtn(treeId, treeNode) {
    if(treeNode.pId==null || treeNode.pId==""){
        treeNode.isParent=true;
    }
    return treeNode.isParent == true ? false : true;
}
function showRenameBtn(treeId, treeNode) {
    return false;
}
function showLog(str) {
    if (!log) log = $("#log");
    log.append("<li class='" + className + "'>" + str + "</li>");
    if (log.children("li").length > 8) {
        log.get(0).removeChild(log.children("li")[0]);
    }
}
function getTime() {
    var now = new Date(),
        h = now.getHours(),
        m = now.getMinutes(),
        s = now.getSeconds(),
        ms = now.getMilliseconds();
    return (h + ":" + m + ":" + s + " " + ms);
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_" + treeNode.id).length > 0) return;
    //   if(!treeNode.isParent)return;
    var addStr = '<span class="button add" style=" margin-right: 7px;" id="addBtn_' + treeNode.id
        + '" title="add node" onfocus="this.blur();"></span>';
    sObj.after(addStr);
    var btn = $("#addBtn_" + treeNode.id);
    if (btn) btn.bind("click", function () {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo");
        zTree.addNodes(treeNode, {id: (-1), pId: treeNode.id, name: "new node" + (newCount++)});
        return false;
    });
}
;
function removeHoverDom(treeId, treeNode) {
    $("#addBtn_" + treeNode.id).unbind().remove();
}
;

function save() {

    if ($.trim($("#pId").val()) == "") {
        alertError("请选择父节点")
        return;
    }
    if ($.trim($("#title").val()) == "") {
        alertError("请填写菜单名称")
        return;
    }
    if($("#title").val().length>100){
        alertError("菜单名称长度不能大于100");
        return;
    }
    if($("#url").val().length>100){
        alertError("菜单URL长度不能大于100");
        return;
    }
    var showType=$('input:radio[name="showType"]:checked').val();
    if(showType==null){
        alertError("请选择显示类型")
        return;
    }
    var isShow=$('input:radio[name="isShow"]:checked').val();
    if(isShow==null){
        alertError("请选择是否显示")
        return;
    }
    if($("#permission").val().length>255){
        alertError("权限信息长度不能大于255");
        return;
    }
    var data = {id: $("#id").val(), pId: $("#pId").val(), url: $("#url").val(), title: $("#title").val(),permission:$("#permission").val(),isShow:$("input[name='isShow']:checked").val(),showType:$("input[name='showType']:checked").val()};
    $.ajax({
        async: false,
        type: "post",
        data: data,
        url: "/menu/save?date=" + getTime,
        success: function (json) {
            if (json == "success") {
                alertSuccess("保存成功")
                confirmVal = true;
                $.ajax({
                    async: false,
                    type: "get",
                    url: "/menu/getMenu.ajax",
                    success: function (json) {
                        zNodes = JSON.parse(json);
                    },
                    error: function () {
                    }
                });
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                zTree.expandAll(true);
                initEditPage();
            } else {
                alertError('操作失败');
            }
        },
        error: function () {
            alertError('系统繁忙，请稍后重试！');
        }
    });
}

function beforeClick(treeId, treeNode) {
    if (treeNode.isParent) {
        return true;
    } else {
        alertError("这个 Demo 是用来测试 展开 / 折叠 的...\n\n去点击父节点吧... ");
        return false;
    }
}
$(document).ready(function () {
    initTree();
});
function initTree(){
    $.ajax({
        async: false,
        type: "get",
        url: "/menu/getMenu.ajax",
        success: function (json) {
            zNodes = JSON.parse(json);
        },
        error: function () {
        }
    });
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.expandAll(true);
}
function colltree() {
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.expandAll(false);
}
function expantree() {
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.expandAll(true);
}
//-->
function initEditPage(){
    $("#title").val("");
    $("#url").val("");
    $("#pId").val("");
    $("#id").val("");
    $("#typetree").val("");
    $("#permission").val("");
    var c =$("input[name='isShow']");
    c[0].checked=true;
    c[1].checked=false;
    var showType =$("input[name='showType']");
    showType[0].checked=true;
    showType[1].checked=false;
}