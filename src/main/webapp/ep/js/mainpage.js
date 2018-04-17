var mainpage = {
    username: "",
    password: "",
    lookhtml: "",
    lookhtmls: "",
    infohtml: "",
    data: "",
    html: "",
    count: "",
    epnoIds: [],
    init: function () {
        loginreg.init();
        $('#username').focus();
        mainpage.examselect();
        mainpage.userselect();
        mainpage.problemselect();
        mainpage.knowledgeselect();
        mainpage.selectNotice();
        // 左侧导航
        var oDivlf = document.getElementById('topnav');
        var links = oDivlf.getElementsByTagName("a");
        for (var i = 0; i < links.length; i++) {
            links[i].index = i;
            links[i].onclick = function () {
                for (var j = 0; j < links.length; j++) {
                    links[j].className = ' ';
                }
                this.className = 'active ';
            };
        }
        $('#carousel-example-generic').carousel({
            interval: 2000
        }); //每隔5秒自动轮播
    },
    /**
     * 考试场次数目
     */
    examselect: function () {
        patest.request({
            url: "../ep/selectExamTotal"
        }, null, function (result) {
            var num = document.getElementById("examnum");
            num.innerText = result.data.total + "场";
        });
    },
    /**
     * 考试人数
     */
    userselect: function () {
        patest.request({
            url: "../ep/selectUserTotal"
        }, null, function (result) {
            var num = document.getElementById("usernum");
            num.innerText = result.data.total + "人";
        });
    },
    /**
     * 考试题目数目
     */
    problemselect: function () {
        patest.request({
            url: "../ep/selectProblemAllCount"
        }, null, function (result) {
            var num = document.getElementById("problemnum");
            num.innerText = result.data.total + "个";
        });
    },
    /**
     * 系统题目类型数目
     */
    knowledgeselect: function () {
        patest.request({
            url: "../ep/selectKnowledgeAllCount"
        }, null, function (result) {
            var num = document.getElementById("knowledgenum");
            num.innerText = result.data.total + "个";
        });
    },
    /**
     * 公告展示
     */
    selectNotice: function () {
        patest.request({
            url: "../ep/epNotice/selectByCondition"
        }, {
            page: mainpage.page,
            row: 10
        }, function (result) {
            mainpage.data = result.data.list;
            mainpage.count = result.data.total;
            mainpage.showNotice();
            $(".shownotice").empty();
            $(".shownotice").append(mainpage.html);
        });
    },
    showNotice: function () {
        mainpage.html = "";
        var length;
        if (mainpage.data.length >= 8) {
            length = 8;
        } else {
            length = mainpage.data.length;
        }
        mainpage.html = "";
        for (var i = 0; i < length; i++) {
            var time = mainpage.data[i].createTime.split(" ")[0];
            mainpage.html += '<li class="list-group-item"><div class="record clearfix"><div class="link">'
                + '<span class="glyphicon glyphicon-chevron-right " aria-hidden="true "></span>'
                + '<a href="epnoticetext.html?epid=' + mainpage.data[i].epnoId + '">' + mainpage.data[i].title + '</a></div>'
                + '<div class="badge">[' + time + ']</div></div></li>';
        }
    }
};