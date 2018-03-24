define(function (require, exports, module) {
    require('jquery');
    require('bootstrap');
    var nav = require('../js/nav.js');
    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    var parm = pubMeth.getQueryObject();
    var program = {
        examNotes: '',
        role: '',
        getWebInfo: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../siteInfo/selectByCondition",
                dataType: 'json',
                async: false,
                data: {
                    name: "examnotes"
                },
                success: function (result) {
                    var length = result.data.length;
                    for (var i = 0; i < length; i++) {
                        if (result.data[i].name.toLowerCase() == "examnotes") {
                            program.examNotes = result.data[i].value;
                            $(".examnotes").html(program.examNotes);
                        }
                    }
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
    console.log(program.role);
    if (program.role == "Admin") {
        require('../js/common.js');
    } else if (program.role == "Student") {
        require('../js/nav.js');
    }
    program.getWebInfo();
    $("#inlineCheckbox").click(function () {
        console.log(111);
        if ($("#inlineCheckbox").is(":checked") == false) { // 未勾选则弹出警示框
            $('.button').attr("disabled", "disabled");
        } else {
            $('.button').removeAttr("disabled");
        }
    });
    $(".button").click(function () {
        window.location.href = "contents.html?" + "&eId=" + parm["eId"];
    });
});