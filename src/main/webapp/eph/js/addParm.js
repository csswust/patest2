var addParm = {
    examId: '',
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
    init: function () {
        commonet.init(); // 公共模块初始化
        commonet.listMenu();

        $(".ettemplate").addClass("on");
        $(".mytest").addClass("onet");

        addParm.flag = 0;
        addParm.opreation = "";
        var par = patest.getQueryObject();
        if (par.Id) {
            $(".pageName").text("修改考试");
            addParm.examId = par.Id;
            addParm.bserCourse();
            addParm.getExamInfoById();
        } else {
            patest.alertInfo("alert-danger", "考试id不存在");
            return;
        }

        // 添加问题模板
        $(".addTemplate").click(function () {
            if (par.Id) {
                addParm.opreation = '<button type="button" class="btn btn-info saveTemp" id="' + addParm.flag + '+' + addParm.flag + '">保存</button>';
            }
            addParm.addTemplate();
            var length = addParm.courseName.length;
            for (var i = 0; i < length; i++)
                $("#courseName-" + addParm.flag).append("<option value=" + addParm.courseName[i].couId + ">" + addParm.courseName[i].courseName + "</option>");
            addParm.flag++;
        });

// 保存问题模板数据
        $("#queTempla").on('click', '.saveTemp', function () {
            var rowIndex = this.id.split("+")[0];
            var knowId = $("#knowName-" + rowIndex + " option:selected").val();
            var levelId = $(".level-" + rowIndex + " option:selected").val();
            var score = $(".score-" + rowIndex).val();
            var allscore = 0;
            var allquestion = 0;
            var num = /^[0-9]+.?[0-9]*$/;
            //获取文本框中的内容
            var kownArr = [],
                levelArr = [],
                scoreArr = [],
                quesArr = [];
            $(".iknowName").each(function (i) {
                if ($(this).val() == "知识点") {
                    kownArr[i] = "";
                } else {
                    kownArr[i] = $(this).val();
                }
            });
            console.log(kownArr);
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
            addParm.kownArr = kownArr.join(',');
            addParm.levelArr = levelArr.join(',');
            addParm.scoreArr = scoreArr.join(',');
            if (levelId == "难度" || knowId == "知识点") {
                patest.alertInfo("alert-info", "请选择难度和知识点！");
                return;
            } else if (score == "") {
                patest.alertInfo("alert-info", "请填写分数！");
                return;
            } else if (!num.test(allscore)) {
                patest.alertInfo("alert-info", "分数请输入数字");
                return;
            } else if (addParm.count == 0) {
                patest.alertInfo("alert-info", "问题总量不可为0，请修改参数");
            } else {
                addParm.insertExamParam();
            }
        });
// 查询问题总量
        $("#queTempla").delegate('tr', 'change', function () {
            var index = this.className.split("-")[0];
            addParm.selectNum(index);
        });
        //删除问题模板
        $("#queTempla").on('click', '.deleteTemp', function () {
            var index = this.id.split("-")[0];
            var expmId = this.id.split("-")[1];
            if (expmId == null || expmId == undefined || expmId == "0") {
                $("." + this.id).remove();
            } else {
                if (confirm("确定要删除该模板吗？")) {
                    addParm.expmId = expmId;
                    addParm.delQuesTemp();
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
                for (var i = 0; i < addParm.course.length; i++) {
                    if (addParm.course[i].courseId == parentId) {
                        $(classname).append("<option value=" + addParm.course[i].knowId + ">" + addParm.course[i].knowName + "</option>");
                    }
                }
            }
        });

        //点击上一步返回题库
        $(".upParm").click(function () {
            window.location.href = 'addBank.html?Id=' + addParm.examId;

        });
        //点击下一步到上传学生名单
        $(".downParm").click(function () {
            var kownArr = [],
                levelArr = [],
                scoreArr = [];
            var allscore = 0;
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

            addParm.kownArr = kownArr.join(',');
            addParm.levelArr = levelArr.join(',');
            addParm.scoreArr = scoreArr.join(',');
            if (par.examId) {
                if (addParm.kownArr != "" && addParm.levelArr != "" && addParm.scoreArr != "") {
                    var num = /^[0-9]+.?[0-9]*$/;
                    if (!num.test(allscore)) {
                        patest.alertInfo("alert-info", "分数请输入数字");
                    } else {
                        if (addParm.count == 0) {
                            patest.alertInfo("alert-info", "问题总量不可为0，请修改参数");
                        } else {
                            $("#modaladdpram").modal({
                                //backdrop: 'static'
                            });
                            $(".allscore").html(allscore);
                            $(".allques").html(allquestion);
                            $(".parmadd").click(function () {
                                addParm.insertExamParam();
                            });
                        }
                    }
                } else {
                    patest.alertInfo("alert-info", "试卷参数的为必填");
                }
            } else if (par.Id) {
                if (addParm.kownArr != "" && addParm.levelArr != "" && addParm.scoreArr != "") {
                    var num = /^[0-9]+.?[0-9]*$/;
                    if (!num.test(allscore)) {
                        patest.alertInfo("alert-info", "分数请输入数字");
                    } else {
                        if (addParm.count == 0) {
                            patest.alertInfo("alert-info", "问题总量不可为0，请修改参数");
                        } else {
                            $("#modaladdpram").modal({
                                //backdrop: 'static'
                            });
                            $(".allscore").html(allscore);
                            $(".allques").html(allquestion);
                            $(".parmadd").click(function () {
                                addParm.updateExamParam();
                            });
                        }
                    }
                } else {
                    patest.alertInfo("alert-info", "试卷参数的为必填");
                }
            }
        });
    },
    //添加试卷参数
    addTemplate: function (exaParId) {
        if (!exaParId) exaParId = 0;
        $("#queTempla").append('<tr class=' + addParm.flag + '-' + exaParId + '>' +
            '<td>' + parseInt(addParm.flag + 1) + '</td>' +
            '<td><select class="form-control  courseName" id="courseName-' + addParm.flag + '"><option>课程</option></select></td>' +
            '<td><select class="form-control iknowName" id="knowName-' + addParm.flag + '"><option>知识点</option></select></td>' +
            '<td><select class="form-control level level-' + addParm.flag + '">' +
            '<option>难度</option>' +
            '<option value="1">容易</option>' +
            '<option value="2">中等</option>' +
            '<option value="3">困难</option>' +
            '</select></td>' +
            '<td><input class="form-control score score-' + addParm.flag + '"  type="text" /></td>' +
            '<td><span class="totalpro  total-' + addParm.flag + '"></span></td>' +
            '<td><span class="glyphicon glyphicon-remove deleteTemp" aria-hidden="true" id="' + addParm.flag + '-' + exaParId + '"></span></td>' +
            '<td>' + addParm.opreation + '</td></tr>');
    },
    //插入考试参数
    insertExamParam: function () {
        patest.request({
            url: "../ep/examParam/insertByArray"
        }, {
            examId: addParm.examId,
            knowIds: addParm.kownArr,
            levels: addParm.levelArr,
            scores: addParm.scoreArr
        }, function (result) {
            if (result.status > 0) {
                patest.alertInfo("alert-success", "添加成功！");
                window.location.href = 'addUplist.html?Id=' + addParm.examId;
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    updateExamParam: function () {
        patest.request({
            url: "../ep/examParam/insertByArray"
        }, {
            examId: addParm.examId,
            knowIds: addParm.kownArr,
            levels: addParm.levelArr,
            scores: addParm.scoreArr
        }, function (result) {
            if (result.status > 0) {
                patest.alertInfo("alert-success", "修改成功");
                window.location.href = 'addUplist.html?Id=' + addParm.examId;
            }
            else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    //删除试卷参数
    delQuesTemp: function () {
        patest.request({
            url: "../ep/examParam/deleteById"
        }, {
            id: addParm.expmId
        }, function (result) {
            if (result.status === 1) {
                patest.alertInfo("alert-success", "删除成功");
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    selectNum: function (index) {
        addParm.know = $("#knowName-" + index + " option:selected").val();
        addParm.level = $(".level-" + index + " option:selected").val();
        if (addParm.level == "难度") {
            addParm.level = "";
        }
        if (addParm.know == "知识点") {
            addParm.know = "";
        }
        patest.request({
            url: "../ep/examParam/selectProblemTotal"
        }, {
            knowId: addParm.know,
            levelId: addParm.level,
            examId: addParm.examId
        }, function (result) {
            addParm.count = result.data.total;
        });
        $(".total-" + index).text(addParm.count);
    },
    //获得考试信息
    getExamInfoById: function () {
        patest.request({
            url: "../ep/examParam/selectByCondition"
        }, {
            examId: addParm.examId
        }, function (result) {
            var length = result.data.examParamList.length;
            for (var i = 0; i < length; i++) {
                var id = "courseName-" + i;
                addParm.addTemplate(result.data.examParamList[i].exaParId);
                $("#" + id).html("<option>课程</option>");
                for (var k = 0; k < addParm.courseName.length; k++) {
                    $("#" + id).append("<option value=" + addParm.courseName[k].couId + ">" + addParm.courseName[k].courseName + "</option>");
                }
                addParm.flag++;
                var parentId = result.data.courseInfoList[i].couId;
                $("#" + id + " option[value='" + parentId + "']").attr("selected", true);
                for (var j = 0; j < addParm.course.length; j++) {//知识点
                    if (addParm.course[j].courseId == parentId) {
                        $("#knowName-" + i).append("<option value=" + addParm.course[j].knowId + ">" + addParm.course[j].knowName + "</option>");
                    }
                }
                $("#knowName-" + i + "  option[value='" + result.data.knowledgeInfoList[i].knowId + "']").attr("selected", true);
                $(".level-" + i + " option[value='" + result.data.examParamList[i].levelId + "']").attr("selected", true);
                $(".score-" + i).val(result.data.examParamList[i].score);
                $(".total-" + i).text(result.data.problemSumList[i]);
            }
        });
    },
    bserCourse: function () {
        patest.request({
            url: "../courseInfo/selectByCondition"
        }, null, function (result) {
            // 查询所有课程
            addParm.courseName = result.list;
            // 查询所有知识点
            patest.getKnowledgeInfo();
            addParm.course = patest.course;
        });
    }
};

