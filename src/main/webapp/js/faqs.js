var program = {
    faqs: '',
    index: '',
    role: '',
    getWebInfo: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../siteInfo/selectByName",
            dataType: 'json',
            async: false,
            data: {
                name: "FAQs"
            },
            success: function (result) {
                program.faqs = result.value;
                $(".content").html(program.faqs);
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
            window.location.href = "login.html";
        }
    }
};
program.getCookie("role");
program.getWebInfo();