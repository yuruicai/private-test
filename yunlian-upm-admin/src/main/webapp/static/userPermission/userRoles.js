var allChoosedIds = [];
var appId = $.cookie('appId');

//用户目前已经拥有的角色
var havedChooseRoles = null;

//当前选择的角色
var currentChooseRoles = [];

//所有用户
var allUsers = null;

var checkedAll = false;

//当前用户信息
var currentUserId = null;
var currentUsernName = null;
$(document).ready(function () {
    $('#selectedRoles').empty();
    checkedAll = false;
    //获取所有用户信息
    getAllUsers();

    currentUserId = $('input[name="user-id"]').val();

    ////根据UserID获取角色信息
    getHasChooseRoles(currentUserId);

    //查询用户下的角色
    $('.js-select').change(function (e, v) {
        var userId = e.target.value;
        var userName = $(e.target).find("option:selected").text();
        $('#roleList').show();
        $('.user-role-list').empty();
        var isExistUser = false;
        $.each(allUsers, function (index, item) {
            if (item.name == userName) {
                currentUserId = item.id;
                currentUsernName = item.name;
                $('#user-id').val(item.id);
                getHasChooseRoles(currentUserId);
                isExistUser = true;
                return false;
            }
        });

        if (!isExistUser) {
            alertError('用户不存在');
            $('#user-id').val(userId);
            $.each($('.js-select option'), function (index, item) {
                if ($(item).val() == currentUserId) {
                    $(item).attr('selected', true);
                    getHasChooseRoles(currentUserId);
                }
            });
        }
    });
});

//获取所有用户信息
function getAllUsers() {
    $.ajax({
        url: "/user/users.ajax",
        error: function () {
            alertError('获取用户出现错误');
        },
        success: function (res) {
            allUsers = eval(res.data);
            addAllUserOption(allUsers);
        }
    });
}
function addAllUserOption(allUsers) {
    var option;
    $('.js-select').append(' <option value="">请输入用户名称</option>');
    $.each(allUsers, function (index, item) {
        if (currentUserId == item.id) {
            var option = '<option  selected="selected" value="' + item.id + '">' + item.name + '</option>';
        } else {
            option = '<option value="' + item.id + '">' + item.name + '</option>';
        }
        $(".js-select").append(option);
    });
    $('.js-select').comboSelect();
    $('.option-item').css({ "font-size": "15px" });
}

function getHasChooseRoles(userId) {
    $.ajax({
        url: "/userRole/roleConf.ajax",
        type: 'POST',
        data: {'userId': userId},
        error: function () {
            alertError('获取当前用户下的角色出现错误');
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
    $.each($('.checkbox'), function (index, item) {
        $(item).removeAttr('disabled');
        $(item).removeAttr('checked');
    });

    if (chooseRoles == undefined || chooseRoles == null || chooseRoles.length == 0) {
        return;
    }
    $('.user-role-list').empty();
    $.each(chooseRoles, function (index, item) {
        if ($('#checkbox_' + item.id).length > 0) {
            $('#checkbox_' + item.id).prop('checked', true);
            $('#checkbox_' + item.id).prop('disabled', 'disabled');
        }

        if(item.code=='UPM_APPM'){
            return true;
        }
        var permission = '' +
            '<li class="alert alert-info">' +
            '    <span class="label label-info" style="font-size: 16px;">' + item.roleName + '</span>' +
            '    <a href="javascript:void(0);" class="context-del" data-rolename="' + item.roleName + '" data-id="' + item.id + '" onclick="deleteRole(this)">删除</a>' +
            '</li>';
        $('.user-role-list').append($(permission));
    });
}
function deleteRole(role) {
    var roleId = $(role).data("id");
    var rolename = $(role).data("rolename");
    layer.confirm("确定删除" + rolename + "角色吗？", {icon: 3, title: '提示'}, function () {
        var data = {'userId': currentUserId, 'roleId': roleId}
        $.ajax({
            url: "/userRole/delete",
            type: 'post',
            data: data,
            error: function () {
                alertError('删除失败');
            },
            success: function (data) {
                alertSuccess('删除成功');
                $(role).parent().remove();
                $('#checkbox_'+roleId).attr('checked',false);
                getHasChooseRoles(currentUserId);
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
            alertError('该用户下已含有此角色,不可重复添加');
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

        if($('#checkbox_' + roleId).attr('disabled')=='disabled'){
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
function saveUserRoles() {
    if (currentUserId == null || currentUserId == '') {
        alertError('请选择用户');
        return;
    }
    var currentUserName = $('.js-select').val();
    if (currentUserName == null || currentUserName == '') {
        alertError('请选择用户');
        return;
    }

    var checkedBoxs = $("input:checked[name='checkbox']");
    var checkedCount = checkedBoxs.length;
    if (checkedCount === 0) {
        alertError("请为用户选择相应的角色！");
        return false;
    }
    currentChooseRoles = [];
    var roleConfBeans = [];
    $.each(checkedBoxs, function (index, item) {
        if($(item).attr('disabled')=='disabled'){
            return true;
        }

        currentChooseRoles.push($(item).data('id'));
        roleConfBeans.push({
            id: $(item).data('id'),
            status: 0,
            typeContext: {}
        });
    });
    var url = '/userRole/save';
    $.ajax({
        type: 'post',
        traditional: true,
        url: url,
        data: JSON.stringify({
            userIds: [currentUserId],
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
            getHasChooseRoles(currentUserId);
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
    $('#roleTable tbody').find('tr').each(function () {
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