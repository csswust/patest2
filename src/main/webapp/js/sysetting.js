$(".sysMana").next(".treeview-menu").toggle("slow");
$(".sysMana").addClass("leftActive");
$(".systemset").css("color", "white");
$(".footer p").css('display', 'block');
$(".footer p").hover(function () {
    $(this).css({"color": "#333", "cursor": "default"});
});

var program = {
    faqs: '',
    index: '',
    examNotes: '',
    dataName: '',
    data: [],
    rowsnum: '',
    page: "1",
    infoId: "",
    infoName: "",
    infoContent: "",
    modifyTime: "",
    selectById: function (id) {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../siteInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                siteId: id
            },
            success: function (result) {
                console.log(result);
                program.content = result.data[0].value;
                program.display = result.data[0].display;
            }
        });
    },
    getSiteInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../siteInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                if (result.status == "1") {
                    program.count = result.total;
                    var length = result.data.length;
                    program.html = "";
                    for (var i = 0; i < length; i++) {
                        var content = "";
                        if (result.data[i].type === 1) content = result.data[i].value;
                        else content = "通过修改查看";
                        program.html += '<tr>'
                            + '<td>' + result.data[i].siteId + '</td>'
                            + '<td>' + result.data[i].name + '</td>'
                            + '<td>' + result.data[i].display + '</td>'
                            + '<td>' + content + '</td>'
                            + '<td>' + result.data[i].modifyTime + '</td>'
                            + '<td><a href="javascript:;" class="title" value="'
                            + result.data[i].siteId + '"' + 'stype="' + result.data[i].type + '"' + '>修改</a></td>'
                            + '</tr>';
                    }
                    $("#listInfo").empty();
                    $("#listInfo").append(program.html);
                    $("#listInfo tr").each(function (i) {
                        $("td:last a.title", this).click(function () {
                            program.siteId = $(this).attr("value");
                            program.stype = $(this).attr("stype");
                            program.selectById(program.siteId);
                            if (program.stype == 1) {
                                $('#oneInput').modal({
                                    //backdrop: 'static'
                                });
                                $(".display").val(program.display);
                                $(".parameterValue").val(program.content);
                            }
                            if (program.stype == 2) {
                                $('#settingdata').modal({
                                    //backdrop: 'static'
                                });
                                $("#myModalLabe2").text(program.display);
                                ue.ready(function () {
                                    ue.setContent(program.content);
                                });
                            }
                        });
                    });
                }
            }
        });
    },
    update: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../siteInfo/updateById",
            dataType: 'json',
            async: false,
            data: {
                siteId: program.siteId,
                value: program.content,
                display: program.display
            },
            success: function (result) {
                console.log(result);
                if (result.status != "0") {
                    pubMeth.alertInfo("alert-success", "修改成功");
                } else {
                    pubMeth.alertInfo("alert-warning", "修改失败");
                }
            }
        });
    },
    refreshconfig: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../system/refreshConfig",
            dataType: 'json',
            async: false,
            data: null,
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", result.desc);
                } else {
                    pubMeth.alertInfo("alert-warning", "修改失败");
                }
            }
        });
    }
};


pubMeth.getRowsnum("rowsnum");
program.getSiteInfo();

var ue = UE.getEditor('data', {
    initialFrameWidth: 1170,//初始化编辑器宽度,默认1000
    initialFrameHeight: 400,  //初始化编辑器高度,默认320
    scaleEnabled: true//设置不自动调整高度
});

$(".refreshConfig").click(function () {
    program.refreshconfig();
});
$(".save").click(function () {
    ue.ready(function () {
        program.content = ue.getContent();
    });
    if (program.siteId != "") {
        program.update();
    }
    $('#settingdata').modal("hide");
    program.getSiteInfo();
});
$(".saveData").click(function () {
    program.display = $(".display").val();
    program.content = $(".parameterValue").val();
    console.log(program.siteId);
    if (program.siteId != "") {
        program.update();
    }
    $("#oneInput").modal('hide');
    program.getSiteInfo();
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
            program.getSiteInfo();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}