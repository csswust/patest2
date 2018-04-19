var uploadsub = {
    init: function () {
        commonet.init(); // 公共模块初始化

        $(".examMana").next(".treeview-menu").toggle("slow");
        $(".examMana").addClass("leftActive");
        $(".examList").css("color", "white");
        $(".uploadsub").addClass("onet");

        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii:ss'
        });
    }
};