define(function (require, exports, module) {
    require('jquery');
    require('bootstrap');
    var nav = require('../js/nav.js');
    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    var parm = pubMeth.getQueryObject();
    var program = {
        uId: '',
        examID: '',
        title: '',
        listProb: [],
        getExam: function () {
            if (program.startTime > (new Date())) {
                $(".examName").html(program.title);
                $(".examDescription").html('<span class="glyphicon glyphicon-list"></span>&nbsp;&nbsp;&nbsp;&nbsp;<strong class="examTime" style="font-size:20px;">考试信息：</strong> 开始时间： ' + program.starttime + '&nbsp;&nbsp;&nbsp;&nbsp;结束时间：  ' + program.endtime + '<hr></hr><div style="text-indent:2em;">' + program.description + '</div>');
                $("#listInfo").html('<div style="width:100%;color:red;"><h1 style="text-align:center;">考试还未开始，请耐心等候</h1></div>');
            }
            else {
                $.ajax({
                    type: "get",
                    content: "application/x-www-form-urlencoded;charset=UTF-8",
                    url: "../student/selectMyProblem",
                    dataType: 'json',
                    async: false,
                    data: {
                        examId: parm["eId"]
                    },
                    success: function (result) {
                        console.log(result);
                        $(".examName").html(program.title);
                        $(".examDescription").html('<span class="glyphicon glyphicon-list"></span>&nbsp;&nbsp;&nbsp;&nbsp;<strong class="examTime" style="font-size:20px;">考试信息：</strong> 开始时间： ' + program.starttime + '&nbsp;&nbsp;&nbsp;&nbsp;结束时间： ' + program.endtime + '<hr></hr><div style="text-indent:2em;">' + program.description + '</div>');
                        program.listProb = result.paperProblemList;
                        program.examID = result.examParamList[0].examId;
                        $(".sumScore").html(result.sumScore);
                        $(".stuGetScore").html(result.examPaper.score);
                        $("#listInfo").html("");
                        if (result.status === 1) {
                            for (var i = 0; i < program.listProb.length; i++) {
                                var asc = String.fromCharCode(65 + i);
                                var statusClass = "status";
                                if (program.listProb[i].isAced == 1) {
                                    statusClass = "status ac";
                                } else if (program.listProb[i].isAced == 0) {
                                    statusClass = "status no";
                                } else if (program.listProb[i].isAced == 5) {
                                    statusClass = "status wa";
                                } else {
                                    statusClass = "status ce";
                                }

                                $("#listInfo").append(' <div class="col-sm-6 col-md-3"><div class="thumbnail">' +
                                    '<div class="caption"><h2>' + asc + '</h2><h5><a href="code.html?' +
                                    '&prproId=' + program.listProb[i].papProId +
                                    '&eId=' + parm["eId"] + '" class="title">' +
                                    result.problemInfoList[i].title + '</a></h5>' +
                                    '<div class="' + statusClass + '">' + nav.status[program.listProb[i].isAced] + '</div></div></div></div>');
                                $(".thumbnail:last").append('<div class="' + statusClass + '">' + program.listProb[i].score + '\/' + result.examParamList[i].score + '</div>');
                            }
                        }
                    }
                });
            }
        },
        getuserId: function () {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == "userId")
                    program.uId = unescape(temp[1]);
            }
            if (program.uId == "" || program.uId == "undefined") {
                window.location.href = "login.html";
            }
        },
        showTime: function () {
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examInfo/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    examId: parm["eId"]
                },
                success: function (result) {

                    console.log(result);
                    program.title = result.examInfoList[0].title;
                    program.description = result.examInfoList[0].description;

                    var nowtime = nav.getNowTime();
                    var nowTime = new Date(nowtime);
                    program.starttime = nav.transTime(result.examInfoList[0].startTime);
                    program.endtime = nav.transTime(result.examInfoList[0].endTime);
                    program.startTime = new Date(program.starttime);
                    program.endTime = new Date(program.endtime);
//						console.log(program.startTime + "     &&     " + program.endTime)
                    var intDiff = 0;
                    if (program.startTime > nowTime)
                        intDiff = parseInt((program.startTime - nowTime) / 1000);
                    else intDiff = parseInt((program.endTime - nowTime) / 1000);//倒计时总秒数量


                    nav.countDown(intDiff);
                }
            });
        },

        /*getSubmit: function () {
         $.ajax({
         type: "get",
         content: "application/x-www-form-urlencoded;charset=UTF-8",
         url: "../submit/selectSubmitInfo",
         dataType: 'json',
         async: false,
         data: {
         "submitInfo.examId": parm["eId"],
         "submitInfo.userId": 2,
         page: program.page,
         rows: '20'
         },
         success: function (result) {
         console.log(result);
         }
         });
         },*/
        getExamNotice: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examNotice/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    examId: parm["eId"]
                },
                success: function (result) {
                    console.log(result);
                    if (result.examNoticeList.length > 0)
                        $("#rolling-news").html(result.examNoticeList[0].content);
                    else
                        $("#rolling-news").html("暂无通知~");
                }
            });
        },
    };
    program.showTime();
    program.getuserId();
    program.getExam();

    $("#rolling-news").mouseover(function () {
        this.stop();
    }).mouseout(function () {
        this.start();
    });
    program.getExamNotice();
});