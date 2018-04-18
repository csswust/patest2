var addUplist = {
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
    init: function () {
        commonet.init(); // 公共模块初始化

        $(".examMana").next(".treeview-menu").toggle("slow");
        $(".examMana").addClass("leftActive");
        $(".examList").css("color", "white");
        $(".stlist").addClass("on");
        $(".mytest").addClass("onet");

        patest.getRowsnum("rowsnum");
        var par = patest.getQueryObject();
        if (par.Id) {
            $(".pageName").text("修改考试");
            addUplist.examId = par.Id;
        } else {
            patest.alertInfo("alert-danger", "考试id不存在");
            return;
        }
        addUplist.selectUserBaseInfo();
        //上传学生名单
        addUplist.pagingFun();
        $(".importList").click(function () {
            $(".namefile").val("");
            $('#import').modal({
                backdrop: 'static'
            });
            addUplist.change("input[type=file]", ".namefile");
        });

        $(".comImport").click(function () {
            addUplist.importList();
        });
        $(".save").click(function () {
            if (par.examId) {
                if (addUplist.userInfo) {
                    window.location.href = "exam.html";
                } else {
                    patest.alertInfo("alert-warning", "考生名单没有上传");
                }
            } else if (par.Id) {
                if (addUplist.userInfo) {
                    window.location.href = "exam.html";
                } else {
                    patest.alertInfo("alert-warning", "考生名单没有上传");
                }
            }
        });
        //下载学生上传模板
        $(".downloada").attr("href", "../system/download?path=/static/考生信息导入模板.xls&isUeditorPath=true");
        //点击上一步返回题库
        $(".upList").click(function () {
            if (par.examId) {
                window.location.href = 'editParm.html?examId=' + addUplist.examId;
            } else if (par.Id) {
                window.location.href = 'editParm.html?Id=' + addUplist.examId;
            }
        });

    },
    showInfo: function () {
        var length = addUplist.userInfo.length;
        if (length != 0) {
            var stulist = '<tr>'
                + '<th>序号</th>'
                + '<th>姓名</th>'
                + '<th>学号</th>'
                + '<th>考号</th>'
                + '<th>班级</th>'
                + '<th>考场</th>'
                + '<tr>';
            $("#stuhead").html(stulist);
        }
        addUplist.html = "";
        for (var i = 0; i < length; i++) {
            var order = i + 1;
            addUplist.html += '<tr>'
                + '<td>' + order + '</td>'
                + '<td>' + addUplist.userProfile[i].realName + '</td>'
                + '<td>' + addUplist.userProfile[i].studentNumber + '</td>'
                + '<td>' + addUplist.userInfo[i].username + '</td>'
                + '<td>' + addUplist.userProfile[i].className + '</td>'
                + '<td>' + addUplist.examPaper[i].classroom + '</td>'
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
                examId: addUplist.examId
            },
            success: function (result) {
                console.log(result);
                if (result.loadResult.status > 0) {
                    patest.alertInfo("alert-success", "上传成功");
                    console.log(result.loadResult.path);
                    addUplist.path = result.loadResult.dirPath;
                    addUplist.fileName = result.loadResult.fileName;
                    window.location.href = '../system/download?path=' + addUplist.path;
                    addUplist.selectUserBaseInfo();
                } else {
                    patest.alertInfo("alert-danger", result.loadResult.desc);
                }
            },
            error: function () {
                patest.alertInfo("alert-danger", "上传失败");
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
                examId: addUplist.examId,
                page: addUplist.page,
                rows: patest.rowsnum,
            },
            success: function (result) {
                console.log(result);
                addUplist.count = result.total;
                addUplist.examPaper = result.examPaperList;
                addUplist.userInfo = result.userInfoList;
                addUplist.userProfile = result.userProfileList;
                addUplist.showInfo();
                $("#stuInfo").empty();
                $("#stuInfo").append(addUplist.html);
                addUplist.pagingFun();
            },
            error: function () {
                patest.alertInfo("alert-danger", "请求失败");
            }
        });
    },
    pagingFun: function () {
        if (addUplist.count > 0) {
            $(".pagenum").css("display", "block");
            $(".countnum").html(addUplist.count);
            $.jqPaginator('#pagination', {
                totalCounts: addUplist.count,
                visiblePages: 5,
                currentPage: 1,
                pageSize: parseInt(patest.rowsnum),
                first: '<li class="first"><a href="javascript:;">首页</a></li>',
                last: '<li class="last"><a href="javascript:;">尾页</a></li>',
                page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
                onPageChange: function (num, type) {
                    if (type == 'init') {
                        return;
                    }
                    addUplist.page = num;
                    addUplist.selectUserBaseInfo();
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
