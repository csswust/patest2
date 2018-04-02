$(".examRoom").next(".treeview-menu").toggle("slow");
$(".examRoom").addClass("leftActive");
$(".examination").css("color", "white");

var program = {
    examId: '',
    userId: [],
    exaPapId: [],
    probIdArr: [],
    index: 0,
    page: '1',
    anthercode: '',
    codeList: [],
    realName: '',
    userName: '',
    submIds: [],
    username: '',
    similaryList: [],
    similaryTotal: 0,
    score: '',
    rscore: '',
    html: '',
    count: '',
    Mycode: '',
    paprId: '',
    probId: '',
    prproId: '',
    probIds: [],
    prproIds: [],
    oldIndex: 0,
    getExamInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId,
                onlyExamInfo: true
            },
            success: function (result) {
                console.log(result);
                $(".pageName").text(result.examInfoList[0].title);
            }
        });
    },
    getAllExamPaper: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId,
                onlyPaper: true
            },
            success: function (result) {
                console.log(result);
                program.count = result.total;
                for (var i = 0; i < result.examPaperList.length; i++) {
                    program.exaPapId[i] = result.examPaperList[i].exaPapId;
                }
            }
        });
    },
    selectPaperById: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/selectPaperById",
            dataType: 'json',
            async: false,
            data: {
                exaPapId: program.exaPapId[program.index]
            },
            success: function (result) {
                console.log(result);
                var length = result.paperProblemList.length;
                program.paprId = result.paperProblemList[0].papProId;
                program.paperProblemList = result.paperProblemList;
                program.problemInfoList = result.problemInfoList;
                program.codeList = result.submitInfoList;
                program.score = result.examPaper.score;
                program.userName = result.userInfo.username;
                program.realName = result.userProfile.realName;
                program.showPaper();
                $(".nav-tabs-custom").empty();
                $(".nav-tabs-custom").append(program.html);
            }
        });
    },
    safeStr: function (str) {
        if (!str) return "";
        return str.replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, "&quot;").replace(/'/g, "&#039;");
    },
    showPaper: function () {
        program.html = "";
        var length = program.codeList.length;
        var tmp = length - 1;
        var flag = length;
        var tab_nav = "", content = "", liActive, divAcitve;
        for (var i = length - 1; i >= 0; i--) {
            if (tmp == program.oldIndex) {
                // 设置为active
                liActive = '<li id="' + tmp + '-' + tmp + '"  class="active">';
                divAcitve = '<div id="Q_' + flag + '" class="tab-pane active">';
            } else {
                liActive = '<li id="' + tmp + '-' + tmp + '">';
                divAcitve = '<div id="Q_' + flag + '" class="tab-pane">';
            }
            tab_nav += '' + liActive + '<a href="#Q_' + flag
                + '" data-toggle="tab" onclick="aClick()">Q' + flag + '：'
                + program.paperProblemList[i].score + '</a></li>';
            program.probIds[i] = program.problemInfoList[i].probId;
            program.prproIds[i] = program.paperProblemList[i].papProId;

            var source = program.safeStr(program.codeList[i].source);
            content += '' + divAcitve
                + '<h2 style="text-align:center;">'
                + program.problemInfoList[i].title
                + '</h2>'
                + '<div style="margin:0 auto;width:800px;"><ul class="nav-pills nav-justified" style="text-align:center;color:#3c8dbc;">'
                + '<li>提交时间:' + program.paperProblemList[i].lastSubmitTime
                + '</li><li>时间:' + program.paperProblemList[i].usedTime
                + '</li>' + '<li>状态:' + program.codeList[i].status
                + '</li><li>分数:' + program.paperProblemList[i].score
                + '</li></ul></div>' + '<div class="allcon"><div class="changed"><pre class="prettyprint linenums pre-scrollable">'
                + source + '</pre></div><div class="addcss"><pre class="addchanged"></pre></div></div></div>';
            flag--;
            tmp--;
        }
        program.html += '<ul class="nav nav-tabs pull-right">'
            + tab_nav
            + '<li class="pull-left">'
            + '<span class="glyphicon glyphicon-bullhorn" aria-hidden="true"></span>'
            + '' + program.userName + '(' + program.realName
            + ')</li></ul>' + '<div class="tab-content">' + content
            + '</div>';
        $(".pull-right").find("li").eq(5).addClass("active");
    },
    updateScore: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../paperProblem/updateById",
            dataType: 'json',
            async: false,
            data: {
                papProId: program.prproId,
                score: program.rscore
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    program.selectPaperById();
                } else {
                    alert("提交失败");
                }
            },
            error: function (result) {
                alert("请求失败");
            }
        });
    },

    selectSimilary: function (temp) {
        console.log(program.codeList[temp].submId);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../submitSimilarity/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                rows: pubMeth.rowsnum,
                submitId1: program.codeList[temp].submId
            },
            success: function (result) {
                console.log(result);
                program.similaryList = result.submitSimilarityList;
                program.similaryTotal = result.total;
                program.userInfoList = result.userInfoList2;
                program.userProfileList = result.userProfileList2;
                program.submitInfoList2 = result.submitInfoList2;
                console.log(program.similaryTotal);
                if ($("#problem_similarity").css("display") == 'none') {
                    $("#problem_similarity").css("display", "block");
                }
                program.showSimilaryInfo();
                $("#problemlistInfo").empty();
                $("#problemlistInfo").append(program.html);
            },
            error: function () {
                alert("相似度检验失败！");
            }
        });
    },
    getSimByProbId: function (temp) {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../submitSimilarity/getSimByProbId",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId,
                probId: program.codeList[temp].problemId
            },
            success: function (result) {
                console.log(result);
                if (result.status <= 0 && result.status !== -2) {
                    $("#tip").modal({
                        backdrop: 'static'
                    });
                } else {
                }
            },
            error: function () {
                alert("相似度检验失败！");
            }
        });
    },
    showSimilaryInfo: function () {
        var order = 1;
        var flag = 0;
        program.html = "";
        for (var i = 0; i < program.similaryList.length; i++) {
            program.html += '<tr><td>' + order + '</td>'
                + '<td>' + program.userInfoList[i].username + '</td>'
                + '<td>' + program.userProfileList[i].realName + '</td>'
                + '<td>' + program.submitInfoList2[i].ip + '</td>'
                + '<td>' + program.similaryList[i].similarity + '</td>'
                + '<td class="search" id="' + flag + '-' + flag + '"><button class="btn btn-info resource">source</button></td>'
                + '</tr>';
            program.submIds[i] = program.submitInfoList2[i].submId;
            order++;
            flag++;
        }
    },
    showMyCode: function (temp) {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../submitInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                submId: program.submId,
                onlySubmitInfo: true
            },
            success: function (result) {
                console.log(result);
                program.Mycode = program.safeStr(result.submitInfoList[0].source);
                $(".allcon").css({
                    "width": "100%",
                    "margin": "0 auto",
                    "overflow": "auto",
                    "zoom": " 1",
                });
                $(".changed").addClass("leftdiv");
                $(".addchanged").addClass("prettyprint linenums pre-scrollable");
                $(".addchanged").empty();
                if ($(".addchanged").css("display") == 'none') {
                    $(".addchanged").css("display", "block");
                }
                $(".addchanged").append(program.Mycode);
                $(".addcss").addClass("rightdiv");
                $(".addcss pre").css({"padding": "9.5px"});
            },
            error: function () {

            }
        });
    },

    lastInfo: function () {
        program.index--;
        if (program.index < 0) {
            alert("已经是第一个了！");
            program.index = 0;
            return false;
        }
        program.selectPaperById();
    },
    nextInfo: function () {
        program.index++;
        if (program.index >= program.count) {
            alert("已经是最后一个了！");
            program.index = program.count - 1;
            return false;
        }
        program.selectPaperById();
    }
};
pubMeth.getRowsnum("rowsnum");
var par = pubMeth.getQueryObject();
program.examId = par.id;
program.getExamInfo();
program.getAllExamPaper();
program.selectPaperById();

$(".similarity").click(function () {
    var str = $(".tab-pane.active").attr("id").split("_");
    var number = program.codeList.length - parseInt(str[str.length - 1]);
    if (!program.codeList[number].problemId) {
        $("#tip").modal({
            backdrop: 'static'
        });
        return;
    }
    program.getSimByProbId(number);
    program.selectSimilary(number);
    if (program.similaryTotal > 0) {
        $.jqPaginator('#pagination', {
            totalCounts: program.similaryTotal,
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
                console.log(number);
                program.selectSimilary(number);
            }
        });
    }

});
$(".nextInfo").click(function () {
    if ($("#problem_similarity").css("display") == 'block') {
        $("#problem_similarity").css("display", "none");
    }
    program.nextInfo();
});
$(".lastInfo").click(function () {
    if ($("#problem_similarity").css("display") == 'block') {
        $("#problem_similarity").css("display", "none");
    }
    program.lastInfo();
});

$(".submit").click(function () {
    program.rscore = $("#rscore").val();
    var index = $(".nav-tabs-custom ul li.active").attr("id").split("-")[0];
    program.oldIndex = index;
    program.prproId = program.prproIds[index];
    console.log(program.prproId);
    program.updateScore();
});

$("#problemlistInfo").on('click', '.search', function () {
    var str = $(".tab-pane.active").attr("id").split("_");
    var number = program.codeList.length - parseInt(str[str.length - 1]);
    var index = this.id.split("-")[0];
    program.submId = program.submIds[index];
    program.showMyCode(number);
});
function aClick() {
    if ($(".addchanged").css("display") == 'block') {
        $(".addchanged").css("display", "none");
    }
    $(".changed").removeClass("leftdiv");
    if ($("#problem_similarity").css("display") == 'block') {
        $("#problem_similarity").css("display", "none");
    }
};