define(function (require, exports, module) {
    require('jquery');
    require('../js/common.js');
    require('bootstrap');
    require('paginator');

    $(".examRoom").next(".treeview-menu").toggle("slow");
    $(".examRoom").addClass("leftActive");
    $(".examination").css("color", "white");
    $(".mySubmit").addClass("active");

    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');

    var program = {
        page: "1",
        examId: '',
        userId: '',
        username: '',
        status: [],
        comp: [],
        statu: "",
        compiler: "",
        probId: "",
        submitInfoList: [],
        submitResultList: [],
        userInfoList: [],
        userProfileList: [],
        problemInfoList: [],
        getuserId: function () {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == "userId")
                    program.userId = unescape(temp[1]);
            }
            if (program.userId == "" || program.userId == "undefined") {
                window.location.href = "login.html";
            }
        },
        getStatus: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../resultInfo/selectByCondition",
                dataType: 'json',
                async: false,
                success: function (result) {
                    program.status[0] = "未提交";
                    for (var i = 0; i < result.data.length; i++) {
                        program.status[result.data[i].resuId] = result.data[i].name;
                        $(".result_select").append('<option value="' + result.data[i].resuId + '">' + result.data[i].name + '</option>');
                    }
                }
            });
        },
        getCompiler: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../judgerInfo/selectByCondition",
                dataType: 'json',
                async: false,
                success: function (result) {
                    program.comp[0] = "unknow";
                    for (var i = 0; i < result.data.length; i++) {
                        program.comp[result.data[i].judId] = result.data[i].name;
                        $(".result_comp").append('<option value="' + result.data[i].judId + '">' + result.data[i].name + '</option>');
                    }
                }
            });
        },
        getSubmitInfo: function () {
            console.log(program.examId);
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../submitInfo/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    "status": program.statu,
                    "judgerId": program.compiler,
                    "problemId": program.probId,
                    "userId": program.userId,
                    //"submitInfo.examId" : program.examId,
                    page: program.page,
                    rows: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    program.count = result.total;
                    program.submitInfoList = result.submitInfoList;
                    program.userProfileList = result.userProfileList;
                    program.userInfoList = result.userInfoList;
                    program.problemInfoList = result.problemInfoList;

                    $("#listInfo").empty();
                    program.showProblem();
                }
            });
        },
        showProblem: function () {
            var html = '';
            for (var i = 0; i < program.submitInfoList.length; i++) {
                var usedMemory = "", usedTime = "", username = "", stuNum = "",
                    result = "", proTitle = "", probId = "", realName = "", statuResult = "", stuIp = "";
                if (program.submitInfoList[i] != null) {
                    usedMemory = program.submitInfoList[i].usedMemory;
                    usedTime = program.submitInfoList[i].usedTime;
                    stuIp = program.submitInfoList[i].ip;
                    var className = '';
                    if (program.submitInfoList[i].status == 2) {//PE
                        className = 'label-primary';
                    } else if (program.submitInfoList[i].status == 5) {//WA
                        className = 'label-danger';
                    } else if (program.submitInfoList[i].status == 11) {//OW
                        className = 'label-default';
                    } else if (program.submitInfoList[i].status == 10) {//OW
                        className = 'label-info';
                    } else if (program.submitInfoList[i].status == 1) {//AC
                        className = 'label-success';
                    } else {//Others
                        className = 'label-warning';
                    }
                    result = '<span class="label ' + className + '">' + program.status[program.submitInfoList[i].status] + '</span>';
                }
                if (program.userInfoList[i] != null) {
                    username = program.userInfoList[i].username;
                }
                if (program.userProfileList[i] != null) {
                    realName = program.userProfileList[i].realName;
                    stuNum = program.userProfileList[i].studentNumber;
                }
                if (program.problemInfoList[i] != null) {
                    proTitle = program.problemInfoList[i].title;
                }
                if (program.problemInfoList[i] != null) {
                    probId = program.problemInfoList[i].probId;
                }
                if (program.submitInfoList[i].status == 8) {
                    statuResult = "ceStatus";
                } else {
                    statuResult = "noCeStatus";
                }
                html += '<tr>'
                    + '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + proTitle + '"><a href="question.html?id=' + probId + '">' + proTitle + '(' + probId + ')' + '</a></td>'
                    + '<td class="' + statuResult + '" id=' + program.submitInfoList[i].submId + '>' + result + '</td>'
                    + '<td>' + usedMemory + '</td>'
                    + '<td>' + usedTime + '</td>'
                    + '<td class="judId"><a class="compiler">' + program.comp[program.submitInfoList[i].judgerId] + '</a></td>'
                    + '<td>' + program.submitInfoList[i].createTime + '</td>'
                    + '<td>' + stuIp + '</td>'
                    + '<td><button class="btn btn-success btn-xs reJudge" type="button" value=' + program.submitInfoList[i].submId + '>重判</button></td>'
                    + '</tr>';
            }
            $('#listInfo').html(html);
            $(".reJudge").click(function () {
                var subIdValue = this.value;
                $.ajax({
                    type: "get",
                    content: "application/x-www-form-urlencoded;charset=UTF-8",
                    url: "../submitInfo/rejudgeBySubmId",
                    dataType: 'json',
                    async: false,
                    data: {
                        submId: subIdValue
                    },
                    success: function (result) {
                        if (result.status == 1) {
                            window.location.reload();
                        }
                    }
                });

            });
            if (program.count > 0) {
                $(".countnum").html(program.count);
                $(".pagenum").css("display", "block");
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
                        program.getSubmitInfo();
                    }
                });
            } else {
                $(".pagenum").css("display", "none");
            }
        },

        getSubmitResult: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../submitResult/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    submitId: program.submId
                },
                success: function (result) {
                    var acNum = 0, length = 0, flag = 1, mainHtml = "", color = "";
                    /*if (result.status == 1) {*/
                    for (var i = 0, length = result.submitResultList.length; i < length; i++) {
                        if (result.submitResultList[i].status == 1) {
                            color = "acColor";
                        } else if (result.submitResultList[i].status == 5) {
                            color = "waColor";
                        } else {
                            color = "otherColor";
                        }
                        mainHtml += '<div class="row"><div class="col-md-3">' + flag + '</div>' +
                            '<div class="col-md-3"><span class="' + color + '">' + program.status[result.submitResultList[i].status] + '</span></div>' +
                            '<div class="col-md-3">' + result.submitResultList[i].usedTime + '</div>' +
                            '<div class="col-md-3">' + result.submitResultList[i].usedMemory + '</div> </div>';
                        flag++;
                        if (result.submitResultList[i].status == 1) {
                            acNum++;
                        }
                    }
                    program.statuhtml = '<div class="panel panel-default"><div class="panel-body">' +
                        '<div class="page-header">' +
                        '<h3>测试数据组数：' + length + '<small>&nbsp&nbsp&nbsp;通过数：' + acNum + '</small></h3></div>' +
                        /*'<table style="border:1px solid black">'+'<tr>'+'<td>Test</td>'+'<td>Result</td>'+'<td>Time[Ms]</td>'+'<td>Memory[KB]</td>'+'</tr>'+'</table>'*/
                        /*'<table id=listhead style="border:1px solid black">'*/
                        /*'<tr><td>'+123+'</td>'
                         +'<td>'+123+'</td>'
                         +'<td>'+123+'</td>'
                         +'</tr>'*/
                        /*+'</table>'*/
                        '<div class="row"><div class="col-md-3">Test</div>' +
                        '<div class="col-md-3">Result</div>' +
                        '<div class="col-md-3">Time[Ms]</div>' +
                        '<div class="col-md-3">Memory[KB]</div></div>'
                        + mainHtml + '</div></div>';
                    /*}*/
                },
                error: function () {
                    pubMeth.alertInfo("alert-info", "请求错误");
                }
            });
        }
    };
    pubMeth.getRowsnum("rowsnum");
    var parm = pubMeth.getQueryObject();
    program.examId = parm["id"];
    program.getStatus();
    program.getuserId();
    program.getCompiler();
    $(".result_select option:first").prop("selected", 'selected');
    $(".result_comp option:first").prop("selected", 'selected');

    program.getSubmitInfo();


    $(".search").click(function () {
        var statuValue = $(".result_select option:selected").val();
        var compValue = $(".result_comp option:selected").val();
        if (statuValue != 0) {
            program.statu = statuValue;
        } else {
            program.statu = "";
        }
        if (compValue != 0) {
            program.compiler = compValue;
        } else {
            program.compiler = "";
        }
        program.probId = $(".probId").val();

        program.getSubmitInfo();
    });

    $("tbody").delegate('td', 'click', function () {
        program.rowIndex = $(this).parent().index();
        if (this.className == 'judId') {
            $('#settingdata').modal();
            $("#myModalLabel").text(program.problemInfoList[program.rowIndex].title);
            $('#code').html('<textarea rows="32"  class="form-control" >' + program.submitInfoList[program.rowIndex].source + '</textarea>');
        } else if (this.className == "ceStatus") {
            $('#settingdata').modal();
            $("#myModalLabel").text(program.problemInfoList[program.rowIndex].title);
            $('#code').html('<textarea rows="32"  class="form-control" >' + program.submitInfoList[program.rowIndex].errMsg + '</textarea>');
        } else if (this.className == "noCeStatus") {
            program.submId = this.id;
            program.getSubmitResult();
            $('#settingdata').modal();
            $("#myModalLabel").text(program.problemInfoList[program.rowIndex].title);
            $("#code").html(program.statuhtml);
        }
    });
//	program.getSubmitInfo();
});