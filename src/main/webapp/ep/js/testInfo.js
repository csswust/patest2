var testInfo = {
    page: "1",
    examId: '',
    userId: '',
    username: '',
    submitInfoList: [],
    submitResultList: [],
    userInfoList: [],
    userProfileList: [],
    problemInfoList: [],
    init: function () {
        commonet.init(); // 公共模块初始化

        $(".mytest").addClass("onet");
        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss'
        });
        var parm = patest.getQueryObject();
        testInfo.examId = parm["id"];
        testInfo.getProblemList();

        $(".examTabs").click(function () {
            var id = this.id;
            if (id === "rank") {
                $(".submitInfo").hide();
                $(".quesList").hide();
                $(".online").hide();
                $(".rankInfo").show();
                testInfo.page = 1;
                testInfo.getRank();
            } else if (id === "quesList") {
                $(".submitInfo").hide();
                $(".rankInfo").hide();
                $(".online").hide();
                $(".quesList").show();
                $(".pagination").html("");
                testInfo.getProblemList();
            } else if (id === "online") {
                testInfo.page = 1;
                $(".submitInfo").hide();
                $(".rankInfo").hide();
                $(".quesList").hide();
                $(".online").show();
                testInfo.selectOnline();
            }
        });
        $(".gradePrint").click(function () {
            testInfo.importGradeByExamId(testInfo.examId);
        });
        $(".codePrint").click(function () {
            testInfo.importCodeByExamId(testInfo.examId);
        });
    },
    getProblemList: function () {
        patest.request({
            url: "../ep/examInfo/selectMyProblem"
        }, {
            examId: testInfo.examId
        }, function (result) {
            result = result.data;
            var html = "";
            $('#listInfo').empty();
            for (var i = 0, len = result.problemInfoList.length; i < len; i++) {
                var title = "", level = "", probId = "", acedNum = "", totalSubmit = "", createTime = "";
                if (result.problemInfoList[i] != null) {
                    title = result.problemInfoList[i].title;
                    level = result.problemInfoList[i].levelId;
                    if (level === 1) level = "简单";
                    else if (level === 2) level = "中等";
                    else if (level === 3) level = "困难";
                    probId = result.problemInfoList[i].probId;
                    acedNum = result.problemInfoList[i].acceptedNum;
                    totalSubmit = result.problemInfoList[i].submitNum;
                    createTime = result.problemInfoList[i].createTime
                }
                html += '<tr>' + '<td>' + probId + '</td>'
                    + '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + title + '"><a href="question.html?id=' + probId + '">' + title + '</a></td>';
                if (result.knowledgeInfoList[i] != null) {
                    html += '<td>'
                        + result.knowledgeInfoList[i].knowName
                        + '</td>';
                } else
                    html += '<td>' + 'null' + '</td>';
                html += '<td>' + level + '</td>' +
                    '<td>' + acedNum + '/' + totalSubmit + '</td>' +
                    '<td>' + createTime + '</td>' +
                    '<td>' + result.countList[i] + '</td>'
                    + '</tr>';
            }
            $('#listInfo').append(html);
        });
    },
    getRank: function () {
        patest.request({
            url: "../ep/examInfo/rankingByGrade"
        }, {
            examId: testInfo.examId,
            page: testInfo.page,
            rows: 10
        }, function (result) {
            result = result.data;
            testInfo.count = result.total;
            testInfo.problemTotal = result.problemTotal;
            if (testInfo.count <= 0) {
                $("#notip").modal('show');
            } else {
                var userName = "", stunum = "", titName = "", content = "", realName = "", className = "",
                    score = "", acedCount = "";
                var ranknav = '';
                for (var j = 1; j <= testInfo.problemTotal; j++) {
                    ranknav += '<th colspan="2">Q' + j + '</th>';
                }
                $(".rankInfo").html(
                    '<th class="examid">考号</th>' +
                    '<th>姓名</th>' +
                    '<th class="noid">学号</th>' +
                    '<th>班级</th>'
                    + '<th>分数</th><th>AC</th>'
                    + ranknav);
                for (var i = 0; i < result.examPaperList.length; i++) {
                    userName = result.userInfoList[i].username;
                    realName = result.userProfileList[i].realName;
                    className = result.userProfileList[i].className;
                    stunum = result.userProfileList[i].studentNumber;
                    titName = "";
                    for (var j = 0; j < testInfo.problemTotal; j++) {
                        if (result.PaperProblemList[i][j].isAced == 1) {
                            var pass = '<span class="glyphicon glyphicon-ok alert-success" aria-hidden="true">';
                        } else {
                            pass = '<span class="glyphicon glyphicon-remove alert-danger" aria-hidden="true"></span>';
                        }
                        var title = result.ProblemInfoList[i][j].title;
                        if (result.ProblemInfoList[i][j].title == null || result.ProblemInfoList[i][j].title == "undefined") {
                            title = "";
                        }
                        var probId = result.ProblemInfoList[i][j].probId;
                        titName += '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + title
                            + '"><a href="question.html?id=' + probId + '">' + title + '</a>'
                            + '</td>'
                            + '<td>' + pass + '</td>';

                    }
                    content += '<tr><td>' + userName + '</td><td>'
                        + realName + '</td>' + '<td>'
                        + stunum + '</td>' + '<td>'
                        + className + '</td><td>'
                        + result.examPaperList[i].score + '</td>'
                        + '<td>'
                        + result.examPaperList[i].acedCount
                        + '</td>' + titName + '</tr>';

                }
                $("#listInfo").html(content);
            }
        });
        if (testInfo.count > 0) {
            $.jqPaginator('#pagination', {
                totalCounts: testInfo.count,
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
                    testInfo.page = num;
                    testInfo.getRank();
                }
            });
        }
    },
    selectOnline: function () {
        patest.request({
            url: "../ep/examPaper/selectByCondition"
        }, {
            page: testInfo.page,
            rows: 10,
            examId: testInfo.examId,
            containOnline: true
        }, function (result) {
            testInfo.userProfileArr = result.data.userProfileList;
            testInfo.userArr = result.data.userInfoList;
            testInfo.examPaperList = result.data.examPaperList;
            testInfo.sessinoList = result.data.sessinoList;
            testInfo.count = result.data.total;
            testInfo.showOnline();
            $("#listInfo").empty();
            $("#listInfo").append(testInfo.ohtml);
        });
        if (testInfo.count > 0) {
            $.jqPaginator('#pagination', {
                totalCounts: testInfo.count,
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
                    testInfo.page = num;
                    testInfo.selectOnline();
                }
            });
        }
    },
    showOnline: function () {
        var length = testInfo.userArr.length;
        testInfo.ohtml = '';
        for (var i = 0; i < length; i++) {
            var order = i + 1;
            var loginStatus = '<td></td>';
            if (testInfo.sessinoList[i] === null) {
                loginStatus = '<td>未登录</td>';
            } else {
                loginStatus = '<td>已登录</td>';
            }
            testInfo.ohtml += '<tr>'
                + '<td>' + order + '</td>'
                + '<td>' + testInfo.userProfileArr[i].studentNumber + '</td>'
                + '<td>' + testInfo.userArr[i].username + '</td>'
                + '<td>' + testInfo.userProfileArr[i].realName + '</td>'
                + '<td>' + testInfo.userProfileArr[i].className + '</td>'
                + '<td>' + testInfo.examPaperList[i].classroom + '</td>'
                + loginStatus
                + '</tr>';
        }
    },
    importGradeByExamId: function (id) {
        patest.request({
            url: "../ep/examInfo/importGradeByExamId"
        }, {
            examId: id
        }, function (result) {
            if (result.status === 1) {
                testInfo.path = result.data.fileDir;
                window.location.href = '../system/download?path=' + testInfo.path;
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    importCodeByExamId: function (id) {
        patest.request({
            url: "../ep/examInfo/importCodeByExamId"
        }, {
            examId: id
        }, function (result) {
            if (result.status === 1) {
                testInfo.path = result.data.fileDir;
                window.location.href = '../system/download?path=' + testInfo.path;
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    }
};
