$(".sysMana").next(".treeview-menu").toggle("slow");
$(".sysMana").addClass("leftActive");
$(".monitor").css("color", "white");
$(".footer p").css('display', 'block');
$(".footer p").hover(function () {
    $(this).css({"color": "#333", "cursor": "default"});
});

var map = {};

var program = {
    number: 200,
    timeUnit: 5,
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
        program.keyList = program.data.keyList;
        program.valueList = program.data.valueList;
        var len = program.keyList.length;
        for (var i = 0; i < len; i++) {
            program.showMonitor(i);
        }
    },
    showMonitor: function (index) {
        var key = program.keyList[index].key;
        var title = program.keyList[index].title;
        $("#monitor").append('<div id="' + key + '" style="height:300px;"></div>');
        var list = program.valueList[index];
        if (!list) return;
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById(key));
        map[key] = myChart;
        var type = [], value = [];
        for (var i = 0; i < list.length; i++) {
            type.push(list[i].date.split(" ")[1]);
            value.push(list[i].data);
        }
        // 指定图表的配置项和数据
        var option = {
            color: "#70BAE1",
            title: {
                text: title
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
                lineStyle: {},
                areaStyle: {}
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
    },
    load: function () {
        program.number = $(".snumber").val();
        program.timeUnit = $(".stimeUnit").val();
        program.selectMonitor();
        program.keyList = program.data.keyList;
        program.valueList = program.data.valueList;
        var len = program.keyList.length;
        for (var i = 0; i < len; i++) {
            var key = program.keyList[i].key;
            var myChart = map[key];
            var option = myChart.getOption();

            var list = program.valueList[i];
            if (!list) return;
            var type = [], value = [];
            for (var j = 0; j < list.length; j++) {
                type.push(list[j].date.split(" ")[1]);
                value.push(list[j].data);
            }
            option.series[0].data = value;
            option.xAxis[0].data = type;
            myChart.setOption(option);
        }
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
setInterval(program.load, 5000);