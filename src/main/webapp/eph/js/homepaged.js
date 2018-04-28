var homepaged = {
    init: function () {
        commonet.init(); // 公共模块初始化
        $(".homepaged").addClass("onet");
        commonet.examselect();
        commonet.userselect();
        commonet.problemselect();
        commonet.knowledgeselect();
        commonet.selectEpinfo();
        commonet.selectNotice();
        homepaged.Carousel();
    },
    Carousel:function () {
        $('#carousel-example-generic').carousel({
            interval: 2000
        }); //每隔5秒自动轮播
    }
};
