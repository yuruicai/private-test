/*jshint node:true*/
/*global $*/
require('highcharts-client/lib/highcharts');

exports.init = function(opts) {

    $(function () {
        $('#audit-uriCount-chart').highcharts({
            title: {
                text: '次数',
                x: -20
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 150,
                dateTimeLabelFormats: {
                    second: '%H:%M:%S',
                    minute: '%m-%d %H:%M',
                    hour: '%Y-%m-%d %H:%M',
                    day: '%Y-%m-%d',
                    month: '%Y-%m-%d',
                    year: '%Y'
                }
            },
            yAxis: {
                title: {
                    text: '次数'
                },
                plotLines: [
                    {
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }
                ]
            },
            tooltip: {
                valueSuffix: '次'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: opts.series
        });
    });
};
