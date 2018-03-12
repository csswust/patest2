define(function (require, exports, module) {
	    require('jquery');
	    require('jCookie');
		require('../js/commonet.js');
		require('fileupload');
		require('bootstrap');	
	$(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".examList").css("color","white");
	$(".etbank").addClass("on");	
	$(".mytest").addClass("onet");	
	 var flag = 0;
	 var bankcon = $.cookie("bank");
	 var pubMeth = require('../js/public.js');
	 var program ={
    	html:"",
    	probArr:[],
	 	examId:'',
	 	expmId:"",	
	    tempProbIds:[],
	    tempProbId:'',		
		showproinfo:function(){
			var length = program.probArr.length;
			var order = 1;
			if(program.probArr.length != 0){
				var banklist ='<tr>'
					 + '<th>Order</th>'
		             + '<th>ID</th>' 
		             + '<th>Title</th>'
		             + '<th>Difficulty</th>'
		             + '<th>Topic</th>'
		             + '<th>Handle</th>'
		             + '<tr>';	
			    $("#listhead").html(banklist);
			}
			program.html="";
			for(var i = 0;i<length;i++){				
				program.html += '<tr class="'+flag+'-'+flag+'">'
				        +'<td>' + order +'</td>'
						+'<td>' + program.probArr[i].probId + '</td>'
						+'<td><a href="question.html?id='+program.probArr[i].probId+'"  class="title">' + program.probArr[i].title + '</a></td>'
						+'<td>' + program.probArr[i].level + '</td>'
						+'<td>' + program.probArr[i].knowName + '</td>'
						+'<td class="deletepro" id="'+flag+'-'+flag+'"><a><span class="glyphicon glyphicon-remove-circle"  ></span></a></td>'
						+'</tr>';
				program.tempProbIds[i] = program.probArr[i].tempProbId;
				order++;
				flag++;
			}
		},		
		//通过考试Id来展示本场考试
		selectByExamId:function(){
			console.log(program.examId);
			$.ajax({
			    type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url:"../exam/selectByExamId",		
				dataType : 'json',
				async : false,
				data:{
					examId:program.examId	
				},
				success:function(result){
					console.log(result.data);
					program.probArr = result.data;
                    program.showproinfo();
                    $("#listInfo").html("");	
					$("#listInfo").append(program.html);								
				},
				error:function(){
					pubMeth.alertInfo("alert-success","请求错误");
				}
		});
		},
		//通过问题Id来删除题目
		deleteTempProblemById:function(){
				$.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../exam/deleteTempProblemById",		
					dataType : 'json',
					async : false,
					data:{
						tempProbId:program.tempProbId,
					},
					success:function(result){
						console.log(result);
						if(result.status == 1){
							pubMeth.alertInfo("alert-success","删除成功");
							flag = 0;
							program.selectByExamId();
						} else {
							pubMeth.alertInfo("alert-danger","删除失败")
						}						
					},
					error:function(){
						pubMeth.alertInfo("alert-danger","请求失败");
					}
				});	
		},
	 };	 	 
	 //解析url
	 pubMeth.serCourse();
	 var par = pubMeth.getQueryObject();	 
        if(par.examId){
        	$(".pageName").text("添加考试");
        	program.examId = par.examId; 
        	console.log(program.examId);
        	program.selectByExamId();
        }
	    //修改考试
		if(par.Id){
			$(".pageName").text("修改考试");
			program.examId = par.Id;	
			program.selectByExamId();
		}
	 	$(".localBank").click(function(){
	 		if(par.examId){
	 			window.location.href = "testbank.html?examId="+program.examId;
	 		} else if(par.Id){
	 			window.location.href = "testbank.html?Id="+program.examId;
	 		}
	 		
	 	});	 	
		$(".upBank").click(function(){
			if(par.examId){
				window.location.href = "addExam.html?examId="+program.examId;
			}else if(par.Id){
				window.location.href = "addExam.html?Id="+par.Id;
			}			
		});
	 	$(".downBank").click(function(){
	 		if(par.examId){
	 			window.location.href = "addParm.html?examId="+program.examId;
	 		} else if(par.Id){
	 			window.location.href = "addParm.html?Id="+par.Id;
	 		}		
	 	});	
	 	$("#listInfo").on('click','.deletepro',function(){
	 		flag--;
	 		var banklength = program.tempProbIds.length;
	 		var index = this.id.split("-")[0];
	 		if(parseInt(index) + 1 > banklength){
	 			$("."+this.id).remove();	 			
	 		}else{
	 			if(confirm("确定要删除该题吗？")){	 			
	 				var index = this.id.split("-")[0];
			 		program.tempProbId = program.tempProbIds[index];
			 		program.deleteTempProblemById();
		 		}
	 		}	 		
	 	});	 		 	
});
