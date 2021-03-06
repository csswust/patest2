var commonet = {
    sysname: null,
    epUserId: null,
    init: function () {
        commonet.getCookie("sysname");
        // 导航
        commonet.headet();
        commonet.getsysname();
        // 页脚
        commonet.footcon();
        commonet.selectEpinfo();
    },

    getsysname: function () {
        patest.request({
            url: "../siteInfo/selectByName"
        }, {
            name: "systemname"
        }, function (result) {
            commonet.systename = result.value;
            $(".navbar-brand").text(commonet.systename);
        });
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
        $(".etstep .row").append(list);
        commonet.editExam();
    },
    editExam: function () {
        var par = patest.getQueryObject();
        $(".etinfo").click(function () {
            commonet.examId = par.Id;
            window.location.href = 'addExam.html?Id=' + commonet.examId;
        });
        $(".etbank").click(function () {
            commonet.examId = par.Id;
            window.location.href = 'addBank.html?Id=' + commonet.examId;
        });
        $(".ettemplate").click(function () {
            commonet.examId = par.Id;
            window.location.href = 'addParm.html?Id=' + commonet.examId;
        });
        $(".etlist").click(function () {
            commonet.examId = par.Id;
            window.location.href = 'addUplist.html?Id=' + commonet.examId;
        });
    },

    getCookie: function (objName) {
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] === objName)
                commonet.sysname = unescape(temp[1]);
        }
    },
    deleCookie: function (name) {
        var date = new Date();
        date.setTime(date.getTime() - 10000);
        document.cookie = name + "=a; expires=" + date.toGMTString();
    },
    headet: function () {
        if (commonet.sysname) {
            commonet.alreadyLogin();
        } else {
            commonet.notLogin();
        }
    },
    alreadyLogin: function () {
        var menuHtml = '<li class="homepaged"><a href="home.html">首页</a></li>' +
            '<li class="applyexam"><a href="applyexam.html">考试申请</a></li> ' +
            '<li class="myapply"><a href="applywait.html">我的申请</a></li> ' +
            '<li class="mybill"><a href="billlist.html">我的账单</a></li> ' +
            '<li class="mytest"><a href="test.html">我的考试</a></li> ' +
            '<li class="uploadsub"><a href="uploadsub.html">上传题目</a></li> ' +
            '<li class="score"><a href="score.html">成绩查询</a></li> ' +
            '<li class="epnotices"><a href="epnotices.html">公告</a></li>';
        $(".menuHtml").html(menuHtml);
        var userHtml = '<li class="dropdown"> ' +
            '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"> ' +
            '<span class="glyphicon glyphicon-user" aria-hidden="true"></span>' +
            commonet.sysname +
            '</a> ' +
            '<ul class="dropdown-menu"> ' +
            '<li class="perinfo"><a href="perinfo.html">个人信息</a></li> ' +
            '<li class="loginoutet"><a href="#">注销</a></li> ' +
            '</ul> ' +
            '</li>';
        $(".userHtml").html(userHtml);
        $(".loginoutet").click(function () {
            commonet.loginoutet();
        });
    },
    notLogin: function () {
        var menuHtml = '<li class="homepaged"><a href="home.html">首页</a></li>' +
            '<li class="score"><a href="score.html">成绩查询</a></li> ' +
            '<li class="epnotices"><a href="epnotices.html">公告</a></li>';
        $(".menuHtml").html(menuHtml);
        var userHtml = '<li class="userlogin"><a href="#" id="userlogin">登录</a></li> ' +
            '<li class="userreg"><a href="#" id="userreg">注册</a></li>' +
            '<li class="student"><a href="../html/login.html">学生登录</a></li>';
        $(".userHtml").html(userHtml);
        commonet.loginModal();
        commonet.registermodal();
        $("#userlogin").click(function () {
            $('#myModal').modal();
        });
        $("#userreg").click(function () {
            $('#myReg').modal();
        });
        $("#btnlogin").click(function () {
            commonet.judgeLogin();
        });
        $("#register").click(function () {
            commonet.judgeRegister();
        });
    },

    footcon: function () {
        var foothtml = '<div class="row"><div class="col-md-4 col-md-offset-1">' +
            '<p style="margin-top: 20px;font-size:20px;font-weight:bold;"><span class="glyphicon glyphicon-asterisk"></span>团队介绍</p>' +
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

    loginoutet: function () {
        patest.request({
            url: "../ep/epUserInfo/logout"
        }, null, function (result) {
            if (result.status === 1) {
                commonet.deleCookie("sysname");
                window.location.href = "home.html";
            }
        });
    },
    loginModal: function () {
        var loginModal = '<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> ' +
            '<div class="modal-dialog " style="width:380px;margin-top: 160px;"> ' +
            '<div class="modal-content"> ' +
            '<div style="text-align: center;"> ' +
            '<button style="margin-right: 30px;" type="button" class="close" data-dismiss="modal" aria-hidden="true"> ' +
            '&times; ' +
            '</button> ' +
            '<h3 style="color: orange;margin: 15px auto;" class="modal-title" id="myModalLabel2">欢迎登录</h3> ' +
            '<form class="form-horizontal"> ' +
            '<div class="form-group"> ' +
            '<div class="col-md-8 col-md-offset-2 "> ' +
            '<input style="height: 40px;" type="text" class="form-control  inputDlg" id="username" placeholder="请输入登录账号" z-index="1"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<div class="col-md-8 col-md-offset-2"> ' +
            '<input style="height: 40px;" type="password" class="form-control inputDlg" id="password" placeholder="请输入密码，区分大小写" z-index="2"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<div class="col-md-8 col-md-offset-2"> ' +
            '<input type="text" name="code" id="idenCode" style="width: 80px;" /> ' +
            '<img id="imgObj" alt="验证码" ' +
            'src="../system/getIdenCode"> <a href="#" onclick="commonet.changeImg()">换一张</a>' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<div class="col-md-8 col-md-offset-2" style="color: #FFFFFF;text-align: center;"> ' +
            '<button id="btnlogin" style="width: 100%;height: 45px;color:#fff;font-size: 16px;font-weight: bold; background-color:#3c8dbd;" type="button" class="btn btn-block btn-default">登&nbsp;&nbsp;录 ' +
            '</button> ' +
            '</div> ' +
            '</div> ' +
            '</form> ' +
            '</div> ' +
            '</div> ' +
            '</div> ' +
            '</div>';
        $("#loginmodal").html(loginModal);
    },
    registermodal: function () {
        var registermodal = '<div class="modal fade" id="myReg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> ' +
            '<div class="modal-dialog " style="width:380px;margin-top: 160px;"> ' +
            '<div class="modal-content"> ' +
            '<div style="text-align: center;"> ' +
            '<button style="margin-right: 30px;" type="button" class="close" data-dismiss="modal" aria-hidden="true"> ' +
            '&times; ' +
            '</button> ' +
            '<h3 style="color: orange;margin: 15px auto;" class="modal-title" id="myModalLabel1">欢迎注册</h3> ' +
            '<form class="form-horizontal"> ' +
            '<div class="form-group"> ' +
            '<label for="inputMail" class="col-md-4 control-label">昵&nbsp;称：</label> ' +
            '<div class="col-md-6 "> ' +
            '<input type="text" class="form-control  inputDlg" id="nickname" placeholder="" z-index="1"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<label for="inputMail" class="col-md-4 control-label">邮&nbsp;箱：</label> ' +
            '<div class="col-md-6 "> ' +
            '<input type="text" class="form-control  inputDlg" id="inputMail" placeholder="" z-index="1"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<label for="inputpassword" class="col-md-4  control-label">密&nbsp;码：</label> ' +
            '<div class="col-md-6 "> ' +
            '<input type="password" class="form-control inputDlg" id="inputpassword" placeholder="" z-index="2"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<label for="inputpass" class="col-md-4  control-label">确认密码：</label> ' +
            '<div class="col-md-6"> ' +
            '<input type="password" class="form-control inputDlg" id="inputpass" placeholder="" z-index="3"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<label for="inputMail" class="col-md-4 control-label">联系方式：</label> ' +
            '<div class="col-md-6 "> ' +
            '<input type="text" class="form-control  inputDlg" id="telephone" placeholder="" z-index="1"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group"> ' +
            '<label for="inputMail" class="col-md-4 control-label">联系单位：</label> ' +
            '<div class="col-md-6 "> ' +
            '<input type="text" class="form-control  inputDlg" id="unit" placeholder="" z-index="1"/> ' +
            '</div> ' +
            '</div> ' +
            '<div class="form-group btnreg"> ' +
            '<div class="col-md-8 col-md-offset-2" style="padding-top: 20px;margin-top:-20px;"> ' +
            '<button style="width: 100%;height: 45px;font-size: 16px;color:#fff;font-weight: bold;background-color:#3c8dbd;"type="button" class="btn btn-block btn-default " id="register">立即注册 </button> ' +
            '</div> ' +
            '</div> ' +
            '</form> ' +
            '</div> ' +
            '</div> ' +
            '</div> ' +
            '</div>';
        $("#registermodal").html(registermodal);
    },
    login: function () {
        patest.request({
            url: "../ep/epUserInfo/login"
        }, {
            username: commonet.username,
            password: commonet.password,
            idenCode: commonet.idenCode
        }, function (result) {
            commonet.changeImg();
            if (result.status !== 1) {
                alert(result.desc);
            }
            else {
                $.cookie("sysname", result.data.epUserName);
                $.cookie("sysuserId", result.epUserId);
                window.location.reload();
            }
        });
    },
    judgeLogin: function () {
        commonet.username = $("#username").val();
        commonet.password = $("#password").val();
        commonet.idenCode = $("#idenCode").val();
        if (commonet.username && commonet.password && commonet.idenCode) {
            commonet.login();
        }
        else if (!commonet.username) {
            alert("用户名不能为空");
        } else if (!commonet.password) {
            alert("密码不能为空");
        } else if (!commonet.idenCode) {
            alert("验证码不能为空");
        } else {
            alert("请完善登录信息或去注册");
        }
    },
    register: function () {
        patest.request({
            url: "../ep/epUserInfo/register"
        }, {
            username: commonet.nickname,
            email: commonet.inputMail,
            password: commonet.inputpassword,
            phone: commonet.telephone,
            unit: commonet.unit
        }, function (result) {
            if (result.status === 1) {
                $("#myReg").modal('hide');
                alert("注册成功");
            } else {
                alert(result.desc);
            }
        });
    },
    judgeRegister: function () {
        var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
        commonet.nickname = $("#nickname").val();
        commonet.inputMail = $("#inputMail").val();
        commonet.inputpassword = $("#inputpassword").val();
        commonet.inputpass = $("#inputpass").val();
        commonet.unit = $("#unit").val();
        commonet.telephone = $("#telephone").val();
        if (commonet.nickname && commonet.inputMail && commonet.inputpassword
            && commonet.inputpass && commonet.unit && commonet.telephone) {
            if (!reg.test(commonet.inputMail)) {
                alert("邮箱格式不正确");
            }
            else {
                if (commonet.inputpassword !== commonet.inputpass) {
                    alert("密码和确认密码不一致");
                }
                else {
                    commonet.register();
                }
            }
        }
        else {
            alert("请完善信息");
        }
    },
    changeImg: function () {
        var imgSrc = $("#imgObj");
        var src = imgSrc.attr("src");
        imgSrc.attr("src", commonet.chgUrl(src));
    },
    chgUrl: function (url) {
        var timestamp = (new Date()).valueOf();
        url = "../system/getIdenCode";
        if ((url.indexOf("&") >= 0)) {
            url = url + "×tamp=" + timestamp;
        } else {
            url = url + "?timestamp=" + timestamp;
        }
        return url;
    }
};
