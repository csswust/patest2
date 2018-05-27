$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".examList").css("color", "white");
$(".stinfo").addClass("on");
var flag = 0;
var program = {
    title: "",
    startTime: "",
    endTime: '',
    description: '',
    examId: null,
    count: '',
    expmArr: "",
    expmIds: [],
    examip: '',
    //增加一场考试
    addExam: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/insertOne",
            dataType: "json",
            async: false,
            data: {
                title: program.title,
                startTime: program.startTime,
                endTime: program.endTime,
                description: program.description,
                allowIp: program.examip
            },
            success: function (result) {
                program.examId = result.examId;
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "保存成功");
                    window.location.href = "editBank.html?examId=" + program.examId;
                } else {
                    pubMeth.alertInfo("alert-warning", "保存失败");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    //更新展示考试
    updateExam: function () {
        $.ajax({
            type: "POST",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/updateById",
            dataType: "json",
            async: false,
            data: {
                examId: program.examId,
                title: program.title,
                startTime: program.startTime,
                endTime: program.endTime,
                description: program.description,
                allowIp: program.examip
            },
            success: function (result) {
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "修改成功");
                    window.location.href = "editBank.html?examId=" + program.examId;
                }
                else {
                    pubMeth.alertInfo("alert-warning", "修改失败！");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    //获得考试信息展示
    getExamInfoById: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: program.examId
            },
            success: function (result) {
                program.title = result.data.examInfoList[0].title;
                program.startTime = result.data.examInfoList[0].startTime;
                program.endTime = result.data.examInfoList[0].endTime;
                program.description = result.data.examInfoList[0].description;
                program.examip = result.data.examInfoList[0].allowIp;
            }
        });
    },
    setValue: function () {
        $(".examTitle").val(program.title);
        $(".description").val(program.description);
        $(".startTime").val(program.startTime);
        $(".endTime").val(program.endTime);
        $(".examip").val(program.examip);
    }
};

var date = new Date();
$(".form_datetime").datetimepicker({
    format: 'yyyy-mm-dd hh:ii:ss',
    startDate: date
});
var par = pubMeth.getQueryObject();
//编辑考试
if (par.examId) {
    $(".pageName").text("修改考试");
    program.examId = par.examId;
    program.getExamInfoById();
    program.setValue();
}
$(".downInfo").click(function () {
    program.title = $(".examTitle").val();
    program.description = $(".description").val();
    program.startTime = $(".startTime").val();
    program.endTime = $(".endTime").val();
    program.examip = $(".examip").val();
    if (!program.examId) {
        if (program.title && program.startTime && program.endTime) {
            if (pubMeth.legTimeRange(program.startTime, program.endTime)) {
                program.addExam();
            } else {
                pubMeth.alertInfo("alert-info", "开始日期不能大于结束日期");
            }
        } else {
            pubMeth.alertInfo("alert-info", "带*号的为必填");
        }
    } else {
        if (program.title && program.startTime && program.endTime) {
            if (pubMeth.legTimeRange(program.startTime, program.endTime)) {
                program.updateExam();
            } else {
                pubMeth.alertInfo("alert-info", "开始日期不能大于结束日期");
            }
        } else {
            pubMeth.alertInfo("alert-info", "带*号的为必填");
        }
    }
});
