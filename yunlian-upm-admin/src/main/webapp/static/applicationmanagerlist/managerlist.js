function getAllUsers() {
    $.ajax({
        url: "/user/users.ajax",
        error: function () {
            alert('获取权限出现错误');
        },
        success: function (res) {
            allUsers = eval(res.data);
            var option;
            $('.js-select-user').append(' <option value="">请输入用户名</option>');
            $.each(allUsers, function (index, item) {
                option = '<option value="' + item.id + '">' + item.name + '</option>';
                $(".js-select-user").append(option);
            });
            $('.js-select-user').comboSelect()
        }
    });
}

function getRoles() {
    var appId = $("#appId").attr("value");
    currentRoleId = $('#roleId').attr("value");
    $.ajax({
        url: "/app/getRoles",
        data:{"appId":appId},
        error: function () {
            alert('获取角色出现错误');
        },
        success: function (res) {
            data = eval(res.data);
            var option;
            $('.js-select-roles').append(' <option value="">请输入角色</option>');
            $.each(data, function (index, item) {
                if (currentRoleId == item.id) {
                    var option = '<option  selected="selected" value="' + item.id + '">' + item.name + '</option>';
                } else {
                    option = '<option value="' + item.id + '">' + item.name + '</option>';
                }
                $(".js-select-roles").append(option);
            });
            $('.js-select-roles').comboSelect();
            $(".combo-select").css("margin-top","-3px");
            $(".combo-select input").css("width","200px");
        }
    });
}

$(function() {
    var table = $('.table').dataTable({
        "aaSorting": [[ 10, "desc" ]]  ,

        "aLengthMenu": [[50,20, 10], [50,20,10]],
        "sPaginationType": "four_button", //分页，首页、上一页、下一页、尾页
        "iDisplayLength": 10,
        "aoColumnDefs":[
            {
                sDefaultContent: '',
                aTargets: ['_all']
            },
            {
                "bSortable": false,
                "aTargets": ['_all']
            },
            { "bSearchable": false, "aTargets": [ 2 ] }

        ],
        "oLanguage": {
            "sLengthMenu": "显示 _MENU_ 条记录",
            "sSearch": "搜索:",
            "sInfo": "显示第 _START_ - _END_ 条记录，共 _TOTAL_ 条",
            "sInfoEmpty": "没有符合条件的记录",
            "sProcessing": "正在加载中...",
            "sZeroRecords": "没有符合条件的记录",
            "oPaginate" : {
                "sFirst" : "首页",
                "sPrevious" : " 上页 ",
                "sNext" : " 下页 ",
                "sLast" : " 尾页 "
            }
        },



    });

    $('.js-select-user').parent().css("margin-top","2px");

    $("input[aria-controls='app-manager-table']").attr("placeholder","用户名/姓名");

    $('.js-select-user').change(function (e, v) {
        var userId = e.target.value;
        $('#userId').val(userId);
    });

    $('.js-select-roles').change(function (e, v) {
        var roleId = e.target.value;
        $('#roleId').val(roleId);
    });

    $("#changeRole").click(function(){
        var appId =encodeURI($("#appId").attr("value"));
        var roleId = encodeURI($("#roleId").val());
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/app/changeRole?appId="+appId+"&roleId="+roleId,
            dataType: 'json',
            success: function() {
                window.location.reload();//刷新当前页面.
            }
        });

    });

    getAllUsers();
    $("#addAppManager").click(function(){
        var appId =encodeURI($("#appId").attr("value"));
        var userId = encodeURI($("#userId").val());
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/app/addAppManager?appId="+appId+"&userId="+userId,
            dataType: 'json',
            success: function(data) {
                if(data.result.isSuccess != null && data.result.isSuccess == false){
                    alert(data.result.msg)
                    return;
                }
                window.location.reload();//刷新当前页面.
            }
        });

    });

    $(".deleteAppManager").each(function(){
        $(this).click(function(){
            var userId = $(this).attr("data-id");
            var appId = $("#appId").attr("value");
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/app/deleteAppManager?appId="+appId+"&userId="+userId,
                dataType: 'json',
                success: function() {
                    window.location.reload();//刷新当前页面.
                }
            });

        });

    });

    getRoles();

});
