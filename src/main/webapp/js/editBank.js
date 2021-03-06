$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".examList").css("color", "white");
$(".stbank").addClass("on");
var flag = 0;
var program = {
    page: '1',
    count: '',
    html: '',
    tempProArr: [],
    probArr: [],
    knowNameArr: [],
    couseNamesArr: [],
    levelArr: [],
    examId: null,
    expmId: '',
    tempProbIds: [],
    tempProbId: '',
    showproinfo: function () {
        var length = program.probArr.length;
        program.html = "";
        for (var i = 0; i < length; i++) {
            var level;
            if (program.probArr[i].levelId == 1) {
                level = '容易';
            } else if (program.probArr[i].levelId == 2) {
                level = '中等';
            } else if (program.probArr[i].levelId == 3) {
                level = '困难';
            }
            program.html += '<tr class="' + flag + '-' + flag + '">'
                + '<td style="width:80px;"><input type="checkbox" value="' + program.tempProArr[i].exaProId + '" name="title"/></td>'
                + '<td>' + program.tempProArr[i].exaProId + '</td>'
                + '<td>' + program.probArr[i].probId + '</td>'
                + '<td><a href="question.html?id=' + program.probArr[i].probId + '"  class="title">' + program.probArr[i].title + '</a></td>'
                + '<td>' + level + '</td>'
                + '<td>' + program.couseNamesArr[i].courseName + '</td>'
                + '<td>' + program.knowNameArr[i].knowName + '</td>'
                + '</tr>';
            program.tempProbIds[i] = program.tempProArr[i].exaProId;
            flag++;
        }
    },
    //通过考试Id来展示本场考试
    selectByExamId: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examProblem/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                program.tempProArr = result.data.examProblemList;
                program.probArr = result.data.problemInfoList;
                program.knowNameArr = result.data.knowledgeInfoList;
                program.couseNamesArr = result.data.courseInfoList;
                program.count = result.data.total;
                program.showproinfo();
                $("#listInfo").html("");
                $("#listInfo").append(program.html);
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    //通过问题Id来删除题目
    deleteTempProblemById: function (vals) {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examProblem/deleteByIds",
            dataType: 'json',
            async: false,
            data: {
                ids: vals
            },
            success: function (result) {
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "删除成功");
                    flag = 0;
                    program.selectByExamId();
                } else {
                    pubMeth.alertInfo("alert-danger", "删除失败")
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求失败");
            }
        });
    }
};
pubMeth.getRowsnum("rowsnum");
pubMeth.serCourse();
//解析url
var par = pubMeth.getQueryObject();
//修改考试
if (par.examId) {
    $(".pageName").text("修改考试");
    program.examId = par.examId;
    program.selectByExamId();
}
$(".localBank").click(function () {
    if (par.examId) {
        window.location.href = "probank.html?examId=" + program.examId;
    }
});
$(".upBank").click(function () {
    if (par.examId) {
        window.location.href = "editExam.html?examId=" + program.examId;
    }
});
$(".downBank").click(function () {
    if (par.examId) {
        window.location.href = "editParm.html?examId=" + program.examId;
    }
});
$(".deletePro").click(function () {
    var valArr = new Array;
    $(":checkbox[name='title']:checked").each(function (i) {
        valArr[i] = $(this).val();
    });
    var vals = valArr.join(',');// 转换为逗号隔开的字符串
    if (vals != "") {
        program.deleteTempProblemById(vals);
    } else {
        pubMeth.alertInfo("alert-info", "请先勾选删除项！");
    }
});
$("#listInfo").on('click', '.deletepro', function () {
    flag--;
    var banklength = program.tempProbIds.length;
    var index = this.id.split("-")[0];
    if (parseInt(index) + 1 > banklength) {
        $("." + this.id).remove();
    } else {
        if (confirm("确定要删除该题吗？")) {
            var index = this.id.split("-")[0];
            program.tempProbId = program.tempProbIds[index];
            program.deleteTempProblemById();
        }
    }
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
            program.selectByExamId();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}
