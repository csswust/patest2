var flag = 0;
var billlist = {
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
    init: function () {
        commonet.init(); // 公共模块初始化
        $(".mybill").addClass("onet");

        patest.getRowsnum("rowsnum");
        billlist.selectallbill();
        $("#listInfo").on('click', '.payment', function () {
            var index = this.id;
            billlist.payment(index);
        });
        if (billlist.count > 0) {
            $.jqPaginator('#pagination', {
                totalCounts: billlist.count,
                visiblePages: 5,
                currentPage: 1,
                pageSize: parseInt(patest.rowsnum),
                first: '<li class="first"><a href="javascript:;">首页</a></li>',
                last: '<li class="last"><a href="javascript:;">尾页</a></li>',
                page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
                onPageChange: function (num, type) {
                    if (type == 'init') {
                        return;
                    }
                    billlist.page = num;
                    billlist.selectallbill();
                }
            });
        }
    },
    showallbill: function () {
        var order = 1;
        var bills = billlist.data;
        var epUsers = billlist.epUserInfoList;
        var epApplys = billlist.epApplyInfoList;
        var length = bills.length;
        billlist.billhtml = "";
        for (var i = 0; i < length; i++) {
            var paystate;
            if (bills[i].isPay == 0) {
                paystate = "未支付";
            } else if (bills[i].isPay == 1) {
                paystate = "已支付"
            }
            var applyId = epApplys[i].applyId;
            var orderId = bills[i].orderId;
            var payMoney = '';
            if (bills[i].isPay === 0) {
                payMoney = '<td><button type="button" class="btn btn-info btn-xs payment" id="' + orderId + '">付款</button></td>'
            } else{
                payMoney = '<td><button type="button" class="btn btn-info btn-xs payment" disabled="disabled" id="' + orderId + '">付款</button></td>'
            }
            billlist.billhtml += '<tr  class="' + orderId + '">'
                + '<td>' + orderId + '</td>'
                + '<td>' + bills[i].orderNum + '</td>'
                + '<td><a   href="applyexam.html?applyid=' + applyId + '">' + epApplys[i].examName + '</a></td>'
                + '<td>' + epApplys[i].peopleNumber + '</td>'
                + '<td>' + epUsers[i].username + '</td>'
                + '<td>' + epUsers[i].phone + '</td>'
                + '<td>' + bills[i].createTime + '</td>'
                + '<td>' + bills[i].money + '</td>'
                + '<td>' + paystate + '</td>'
                + payMoney
                + '</tr>';
        }

    },
    selectallbill: function () {
        patest.request({
            url: "../ep/epOrderInfo/selectByCondition"
        }, {
            page: billlist.page,
            rows: patest.rowsnum
        }, function (result) {
            billlist.data = result.data.list;
            billlist.count = result.data.total;
            billlist.epUserInfoList = result.data.epUserInfoList;
            billlist.epApplyInfoList = result.data.epApplyInfoList;
            billlist.showallbill();
            $("#listInfo").empty();
            $("#listInfo").append(billlist.billhtml);
        });
    },
    // 付款
    payment: function (id) {
        $('#payModal').modal({
            backdrop: 'static'
        });
        patest.request({
            url: "../siteInfo/selectByName"
        }, {
            name: "ep_pay_info"
        }, function (result) {
            billlist.faqs = result.value;
            $(".content").html(billlist.faqs);
        });
    }
};