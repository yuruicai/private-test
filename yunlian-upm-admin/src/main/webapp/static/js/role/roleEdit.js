$(document).ready(function () {
    var appId = $.cookie("appId");
    var appName = $.cookie("appName");
    $("#applicationId").val(appId);
    $("#applicationName").val(appName);

    $('#createRole').click(function () {
        var perName = $("#name").val();
        var perCode = $("#code").val();
        var comment = $("#comment").val();
        if ($("#name").length < 0 || perName == null || perName.trim() == '') {
            alert("角色名不能为空")
            return;
        }

        if($("#name").val().length>30){
            alert("角色名长度不能大于30");
            return;
        }

        if ($("#code").length < 0 || perCode == null || perCode.trim() == '') {
            alert("角色编码不能为空");
            return;
        }

        if($("#comment").val().length>100){
            alert("角色说明长度不能大于100");
            return;
        }
        var data = $("#roleCreateForm").serialize();
        $.post("/role/createRole", data, function (result) {
            alert(result);
            window.location.href="/role/list";
        }, "text");
    })

    $('#permissionBtn').click(function () {
        var roleId = $("#roleId").val();
        if ($("#roleId").length < 0 || roleId == null || roleId.trim() == '') {
            alert("角色ID不能为空")
            return;
        }
        window.location.href = "/role/permission?curRoleId=" + roleId + "&appId=" + appId;
    })

    $("input#name").on('blur', function () {
        var name = $("#name").val();
        var code = $("#code").val();
        if (name != null && name != "" && (code == null || code == "")) {
            $.ajax({
                url: "/role/createCode.ajax",
                type: 'GET',
                data: {"appId": appId, "name": name},
                error: function () {
                    alert("获取角色编码失败");
                    $("#code").focus();
                },
                success: function (data) {
                    $("#code").val(data.data);
                }
            });
        }
    });
});
