$(".userMana").next(".treeview-menu").toggle("slow");
$(".userMana").addClass("leftActive");
$(".accountInfo").css("color", "white");

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
    showAccount: function () {
        var length = program.userInfoList.length;
        program.html = "";
        for (var i = 0; i < length; i++) {
            var role;
            if (program.userInfoList[i].isAdmin == 1) {
                role = "管理员";
            } else if (program.userInfoList[i].isTeacher == 1) {
                role = "老师";
            } else {
                role = "学生";
            }

            if (program.userInfoList[i].lastLogin == null) {
                program.userInfoList[i].lastLogin = "";
            }
            var examTitle = program.examInfoList[i].title;
            var exmaId = program.examInfoList[i].examId;
            program.html += '<tr>'
                + '<td style="width:80px;"><input type="checkbox" value="' + program.userInfoList[i].userId + '" name="title"/></td>'
                + '<td>' + program.userInfoList[i].userId + '</td>'
                + '<td class="usernamet">' + program.userInfoList[i].username + '</td>'
                + '<td>' + program.userProfileList[i].realName + '</td>'
                + '<td>' + program.userProfileList[i].studentNumber + '</td>'
                + '<td>' + program.userProfileList[i].className + '</td>'
                + '<td>' + role + '</td>'
                + '<td>' + program.userInfoList[i].lastLogin + '</td>'
                + '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + examTitle
                + '"><a href="examInfo.html?id=' + exmaId + '">' + examTitle + '</a>'
                + '</td>'
                + '<td><a href="javascript:;"class="title" value="' + program.userInfoList[i].userId + '" >修改</a></td>'
                + '</tr>';
        }

    },
    //查询账号
    selectAccount: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                realName: program.srealname,
                isAdmin: program.sadmin,
                isTeacher: program.steacher,
                username: program.susername,
                studentNumber: program.sstudentNumber,
                isContainExamInfo: true,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                program.count = result.total;
                program.userInfoList = result.userInfoList;
                program.userProfileList = result.userProfileList;
                program.examInfoList = result.examInfoList;
                program.showAccount();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);
            }
        });
    },
    setValue: function () {
        $(".studentNum").val("");
        $(".realName").val("");
        $(".className").val("");
        $(".term").val("");
    },
    selectyn: function () {
        $(".isAdmin").append("<option value =" + 0 + ">否</option><option value =" + 1 + ">是</option>");
        $(".isTeacher").append("<option value =" + 0 + ">否</option><option value =" + 1 + ">是</option>");
    },
    setValueId: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                userId: program.userId,
            },
            success: function (result) {
                console.log(result);
                var admin, teacher;
                program.count = result.total;
                program.userInfoList = result.userInfoList;
                program.userProfileList = result.userProfileList;
                $(".isAdmin").empty();
                $(".isTeacher").empty();
                program.selectyn();
                $(".username").val(program.userInfoList[0].username);
                $(".schoolNumber").val(program.userProfileList[0].studentNumber);
                $(".isAdmin option[value=" + program.userInfoList[0].isAdmin + "]").attr("selected", true);
                $(".isTeacher option[value=" + program.userInfoList[0].isTeacher + "]").attr("selected", true);
            }
        });
    },
    //删除账号
    deleteUser: function (vals) {
        console.log(vals);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userInfo/deleteByIds",
            dataType: 'json',
            async: false,
            data: {
                ids: vals
            },
            success: function (result) {
                console.log(result);
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "删除成功！");
                    program.selectAccount();
                } else {
                    pubMeth.alertInfo("alert-danger", "删除失败！");
                }
            }
        });
    },
    //更新账号
    updateUser: function () {
        console.log(program.isAdmin);
        console.log(program.isTeacher);
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userInfo/updateById",
            dataType: 'json',
            async: false,
            data: {
                userId: program.userId,
                username: program.username,
                password: program.password,
                studentNumber: program.studentNumber,
                isTeacher: program.isTeacher,
                isAdmin: program.isAdmin,
            },
            success: function (result) {
                console.log(result);
                if (result.APIResult.status === 1) {
                    pubMeth.alertInfo("alert-success", "修改成功！");
                    program.selectAccount();
                    $('#user').modal('hide');

                } else if (result.APIResult.status === 0) {
                    pubMeth.alertInfo("alert-danger", "修改失败！");
                } else {
                    pubMeth.alertInfo("alert-danger", result.APIResult.desc);
                    /*pubMeth.alertInfo("alert-danger", "修改失败！");*/
                }
            }
        });
    },
    //添加账号
    addUser: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userInfo/insertOne",
            dataType: 'json',
            async: false,
            data: {
                username: program.username,
                password: program.password,
                studentNumber: program.studentNumber,
                admin: program.isAdmin,
                teacher: program.isTeacher,
            },
            success: function (result) {
                console.log(result);
                if (result.userInfoInsertRe.status == 1) {
                    program.selectAccount();
                    pubMeth.alertInfo("alert-success", "添加成功！");
                } /*else if (result.status == -1) {
                 pubMeth.alertInfo("alert-info", "无此对应学号的考生！");
                 } */ else {
                    pubMeth.alertInfo("alert-danger", result.userInfoInsertRe.desc);
                }
            }
        });
    }
};
pubMeth.getRowsnum("rowsnum");
program.selectAccount();
$(".addUser").click(function () {
    $('#user').modal({
        backdrop: 'static'
    });
    $(".username").val("");
    $(".schoolNumber").val("");
    $(".isAdmin").empty();
    $(".isTeacher").empty();
    program.selectyn();
    program.userId = "";
});
$(".closebutton").click(function () {
    program.setValue();
});
$(".saveData").click(function () {
    program.username = $(".username").val();
    program.password = $(".password").val();
    program.studentNumber = $(".schoolNumber").val();
    program.isAdmin = $(".isAdmin option:selected").val();
    program.isTeacher = $(".isTeacher option:selected").val();
    if (program.userId != "") {
        program.updateUser();
    } else {

        program.addUser();
        $("#user").modal('hide');
    }
});
$(".deleteUser").click(function () {
    var valArr = new Array;
    $(":checkbox[name='title']:checked").each(function (i) {
        valArr[i] = $(this).val();
    });
    var vals = valArr.join(',');// 转换为逗号隔开的字符串
    if (vals != "") {
        program.deleteUser(vals);
    } else {
        pubMeth.alertInfo("alert-info", "请先勾选删除项！");
    }
});
$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    program.sadmin = $(".admin option:selected").val();
    program.steacher = $(".teacher option:selected").val();
    program.susername = $(".susername").val();
    program.sstudentNumber = $(".sstudentNumber").val();
    console.log(program.sadmin);
    if (program.sadmin == "管理员") {
        program.sadmin = "";
    }
    console.log(program.sadmin);
    if (program.steacher == "老师") {
        program.steacher = "";
    }
    program.srealname = $(".searname").val();
    program.selectAccount();
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
            program.selectAccount();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}