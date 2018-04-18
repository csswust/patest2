var addExam = {
    title: "",
    startTime: "",
    endTime: '',
    description: '',
    examId: '',
    orderid: '',
    count: '',
    expmArr: "",
    expmIds: [],
    init: function () {
        commonet.init(); // 公共模块初始化
        commonet.selectEpinfo();

        $(".examMana").next(".treeview-menu").toggle("slow");
        $(".examMana").addClass("leftActive");
        $(".examList").css("color", "white");
        $(".etinfo").addClass("on");
        $(".mytest").addClass("onet");

        var date = new Date();
        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss',
            startDate: date
        });
        var par = patest.getQueryObject();
        //编辑考试
        if (par.Id) {
            $(".pageName").text("修改考试");
            addExam.examId = par.Id;
            addExam.getExamInfoById();
            addExam.setValue();
        } else {
            patest.alertInfo("alert-danger", "考试id不存在");
            return;
        }
        $(".downInfo").click(function () {
            addExam.title = $(".examTitle").val();
            addExam.description = $(".description").val();
            addExam.startTime = $(".startTime").val();
            addExam.endTime = $(".endTime").val();
            if (addExam.title && addExam.startTime && addExam.endTime) {
                if (patest.legTimeRange(addExam.startTime, addExam.endTime)) {
                    addExam.updateExam();
                } else {
                    patest.alertInfo("alert-info", "开始日期不能大于结束日期");
                }
            } else {
                patest.alertInfo("alert-info", "带*号的为必填");
            }
        });
    },
    //更新展示考试
    updateExam: function () {
        $.ajax({
            type: "POST",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../ep//examInfo/updateById",
            dataType: "json",
            async: false,
            data: {
                examId: addExam.examId,
                title: addExam.title,
                startTime: addExam.startTime,
                endTime: addExam.endTime,
                description: addExam.description
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    patest.alertInfo("alert-success", "修改成功");
                    window.location.href = "addBank.html?Id=" + addExam.examId;
                } else {
                    patest.alertInfo("alert-danger", result.desc);
                }
            },
            error: function () {
                patest.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    //获得考试信息展示
    getExamInfoById: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../ep/examInfo/selectById",
            dataType: 'json',
            async: false,
            data: {
                examId: addExam.examId
            },
            success: function (result) {
                console.log(result);
                addExam.title = result.data.examInfo.title;
                addExam.startTime = result.data.examInfo.startTime;
                addExam.endTime = result.data.examInfo.endTime;
                addExam.description = result.data.examInfo.description;
            }
        });
    },
    setValue: function () {
        $(".examTitle").val(addExam.title);
        $(".description").val(addExam.description);
        $(".startTime").val(addExam.startTime);
        $(".endTime").val(addExam.endTime);
    }
};

