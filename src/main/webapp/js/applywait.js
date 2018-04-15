define(function (require, exports, module) {
    require('jquery');
    require('jCookie');
    require('../js/commonet.js');
    require('fileupload');
    require('bootstrap');
    require('paginator');
    $(".myapply").addClass("onet");

    var pubMeth = require('../js/public.js');

    var flag = 0;
    var program = {
        page: '1',
        applyid: '',
        applyids: [],
        orderid: '',
        orderids: [],
        applyhtml: '',
        count: '',
        data: '',
        paydata: '',
        paydatas: [],
        pay: '',
        testNum: '',
        testName: '',
        applypeo: '',
        isPass: '',
        isPasss: [],
        peoNum: '',
        startTime: '',
        endTime: '',
        sysuserid: '',
        showapplyInfo: function () {
            var infolist = program.data;
            var paylist = program.paydata;
            program.applyhtml = "";
            for (var i = 0; i < infolist.length; i++) {
                var ispassinfo, paystate;
                if (infolist[i].isPass == 1) {
                    ispassinfo = '申请成功';
                } else if (infolist[i].isPass == 0) {
                    ispassinfo = '申请中...';
                } else if (infolist[i].isPass == 2) {
                    ispassinfo = '申请失败'
                }
                if (paylist[i] == 0) {
                    paystate = '否';
                } else if (paylist[i] == 1) {
                    paystate = '是';
                }

                program.applyhtml += '<tr  class="' + flag + '-' + flag + '"><td>' + infolist[i].applyExamId + '</td>'
                    + '<td><a   href="applyexam.html?applyid=' + infolist[i].applyExamId + '">' + infolist[i].examName + '</a></td>'
                    + '<td>' + infolist[i].peopleNumber + '</td>'
                    + '<td>' + infolist[i].startTime + '</td>'
                    + '<td>' + infolist[i].endTime + '</td>'
                    + '<td>' + infolist[i].applicant + '</td>'
                    + '<td class="ispass" id="' + flag + '-' + flag + '">' + ispassinfo + '</td>'
                    + '<td class="deleteap" id="' + flag + '-' + flag + '"><button type="button" class="btn btn-primary btn-xs  ">取消申请</button></td>'
                    + '<td id="' + flag + '-' + flag + '" style="text-align:center;">' + paystate + '</td>'
                    + '<td class="payment" id="' + flag + '-' + flag + '"><button type="button" class="btn btn-info btn-xs" style="margin-left:17px;" id="pay">付款</button></td>'
                    + '<td class="addexam" id="' + flag + '-' + flag + '"><button type="button" class="btn  btn-success btn-xs">添加考试</button></td>'
                    + '</tr>';
                program.isPasss[i] = infolist[i].isPass;
                program.applyids[i] = infolist[i].applyExamId;
                program.paydatas[i] = paylist[i];
                flag++;
            }
        },
        //删除一场申请
        deleteApply: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../applyExam/deleteById",
                dataType: 'json',
                async: false,
                data: {
                    applyExamId: program.applyid,
                },
                success: function (result) {
                    console.log(result);
                    if (result.status == '1') {
                        pubMeth.alertInfo("alert-success", "删除成功");
                        flag = 0;
                        program.selectallInfo();
                    } else {
                        pubMeth.alertInfo("alert-danger", "删除失败");
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                }
            });
        },
        //查询申请的考试
        selectallInfo: function () {
            console.log(program.sysuserid);
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../applyExam/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    sysusrId: program.sysuserid,
                    page: program.page,
                    rows: 10,
                },
                success: function (result) {
                    console.log(result);
                    program.count = result.total;
                    program.data = result.listData;
                    program.paydata = result.payList;
                    program.showapplyInfo();
                    $("#applyInfo").empty();
                    $("#applyInfo").append(program.applyhtml);
                }
            });
        },
        selectorderid: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../order/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    applyId: program.applyid,
                },
                success: function (result) {
                    console.log(result);
                    for (var i = 0; i < result.listData.length; i++) {
                        program.orderids[i] = result.listData[i].orderId;
                        flag++;
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                }
            });
        },
    };

    function getUrlParam(name) {
        var reg = new RegExp();
    }

    pubMeth.getRowsnum("rowsnum");
    pubMeth.serCourse();
    program.sysuserid = $.cookie("sysuserId");
    var par = pubMeth.getQueryObject();
    program.selectallInfo();
    program.selectorderid();

    $("#applyInfo").on('click', '.deleteap', function () {
        flag--;
        var length = program.count;
        var index = this.id.split("-")[0];
        if (parseInt(index) + 1 > length) {
            $("." + this.id).remove();
        } else {
            if (confirm("确定要删除申请？")) {
                var index = this.id.split("-")[0];
                program.applyid = program.applyids[index];
                program.deleteApply();
            }
        }
    });
    $("#applyInfo").on('click', '.payment', function () {
        flag--;
        var length = program.count;
        var index = this.id.split("-")[0];
        if (parseInt(index) + 1 > length) {
            $("." + this.id).remove();
        } else {
            var index = this.id.split("-")[0];
            program.applyid = program.applyids[index];
            program.isPass = program.isPasss[index];
            if (program.isPass == 1) {
                window.location.href = "bill.html?applyid=" + program.applyid;
            } else {
                pubMeth.alertInfo("alert-info", " 未申请通过");
            }

        }
    });
    $("#applyInfo").on('click', '.addexam', function () {
        flag--;
        var length = program.count;
        var index = this.id.split("-")[0];
        if (parseInt(index) + 1 > length) {
            $("." + this.id).remove();
        } else {
            var index = this.id.split("-")[0];
            program.orderid = program.orderids[index];
            program.pay = program.paydatas[index];
            window.location.href = "addExam.html?orderid=" + program.orderid;
            /*	if(program.pay == 1){
             window.location.href = "addExam.html?orderid="+program.orderid;
             }else{
             pubMeth.alertInfo("alert-info","未支付");
             }*/
        }
    });
    $(".contapply").click(function () {
        window.location.href = "applyexam.html";
    });

    if (program.count > 0) {
        $.jqPaginator('#pagination', {
            totalCounts: program.count,
            visiblePages: 5,
            currentPage: 1,
            pageSize: parseInt(pubMeth.rowsnum),
            first: '<li class="first"><a href="javascript:;">首页</a></li>',
            last: '<li class="last"><a href="javascript:;">尾页</a></li>',
            page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
            onPageChange: function (num, type) {
                if (type == 'init') {
                    return;
                }
                program.page = num;
                program.selectallInfo();
            }
        });
    }
});


