var applywait = {
    flag: 0,
    epUserInfoList: [],
    init: function () {
        commonet.init(); // 公共模块初始化
        commonet.selectEpinfo();
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
        $("#applyInfo").on('click', '.payment', function () {
            var length = applywait.count;
            var index = this.id.split("-")[0];
            if (parseInt(index) + 1 > length) {
                $("." + this.id).remove();
            } else {
                var index = this.id.split("-")[0];
                applywait.applyid = applywait.applyids[index];
                applywait.isPass = applywait.isPasss[index];
                if (applywait.isPass == 1) {
                    window.location.href = "bill.html?applyid=" + applywait.applyid;
                } else {
                    pubMeth.alertInfo("alert-info", " 未申请通过");
                }
            }
        });
        $("#applyInfo").on('click', '.addexam', function () {
            var length = applywait.count;
            var index = this.id.split("-")[0];
            if (parseInt(index) + 1 > length) {
                $("." + this.id).remove();
            } else {
                var index = this.id.split("-")[0];
                applywait.orderid = applywait.orderids[index];
                applywait.pay = applywait.paydatas[index];
                window.location.href = "addExam.html?orderid=" + applywait.orderid;
                /*	if(applywait.pay == 1){
                 window.location.href = "addExam.html?orderid="+applywait.orderid;
                 }else{
                 pubMeth.alertInfo("alert-info","未支付");
                 }*/
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
                patest.alertInfo("alert-success", "删除成功");
                applywait.selectallInfo();
            } else {
                patest.alertInfo("alert-danger", "删除失败");
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
            applywait.showapplyInfo();
            $("#applyInfo").empty();
            $("#applyInfo").append(applywait.applyhtml);
        });
    },
    showapplyInfo: function () {
        var infolist = applywait.data;
        var epUserInfoList = applywait.epUserInfoList;
        applywait.applyhtml = "";
        var statusDesc = {
            "0": "申请中...",
            "-1": "申请失败",
            "1": "申请成功,待付款",
            "-2": "已付款"
        };
        applywait.applyhtml = "";
        for (var i = 0; i < infolist.length; i++) {
            var ispassinfo = statusDesc[infolist[i].status];
            var id = infolist[i].applyId;
            applywait.applyhtml += '<tr  class="' + id + '"><td>' + infolist[i].applyId + '</td>'
                + '<td><a   href="applyexam.html?applyid=' + id + '">' + infolist[i].examName + '</a></td>'
                + '<td>' + infolist[i].peopleNumber + '</td>'
                + '<td>' + infolist[i].startTime + '</td>'
                + '<td>' + infolist[i].endTime + '</td>'
                + '<td>' + epUserInfoList[i].username + '</td>'
                + '<td class="ispass" id="' + id + '">' + ispassinfo + '</td>'
                + '<td class="deleteap" id="' + id + '"><button type="button" class="btn btn-primary btn-xs  ">取消申请</button></td>'
                + '<td class="payment" id="' + id + '"><button type="button" class="btn btn-info btn-xs" style="margin-left:17px;" id="pay">付款</button></td>'
                + '<td class="addexam" id="' + id + '"><button type="button" class="btn  btn-success btn-xs">编辑考试</button></td>'
                + '</tr>';
        }
    }
};


