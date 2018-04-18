
var addBank = {
    html: "",
    probArr: [],
    examId: '',
    expmId: "",
    tempProbIds: [],
    tempProbId: '',
    init:function () {
        commonet.init(); // 公共模块初始化
        commonet.selectEpinfo();

        $(".examMana").next(".treeview-menu").toggle("slow");
        $(".examMana").addClass("leftActive");
        $(".examList").css("color", "white");
        $(".etbank").addClass("on");
        $(".mytest").addClass("onet");
        //解析url
        patest.serCourse();
        var par = patest.getQueryObject();
        //修改考试
        if (par.Id) {
            $(".pageName").text("修改考试");
            addBank.examId = par.Id;
            addBank.selectByExamId();
        }
        $(".localBank").click(function () {
            if (par.examId) {
                window.location.href = "testbank.html?examId=" + addBank.examId;
            } else if (par.Id) {
                window.location.href = "testbank.html?Id=" + addBank.examId;
            }

        });
        $(".upBank").click(function () {
            if (par.examId) {
                window.location.href = "addExam.html?examId=" + addBank.examId;
            } else if (par.Id) {
                window.location.href = "addExam.html?Id=" + par.Id;
            }
        });
        $(".downBank").click(function () {
            if (par.examId) {
                window.location.href = "addParm.html?examId=" + addBank.examId;
            } else if (par.Id) {
                window.location.href = "addParm.html?Id=" + par.Id;
            }
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
    },
    showproinfo: function () {
        var length = addBank.probArr.length;
        var order = 1;
        if (addBank.probArr.length != 0) {
            var banklist = '<tr>'
                + '<th>Order</th>'
                + '<th>ID</th>'
                + '<th>Title</th>'
                + '<th>Difficulty</th>'
                + '<th>Topic</th>'
                + '<th>Handle</th>'
                + '<tr>';
            $("#listhead").html(banklist);
        }
        addBank.html = "";
        for (var i = 0; i < length; i++) {
            addBank.html += '<tr class="' + flag + '-' + flag + '">'
                + '<td>' + order + '</td>'
                + '<td>' + addBank.probArr[i].probId + '</td>'
                + '<td><a href="question.html?id=' + addBank.probArr[i].probId + '"  class="title">' + addBank.probArr[i].title + '</a></td>'
                + '<td>' + addBank.probArr[i].level + '</td>'
                + '<td>' + addBank.probArr[i].knowName + '</td>'
                + '<td class="deletepro" id="' + flag + '-' + flag + '"><a><span class="glyphicon glyphicon-remove-circle"  ></span></a></td>'
                + '</tr>';
            addBank.tempProbIds[i] = addBank.probArr[i].tempProbId;
            order++;
            flag++;
        }
    },
    //通过考试Id来展示本场考试
    selectByExamId: function () {
        console.log(addBank.examId);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../exam/selectByExamId",
            dataType: 'json',
            async: false,
            data: {
                examId: addBank.examId
            },
            success: function (result) {
                console.log(result.data);
                addBank.probArr = result.data;
                addBank.showproinfo();
                $("#listInfo").html("");
                $("#listInfo").append(addBank.html);
            },
            error: function () {
                patest.alertInfo("alert-success", "请求错误");
            }
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

