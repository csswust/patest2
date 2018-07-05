var now = (new Date()).toLocaleString();
$('.nowTime').text(now);
setInterval(function () {
    now = (new Date()).toLocaleString();
    $('.nowTime').text(now);
}, 1000);
$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".examination").css("color", "white");

var program = {
    title: '',
    page: "1",
    data: [],
    state: [],
    prostate: [],
    html: '',
    studentTotalList: [],
    examId: '',
    getExamInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                program.count = result.data.total;
                program.data = result.data.examInfoList;
                program.state = result.data.statusList;
                program.prostate = result.data.proState;
                program.studentTotalList = result.data.peopleTotal;
                program.showInfo();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);
            }
        });
    },
    drawQuestion: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/drawProblem",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId
            },
            success: function (result) {
                console.log(result);
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "抽题成功！");
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
                program.getExamInfo();
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误！");
            }
        });
    },
    showInfo: function () {
        var length = program.data.length;
        var stateInfo = "";
        var title = '';
        program.html = "";
        for (var i = 0; i < length; i++) {
            title = '<a  href="examInfo.html?id=' + program.data[i].examId + '">' + program.data[i].title + '</a>';
            if (program.state[i] === 2) {
                if (program.data[i].isDrawProblem === 1) {
                    stateInfo = '已结束&nbsp;<a href="remark.html?id=' + program.data[i].examId + ' "class="markPaper">阅卷</a>';
                    if (program.data[i].isSimTest === 0) {
                        stateInfo += '&nbsp;<a href="#" id=' + program.data[i].examId + ' class="simTest">检测相似度</a>';
                    }
                } else if (program.data[i].isDrawProblem === 0) {
                    stateInfo = '已结束 未抽题';
                }
            } else if (program.state[i] === 1) {
                if (program.data[i].isDrawProblem === 1) {
                    stateInfo = "进行中";
                } else if (program.data[i].isDrawProblem === 0) {
                    stateInfo = '进行中&nbsp;未抽题&nbsp;<a href="#" class="drawQuestion" id="' + program.data[i].examId + '">抽题</a>&nbsp;<span class="loading"><img /></span>';
                }
            } else if (program.state[i] === 0) {
                if (program.data[i].isDrawProblem === 0) {
                    stateInfo = '未开始&nbsp;未抽题&nbsp;<a href="#" class="drawQuestion" id="' + program.data[i].examId + '">抽题</a>&nbsp;<span class="loading"><img /></span>';
                } else if (program.data[i].isDrawProblem === 1) {
                    stateInfo = '未开始&nbsp;已抽题&nbsp;<a href="#" class="drawQuestion" id="' + program.data[i].examId + '">重新抽题</a>&nbsp;<span class="loading"><img /></span>';
                }
            }
            program.html += '<tr><td>' + program.data[i].examId + '</td>'
                + '<td>' + title + '</td>'
                + '<td>' + program.data[i].startTime + '</td>'
                + '<td>' + program.data[i].endTime + '</td>'
                + '<td>' + program.studentTotalList[i] + '</td>'
                + '<td>' + stateInfo + '</td>'
                + '</tr>';
        }
    },
    loadStatus: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../submitSimilarity/getSimStatus",
            dataType: 'json',
            async: true,
            data: {
                examId: program.statusId
            },
            success: function (result) {
                console.log(result);
                if (result.status === 0) {
                    pubMeth.alertInfo("alert-info", "运行中，进度：" + result.data.schedule);
                    setTimeout(program.loadStatus, 500);
                } else if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", result.desc);
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误！");
            }
        });
    },
    simTest: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../submitSimilarity/getSimByExamId",
            dataType: 'json',
            async: true,
            data: {
                examId: program.examId
            },
            success: function (result) {
                console.log(result);
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", result.desc);
                    program.statusId = program.examId;
                    setTimeout(program.loadStatus, 500);
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误！");
            }
        });
    }
};
pubMeth.getRowsnum("rowsnum");
program.getExamInfo();

$("#listInfo").on('click', '.drawQuestion', function () {
    console.log(this.id);
    program.examId = this.id;
    program.drawQuestion();
    return false;
});
$("#listInfo").on('click', '.simTest', function () {
    console.log(this.id);
    program.examId = this.id;
    program.simTest();
});

if (program.count > 0) {
    $(".countnum").html(program.count);
    $.jqPaginator('#pagination', {
        totalCounts: program.count,
        visiblePages: 5,
        currentPage: 1,
        pageSize: parseInt(pubMeth.rowsnum),
        first: '<li class="first"><a href="javascript:;">首页</a></li>',
        last: '<li class="last"><a href="javascript:;">尾页</a></li>',
        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
        onPageChange: function (num, type) {
            if (type == 'init') {
                return;
            }
            program.page = num;
            program.getExamInfo();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}