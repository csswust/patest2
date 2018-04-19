

var testInfo = {
    page: "1",
    examId: '',
    userId: '',
    username: '',
    submitInfoList: [],
    submitResultList: [],
    userInfoList: [],
    userProfileList: [],
    problemInfoList: [],
    init:function () {
        commonet.init(); // 公共模块初始化

        $(".mytest").addClass("onet");

        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss'
        });
        var parm = pubMeth.getQueryObject();
        testInfo.examId = parm["id"];
        testInfo.getProblemList();

        $(".examTabs").click(function () {
            var id = this.id;
            if (id == "rank") {
                testInfo.getRank();
            } else if (id == "quesList") {
                $(".submitInfo").hide();
                $(".rankInfo").hide();
                $(".online").hide();
                $(".quesList").show();
                testInfo.getProblemList();
            } else if (id == "online") {
                $(".submitInfo").hide();
                $(".rankInfo").hide();
                $(".quesList").hide();
                $(".online").show();
                testInfo.selectOnline();
            }
        });
        $(".gradePrint").click(function () {
            window.location.href = "../exam/selectGradeByExamId?examId=" + testInfo.examId;
        });
        $(".codePrint").click(function () {
            window.location.href = "../exam/selectCodeByExamId?examId=" + testInfo.examId;
        });
        if (testInfo.count > 0) {
            $.jqPaginator('#pagination', {
                totalCounts: testInfo.count,
                visiblePages: 5,
                currentPage: 1,
                pageSize: 20,
                first: '<li class="first"><a href="javascript:;">首页</a></li>',
                last: '<li class="last"><a href="javascript:;">尾页</a></li>',
                page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
                onPageChange: function (num, type) {
                    if (type == 'init') {
                        return;
                    }
                    testInfo.page = num;

                }
            });
        }
    },
    getProblemList: function () {
        console.log(testInfo.examId);
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../sysUser/selectMyProblem",
            dataType: 'json',
            async: false,
            data: {
                examId: testInfo.examId
            },
            success: function (result) {
                console.log(result);
                var html = "";
                var flag = 1;
                $('#listInfo').empty();
                for (var i = 0, len = result.problemInfoList.length; i < len; i++) {
                    var title = "", level = "";
                    if (result.problemInfoList[i] != null) {
                        title = result.problemInfoList[i].title;
                        level = result.problemInfoList[i].level;
                    }
                    html += '<tr>' + '<td>' + flag + '</td>'
                        + '<td>' + title + '</td>';
                    if (result.knowledgeList[i] != null) {
                        html += '<td>'
                            + result.knowledgeList[i].knowName
                            + '</td>';
                    } else
                        html += '<td>' + 'null' + '</td>';
                    html += '<td>' + level + '</td>' + '<td>'
                        + result.countList[i] + '</td>'
                        + '</tr>';
                    flag++;
                }
                $('#listInfo').append(html);
            },
            error: function () {
            }
        });
    },
    getRank: function () {
        $(".submitInfo").hide();
        $(".quesList").hide();
        $(".online").hide();
        $(".rankInfo").show();
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../exam/rankingByGrade",
            dataType: 'json',
            async: false,
            data: {
                examId: testInfo.examId,
                page: testInfo.page,
                rows: "20"
            },
            success: function (result) {
                var length = result.paperData.length;
                var titLength = result.problemNameData[0].length;
                var userName = "", titName = "", content = "", realName = "", className = "", score = "",
                    acedCount = "";
                var ranknav = '';
                for (var j = 1; j <= titLength; j++) {
                    ranknav += '<th>Q' + j + '</th>';
                }
                $(".rankInfo").html(
                    '<th>考号</th><th>姓名</th><th>班级</th>'
                    + '<th>分数</th><th>AC</th>'
                    + ranknav);
                for (var i = 0; i < result.paperData.length; i++) {
                    userName = result.userNameData[i];
                    realName = result.realNameData[i];
                    className = result.classNameData[i];
                    if (result.userNameData[i] == null
                        || result.userNameData[i] == "undefined") {
                        userName = "";
                    }
                    if (result.realNameData[i] == null
                        || result.realNameData[i] == "undefined") {
                        realName = ""
                    }
                    if (result.classNameData[i] == null
                        || result.classNameData[i] == "undefined") {
                        className = ""
                    }
                    titName = ""
                    for (var j = 0; j < titLength; j++) {
                        titName += '<td>'
                            + result.problemNameData[i][j]
                            + '</td>';
                    }
                    content += '<tr><td>' + userName + '</td><td>'
                        + realName + '</td>' + '<td>'
                        + className + '</td><td>'
                        + result.paperData[i].score + '</td>'
                        + '<td>'
                        + result.paperData[i].acedCount
                        + '</td>' + titName + '</tr>';

                }
                $("#listInfo").html(content);
            }
        });

    },
    selectOnline: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../user/selectOnline",
            dataType: 'json',
            async: false,
            data: {
                page: '1',
                rows: "20",
                examId: testInfo.examId
            },
            success: function (result) {
                console.log(result);
                $("#listInfo").empty();
                /*
                 * var iplength = result.iPList.length; var userlength =
                 * result.userinfoList.length; var flag = 1; for(var i = 0;i<iplength;i++){
                 * var classroom = result.userinfoList[i].classroom;
                 * if(classroom==null ||classroom=="undefined"){
                 * classroom=""; } $("#listinfo").append('<tr><td>'+flag+'</td><td>'+result.userinfoList[i].username+'</td>' + '<td>'+2+'</td><td>'+classroom+'</td><td>'+result.iPList[i]+'</td></tr>'); }
                 */
            }
        });
    }
};
