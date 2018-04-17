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
        commonet.selectEpinfo();
        commonet.examselect();
        commonet.userselect();
        commonet.problemselect();
        commonet.knowledgeselect();
        commonet.selectNotice();
        mainpage.Carousel();
    },
    Carousel: function () {
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
    }
};