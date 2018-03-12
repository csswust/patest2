define(function(require, exports, module) {
    require('jquery');
    require('jCookie');
    require('jSession');
	require('../js/common.js');
	require('paginator');
	require('bootstrap');

	$(".examRoom").next(".treeview-menu").toggle("slow");
	$(".examRoom").addClass("leftActive");
	$(".examination").css("color", "white");
    $(".online").addClass("active");
    
	var template = require('artTemplate');
	var pubMeth = require('../js/public.js');

	var program = {
		page : "1",
		examId : '',
		userId : '',
		username : '',
		count:'',
		ohtml:'',
		userArr:[],
		userProfileArr : [],
		listuserip : [],
		selectOnline : function() {
			console.log(program.examId);
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../user/selectOnlineUserByExamId",
				dataType : 'json',
				async : false,
				data : {
					page : program.page,
					rows : pubMeth.rowsnum,
					examId : program.examId
				},
				success : function(result) {
					console.log(result);
					program.userProfileArr = result.userProfileList;
					program.userArr = result.userList;
					program.listuserip = result.listuserReleaseLock;
					program.count = result.realNumber;
					program.showOnline();
					$("#listInfo").empty();
					$("#listInfo").append(program.ohtml);
				}
			});
		},
		showOnline:function(){
			var length = program.userArr.length;
			program.ohtml = '';
			for(var i = 0;i < length;i++){
				var order = i + 1;
				program.ohtml += '<tr>'
				    +'<td>' + order + '</td>'
					+'<td>' + program.userProfileArr[i].studentNumber + '</td>'
					+'<td>' + program.userArr[i].username + '</td>'
					+'<td>' + program.userProfileArr[i].realName + '</td>'
					+'<td>' + program.userProfileArr[i].className + '</td>'
					+'<td>' + program.userArr[i].classroom + '</td>'
					+'<td>' + program.listuserip[i].loginIp + '</td>'
					+'</tr>';		
			}
		},
	};
	pubMeth.getRowsnum("rowsnum");
	var parm = pubMeth.getQueryObject();
	program.examId = parm["id"];
	program.selectOnline();
	
	$(".gradePrint").click(function() {
//		program.selectGradeByExamId();
		window.location.href="../exam/selectGradeByExamId?examId=" + program.examId;
	});
	$(".codePrint").click(function() {
		window.location.href="../exam/selectCodeByExamId?examId=" + program.examId;
	});
//	program.getSubmitInfo();
	
	if(program.count > 0){
		$(".countnum").html(program.count);
	 	$.jqPaginator('#pagination', {	 		
	    	totalCounts : program.count,
	        visiblePages: 5,
	        currentPage: 1,
	        pageSize: parseInt(pubMeth.rowsnum),
	        first: '<li class="first"><a href="javascript:;">首页</a></li>',
	        last: '<li class="last"><a href="javascript:;">尾页</a></li>',
	        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
	        onPageChange: function (num, type) {
	      	if(type == 'init') {return;}
	      		program.page = num;
				program.selectOnline();
	        }
        });
	} else {
		$(".pagenum").css("display","none");
	}

});