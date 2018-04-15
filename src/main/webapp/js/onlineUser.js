$(".examRoom").next(".treeview-menu").toggle("slow");
$(".examRoom").addClass("leftActive");
$(".examination").css("color", "white");
$(".online").addClass("active");

var program = {
    page: "1",
    examId: '',
    userId: '',
    username: '',
    count: '',
    ohtml: '',
    userArr: [],
    userProfileArr: [],
    listuserip: [],
    examPaperList: [],
    selectOnline: function () {
        console.log(program.examId);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                rows: pubMeth.rowsnum,
                examId: program.examId,
                userName: program.susername,
                studentNumber: program.sstudentNumber,
                containOnline: true
            },
            success: function (result) {
                console.log(result);
                program.userProfileArr = result.userProfileList;
                program.userArr = result.userInfoList;
                program.examPaperList = result.examPaperList;
                program.sessinoList = result.sessinoList;
                program.count = result.total;
                program.showOnline();
                $("#listInfo").empty();
                $("#listInfo").append(program.ohtml);
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
            }
        });
    },
    showOnline: function () {
        var length = program.userArr.length;
        program.ohtml = '';
        for (var i = 0; i < length; i++) {
            var order = i + 1;
            var loginStatus = '<td></td>';
            if (program.sessinoList[i] === null) {
                loginStatus = '<td>未登录</td>';
            } else {
                loginStatus = '<td>已登录<button class="btn btn-success btn-xs signOut" type="button" value='
                    + program.sessinoList[i] + '>退出</button></td>';
            }
            program.ohtml += '<tr>'
                + '<td>' + order + '</td>'
                + '<td>' + program.userProfileArr[i].studentNumber + '</td>'
                + '<td>' + program.userArr[i].username + '</td>'
                + '<td>' + program.userProfileArr[i].realName + '</td>'
                + '<td>' + program.userProfileArr[i].className + '</td>'
                + '<td>' + program.examPaperList[i].classroom + '</td>'
                + loginStatus
                + '</tr>';
        }
    }
};
pubMeth.getRowsnum("rowsnum");
var parm = pubMeth.getQueryObject();
program.examId = parm["id"];
program.selectOnline();

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