define(function (require, exports, module) {
    require('jquery');
    require('jCooike');
    require('../js/common.js');
    require('bootstrap');
    require('paginator');

    $(".applyMana").next(".treeview-menu").toggle("slow");
    $(".applyMana").addClass("leftActive");
    $(".applyset").css("color", "white");

    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');

    var flag = 0;
    var program = {
        page: '1',
        applyid: '',
        applyids: [],
        applytime: '',
        billdes: '',
        money: '',
        applyhtml: '',
        count: '',
        data: '',
        uesrnamelist: [],
        testNum: '',
        testName: '',
        applypeo: '',
        applypeos: [],
        phone: '',
        phones: [],
        isPass: '',
        isPasss: [],
        peoNum: '',
        sysname: '',
        startTime: '',
        endTime: '',
        sysuserid: '',
        sysuserids: [],
        payArr: [],
        reason: [],
        showapplyInfo: function () {
            var infolist = program.data;
            var order = 1;
            program.applyhtml = "";
            for (var i = 0; i < infolist.length; i++) {
                var passstate;
                if (infolist[i].isPass == 1) {
                    //passstate = "已通过";
                    if (program.payArr[i] == 1) {
                        passstate = "已支付";
                    } else {
                        passstate = "未支付";
                    }
                } else if (infolist[i].isPass == 0) {
                    passstate = "待审核";
                } else if (infolist[i].isPass == 2) {
                    passstate = "未通过";
                }
                program.applyhtml += '<tr  class="' + flag + '-' + flag + '" value="' + infolist[i].applyExamId + ',' + infolist[i].sysusrId + '">'
                    + '<td><input type="checkbox" value="' + infolist[i].applyExamId + '" name="title"/></td>'
                    + '<td>' + order + '</td>'
                    + '<td><a class="title"  href="applyexam.html?applyid=' + infolist[i].applyExamId + '">' + infolist[i].examName + '</a></td>'
                    + '<td>' + infolist[i].phone + '</td>'
                    + '<td>' + infolist[i].applicant + '</td>'
                    + '<td>' + program.uesrnamelist[i] + '</td>'
                    + '<td>' + infolist[i].peopleNumber + '</td>'
                    + '<td>' + infolist[i].startTime + '</td>'
                    + '<td>' + infolist[i].endTime + '</td>'
                    + '<td>' + passstate + '</td>'
                    + '<td><button type="button" class="btn btn-primary btn-xs examine" id="' + flag + '-' + flag + '">审核</button></td>'
                    + '</tr>';
                program.applyids[i] = infolist[i].applyExamId;
                program.applypeos[i] = infolist[i].applicant;
                program.phones[i] = infolist[i].phone;
                program.isPasss[i] = infolist[i].isPass;
                program.sysuserids[i] = infolist[i].sysusrId;
                flag++;
                order++;
            }
        },
        selectallInfo: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../applyExam/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    page: program.page,
                    rows: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    program.count = result.total;
                    program.data = result.listData;
                    program.uesrnamelist = result.userNameList;
                    program.payArr = result.payList;
                    program.showapplyInfo();
                    $("#applyInfo").empty();
                    $("#applyInfo").append(program.applyhtml);
                }
            });
        },
        updateInfo: function () {
            console.log(program.isPass);
            console.log(program.applyid);
            console.log(program.sysuserid);
            console.log(program.reason);
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../applyExam/updateById",
                dataType: 'json',
                async: false,
                data: {
                    sysusrId: program.sysuserid,
                    isPass: program.isPass,
                    applyExamId: program.applyid,
                    reason: program.reason,
                },
                success: function (result) {
                    console.log(result);
                    if (result.status == 1) {
                        program.selectallInfo();
                        pubMeth.alertInfo("alert-success", "修改成功");
                    } else {
                        pubMeth.alertInfo("alert-danger", "修改失败");
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                },
            });
        },
        //插入一个账单
        insertOrder: function () {
            console.log(program.money);
            console.log(program.applypeo);
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../order/insertOrder",
                dataType: "json",
                async: false,
                data: {
                    money: program.money,
                    linkman: program.applypeo,
                    phone: program.phone,
                    sysusrId: program.sysuserid,
                    isPay: '0',
                    applyId: program.applyid,
                },
                success: function (result) {
                    console.log(result);
                    program.applyid = result.status;
                    if (result.status > 0) {
                        pubMeth.alertInfo("alert-success", "保存成功");
                        program.isPass = 1;
                        program.updateInfo();
                        window.location.href = "adminbill.html?applyid=" + program.applyid;
                    }
                    else {
                        pubMeth.alertInfo("alert-warning", "保存失败！");
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求错误");
                }
            });
        },
        deleteIt: function () {
            $('.delete').on('click', function (e) {
                var valArr = new Array;
                $(":checkbox[name='title']:checked").each(function (i) {
                    valArr[i] = $(this).val();
                });
                var vals = valArr.join(',');// 转换为逗号隔开的字符串
                if (vals != "") {
                    $("#modalexamdelete").modal(function () {
                        backdrop : 'static'
                    });
                    $(".examquess").html(vals);
                    $(".examdelete").click(function () {
                        $.ajax({
                            type: "post",
                            content: "application/x-www-form-urlencoded;charset=UTF-8",
                            url: "../applyExam/deleteApplyExam",
                            dataType: 'json',
                            async: false,
                            data: {
                                applyExamIds: vals
                            },
                            success: function (result) {
                                console.log(result);
                                if (result.status == 1) {
                                    pubMeth.alertInfo("alert-success", "删除成功！");
                                    program.selectallInfo();
                                    $("#modalexamdelete").modal('hide');
                                } else {
                                    pubMeth.alertInfo("alert-danger", "删除失败！");
                                }
                            }
                        });
                    });
                } else {
                    pubMeth.alertInfo("alert-info", "请先勾选删除项！");
                }
            });
        },
    };
    pubMeth.getRowsnum("rowsnum");
    pubMeth.serCourse();
    var par = pubMeth.getQueryObject();
    program.selectallInfo();
    program.deleteIt();

    $("#applyInfo").on('click', '.examine', function () {
        $('#mexamine').modal({
            backdrop: 'static'
        });
        var index = this.id.split("-")[0];
        program.sysuserid = program.sysuserids[index];
        program.applyid = program.applyids[index];
        program.phone = program.phones[index];
        program.applypeo = program.applypeos[index];
    });
    $(".saveapply").click(function () {
        program.money = $(".money").val();
        program.reason = $(".reason").val();
        program.isPass = $(".handle option:selected").val();
        program.updateInfo();
        if (program.isPass == 1) {
            $("#mexamine").modal('hide');
            program.insertOrder();
        } else if (program.isPass == 2) {
            $("#mexamine").modal('hide');
        }

    });
    if (program.count > 0) {
        $(".countnum").html(program.count);
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
    } else {
        $(".pagenum").css("display", "none");
    }
});