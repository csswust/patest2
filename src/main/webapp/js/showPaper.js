$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".paperInfo").css("color", "white");
var program = {
    exaPapId: null,
    getPerPaper: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../examPaper/selectPaperById",
            dataType: 'json',
            async: false,
            data: {
                exaPapId: program.exaPapId
            },
            success: function (result) {
                console.log(result);
                $(".examName").text(result.examInfo.title);
                $(".userName").text(result.userInfo.username);
                $(".totalNum").text(result.examPaper.score);
                $(".acNum").text(result.examPaper.acedCount);
                $(".time").text(result.examPaper.usedTime);
                $(".realName").text(result.userProfile.realName);
                $(".studentNumber").text(result.userProfile.studentNumber);
                var render = template("getcontent", result);
                $("#paperTempla").html(render);
            }
        });
    }
};
var parm = pubMeth.getQueryObject();
program.exaPapId = parm["exaPapId"];
program.getPerPaper();