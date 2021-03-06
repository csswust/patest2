var applywait = {
    flag: 0,
    epUserInfoList: [],
    init: function () {
        commonet.init(); // 公共模块初始化
        $(".myapply").addClass("onet");

        patest.getRowsnum("rowsnum");
        applywait.sysuserid = $.cookie("sysuserId");
        applywait.selectallInfo();

        $("#applyInfo").on('click', '.deleteap', function () {
            var id = this.id;
            if (confirm("确定要删除申请？")) {
                applywait.deleteApply(id);
            }
        });
        $("#applyInfo").on('click', '.addexam', function () {
            var index = this.id;
            if (index !== "null") {
                window.location.href = "addExam.html?Id=" + index;
            } else {
                patest.alertInfo("alert-danger", "未付款");
            }
        });
        $(".contapply").click(function () {
            window.location.href = "applyexam.html";
        });
        if (applywait.count > 0) {
            $.jqPaginator('#pagination', {
                totalCounts: applywait.count,
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
                    applywait.page = num;
                    applywait.selectallInfo();
                }
            });
        }
    },
    //删除一场申请
    deleteApply: function (id) {
        patest.request({
            url: "../ep/epApplyInfo/deleteById"
        }, {
            applyId: id
        }, function (result) {
            if (result.status === 1) {
                patest.alertInfo("alert-success", result.desc);
                applywait.selectallInfo();
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    //查询申请的考试
    selectallInfo: function () {
        patest.request({
            url: "../ep/epApplyInfo/selectByCondition"
        }, {
            page: applywait.page,
            rows: 10
        }, function (result) {
            applywait.count = result.data.total;
            applywait.data = result.data.list;
            applywait.epUserInfoList = result.data.epUserInfoList;
            applywait.epOrderInfoList = result.data.epOrderInfoList;
            applywait.showapplyInfo();
            $("#applyInfo").empty();
            $("#applyInfo").append(applywait.applyhtml);
        });
    },
    showapplyInfo: function () {
        var infolist = applywait.data;
        var epUserInfoList = applywait.epUserInfoList;
        var epOrderInfoList = applywait.epOrderInfoList;
        applywait.applyhtml = "";
        var statusDesc = {
            "0": "申请中...",
            "-1": "申请失败",
            "1": "申请成功,待付款",
            "2": "已付款"
        };
        applywait.applyhtml = "";
        for (var i = 0; i < infolist.length; i++) {
            var ispassinfo = statusDesc[infolist[i].status];
            var id = infolist[i].applyId;
            var orderId = epOrderInfoList[i].orderId;
            var oparate = "";
            if (infolist[i].status === 0) {
                oparate = '<td ><button type="button" class="btn btn-primary btn-xs deleteap" id="' + id + '">取消申请</button></td>';
            } else if (infolist[i].status === 1) {
                oparate = '<td><a href="billlist.html">请在我的账单付款</a></td>';
            } else if (infolist[i].status === 2) {
                oparate = '<td ><button type="button" class="btn btn-success btn-xs addexam" id="' + infolist[i].examId + '">编辑考试</button></td>';
            } else if (infolist[i].status === -1) {
                oparate = '<td>' + '拒绝原因：' + infolist[i].reason + '</td>';
            } else {
                oparate = '<td>' + '异常状态' + '</td>';
            }
            applywait.applyhtml += '<tr class="' + id + '"><td>' + infolist[i].applyId + '</td>'
                + '<td><a href="applyexam.html?applyid=' + id + '">' + infolist[i].examName + '</a></td>'
                + '<td>' + infolist[i].peopleNumber + '</td>'
                + '<td>' + infolist[i].startTime + '</td>'
                + '<td>' + infolist[i].endTime + '</td>'
                + '<td>' + epUserInfoList[i].username + '</td>'
                + '<td class="ispass" id="' + id + '">' + ispassinfo + '</td>'
                + oparate
                + '</tr>';
        }
    }
};


