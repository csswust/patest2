$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".examList").css("color", "white");
$(".sttemplate").addClass("on");

var flag = 0;
var opreation = "";
var program = {
    examId: null,
    kown: '',
    score: '',
    level: '',
    count: '',
    kownArr: '',
    levelArr: '',
    scoreArr: '',
    expmArr: '',
    expmId: '',
    course: [],
    courseName: [],
    sumList: [],
    knowName: '',
    //添加试卷参数
    addTemplate: function (exaParId) {
        if (!exaParId) exaParId = 0;
        $("#queTempla").append('<tr class=' + flag + '-' + exaParId + '>' +
            '<td>' + parseInt(flag + 1) + '</td>' +
            '<td><select class="form-control  courseName" id="courseName-' + flag + '"><option>课程</option></select></td>' +
            '<td><select class="form-control iknowName" id="knowName-' + flag + '"><option>知识点</option></select></td>' +
            '<td><select class="form-control level level-' + flag + '">' +
            '<option>难度</option>' +
            '<option value="1">容易</option>' +
            '<option value="2">中等</option>' +
            '<option value="3">困难</option>' +
            '</select></td>' +
            '<td><input class="form-control score score-' + flag + '"  type="text" /></td>' +
            '<td><span class="totalpro  total-' + flag + '"></span></td>' +
            '<td><span class="glyphicon glyphicon-remove deleteTemp" aria-hidden="true" id="' + flag + '-' + exaParId + '"></span></td>' +
            '<td>' + opreation + '</td></tr>');
    },
    //插入考试参数
    insertExamParam: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examParam/insertByArray",
            dataType: "json",
            async: false,
            data: {
                examId: program.examId,
                knowIds: program.kownArr,
                levels: program.levelArr,
                scores: program.scoreArr
            },
            success: function (result) {
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "添加成功！");
                    if (par.examId) {
                        window.location.href = 'editUplist.html?examId=' + program.examId;
                    }
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    updateExamParam: function () {
        $.ajax({
            type: "POST",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examParam/insertByArray",
            dataType: "json",
            async: false,
            data: {
                examId: program.examId,
                knowIds: program.kownArr,
                levels: program.levelArr,
                scores: program.scoreArr
            },
            success: function (result) {
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "修改成功");
                    window.location.href = 'editUplist.html?Id=' + program.examId;
                }
                else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    //删除试卷参数
    delQuesTemp: function () {
        $.ajax({
            type: "POST",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examParam/deleteByIds",
            dataType: "json",
            async: false,
            data: {
                ids: program.expmId
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "删除成功");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    selectNum: function (index) {
        program.know = $("#knowName-" + index + " option:selected").val();
        program.level = $(".level-" + index + " option:selected").val();
        if (program.level == "难度") {
            program.level = "";
        }
        if (program.know == "知识点") {
            program.know = "";
        }
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examParam/selectProblemTotal",
            dataType: 'json',
            async: false,
            data: {
                knowId: program.know,
                levelId: program.level,
                examId: program.examId
            },
            success: function (result) {
                console.log(result);
                program.count = result.data.total;
            }, error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
        $(".total-" + index).text(program.count);
    },
    //获得考试信息
    getExamInfoById: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examParam/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId
            },
            success: function (result) {
                var length = result.data.examParamList.length;
                for (var i = 0; i < length; i++) {
                    var id = "courseName-" + i;
                    program.addTemplate(result.data.examParamList[i].exaParId);
                    $("#" + id).html("<option>课程</option>");
                    for (var k = 0; k < program.courseName.length; k++) {
                        $("#" + id).append("<option value=" + program.courseName[k].couId + ">" + program.courseName[k].courseName + "</option>");
                    }
                    flag++;
                    var parentId = result.data.courseInfoList[i].couId;
                    $("#" + id + " option[value='" + parentId + "']").attr("selected", true);
                    for (var j = 0; j < program.course.length; j++) {//知识点
                        if (program.course[j].courseId == parentId) {
                            $("#knowName-" + i).append("<option value=" + program.course[j].knowId + ">" + program.course[j].knowName + "</option>");
                        }
                    }
                    $("#knowName-" + i + "  option[value='" + result.data.knowledgeInfoList[i].knowId + "']").attr("selected", true);
                    $(".level-" + i + " option[value='" + result.data.examParamList[i].levelId + "']").attr("selected", true);
                    $(".score-" + i).val(result.data.examParamList[i].score);
                    $(".total-" + i).text(result.data.problemSumList[i]);
                }
            }
        });
    },
    bserCourse: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../courseInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {},
            success: function (result) {
                // 查询所有课程
                program.courseName = result.list;
                // 查询所有知识点
                pubMeth.getKnowledgeInfo();
                program.course = pubMeth.course;
            }
        });
    }
};
//获取url
var par = pubMeth.getQueryObject();
if (par.examId) {
    $(".pageName").text("修改考试");
    program.examId = par.examId;
    program.bserCourse();
    program.getExamInfoById();
}
// 添加问题模板
$(".addTemplate").click(function () {
    if (program.examId) {
        opreation = '<button type="button" class="btn btn-info saveTemp" id="' + flag + '+' + flag + '">保存</button>';
    }
    program.addTemplate();
    var length = program.courseName.length;
    for (var i = 0; i < length; i++)
        $("#courseName-" + flag).append("<option value=" + program.courseName[i].couId + ">" + program.courseName[i].courseName + "</option>");
    flag++;
});

// 保存问题模板数据
$("#queTempla").on('click', '.saveTemp', function () {
    var rowIndex = this.id.split("+")[0];
    var knowId = $("#knowName-" + rowIndex + " option:selected").val();
    var levelId = $(".level-" + rowIndex + " option:selected").val();
    var score = $(".score-" + rowIndex).val();
    var allscore = 0;
    var num = /^[0-9]+.?[0-9]*$/;
    //获取文本框中的内容
    var kownArr = [],
        levelArr = [],
        scoreArr = [];
    $(".iknowName").each(function (i) {
        if ($(this).val() == "知识点") {
            kownArr[i] = "";
        } else {
            kownArr[i] = $(this).val();
        }
    });
    $(".level").each(function (i) {
        if ($(this).val() == "知识点") {
            levelArr[i] = "";
        } else {
            levelArr[i] = $(this).val();
        }
    });
    $(".score").each(function (i) {
        scoreArr[i] = $(this).val();
        allscore += parseInt(scoreArr[i]);
    });

    allquestion = $("#queTempla tr:last").children("td:first").text();
    program.kownArr = kownArr.join(',');
    program.levelArr = levelArr.join(',');
    program.scoreArr = scoreArr.join(',');
    if (levelId == "难度" || knowId == "知识点") {
        pubMeth.alertInfo("alert-info", "请选择难度和知识点！");
        return;
    } else if (score == "") {
        pubMeth.alertInfo("alert-info", "请填写分数！");
        return;
    } else if (!num.test(allscore)) {
        pubMeth.alertInfo("alert-info", "分数请输入数字");
        return;
    } else if (program.count == 0) {
        pubMeth.alertInfo("alert-info", "问题总量不可为0，请修改参数");
    } else {
        program.insertExamParam();
    }
});
// 查询问题总量
$("#queTempla").delegate('tr', 'change', function () {
    var index = this.className.split("-")[0];
    program.selectNum(index);
});
//删除问题模板
$("#queTempla").on('click', '.deleteTemp', function () {
    var index = this.id.split("-")[0];
    var expmId = this.id.split("-")[1];
    if (expmId == null || expmId == undefined || expmId == "0") {
        $("." + this.id).remove();
    } else {
        if (confirm("确定要删除该模板吗？")) {
            program.expmId = expmId;
            program.delQuesTemp();
            $("." + this.id).remove();
        }
    }
});
//改变
$("#queTempla").delegate('.courseName', 'change', function () {
    var id = this.id.split("-")[1];
    if (id != "") {
        var classname = "#knowName-" + id;
        $(classname).html("");
        var parentId = $("#courseName-" + id + " option:selected").val();
        if (parentId == "课程") {
            $(classname).html("<option>知识点</option>");
            return;
        }
        for (var i = 0; i < program.course.length; i++) {
            if (program.course[i].courseId == parentId) {
                $(classname).append("<option value=" + program.course[i].knowId + ">" + program.course[i].knowName + "</option>");
            }
        }
    }
});

//点击上一步返回题库
$(".upParm").click(function () {
    if (par.examId) {
        window.location.href = 'editBank.html?examId=' + program.examId;
    }
});
//点击下一步到上传学生名单
$(".downParm").click(function () {
    var kownArr = [],
        levelArr = [],
        scoreArr = [],
        quesArr = [];
    var allscore = 0;
    var allquestion = 0;
    $(".iknowName").each(function (i) {
        if ($(this).val() == "知识点") {
            kownArr[i] = "";
        } else {
            kownArr[i] = $(this).val();
        }

    });
    $(".level").each(function (i) {
        if ($(this).val() == "知识点") {
            levelArr[i] = "";
        } else {
            levelArr[i] = $(this).val();
        }
    });
    $(".score").each(function (i) {
        scoreArr[i] = $(this).val();
        allscore += parseInt(scoreArr[i]);
    });
    allquestion = $("#queTempla tr:last").children("td:first").text();

    program.kownArr = kownArr.join(',');
    program.levelArr = levelArr.join(',');
    program.scoreArr = scoreArr.join(',');
    if (par.examId) {
        if (program.kownArr != "" && program.levelArr != "" && program.scoreArr != "") {
            var num = /^[0-9]+.?[0-9]*$/;
            if (!num.test(allscore)) {
                pubMeth.alertInfo("alert-info", "分数请输入数字");
            } else {
                if (program.count == 0) {
                    pubMeth.alertInfo("alert-info", "问题总量不可为0，请修改参数");
                } else {
                    $("#modaladdpram").modal({});
                    $(".allscore").html(allscore);
                    $(".allques").html(allquestion);
                    $(".parmadd").click(function () {
                        program.insertExamParam();
                    });
                }
            }
        } else {
            pubMeth.alertInfo("alert-info", "试卷参数的为必填");
        }
    }
});
