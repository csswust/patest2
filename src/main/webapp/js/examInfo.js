$(".examRoom").next(".treeview-menu").toggle("slow");
$(".examRoom").addClass("leftActive");
$(".examination").css("color", "white");
$(".quesList").addClass("active");

var program = {
    page: "1",
    examId: '',
    userId: '',
    username: '',
    count: '',
    html: '',
    submitInfoList: [],
    userInfoList: [],
    userProfileList: [],
    problemInfoList: [],
    getProblemList: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/selectMyProblem",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId
            },
            success: function (result) {
                result = result.data;
                console.log(result);
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
            },
            error: function () {
            }
        });
    },
};
var parm = pubMeth.getQueryObject();
program.examId = parm["id"];
program.getProblemList();

/*    $(".gradePrint").click(function () {
 //		program.selectGradeByExamId();
 window.location.href = "../exam/selectGradeByExamId?examId=" + program.examId;
 });
 $(".codePrint").click(function () {
 window.location.href = "../exam/selectCodeByExamId?examId=" + program.examId;
 });*/
//	program.getSubmitInfo();
console.log(program.count);
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
            program.getSubmitInfo();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}