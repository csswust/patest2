$(".examRoom").next(".treeview-menu").toggle("slow");
$(".examRoom").addClass("leftActive");
$(".examination").css("color", "white");
$(".notice").addClass("active");

var flag = 0;
var program = {
    page: "1",
    examId: '',
    count: '',
    html: '',
    title: '',
    stnoId: '',
    describle: '',
    addtime: '',
    data: '',
    addNotice: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examNotice/insertOne",
            dataType: 'json',
            async: false,
            data: {
                content: program.describle,
                title: program.title,
                examId: program.examId,
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    pubMeth.alertInfo("alert-success", "添加成功！");
                    $("#addnotice").modal('hide');
                    window.location.reload();
                } else {
                    pubMeth.alertInfo("alert-danger", "添加失败！");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    selectNotice: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examNotice/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                row: pubMeth.rowsnum,
                examId: program.examId,
            },
            success: function (result) {
                console.log(result);
                program.data = result.examNoticeList;
                program.count = result.total;
                program.showNotice();
                $("#listInfo").empty();
                $("#listInfo").append(program.html);

                $("#listInfo").on('click', '.show', function () {
                    var index = this.id.split("-")[0];
                    $("#updatenotice").modal();
                    $(".updatenotice").val(program.data[index].title);
                    $(".updatedes").val(program.data[index].content);
                    program.stnoId = program.data[index].exaNotId;
                });
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    showNotice: function () {
        program.html = "";
        var length = program.data.length;
        program.html = "";
        flag = 0;
        for (var i = 0; i < length; i++) {
            program.html +=
                '<tr><td><input type="checkbox" value="' + program.data[i].exaNotId + '" name="title"/></td>'
                + '<td>' + program.data[i].exaNotId + '</td>'
                + '<td class="show" id="' + flag + '-' + flag + '"><a class="title" >' + program.data[i].title + '</a></td>'
                + '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + program.data[i].content + '">' + program.data[i].content + '</td>'
                + '<td>' + program.data[i].createTime + '</td>'
                + '</tr>';
            flag++;
        }
    },
    deleteNotice: function () {
        $('.delnotice').on('click', function (e) {
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
                        url: "../examNotice/deleteByIds",
                        dataType: 'json',
                        async: false,
                        data: {
                            ids: vals,
                        },
                        success: function (result) {
                            if (result.status > 0) {
                                pubMeth.alertInfo("alert-success", "删除成功！");
                                program.selectNotice();
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
    updateNotice: function () {
        console.log(program.stnoId);
        console.log(program.describle);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examNotice/updateById",
            dataType: 'json',
            async: false,
            data: {
                exaNotId: program.stnoId,
                content: program.describle,
                title: program.title,
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    pubMeth.alertInfo("alert-success", "修改成功！");
                }
                $("#updatenotice").modal('hide');
                program.selectNotice();
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
};
pubMeth.getRowsnum("rowsnum");
var parm = pubMeth.getQueryObject();
program.examId = parm["id"];
program.selectNotice();
program.deleteNotice();

/*    $(".gradePrint").click(function () {
 //		program.selectGradeByExamId();
 window.location.href = "../exam/selectGradeByExamId?examId=" + program.examId;
 });
 $(".codePrint").click(function () {
 window.location.href = "../exam/selectCodeByExamId?examId=" + program.examId;
 });*/
//	program.getSubmitInfo();

$(".addnotice").click(function () {
    $('#addnotice').modal({
        backdrop: 'static'
    });
    $(".savenot").click(function () {
        program.title = $(".nottitle").val();
        program.describle = $(".notdes").val();
        if (program.title != null && program.describle != null) {
            program.addNotice();
        }
    });

});
/*$("#listInfo").on('click', '.show', function () {
 var index = this.id.split("-")[0];
 $("#updatenotice").modal();
 $(".updatenotice").val(program.data[index].title);
 $(".updatedes").val(program.data[index].content);
 program.stnoId = program.data[index].stnoId;

 });*/
$(".saveupdate").click(function () {
    program.title = $(".updatenotice").val();
    program.describle = $(".updatedes").val();
    console.log(program.describle);
    if (program.title != null && program.describle != null) {
        program.updateNotice();
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
            program.selectNotice();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}