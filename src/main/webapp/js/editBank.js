define(function (require, exports, module) {
    require('jquery');
    require('jCookie');
    require('../js/common.js');
    require('bootstrap');
    require('paginator');
    $(".examMana").next(".treeview-menu").toggle("slow");
    $(".examMana").addClass("leftActive");
    $(".examList").css("color", "white");
    $(".stbank").addClass("on");

    var flag = 0;
    var pubMeth = require('../js/public.js');
    var program = {
        page: '1',
        count: '',
        html: '',
        tempProArr: [],
        probArr: [],
        knowNameArr: [],
        couseNamesArr: [],
        levelArr: [],
        examId: '',
        expmId: '',
        tempProbIds: [],
        tempProbId: '',
        showproinfo: function () {
            var length = program.probArr.length;
            var order = 1;
            if (program.probArr.length != 0) {
                var banklist = '<tr>'
                    + '<th>序号</th>'
                    + '<th>编号</th>'
                    + '<th style="width:200px">题目</th>'
                    + '<th>难度</th>'
                    + '<th>课程</th>'
                    + '<th>知识点</th>'
                    + '<th>操作</th>'
                    + '<tr>';
                $("#listhead").html(banklist);
            }
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
                    + '<td>' + order + '</td>'
                    + '<td>' + program.probArr[i].probId + '</td>'
                    + '<td><a href="question.html?id=' + program.probArr[i].probId + '"  class="title">' + program.probArr[i].title + '</a></td>'
                    + '<td>' + level + '</td>'
                    + '<td>' + program.couseNamesArr[i].courseName + '</td>'
                    + '<td>' + program.knowNameArr[i].knowName + '</td>'
                    + '<td class="deletepro" id="' + flag + '-' + flag + '"><a><span class="glyphicon glyphicon-remove-circle"  ></span></a></td>'
                    + '</tr>';
                program.tempProbIds[i] = program.tempProArr[i].exaProId;
                order++;
                flag++;
            }
        },
        //通过考试Id来展示本场考试
        selectByExamId: function () {
            console.log(program.examId);
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examProblem/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    examId: program.examId,
                    page: program.page,
                    rows: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    program.tempProArr = result.examProblemList;
                    program.probArr = result.problemInfoList;
                    program.knowNameArr = result.knowledgeInfoList;
                    program.couseNamesArr = result.courseInfoList;
                    // program.levelArr = result.levelList;
                    program.count = result.total;
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
        deleteTempProblemById: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examProblem/deleteByIds",
                dataType: 'json',
                async: false,
                data: {
                    ids: program.tempProbId,
                },
                success: function (result) {
                    console.log(result);
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
        },
    };
    pubMeth.getRowsnum("rowsnum");
    //解析url
    pubMeth.serCourse();
    var par = pubMeth.getQueryObject();
    if (par.examId) {
        $(".pageName").text("添加考试");
        program.examId = par.examId;
        console.log(program.examId);
        program.selectByExamId();
    }
    //修改考试
    if (par.Id) {
        $(".pageName").text("修改考试");
        program.examId = par.Id;
        program.selectByExamId();
    }
    $(".localBank").click(function () {
        if (par.examId) {
            window.location.href = "probank.html?examId=" + program.examId;
        } else if (par.Id) {
            window.location.href = "probank.html?Id=" + program.examId;
        }

    });
    $(".upBank").click(function () {
        if (par.examId) {
            window.location.href = "editExam.html?examId=" + program.examId;
        } else if (par.Id) {
            window.location.href = "editExam.html?Id=" + par.Id;
        }
    });
    $(".downBank").click(function () {
        if (par.examId) {
            window.location.href = "editParm.html?examId=" + program.examId;
        } else if (par.Id) {
            window.location.href = "editParm.html?Id=" + par.Id;
        }
    });
    $("#listInfo").on('click', '.deletepro', function () {
        flag--;
        var banklength = program.tempProbIds.length;
        var index = this.id.split("-")[0];
        if (parseInt(index) + 1 > banklength) {
            $("." + this.id).remove();
        } else {
            /*$("#modaldeletebank").modal({
             backdrop : 'static'
             });
             $(".bankdelete").click(function(){
             var index = this.id.split("-")[0];
             program.tempProbId = program.tempProbIds[index];
             program.deleteTempProblemById();
             });*/
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
});
