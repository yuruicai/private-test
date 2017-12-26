<div id="app-list">
    <a href='/app/create' class="btn btn-primary">创建应用</a>
<hr class="hr-bottom"/>
    <#--<div style="clear:both;">-->
        <#--搜索：<input class="f-text" type="text" style="height:20px" value="" onkeyup="search()" name="app_name" id="app_name" placeholder="应用名称"/>-->
    <#--</div>-->
    <table id="app-table" class="table table-striped table-bordered table-hover table-full-width">

        <thead>
        <tr>
            <th data-params="{name:'name'}"  style="width: 30">应用名称</th>
            <th data-params="{name:'roleId'}" style="width: 30">应用默认角色</th>
            <th data-params="{name:'appkey'}" style="width: 30">appKey</th>
            <th data-params="{name:'secret'}" style="width: 30">secret</th>
            <th data-params="{name:'set'}"  style="width: 50">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list apps as app>
        <tr>
            <td>${app.name!""}</td>
            <#if app.roleId?? && app.roleId ??>
                <td>${app.roleName!""}</td>
            <#else>
                <td></td>
            </#if>
            <td>${app.appKey!""}</td>
            <td>${app.secret!""}</td>
            <td>
                <a class="btn btn-small btn-primary"  href="/app/edit?id=${app.id!''}">编辑</a>
                <a class="btn btn-small btn-success"  href="/app/managerList?appId=${app.id!''}">配置</a>
                <#--<a class="delete btn btn-small btn-primary btn-warning" data-id="${app.id!''}" data-name="${app.name!""}" data-href="/app/delete" href="javascript:void(0)">删除</a>-->
            </td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
<script src="/static/layer/layerUtil.js"></script>
<script src="/static/datatables/jquery.dataTables.min.js"></script>
<script src="/static/datatables/jquery.dataTables.bootstrap.js"></script>
<script>
    $(function() {
        $('.table').dataTable({
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
                "sSearch":"查找:",
                "sLengthMenu": "显示 _MENU_ 条记录",
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
            }
        });
        $("input[aria-controls='app-table']").attr("placeholder","应用名称/默认角色");

        $('.delete').click(function () {
            var url =$(this).data("href");
            var data ='id=' + $(this).data("id");
            layer.confirm("确定删除" + $(this).data("name") + "吗？", {icon: 3, title:'提示'}, function(index){
                $.ajax({
                    url:url,
                    type: 'POST',
                    data: data,
                    //async : false, //默认为true 异步
                    error: function () {
                        alertError('删除失败');
                        location.reload();
                    },
                    success: function (data) {
                        alertSuccess("删除成功");
                        location.reload();
                    }
                });
            });
        })
    });
    var methodObj =function(index){
        location.reload();
    }
//    function search() {
//        var param = $('#app_name').val();
//        $('#app-table tbody').find('tr').each(function () {
//            var targetText = $($(this).find('td').get(0)).text();
//            if (targetText.indexOf(param) == -1) {
//                $(this).hide();
//                $(this).removeClass("isMatched");
//            } else {
//                $(this).show();
//                $(this).addClass("isMatched");
//            }
//        })
//    }
</script>

<script src="/static/datatables/jquery.dataTables.min.js"></script>
<script src="/static/datatables/jquery.dataTables.bootstrap.js"></script>
<script type="text/javascript" src="/static/jquery.combo.select.js"></script>