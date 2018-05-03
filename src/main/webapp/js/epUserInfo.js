$(".applyMana").next(".treeview-menu").toggle("slow");
$(".applyMana").addClass("leftActive");
$(".epUserInfo").css("color", "white");

var program = {
    page: '1',
    //显示账号
    showAccount: function () {
        var length = program.epUserInfoList.length;
        program.html = "";
        for (var i = 0; i < length; i++) {
            var active = program.epUserInfoList[i].isActive, opate = "<td>";
            var userId = program.epUserInfoList[i].userId;
            if (active === 0) {
                opate += '<button type="button" class="btn btn-primary btn-xs activeUser" id="'
                    + userId + '">激活</button> '
            }
            opate += '<button type="button" class="btn btn-info btn-xs updateUser" id="'
                + i + '">修改</button>';
            program.html += '<tr>'
                + '<td style="width:80px;"><input type="checkbox" value="' + program.epUserInfoList[i].userId + '" name="title"/></td>'
                + '<td>' + program.epUserInfoList[i].userId + '</td>'
                + '<td class="usernamet">' + program.epUserInfoList[i].username + '</td>'
                + '<td>' + program.epUserInfoList[i].email + '</td>'
                + '<td>' + program.epUserInfoList[i].phone + '</td>'
                + '<td>' + program.epUserInfoList[i].unit + '</td>'
                + '<td>' + program.epUserInfoList[i].realName + '</td>'
                + '<td>' + program.epUserInfoList[i].createTime + '</td>'
                + opate
                + '</tr>';
        }
    },
    //查询账号
    selectAccount: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epUserInfo/selectByCondition.do",
            dataType: 'json',
            async: false,
            data: {
                username: program.susername,
                email: program.semail,
                realName: program.srealname,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                result = result.data;
                program.count = result.total;
                program.epUserInfoList = result.epUserInfoList;
                program.showAccount();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);
            }
        });
    },
    setValueId: function (id) {
        $(".musername").val(program.epUserInfoList[id].username);
        $(".memail").val(program.epUserInfoList[id].email);
        $(".mphone").val(program.epUserInfoList[id].phone);
        $(".munit").val(program.epUserInfoList[id].unit);
        $(".mrealName").val(program.epUserInfoList[id].realName);
    },
    //删除账号
    deleteUser: function (vals) {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epUserInfo/deleteByIds.do",
            dataType: 'json',
            async: false,
            data: {
                ids: vals
            },
            success: function (result) {
                console.log(result);
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", result.desc);
                    program.selectAccount();
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }
        });
    },
    //更新账号
    updateUser: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epUserInfo/updateById.do",
            dataType: 'json',
            async: false,
            data: {
                userId: program.userId,
                username: program.username,
                password: program.password,
                email: program.email,
                phone: program.phone,
                unit: program.unit,
                realName: program.realName
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", result.desc);
                    program.selectAccount();
                    $('#user').modal('hide');
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }
        });
    },
    //添加账号
    addUser: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epUserInfo/insertOne.do",
            dataType: 'json',
            async: false,
            data: {
                username: program.username,
                password: program.password,
                email: program.email,
                phone: program.phone,
                unit: program.unit,
                realName: program.realName
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    program.selectAccount();
                    pubMeth.alertInfo("alert-success", result.desc);
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }
        });
    },
    active: function (id) {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epUserInfo/active.do",
            dataType: 'json',
            async: false,
            data: {
                id: id
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", result.desc);
                    program.selectAccount();
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }
        });
    }
};

pubMeth.getRowsnum("rowsnum");
program.selectAccount();

$("#listInfo").on('click', '.activeUser', function () {
    program.active(this.id);
});
$("#listInfo").on('click', '.updateUser', function () {
    $('#user').modal({});
    program.userId = program.epUserInfoList[this.id].userId;
    program.setValueId(this.id);
});
$(".addUser").click(function () {
    $('#user').modal({});
    $(".musername").val("");
    $(".mpassword").val("");
    $(".memail").val("");
    $(".mphone").val("");
    $(".munit").val("");
    $(".mrealName").val("");
    program.userId = null;
});
$(".saveData").click(function () {
    program.username = $(".musername").val();
    program.password = $(".mpassword").val();
    program.email = $(".memail").val();
    program.phone = $(".mphone").val();
    program.unit = $(".munit").val();
    program.realName = $(".mrealName").val();
    if (program.userId !== null) {
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
    program.susername = $(".susername").val();
    program.semail = $(".semail").val();
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