define(function (require, exports, module) {
    /*	require('jquery');*/
    require('../js/common.js');
    require('paginator');
    require('bootstrap');
    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    $(".quesMana").next(".treeview-menu").toggle("slow");
    $(".quesMana").addClass("leftActive");
    $(".problemList").css("color", "white");
    $(".check_list").click(function () {
        if (this.checked) {
            $("#listInfo").find("input[type='checkbox']").prop("checked", true);
        } else {
            $("#listInfo").find("input[type='checkbox']").prop("checked", false);
        }
    });
    var program = {
        count: '',
        title: '',
        page: '1',
        level: '',
        know: '',
        data: '',
        getProblemInfo: function () {
            console.log("get" + program.know);
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../problemInfo/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    knowId: program.know,
                    page: program.page,
                    rows: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    program.count = result.total;
                    program.data = result;
                    program.showProblem();
                }, error: function () {
                    pubMeth.alertInfo("alert-info", "请求错误");
                }
            });
        },
        showProblem: function () {
            $("#listInfo").empty();
            var length = program.data.data.length;
            var flag = 1;
            for (var i = 0; i < length; i++) {
                var difficult;
                if (program.data.data[i].levelId == 1) {
                    difficult = "容易";
                } else if (program.data.data[i].levelId == 2) {
                    difficult = "中等";
                } else if (program.data.data[i].levelId == 3) {
                    difficult = "困难";
                }
                var courseName = "";
                var knowName = "";
                var testNum = 0;
                if (program.data.course[i] != null) {
                    courseName = program.data.course[i].courseName;
                }
                if (program.data.knowledge[i] != null) {
                    knowName = program.data.knowledge[i].knowName;
                }
                if (program.data.data[i] != null) {
                    testNum = program.data.data[i].testdataNum;
                }
                $("#listInfo").append('<tr>' +
                    '<td><input type="checkbox" value="' + program.data.data[i].probId + '" name="title"/></td>' +
                    '<td>' + program.data.data[i].probId + '</td>' +
                    '<td><a href="question.html?id=' + program.data.data[i].probId + '"  class="title">' + program.data.data[i].title + '</a></td>' +
                    '<td class="garde">' + difficult + '</td>' +
                    '<td>' + program.data.data[i].acceptedNum + '/' + program.data.data[i].submitNum + '</td>' +
                    '<td>' + program.data.data[i].createTime + '</td>' +
                    '<td>' + courseName + '</td>' +
                    '<td>' + knowName + '</td>' +
                    '<td>' + testNum + '</td>' +
                    '</tr>');
                flag++;
            }
        },
        deleteIt: function () {
            $('#delete').on('click', function (e) {
                var valArr = new Array;
                $(":checkbox[name='title']:checked").each(function (i) {
                    valArr[i] = $(this).val();
                });
                var vals = valArr.join(',');// 转换为逗号隔开的字符串
                if (vals != "") {
                    if (confirm("你确定要删除这些" + vals + "题目吗？")) {
                        $.ajax({
                            type: "get",
                            content: "application/x-www-form-urlencoded;charset=UTF-8",
                            url: "../problemInfo/deleteByIds",
                            dataType: 'json',
                            async: false,
                            data: {
                                ids: vals
                            },
                            success: function (result) {
                                if (result.status > 0) {
                                    pubMeth.alertInfo("alert-success", "删除成功！");
                                    program.getProblemInfo();
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
        alertInfo: function (className, info) {
            if ($(".tip").text().trim() == "") {
                $(".tip").html(' <div class="alert  ' + className + '" style="margin-top:10px;" id="tip">' +
                    '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                    '<strong>' + info + '</strong></div>');
            } else {
                $("#tip").removeClass();
                $("#tip").addClass("alert " + className);
                $("strong").text(info);
            }
        },
        searchProblem: function () {
            console.log("search" + program.know);
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../problemInfo/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    "title": program.title,
                    "levelId": program.level,
                    "probId": program.probId,
                    knowId: program.know,
                    page: program.page,
                    rows: pubMeth.rowsnum
                },
                success: function (result) {
                    program.count = result.total;
                    program.data = result;
                    program.showProblem();
                },
                error: function () {
                    pubMeth.alertInfo("alert-info", "请求错误");
                }
            });
        },
        queryCondition: function () {//为真为有查询条件，为假为无条件查询
            program.title = $(".searTitle").val().trim();
            program.probId = $(".searTitleNum").val().trim();
            program.level = $(".level option:selected").val();
            program.know = $(".knowName option:selected").val();
            if (program.know == "知识点")
                program.know = "";
            if (program.know == "难度")
                program.level = "";
            if (parm["id"] != null) {
                program.know = parm["id"];
            }
            if (program.title == "" && program.level == "" && program.know == "" && program.probId == "") {
                return false;
            }
            return true;
        }
    };
    pubMeth.getRowsnum("rowsnum");
    var parm = pubMeth.getQueryObject();
    if (parm["id"] != null) {
        program.know = parm["id"];
    }
    program.getProblemInfo();
    program.deleteIt();
    pubMeth.serCourse();

    $(".search").click(function () {
        program.page = "1";
        if (program.queryCondition()) {
            program.searchProblem();
        } else {
            program.getProblemInfo();
        }
    });
    $(".addProblem").click(function () {
        window.location.href = "question.html";
    });
    $(".reset").click(function () {
        $(".searTitleNum").val("");
        $(".searTitle").val("");
        $(".courseName option:first").prop("selected", true);
        $(".knowName").empty().append('<option value="">知识点</option>');
        $(".level option:first").prop("selected", true);
        program.page = "1";
        program.know = "";
        program.getProblemInfo();
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
                if (program.queryCondition()) {
                    program.searchProblem();
                } else {
                    program.getProblemInfo();
                }
            }
        });
    } else {
        $(".pagenum").css("display", "none");
    }
});