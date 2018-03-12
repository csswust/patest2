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
    $(".rank").addClass("active");
    
	var template = require('artTemplate');
	var pubMeth = require('../js/public.js');

	var program = {
		page : "1",
		examId : '',
		userId : '',
		username : '',
		count:'',
		html:'',
		ohtml:'',
		examNum:'',
		userName:'',
		stuNum:'',
		className:'',
		getScore:'',
		ACnum:'',
		problemTitle:'',
		problemTotal:'',
		submitInfoList : [],
		userInfoList : [],
		userProfileList : [],
		problemInfoList : [],
		oClassNameList : [],
		oipList : [],
		orealNameList : [],
		ostuNum : [],
		ouserNameList : [],
		getRank : function() {
		    $.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url : "../exam/rankingByGrade",
					dataType : 'json',
					async : false,
					data : {
						examId : program.examId,
						page : program.page,
						rows : pubMeth.rowsnum,
					},
					success : function(result) {
						console.log(result);
						program.count = result.total;
						program.problemTotal = result.problemTotal;
						if(program.count <= 0){
							$("#notip").modal('show');
						}else{
							var length = result.paperData.length;
							var titLength = result.problemNameData[0].length;
							var userName = "", stunum = "",titName = "", content = "", realName = "", className = "", score = "", acedCount = "";
							var ranknav = '';
							for (var j = 1; j <= titLength; j++) {
								ranknav += '<th colspan="2">Q' + j + '</th>';
							}
							
							$(".rankInfo").html(
									'<th class="examid">考号</th><th>姓名</th><th class="noid">学号</th><th>班级</th>'
											+ '<th>分数</th><th>AC</th>'
											+ ranknav);
							for (var i = 0; i < result.paperData.length; i++) {
								userName = result.userNameData[i];
								realName = result.realNameData[i];
								className = result.classNameData[i];
								stunum = result.studentList[i];
								if (result.userNameData[i] == null
										|| result.userNameData[i] == "undefined") {
									userName = "";
								}
								if (result.realNameData[i] == null
										|| result.realNameData[i] == "undefined") {
									realName = "";
								}
								if (result.classNameData[i] == null
										|| result.classNameData[i] == "undefined") {
									className = "";
								}
								if(result.studentList[i] == null
										|| result.studentList[i] == "undefined") {
									stunum = "";
								}
								titName = "";
								for (var j = 0; j < titLength; j++) {						
									if(result.problemAcedList[i][j] == 1){
										var pass = '<span class="glyphicon glyphicon-ok alert-success" aria-hidden="true">';
									}else{
										pass = '<span class="glyphicon glyphicon-remove alert-danger" aria-hidden="true"></span>';
									}
									if(result.problemNameData[i][j]	 == null || result.problemNameData[i][j]	 =="undefined"){
										result.problemNameData[i][j]	 = "";
									}
									titName +='<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="'+result.problemNameData[i][j]+'">'
											+ result.problemNameData[i][j]	
									        +'</td>'
									        +'<td>'
									        + pass
									        +'</td>';

								}
								content += '<tr><td>' + userName + '</td><td>'
										+ realName + '</td>' + '<td>'
										+ stunum + '</td>' + '<td>'
										+ className + '</td><td>'
										+ result.paperData[i].score + '</td>'
										+ '<td>'
										+ result.paperData[i].acedCount
										+ '</td>' + titName + '</tr>';

							}
							$("#listInfo").html(content);
						}
					}
				});

		},
	};
	pubMeth.getRowsnum("rowsnum");
	var parm = pubMeth.getQueryObject();
	program.examId = parm["id"];
	program.getRank();
	
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
	 	$.jqPaginator('#paginationGet', {	 		
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
				program.getRank();
	        }
        });
	} else {
		$(".pagenum").css("display","none");
	}

});