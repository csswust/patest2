$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".examList").css("color", "white");
$(".form_datetime").datetimepicker({
    format: 'yyyy-mm-dd hh:ii:ss'
});
var program = {
    title: '',
    page: '1',
    startTime: '',
    endTime: '',
    examId: '',
    courseName: '',
    data: [],
    html: '',
    KnowledgeData: null,
    peototal: [],
    getExamInfo: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                startTime: program.startTime,
                endTime: program.endTime,
                containUModify: true,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                program.count = result.total;
                program.data = result.examInfoList;
                program.peototal = result.peopleTotal;
                program.userProfileList = result.userProfileList;
                program.showInfo();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);
            }
        });
    },
    findCourseName: function (courseId) {
        var length = pubMeth.courseName.length;
        for (var i = 0; i < length; i++) {
            if (pubMeth.courseName[i].knowId == courseId) {
                program.courseName = pubMeth.courseName[i].knowName;
                break;
            }
        }
        return program.courseName;
    },
    showInfo: function () {
        var length = program.data.length;
        var order = 1;
        program.html = "";
        for (var i = 0; i < length; i++) {
            program.html += '<tr><td><input type="checkbox" value="' + program.data[i].examId + '" name="title"/></td>'
                + '<td>' + program.data[i].examId + '</td>'
                + '<td><a class="title" href="editExam.html?Id=' + program.data[i].examId + '">' + program.data[i].title + '</a></td>'
                + '<td>' + program.peototal[i] + '</td>'
                + '<td>' + program.data[i].startTime + '</td>'
                + '<td>' + program.data[i].endTime + '</td>'
                + '<td>' + program.userProfileList[i].realName + '</td>'
                + '</tr>';
            order++;
        }
    },
    deleteIt: function () {
        $('.delete').on('click', function (e) {
            var valArr = new Array;
            $(":checkbox[name='title']:checked").each(function (i) {
                valArr[i] = $(this).val();
            });
            var vals = valArr.join(',');// 转换为逗号隔开的字符串
            if (vals != "") {
                $("#modalexamdelete").modal(function () {
                    backdrop : 'static'
                });
                $(".examquess").html(vals);
                $(".examdelete").click(function () {
                    $.ajax({
                        type: "post",
                        content: "application/x-www-form-urlencoded;charset=UTF-8",
                        url: "../examInfo/deleteByIds",
                        dataType: 'json',
                        async: false,
                        data: {
                            ids: vals
                        },
                        success: function (result) {
                            if (result.status == 1) {
                                pubMeth.alertInfo("alert-success", "删除成功！");
                                program.getExamInfo();
                                $("#modalexamdelete").modal('hide');
                            } else {
                                pubMeth.alertInfo("alert-danger", "删除失败！");
                            }
                        }
                    });
                });
            } else {
                pubMeth.alertInfo("alert-info", "请先勾选删除项！");
            }
        });
    },
    drawQuestion: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../problem/drawProblem",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId
            },
            success: function (result) {
                console.log(result);
                if (result.status == "-3") {
                    pubMeth.alertInfo("alert-danger", "缺少用户名单！");
                }
                else if (result.status == "-1") {
                    pubMeth.alertInfo("alert-danger", "参数异常！");
                }
                else if (result.status == "-4") {
                    pubMeth.alertInfo("alert-danger", "缺少题目模板！");
                }
                else if (result.status == "-5") {
                    pubMeth.alertInfo("alert-danger", "模板的重复次数大于满足条件的题目数目！");
                }
                else if (result.status == "-2") {
                    pubMeth.alertInfo("alert-danger", "该场考试不存在！");
                }
            }
        });
    }
};

pubMeth.getRowsnum("rowsnum");
pubMeth.serCourse();
program.getExamInfo();
program.deleteIt();

$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    program.startTime = $(".startTime").val();
    program.endTime = $(".endTime").val();
    if (program.startTime == "" || program.endTime == "") {
        pubMeth.alertInfo("alert-info", "请输入查询日期范围");
    } else {
        if (pubMeth.legTimeRange(program.startTime, program.endTime)) {
            program.getExamInfo();
            $(".countnum").html(program.count);
            $('#pagination').jqPaginator('option', {
                totalCounts: program.count
            });
        } else {
            pubMeth.alertInfo("alert-info", "开始日期不能大于结束日期");
        }
    }
});
$(".addExam").click(function () {
    window.location.href = "editExam.html";
});
$(".drawQuestion").on('click', function () {
    program.examId = this.id;
    program.drawQuestion();
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