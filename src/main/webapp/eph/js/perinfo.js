var perinfo = {
    sysuserid: '',
    username: '',
    mail: '',
    address: '',
    telephone: '',
    realname: '',
    init: function () {
        commonet.init(); // 公共模块初始化

        perinfo.selectallbill();
        perinfo.setInfo();
        $(".saveInfo").click(function () {
            $('#perinfo').modal({
                backdrop: 'static'
            });
            $(".mtestname").val(perinfo.username);
            $(".mmail").val(perinfo.mail);
            $(".maddress").val(perinfo.address);
            $(".mtelephone").val(perinfo.telephone);
            $(".mrealname").val(perinfo.realname);
        });
        $(".saveper").click(function () {
            perinfo.username = $(".mtestname").val();
            perinfo.mail = $(".mmail").val();
            perinfo.address = $(".maddress").val();
            perinfo.telephone = $(".mtelephone").val();
            perinfo.realname = $(".mrealname").val();
            perinfo.updateallbill();
        });
        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss'
        });
    },
    selectallbill: function () {
        patest.request({
            url: "../ep/epUserInfo/selectMe"
        }, null, function (result) {
            if (result.status === 1) {
                perinfo.sysuserid = result.data.epUserInfo.userId;
                perinfo.username = result.data.epUserInfo.username;
                perinfo.mail = result.data.epUserInfo.email;
                perinfo.address = result.data.epUserInfo.unit;
                perinfo.telephone = result.data.epUserInfo.phone;
                perinfo.realname = result.data.epUserInfo.realName;
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    updateallbill: function () {
        patest.request({
            url: "../ep/epUserInfo/updateMe"
        }, {
            username: perinfo.username,
            email: perinfo.mail,
            unit: perinfo.address,
            phone: perinfo.telephone,
            realName: perinfo.realname
        }, function (result) {
            if (result.status === 1) {
                patest.alertInfo("alert-success", "保存成功");
                location.reload(false);
                perinfo.selectallbill();
                perinfo.setInfo();
            } else {
                patest.alertInfo("alert-danger", result.desc);
            }
        });
    },
    setInfo: function () {
        $(".testname").val(perinfo.username);
        $(".mail").val(perinfo.mail);
        $(".address").val(perinfo.address);
        $(".telephone").val(perinfo.telephone);
        $(".realname").val(perinfo.realname);
    }
};
