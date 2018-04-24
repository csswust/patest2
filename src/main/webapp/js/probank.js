$(".quesMana").next(".treeview-menu").toggle("slow");
$(".quesMana").addClass("leftActive");
$(".problemList").css("color", "white");

var program = {
    count: '',
    title: '',
    page: '1',
    level: '',
    know: '',
    data: '',
    valArr: [],
    probIdArr: [],
    probtiArr: [],
    bankId: '',
    bankIds: '',
    probIdstr: '',
    titlestr: '',
    knowIdstr: '',
    problist: [],
    knowlist: [],
    probIdList: [],
    titleList: [],
    knowIdList: [],
    proinfoArr: [],
    getProblemInfo: function () {
        $.ajax({
            type: "get",
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
                result = result.data;
                program.count = result.total;
                program.data = result;
                program.showProblem();
            },
            error: function () {
                pubMeth.alertInfo("alert-info", "请求错误");
            }
        });
    },
    showProblem: function () {
        $("#listInfo").empty();
        $(".check_list").prop("checked", false);
        var length = program.data.data.length;
        var flag = 1;
        for (var i = 0; i < length; i++) {
            var difficult = "";
            var courseName = "";
            var knowName = "";
            if (program.data.data[i].levelId == 1) {
                difficult = "容易";
            } else if (program.data.data[i].levelId == 2) {
                difficult = "中等";
            } else if (program.data.data[i].levelId == 3) {
                difficult = "困难";
            }
            if (program.data.course[i] != null) {
                courseName = program.data.course[i].courseName;
            }
            if (program.data.knowledge[i] != null) {
                knowName = program.data.knowledge[i].knowName;
            }
            var str = '<tr>' + '<td><input type="checkbox" value="'
                + program.data.data[i].probId + ','
                + program.data.data[i].title + '"';
            if (program.bankIds.indexOf(program.data.data[i].probId) !== -1) {
                str += 'checked';
            }
            str += ' name="title" class="show"/></td>' + '<td>'
                + program.data.data[i].probId + '</td>'
                + '<td><a href="question.html?id='
                + program.data.data[i].probId + '"  class="title">'
                + program.data.data[i].title + '</a></td>'
                + '<td>' + difficult + '</td>' + '<td>'
                + program.data.data[i].acceptedNum + '/'
                + program.data.data[i].submitNum + '</td>'
                + '<td>' + program.data.data[i].createTime
                + '</td>' + '<td>' + courseName + '</td>'
                + '<td>' + knowName + '</td>' + '</tr>'
            $("#listInfo").append(str);
            flag++;
        }
        program.checkbank();
    },
    alertInfo: function (className, info) {
        if ($(".tip").text().trim() == "") {
            $(".tip").html(' <div class="alert'
                + className
                + '"style="margin-top:10px;" id="tip">'
                + '<a href="#" class="close" data-dismiss="alert">&times;</a>'
                + '<strong>' + info + '</strong></div>');
        } else {
            $("#tip").removeClass();
            $("#tip").addClass("alert " + className);
            $("strong").text(info);
        }
    },
    searchProblem: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../problemInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                "title": program.title,
                "levelId": program.level,
                knowId: program.know,
                page: program.page,
                rows: "10"
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
    queryCondition: function () {// 为真为有查询条件，为假为无条件查询
        program.title = $(".searTitle").val().trim();
        program.level = $(".level option:selected").val();
        program.know = $(".knowName option:selected").val();
        if (program.know == "知识点")
            program.know = "";
        if (program.know == "难度")
            program.level = "";
        if (parm["id"] != null) {
            program.know = parm["id"];
        }
        if (program.title == "" && program.level == ""
            && program.know == "") {
            return false;
        }
        return true;
    },
    getproblemInfoById: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../problemInfo/selectByIds",
            dataType: 'json',
            async: false,
            data: {
                ids: program.bankIds,
            },
            success: function (result) {
                console.log(result);
                program.problist = result.problemInfoList;
                program.getproinfo();
            },
        });
    },
    getproinfo: function () {
        var length = program.problist.length;
        for (var i = 0; i < length; i++) {
            program.probIdList[i] = program.problist[i].probId;
        }
        program.probIdstr = program.probIdList.join(",");
    },
    // 将所选的问题题库添加到数据库
    insertTempProblem: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examProblem/insertByArray",
            dataType: 'json',
            async: false,
            data: {
                probIdList: program.probIdstr,
                examId: program.examId
            },
            success: function (result) {
                console.log(result);
                if (result.status >= 1) {
                    if (parm.examId) {
                        window.location.href = "editBank.html?examId=" + parm.examId;
                    } else if (parm.Id) {
                        window.location.href = "editBank.html?Id=" + parm.Id;
                    }
                    pubMeth.alertInfo("alert-success", "保存成功");
                } else if (result.status == 0) {
                    $("#modaladdbank").modal('hide');
                    pubMeth.alertInfo("alert-danger", "保存失败,请查看是否有相同题目");
                } else if (result.status == -1) {
                    $("#modaladdbank").modal('hide');
                    pubMeth.alertInfo("alert-danger", "不允许添加相同的题目");
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求失败");
            }
        });
    },
    showbank: function () {
        $(".proinfo").empty();
        for (var i = 0; i < program.proinfoArr.length; i++) {
            $(".proinfo").show();
            $(".proinfo").append(
                '<span>' + program.proinfoArr[i] + ';' + '</span>');
        }
        program.getIds();
    },
    getIds: function () {
        var proIds = new Array();
        console.log(program.proinfoArr);
        for (var i = 0; i < program.proinfoArr.length; i++) {
            proIds[i] = program.proinfoArr[i].split(",")[0];
        }
        program.bankIds = proIds.join(",");
        console.log(program.bankIds);
    },
    // 全选
    allcheckbank: function () {
        $(".check_list").click(function () {
            if (this.checked) {
                $("#listInfo").find("input[type='checkbox']").prop("checked", true);
                $(".show").each(function (i) {
                    var val = $(this).val();
                    if (program.proinfoArr.indexOf(val) === -1) {
                        program.proinfoArr.push($(this).val());
                    }
                });
                program.showbank();
            } else {
                $("#listInfo").find("input[type='checkbox']").prop("checked", false);
                $(".show").each(function (i) {
                    program.proinfoArr.pop();
                });
                program.showbank();
            }
        });
    },
    // 单个选
    checkbank: function () {
        $("input[name='title']").click(function () {
            if ($(this).prop("checked") == true) {
                program.proinfoArr.push($(this).val());
                program.showbank();
            } else {
                program.proinfoArr.splice($.inArray($(this).val(), program.proinfoArr), 1);
                $(".proinfo").empty();
                program.showbank();
            }
        });
    },
};
pubMeth.getRowsnum("rowsnum");
var parm = pubMeth.getQueryObject();
if (parm["id"] != null) {
    program.know = parm["id"];
}
program.getProblemInfo();
pubMeth.serCourse();
program.allcheckbank();
$('.addbank').on('click', function (e) {
    if (program.bankIds != "") {
        if (parm.examId) {
            $("#modaladdbank").modal({
                backdrop: 'static'
            });
            $(".confirmadd").click(function () {
                program.getproblemInfoById();
                program.examId = parm["examId"];
                program.insertTempProblem();
            });
        } else if (parm.Id) {
            $("#modaladdbank").modal({
                backdrop: 'static'
            });
            $(".confirmadd").click(function () {
                program.getproblemInfoById();
                program.examId = parm["Id"];
                program.insertTempProblem();
            });
        }
    } else {
        pubMeth.alertInfo("alert-info", "请先勾选添加项！");
    }
});
$(".backexam").click(function () {
    if (parm.examId) {
        window.location.href = "editBank.html?examId="
            + parm.examId;
    } else if (parm.Id) {
        window.location.href = "editBank.html?Id="
            + parm.Id;
    }
});
$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    if (program.queryCondition()) {
        program.searchProblem();
    } else {
        program.getProblemInfo();
    }
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