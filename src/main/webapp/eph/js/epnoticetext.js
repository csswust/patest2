var epnoticetext = {
    epnoId: '',
    init: function () {
        commonet.init();
        $(".epnotices").addClass("onet");

        var par = patest.getQueryObject();
        console.log(par.epid);
        epnoticetext.epnoId = par.epid;
        epnoticetext.selectId();
    },
    selectId: function () {
        patest.request({
            url: "../ep/epNotice/selectByCondition"
        }, {
            epnoId: epnoticetext.epnoId
        }, function (result) {
            $(".title").html(result.data.list[0].title);
            $(".content").html(result.data.list[0].content);
        });
    }
};

