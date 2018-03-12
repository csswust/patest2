define(function (require, exports, module) {
	require('jquery');
	require('../js/common.js');
	require('bootstrap');
	require('paginator');
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    $(".quesMana").next(".treeview-menu").toggle("slow");
	$(".quesMana").addClass("leftActive");
	$(".submitInfo").css("color","white");
	var program={
			probId:'',
			getProById:function(){
				 $.ajax({
	     					type : "get",
	     					content : "application/x-www-form-urlencoded;charset=UTF-8",
	     					url:"../problem/selectProblem",		
	     					dataType : 'json',
	     					async : false,
	     					data:{
	     						"problemInfo.probId" : program.probId 
	     					},
	     					success:function(result){ 
	     						console.log(result);
	     					    program.course=result.course;
	     					    program.data=result.data;
	     					    program.knowledge=result.knowledge;
							    program.showProblem();
	     					},error:function(){
	     					   pubMeth.alertInfo("alert-info","请求错误");
	     					}
	     				});
			},
			showProblem:function(){
				$("#title").append('<div class="media"><div class="media-body text_center">' +
						'<h3 class="media-heading "> '+program.data[0].title+'</h3></div></div>');
				$("#informdesp").append('<div class="media col-md-3 col-md-offset-3"><div class="media-body"><h5 class="media-heading probtitle-flot know-margin">课程知识点 :&nbsp</h5>' +
						'<h6 class="media-heading probtitle-flot know-margin"> '+program.course[0].knowName+'</h6><p class="media-heading probtitle-flot know-margin">-</p><h6 class="media-heading know-margin">'+program.knowledge[0].knowName+'</h6></div></div>');
				$("#informdesp").append('<div class="media col-md-2 limitMemory"><div class="media-body"><h5 class="media-heading probtitle-flot">limitMemory :&nbsp</h5>' +
						'<h6 class="media-heading "> '+program.data[0].limitMemory+'</h6></div></div>');
				$("#informdesp").append('<div class="media col-md-2"><div class="media-body"><h5 class="media-heading probtitle-flot">limitTime :&nbsp</h5>' +
						'<h6 class="media-heading "> '+program.data[0].limitTime+'</h6></div></div>');
				$("#listInfo").append('<div class="media"><div class="media-body"><h4 class="media-heading">问题描述 :</h4>' +
						'<h5 class="media-heading color">'+program.data[0].description+'</h5></div></div>');
				$("#listInfo").append('<div class="media"><div class="media-body"><h4 class="media-heading">输入描述 :</h4>' +
						'<h5 class="media-heading color">'+program.data[0].inputTip+'</h5></div></div>');
				$("#listInfo").append('<div class="media"><div class="media-body"><h4 class="media-heading">输出描述 :</h4>' +
						'<h5 class="media-heading color">'+program.data[0].outputTip+'</h5></div></div>');
				$("#listInfo").append('<div class="media"><div class="media-body"><h4 class="media-heading">输入样例 :</h4>' +
						'<h5 class="media-heading color">'+program.data[0].inputSample+'</h5></div></div>');
				$("#listInfo").append('<div class="media"><div class="media-body"><h4 class="media-heading">输出样例 :</h4>' +
			            '<h5 class="media-heading color">'+program.data[0].outputSample+'</h5></div></div>');
				$("#submit").click(function(){
					$('#settingdata').modal();
				});
			},
};
	var par = pubMeth.getQueryObject();
	program.probId=par.probId;
	program.getProById();
});
