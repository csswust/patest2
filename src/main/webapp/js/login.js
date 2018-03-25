$(function () {
    $('#username').focus();
    var program = {
        username: "",
        password: "",
        login: function () {
            $.ajax({
                type: "POST",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../userInfo/login",
                dataType: 'json',
                async: false,
                data: {
                    username: program.username,
                    password: program.password
                },
                success: function (result) {
                    console.log(result);
                    var exp = new Date();
                    document.cookie = "name=" + escape(result.username);
                    document.cookie = "userId=" + escape(result.userId);
                    document.cookie = "realName=" + escape(result.realName);
                    document.cookie = "studentNumber=" + escape(result.studentNumber);
                    /*if (result.status == "0") {
                     program.alertInfo("alert-danger", "无此账号!");
                     }
                     else */
                    if (result.loginRe.status == "1" && result.isAdmin == "1") {
                        window.location.href = "index.html";
                        document.cookie = "role=" + escape('Admin');
                    }
                    else if (result.loginRe.status == "1" && result.isAdmin == "0" && result.isTeacher == "0") {
                        var nowTime = program.getNowTime();
                        window.location.href = "examnotes.html?" + "&eId=" + result.examId;
                        document.cookie = "role=" + escape('Student');
                    }
                    else {
                        program.alertInfo("alert-danger", result.loginRe.desc);
                    }
                    /*else if (result.status == "4") {
                     program.alertInfo("alert-danger", "账号被锁定! 请联系老师！");
                     }
                     else if (result.status == "2") {
                     program.alertInfo("alert-danger", "密码错误!");
                     }
                     else if (result.status == "3") {
                     program.alertInfo("alert-danger", "账号未激活!");
                     }
                     else if (result.status == "6") {
                     program.alertInfo("alert-danger", "登录地点异常");
                     }*/
                },
                error: function () {
                    program.alertInfo("alert-danger", "登录失败!");
                    $("#username").val("");
                    $("#password").val("");
                }
            });
        },
        alertInfo: function (className, info) {
            if ($(".tip").text().trim() == "") {
                $(".tip").html(' <div class="alert  ' + className + '"  id="tip">' +
                    '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                    '<strong>' + info + '</strong></div>');
            } else {
                $("#tip").removeClass();
                $("#tip").addClass("alert " + className);
                $("strong").text(info);
            }
        },
        judge: function () {
            program.username = $("#username").val();
            program.password = $("#password").val();
            if (program.username != "" && program.password != "") {
                program.login();
            } else {
                program.alertInfo("alert-danger", "账号和密码不能为空！");
            }
        },
        getNowTime: function () {
            var now = new Date();
            var year = now.getFullYear();
            var month = ( now.getMonth() + 1) < 10 ? '0' + (now.getMonth() + 1) : now.getMonth() + 1;
            var day = now.getDate();
            var dayInfo = now.toString().split(" ")[4];
            var nowTime = year + "-" + month + "-" + day + " " + dayInfo;
            console.log(nowTime);
            return nowTime;
        },
        legTimeRange: function (startTime, endTime) {
            var stadate = startTime.split(" ");
            var enddate = endTime.split(" ");
            var stayear = stadate[0].split('-');
            var endyear = enddate[0].split('-');
            var stadate = stadate[1].split(':');
            var enddate = enddate[1].split(':');

            //年份更大，同年月份更大，同年同月日更大，同年同月同日时更大，同年同月同日同时分更大，同年同月同日同时同分秒更大
            if (stayear[0] > endyear[0] || stayear[0] == endyear[0] && stayear[1] > endyear[1] ||
                stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] > endyear[2] ||
                stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] == endyear[2] && stadate[0] > enddate[0] ||
                stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] == endyear[2] && stadate[0] == enddate[0] && stadate[1] > enddate[1] ||
                stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] == endyear[2] && stadate[0] == enddate[0] && stadate[1] == enddate[1] && stadate[2] > enddate[2]) {
                return false;
            }
            return true;
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
                '<a href="../system/download?path=/static/devcpp.exe&isUeditorPath=true">Dev cpp</a>' +
                '<span> 体验最佳浏览效果，请使用谷歌浏览器，</span>' +
                '<a href="../system/download?path=/static/Chrome.exe&isUeditorPath=true">点击下载</a>' +
                '</p></div></div>';
            $('body').append(footerHtml);
        }
    };
    program.getfooter();
    $(".loginRepeat").click(function () {
        $("#username").val("");
        $("#password").val("");
    });
    $(".login").click(function () {
        program.judge();
    });
    $("body").keydown(function () {
        if (event.keyCode == "13") {        //keyCode=13是回车键
            program.judge();
        }
    });
});