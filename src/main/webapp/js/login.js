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
                    document.cookie = "name=" + escape(result.username);
                    document.cookie = "userId=" + escape(result.userId);
                    document.cookie = "realName=" + escape(result.realName);
                    document.cookie = "studentNumber=" + escape(result.studentNumber);
                    if (result.loginRe.status === 1 && (result.isAdmin === 1 || result.isTeacher === 1)) {
                        window.location.href = "index.html";
                        document.cookie = "role=" + escape('Admin');
                    }
                    else if (result.loginRe.status === 1 && result.isAdmin === 0 && result.isTeacher === 0) {
                        window.location.href = "examnotes.html?" + "&eId=" + result.examId;
                        document.cookie = "role=" + escape('Student');
                    }
                    else {
                        program.alertInfo("alert-danger", result.loginRe.desc);
                    }
                },
                error: function () {
                    program.alertInfo("alert-danger", "登录失败!");
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
            if (program.username && program.password) {
                program.login();
            } else {
                program.alertInfo("alert-danger", "账号和密码不能为空！");
            }
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
        },
        getsysname: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../siteInfo/selectByName",
                dataType: 'json',
                async: false,
                data: {
                    name: "systemname"
                },
                success: function (result) {
                    program.systename = result.value;
                }
            });
        }
    };
    program.getfooter();
    program.getsysname();
    $(".form-title").text(program.systename);
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