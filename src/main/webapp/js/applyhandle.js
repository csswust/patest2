$(".applyMana").next(".treeview-menu").toggle("slow");
$(".applyMana").addClass("leftActive");
$(".applyset").css("color", "white");
var flag = 0;
var program = {
    page: '1',
    showapplyInfo: function () {
        var infolist = program.data;
        var uesrs = program.uesrs;
        program.applyhtml = "";
        var statusDesc = {
            "0": "申请中...",
            "-1": "申请失败",
            "1": "申请成功,待付款",
            "2": "已付款"
        };
        for (var i = 0; i < infolist.length; i++) {
            var passstate = statusDesc[infolist[i].status];
            var id = infolist[i].applyId;
            var userId = uesrs[i].userId;
            var username = uesrs[i].username;
            var opate = '';
            if (infolist[i].status === 0) {
                opate = '<td><button type="button" class="btn btn-primary btn-xs examine" id="' + id + '">审核</button></td>';
            } else if (infolist[i].status === 1) {
                opate = '<td><button type="button" class="btn btn-success btn-xs selectApply" id="' + i + '">查看审核信息</button></td>';
            } else if (infolist[i].status === 2) {
                opate = '<td class="addexam" id="' + infolist[i].examId + '"><button type="button" class="btn btn-success btn-xs">编辑考试</button></td>';
            } else if (infolist[i].status === -1) {
                opate = '<td>' + '拒绝原因：' + infolist[i].reason + '</td>';
            } else {
                opate = '<td>' + '异常状态' + '</td>';
            }
            program.applyhtml += '<tr>'
                + '<td><input type="checkbox" value="' + id + '" name="title" /></td>'
                + '<td>' + id + '</td>'
                + '<td>' + infolist[i].examName + '</td>'
                + '<td>' + uesrs[i].phone + '</td>'
                + '<td>' + username + '</td>'
                + '<td>' + infolist[i].peopleNumber + '</td>'
                + '<td>' + infolist[i].startTime + '</td>'
                + '<td>' + infolist[i].endTime + '</td>'
                + '<td>' + passstate + '</td>'
                + opate
                + '</tr>';
        }
    },
    selectallInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epApplyInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                program.count = result.data.total;
                program.data = result.data.list;
                program.uesrs = result.data.epUserInfoList;
                program.epOrderInfoList = result.data.epOrderInfoList;
                program.userInfoList = result.data.userInfoList;
                program.userProfileList = result.data.userProfileList;
                program.showapplyInfo();
                $("#listInfo").empty();
                $("#listInfo").append(program.applyhtml);
            }
        });
    },
    updateInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epApplyInfo/accept",
            dataType: 'json',
            async: false,
            data: {
                status: program.isPass,
                applyId: program.applyId,
                reason: program.reason,
                money: program.money
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    program.selectallInfo();
                    pubMeth.alertInfo("alert-success", result.desc);
                    $("#mexamine").modal('hide');
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求失败");
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
                        url: "../epApplyInfo/deleteByIds",
                        dataType: 'json',
                        async: false,
                        data: {
                            ids: vals
                        },
                        success: function (result) {
                            console.log(result);
                            if (result.status > 0) {
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
    }
};
pubMeth.getRowsnum("rowsnum");
pubMeth.serCourse();
var par = pubMeth.getQueryObject();
program.selectallInfo();
program.deleteIt();

$("#listInfo").on('click', '.examine', function () {
    $('#mexamine').modal({});
    program.applyId = this.id;
});
$("#listInfo").on('click', '.selectApply', function () {
    $('#passed').modal({});
    var index = this.id;
    $(".passUsername").text(program.userInfoList[index].username +
        "(" + program.userProfileList[index].realName + ")");
    $(".passTime").text(program.data[index].examineTime);
    $(".passId").text(program.epOrderInfoList[index].orderNum);
    $(".passMoney").text(program.epOrderInfoList[index].money);
});

$("#listInfo").on('click', '.addexam', function () {
    var index = this.id;
    if (index !== "null") {
        window.location.href = "editExam.html?Id=" + index;
    } else {
        patest.alertInfo("alert-danger", "未付款");
    }
});
$(".saveapply").click(function () {
    program.money = $(".money").val();
    program.reason = $(".reason").val();
    program.isPass = $(".handle option:selected").val();
    program.updateInfo();

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