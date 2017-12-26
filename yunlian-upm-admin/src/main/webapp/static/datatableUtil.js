/**
 * Created by zhanggm on 2015/11/25.
 */

document.write(' <script language="javascript" src="datatables/jquery.dataTables.min.js" > <\/script>');
document.write(' <script language="javascript" src="datatables/jquery.dataTables.bootstrap.js" > <\/script>');


function createLocalDatatable(tableDom){
    tableDom.dataTable({
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
            }
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
    return tableDom;
}
