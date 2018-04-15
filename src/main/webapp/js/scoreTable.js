$(".examRoom").next(".treeview-menu").toggle("slow");
$(".examRoom").addClass("leftActive");
$(".examination").css("color", "white");
$(".rank").addClass("active");

var program = {
    page: "1",
    examId: '',
    userId: '',
    username: '',
    count: '',
    html: '',
    ohtml: '',
    examNum: '',
    userName: '',
    stuNum: '',
    className: '',
    getScore: '',
    ACnum: '',
    problemTitle: '',
    problemTotal: '',
    submitInfoList: [],
    userInfoList: [],
    userProfileList: [],
    problemInfoList: [],
    oClassNameList: [],
    oipList: [],
    orealNameList: [],
    ostuNum: [],
    ouserNameList: [],
    getRank: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/rankingByGrade",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId,
                page: program.page,
                rows: pubMeth.rowsnum,
                userName: program.susername,
                studentNumber: program.sstudentNumber
            },
            success: function (result) {
                console.log(result);
                program.count = result.total;
                program.problemTotal = result.problemTotal;
                if (program.count <= 0) {
                    $("#notip").modal('show');
                } else {
                    var userName = "", stunum = "", titName = "", content = "", realName = "", className = "",
                        score = "", acedCount = "";
                    var ranknav = '';
                    for (var j = 1; j <= program.problemTotal; j++) {
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
                        for (var j = 0; j < program.problemTotal; j++) {
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
            }
        });
    },
};
pubMeth.getRowsnum("rowsnum");
var parm = pubMeth.getQueryObject();
program.examId = parm["id"];
program.getRank();
$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    program.susername = $(".susername").val();
    program.sstudentNumber = $(".sstudentNumber").val();
    program.getRank();
    $(".countnum").html(program.count);
    $('#pagination').jqPaginator('option', {
        totalCounts: program.count
    });
});
if (program.count > 0) {
    $(".countnum").html(program.count);
    $.jqPaginator('#paginationGet', {
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
            program.getRank();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}