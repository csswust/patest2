define(function (require, exports, module) {
	require('jquery');
	require('../js/common.js');
	require('bootstrap');
	require('paginator');
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    $(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".similarityInfo").css("color","white");
	var program = {
		count:'',
		page:"1",
		stunum : '',
		examId : '',
		seardu : '',
		content : '',
		html : '',
/*		getSimilarityInfo:function(){
			$.ajax({
     					type : "post",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../submit/selectSubmitSimilarity",
     					dataType : 'json',
     					async : false,
     					data:{
     						page :program.page,
     						rows :"10",
     					},
     					success:function(result){ 
     						console.log(result);
     						program.count = result.total;
     						var rander = template("getcontent",result);
     						$("#listInfo").empty();
     						$("#listInfo").append(rander);
     					},
     					error:function(){
     						console.log(0);
     					}
     			});
		},*/
		showSimilarity : function(){
			var length = program.content.length;
			program.html="";
			for(var i = 0;i < length;i++){
				for(var j = 0;j < program.content[i].length;j++){
					program.html += '<tr>'
			        +'<td><input type="checkbox" name="title"/></td>'
					+'<td>' + program.content[i][j].submsId + '</td>'
					+'<td>' + program.content[i][j].problemTitle1 +'+' + program.content[i][j].userName1 + '</td>'
					+'<td>' + program.content[i][j].problemTitle2+ '+' + program.content[i][j].userName2 + '</td>'
					+'<td>' + program.content[i][j].similarity+ '</td>'
					+'</tr>';
				}
			}
		},
		searchbyid : function(){
			$.ajax({
					type : "post",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../submit/selectSubmitSimilarityByStudentNum",
					dataType : 'json',
					async : false,
					data:{
						username : program.stunum ,
					    standerValue : program.seardu,
						examId : program.examId,
					},
					success:function(result){ 
						console.log(result.data.result);
						program.count = result.data.total;
						program.content = result.data.result;
						program.showSimilarity();
						$("#listInfo").html("");
						$("#listInfo").append(program.html);
					},
					error:function(){
						console.log(0);
					}
			});
		},
	};
	 pubMeth.getRowsnum("rowsnum");
	 var par = pubMeth.getQueryObject();	 
	 program.examId = par.id;
	 program.searchbyid();
	$(".search").click(function(){
		program.stunum = 	$(".searTitle").val();
		program.seardu = $(".seardu ").val();
		program.searchbyid();
	});
	
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
		     	program.searchbyid();
	        }
      });
	} else {
		 $(".pagenum").css("display","none");
	}
});