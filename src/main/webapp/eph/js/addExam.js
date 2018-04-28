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
        commonet.listMenu();

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
        patest.request({
            url: "../ep/examInfo/updateById"
        }, {
            examId: addExam.examId,
            title: addExam.title,
            startTime: addExam.startTime,
            endTime: addExam.endTime,
            description: addExam.description
        }, function (result) {
            if (result.status === 1) {
                patest.alertInfo("alert-success", "修改成功");
                window.location.href = "addBank.html?Id=" + addExam.examId;
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    //获得考试信息展示
    getExamInfoById: function () {
        patest.request({
            url: "../ep/examInfo/selectById"
        }, {
            examId: addExam.examId
        }, function (result) {
            addExam.title = result.data.examInfo.title;
            addExam.startTime = result.data.examInfo.startTime;
            addExam.endTime = result.data.examInfo.endTime;
            addExam.description = result.data.examInfo.description;
        });
    },
    setValue: function () {
        $(".examTitle").val(addExam.title);
        $(".description").val(addExam.description);
        $(".startTime").val(addExam.startTime);
        $(".endTime").val(addExam.endTime);
    }
};

