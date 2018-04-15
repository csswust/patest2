define(function (require, exports, module) {
    require('jquery');
    require('jCookie');
    require('bootstrap');
    require('../js/loginreg.js');
    require('paginator');
    $(".homepage").addClass("onet");

    var pubMeth = require('../js/public.js');
    var program = {
        html: '',
        data: '',
        count: '',
        /**
         * 公告展示
         */
        selectNotice: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../epNotice/selecByCondition",
                dataType: 'json',
                async: false,
                data: {
                    page: program.page,
                    row: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    if (result.status == 1) {
                        program.data = result.list;
                        program.count = result.total;
                        program.showNotice();
                        $("#applyInfo").empty();
                        $("#applyInfo").append(program.html);
                    } else {
                        pubMeth.alertInfo("alert-danger", "查询错误");
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求错误");
                }
            });
        },
        showNotice: function () {
            program.html = "";
            var length;
            if (program.data.length >= 8) {
                length = 8;
            } else {
                length = program.data.length;
            }
            program.html = "";
            for (var i = 0; i < length; i++) {
                program.html += '<tr><td>' + program.data[i].epnoId + '</td>'
                    + '<td><a href="epnoticetext.html?epid=' + program.data[i].epnoId + '">' + program.data[i].title + '</a></td>'
                    + '<td>' + program.data[i].addTime + '</td>'
                    + '</tr>';
            }
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
                        $(".teaminfo").text(result.data.teamInfo);
                        $(".principal").text(result.data.principal);
                        $(".phone").text("(" + result.data.telephone + ")");
                        $(".email").text(result.data.eMail);
                        $(".taddress").text(result.data.address);
                        $(".address").text(result.data.address);
                    }
                },
                error: function () {

                }
            });
        },
    };
    pubMeth.getRowsnum("rowsnum");
    program.selectNotice();
    program.footcon();
    program.selectEpinfo();
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
                program.selectallInfo();
            }
        });
    }

});
