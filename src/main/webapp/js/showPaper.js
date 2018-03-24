define(function (require, exports, module) {
    require('jquery');
    require('../js/common.js');
    require('bootstrap');
    $(".examMana").next(".treeview-menu").toggle("slow");
    $(".examMana").addClass("leftActive");
    $(".paperInfo").css("color", "white");
    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    var program = {
        /*eId:"",
         uId:"",*/
        exaPapId: null,
        getPerPaper: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../examPaper/selectPaperById",
                dataType: 'json',
                async: false,
                data: {
                    /*examId : program.eId,
                     userId : program.uId,*/
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
    /*program.eId = parm["eId"];
     program.uId = parm["uId"];*/
    program.exaPapId = parm["exaPapId"];
    program.getPerPaper();
});