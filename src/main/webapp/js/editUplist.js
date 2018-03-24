define(function (require, exports, module) {
    require('jquery');
    require('jCookie');
    require('bootstrap');
    require('paginator');
    require('jSession');
    require('../js/common.js');
    require('fileupload');

    $(".examMana").next(".treeview-menu").toggle("slow");
    $(".examMana").addClass("leftActive");
    $(".examList").css("color", "white");
    $(".stlist").addClass("on");

    var pubMeth = require('../js/public.js');
    var program = {
        page: '1',
        examId: '',
        html: '',
        examPaper: '',
        userInfo: '',
        userProfile: '',
        count: '',
        path: '',
        fileName: '',
        expmArr: '',
        expmIds: [],
        expmId: '',
        showInfo: function () {
            var length = program.userInfo.length;
            if (length != 0) {
                var stulist = '<tr>'
                    + '<th>序号</th>'
                    + '<th>姓名</th>'
                    + '<th>学号</th>'
                    + '<th>考号</th>'
                    + '<th>班级</th>'
                    + '<tr>';
                $("#stuhead").html(stulist);
            }
            program.html = "";
            for (var i = 0; i < length; i++) {
                var order = i + 1;
                program.html += '<tr>'
                    + '<td>' + order + '</td>'
                    + '<td>' + program.userProfile[i].realName + '</td>'
                    + '<td>' + program.userProfile[i].studentNumber + '</td>'
                    + '<td>' + program.userInfo[i].username + '</td>'
                    + '<td>' + program.userProfile[i].className + '</td>'
                    + '</tr>';
            }
        },
        //上传学生名单
        importList: function () {
            console.log(123);
            $.ajaxFileUpload({
                url: "../examPaper/uploadUserByExamId",
                secureuri: false,
                fileElementId: "namefile",// 文件选择框的id属性
                dataType: "json",
                data: {
                    examId: program.examId
                },
                success: function (result) {
                    console.log(result);
                    if (result.loadResult.status > 0) {
                        pubMeth.alertInfo("alert-success", "上传成功");
                        console.log(result.loadResult.path);
                        program.path = result.loadResult.dirPath;
                        program.fileName = result.loadResult.fileName;
                        window.location.href = '../system/download?path=' + program.path + "&fileName=" + program.fileName;
                        program.selectUserBaseInfo();
                    } else {
                        pubMeth.alertInfo("alert-danger", result.loadResult.desc);
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "上传失败");
                }
            });
        },
        selectUserBaseInfo: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examPaper/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    examId: program.examId,
                    page: program.page,
                    rows: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    program.count = result.total;
                    program.examPaper = result.examPaperList;
                    program.userInfo = result.userInfoList;
                    program.userProfile = result.userProfileList;
                    program.showInfo();
                    $("#stuInfo").empty();
                    $("#stuInfo").append(program.html);
                    program.pagingFun();
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                }
            });
        },
        pagingFun: function () {
            if (program.count > 0) {
                $(".pagenum").css("display", "block");
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
                        program.selectUserBaseInfo();
                    }
                });
            } else {
                $(".pagenum").css("display", "none");
            }
        },
        change: function (tagert, className) {
            console.log('changed');
            $(tagert).change(function () {
                console.log(0);
                var path = $(this).val();
                var path1 = path.lastIndexOf("\\");
                var name = path.substring(path1 + 1);

                console.log(name);
                $(className).val(name);
            });
        },
    };
    pubMeth.getRowsnum("rowsnum");
    var par = pubMeth.getQueryObject();
    if (par.examId) {
        $(".pageName").text("添加考试");
        program.examId = par.examId;
    }
    if (par.Id) {
        $(".pageName").text("修改考试");
        program.examId = par.Id;
    }

    program.selectUserBaseInfo();
    //上传学生名单
    program.pagingFun();
    $(".importList").click(function () {
        $(".namefile").val("");
        $('#import').modal({
            backdrop: 'static'
        });
        program.change("input[type=file]", ".namefile");
    });

    $(".comImport").click(function () {
        program.importList();
    });
    $(".save").click(function () {
        if (par.examId) {
            if (program.userInfo) {
                window.location.href = "exam.html";
            } else {
                pubMeth.alertInfo("alert-warning", "考生名单没有上传");
            }
        } else if (par.Id) {
            if (program.userInfo) {
                window.location.href = "exam.html";
            } else {
                pubMeth.alertInfo("alert-warning", "考生名单没有上传");
            }
        }
    });
    //下载学生上传模板
    $(".downloada").attr("href", "../resource/考生信息导入模板.xls");
    //点击上一步返回题库
    $(".upList").click(function () {
        if (par.examId) {
            window.location.href = 'editParm.html?examId=' + program.examId;
        } else if (par.Id) {
            window.location.href = 'editParm.html?Id=' + program.examId;
        }
    });


});
