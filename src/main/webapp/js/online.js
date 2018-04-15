var program = {
    page: "1",
    count: '',
    selectOnline: function () {
        $("#listinfo").empty();
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../system/selectOnline",
            dataType: 'json',
            async: false,
            data: {
                userName: program.susername,
                studentNumber: program.sstudentNumber,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                result = result.onlineListRe;
                program.count = result.total;
                var userlength = result.userInfoList.length;
                for (var i = 0; i < userlength; i++) {
                    var username = result.userInfoList[i].username;
                    var realName = result.userProfileList[i].realName;
                    var studentNum = result.userProfileList[i].studentNumber;
                    var classroom = result.examPaperList[i].classroom;
                    var className = result.userProfileList[i].className;
                    var ip = result.userLoginLogList[i].loginIp;
                    var sessionId = result.sessionIdList[i];
                    if (username == null || username == "undefined") {
                        username = "";
                    }
                    if (realName == null || realName == "undefined") {
                        realName = "";
                    }
                    if (studentNum == null || studentNum == "undefined") {
                        studentNum = "";
                    }
                    if (classroom == null || classroom == "undefined") {
                        classroom = "";
                    }
                    if (className == null || className == "undefined") {
                        className = "";
                    }
                    if (ip == null || ip == "undefined") {
                        ip = "";
                    }

                    $("#listinfo").append('<tr>' +
                        '<td>' + username + '</td>' +
                        '<td>' + realName + '</td>' +
                        '<td>' + studentNum + '</td>' +
                        '<td>' + className + '</td>' +
                        '<td>' + classroom + '</td>' +
                        '<td>' + ip + '</td>' +
                        '<td><button class="btn btn-success btn-xs signOut" type="button" value=' + sessionId + '>退出</button></td>' +
                        '</tr>');
                }
                $(".signOut").click(function () {
                    var sessionIdValue = this.value;
                    $.ajax({
                        type: "get",
                        content: "application/x-www-form-urlencoded;charset=UTF-8",
                        url: "../system/signOut",
                        dataType: 'json',
                        async: false,
                        data: {
                            sessinoId: sessionIdValue
                        },
                        success: function (result) {
                            if (result.status == 1) {
                                window.location.reload();
                            }
                        }
                    });
                });
            },
            error: function () {
                //alert("111");
            }
        });
    }
};
pubMeth.getRowsnum("rowsnum");
program.selectOnline();
console.log(program.count);
$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    program.susername = $(".susername").val();
    program.sstudentNumber = $(".sstudentNumber").val();
    program.selectOnline();
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
            program.selectOnline();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}