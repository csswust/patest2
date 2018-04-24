var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
    lineNumbers: true,
    indentUnit: 4,
    indentWithTabs: true,
    styleActiveLine: true,
    autoMatchParens: true,
    theme: "3024-night",
    mode: "text/x-c++src"
});
var program = {
    code: "",
    judId: '',
    papbId: '',
    submitId: '',
    submitResult: '',
    preId: '',
    nextId: '',
    eId: '',
    status: [],
    comp: [],
    getExamInfo: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                examId: par.eId
            },
            success: function (result) {
                console.log(result);
                result = result.data;
                var nowtime = nav.getNowTime();
                var nowTime = new Date(nowtime);
                program.starttime = nav.transTime(result.examInfoList[0].startTime);
                program.endtime = nav.transTime(result.examInfoList[0].endTime);
                program.startTime = new Date(program.starttime);
                program.endTime = new Date(program.endtime);
            }
        });

    },
    getProById: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../student/selectProblemByPapProId",
            dataType: 'json',
            async: false,
            data: {
                "papProId": program.papbId
            },
            success: function (result) {
                console.log(result);
                $(".quesName").text(result.problemInfo.title);
                $(".time").text(result.problemInfo.timeLimit + "S   ");
                $(".memory").text(result.problemInfo.memoryLimit + "K");
                $(".titleDescription").html(result.problemInfo.description);
                $(".inputTip").html(result.problemInfo.inputTip);
                $(".outputTip").html(result.problemInfo.outputTip);
                $(".inputSample").html(result.problemInfo.inputSample);
                $(".outputSample").html(result.problemInfo.outputSample);
                program.preId = result.preId;
                program.nextId = result.nextId;
            }, error: function () {
                pubMeth.alertInfo("alert-info", "请求错误");
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
                    $(".selectJudId").append('<option value="' + result.data[i].judId + '">' + result.data[i].repr + '</option>');
                }
            }
        });
    },
    submit: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../student/insertSubmitInfo",
            dataType: 'json',
            async: false,
            data: {
                "source": program.code,
                "judgerId": program.judId,
                "paperProblemId": program.papbId
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    program.submitId = result.submId;
                    pubMeth.alertInfo("alert-success", "提交成功，判断中...");

                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }, error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    getsubmit: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../student/selectMySubmit",
            dataType: 'json',
            async: false,
            data: {
                "submId": program.submitId
            },
            success: function (result) {
                console.log(result);
                program.submitResult = result.submitInfoList[0];//得到提交后信息
                var className = "", statuResult = "", result = "";
                if (program.submitResult == null) {
                    $(".submitResult").show().delay(1000).fadeOut();
                } else {
                    if (program.submitResult.status === 2 ||
                        program.submitResult.status === 12) {//PE
                        className = 'label-primary';
                    } else if (program.submitResult.status == 5) {//WA
                        className = 'label-danger';
                    } else if (program.submitResult.status === 11
                        || program.submitResult.status === 13) {//OW
                        className = 'label-default';
                    } else if (program.submitResult.status == 10) {//OW
                        className = 'label-info';
                    } else if (program.submitResult.status == 1) {//AC
                        className = 'label-success';
                    } else {//Others
                        className = 'label-warning';
                    }
                    var resultText = program.status[program.submitResult.status];
                    result = '<span class="label ' + className + '">' + resultText + '</span>';
                    if (program.submitResult.status == 8 ||
                        program.submitResult.status === 10) {
                        statuResult = "ceStatus";
                    } else {
                        statuResult = "noCeStatus";
                    }
                    $(".submitResult").html('<span class="' + statuResult + '" id=' + program.submitResult.submId + '>' + result + '</span>');
                    $(".submitResult").css('display', 'block');
                    if (program.submitResult.status === 11
                        || program.submitResult.status === 12
                        || program.submitResult.status === 13) {
                        setTimeout(program.getsubmit, 500);
                    } else {
                        pubMeth.alertInfo("alert-info", "你的结果为：" + resultText);
                    }
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-danger", "请求错误");
            }
        });
    },
    getSubmitResult: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../student/selectDetailResult",
            dataType: 'json',
            async: false,
            data: {
                submId: program.submitId
            },
            success: function (result) {
                console.log(result);
                var acNum = 0, length = 0, flag = 1, mainHtml = "", color = "";
                for (var i = 0, length = result.submitResultList.length; i < length; i++) {
                    if (result.submitResultList[i].status == 1) {
                        color = "acColor";
                    } else if (result.submitResultList[i].status == 5) {
                        color = "waColor";
                    } else {
                        color = "otherColor";
                    }
                    mainHtml += '<div class="row"><div class="col-md-1">' + flag + '</div>' +
                        '<div class="col-md-4"><span class="' + color + '">' + program.status[result.submitResultList[i].status] + '</span></div>' +
                        '<div class="col-md-2">' + result.submitResultList[i].usedTime + '</div>' +
                        '<div class="col-md-2">' + result.submitResultList[i].usedMemory + '</div>' +
                        '<div class="col-md-3">' + result.scoreRatioList[i] + '</div></div>';
                    flag++;
                    if (result.submitResultList[i].status == 1) {
                        acNum++;
                    }
                }
                program.statuhtml = '<div class="panel panel-default"><div class="panel-body">' +
                    '<div class="page-header">' +
                    '<h3>测试数据组数：' + length + '<small>&nbsp&nbsp&nbsp;通过数：' + acNum + '</small></h3></div>' +
                    '<div class="row"><div class="col-md-1">Test</div>' +
                    '<div class="col-md-4">Result</div>' +
                    '<div class="col-md-2">Time[Ms]</div>' +
                    '<div class="col-md-2">Memory[KB]</div>' +
                    '<div class="col-md-3">Ratio[%]</div></div>'
                    + mainHtml + '</div></div>';
            },
            error: function () {
                pubMeth.alertInfo("alert-info", "请求错误");
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
                }
            },
            error: function () {
                pubMeth.alertInfo("alert-info", "请求错误");
            }
        });
    }
};

var par = pubMeth.getQueryObject();
program.papbId = par.prproId;
program.eId = par.eId;
program.getProById();
program.getStatus();
program.getCompiler();
program.getExamInfo();

if (program.preId != null) {
    $("#problemLeft").attr('href', 'code.html?' +
        '&prproId=' + program.preId + '&eId=' + program.eId);
}
if (program.nextId != null) {
    $("#problemRight").attr('href', 'code.html?' +
        '&prproId=' + program.nextId + '&eId=' + program.eId);
}

$("#problemLeft").on('click', function () {
    if (program.preId == null) {
        pubMeth.alertInfo("alert-info", "小提示：已经是第一题");
    }
});
$("#problemRight").on('click', function () {
    if (program.nextId == null) {
        pubMeth.alertInfo("alert-info", "小提示：已经是最后一题");
    }
});

$("#submit").on('click', function () {
    pubMeth.alertInfo("alert-info", "小提示：出现问题刷新试试");
    var $btn = $(this).button('loading');
    $(".submitResult").html('');
    program.submitId = null;
    program.code = editor.getValue();
    program.judId = $(".selectJudId option:selected").val();
    program.submit();
    setTimeout(function () {
        $btn.button('reset');
    }, 1000);
    if (program.submitId !== null) {
        setTimeout(program.getsubmit, 500);
    }
});
$(".submitResult").on('click', function () {
    console.log(program.submitResult);
    var title = $(".submitResult").text();
    $('#settingData').modal();
    $("#myModalDataLabel").text(title);
    if (title == 'Compile Error') {
        $("#codeData").html('<textarea rows="32" class="form-control">' + program.submitResult.errMsg + '</textarea>');
    } else {
        program.getSubmitResult();
        $("#codeData").html(program.statuhtml);
    }
});