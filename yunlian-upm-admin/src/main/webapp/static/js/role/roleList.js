var oTable;
$(function () {
    oTable = $('#role-table').dataTable({

        "aaSorting": [[ 20, "desc" ]]  ,
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
    $('.table').on('click', ' tbody td .row-details',
        function() {
            var nTr = $(this).parents('tr')[0];
            if (oTable.fnIsOpen(nTr)) //判断是否已打开
            {
                /* This row is already open - close it */
                $(this).addClass("row-details-close").removeClass("row-details-open");
                oTable.fnClose(nTr);
            } else {
                /* Open this row */
                $(this).addClass("row-details-open").removeClass("row-details-close");
                //  alert($(this).attr("data_id"));
                //oTable.fnOpen( nTr,
                // 调用方法显示详细信息 data_id为自定义属性 存放配置ID
                fnFormatDetails(nTr, $(this).attr("data_id"));
            }
        }
    );
});


function fnFormatDetails(nTr, pdataId) {
    //根据配置Id 异步查询数据
    //Content-Type: application/json;charset=UTF-8
    $.get("/userRole/listHaveRoleNon/"+pdataId,
        function(json) {
            var resData = json;
            jsondata=$.parseJSON(resData);
            var sOut = '<table style="width:100%;color: #003399;" >';
            if(jsondata.length>0){
                sOut+='<tr style="font-size: 6px;">';
                sOut+='<td width="10%">用户名</td>';
                sOut+='<td width="10%">登录名</td>';
                sOut+='<td width="10%">部门</td>';
                sOut+='<td width="10%">所在组织</td>';
                sOut+='<td width="10%">岗位</td>';
                sOut+='<td width="10%">邮箱</td>';
                sOut+='<td width="10%">用户类型</td>';
                sOut+='</tr>'
            }else{
                sOut+='<tr style="font-size: 6px;">';
                sOut+='<td width="10%" align="center">暂无数据</td>';
                sOut+='</tr>'
            }

            for(var i=0;i<jsondata.length;i++){
                sOut+='<center> <p style="width:70%"><tr style="font-size: 6px;">';
                sOut+='<td width="10%">';
                if(jsondata[i].name!=null&&jsondata[i].name!=undefined){
                    sOut+=jsondata[i].name;
                }
                sOut+='</td>';
                sOut+='<td width="10%">';
                if(jsondata[i].loginName!=null&&jsondata[i].loginName!=undefined){
                    sOut+=jsondata[i].loginName;
                }
                sOut+='</td>';
                sOut+='<td width="10%">';
                if(jsondata[i].departmentName!=null&&jsondata[i].departmentName!=undefined){
                    sOut+=jsondata[i].departmentName;
                }
                sOut+='</td>';
                sOut+='<td width="10%">';
                if(jsondata[i].orgName!=null&&jsondata[i].orgName!=undefined){
                    sOut+=jsondata[i].orgName;
                }
                sOut+='</td>';
                sOut+='<td width="10%">';
                if(jsondata[i].posName!=null&&jsondata[i].posName!=undefined){
                    sOut+=jsondata[i].posName;
                }
                sOut+='</td>';
                sOut+='<td width="10%">';
                if(jsondata[i].email!=null&&jsondata[i].email!=undefined){
                    sOut+=jsondata[i].email;
                }
                sOut+='</td>';
                sOut+='<td width="10%">';
                if(jsondata[i].typeName!=null&&jsondata[i].typeName!=undefined){
                    sOut+=jsondata[i].typeName;
                }
                sOut+='</td>';
                //sOut+='<td width="10%">'+jsondata[i].loginName===undefined?"a":jsondata[i].loginName+'</td>';
                //sOut+='<td width="10%">'+jsondata[i].departmentName+'</td>';
                //sOut+='<td width="10%">'+jsondata[i].orgName+'</td>';
                //sOut+='<td width="10%">'+jsondata[i].posName+'</td>';
                //sOut+='<td width="10%">'+jsondata[i].email+'</td>';
                //sOut+='<td width="10%">'+jsondata[i].typeName+'</td>';
                sOut+='</tr></p></center>'
            }
            sOut+='</table>';
            oTable.fnOpen( nTr,sOut, 'details' );
        });

    /**
     $.ajax({
               type:'post',
               url:'${pageContext.request.contextPath }/statistic/statistic!ajaxDetails.do',
               data:{"pdataId":pdataId},
               dataType:"text",
               async:true,
               beforeSend:function(xhr){//信息加载中
                   oTable.fnOpen( nTr, '<span id="configure_chart_loading"><img src="${pageContext.request.contextPath }/image/select2-spinner.gif"/>详细信息加载中...</span>', 'details' );
               },
               success:function (data,textStatus){
                   if(textStatus=="success"){  //转换格式 组合显示内容
                       var res = eval("("+data+")");
                       var sOut = '<table style="width:100%;">';
                       for(var i=0;i<res.length;i++){
                           sOut+='<tr>';
                           sOut+='<td width="5%"></td><td width="35%">'+res[i].name+'</td>';
                           sOut+='<td width="30%"><div class="div_left">'+res[i].num1+'</div><div class="div_center">|</div><div class="div_right">'+res[i].count1+'</div></td>';
                           sOut+='<td style="width:30%"><div class="div_left">'+res[i].num2+'</div><div class="div_center">|</div><div class="div_right">'+res[i].count2+'</div></td>';
                           sOut+='</tr>'
                       }
                       sOut+='</table>';
                       oTable.fnOpen( nTr,sOut, 'details' );
                   }
               },
               error: function(){//请求出错处理
                   oTable.fnOpen( nTr,'加载数据超时~', 'details' );
               }
           });**/

}


$(function () {
    $('.paging_four_button').css({"float": "right"});
});
$(document).ready(function () {
    $('.delete').click(function () {
        var url =$(this).data("href");
        var data ='id=' + $(this).data("id");
        layer.confirm("确定删除" + $(this).data("name") + "角色吗？", {icon: 3, title:'提示'}, function(index){
            $.ajax({
                url:url,
                type: 'POST',
                data: data,
                //async : false, //默认为true 异步
                error: function () {
                    alertError('操作失败',methodObj);
                },
                success: function (data) {
                    alertSuccess(eval(data).result.msg,methodObj);
                }
            });
        });
    })
});

var methodObj =function(index){
    location.reload();
}

function checkSuperRole(){
    alertError('超级管理员角色不可修改');
}