$(".courseMana").next(".treeview-menu").toggle("slow");
$(".courseMana").addClass("leftActive");
$(".knowLeage").css("color", "white");

var program = {
    courseName: [],
    sumList: [],
    course: [],
    kName: '',
    knowId: '',
    kIds: [],
    id: [],
    chval: '0',
    courselength: "",
    parentId: '',
    page: '1',
    count: '',
    selectbyId: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../knowledgeInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                'knowId': program.id,
                "containSum": false
            },
            success: function (result) {
                console.log(result);
                $(".courseName").empty();
                $(".courseName").append("<option>课程</option>");
                program.selectOnlyCourse();
                for (var i = 0; i < program.courseName.length; i++) {
                    $(".courseName").append("<option value=" + program.courseName[i].couId + ">" + program.courseName[i].courseName + "</option>");
                }
                /*if (result.knowledgeInfoList[0].isCourse == 1) {*/
                /*$(".isCourse").show();
                 $(".isknow").hide();
                 /!*$(".choose option[value="+ result.data[0].isCourse +"]").attr("selected",true);*!/
                 $(".choose").val(1);
                 $(".coName").val(result.courseList[0].knowName);
                 } else {*/
                /*                        $(".isCourse").hide();
                 $(".isknow").show();*/
                /*$(".choose option[value="+ result.data[0].isCourse +"]").attr("selected",true);*/
                $(".choose").val(0);
                $(".courseName option[value=" + result.knowledgeInfoList[0].courseId + "]").attr("selected", true);
                $(".knowName").val(result.knowledgeInfoList[0].knowName);
                /*}*/
            },
            error: function () {
                console.log("error");
            }
        });
    },
    getCourseInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../knowledgeInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {},
            sumList: {},
            success: function (result) {
                console.log(result);
                /*var length = result.total;*/
                program.count = result.total;
                $("#ClistInfo").empty();
                $("#datalistInfo").empty();
                $("#algorithmlistInfo").empty();
                program.courseName = result.courseInfoList;
                program.sumList = result.sumList;
                program.course = result.knowledgeInfoList;
                for (var i = 0; i < result.knowledgeInfoList.length; i++) {
                    /*if (result.data[i].isCourse == 0) {*/
                    if (result.courseInfoList[i].courseName == 'C程序设计') {
                        $("#ClistInfo").append('<li class="list-group-item">' +
                            '<span><input type="checkbox" value="' + program.course[i].knowId + '" name="title"/></span>' +
                            '<span ><a href="problem.html?id=' + program.course[i].knowId + '">' + program.course[i].knowName + '</a></span>' +
                            '<span style="float:right;"><a href="javascript:;" class="title" value="' + program.course[i].knowId + '">修改</a></span>' +
                            '<span style="float:right;margin-right:30px;">' + program.sumList[i] + '</span>' +
                            '</li>');
                    } else if (result.courseInfoList[i].courseName == '数据结构') {
                        $("#datalistInfo").append('<li class="list-group-item">' +
                            '<span><input type="checkbox" value="' + program.course[i].knowId + '" name="title"/></span>' +
                            '<span style="width:100px;"><a href="problem.html?id=' + program.course[i].knowId + '">' + program.course[i].knowName + '</a></span>' +
                            '<span style="float:right;"><a href="javascript:;" class="title" value="' + program.course[i].knowId + '">修改</a></span>' +
                            '<span style="float:right;margin-right:30px;">' + program.sumList[i] + '</span>' +
                            '</li>');
                    } else if (result.courseInfoList[i].courseName == '算法分析') {
                        $("#algorithmlistInfo").append('<li class="list-group-item">' +
                            '<span><input type="checkbox" value="' + program.course[i].knowId + '" name="title"/></span>' +
                            '<span style="width:100px;"><a href="problem.html?id=' + program.course[i].knowId + '">' + program.course[i].knowName + '</a></span>' +
                            '<span style="float:right;"><a href="javascript:;" class="title" value="' + program.course[i].knowId + '">修改</a></span>' +
                            '<span style="float:right;margin-right:30px;">' + program.sumList[i] + '</span>' +
                            '</li>');
                    }
                    /*}*/
                }
                $("#ClistInfo li").each(function (i) {
                    $("a.title", this).click(function () {
                        program.id = $(this).attr("value");
                        console.log(i);
                        $('#addKnow').modal({
                            backdrop: 'static'
                        });
                        $(".coName").empty();
                        $(".knowName").empty();
                        $(".courseName").empty();
                        program.selectbyId();
                        isAdd = false;
                    });
                });

                $("#datalistInfo li").each(function (i) {
                    $("a.title", this).click(function () {
                        program.id = $(this).attr("value");
                        console.log(i);
                        $('#addKnow').modal({
                            backdrop: 'static'
                        });
                        $(".coName").empty();
                        $(".knowName").empty();
                        $(".courseName").empty();
                        program.selectbyId();
                        isAdd = false;
                    });
                });

                $("#algorithmlistInfo li").each(function (i) {
                    $("a.title", this).click(function () {
                        program.id = $(this).attr("value");
                        console.log(i);
                        $('#addKnow').modal({
                            backdrop: 'static'
                        });
                        $(".coName").empty();
                        $(".knowName").empty();
                        $(".courseName").empty();
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
    selectOnlyCourse: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../courseInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                /*'knowledge.isCourse': 1,*/
            },
            success: function (result) {
                console.log(result);
                program.courseName = result.list;
            }
        });
    },
    addKnow: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../knowledgeInfo/insertOne",
            dataType: 'json',
            async: false,
            data: {
                "knowName": program.kName,
                /*"knowledge.isCourse": program.chval,*/
                "courseId": program.parentId
            },
            success: function (result) {
                console.log(result);
                if (result.status == "1") {
                    $("#addKnow").modal('hide');
                    pubMeth.alertInfo("alert-success", "添加成功！");
                    program.getCourseInfo();
                }
                else if (result.status == "9") {
                    $("#addKnow").modal('hide');
                    pubMeth.alertInfo("alert-danger", "添加重复！");
                }
                else {
                    $("#addKnow").modal('hide');
                    pubMeth.alertInfo("alert-danger", "添加失败！");
                }
            }
        });
    },
    deleteKnow: function (vals) {
        if (confirm("你确定要删除吗？")) {
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../knowledgeInfo/deleteByIds",
                dataType: 'json',
                async: false,
                data: {
                    ids: vals
                },
                success: function (result) {
                    console.log(result);
                    if (result.status > 0) {
                        pubMeth.alertInfo("alert-success", "删除成功！");
                        program.getCourseInfo();
                    } else {
                        pubMeth.alertInfo("alert-danger", "删除失败！");
                    }
                }
            });

        }
    },
    updateKnow: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../knowledgeInfo/updateById",
            dataType: 'json',
            async: false,
            data: {
                "knowName": program.kName,
                /*"knowledge.isCourse": program.chval,*/
                "courseId": program.parentId,
                "knowId": program.id
            },
            success: function (result) {
                console.log(result);
                if (result.status == 1) {
                    $("#addKnow").modal('hide');
                    program.getCourseInfo();
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
program.getCourseInfo();
var isAdd;
$(".add").click(function () {
    isAdd = true;
    $('#addKnow').modal();
    $(".courseName").empty();
    $(".courseName").append("<option>课程</option>");
    program.selectOnlyCourse();
    for (var i = 0; i < program.courseName.length; i++) {
        $(".courseName").append("<option value=" + program.courseName[i].couId + ">" + program.courseName[i].courseName + "</option>");
    }
});
$(".delete").click(function () {
    var valArr = new Array;
    $(":checkbox[name='title']:checked").each(function (i) {
        /*console.log(this.id);*/
        valArr[i] = $(this).val();
    });
    var vals = valArr.join(',');// 转换为逗号隔开的字符串
    if (vals != "") {
        program.deleteKnow(vals);
    } else {
        pubMeth.alertInfo("alert-info", "请先勾选删除项！");
    }
});
$(".saveData").click(function () {
    /*program.chval = $(".choose option:selected").val();*/
    if (program.chval == 1) {
        program.kName = $(".coName").val();
        if (program.kName == "") {
            alert("带*号的为必填项");
        } else {
            console.log(isAdd);
            if (isAdd == true) {
                program.addKnow();
                /*$("#addKnow").modal('hide');*/
            } else {
                program.updateKnow();
            }
        }
    } else {
        program.parentId = $(".courseName option:selected").val();
        program.kName = $(".knowName").val();
        if (program.parentId == "" || program.parentId == "选择课程" || program.kName == "") {
            alert("带*号的为必填项");
        } else {
            /*program.addKnow();*/
            /*$("#addKnow").modal('hide');;*/
            console.log(isAdd);
            if (isAdd == true) {
                program.addKnow();
                /*$("#addKnow").modal('hide');*/
            } else {
                program.updateKnow();
            }
        }
    }
});
$(".isCourse").hide();
$(".choose").change(function () {
    program.chval = $(".choose option:selected").val();
    if (program.chval == 1) {
        $(".isCourse").show();
        $(".isknow").hide();
    } else {
        $(".isCourse").hide();
        $(".isknow").show();
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
            program.getCourseInfo();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}