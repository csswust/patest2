define(function (require, exports, module) {
    require("jquery");
    var pubMeth = require('../js/public.js');
    var program = {
        examId: '',
        username: '',
        stuId: '',
        status: [],
        getCookie1: function (objName) {
            var arrStr = document.cookie.split("; ");
            console.log(arrStr);
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName)
                    program.username = unescape(temp[1]);
            }
            if (program.username == "" || program.username == "undefined") {
                window.location.href = "login.html";
            }
        },
        getCookie2: function (objName) {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName)
                    program.realName = unescape(temp[1]);
            }
            if (program.username == "" || program.username == "undefined") {
                window.location.href = "login.html";
            }
        },
        getCookie3: function (objName) {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName)
                    program.studentNumber = unescape(temp[1]);
            }
            if (program.username == "" || program.username == "undefined") {
                window.location.href = "login.html";
            }
        },
        deleCookie: function (name) {//为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间
            var date = new Date();
            date.setTime(date.getTime() - 10000);
            document.cookie = name + "=a; expires=" + date.toGMTString();
        },
        head: function () {
            var par = pubMeth.getQueryObject();
            var htmlheader =
                '<nav class="navbar navbar-default">' +
                '<div class="navbar-header "><a class="navbar-brand" href="#">LExamV3.0</a></div>' +
                '<ul class="nav navbar-nav navbar-right">' +
                '<li><a href="contents.html?&eId=' + par.eId + '"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> 首页</a></li>' +
                '<li><a href="contents.html?&eId=' + par.eId + '"><span class="glyphicon glyphicon-th-list" aria-hidden="true"></span> 题目列表</a></li>' +
                '<li><a href="submit.html?&eId=' + par.eId + '"><span class="glyphicon glyphicon-ok-sign" aria-hidden="true"></span> 我的提交</a></li>' +
                '<li><a href="faqs.html?&eId=' + par.eId + '"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> FAQS</a></li>' +
                '<li><a href="javascript:void(0);"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>' + program.username + ' [' + program.realName + ']' + ' (' + program.studentNumber + ')' + '</a></li>' +
                '<li><a href="login.html"><span class="glyphicon glyphicon-off" aria-hidden="true"></span> 注销</a></li></ul> </li>';

            /*'<li class="dropdown">'+
             '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">'+
             '<span class="glyphicon glyphicon-user" aria-hidden="true"></span> '+
             ''+program.username+' <span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span> </a>'+
             '<ul class="dropdown-menu">'+
             ' <li class="loginout"><a href="login.html">注销</a></li></ul> </li></ul></nav>';*/
            var noBegain = '<nav class="navbar navbar-default">' +
                '<div class="navbar-header "><a class="navbar-brand" href="#">Lexam</a></div>' +
                '<li class="dropdown">' +
                '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">' +
                '' + program.username + ' <span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span></a>' +
                '<ul class="dropdown-menu">' +
                ' <li class="loginout"><a href="login.html">注销</a></li></ul> </li></ul></nav>';
            $("#head").append(htmlheader);
        },
        getNowTime: function () {
            var now = new Date();
            program.year = now.getFullYear();
            var month = ( now.getMonth() + 1) < 10 ? '0' + (now.getMonth() + 1) : now.getMonth() + 1;
            var day = now.getDate();
            var dayInfo = now.toString().split(" ")[4];
            var nowTime = program.year + "/" + month + "/" + day + " " + dayInfo;
            //var nowTime = "2013-06-01 05:30:00";
            return nowTime;
        },
        transTime: function (date) {
            var reg = /-/g;
            var time = String(date);
            time = time.replace(reg, "/");
            return time;
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
                    //program.status = result.data;
                }
            });
        },
        loginout: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../user/logout",
                dataType: 'json',
                async: false,
                success: function (result) {
                    if (result.status == "1") {
                        window.location.href = "login.html";
                    }
                }
            });
            program.deleCookie("name");
            program.deleCookie("userId");
            program.deleCookie("role");
        },
        countDown: function (intDiff) {
            setInterval(function () {
                var day = 0, hour = 0, minute = 0, second = 0;// 时间默认值
                if (intDiff > 0) {
                    day = Math.floor(intDiff / (60 * 60 * 24));
                    hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
                    minute = Math.floor(intDiff / 60) - (day * 24 * 60)
                        - (hour * 60);
                    second = Math.floor(intDiff) - (day * 24 * 60 * 60)
                        - (hour * 60 * 60) - (minute * 60);
                    if (day <= 9)
                        day = '0' + day;
                    if (hour <= 9)
                        hour = '0' + hour;
                    if (minute <= 9)
                        minute = '0' + minute;
                    if (second <= 9)
                        second = '0' + second;
                    $(".day").text(day);
                    $('.hours').text(hour);
                    $('.minute').text(minute);
                    $('.second').text(second);
                    intDiff--;
                } else {
                    $(".timeTip").html('<span class="overTime">考试已结束</span>');
                }

            }, 1000);
        },
        getfooter: function () {
            var now = new Date();
            program.year = now.getFullYear();
            var footerHtml = "";
            footerHtml =
                '<div class="row ">' +
                '<div class="footer">' +
                '<p>© 2012-' + program.year + '计算机科学与技术学院 数据与知识工程实验室</p>' +
                '<p>要使用GCC编译器，请下载：' +
                '<a href="../resource/devcpp.exe">Dev cpp</a>' +
                '<span> 体验最佳浏览效果，请使用谷歌浏览器，</span>' +
                '<a href="../resource/Chrome.exe">点击下载</a>' +
                '</p></div></div>';
            $('body').append(footerHtml);
        }
    };
    program.getCookie1("name");
    program.getCookie2("realName");
    program.getCookie3("studentNumber");
    program.head();
    //program.getNowTime();
    program.getStatus();
    module.exports = program;
    program.getfooter();
});