var homepaged = {
    init: function () {
        commonet.init(); // 公共模块初始化
        $(".homepaged").addClass("onet");
        homepaged.testMenu();

        // 统计个数
        homepaged.examselect();
        homepaged.userselect();
        homepaged.problemselect();
        homepaged.knowledgeselect();
        // 查询公告
        homepaged.selectNotice();
        homepaged.Carousel();
    },
    Carousel: function () {
        $('#carousel-example-generic').carousel({
            interval: 2000
        }); //每隔5秒自动轮播
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
    examselect: function () {
        patest.request({
            url: "../ep/selectExamTotal"
        }, null, function (result) {
            var num = document.getElementById("examnum");
            num.innerText = result.data.total + "场";
        });
    },
    userselect: function () {
        patest.request({
            url: "../ep/selectUserTotal"
        }, null, function (result) {
            var num = document.getElementById("usernum");
            num.innerText = result.data.total + "人";
        });
    },
    problemselect: function () {
        patest.request({
            url: "../ep/selectProblemAllCount"
        }, null, function (result) {
            var num = document.getElementById("problemnum");
            num.innerText = result.data.total + "个";
        });
    },
    knowledgeselect: function () {
        patest.request({
            url: "../ep/selectKnowledgeAllCount"
        }, null, function (result) {
            var num = document.getElementById("knowledgenum");
            num.innerText = result.data.total + "个";
        });
    },
    selectNotice: function () {
        patest.request({
            url: "../ep/epNotice/selectByCondition"
        }, {
            page: 1,
            row: 10
        }, function (result) {
            homepaged.data = result.data.list;
            homepaged.count = result.data.total;
            homepaged.showNotice();
            $(".shownotice").empty();
            $(".shownotice").append(homepaged.html);
        });
    },
    showNotice: function () {
        homepaged.html = "";
        var length;
        if (homepaged.data.length >= 8) {
            length = 8;
        } else {
            length = homepaged.data.length;
        }
        homepaged.html = "";
        for (var i = 0; i < length; i++) {
            var time = homepaged.data[i].createTime.split(" ")[0];
            homepaged.html += '<li class="list-group-item"><div class="record clearfix"><div class="link">'
                + '<span class="glyphicon glyphicon-chevron-right " aria-hidden="true "></span>'
                + '<a href="epnoticetext.html?epid=' + homepaged.data[i].epnoId + '">' + homepaged.data[i].title + '</a></div>'
                + '<div class="badge">[' + time + ']</div></div></li>';
        }
    }
};
