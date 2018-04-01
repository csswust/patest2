var parm = pubMeth.getQueryObject();
var program = {
    examNotes: '',
    role: '',
    getWebInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../siteInfo/selectByName",
            dataType: 'json',
            async: false,
            data: {
                name: "examNotes"
            },
            success: function (result) {
                program.examNotes = result.value;
                $(".examnotes").html(program.examNotes);
            }
        });
    },
    getCookie: function (objName) {
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] == objName)
                program.role = unescape(temp[1]);
        }
        if (program.role == "" || program.role == "undefined") {
            window.location.href = "examnotes.html";
        }
    }
};
program.getCookie("role");
program.getWebInfo();
$("#inlineCheckbox").click(function () {
    if ($("#inlineCheckbox").is(":checked") == false) { // 未勾选则弹出警示框
        $('.button').attr("disabled", "disabled");
    } else {
        $('.button').removeAttr("disabled");
    }
});
$(".button").click(function () {
    window.location.href = "contents.html?" + "&eId=" + parm["eId"];
});