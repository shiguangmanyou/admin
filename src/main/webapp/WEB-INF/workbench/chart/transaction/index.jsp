<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="/crm/jquery/ECharts/echarts.min.js"></script>
    <script src="/crm/jquery/jquery-1.11.1-min.js"></script>
</head>
<body>
<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<div id="main" style="width: 1000px;height:500px;"></div>

<div id="main2" style="width: 600px;height:400px;"></div>
<script>
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    var myChart2 = echarts.init(document.getElementById('main2'));

    //柱状图
    $.get("/crm/workbench/chart/barVoEcharts",function (data) {
        //data:BarVo
        // 指定图表的配置项和数据
        var option = {
            title: {
                text: '交易图标'
            },
            tooltip: {},
            legend: {
                data: ['交易']
            },
            xAxis: {
                data: data.titles
            },
            yAxis: {},
            series: [
                {
                    name: '交易',
                    type: 'bar',
                    data: data.values
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    },'json');


    //饼状图
    $.get("/crm/workbench/chart/pieVoEcharts",function (data) {
        //data:PieVo
        var option = {
            title: {
                text: '网站数据统计',
                subtext: 'Fake Data',
                left: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left'
            },
            series: [
                {
                    name: 'Access From',
                    type: 'pie',
                    radius: '70%',
                    data: data,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 200,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, .7)'
                        }
                    }
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart2.setOption(option);
    },'json');

</script>
</body>
</html>
