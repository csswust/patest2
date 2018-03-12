define(function(require, exports, module) {
	require('../js/common.js');
	require('bootstrap');
	require('paginator');
	$(".examRoom").next(".treeview-menu").toggle("slow");
	$(".examRoom").addClass("leftActive");
	$(".examination").css("color", "white");
	var template = require('artTemplate');
	var pubMeth = require('../js/public.js');

	var program = {
		examId : '',
		userId : [],
		probIds : '',
		probIdArr : [],
		index : 0,
		page : '1',
		anthercode : '',
		codeList : [],
		realName : '',
		userName : '',
		usernameArr : [],
		username : '',
		similaryList : [],
		similaryTotal : 0,
		score : '',
		rscore : '',
		html : '',
		count : '',
		Mycode : '',
		paprId : '',
		probId : '',
		prproId : '',
     	probIds : [],
		prproIds : [],
		markPaper : function() {
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../exam/selectExamPaper",
				dataType : 'json',
				async : false,
				data : {
					examId : program.examId
				},
				success : function(result) {
					console.log(result);
					$(".pageName").text(result.data[0].examName);
					program.count = result.total;
					for (var i = 0; i < result.data.length; i++) {
						program.userId[i] = result.data[i].userId;
					}
				}
			});
		},
		insertSimilarity : function(temp) {
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../submit/insertSubmitSimilarity",
				dataType : 'json',
				async : false,
				data : {
					examIds : program.examId,
					probIds : program.codeList[temp].probId
				},
				success : function(result) {
					console.log(result);
					if(result.status == 2){
						$("#tip").modal({
			 				backdrop : 'static'
			 			});
					}
				},
				error : function() {
					alert("相似度检验失败！");
				}
			});
		},
		getProbIds : function() {
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../exam/selectProbIdsByExamId",
				dataType : 'json',
				async : false,
				data : {
					examId : program.examId,
				},
				success : function(result) {
					console.log(result);
					program.probIds = result.data				
				},
				error : function() {
				}
			});
		},
		queryCode : function() {
//			console.log(program.userId[program.index]);
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../exam/selectPaperProplem",
				dataType : 'json',
				async : false,
				data : {
					examId : program.examId,
					userId : program.userId[program.index]
				},
				success : function(result) {
					console.log(result);
					var length = result.listMapProblem.length;
					program.paprId = result.listMapProblem[0].paprId;
					/*program.probId = result.listMapProblem[length - 1].paprId;
					program.prproId = result.listMapProblem[length - 1].prproId;*/
					program.codeList = result.listMapProblem;
					program.score = result.mapPaper.score;
					program.userName = result.mapPaper.userName;
					program.realName = result.mapPaper.realName;
					program.showCode();
					$(".nav-tabs-custom").empty();
					$(".nav-tabs-custom").append(program.html);
				}
			});
		},
		showCode : function() {
			program.html = "";
			var tmp = 0;
			var length = program.codeList.length;
			var flag = length;
			var tab_nav = "", content = "", liActive, divAcitve;
			for (var i = 0; i < length; i++) {
				if (flag == 1) {
					liActive = '<li id="'+tmp+'-'+tmp+'"  class="active">';
					divAcitve = '<div id="Q_' + flag
							+ '" class="tab-pane active">';
				} else {
					liActive = '<li id="'+tmp+'-'+tmp+'">';
					divAcitve = '<div id="Q_' + flag + '" class="tab-pane">';
				}
				tab_nav += '' + liActive + '<a href="#Q_' + flag
						+ '" data-toggle="tab" onclick="aClick()">Q' + flag + '：'
						+ program.codeList[i].score + '</a></li>';
				program.probIds[i] = program.codeList[i].probId;
				program.prproIds[i] = program.codeList[i].prproId;
				content += ''
						+ divAcitve
						+ '<h2 style="text-align:center;">'
						+ program.codeList[i].problemName
						+ '</h2>'
						+ '<div style="margin:0 auto;width:800px;"><ul class="nav-pills nav-justified" style="text-align:center;color:#3c8dbc;">'
						+ '<li>提交时间:' + program.codeList[i].lastSubmitTime
						+ '</li><li>时间:' + program.codeList[i].usedTime
						+ '</li>' + '<li>状态:' + program.codeList[i].status
						+ '</li><li>分数:' + program.codeList[i].score
						+ '</li></ul></div>' +'<div class="allcon"><div class="changed"><pre class="prettyprint linenums pre-scrollable">'
						+ program.codeList[i].subSource + '</pre></div><div class="addcss"><pre class="addchanged"></pre></div></div></div>';
				flag--;
				tmp++;
			}
			program.html += '<ul class="nav nav-tabs pull-right">'
					+ tab_nav
					+ '<li class="pull-left">'
					+ '<span class="glyphicon glyphicon-bullhorn" aria-hidden="true"></span>'
					+ '' + program.userName + '(' + program.realName
					+ ')</li></ul>' + '<div class="tab-content">' + content
					+ '</div>';
			$(".pull-right").find("li").eq(5).addClass("active");
		},
		selectSimilary : function(temp) {
			console.log(program.codeList[temp].probId);			
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../submit/selectAnotherSimiarity",
				dataType : 'json',
				async : false,
				data : {
					page : program.page,
					rows : pubMeth.rowsnum,
					examId : program.examId,
					userId : program.userId[program.index],
					probId : program.codeList[temp].probId
					
				},
				success : function(result) {	
					console.log(result);
					program.similaryList = result.list;
					program.similaryTotal = result.total;
					console.log(program.similaryTotal);
					if(program.similaryTotal > 0){
						if ($("#problem_similarity").css("display") == 'none') {
							$("#problem_similarity").css("display", "block");
						}
						program.showSimilaryInfo();
						$("#problemlistInfo").empty();
						$("#problemlistInfo").append(program.html);
					}				
				},
				error : function(){
					alert("相似度检验失败！");
				}
			});
		},
		showMyCode : function(temp) {
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../submit/showMyCode",
				dataType : 'json',
				async : false,
				data : {
					examId:program.examId,
					username:program.username,
					probId:program.codeList[temp].probId,				
				},
				success : function(result) {	
					console.log(result);
				    program.Mycode = result.submitInfo.submSource;
				    if(result.status == 1){
				    	$(".allcon").css({
				    		"width":"100%",
				    		"margin":"0 auto",
				    		"overflow":"auto",
				    		 "zoom":" 1",
				    	});
				    	$(".changed").addClass("leftdiv");
				  	    $(".addchanged").addClass("prettyprint linenums pre-scrollable");
				  	    $(".addchanged").empty();
					  	  if($(".addchanged").css("display") == 'none') {
					  		$(".addchanged").css("display","block");
					  	}
				    	$(".addchanged").append(program.Mycode);
				    	$(".addcss").addClass("rightdiv");
				    	$(".addcss pre").css({"padding":"9.5px"});
				    }
				},
				error : function(){
					
				}
			});
		},
		showSimilaryInfo : function() {
			var order = 1;
//			console.log(program.similaryList.length);
			var flag = 0;
			program.html = "";
			for (var i = 0; i < program.similaryList.length; i++) {
				program.html += '<tr><td>'+order+'</td>'
						+ '<td>'+program.similaryList[i].userName+'</td>'
						+ '<td>'+ program.similaryList[i].realName+'</td>'
						+ '<td>'+ program.similaryList[i].ip+'</td>'
						+ '<td>'+ program.similaryList[i].similarity+ '</td>'
						+ '<td class="search" id="'+flag+'-'+flag+'"><button class="btn btn-info resource">source</button></td>'
						+ '</tr>';
				program.usernameArr[i] = program.similaryList[i].userName;
				order++;
				flag++;
			}	
		},
		updateScore:function(){
			$.ajax({
     			type : "get",
     			content : "application/x-www-form-urlencoded;charset=UTF-8",
     			url:"../paperProblem/updatePaperProblem",
     			dataType : 'json',
     			async : false,
     			data:{
     				paprId:program.paprId,
     				probId:program.probId,
     				prproId:program.prproId,
     				score:program.rscore
     			},
     			success:function(result){ 
     				console.log(result);
     				if(result.status == 1){
     					program.queryCode();
     				}else {
     					alert("提交失败");
     				}
     			},
     			error:function(result){
     				alert("请求失败");
     			}
			});
		},
		lastInfo : function() {
			program.index--;
			if (program.index < 0) {
				alert("已经是第一个了！");
				return false;
			}
			program.queryCode();
		},
		nextInfo : function() {
			program.index++;
			if (program.index > program.count) {
				alert("已经是最后一个了！");
				return false;
			}
			program.queryCode();
		}
	};
	pubMeth.getRowsnum("rowsnum");
	var par = pubMeth.getQueryObject();
	program.examId = par.id;
	program.getProbIds();
	program.markPaper();
	program.queryCode();
	$(".similarity").click(function() {
		var str = $(".tab-pane.active").attr("id").split("_");
		var number = program.codeList.length-parseInt(str[str.length-1]);
		program.insertSimilarity(number);
		program.selectSimilary(number);
		if(program.similaryTotal > 0){
			$.jqPaginator('#pagination', {
		    	totalCounts : program.similaryTotal,
		        visiblePages: 5,
		        currentPage: 1,
		        pageSize:  parseInt(pubMeth.rowsnum),
		        first: '<li class="first"><a href="javascript:;">首页</a></li>',
		        last: '<li class="last"><a href="javascript:;">尾页</a></li>',
		        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
		        onPageChange: function (num, type) {
		        	if(type == 'init') {return;}
		        	program.page = num;
		        	console.log(number);
		        	program.selectSimilary(number);
		        }
	       });
		}
		
	});
	$(".nextInfo").click(function() {
		if ($("#problem_similarity").css("display") == 'block') {
			$("#problem_similarity").css("display", "none");
		}
		program.nextInfo();
	});
	$(".lastInfo").click(function() {
		if ($("#problem_similarity").css("display") == 'block') {
			$("#problem_similarity").css("display", "none");
		}
		program.lastInfo();
	});
	
	$(".submit").click(function(){
		program.rscore = $("#rscore").val();
		var index = $(".nav-tabs-custom ul li.active").attr("id").split("-")[0];
		program.probId = program.probIds[index];
		program.prproId = program.prproIds[index];
		console.log(program.probId);
		console.log(program.prproId);
		console.log(program.probIds);
		console.log(program.prproIds);
		program.updateScore();
	});
 	$("#problemlistInfo").on('click','.search',function(){
 		var str = $(".tab-pane.active").attr("id").split("_");
		var number = program.codeList.length-parseInt(str[str.length-1]);
 		var index = this.id.split("-")[0];
        program.username = 	program.usernameArr[index];
        program.showMyCode(number);     
 	});	 
});
function aClick(){
	if($(".addchanged").css("display") == 'block') {
		$(".addchanged").css("display","none");
	}
	$(".changed").removeClass("leftdiv");
	if ($("#problem_similarity").css("display") == 'block') {
		$("#problem_similarity").css("display", "none");
	}
};