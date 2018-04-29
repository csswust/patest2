$(".userMana").next(".treeview-menu").toggle("slow");
$(".userMana").addClass("leftActive");
$(".academyInfo").css("color", "white");
var program = {
    page: '1',
    schoolName: '',
    majorName: '',
    id: '',
    school: [],
    major: [],
    count: '',
    selectbyId: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../academyInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                acaId: program.id
            },
            success: function (result) {
                console.log(result);
                $(".schoolName").val(result.list[0].academyName);
            },
            error: function () {
                console.log("error");
            }
        });
    },
    getAcademyInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../academyInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                program.count = result.total;
                $("#listInfo").empty();
                var length = result.list.length;
                for (var i = 0; i < length; i++) {
                    program.school.push(result.list[i].academyName);
                    /*program.major.push(result.list[i].majorName);*/
                    $("#listInfo").append('<tr>' +
                        '<td style="width:80px;"><input type="checkbox" value="' + result.list[i].acaId + '" name="title"/></td>' +
                        '<td>' + result.list[i].acaId + '</td>' +
                        '<td>' + result.list[i].academyName + '</td>' +
                        /*'<td>' + result.list[i].majorName + '</td>' +*/
                        '<td><a href="javascript:;" class="title" value="' + result.list[i].acaId + '">修改</a></td>' +
                        '</tr>');
                }
                $("#listInfo tr").each(function (i) {
                    $("td:last a.title", this).click(function () {
                        program.id = $(this).attr("value");
                        $('#major').modal({
                            //backdrop: 'static'
                        });
                        $(".majorName").empty();
                        $(".schoolName").empty();
                        program.selectbyId();
                        isAdd = false;
                    });
                });
            },
            error: function () {
                console.log("error");
            }
        });
    },
    addAcademyInfo: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../academyInfo/insertOne",
            dataType: 'json',
            async: false,
            data: {
                /*"academyInfo.majorName": program.majorName,*/
                "academyName": program.schoolName
            },
            success: function (result) {
                console.log(result);
                if (result.status > 0) {
                    $("#major").modal('hide');
                    program.getAcademyInfo();
                    pubMeth.alertInfo("alert-success", "添加成功！");
                }
            },
            error: function () {
                console.log("error");
            }
        });
    },
    deleteAcademyInfo: function (vals) {
        console.log(vals);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../academyInfo/deleteByIds",
            dataType: 'json',
            async: false,
            data: {
                ids: vals
            },
            success: function (result) {
                console.log(result);
                if (result.status > 0) {
                    pubMeth.alertInfo("alert-success", "删除成功！");
                    program.getAcademyInfo();
                } else {
                    pubMeth.alertInfo("alert-danger", "删除失败！");
                }
            }
        });
    },
    updateAcademyInfo: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../academyInfo/updateById",
            dataType: 'json',
            async: false,
            data: {
                /*"record.majorName": program.majorName,*/
                "academyName": program.schoolName,
                "acaId": program.id
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    $("#major").modal('hide');
                    program.getAcademyInfo();
                    pubMeth.alertInfo("alert-success", "修改成功！");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求失败！");
            }
        });
    }
};
pubMeth.getRowsnum("rowsnum");
program.getAcademyInfo();
var isAdd;
$(".add").click(function () {
    $("#major").modal();
    $(".majorName").val("");
    $(".schoolName").val("");
    isAdd = true;
});
$(".saveData").click(function () {
    program.majorName = $(".majorName").val();
    program.schoolName = $(".schoolName").val();
    console.log(isAdd);
    if (isAdd == true) {
        program.addAcademyInfo();
    } else {
        program.updateAcademyInfo();
    }
});
$("#delete").click(function () {
    var valArr = new Array;
    $(":checkbox[name='title']:checked").each(function (i) {
        valArr[i] = $(this).val();
    });
    var vals = valArr.join(',');// 转换为逗号隔开的字符串
    if (vals != "") {
        program.deleteAcademyInfo(vals);
    } else {
        pubMeth.alertInfo("alert-info", "请先勾选删除项！");
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
            program.getAcademyInfo();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}