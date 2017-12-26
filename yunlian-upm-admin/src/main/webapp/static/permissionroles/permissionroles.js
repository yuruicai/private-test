var allChoosedIds = [];
var appId = $.cookie('appId');

//角色目前已经拥有的角色
var havedChooseRoles = null;

//当前选择的角色
var currentChooseRoles = [];

//所有权限
var allPermissions = null;

var checkedAll = false;

//当前权限信息
var currentPermissionId = null;
var currentPermissionName = null;
$(document).ready(function () {
    $('#selectedRoles').empty();
    checkedAll = false;
    //获取所有角色信息
    getAllPermissions();

    currentPermissionId = $('input[name="permissionId"]').val();
    currentPermissionName = $('#permissionSearch').val();

    ////根据权限ID获取角色信息
    getHasChooseRoles(currentPermissionId);
    //查询角色下的权限
    $('.js-select').change(function (e, v) {
        var permissionId = e.target.value;
        var permissionName = $(e.target).find("option:selected").text();
        $('.permission-role-list').empty();
        var isExistPermission = false;
        $.each(allPermissions, function (index, item) {
            if (item.name == permissionName) {
                currentPermissionId = item.id;
                currentPermissionName = item.name;
                $('#permissionId').val(item.id);
                getHasChooseRoles(currentPermissionId);
                isExistPermission = true;
                return false;
            }
        });

        if (!isExistPermission) {
            alertError('权限不存在');
            $('#permissionId').val(permissionId);
            $.each($('.js-select option'), function (index, item) {
                if ($(item).val() == currentPermissionId) {
                    $(item).attr('selected', true);
                    getHasChooseRoles(currentPermissionId);
                }
            });
        }
    });
});
//获取所有权限信息
function getAllPermissions() {
    $.ajax({
        url: "/permissionRole/getPermissions",
        error: function () {
            alertError('获取权限出现错误');
        },
        success: function (res) {
            allPermissions = eval(res.data);
            addAllpermissionOption(allPermissions);
        }
    });
}
function addAllpermissionOption(allPermissions) {
    var option;
    $('.js-select').append(' <option value="">请输入权限名称</option>');
    $.each(allPermissions, function (index, item) {
        if (currentPermissionId == item.id) {
            var option = '<option  selected="selected" value="' + item.id + '">' + item.name + '</option>';
        } else {
            option = '<option value="' + item.id + '">' + item.name + '</option>';
        }
        $(".js-select").append(option);
    });
    $('.js-select').comboSelect()
}

function getHasChooseRoles(permissionId) {
    $.ajax({
        url: "/permissionRole/roleConf",
        type: 'POST',
        data: {permissionId: permissionId},
        error: function () {
            alertError('获取当前角色下的权限出现错误');
        },
        success: function (res) {
            havedChooseRoles = eval(res.data);
            //渲染左侧角色下已有的角色
            initLeftTableRole(havedChooseRoles);
        }
    });
}

//渲染左侧角色下已有的权限
function initLeftTableRole(chooseRoles) {
    if (chooseRoles == undefined || chooseRoles == null || chooseRoles.length == 0) {
        return;
    }
    $('.permission-role-list').empty();
    $.each(chooseRoles, function (index, item) {
        /*var permission1 = '<li class="alert alert-info">' +
         '<span class="label label-info">' + item.roleName + '</span>&nbsp;' +
         '<button  class="context-del" data-rolename="' + item.roleName + '" data-id ="' + item.id + '" onclick="deletePermission(this)">删除</button>' +
         '</li>';*/
        var permission = '' +
            '<li class="alert alert-info">' +
            '    <span class="label label-info">' + item.roleName + '</span>' +
            '    <a href="javascript:void(0);" class="context-del" data-rolename="' + item.roleName + '" data-id="' + item.id + '" onclick="deleteRole(this)">删除</a>' +
            '</li>';
        $('.permission-role-list').append($(permission));
    });
}
function deleteRole(role) {
    var roleId = $(role).data("id");
    var rolename=$(role).data("rolename");
    layer.confirm("确定删除" + rolename + "角色吗？", {icon: 3, title:'提示'}, function(){
        $.ajax({
            url: "/permissionRole/save",
            type: 'post',
            data: JSON.stringify({
                permissionIds: [currentPermissionId],
                roleConfBeans: [{
                    id: roleId,
                    status: 128,
                    typeContext: null
                }]
            }),
            headers: {
                'Content-Type': 'application/json',
            },
            error: function () {
                alertError('删除失败');
            },
            success: function (data) {
                alertSuccess('删除成功');
                $(role).parent().remove();
                getHasChooseRoles(currentPermissionId);
            }
        });
    })
}
//单击选中
function selectRole(role) {
    var isChecked = $(role)[0].checked;
    if (isChecked == true) {
        var roleId = $(role).data('id');
        var isExist = false;
        $.each(havedChooseRoles, function (index, item) {
            if (item.id == roleId) {
                isExist = true;
                return false;
            }
        });
        if (isExist) {
            $(role).attr('checked', false);
            alertError('该权限下已含有此角色,不可重复添加');
            return;
        }
        var selectStr = '<span id ="' + roleId + '" data-id="' + roleId + '">' + $(role).data('name') + '<a data-id="' + roleId + '" onClick="deleteSelectedRole(this)" class="cancelSelectedRole" href="javascript:void(0);">取消</a></span>';
        $('#selectedRoles').append($(selectStr));
    } else {
        ($('#selectedRoles').find("span[id ='" + $(role).data('id') + "']")).remove();
    }
}
//删除页面上已选择的权限
function deleteSelectedRole(role) {
    $(role).parent().remove();
    var roleId = $(role).data('id');
    var targetId = '#checkbox_' + roleId;
    if ($(targetId) != undefined && $(targetId).length > 0) {
        $(targetId).attr('checked', false);
    }
}
//全选按钮
function selectAll() {
    var checkboxs = $("#roleTable tbody tr").find("input[type='checkbox']");
    $('#selectedRoles').empty();
    for (var i = 0; i < checkboxs.length; i++) {
        var e = checkboxs[i];
        var roleId = $(e).data('id');
        if (roleId == undefined || roleId == null || roleId.length == 0) {
            continue;
        }
        if (checkedAll) {
            e.checked = false;
            $('#selectedRoles').empty();
        } else {
            var isExist = false;
            $.each(havedChooseRoles, function (index, item) {
                if (item.id == roleId) {
                    isExist = true;
                    return false;
                }
            });

            if (isExist) {
                continue;
            }

            e.checked = true;

            var selectStr = '<span class =" margin-type" id ="' + roleId + '"  data-id="' + roleId + '">' + $(e).data('name') + '<a data-id="' + roleId + '" onClick="deleteSelectedRole(this)" class="cancelSelectedRole" href="javascript:void(0);">取消</a></span>';
            $('#selectedRoles').append($(selectStr));
        }
    }

    if (checkedAll) {
        checkedAll = false;
    } else {
        checkedAll = true;
    }
}
//保存角色信息
function saveRoles() {
    if (currentPermissionId == null || currentPermissionId == '') {
        alertError('请选择权限');
        return;
    }

    var checkedBoxs = $("input:checked[name='checkbox']");
    var checkedCount = checkedBoxs.length;
    if (checkedCount === 0) {
        alertError("请为权限选择相应的角色！");
        return false;
    }
    currentChooseRoles = [];
    var roleConfBeans = [];
    $.each(checkedBoxs, function (index, item) {
        currentChooseRoles.push($(item).data('id'));
        roleConfBeans.push({
            id: $(item).data('id'),
            status: 0
        });
    });
    var url = '/permissionRole/save';
    $.ajax({
        type: 'post',
        traditional: true,
        url: url,
        data: JSON.stringify({
            permissionIds: [currentPermissionId],
            roleConfBeans: roleConfBeans
        }),
        headers: {
            'Content-Type': 'application/json',
        },
        success: function (data) {
            alertSuccess('保存成功');
            checkedAll = true;
            $('.checkbox-all').attr('checked', false);
            selectAll();
            checkedAll = false;
            getHasChooseRoles(currentPermissionId);
        },
        error: function (data) {
            alertError('保存失败');
        }
    });
}
/**
 * 通过角色名搜索角色
 */
function serachRoles() {
    var param = $('#role-name').val();
    $('#roleTable tbody').eq(1).find('tr').each(function () {
        var targetText = $($(this).find('td').get(1)).text();
        if (targetText.toLowerCase().indexOf(param.toLowerCase()) == -1) {
            $(this).hide();
            $(this).removeClass("isMatched");
        } else {
            $(this).show();
            $(this).addClass("isMatched");
        }
    })
}