define(function (require, exports, module) {
    require('jquery');
    require('jCookie');
    require('../js/commonet.js');
    require('bootstrap');
    require('paginator');

    $(".examMana").next(".treeview-menu").toggle("slow");
    $(".examMana").addClass("leftActive");
    $(".examList").css("color", "white");
    $(".mybill").addClass("onet");
    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');

    require('datetimepicker');
    $(".form_datetime").datetimepicker({
        format: 'yyyy-mm-dd hh:ii:ss'
    });
    var program = {
        applyid: '',
        orderid: '',
        pay: '',
        billnum: '',
        billdes: '',
        billpeo: '',
        telephone: '',
        money: '',
        applytime: '',
        sysuserid: '',
        selectbill: function () {
            console.log(program.sysuserid);
            console.log(program.applyid);
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../order/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    applyId: program.applyid,
                },
                success: function (result) {
                    console.log(result);
                    var list = result.listData;
                    program.billnum = list[0].orderNum;
                    program.billdes = list[0].description;
                    program.billpeo = list[0].linkman;
                    program.telephone = list[0].phone;
                    program.money = list[0].money;
                    program.applytime = list[0].time;
                    program.orderid = list[0].orderId;
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                }
            });
        },
        setValue: function () {
            $(".billnum").val(program.billnum);
            $(".billdes").val(program.billdes);
            $(".billpeo").val(program.billpeo);
            $(".telephone").val(program.telephone);
            $(".money").val(program.money);
            $(".applytime").val(program.applytime);
        },
        updateallbill: function () {
            console.log(program.pay);
            console.log(program.orderid);
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../order/updateById",
                dataType: 'json',
                async: false,
                data: {
                    isPay: program.pay,
                    orderId: program.orderid,
                },
                success: function (result) {
                    console.log(result);
                    if (result.status == 1) {
                        pubMeth.alertInfo("alert-success", "支付成功");
                        window.location.href = "applywait.html?orderid=" + program.orderid;
                    } else {
                        pubMeth.alertInfo("alert-danger", "支付失败");
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                },
            });
        },

    };

    function getUrlParam(name) {
        var reg = new RegExp();
    }

    pubMeth.serCourse();
    var par = pubMeth.getQueryObject();
    program.applyid = par.applyid;
    program.sysuserid = $.cookie("sysuserId");
    program.selectbill();
    program.setValue();

    $("#confirmpay").click(function () {
        console.log(program.orderid);
        program.pay = 1;
        program.updateallbill();
    });

});