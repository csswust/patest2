$(".userMana").next(".treeview-menu").toggle("slow");
$(".userMana").addClass("leftActive");
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
    showOperateLog: function () {
        var length = program.operateLogList.length;
        program.html = "";
        for (var i = 0; i < length; i++) {
            program.html += '<tr>'
                + '<td style="width:80px;"><input type="checkbox" value="' + program.operateLogList[i].id + '" name="title"/></td>'
                + '<td>' + program.operateLogList[i].id + '</td>'
                /*+ '<td class="usernamet">' + program.userInfoList[i].username + '</td>'*/
                + '<td>' + program.userProfileList[i].realName + '</td>'
                /*+ '<td>' + program.userProfileList[i].studentNumber + '</td>'*/
                + '<td>' + program.operateLogList[i].apiUrl + '</td>'
                + '<td>' + program.operateLogList[i].apiDesc + '</td>'
                + '<td class="apiArgc" id="' + i + '"><a class="compiler">' + '查看' + '</a></td>'
                + '<td>' + program.operateLogList[i].examId + '</td>'
                + '<td>' + program.operateLogList[i].problemId + '</td>'
                + '<td>' + program.operateLogList[i].ip + '</td>'
                + '<td>' + program.operateLogList[i].createTime + '</td>'
                + '</tr>';
        }
    },
    //查询账号
    selectOperateLog: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../operateLog/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                apiUrl: program.apiUrl,
                examId: program.examId,
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
                program.operateLogList = result.operateLogList;
                program.userInfoList = result.userInfoList;
                program.userProfileList = result.userProfileList;
                program.showOperateLog();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);
                $(".apiArgc").click(function () {
                    var index = this.id;
                    $('#settingdata').modal();
                    //$("#myModalLabel").text(program.problemInfoList[program.rowIndex].title);
                    $('#code').html('<textarea rows="32"  class="form-control" >' +
                        program.operateLogList[index].apiArgc + '</textarea>');
                });
            }
        });
    }
};
pubMeth.getRowsnum("rowsnum");
program.selectOperateLog();
$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    program.susername = $(".susername").val();
    program.sstudentNumber = $(".sstudentNumber").val();
    program.apiUrl = $(".sapiUrl").val();
    program.examId = $(".sexamId").val();
    program.startTime = $(".startTime").val();
    program.endTime = $(".endTime").val();
    program.selectOperateLog();

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
            program.selectOperateLog();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}