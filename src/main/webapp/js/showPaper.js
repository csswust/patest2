define(function (require, exports, module) {
	require('jquery');
	require('../js/common.js');
	require('bootstrap');
    $(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".paperInfo").css("color","white");
	var template = require('artTemplate');
	var pubMeth = require('../js/public.js');
	var program = {
		eId:"",
		uId:"",
		getPerPaper : function(){
			$.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../exam/selectPaperProplem",
     					dataType : 'json',
     					async : false,
     					data:{
     						examId : program.eId,
     						userId : program.uId
     					},
     					success:function(result){ 
     						console.log(result);
     						$(".examName").text(result.mapPaper.examName);
     						$(".userName").text(result.mapPaper.userName);
     						$(".totalNum").text(result.mapPaper.score);
     						$(".acNum").text(result.mapPaper.acedCount);
     						$(".time").text(result.mapPaper.usedTime);
     						$(".realName").text(result.mapPaper.realName);
     						$(".studentNumber").text(result.mapPaper.studentNumber);
     						var render = template("getcontent",result);
     						$("#paperTempla").html(render);
     					}
     			});
		}
	};
	var parm = pubMeth.getQueryObject();
	program.eId = parm["eId"];
	program.uId = parm["uId"];
	program.getPerPaper();
});