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
    $(".quesList").addClass("active");
    
	var template = require('artTemplate');
	var pubMeth = require('../js/public.js');

	var program = {
		page : "1",
		examId : '',
		userId : '',
		username : '',
		count:'',
		html:'',
		submitInfoList : [],
		userInfoList : [],
		userProfileList : [],
		problemInfoList : [],
      /*  //查询提交
		getSubmitInfo : function() {
			$.ajax({
				type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../submit/selectSubmitInfo",
				dataType : 'json',
				async : false,
				data : {
					"submitInfo.examId" : program.examId,
					"submitInfo.userId" : program.userId,
					usernamen : program.username,
					page : program.page,
					rows : '10'
				},
				success : function(result) {
					console.log(result);
					program.count = result.total;
					program.submitInfoList = result.submitInfoList;
					program.userProfileList = result.userProfileList;
					program.userInfoList = result.userInfoList;
					program.problemInfoList = result.problemInfoList;
					program.showProblem();
					$("#listInfo").empty();						
					$("#listInfo").append(program.html);
				}
			});
		},
		showProblem : function() {
			program.html = '';
			length = program.submitInfoList.length;
			for (var i = 0; i < length; i++) {
				program.html += '<tr><td>' + program.submitInfoList[i].submId + '</td>'
						+ '<td><a  href="#">' + program.problemInfoList[i].title + '</a></td>' + '<td>'
						+ program.userInfoList[i].username + '</td>' + '<td>' + program.userProfileList[i].realName + '</td>'
						+ '<td>' + program.userProfileList[i].className + '</td>' + '<td>' + program.submitInfoList[i].status
						+ '</td>' + '<td>' + program.submitInfoList[i].usedMemory + '</td>' + '<td>'
						+ program.submitInfoList[i].usedTime + '</td>'
						+ '<td><a class="compiler" href="code.html?id='
						+ program.submitInfoList[i].submId + '">'
						+ program.submitInfoList[i].judId + '</a></td>'
						+ '<td>' + program.submitInfoList[i].submTime + '</td>'
						+ '<td>' + program.submitInfoList[i].submIp + '</td>'
						+ '</tr>';
			}
		},*/
	getProblemList : function() {
			$.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url : "../problem/selectMyProblem",
					dataType : 'json',
					async : false,
					data : {
						examId : program.examId
					},
					success : function(result) {
						console.log(result);
						var html = "";
						$('#listInfo').empty();
						for (var i = 0, len = result.problemInfoList.length; i < len; i++) {
							var title = "", level = "",probId ="",acedNum = "",totalSubmit = "",createTime = "";
							if (result.problemInfoList[i] != null) {
								title = result.problemInfoList[i].title;
								level = result.problemInfoList[i].level;
								probId = result.problemInfoList[i].probId;
								acedNum = result.problemInfoList[i].acedNum;
								totalSubmit = result.problemInfoList[i].totalSubmit;
								createTime = result.problemInfoList[i].createTime
							} 
							html += '<tr>' + '<td>' + probId + '</td>'
									+ '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="'+ title+ '"><a href="question.html?id='+probId+'">' + title + '</a></td>';
							if (result.knowledgeList[i] != null) {
								html += '<td>'
										+ result.knowledgeList[i].knowName
										+ '</td>';
							} else
							html += '<td>' + 'null' + '</td>';
							html += '<td>' + level + '</td>' + 
								'<td>' + acedNum +'/' +totalSubmit+'</td>'+
								'<td>'+ createTime +'</td>'+
							'<td>'+ result.countList[i] + '</td>'
									+ '</tr>';
						}
						$('#listInfo').append(html);
					},
					error : function() {
					}
				});
		},
	};
	var parm = pubMeth.getQueryObject();
	program.examId = parm["id"];
	program.getProblemList();
	
	$(".gradePrint").click(function() {
//		program.selectGradeByExamId();
		window.location.href="../exam/selectGradeByExamId?examId=" + program.examId;
	});
	$(".codePrint").click(function() {
		window.location.href="../exam/selectCodeByExamId?examId=" + program.examId;
	});
//	program.getSubmitInfo();
	console.log(program.count);
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
				program.getSubmitInfo();
	        }
        });
	} else {
		$(".pagenum").css("display","none");
	}

});