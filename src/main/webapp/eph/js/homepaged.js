var homepaged = {
    init: function () {
        commonet.init(); // 公共模块初始化
        //$(".examMana").next(".treeview-menu").toggle("slow");
        //$(".homepaged").addClass("active");
        //$(".homepaged").css("color", "white");
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
