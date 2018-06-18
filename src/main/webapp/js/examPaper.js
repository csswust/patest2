$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".paperInfo").css("color", "white");
var program = {
    page: "1",
    count: "",
    expIds: '',
    searExamID: '',
    searuserName: '',
    studentNumber: '',
    examId: null,
    getExam: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                onlyExamInfo: true
            },
            success: function (result) {
                var examNameList = result.data.examInfoList;
                for (var i = 0; i < examNameList.length; i++) {
                    (function (num) {
                        $(".examId").append('<option value="' + examNameList[i].examId + '">' + examNameList[i].title + '</option>');
                    })(i);
                }
            }
        });
    },
    getPaper: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId,
                studentNumber: program.studentNumber,
                userName: program.searuserName,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                program.count = result.data.total;
                console.log(result);
                var rander = template("getcontent", result.data);
                $("#listInfo").html(rander);
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
                    program.getPaper();
                }
            });
        } else {
            $(".pagenum").css("display", "none");
        }
    },
    deletePerExam: function () {
        $(".delete").click(function () {
            var valArr = new Array;
            $(":checkbox[name='title']:checked").each(function (i) {
                valArr[i] = $(this).val();
            });
            program.expIds = valArr.join(',');
            if (program.expIds != "") {
                if (confirm("你确定要删除这些" + program.expIds + "试卷吗？")) {
                    $.ajax({
                        type: "get",
                        content: "application/x-www-form-urlencoded;charset=UTF-8",
                        url: "../examPaper/deleteByIds",
                        dataType: 'json',
                        async: false,
                        data: {
                            ids: program.expIds
                        },
                        success: function (result) {
                            if (result.status > 0) {
                                pubMeth.alertInfo("alert-success", "删除成功！");
                                program.getPaper();
                            } else {
                                pubMeth.alertInfo("alert-danger", "删除失败！");
                            }
                        }
                    });
                }
            } else {
                pubMeth.alertInfo("alert-info", "请先勾选删除项！");
            }

        });
    },
    ser: function () {
        $(".search").click(function () {
            program.page = 1;
            $('#pagination').jqPaginator('option', {
                currentPage: program.page
            });
            program.studentNumber = $(".searTitle").val();
            program.examId = $(".searExamName").val();
            program.searuserName = $(".searuserName").val();
            console.log(program.studentNumber);
            program.getPaper();
            $(".countnum").html(program.count);
            $('#pagination').jqPaginator('option', {
                totalCounts: program.count
            });
        });
    }
};
pubMeth.getRowsnum();
//program.getExam();
program.deletePerExam();
program.ser();
var param = pubMeth.getQueryObject();
if (param.studentNumber) {
    program.studentNumber = param.studentNumber;
}
program.getPaper();