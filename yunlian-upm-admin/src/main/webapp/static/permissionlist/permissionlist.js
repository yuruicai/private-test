$(function () {
    $('.table').dataTable({
        "aaSorting": [[10, "desc"]],
        "aLengthMenu": [[100, 50, 20], [100, 50, 20]],
        "sPaginationType": "four_button", //分页，首页、上一页、下一页、尾页
        "iDisplayLength": 20,
        "aoColumnDefs": [
            {
                sDefaultContent: '',
                aTargets: ['_all']
            },
            {
                "bSortable": false,
                "aTargets": ['_all']
            }
        ],
        "oLanguage": {
            "sLengthMenu": "显示 _MENU_ 条记录",
            "sInfo": "显示第 _START_ - _END_ 条记录，共 _TOTAL_ 条",
            "sInfoEmpty": "没有符合条件的记录",
            "sProcessing": "正在加载中...",
            "sZeroRecords": "没有符合条件的记录",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": " 上页 ",
                "sNext": " 下页 ",
                "sLast": " 尾页 "
            }
        },
    });
});
$(function () {
    $('.paging_four_button').css({"float": "right"});
});
$('.delete').click(function () {
    var url=$(this).data("href");
    var id=$(this).data("id");
    layer.confirm("确定删除" + $(this).data("name") + "权限吗？", {icon: 3, title:'提示'}, function(){
        $.ajax({
            url: url,
            type: 'post',
            data: 'id=' + id,
            error: function () {
                alertError('删除失败',reload);
            },
            success: function (data) {
                alertSuccess(eval(data).result.msg,reload);
            }
        });
    });
})
var reload =function(index){
    location.reload();
}