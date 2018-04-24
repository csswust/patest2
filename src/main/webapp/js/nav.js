var nav = {
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
                nav.username = unescape(temp[1]);
        }
        if (nav.username == "" || nav.username == "undefined") {
            window.location.href = "login.html";
        }
    },
    getCookie2: function (objName) {
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] == objName)
                nav.realName = unescape(temp[1]);
        }
        if (nav.username == "" || nav.username == "undefined") {
            window.location.href = "login.html";
        }
    },
    getCookie3: function (objName) {
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] == objName)
                nav.studentNumber = unescape(temp[1]);
        }
        if (nav.username == "" || nav.username == "undefined") {
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
            '<li><a href="studentfaqs.html?&eId=' + par.eId + '"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> FAQS</a></li>' +
            '<li><a href="javascript:void(0);"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>' + nav.username + ' [' + nav.realName + ']' + ' (' + nav.studentNumber + ')' + '</a></li>' +
            '<li class="loginout"><a href="#"><span class="glyphicon glyphicon-off" aria-hidden="true"></span> 注销</a></li></ul> </li>';
        $("#head").append(htmlheader);
    },
    getNowTime: function () {
        var now = new Date();
        nav.year = now.getFullYear();
        var month = ( now.getMonth() + 1) < 10 ? '0' + (now.getMonth() + 1) : now.getMonth() + 1;
        var day = now.getDate();
        var dayInfo = now.toString().split(" ")[4];
        var nowTime = nav.year + "/" + month + "/" + day + " " + dayInfo;
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
                nav.status[0] = "未提交";
                for (var i = 0; i < result.data.length; i++) {
                    nav.status[result.data[i].resuId] = result.data[i].name;
                }
            }
        });
    },
    loginout: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../userInfo/logout",
            dataType: 'json',
            async: false,
            success: function (result) {
                window.location.href = "login.html";
            }
        });
        nav.deleCookie("name");
        nav.deleCookie("userId");
        nav.deleCookie("role");
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
        nav.year = now.getFullYear();
        var footerHtml = "";
        footerHtml =
            '<div class="row ">' +
            '<div class="footer">' +
            '<p>© 2012-' + nav.year + '计算机科学与技术学院 数据与知识工程实验室</p>' +
            '<p>要使用GCC编译器，请下载：' +
            '<a href="../system/download?path=/static/devcpp.exe&isUeditorPath=true">Dev cpp</a>' +
            '<span> 体验最佳浏览效果，请使用谷歌浏览器，</span>' +
            '<a href="../system/download?path=/static/Chrome.exe&isUeditorPath=true">点击下载</a>' +
            '</p></div></div>';
        $('body').append(footerHtml);
    }
};
nav.getCookie1("name");
nav.getCookie2("realName");
nav.getCookie3("studentNumber");
nav.head();
nav.getStatus();
nav.getfooter();
$(".loginout").click(function () {
    nav.loginout();
});