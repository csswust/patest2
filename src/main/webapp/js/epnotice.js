$(".applyMana").next(".treeview-menu").toggle("slow");
$(".applyMana").addClass("leftActive");
$(".epnotice").css("color", "white");
$(".notice").addClass("active");
var program = {
    page: "1",
    count: '',
    title: '',
    describle: '',
    addtime: '',
    data: '',
    html: '',
    epnoId: '',
    epnoIds: [],
    addNotice: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epNotice/insertOne",
            dataType: 'json',
            async: false,
            data: {
                content: program.describle,
                title: program.title
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "添加成功！");
                } else {
                    pubMeth.alertInfo("alert-danger", "添加失败！");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    selectId: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epNotice/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                epnoId: program.epnoId
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    $(".nottitle").val(result.data.list[0].title);
                    ue.ready(function () {
                        ue.setContent(result.data.list[0].content);
                    });
                } else {
                    pubMeth.alertInfo("alert-danger", "查询错误");
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
            url: "../epNotice/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                row: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    program.data = result.data.list;
                    program.count = result.data.total;
                    program.showNotice();
                    $("#listInfo").empty();
                    $("#listInfo").append(program.html);
                    $("#listInfo tr").each(function (i) {
                        $("td:last a.title", this).click(function () {
                            $('#addnotice').modal({
                                //backdrop: 'static'
                            });
                            program.epnoId = $(this).attr("value");
                            program.selectId();
                        });
                    });
                } else {
                    pubMeth.alertInfo("alert-danger", "查询错误");
                }
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
        for (var i = 0; i < length; i++) {
            program.html +=
                '<tr><td><input type="checkbox" value="' + program.data[i].epnoId + '" name="title"/></td>'
                + '<td>' + program.data[i].epnoId + '</td>'
                + '<td >' + program.data[i].title + '</td>'
                + '<td>' + program.data[i].createTime + '</td>'
                + '<td>' + program.data[i].modifyTime + '</td>'
                + '<td><a href="javascript:;" class="title" value="' + program.data[i].epnoId + '" >修改</a></td>'
                + '</tr>';
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
                if (confirm("你确定要删除这些" + vals + "公告吗？")) {
                    $.ajax({
                        type: "post",
                        content: "application/x-www-form-urlencoded;charset=UTF-8",
                        url: "../epNotice/deleteByIds",
                        dataType: 'json',
                        async: false,
                        data: {
                            ids: vals
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
                }
            } else {
                pubMeth.alertInfo("alert-info", "请先勾选删除项！");
            }
        });
    },
    /**
     * 更新公告
     */
    updateNotice: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epNotice/updateById",
            dataType: 'json',
            async: false,
            data: {
                epnoId: program.epnoId,
                content: program.describle,
                title: program.title
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "修改成功！");
                } else {
                    pubMeth.alertInfo("alert-danger", "修改失败！");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    }
};
var ue = UE.getEditor('notdes', {
    initialFrameWidth: 700,//初始化编辑器宽度,默认1000
    initialFrameHeight: 400,  //初始化编辑器高度,默认320
    scaleEnabled: true//设置不自动调整高度
});
pubMeth.getRowsnum("rowsnum");
var parm = pubMeth.getQueryObject();
program.examId = parm["id"];
program.selectNotice();
program.deleteNotice();

$(".addnotice").click(function () {
    $('#addnotice').modal({
        //backdrop: 'static'
    });
    $(".nottitle").val("");
    ue.ready(function () {
        ue.setContent("");
    });
    program.epnoId = "";
});
$(".savenot").click(function () {
    program.title = $(".nottitle").val();
    ue.ready(function () {
        program.describle = ue.getContent();
    });
    if (program.epnoId) {
        program.updateNotice();
    } else {
        if (program.title && program.describle) {
            program.addNotice();
        }
    }
    $('#addnotice').modal("hide");
    program.selectNotice();
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