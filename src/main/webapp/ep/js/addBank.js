var addBank = {
    html: "",
    probArr: [],
    examId: '',
    expmId: "",
    tempProbIds: [],
    tempProbId: '',
    init: function () {
        commonet.init(); // 公共模块初始化
        commonet.selectEpinfo();

        $(".examMana").next(".treeview-menu").toggle("slow");
        $(".examMana").addClass("leftActive");
        $(".examList").css("color", "white");
        $(".etbank").addClass("on");
        $(".mytest").addClass("onet");
        var par = patest.getQueryObject();
        //修改考试
        if (par.Id) {
            $(".pageName").text("修改考试");
            addBank.examId = par.Id;
            addBank.selectByExamId();
        } else {
            patest.alertInfo("alert-danger", "考试id不存在");
            return;
        }
        $(".localBank").click(function () {
            window.location.href = "testbank.html?Id=" + addBank.examId;
        });
        $(".upBank").click(function () {
            window.location.href = "addExam.html?Id=" + par.Id;
        });
        $(".downBank").click(function () {
            window.location.href = "addParm.html?Id=" + par.Id;
        });
        $("#listInfo").on('click', '.deletepro', function () {
            flag--;
            var banklength = addBank.tempProbIds.length;
            var index = this.id.split("-")[0];
            if (parseInt(index) + 1 > banklength) {
                $("." + this.id).remove();
            } else {
                if (confirm("确定要删除该题吗？")) {
                    var index = this.id.split("-")[0];
                    addBank.tempProbId = addBank.tempProbIds[index];
                    addBank.deleteTempProblemById();
                }
            }
        });
        if (addBank.count > 0) {
            $(".countnum").html(addBank.count);
            $.jqPaginator('#pagination', {
                totalCounts: addBank.count,
                visiblePages: 5,
                currentPage: 1,
                pageSize: parseInt(patest.rowsnum),
                first: '<li class="first"><a href="javascript:;">首页</a></li>',
                last: '<li class="last"><a href="javascript:;">尾页</a></li>',
                page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
                onPageChange: function (num, type) {
                    if (type == 'init') {
                        return;
                    }
                    addBank.page = num;
                    addBank.selectByExamId();
                }
            });
        } else {
            $(".pagenum").css("display", "none");
        }
    },
    showproinfo: function () {
        var length = addBank.probArr.length;
        var order = 1;
        if (addBank.probArr.length != 0) {
            var banklist = '<tr>'
                + '<th>序号</th>'
                + '<th>编号</th>'
                + '<th style="width:400px">题目</th>'
                + '<th>难度</th>'
                + '<th>课程</th>'
                + '<th>知识点</th>'
                + '<th>操作</th>'
                + '<tr>';
            $("#listhead").html(banklist);
        }
        addBank.html = "";
        for (var i = 0; i < length; i++) {
            var level;
            if (addBank.probArr[i].levelId == 1) {
                level = '容易';
            } else if (addBank.probArr[i].levelId == 2) {
                level = '中等';
            } else if (addBank.probArr[i].levelId == 3) {
                level = '困难';
            }
            addBank.html += '<tr class="' + flag + '-' + flag + '">'
                + '<td>' + order + '</td>'
                + '<td>' + addBank.probArr[i].probId + '</td>'
                + '<td><a href="question.html?id=' + addBank.probArr[i].probId + '"  class="title">' + addBank.probArr[i].title + '</a></td>'
                + '<td>' + level + '</td>'
                + '<td>' + addBank.couseNamesArr[i].courseName + '</td>'
                + '<td>' + addBank.knowNameArr[i].knowName + '</td>'
                + '<td class="deletepro" id="' + flag + '-' + flag + '"><a><span class="glyphicon glyphicon-remove-circle"  ></span></a></td>'
                + '</tr>';
            addBank.tempProbIds[i] = addBank.tempProArr[i].exaProId;
            order++;
            flag++;
        }
    },
    //通过考试Id来展示本场考试
    selectByExamId: function () {
        patest.request({
            url: "../ep/examProblem/selectByCondition"
        }, {
            examId: addBank.examId,
            page: addBank.page,
            rows: patest.rowsnum
        }, function (result) {
            addBank.tempProArr = result.data.examProblemList;
            addBank.probArr = result.data.problemInfoList;
            addBank.knowNameArr = result.data.knowledgeInfoList;
            addBank.couseNamesArr = result.data.courseInfoList;
            addBank.count = result.data.total;
            addBank.probArr = result.data;
            addBank.showproinfo();
            $("#listInfo").html("");
            $("#listInfo").append(addBank.html);
        });
    },
    //通过问题Id来删除题目
    deleteTempProblemById: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../exam/deleteTempProblemById",
            dataType: 'json',
            async: false,
            data: {
                tempProbId: addBank.tempProbId,
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    patest.alertInfo("alert-success", "删除成功");
                    flag = 0;
                    addBank.selectByExamId();
                } else {
                    patest.alertInfo("alert-danger", "删除失败")
                }
            },
            error: function () {
                patest.alertInfo("alert-danger", "请求失败");
            }
        });
    }
};

