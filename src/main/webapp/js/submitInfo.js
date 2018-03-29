define(function (require, exports, module) {
    require('jquery');
    require('../js/common.js');
    require('bootstrap');
    require('paginator');
    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    $(".quesMana").next(".treeview-menu").toggle("slow");
    $(".quesMana").addClass("leftActive");
    $(".submitInfo").css("color", "white");
    var program = {
        page: '1',
        count: "",
        examId: '',
        username: null,
        probId: '',
        status: [],
        comp: [],
        statu: '',
        submId: '',
        compiler: '',
        submitInfoList: [],
        userInfoList: [],
        userProfileList: [],
        problemInfoList: [],
        getSubmitInfo: function () {
            program.username = program.username === "" ? null : program.username;
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
                    username: program.username,
                    page: program.page,
                    rows: pubMeth.rowsnum
                },
                success: function (result) {
                    program.count = result.total;
                    console.log(result.submitInfoList);
                    program.submitInfoList = result.submitInfoList;
                    program.userProfileList = result.userProfileList;
                    program.userInfoList = result.userInfoList;
                    /*		console.log(program.userProfileList);*/
                    program.problemInfoList = result.problemInfoList;
                    program.showProblem();
                }
            });
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
        showProblem: function () {
            var html = '';
            for (var i = 0; i < program.submitInfoList.length; i++) {
                var usedMemory = "", usedTime = "", username = "", cName = "",
                    result = "", proTitle = "", probId = "", realName = "", statuResult = "";
                if (program.submitInfoList[i] != null) {
                    usedMemory = program.submitInfoList[i].usedMemory;
                    usedTime = program.submitInfoList[i].usedTime;
                    var className = '';
                    if (program.submitInfoList[i].status === 2 ||
                        program.submitInfoList[i].status === 12) {//PE
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
                    cName = program.userProfileList[i].className;
                    realName = program.userProfileList[i].realName;
                }
                if (program.problemInfoList[i] != null) {
                    proTitle = program.problemInfoList[i].title;
                }
                if (program.problemInfoList[i] != null) {
                    probId = program.problemInfoList[i].probId;
                }
                if (program.submitInfoList[i].status === 8 ||
                    program.submitInfoList[i].status === 10) {
                    statuResult = "ceStatus";
                } else {
                    statuResult = "noCeStatus";
                }
                html += '<tr><td>' + program.submitInfoList[i].submId + '</td>'
                    + '<td style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;" class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + proTitle + '"><a href="question.html?id=' + probId + '">' + proTitle + '(' + probId + ')' + '</a></td>'
                    + '<td>' + username + '</td>'
                    + '<td>' + realName + '</td>'
                    /*+ '<td>' + cName + '</td>'*/
                    + '<td class="' + statuResult + '" id=' + program.submitInfoList[i].submId + '>' + result + '</td>'
                    + '<td>' + usedMemory + '</td>'
                    + '<td>' + usedTime + '</td>'
                    + '<td class="judId"><a class="compiler">' + program.comp[program.submitInfoList[i].judgerId] + '</a></td>'
                    + '<td>' + program.submitInfoList[i].createTime + '</td>'
                    + '<td>' + program.submitInfoList[i].ip + '</td>'
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
                    console.log(result);
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
    program.getStatus();
    program.getCompiler();
    program.getSubmitInfo();
    $(".search").click(function () {
        program.page = "1";
        program.userId = $(".userId").val();
        program.probId = $(".probId").val();
        program.statu = $(".result_select option:selected").val();
        program.compiler = $(".result_comp option:selected").val();
        if (program.statu == 0) {
            program.statu = "";
        }
        if (program.compiler == 0) {
            program.compiler = "";
        }
        program.getSubmitInfo();
    });
    $(".reset").click(function () {
        $(".probId").val("");
        $(".userId").val("");
        $(".result_comp option:first").prop("selected", true);
        $(".result_select option:first").prop("selected", true);
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
    var par = pubMeth.getQueryObject();
    var currPage = 1;
    if (par.page !== null && par.page !== undefined) currPage = parseInt(par.page);
    if (program.count > 0) {
        $(".countnum").html(program.count);
        $.jqPaginator('#pagination', {
            totalCounts: program.count,
            visiblePages: 10,
            currentPage: currPage,
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
});
