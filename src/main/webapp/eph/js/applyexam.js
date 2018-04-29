var applyexam = {
    isProblem:0,
    init: function () {
        commonet.init(); // 公共模块初始化
        $(".applyexam").addClass("onet");

        var date = new Date();
        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss',
            startDate: date
        });
        var par = patest.getQueryObject();
        applyexam.sysuserid = $.cookie("sysuserId");
        if (par.applyid) {
            applyexam.applyid = par.applyid;
            $(".pageName").text("修改申请");
            applyexam.selectInfo();
            applyexam.setValue();
        }
        // applyexam.getTop();
        $("#applyexam").click(function () {
            applyexam.testName = $(".testname").val();
            applyexam.peopleNum = $(".peoplenum").val();
            applyexam.startTime = $(".startTime").val();
            applyexam.endTime = $(".endTime").val();
            if (applyexam.testName && applyexam.peopleNum && applyexam.isProblem !== ""
                && applyexam.startTime && applyexam.endTime) {
                if (patest.legTimeRange(applyexam.startTime, applyexam.endTime)) {
                    applyexam.insertApplyExam();
                } else {
                    patest.alertInfo("alert-info", "开始日期不能大于结束日期");
                }
            } else {
                patest.alertInfo("alert-info", "带*号的为必填");
            }
        });
    },
    //添加一场申请
    insertApplyExam: function () {
        patest.request({
            url: "../ep/epApplyInfo/insert"
        }, {
            examName: applyexam.testName,
            peopleNumber: applyexam.peopleNum,
            isProv: applyexam.isProblem,
            startTime: applyexam.startTime,
            endTime: applyexam.endTime
        }, function (result) {
            applyexam.applyid = result.status;
            if (result.status === 1) {
                patest.alertInfo("alert-success", "保存成功");
                window.location.href = "applywait.html";
            } else if (result.status === 0) {
                patest.alertInfo("alert-danger", "保存失败！");
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    //查询申请
    selectInfo: function () {
        patest.request({
            url: "../ep/epApplyInfo/selectByCondition"
        }, {
            applyId: applyexam.applyid
        }, function (result) {
            var data = result.data.list[0];
            applyexam.testName = data.examName;
            applyexam.peopleNum = data.peopleNumber;
            applyexam.isProblem = data.isProblem;
            applyexam.startTime = data.startTime;
            applyexam.endTime = data.endTime;
        });
    },
    /*getTop: function () {
        var radios = $('input[name="status"]');
        for (var i = 0; i < radios.length; i++) {
            applyexam.isProblem = $('input[name="status"]:checked').val();
            radios[i].onclick = function () {
                applyexam.isProblem = $('input[name="status"]:checked').val();
            }
        }
    },*/
    setValue: function () {
        $(".testname").val(applyexam.testName);
        $(".peoplenum").val(applyexam.peopleNum);
        $(".startTime").val(applyexam.startTime);
        $(".endTime").val(applyexam.endTime);
        /*if (applyexam.isProblem === 1) {
            $(".yesbank").prop("checked", true);
        } else {
            $(".nobank").prop("checked", true);
        }*/
    }
};
