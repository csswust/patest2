var epnotices = {
    html: '',
    data: '',
    count: '',
    init: function () {
        patest.getRowsnum("rowsnum");

        commonet.init();
        $(".epnotices").addClass("onet");
        epnotices.selectNotice();

        if (epnotices.count > 0) {
            $.jqPaginator('#pagination', {
                totalCounts: epnotices.count,
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
                    epnotices.page = num;
                    epnotices.selectallInfo();
                }
            });
        }
    },
    /**
     * 公告展示
     */
    selectNotice: function () {
        patest.request({
            url: "../ep/epNotice/selectByCondition"
        }, {
            page: epnotices.page,
            row: patest.rowsnum
        }, function (result) {
            epnotices.data = result.data.list;
            epnotices.count = result.data.total;
            epnotices.showNotice();
            $("#applyInfo").empty();
            $("#applyInfo").append(epnotices.html);
        });
    },
    showNotice: function () {
        epnotices.html = "";
        for (var i = 0; i < epnotices.data.length; i++) {
            epnotices.html += '<tr><td>' + epnotices.data[i].epnoId + '</td>'
                + '<td><a href="epnoticetext.html?epid=' + epnotices.data[i].epnoId + '">' + epnotices.data[i].title + '</a></td>'
                + '<td>' + epnotices.data[i].createTime + '</td>'
                + '</tr>';
        }
    }
};

