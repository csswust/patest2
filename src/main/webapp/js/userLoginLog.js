$(".sysMana").next(".treeview-menu").toggle("slow");
$(".sysMana").addClass("leftActive");
$(".userLoginLog").css("color", "white");

$(".form_datetime").datetimepicker({
    format: 'yyyy-mm-dd hh:ii:ss'
});

var program = {
    page: '1',
    userId: null,
    className: null,
    realName: null,
    term: null,
    studentNum: null,
    username: null,
    password: null,
    studentNumber: null,
    isAdmin: null,
    isTeacher: null,
    admin: null,
    teacher: null,
    realname: null,
    html: null,
    userInfoList: [],
    userProfileList: [],
    userIds: [],
    //显示账号
    showLoginLog: function () {
        var length = program.userLoginLogList.length;
        program.html = "";
        for (var i = 0; i < length; i++) {
            program.html += '<tr>'
                + '<td style="width:80px;"><input type="checkbox" value="' + program.userLoginLogList[i].useLogId + '" name="title"/></td>'
                + '<td>' + program.userLoginLogList[i].useLogId + '</td>'
                + '<td>' + program.userInfoList[i].userId + '</td>'
                + '<td class="usernamet">' + program.userInfoList[i].username + '</td>'
                + '<td>' + program.userProfileList[i].realName + '</td>'
                + '<td>' + program.userProfileList[i].studentNumber + '</td>'
                + '<td>' + program.userLoginLogList[i].loginIp + '</td>'
                + '<td>' + program.userLoginLogList[i].createTime + '</td>'
                + '</tr>';
        }
    },
    //查询账号
    selectLoginLog: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userLoginLog/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                username: program.susername,
                studentNumber: program.sstudentNumber,
                startTime: program.startTime,
                endTime: program.endTime,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                result = result.data;
                console.log(result);
                program.count = result.total;
                program.userLoginLogList = result.userLoginLogList;
                program.userInfoList = result.userInfoList;
                program.userProfileList = result.userProfileList;
                program.showLoginLog();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);
            }
        });
    },
    //删除账号
    deleteLoginLog: function (vals) {
        console.log(vals);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userLoginLog/deleteByIds",
            dataType: 'json',
            async: false,
            data: {
                ids: vals
            },
            success: function (result) {
                console.log(result);
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "删除成功！");
                    program.selectLoginLog();
                } else {
                    pubMeth.alertInfo("alert-danger", "删除失败！");
                }
            }
        });
    }
};
pubMeth.getRowsnum("rowsnum");
program.selectLoginLog();
$(".deleteUser").click(function () {
    var valArr = new Array;
    $(":checkbox[name='title']:checked").each(function (i) {
        valArr[i] = $(this).val();
    });
    var vals = valArr.join(',');// 转换为逗号隔开的字符串
    if (vals != "") {
        program.deleteLoginLog(vals);
    } else {
        pubMeth.alertInfo("alert-info", "请先勾选删除项！");
    }
});
$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    program.susername = $(".susername").val();
    program.sstudentNumber = $(".sstudentNumber").val();
    program.startTime = $(".startTime").val();
    program.endTime = $(".endTime").val();
    program.selectLoginLog();

    $(".countnum").html(program.count);
    $('#pagination').jqPaginator('option', {
        totalCounts: program.count
    });
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
            program.selectLoginLog();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}