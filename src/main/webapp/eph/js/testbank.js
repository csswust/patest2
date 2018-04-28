var testbank = {
    page: '1',
    bankIds: "",
    proinfoArr: [],
    probIdList: [],
    init: function () {
        commonet.init(); // 公共模块初始化
        // $(".etbank").addClass("on");
        $(".mytest").addClass("onet");
        patest.getRowsnum("rowsnum");
        var parm = patest.getQueryObject();
        if (parm["Id"] !== null) {
            testbank.examId = parm["Id"];
        } else {
            patest.alertInfo("alert-danger", "考试id不存在");
            return;
        }
        testbank.getProblemInfo();
        patest.serCourse();
        testbank.allcheckbank();
        $('.addbank').on('click', function (e) {
            if (testbank.bankIds !== "") {
                $("#modaladdbank").modal({
                    backdrop: 'static'
                });
                $(".confirmadd").click(function () {
                    testbank.getproblemInfoById();
                    testbank.insertTempProblem();
                });
            } else {
                patest.alertInfo("alert-info", "请先勾选添加项！");
            }
        });
        $(".backexam").click(function () {
            window.location.href = "addBank.html?Id=" + testbank.examId;
        });
        $(".search").click(function () {
            testbank.page = 1;
            $('#pagination').jqPaginator('option', {
                currentPage: testbank.page
            });
            if (testbank.queryCondition()) {
                testbank.searchProblem();
            } else {
                testbank.getProblemInfo();
            }
            $(".countnum").html(testbank.count);
            $('#pagination').jqPaginator('option', {
                totalCounts: testbank.count
            });
        });
        if (testbank.count > 0) {
            $(".countnum").html(testbank.count);
            $.jqPaginator('#pagination', {
                totalCounts: testbank.count,
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
                    testbank.page = num;
                    if (testbank.queryCondition()) {
                        testbank.searchProblem();
                    } else {
                        testbank.getProblemInfo();
                    }
                }
            });
        } else {
            $(".pagenum").css("display", "none");
        }
    },
    getProblemInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../ep/problemInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                knowId: testbank.know,
                page: testbank.page,
                rows: patest.rowsnum,
                examId: testbank.examId
            },
            success: function (result) {
                result = result.data;
                console.log(result);
                testbank.count = result.total;
                testbank.data = result;
                testbank.showProblem();
            },
            error: function () {
                patest.alertInfo("alert-info", "请求错误");
            }
        });
    },
    showProblem: function () {
        $("#listInfo").empty();
        $(".check_list").prop("checked", false);
        var length = testbank.data.data.length;
        var flag = 1;
        for (var i = 0; i < length; i++) {
            var difficult = "";
            var courseName = "";
            var knowName = "";
            if (testbank.data.data[i].levelId == 1) {
                difficult = "容易";
            } else if (testbank.data.data[i].levelId == 2) {
                difficult = "中等";
            } else if (testbank.data.data[i].levelId == 3) {
                difficult = "困难";
            }
            if (testbank.data.course[i] != null) {
                courseName = testbank.data.course[i].courseName;
            }
            if (testbank.data.knowledge[i] !== null) {
                knowName = testbank.data.knowledge[i].knowName;
            }
            var str = '<tr>' + '<td><input type="checkbox" value="'
                + testbank.data.data[i].probId + ','
                + testbank.data.data[i].title + '"';
            if (testbank.bankIds.indexOf(testbank.data.data[i].probId) !== -1) {
                str += 'checked';
            }
            str += ' name="title" class="show"/></td>' + '<td>'
                + testbank.data.data[i].probId + '</td>'
                + '<td><a href="question.html?id='
                + testbank.data.data[i].probId + '"  class="title">'
                + testbank.data.data[i].title + '</a></td>'
                + '<td>' + difficult + '</td>' + '<td>'
                + testbank.data.data[i].acceptedNum + '/'
                + testbank.data.data[i].submitNum + '</td>'
                + '<td>' + testbank.data.data[i].createTime
                + '</td>' + '<td>' + courseName + '</td>'
                + '<td>' + knowName + '</td>' + '</tr>'
            $("#listInfo").append(str);
            flag++;
        }
        testbank.checkbank();
    },
    searchProblem: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../ep/problemInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                "title": testbank.title,
                "levelId": testbank.level,
                knowId: testbank.know,
                page: testbank.page,
                examId: testbank.examId,
                rows: "10"
            },
            success: function (result) {
                result = result.data;
                testbank.count = result.total;
                testbank.data = result;
                testbank.showProblem();
            },
            error: function () {
                patest.alertInfo("alert-info", "请求错误");
            }
        });
    },
    queryCondition: function () {// 为真为有查询条件，为假为无条件查询
        testbank.title = $(".searTitle").val().trim();
        testbank.level = $(".level option:selected").val();
        testbank.know = $(".knowName option:selected").val();
        if (testbank.know === "知识点")
            testbank.know = "";
        if (testbank.know === "难度")
            testbank.level = "";
        if (testbank.title === "" && testbank.level === "" && testbank.know === "") {
            return false;
        }
        return true;
    },
    getproblemInfoById: function () {
        var problist = testbank.bankIds.split(",");
        for (var i = 0; i < problist.length; i++) {
            testbank.probIdList[i] = parseInt(problist[i]);
        }
        testbank.probIdstr = testbank.probIdList.join(",");
    },
    // 将所选的问题题库添加到数据库
    insertTempProblem: function () {
        patest.request({
            url: "../ep/examProblem/insertByArray"
        }, {
            probIdList: testbank.probIdstr,
            examId: testbank.examId
        }, function (result) {
            if (result.status >= 1) {
                window.location.href = "addBank.html?Id=" + testbank.examId;
                patest.alertInfo("alert-success", "保存成功");
            } else if (result.status === 0) {
                $("#modaladdbank").modal('hide');
                patest.alertInfo("alert-danger", "保存失败,请查看是否有相同题目");
            } else if (result.status === -1) {
                $("#modaladdbank").modal('hide');
                patest.alertInfo("alert-danger", "不允许添加相同的题目");
            }
        });
    },
    showbank: function () {
        $(".proinfo").empty();
        for (var i = 0; i < testbank.proinfoArr.length; i++) {
            $(".proinfo").show();
            $(".proinfo").append(
                '<span>' + testbank.proinfoArr[i] + ';' + '</span>');
        }
        testbank.getIds();
    },
    getIds: function () {
        var proIds = new Array();
        console.log(testbank.proinfoArr);
        for (var i = 0; i < testbank.proinfoArr.length; i++) {
            proIds[i] = testbank.proinfoArr[i].split(",")[0];
        }
        testbank.bankIds = proIds.join(",");
        console.log(testbank.bankIds);
    },
    // 全选
    allcheckbank: function () {
        $(".check_list").click(function () {
            if (this.checked) {
                $("#listInfo").find("input[type='checkbox']").prop("checked", true);
                $(".show").each(function (i) {
                    var val = $(this).val();
                    if (testbank.proinfoArr.indexOf(val) === -1) {
                        testbank.proinfoArr.push($(this).val());
                    }
                });
                testbank.showbank();
            } else {
                $("#listInfo").find("input[type='checkbox']").prop("checked", false);
                $(".show").each(function (i) {
                    testbank.proinfoArr.pop();
                });
                testbank.showbank();
            }
        });
    },
    // 单个选
    checkbank: function () {
        $("input[name='title']").click(function () {
            if ($(this).prop("checked") == true) {
                testbank.proinfoArr.push($(this).val());
                testbank.showbank();
            } else {
                testbank.proinfoArr.splice($.inArray($(this).val(), testbank.proinfoArr), 1);
                $(".proinfo").empty();
                testbank.showbank();
            }
        });
    },
};
