var loginreg = {
    username: '',
    password: '',
    nickname: '',
    inputMail: '',
    inputpassword: '',
    inputpass: '',
    realName: '',
    telephone: '',
    init: function () {
        loginreg.selectEpinfo();
        $("#userlogin").click(function () {
            $('#myModal').modal();
        });
        $("#userreg").click(function () {
            $('#myReg').modal();
        });
        $("#btnlogin").click(function () {
            loginreg.judgeLogin();
        });
        $("#register").click(function () {
            loginreg.judgeRegister();
        });
    },
    login: function () {
        patest.request({
            url: "../ep/epUserInfo/login"
        }, {
            username: loginreg.username,
            password: loginreg.password
        }, function (result) {
            var exp = new Date();
            if (result.status !== 1) {
                alert(result.desc);
            }
            else {
                $.cookie("sysname", result.data.epUserName);
                $.cookie("sysuserId", result.epUserId);
                window.location.href = "homepaged.html";
            }
        });
    },
    judgeLogin: function () {
        loginreg.username = $("#username").val();
        loginreg.password = $("#password").val();
        if (loginreg.username && loginreg.password) {
            loginreg.login();
        }
        else if (!loginreg.username) {
            alert("用户名不能为空");
        } else if (!loginreg.password) {
            alert("密码不能为空");
        } else {
            alert("请完善登录信息或去注册");
        }
    },
    register: function () {
        patest.request({
            url: "../ep/epUserInfo/register"
        }, {
            username: loginreg.nickname,
            email: loginreg.inputMail,
            password: loginreg.inputpassword,
            phone: loginreg.telephone,
            unit: loginreg.unit
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
        loginreg.nickname = $("#nickname").val();
        loginreg.inputMail = $("#inputMail").val();
        loginreg.inputpassword = $("#inputpassword").val();
        loginreg.inputpass = $("#inputpass").val();
        loginreg.unit = $("#unit").val();
        loginreg.telephone = $("#telephone").val();
        if (loginreg.nickname && loginreg.inputMail && loginreg.inputpassword
            && loginreg.inputpass && loginreg.unit && loginreg.telephone) {
            if (!reg.test(loginreg.inputMail)) {
                alert("邮箱格式不正确");
            }
            else {
                if (loginreg.inputpassword !== loginreg.inputpass) {
                    alert("密码和确认密码不一致");
                }
                else {
                    loginreg.register();
                }
            }
        }
        else {
            alert("请完善信息");
        }
    },
    getQueryObject: function () {
        var url = window.location.href;
        var search = url.substring(url.lastIndexOf("?") + 1);
        var obj = {};
        var reg = /([^?&=]+)=([^?&=]*)/g;
        search.replace(reg, function (rs, $1, $2) {
            var name = decodeURIComponent($1);
            var val = decodeURIComponent($2);
            val = String(val);
            obj[name] = val;
            return rs;
        });
        return obj;
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
    }
};