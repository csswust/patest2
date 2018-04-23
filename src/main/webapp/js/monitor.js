$(".sysMana").next(".treeview-menu").toggle("slow");
$(".sysMana").addClass("leftActive");
$(".monitor").css("color", "white");
$(".footer p").css('display', 'block');
$(".footer p").hover(function () {
    $(this).css({"color": "#333", "cursor": "default"});
});

var program = {
    number: 20,
    timeUnit: 20,
    selectMonitor: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../system/monitor",
            dataType: 'json',
            async: false,
            data: {
                number: program.number,
                timeUnit: program.timeUnit
            },
            success: function (result) {
                program.data = result.data;
            }
        });
    },
    showAll: function () {
        for (var i in program.data) {
            var list = program.data[i];
            if (i.indexOf("_monitorKey") !== -1) continue;
            program.showMonitor(i);
        }
    },
    showMonitor: function (key) {
        $("#monitor").append('<div id="' + key + '" style="height:300px;"></div>');
        var list = program.data[key];
        if (!list) return;
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById(key));
        var type = [], value = [];
        for (var i = 0; i < list.length; i++) {
            type.push(list[i].date.split(" ")[1]);
            value.push(list[i].data);
        }
        // 指定图表的配置项和数据
        var option = {
            color: "#70BAE1",
            title: {
                text: program.data[key + "_monitorKey"].title
            },
            tooltip: {},
            xAxis: {
                type: 'category',
                data: type,
                boundaryGap: false
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: value,
                type: 'line',
                smooth: true,
                lineStyle: {
                    color: "#70BAE1"
                },
                areaStyle: {
                    color: "#70BAE1"
                }
            }]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    },
    refresh: function () {
        $("#monitor").html("");
        program.number = $(".snumber").val();
        program.timeUnit = $(".stimeUnit").val();
        program.selectMonitor();
        program.showAll();
    }
};
var par = pubMeth.getQueryObject();
if (par.number) {
    program.number = par.number;
}
if (par.timeUnit) {
    program.timeUnit = par.timeUnit;
}
$(".snumber").val(program.number);
$(".stimeUnit").val(program.timeUnit);
program.selectMonitor();
program.showAll();
$(".search").click(function () {
    program.refresh();
});
$(".timeUpdate").click(function () {
    var value = this.value;
    $(".stimeUnit").val(value);
    program.refresh();
});