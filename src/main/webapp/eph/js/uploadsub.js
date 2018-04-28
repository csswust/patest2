var uploadsub = {
    init: function () {
        commonet.init(); // 公共模块初始化
        $(".uploadsub").addClass("onet");

        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss'
        });
    }
};