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

    var flag = 0;
    var program = {
        data: '',
        count: '',
        billhtml: '',
        pay: '',
        page: '',
        isPay: '',
        isPays: [],
        orderid: '',
        orderids: [],
        sysuserid: '',
        showallbill: function () {
            var order = 1;
            var billlist = program.data;
            var length = program.data.length;
            program.billhtml = "";
            for (var i = 0; i < length; i++) {
                var paystate;
                if (billlist[i].isPay == 0) {
                    paystate = "未支付";
                } else if (billlist[i].isPay == 1) {
                    paystate = "已支付"
                }
                program.billhtml += '<tr  class="' + flag + '-' + flag + '">'
                    + '<td>' + order + '</td>'
                    + '<td>' + billlist[i].orderNum + '</td>'
                    + '<td>' + billlist[i].linkman + '</td>'
                    + '<td>' + billlist[i].phone + '</td>'
                    + '<td>' + billlist[i].time + '</td>'
                    + '<td>' + billlist[i].money + '</td>'
                    + '<td>' + paystate + '</td>'
                    + '<td><button type="button" class="btn btn-info btn-xs payment" id="' + flag + '-' + flag + '">付款</button></td>'
                    + '</tr>';
                program.orderids[i] = billlist[i].orderId;
                program.isPays[i] = billlist[i].isPay;
                flag++;
                order++;
            }

        },
        selectallbill: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../order/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    sysusrId: program.sysuserid,
                    page: program.page,
                    rows: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    program.data = result.listData;
                    program.count = result.total;
                    program.showallbill();
                    $("#listInfo").empty();
                    $("#listInfo").append(program.billhtml);
                }
            });
        },

    }

    function getUrlParam(name) {
        var reg = new RegExp();
    }

    pubMeth.getRowsnum("rowsnum");
    pubMeth.serCourse();
    var par = pubMeth.getQueryObject();
    program.sysuserid = $.cookie("sysuserId");
    program.selectallbill();
    $("#listInfo").on('click', '.payment', function () {
        flag--;
        var length = program.count;
        var index = this.id.split("-")[0];
        if (parseInt(index) + 1 > length) {
            $("." + this.id).remove();
        } else {
            var index = this.id.split("-")[0];
            program.isPay = program.isPays[index];
            if (program.isPay == 0) {
                pubMeth.alertInfo("alert-warning", "未支付！");
            }

        }
    });
    if (program.count > 0) {
        $.jqPaginator('#pagination', {
            totalCounts: program.count,
            visiblePages: 5,
            currentPage: 1,
            pageSize: parseInt(pubMeth.rowsnum),
            first: '<li class="first"><a href="javascript:;">首页</a></li>',
            last: '<li class="last"><a href="javascript:;">尾页</a></li>',
            page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
            onPageChange: function (num, type) {
                if (type == 'init') {
                    return;
                }
                program.page = num;
                program.selectallbill();
            }
        });
    }

});