var test = {
    title: '',
    page: "1",
    data: [],
    testdata: [],
    state: [],
    info: [],
    html: '',
    studentTotalList: [],
    examId: '',
    sysuserid: '',
    init: function () {
        commonet.init(); // 公共模块初始化

        var now = (new Date()).toLocaleString();
        $('.nowTime').text(now);
        setInterval(function () {
            now = (new Date()).toLocaleString();
            $('.nowTime').text(now);
        }, 1000);

        $(".examRoom").next(".treeview-menu").toggle("slow");
        $(".examRoom").addClass("leftActive");
        $(".examination").css("color", "white");
        $(".mytest").addClass("onet");

        test.getExamInfo();
        $(".drawQuestion").on('click', function () {
            test.examId = this.id;
            test.drawQuestion();
        });

        $.jqPaginator('#pagination', {
            totalCounts: test.count,
            visiblePages: 5,
            currentPage: 1,
            pageSize: 10,
            first: '<li class="first"><a href="javascript:;">首页</a></li>',
            last: '<li class="last"><a href="javascript:;">尾页</a></li>',
            page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
            onPageChange: function (num, type) {
                if (type == 'init') {
                    return;
                }
                test.page = num;
                test.getExamInfo();
            }
        });
    },
    getExamInfo: function () {
        patest.request({
            url: "../ep/examInfo/selectMyExam"
        }, {
            page: test.page,
            rows: "10"
        }, function (result) {
            test.count = result.data.total;
            test.data = result.data.examInfoList;
            test.state = result.data.statusList;
            test.prostate = result.data.proState;
            test.peopleTotal = result.data.peopleTotal;
            test.showInfo();
            $("#listInfo").empty();
            $("#listInfo").append(test.html);
        });
    },
    drawQuestion: function () {
        patest.request({
            url: "../ep/examPaper/drawProblem"
        }, {
            examId: test.examId
        }, function (result) {
            $(".loading").html("<img />");
            if (result.status > 0) {
                pubMeth.alertInfo("alert-success", "抽题成功！");
            } else {
                pubMeth.alertInfo("alert-danger", result.desc);
            }
            test.getExamInfo();
        });
    },
    showInfo: function () {
        var length = test.data.length;
        var order = 1;
        var stateInfo = "";
        var title = '';
        test.html = "";
        for (var i = 0; i < length; i++) {
            title = '<a  href="testInfo.html?id=' + test.data[i].examId + '">' + test.data[i].title + '</a>';
            if (test.state[i] == '2') {
                if (test.prostate[i] == '1') {
                    stateInfo = '已结束&nbsp;';
                } else if (test.prostate[i] == '0') {
                    stateInfo = '已结束 未抽题';
                }
            } else if (test.state[i] == '1') {
                if (test.prostate[i] == '1') {
                    stateInfo = "进行中";
                } else if (test.prostate[i] == '0') {
                    stateInfo = '进行中&nbsp;未抽题<a href="#" class="drawQuestion" id="' + test.data[i].examId + '">抽题</a>&nbsp;<span class="loading"><img /></span>';
                }
            } else if (test.state[i] == '0') {
                if (test.prostate[i] == '0') {
                    stateInfo = '未开始&nbsp;未抽题<a href="#" class="drawQuestion" id="' + test.data[i].examId + '">抽题</a>&nbsp;<span class="loading"><img /></span>';
                } else if (test.prostate[i] == '1') {
                    stateInfo = '未开始&nbsp;已抽题<a href="#" class="drawQuestion" id="' + test.data[i].examId + '">重新抽题</a>&nbsp;<span class="loading"><img /></span>';
                }
            }
            test.html += '<tr><td>' + order + '</td>'
                + '<td>' + title + '</td>'
                + '<td>' + test.data[i].startTime + '</td>'
                + '<td>' + test.data[i].endTime + '</td>'
                + '<td>' + test.peopleTotal[i] + '</td>'
                + '<td>' + stateInfo + '</td>'
                + '</tr>';
            order++;
        }
    }
};
