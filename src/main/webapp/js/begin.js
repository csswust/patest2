define(function (require, exports, module) {
	var nav = require('../js/nav.js');
	var pubMeth = require('../js/public.js');
	var parm = pubMeth.getQueryObject();
	var program = {
		title:'',
		time:''
	};
	program.title = parm["title"];
	program.time = parm["time"];
	$(".beginName").text(program.title);
	var nowTime = nav.getNowTime();
	var startTime = new Date(nowTime);
	var endTime = new Date(parm["time"]);
	var intDiff = parseInt((endTime-startTime)/1000);//倒计时总秒数量
	nav.countDown(intDiff);
	setTimeout(function(){
			if($(".day").text()=="0"&&$(".hours").text()=="0"&&$(".minute").text()=="00"&&$(".second").text()=="00"){
				$(".startAns").removeClass('disabled');
			}
	},2000);
	
});