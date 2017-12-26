$(document).ready(function () {
    $('#saveButton').click(function () {
        var perName = $("#name").val();
        var perCode = $("#code").val();
        if($("#name").length<0|| perName==null || perName.trim()==''){
            alertError('权限名称不能为空');
            return;
        }
        if($("#code").length<0|| perCode==null || perCode.trim()==''){
            alertError('权限编码不能为空', {icon: 2});
            return;
        }
        $("#resource-form").submit();
    })
    $('#permissionRoleHref').click(function () {
        var permissionId = $("#permissionId").val();
        if($("#permissionId").length<0|| permissionId==null || permissionId.trim()==''){
            alertError("请先新建权限")
            return;
        }
        window.location.href = "/permissionRole/permissionRoles?permissionId=" + permissionId;
    })
    $('#listWithPermissionHref').click(function () {
        var permissionId = $("#permissionId").val();
        if($("#permissionId").length<0|| permissionId==null || permissionId.trim()==''){
            alertError("请先新建权限")
            return;
        }
        window.location.href = "/permissionRole/listWithPermission?permissionId=" + permissionId;
    })
    $("input#name").on('blur', function () {
        var name = $("#name").val();
        var code = $("#code").val();
        if (name != null && name != "" && (code == null || code == "")) {
            $.ajax({
                url: "/permission/createCode",
                type: 'GET',
                data: {name: name},
                error: function () {
                },
                success: function (data) {
                    $("#code").val(data);
                }
            });
        }
    });
});
