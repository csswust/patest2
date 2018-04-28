define(function (require, exports, module) {
    require("jquery");
    var pubMeth = require('../js/public.js');
    var program = {
        sysname: '',
        getCookie: function (objName) {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName)
                    program.sysname = unescape(temp[1]);
            }
            if (program.sysname == "" || program.sysname == "undefined") {
                window.location.href = "mainpage.html";
            }
        },
        deleCookie: function (name) {//为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间 
            var date = new Date();
            date.setTime(date.getTime() - 10000);
            document.cookie = name + "=a; expires=" + date.toGMTString();
        },
        headet: function () {
            var htmlheader =
                '<nav class="navbar navbar-default">' +
                '<div class="navbar-header "><a class="navbar-brand" href="#">程序设计能力认证平台</a></div>' +
                '<ul class="nav navbar-nav navbar-right">' +
                '<li class="dropdown">' +
                '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">' +
                '<span class="glyphicon glyphicon-user" aria-hidden="true"></span>' + program.sysname + ' </a>' +
                '<ul class="dropdown-menu">' +
                ' <li class="loginoutet"><a href="#">注销</a></li></ul> </li></ul></nav>';
            $("#headet").append(htmlheader);
        },
        topMenu: function () {
            var tophtml = '<div id="nav"  class="navmenu"><ul>' +
                '<li class="homepage"><a href="home.html">首页</a>' +
                '<ul><li><a href="#instro">平台介绍</a></li>' +
                '<li><a href="#info">平台公告</a></li>' +
                '<li><a href="score.html">成绩查询</a></li>' +
                '<li><a href="#abtwe">关于我们</a></li></ul></li>' +
                '<li class="applyexam"><a href="applyexam.html">考试申请</a></li>' +
                '<li class="myapply"><a href="applywait.html">我的申请</a></li>' +
                '<li class="uploadsub"><a href="uploadsub.html">上传题目</a></li>' +
                '<li class="perinfo"><a href="perinfo.html">个人信息</a></li>' +
                '<li class="mybill"><a href="billlist.html">我的账单</a></li>' +
                '<li class="mytest"><a href="test.html">我的考试</a></li> </ul></div>';
            $(".topmenu").html(tophtml);
        },
        testMenu: function () {
            var list = '<div class="col-md-3 etinfo" id="sinfo">' +
                '<h5>基本信息</h5>' +
                '</div>' +
                '<div class="col-md-3  etbank" id="sbank">' +
                ' <h5>所选题库</h5>' +
                '</div>' +
                '<div class="col-md-3  ettemplate">' +
                '<h5>试卷参数</h5>' +
                '</div>' +
                '<div class="col-md-3  etlist">' +
                '<h5>考生名单</h5>' +
                '</div>';
            $(".etstep .row").append(list);
        },
        footcon: function () {
            var foothtml = '<div class="row"><div class="col-md-4 col-md-offset-1">' +
                '<p style="font-size:20px;font-weight:bold;"><span class="glyphicon glyphicon-asterisk"></span>团队介绍</p>' +
                '<p class="teaminfo"></p>' +
                '</div>' +
                '<div class="col-md-5 col-md-offset-1">' +
                '<div class="linkt"><span class="glyphicon glyphicon-envelope"></span> 关于我们</div>' +
                '<ul class="contact">' +
                '<li><span class="glyphicon glyphicon-phone"></span> <span>联系人:</span>  <span class="principal"></span>  <span class="phone"></span></li>' +
                '<li><span class="glyphicon glyphicon-envelope"></span> <span>E-Mail：</span> <span class="email"></span></li>' +
                '<li><span class="glyphicon glyphicon-map-marker"></span> <span>实验室地址：</span> <span class="taddress"> </span></li>' +
                '<li><span class="glyphicon glyphicon-home"></span> <span>地址：</span> <span class="address"></span></li>' +
                '</ul></div></div>' +
                '<div class="row copyright">' +
                '©Copyright 2016 - 2017<a href="http://www.cs.swust.edu.cn/academic/lab-kownledge.html">西南科技大学数据与知识工程实验室</a>版权所有' +
                '</div>';
            $("#footer").append(foothtml);
        },
        selectEpinfo: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../epInfo/selectEpInfo",
                dataType: 'json',
                async: false,
                data: {},
                success: function (result) {
                    console.log(result);
                    if (result.status == 1) {
                        $(".teaminfo").html(result.data.teamInfo);
                        $(".principal").html(result.data.principal);
                        $(".phone").html("(" + result.data.telephone + ")");
                        $(".email").html(result.data.eMail);
                        $(".taddress").html(result.data.address);
                        $(".address").html(result.data.address);
                    }
                },
                error: function () {

                }
            });
        },
        loginoutet: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../sysUser/logout",
                dataType: 'json',
                async: false,
                success: function (result) {
                    if (result.status == "1") {
                        window.location.href = "mainpage.html";
                    }
                }
            });
            program.deleCookie("sysname");
        }
    };
    program.getCookie("sysname");
    program.headet();
    program.topMenu();
    program.testMenu();
    program.footcon();
    program.selectEpinfo();
    pubMeth.serCourse();
    var par = pubMeth.getQueryObject();


    $(".treeview").on('click', function () {
        $(this).next(".treeview-menu").toggle("slow");
    });
    $(".loginout").click(function () {
        program.loginout();
    });
    $(".loginoutet").click(function () {
        program.loginoutet();
    });
    $(".etinfo").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'addExam.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'addExam.html?Id=' + program.examId;
        }
    });
    $(".etbank").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'addBank.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'addBank.html?Id=' + program.examId;
        }
    });
    $(".ettemplate").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'addParm.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'addParm.html?Id=' + program.examId;
        }
    });
    $(".etlist").click(function () {
        if (par.examId) {
            program.examId = par.examId;
            window.location.href = 'addUplist.html?examId=' + program.examId;
        } else if (par.Id) {
            program.examId = par.Id;
            window.location.href = 'addUplist.html?Id=' + program.examId;
        }
    });
});