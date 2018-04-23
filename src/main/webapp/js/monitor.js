$(".sysMana").next(".treeview-menu").toggle("slow");
$(".sysMana").addClass("leftActive");
$(".monitor").css("color", "white");

var program = {
    selectMonitor: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../system/monitor",
            dataType: 'json',
            async: false,
            data: {
                number: 10,
                timeUnit: 20
            },
            success: function (result) {
                program.data = result.data;
            }
        });
    },
    showMonitor: function (key) {
        var list = program.data[key];
        if (!list) return;
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        var type = [], value = [];
        for (var i = 0; i < list.length; i++) {
            type.push(list[i].date.split(" ")[1]);
            value.push(list[i].data);
        }
        // 指定图表的配置项和数据
        var option = {
            xAxis: {
                type: 'category',
                data: type
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: value,
                type: 'line'
            }]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
};
/*pubMeth.getRowsnum("rowsnum");*/
program.selectMonitor();
program.showMonitor("userInfoSelect");