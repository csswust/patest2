$(".applyMana").next(".treeview-menu").toggle("slow");
$(".applyMana").addClass("leftActive");
$(".billset").css("color", "white");

var program = {
    page: '1',
    data: [],
    billhtml: '',
    count: '',
    sysusrid: '',
    perbill: '',
    allbill: '',
    examname: [],
    showallbill: function () {
        var billlist = program.data;
        var epApplys = program.epApplyInfoList;
        var epUsers = program.epUserInfoList;
        program.billhtml = "";
        for (var i = 0; i < billlist.length; i++) {
            var payinfo;
            if (billlist[i].isPay === 0) {
                payinfo = "未支付";
            } else if (billlist[i].isPay === 1) {
                payinfo = "已支付";
            }
            var orderId = billlist[i].orderId;
            var payMoney = '<td><button type="button" class="btn btn-primary btn-xs updateOrder" id="' + i + '">修改</button>';
            if (billlist[i].isPay === 0) {
                payMoney += ' <button type="button" class="btn btn-info btn-xs payment" id="' + orderId + '">付款</button>'
            } else {
                payMoney += ' <button type="button" class="btn btn-info btn-xs payment" disabled="disabled" id="' + orderId + '">付款</button>'
            }
            payMoney += '</td>';
            program.billhtml += '<tr  class="' + orderId + '">'
                + '<td><input type="checkbox" value="' + orderId + '" name="title"/></td>'
                + '<td>' + orderId + '</td>'
                + '<td>' + billlist[i].orderNum + '</td>'
                + '<td>' + epApplys[i].examName + '</td>'
                + '<td>' + epUsers[i].realName + '</td>'
                + '<td>' + epUsers[i].phone + '</td>'
                + '<td>' + billlist[i].createTime + '</td>'
                + '<td>' + billlist[i].money + '</td>'
                + '<td>' + payinfo + '</td>'
                + payMoney
                + '</tr>';
        }
    },
    //查询账单信息
    selectallbill: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epOrderInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                program.data = result.data.list;
                program.epApplyInfoList = result.data.epApplyInfoList;
                program.epUserInfoList = result.data.epUserInfoList;
                program.count = result.data.total;
                program.showallbill();
                $("#billInfo").empty();
                $("#billInfo").append(program.billhtml);
            }
        });
    },
    //删除账单信息
    deletebill: function () {
        $('.delete').on('click', function (e) {
            var valArr = new Array;
            $(":checkbox[name='title']:checked").each(function (i) {
                valArr[i] = $(this).val();
            });
            var vals = valArr.join(',');// 转换为逗号隔开的字符串
            console.log(vals);
            if (vals !== "") {
                if (confirm("你确定要删除这些" + vals + "账单吗？")) {
                    $.ajax({
                        type: "post",
                        content: "application/x-www-form-urlencoded;charset=UTF-8",
                        url: "../epOrderInfo/deleteByIds",
                        dataType: 'json',
                        async: false,
                        data: {
                            ids: vals
                        },
                        success: function (result) {
                            console.log(result);
                            if (result.status > 0) {
                                pubMeth.alertInfo("alert-success", "删除成功！");
                                program.selectallbill();
                                $("#modalexamdelete").modal('hide');
                            } else {
                                pubMeth.alertInfo("alert-danger", "删除失败！");
                            }
                        }
                    });
                }
            } else {
                pubMeth.alertInfo("alert-info", "请先勾选删除项！");
            }
        });
    },
    // 付款
    payment: function (id) {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epOrderInfo/payment",
            dataType: 'json',
            async: false,
            data: {
                orderId: id
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "付款成功");
                    program.selectallbill();
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }
        });
    },
    // 付款
    updtateById: function () {

        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../epOrderInfo/updateById",
            dataType: 'json',
            async: false,
            data: {
                orderId: program.orderId,
                money: program.money
            },
            success: function (result) {
                console.log(result);
                if (result.status === 1) {
                    pubMeth.alertInfo("alert-success", "修改成功");
                    program.selectallbill();
                } else {
                    pubMeth.alertInfo("alert-danger", result.desc);
                }
            }
        });
    }
};
$("#billInfo").on('click', '.payment', function () {
    var index = this.id;
    program.payment(index);
});
$("#billInfo").on('click', '.updateOrder', function () {
    $('#updateOrder').modal({});
    $(".updateNum").val(program.data[this.id].orderNum);
    $(".updateMoney").val(program.data[this.id].money);
    program.orderId = program.data[this.id].orderId;
});
$(".saveOrder").click(function () {
    program.money = $(".updateMoney").val();
    if (!program.money) {
        pubMeth.alertInfo("alert-danger", "金额是必填参数");
    } else {
        program.updtateById();
    }
    $('#updateOrder').modal('hide');
});
pubMeth.getRowsnum("rowsnum");
program.selectallbill();
program.deletebill();
if (program.count > 0) {
    $(".countnum").html(program.count);
    $(".perbill").html(program.perbill);
    $(".allbill").html(program.allbill);
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
} else {
    $(".pagenum").css("display", "none");
}