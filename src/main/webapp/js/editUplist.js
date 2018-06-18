$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".examList").css("color", "white");
$(".stlist").addClass("on");

var program = {
    page: '1',
    examId: null,
    html: '',
    examPaper: '',
    userInfo: '',
    userProfile: '',
    count: '',
    path: '',
    fileName: '',
    expmArr: '',
    expmIds: [],
    expmId: '',
    showInfo: function () {
        var length = program.userInfo.length;
        program.html = "";
        for (var i = 0; i < length; i++) {
            var order = i + 1;
            program.html += '<tr>'
                + '<td style="width:80px;"><input type="checkbox" value="' + program.examPaper[i].exaPapId + '" name="title"/></td>'
                + '<td>' + program.examPaper[i].exaPapId + '</td>'
                + '<td>' + program.userProfile[i].realName + '</td>'
                + '<td>' + program.userProfile[i].studentNumber + '</td>'
                + '<td><a href="showPaper.html?exaPapId=' + program.examPaper[i].exaPapId + '" class="title">' + program.userInfo[i].username + '</a></td>'
                + '<td>' + program.userProfile[i].className + '</td>'
                + '<td>' + program.examPaper[i].classroom + '</td>'
                + '</tr>';
        }
    },
    addPerExam: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/insertOne",
            dataType: 'json',
            async: false,
            data: {
                studentNumber: program.istudentNumber,
                password:program.ipassword,
                examId: program.examId
            },
            success: function (result) {
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "添加成功！");
                    program.selectUserBaseInfo();
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }
        });
    },
    //上传学生名单
    importList: function () {
        $.ajaxFileUpload({
            url: "../examPaper/uploadUserByExamId",
            secureuri: false,
            fileElementId: "namefile",// 文件选择框的id属性
            dataType: "json",
            data: {
                examId: program.examId
            },
            success: function (result) {
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "上传成功");
                    program.path = result.data.dirPath;
                    window.location.href = '../system/download?path=' + program.path;
                    program.selectUserBaseInfo();
                } else {
                    if (result.status !== -200) {
                        pubMeth.alertInfo("alert-danger", result.desc);
                        return;
                    }
                    var errorInfo = "";
                    var digitList = result.data.numberDigitErrorList;
                    var existList = result.data.numberExistErrorList;
                    if (digitList) {
                        errorInfo += "下列学号小于4位：";
                        for (var i = 0; i < digitList.length; i++) {
                            errorInfo += digitList[i] + ", ";
                        }
                    }
                    if (existList) {
                        errorInfo += "下列学号对应学生不存在（请在学生信息管理添加）：";
                        for (var i = 0; i < existList.length; i++) {
                            errorInfo += existList[i] + ", ";
                        }
                    }
                    pubMeth.alertInfo("alert-danger", errorInfo);
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "上传失败");
            }
        });
    },
    selectUserBaseInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                program.count = result.data.total;
                program.examPaper = result.data.examPaperList;
                program.userInfo = result.data.userInfoList;
                program.userProfile = result.data.userProfileList;
                program.showInfo();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);
                program.pagingFun();
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求失败");
            }
        });
    },
    //通过问题Id来删除题目
    deletePaperById: function (vals) {
        if (confirm("你确定要删除这些" + vals + "试卷吗？")) {
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examPaper/deleteByIds",
                dataType: 'json',
                async: false,
                data: {
                    ids: vals
                },
                success: function (result) {
                    if (result.status > 0) {
                        pubMeth.alertInfo("alert-success", "删除成功");
                        program.selectUserBaseInfo();
                    } else {
                        pubMeth.alertInfo("alert-danger", "删除失败")
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                }
            });
        }
    },
    //通过问题Id来删除题目
    deleteAllByExamId: function () {
        if (confirm("你确定要删除所有试卷吗？此操作有风险")) {
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examPaper/deleteAllByExamId",
                dataType: 'json',
                async: false,
                data: {
                    examId: program.examId
                },
                success: function (result) {
                    if (result.status > 0) {
                        pubMeth.alertInfo("alert-success", "删除成功");
                        program.selectUserBaseInfo();
                    } else {
                        pubMeth.alertInfo("alert-danger", "删除失败")
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                }
            });
        }
    },
    pagingFun: function () {
        if (program.count > 0) {
            $(".pagenum").css("display", "block");
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
                    program.selectUserBaseInfo();
                }
            });
        } else {
            $(".pagenum").css("display", "none");
        }
    },
    change: function (tagert, className) {
        $(tagert).change(function () {
            var path = $(this).val();
            var path1 = path.lastIndexOf("\\");
            var name = path.substring(path1 + 1);
            $(className).val(name);
        });
    }
};
pubMeth.getRowsnum("rowsnum");
var par = pubMeth.getQueryObject();
if (par.examId) {
    $(".pageName").text("修改考试");
    program.examId = par.examId;
}

program.selectUserBaseInfo();
//上传学生名单
program.pagingFun();
$(".importList").click(function () {
    $(".namefile").val("");
    $('#import').modal({});
    program.change("input[type=file]", ".namefile");
});
$(".comImport").click(function () {
    program.importList();
});
$(".deletePaper").click(function () {
    var valArr = new Array;
    $(":checkbox[name='title']:checked").each(function (i) {
        valArr[i] = $(this).val();
    });
    var vals = valArr.join(',');// 转换为逗号隔开的字符串
    if (vals != "") {
        program.deletePaperById(vals);
    } else {
        pubMeth.alertInfo("alert-info", "请先勾选删除项！");
    }
});
$(".deleteAllPaper").click(function () {
    program.deleteAllByExamId();
});
$(".save").click(function () {
    if (par.examId) {
        if (program.userInfo) {
            window.location.href = "exam.html";
        } else {
            pubMeth.alertInfo("alert-warning", "考生名单没有上传");
        }
    }
});
//下载学生上传模板
$(".downloada").attr("href", "../system/download?path=/static/考生信息导入模板.xls&isUeditorPath=true");
//点击上一步返回题库
$(".upList").click(function () {
    if (par.examId) {
        window.location.href = 'editParm.html?examId=' + program.examId;
    }
});
$(".addPaper").click(function () {
    $("#addpaperexam").modal();
});
$(".addPerExam").click(function () {
    program.istudentNumber = $(".istudentNumber").val();
    program.ipassword = $(".ipassword").val();
    program.addPerExam();
    $("#addpaperexam").modal('hide');
});
