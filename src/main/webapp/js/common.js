define(function (require, exports, module) {
    require("jquery");
    var pubMeth = require('../js/public.js');
    var program = {
        username: '',
        systename: '',
        getCookie: function (objName) {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName) {
                    program.username = unescape(temp[1]);
                }
            }
            if (program.username == "" || program.username == "undefined") {
                window.location.href = "login.html";
            }
        },
        deleCookie: function (name) {// 为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间
            var date = new Date();
            date.setTime(date.getTime() - 10000);
            document.cookie = name + "=a; expires=" + date.toGMTString();
        },
        head: function () {
            var htmlheader = '<nav class="navbar navbar-default">'
                + '<div class="navbar-header "><a class="navbar-brand" href="#">' + program.systename + '</a></div>'
                + '<ul class="nav navbar-nav navbar-right">'
                + '<li><a href="index.html"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> 主页</a></li>'
                + '<li><a href="online.html"><span class="glyphicon glyphicon-th" aria-hidden="true"></span> 在线用户</a></li>'
                + '<li><a href="lock.html"><span class="glyphicon glyphicon-bookmark" aria-hidden="true"></span> 登录请求</a></li>'
                + '<li><a href="faqs.html"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> FAQS</a></li>'
                + '<li><a href="#"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> ' + program.username + '</a></li>'
                + '<li class="loginout"><a href="#"><span class="glyphicon glyphicon-off" aria-hidden="true"></span> 注销</a></li></ul></nav>';
            $("#head").append(htmlheader);
        },
        leftMenu: function () {
            var htmlLeft = '<ul class="sidebar-menu  nav-stacked nav">'
                + '<li class="tree">'
                + '<a href="javascript:;" class="treeview examMana">'
                + '<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>'
                + ' <span>考试管理</span></a>'
                + '<ul class="treeview-menu ">'
                + '<li><a href="exam.html" class="examList">考试管理</a></li>'
                + '<li><a href="examPaper.html" class="paperInfo">试卷管理</a></li>'
                + '<li><a href="examList.html" class="examination">考场管理</a></li>'
                + '<li><a href="similarity.html" class="similarityInfo">相似度</a></li>'
                + '</ul>'
                + '</li>'
                + '<li  class="tree">'
                + '<a href="javascript:;" class="treeview quesMana courseMana"><span class="glyphicon glyphicon-duplicate" aria-hidden="true"></span> <span>题库管理</span></a>'
                + '<ul class="treeview-menu">'
                + '<li><a href="problem.html" class="problemList">题库列表</a></li>'
                + '<li><a href="submitInfo.html" class="submitInfo">用户提交</a></li>'
                + '<li><a href="knowLeage.html" class="knowLeage">知识点</a></li>'
                + '</ul>'
                + '</li>'
                + '<li class="tree">'
                + '<a href="javascript:;" class="treeview userMana"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>  <span>考生管理</span></a>'
                + '<ul class="treeview-menu">'
                + '<li><a href="userProfile.html" class="userInfo">学生信息</a></li>'
                + '<li><a href="account.html" class="accountInfo">账号管理</a></li>'
                + '<li><a href="academyInfo.html" class="academyInfo">学院管理</a></li>'
                + '<li><a href="majorMana.html" class="majorInfo">专业管理</a></li>'
                + '</ul>'
                + '</li>'
                + '<li>'
                + '<a href="javascript:;" class="treeview applyMana"><span class="glyphicon glyphicon-adjust" aria-hidden="true"></span> <span>认证管理</span></a>'
                + '<ul class="treeview-menu">'
                + '<li><a href="applyhandle.html" class="applyset">申请管理</a></li>'
                + '<li><a href="adminbill.html" class="billset">账单管理</a></li>'
                + '<li><a href="epnotice.html" class="epnotice">公告管理</a></li>'
                + '</ul>'
                + '</li>'
                + '<li>'
                + '<a href="javascript:;" class="treeview sysMana"><span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span> <span>系统管理</span></a>'
                + '<ul class="treeview-menu">'
                + '<li><a href="sysetting.html" class="systemset">系统参数设置</a></li>'
                + '<li><a href="epsetting.html" class="epstemset">认证参数设置</a></li>'
                + '</ul>' + '</li>' + '</ul>';
            $(".side-bar").append(htmlLeft);
        },
        listMenu: function () {
            var list = '<div class="col-md-3 stinfo  etinfo" id="sinfo">'
                + '<h5>基本信息</h5>' + '</div>'
                + '<div class="col-md-3 stbank etbank" id="sbank">'
                + ' <h5>所选题库</h5>' + '</div>'
                + '<div class="col-md-3 sttemplate ettemplate">'
                + '<h5>试卷参数</h5>' + '</div>'
                + '<div class="col-md-3 stlist etlist">' + '<h5>考生名单</h5>'
                + '</div>';
            $(".step .row").append(list);
        },
        topExam: function (id) {
            var tophtml = '<li role="presentation"  class="quesList" id="quesList"><a href="examInfo.html?id='
                + id
                + '"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> 题目列表</a></li>'
                + '<li role="presentation"  class="mySubmit" id="mySubmit"><a href="mySubmit.html?id='
                + id
                + '"><span class="glyphicon glyphicon-collapse-up" aria-hidden="true"></span> 我的提交</a></li>'
                + '<li role="presentation"  class="allSubmit" id="allSubmit"><a href="allSubmit.html?id='
                + id
                + '"><span class="glyphicon glyphicon-unchecked" aria-hidden="true"></span> 所有提交</a></li>'
                + '<li role="presentation"  class="online" id="online"><a href="onlineUser.html?id='
                + id
                + '"><span class="glyphicon glyphicon-th" aria-hidden="true"></span> 考生名单</a></li>'
                + '<li role="presentation"  class="rank"  id="rank"><a href="scoreTable.html?id='
                + id
                + '"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> 成绩表</a></li>'
                + '<li role="presentation"  class="notice" id="notice"><a href="notice.html?id='
                + id
                + '"><span class="glyphicon glyphicon-align-justify" aria-hidden="true"></span> 公告</a></li>'
                + '<li role="presentation"  class="examTabs" style="float:right;border: 10px;margin-right  :10px"><button type="button" class="btn  btn-primary gradePrint"><span class="glyphicon glyphicon-forward" aria-hidden="true"></span>成绩导出</botton></li>'
                + '<li role="presentation"  class="examTabs" style="float:right;border: 10px"><button type="button" class="btn  btn-info codePrint"><span class="glyphicon glyphicon-forward" aria-hidden="true"></span>代码导出</a></li>';
            $(".examInfoNav").append(tophtml);

        },
        loginout: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../userInfo/logout",
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
        },
        getfooter: function () {
            var footerHtml = "";
            var now = new Date();
            program.year = now.getFullYear();
            footerHtml = '<div class="row ">' + '<div class="footer">'
                + '<p>© 2012-' + program.year
                + ' 计算机科学与技术学院 数据与知识工程实验室</p>' + '<p>要使用GCC编译器，请下载：'
                + '<a href="../system/download?path=/static/devcpp.exe&isUeditorPath=true">Dev cpp</a>'
                + '<span> 体验最佳浏览效果，请使用谷歌浏览器，</span>'
                + '<a href="../system/download?path=/static/Chrome.exe&isUeditorPath=true">点击下载</a>'
                + '</p></div></div>';
            $('body').append(footerHtml);
        },
        importGradeByExamId: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examInfo/importGradeByExamId",
                dataType: 'json',
                async: false,
                data: {
                    examId: par.id
                },
                success: function (result) {
                    console.log(result);
                    if (result.status === 1) {
                        // pubMeth.alertInfo("alert-success", "上传成功");
                        program.path = result.fileDir;
                        program.fileName = result.fileName;
                        window.location.href = '../system/download?path=' + program.path;
                    } else {
                        pubMeth.alertInfo("alert-danger", result.desc);
                    }
                }
            })
        },
        importCodeByExamId: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examInfo/importCodeByExamId",
                dataType: 'json',
                async: false,
                data: {
                    examId: par.id
                },
                success: function (result) {
                    console.log(result);
                    if (result.status === 1) {
                        // pubMeth.alertInfo("alert-success", "上传成功");
                        program.path = result.fileDir;
                        program.fileName = result.fileName;
                        window.location.href = '../system/download?path=' + program.path;
                    } else {
                        pubMeth.alertInfo("alert-danger", result.desc);
                    }
                }
            })
        },
        getsysname: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../siteInfo/selectByCondition",
                dataType: 'json',
                async: false,
                data: {},
                success: function (result) {
                    if (result.status == "1") {
                        var length = result.data.length;
                        for (var i = 0; i < length; i++) {
                            if (result.data[i].name == "systemname") {
                                program.systename = result.data[i].value;
                            }
                        }
                    }
                }
            })
        }
    };
    var par = pubMeth.getQueryObject();
    program.getCookie("name");
    program.getsysname();
    program.head();
    program.leftMenu();
    program.listMenu();
    program.topExam(par.id);
    program.getfooter();

    $(".treeview").on('click', function () {
        $(this).next(".treeview-menu").toggle("slow");
    });
    $(".loginout").click(function () {
        program.loginout();
    });
    $(".loginoutet").click(function () {
        program.loginoutet();
    });
    $(".stinfo").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'editExam.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'editExam.html?Id=' + program.examId;
        }
    });
    $(".stbank").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'editBank.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'editBank.html?Id=' + program.examId;
        }
    });
    $(".sttemplate").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'editParm.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'editParm.html?Id=' + program.examId;
        }
    });
    $(".stlist").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'editUplist.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'editUplist.html?Id=' + program.examId;
        }
    });

    $(".gradePrint").click(function () {
        program.importGradeByExamId();
    });
    $(".codePrint").click(function () {
        program.importCodeByExamId();
    });
});