var commonet = {
    sysname: '',
    init: function () {
        commonet.getCookie("sysname");
        commonet.headet();
        commonet.topMenu();
        commonet.testMenu();
        commonet.footcon();
        commonet.selectEpinfo();
        // pubMeth.serCourse();
        var par = patest.getQueryObject();
        $(".treeview").on('click', function () {
            $(this).next(".treeview-menu").toggle("slow");
        });
        $(".loginout").click(function () {
            commonet.loginout();
        });
        $(".loginoutet").click(function () {
            commonet.loginoutet();
        });
        $(".etinfo").click(function () {
            if (par.examId) {
                commonet.examId = par.examId;
                window.location.href = 'addExam.html?examId=' + commonet.examId;
            } else if (par.Id) {
                commonet.examId = par.Id;
                window.location.href = 'addExam.html?Id=' + commonet.examId;
            }
        });
        $(".etbank").click(function () {
            if (par.examId) {
                commonet.examId = par.examId;
                window.location.href = 'addBank.html?examId=' + commonet.examId;
            } else if (par.Id) {
                commonet.examId = par.Id;
                window.location.href = 'addBank.html?Id=' + commonet.examId;
            }
        });
        $(".ettemplate").click(function () {
            if (par.examId) {
                commonet.examId = par.examId;
                window.location.href = 'addParm.html?examId=' + commonet.examId;
            } else if (par.Id) {
                commonet.examId = par.Id;
                window.location.href = 'addParm.html?Id=' + commonet.examId;
            }
        });
        $(".etlist").click(function () {
            if (par.examId) {
                commonet.examId = par.examId;
                window.location.href = 'addUplist.html?examId=' + commonet.examId;
            } else if (par.Id) {
                commonet.examId = par.Id;
                window.location.href = 'addUplist.html?Id=' + commonet.examId;
            }
        });
    },
    /**
     * 考试场次数目
     */
    examselect: function () {
        patest.request({
            url: "../ep/selectExamTotal"
        }, null, function (result) {
            var num = document.getElementById("examnum");
            num.innerText = result.data.total + "场";
        });
    },
    /**
     * 考试人数
     */
    userselect: function () {
        patest.request({
            url: "../ep/selectUserTotal"
        }, null, function (result) {
            var num = document.getElementById("usernum");
            num.innerText = result.data.total + "人";
        });
    },
    /**
     * 考试题目数目
     */
    problemselect: function () {
        patest.request({
            url: "../ep/selectProblemAllCount"
        }, null, function (result) {
            var num = document.getElementById("problemnum");
            num.innerText = result.data.total + "个";
        });
    },
    /**
     * 系统题目类型数目
     */
    knowledgeselect: function () {
        patest.request({
            url: "../ep/selectKnowledgeAllCount"
        }, null, function (result) {
            var num = document.getElementById("knowledgenum");
            num.innerText = result.data.total + "个";
        });
    },

    getCookie: function (objName) {
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] === objName)
                commonet.sysname = unescape(temp[1]);
        }
        if (!commonet.sysname) {
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
            '<span class="glyphicon glyphicon-user" aria-hidden="true"></span>' + commonet.sysname + ' </a>' +
            '<ul class="dropdown-menu">' +
            ' <li class="loginoutet"><a href="#">注销</a></li></ul> </li></ul></nav>';
        $("#headet").append(htmlheader);
    },
    topMenu: function () {
        var tophtml = '<div id="nav"  class="navmenu"><ul>' +
            '<li class="homepage"><a href="homepaged.html">首页</a>' +
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
    loginoutet: function () {
        patest.request({
            url: "../ep/epUserInfo/logout"
        }, null, function (result) {
            if (result.status === 1) {
                commonet.deleCookie("sysname");
                window.location.href = "mainpage.html";
            }
        });
    },
    selectEpinfo: function () {
        patest.request({
            url: "../ep/selectEpSite"
        }, {
            extra: "ep"
        }, function (result) {
            if (result.status === 1) {
                $(".contpeo").html(result.data.ep_serv_people);
                $(".contway").html(result.data.ep_serv_concern);
                $(".teaminfo").html(result.data.ep_team_info);
                $(".principal").html(result.data.ep_principal);
                $(".phone").html("(" + result.data.ep_telephone + ")");
                $(".email").html(result.data.ep_email);
                $(".taddress").html(result.data.ep_address);
                $(".address").html(result.data.ep_address);
            }
        });
    },
    selectNotice: function () {
        patest.request({
            url: "../ep/epNotice/selectByCondition"
        }, {
            page: 1,
            row: 10
        }, function (result) {
            commonet.data = result.data.list;
            commonet.count = result.data.total;
            commonet.showNotice();
            $(".shownotice").empty();
            $(".shownotice").append(commonet.html);
        });
    },
    showNotice: function () {
        commonet.html = "";
        var length;
        if (commonet.data.length >= 8) {
            length = 8;
        } else {
            length = commonet.data.length;
        }
        commonet.html = "";
        for (var i = 0; i < length; i++) {
            var time = commonet.data[i].createTime.split(" ")[0];
            commonet.html += '<li class="list-group-item"><div class="record clearfix"><div class="link">'
                + '<span class="glyphicon glyphicon-chevron-right " aria-hidden="true "></span>'
                + '<a href="epnoticetext.html?epid=' + commonet.data[i].epnoId + '">' + commonet.data[i].title + '</a></div>'
                + '<div class="badge">[' + time + ']</div></div></li>';
        }
    }
};
