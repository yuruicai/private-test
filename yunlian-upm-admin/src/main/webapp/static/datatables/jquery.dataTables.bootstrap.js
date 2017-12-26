//http://datatables.net/plug-ins/pagination#bootstrap
$.extend( true, $.fn.dataTable.defaults, {
	"sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
	"sPaginationType": "bootstrap",
	"oLanguage": {
		"sLengthMenu": "Display _MENU_ records"
	}
} );


/* API method to get paging information */
$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
{
    return {
        "iStart":         oSettings._iDisplayStart,
        "iEnd":           oSettings.fnDisplayEnd(),
        "iLength":        oSettings._iDisplayLength,
        "iTotal":         oSettings.fnRecordsTotal(),
        "iFilteredTotal": oSettings.fnRecordsDisplay(),
        "iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
        "iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
    };
}
 
/* Bootstrap style pagination control */
$.extend( $.fn.dataTableExt.oPagination, {
    "bootstrap": {
        "fnInit": function( oSettings, nPaging, fnDraw ) {
            var oLang = oSettings.oLanguage.oPaginate;
            var fnClickHandler = function ( e ) {
                e.preventDefault();
                if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
                    fnDraw( oSettings );
                }
            };
 
            $(nPaging).addClass('pagination').append(
                '<ul>'+
                    '<li class="prev disabled"><a href="#"><i class="icon-double-angle-left"></i></a></li>'+
                    '<li class="next disabled"><a href="#"><i class="icon-double-angle-right"></i></a></li>'+
                '</ul>'
            );
            var els = $('a', nPaging);
            $(els[0]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
            $(els[1]).bind( 'click.DT', { action: "next" }, fnClickHandler );
        },
 
        "fnUpdate": function ( oSettings, fnDraw ) {
            var iListLength = 5;
            var oPaging = oSettings.oInstance.fnPagingInfo();
            var an = oSettings.aanFeatures.p;
            var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);
 
            if ( oPaging.iTotalPages < iListLength) {
                iStart = 1;
                iEnd = oPaging.iTotalPages;
            }
            else if ( oPaging.iPage <= iHalf ) {
                iStart = 1;
                iEnd = iListLength;
            } else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
                iStart = oPaging.iTotalPages - iListLength + 1;
                iEnd = oPaging.iTotalPages;
            } else {
                iStart = oPaging.iPage - iHalf + 1;
                iEnd = iStart + iListLength - 1;
            }
 
            for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
                // Remove the middle elements
                $('li:gt(0)', an[i]).filter(':not(:last)').remove();
 
                // Add the new list items and their event handlers
                for ( j=iStart ; j<=iEnd ; j++ ) {
                    sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
                    $('<li '+sClass+'><a href="#">'+j+'</a></li>')
                        .insertBefore( $('li:last', an[i])[0] )
                        .bind('click', function (e) {
                            e.preventDefault();
                            oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
                            fnDraw( oSettings );
                        } );
                }
 
                // Add / remove disabled classes from the static elements
                if ( oPaging.iPage === 0 ) {
                    $('li:first', an[i]).addClass('disabled');
                } else {
                    $('li:first', an[i]).removeClass('disabled');
                }
 
                if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
                    $('li:last', an[i]).addClass('disabled');
                } else {
                    $('li:last', an[i]).removeClass('disabled');
                }
            }
        }
    }
} );

$.fn.dataTableExt.oPagination.four_button = {
    "fnInit": function (oSettings, nPaging, fnDraw) {
        var oLang = oSettings.oLanguage.oPaginate;
        var fnClickHandler = function (e) {
            e.preventDefault();
            if (oSettings.oApi._fnPageChange(oSettings, e.data.action)) {
                fnDraw(oSettings);
            }
        };
        $(nPaging).addClass('pagination').append(
            '<ul>' +
            '<li class="first disabled"><a href="#">' + oLang.sFirst + '</a></li>' +
            '<li class="prev  disabled"><a href="#">' + oLang.sPrevious + '</a></li>' +
            '<li class="next  disabled"><a href="#">' + oLang.sNext + '</a></li>' +
            '<li class="last  disabled"><a href="#">' + oLang.sLast + '</a></li> &nbsp;&nbsp;' +
            '<span id="pageTotalSpan" style="font-size:16px;vertical-align :sub;color: #428bca"></span>'+
            '<input type="text" style="width:25px;padding-top: 5px;padding-bottom: 5px;height: 18px;border-left: 0px;border-radius: 4px 4px 4px 4px;color: #428bca" id="redirect" class="redirect">'+
            '<span style="font-size:16px; vertical-align :sub;color: #428bca">页</span>'+
            '</ul>'
        );
        //datatables分页跳转
        $(nPaging).find(".redirect").keyup(function(e){
            var ipage = parseInt($(this).val());
            var oPaging = oSettings.oInstance.fnPagingInfo();
            if(isNaN(ipage) || ipage<1 || oPaging.iTotalPages==0){
                ipage = 1;
            }else if(ipage>oPaging.iTotalPages){
                ipage=oPaging.iTotalPages;
            }
            $(this).val(ipage);
            ipage--;
            oSettings._iDisplayStart = ipage * oPaging.iLength;
            fnDraw( oSettings );
        });
        var els = $('a', nPaging);
        $(els[0]).bind('click.DT', {action: "first"}, fnClickHandler);
        $(els[1]).bind('click.DT', {action: "previous"}, fnClickHandler);
        $(els[2]).bind('click.DT', {action: "next"}, fnClickHandler);
        $(els[3]).bind('click.DT', {action: "last"}, fnClickHandler);
    },

    "fnUpdate": function (oSettings, fnDraw) {
        var iListLength = 5;
        var oPaging = oSettings.oInstance.fnPagingInfo();
        var an = oSettings.aanFeatures.p;
        var i, j, sClass, iStart, iEnd, iHalf = Math.floor(iListLength / 2);

        if (oPaging.iTotalPages < iListLength) {
            iStart = 1;
            iEnd = oPaging.iTotalPages;
        }
        else if (oPaging.iPage <= iHalf) {
            iStart = 1;
            iEnd = iListLength;
        } else if (oPaging.iPage >= (oPaging.iTotalPages - iHalf)) {
            iStart = oPaging.iTotalPages - iListLength + 1;
            iEnd = oPaging.iTotalPages;
        } else {
            iStart = oPaging.iPage - iHalf + 1;
            iEnd = iStart + iListLength - 1;
        }
        $("#pageTotalSpan").text('共'+oPaging.iTotalPages+'页，至');
        $("#redirect").css({"margin-right":"2px","box-shadow":"-1px 0 0 #eee"});
        for (i = 0, iLen = an.length; i < iLen; i++) {
            // Remove the middle elements
            $('li:gt(1)', an[i]).filter(':lt(-2)').remove();
            // Add the new list items and their event handlers
            for (j = iStart; j <= iEnd; j++) {
                sClass = (j == oPaging.iPage + 1) ? 'class="active"' : '';
                $('<li ' + sClass + '><a href="#">' + j + '</a></li>')
                    .insertBefore($('li:eq(-2)', an[i])[0])
                    .bind('click', function (e) {
                        e.preventDefault();
                        oSettings._iDisplayStart = (parseInt($('a', this).text(), 10) - 1) * oPaging.iLength;
                        fnDraw(oSettings);
                    });
            }

            // Add / remove disabled classes from the static elements
            if (oPaging.iPage === 0) {
                $('li:lt(2)', an[i]).addClass('disabled');
            } else {
                $('li:lt(2)', an[i]).removeClass('disabled');
            }
            if (oPaging.iPage === oPaging.iTotalPages - 1 || oPaging.iTotalPages === 0) {
                $('li:gt(-3)', an[i]).addClass('disabled');
            } else {
                $('li:gt(-3)', an[i]).removeClass('disabled');
            }
        }

        $('.paging_four_button').css({"float":"right"});

    }
};