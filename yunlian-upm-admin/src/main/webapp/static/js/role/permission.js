//所有角色
var allRoles = null;
var zNodes = null;
//当前角色信息
var currentRoleId = null;
var currentRoleName = null;
//z-Tree
var setting = {
    check: {
        enable: true
    },
    data: {
        simpleData: {
            enable: true
        }
    }
};

$(document).ready(function () {
    //获取所有角色信息
    getAllRoles();

    $('#selectedPermissions').empty();
    checkedAll = false;

    currentRoleId = $('input[name="roleId"]').val();
    currentRoleName = $('#roleSearch').val();


    //查询角色下的权限
    $('.js-select').change(function (e, v) {
        var roleId = e.target.value;
        var roleName = $(e.target).find("option:selected").text();

        $('.permission-role-list').empty();
        var isExistRole = false;
        $.each(allRoles, function (index, item) {
            if (item.name == roleName) {
                currentRoleId = item.id;
                currentRoleName = item.name;
                $('#roleId').val(item.id);
                showRoleInfo(currentRoleId);
                isExistRole = true;
                return false;
            }
        });

        if (!isExistRole) {
            alertError('角色不存在');
            currentRoleId = null;
            currentRoleName = null;
            $('#roleId').val(currentRoleId);
            $.each($('.js-select option'), function (index, item) {
                if ($(item).val() == currentRoleId) {
                    $(item).attr('selected', true);
                }
            });
        }
    });
    initZTree();
});

function initZTree() {
    //z-tree
    $.ajax({
        async: false,
        type: "get",
        url: "/role/getMenus/" + currentRoleId,
        success: function (json) {
            zNodes = JSON.parse(json);
            $.fn.zTree.init($("#treePermissions"), setting, zNodes);
            var zTree = $.fn.zTree.getZTreeObj("treePermissions");
            zTree.expandAll(true);
        },
        error: function () {
            alertError('获取信息出错');
        }
    });
}


//根据角色名称查询角色
function getRoleInfoByRoleName(name) {
    if (name == null || name == '') {
        return;
    }
    if (allRoles != null) {
        $.each(allRoles, function (index, item) {
            if (name == item.name) {
                $('#roleId').val(item.id);
                currentRoleId = item.id;
                currentRoleName = item.name;
                showRoleInfo(currentRoleId);
            }
        });
    }
}

//获取所有角色信息
function getAllRoles() {
    var url = '/role/listOfApp';
    $.get(url, function (data) {
        if (data.status == '200') {
            allRoles = eval(data.data);
            //$('.js-select').empty();
            //$('.js-select').append(' <option value="">请输入角色名称</option>');
            var queryRoleId=null;
            $.each(allRoles, function (index, item) {
                if(queryRoleId==null){
                    queryRoleId=item.id;
                }

                var option = '<option value="' + item.id + '">' + item.name + '</option>';
                if (currentRoleId != undefined && currentRoleId != null && currentRoleId==item.id) {
                    option = '<option value="' + item.id + '" selected>' + item.name + '</option>';
                }
                $('.js-select').append(option);

            });
            showRoleInfo($('#roleId').val());
            $('.js-select').comboSelect();
        } else {
            alertError('获取当前角色出现错误');
        }
    });
}

$('.js-select').change(function (event) {
    showRoleInfo(event.target.value);
})


//根据角色ID获取角色信息并展示在页面上
function showRoleInfo(roleId) {
    if(allRoles==null){
        $('#roleId').html('');
        $('#code').html('');
        $('#name').html('');
        $('#comment').html('');
        return;
    }
    $.each(allRoles, function (index, item) {
        if (roleId != undefined && roleId != null && roleId == item.id) {
            var url = '';
            initZTree();
            $('#roleId').html(item.id);
            $('#code').html(item.code);
            $('#name').html(item.name);
            $('#comment').html(item.comment);
        }
    });
}

function savePermissions() {
    var permissions = $.fn.zTree.getZTreeObj("treePermissions").getCheckedNodes();
    if (permissions == undefined || permissions == null || permissions.length == 0) {
        alert('请选择权限');
        return;
    }
    var targetPermissions = [];
    $.each(permissions, function (index, item) {
            targetPermissions.push(item.id);
        }
    );
    var url = '/role/permission';
    var data = {'roleId': currentRoleId, 'ids': targetPermissions};
    $.ajax({
        type: 'post',
        traditional: true,
        url: url,
        data: data,
        success: function (data) {
            alertSuccess('保存成功');
            initZTree();
        },
        error: function (data) {
            alertError('保存失败');
        }
    });
}

function colltree() {
    $.fn.zTree.init($("#treePermissions"), setting, zNodes);
    var zTree = $.fn.zTree.getZTreeObj("treePermissions");
    zTree.expandAll(false);
}
function expantree() {
    $.fn.zTree.init($("#treePermissions"), setting, zNodes);
    var zTree = $.fn.zTree.getZTreeObj("treePermissions");
    zTree.expandAll(true);
}