define(function (require, exports, module) {
    require('jquery');
    require('jCookie');
	require('../js/common.js');
	require('bootstrap');
		
	$(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".examList").css("color","white");
	$(".sttemplate").addClass("on");
	
	 var flag = 0;
	 var opreation = "";
	 var savetip = "";
	 var pubMeth = require('../js/public.js');
	 var program ={
	 	examId :'',
	 	kown:'',
	 	score:'',
	 	level:'',
	 	count:'',
	 	kownArr : '',
		levelArr:'',
	    scoreArr  : '',
	    expmArr : '',
	    expmIds :[],
	    expmId:'',
	    course:[],
 	    courseName:[],
 	    sumList :[],
 	    knowName:'',
	    //添加试卷参数
		addTemplate:function(){
	 		$("#queTempla").append('<tr class='+flag+'-'+flag+'>' +
	 				'<td>'+ parseInt(flag + 1) +'</td>' +
	 				'<td><select class="form-control  courseName" id="courseName-'+flag+'"><option>课程</option></select></td>' +
	 				'<td><select class="form-control iknowName courseName-'+flag+'"><option>知识点</option></select></td>' +
	 				'<td><select class="form-control level level-'+flag+'"><option>难度</option><option value="0">容易</option><option value="1">中等</option><option value="2">困难</option></select></td>' +
	 				'<td><input class="form-control score score-'+flag+'"  type="text" /></td>' +
	 				'<td><span class="totalpro  total-'+flag+'"></span></td>' +
	 				'<td><span class="glyphicon glyphicon-remove deleteTemp" aria-hidden="true" id="'+flag+'-'+flag+'"></span></td>' +
	 				'<td>'+opreation+'</td></tr>');
	 	},
	 	//插入考试参数
	 	insertExamParam:function(){
	 	     $.ajax({
 					type : "post",
 					content : "application/x-www-form-urlencoded;charset=UTF-8",
 					url:"../examParam/insertExamParam",		
 					dataType : "json",
 					async : false,
 					data:{
 						examId : program.examId,
 						knowIds : program.kownArr,
 						levels : program.levelArr,
 						scores : program.scoreArr,
 					},
 					success:function(result){
 						console.log(result);
 						if(result.status=="1"){ 
 							pubMeth.alertInfo("alert-success","添加成功！");
 							if(par.examId){
 								window.location.href = 'editUplist.html?examId='+program.examId;  							
 							}else if(par.Id){    								
 								$(".saveTemp").css("display","none");
 							}		
 						}else{
 							pubMeth.alertInfo("alert-warning","添加失败！");
 						}
 					},
 					error:function(){
 						  pubMeth.alertInfo("alert-danger","请求错误");
 					}
 	      });
	 	},
	 	updateExamParam:function(){
			   $.ajax({
					type : "POST",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../examParam/updateExamParam",	
					dataType : "json",
					async : false,
					data:{
						examId:program.examId,
						knowIds:program.kownArr,
						levels:program.levelArr,
						scores: program.scoreArr,
					},
					success:function(result){
							if(result.status=='1'){
							pubMeth.alertInfo("alert-success","修改成功");
							window.location.href = 'editUplist.html?Id='+program.examId;  							
						}
						else{
							pubMeth.alertInfo("alert-warning","修改失败！");
						}
					},
					error:function(){
						  pubMeth.alertInfo("alert-danger","请求错误");
					}
	           });
	 	},
	 	//删除试卷参数
	 	delQuesTemp:function(){
	 		   $.ajax({
     					type : "POST",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../examParam/deleteExamParam",	
     					dataType : "json",
     					async : false,
     					data:{
     						expmIds : program.expmId
     					},
     					success:function(result){
     						console.log(result);
     						if(result.status == 1){
     							pubMeth.alertInfo("alert-success","删除成功");
     						}
     					},
     					error:function(){
     						  pubMeth.alertInfo("alert-danger","请求错误");
     					}
	 	      });
	 	},

		selectNum:function(index){
			 program.know = $(".courseName-"+index+" option:selected").val();
			 program.level =  $(".level-"+index+" option:selected").val();
			 if(program.level =="难度"){
			 	program.level = "";
			 }
			 if(program.know =="知识点"){
			 	program.know = "";
			 }
			 $.ajax({
 					type : "get",
 					content : "application/x-www-form-urlencoded;charset=UTF-8",
 					url:"../exam/selectProblemTotal",		
 					dataType : 'json',
 					async : false,
 					data:{
 						knowId:program.know,
 						level: program.level,
 						examId:program.examId,
 					},
 					success:function(result){ 
 						console.log(result);
 					     program.count = result.total;
 					},error:function(){
 					   pubMeth.alertInfo("alert-danger","请求错误");
 					}
 				});		 
 			$(".total-"+index).text(program.count);
		},
		//获得考试信息
		getExamInfoById:function(){
				$.ajax({
     					type : "post",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../exam/selectExam",		
     					dataType : 'json',
     					async : false,
     					data:{
     						examId:program.examId
     					},
     					success:function(result){
     						console.log(result);
     						var length = result.paramTotal;
     						for(var i = 0; i < length;i++){
     							var id = "courseName-"+i;
     							program.addTemplate();
     							$("#"+id).html("<option>课程</option>");
     							for(var k = 0; k < program.courseName.length;k++){
     								$("#"+id).append("<option value="+program.courseName[k].knowId+">"+program.courseName[k].knowName+"</option>");
     							}
     							flag++;
     							var parentId = result.KnowledgeData[i].parentId;
     							$("#"+id+" option[value='"+parentId+"']").attr("selected",true);
     							for(var j = 0; j < program.course.length ; j++){//知识点
									if(program.course[j].parentId == parentId){
										$(".iknowName").append("<option value="+program.course[j].knowId+">"+program.course[j].knowName+"</option>");
									}
								}
     							$(".courseName-"+i+"  option[value='"+result.KnowledgeData[i].knowId+"']").attr("selected",true);
     							$(".level-"+i+" option[value='"+result.paramData[i].level+"']").attr("selected",true);
     							$(".score-"+i).val(result.paramData[i].score);
     							program.expmIds[i] = result.paramData[i].expmId;
     							program.selectNum(i);   
     						}
     					}
				});
		},
		bserCourse:function(){
			$.ajax({
				type:"get",
				content:"application/x-www-form-urlencoded;charset=UTF-8",
				url:"../exam/selectCouseAndKnowledge",
				dataType : 'json',
				async : false,
				data:{
					examId:program.examId,
				},
				success:function( result ){
					console.log(result);
					var currId = "";
					var flag = 0;
					$(".courseName").empty();
					$(".courseName").append("<option>课程</option>");
					for(var i = 0 ; i < result.courseList.length ; i++){
						if(result.courseList[i].isCourse){
							program.courseName.push(result.courseList[i]);
							$(".courseName").append("<option value="+result.data[i].knowId+">"+result.data[i].knowName+"</option>");
						}else{
							program.course.push(result.data[i]);
						}
					}
					for(var i = 0;i < result.data.length;i++){
						program.course.push(result.data[i]);
					}
					program.onchange();
				}
			});
	},
	onchange:function(){
		$(".knowName").html("");
		var parentId = $(".courseName option:selected").val();
		if(parentId =="课程"){
			$(".knowName").html("<option>知识点</option>");
			return ;
		}
		for(var i = 0; i < program.course.length ; i++){
			if(program.course[i].parentId == parentId){
				$(".knowName").append("<option value="+program.course[i].knowId+">"+program.course[i].knowName+"</option>");
			}
		}
	},
};
	 
	 //获取url
	var par = pubMeth.getQueryObject();
	if(par.examId){
		$(".pageName").text("添加考试");
		program.examId = par.examId;
		program.bserCourse();
		program.getExamInfoById();
	}
	if(par.Id){
		$(".pageName").text("修改考试");
		program.examId = par.Id;
		program.bserCourse();
		program.getExamInfoById();
		
	}
	 $(".courseName").change(function(){
			program.onchange();
		});

	 //添加问题模板
	 $(".addTemplate").click(function(){
	 	if(par.Id){
	 		opreation = '<button type="button" class="btn btn-info saveTemp" id="'+flag+'+'+flag+'">保存</button>';
	 	}
	     program.addTemplate();
	     var length = program.courseName.length;
	     for(var i = 0;i<length;i++)
	     	$("#courseName-"+flag).append("<option value="+program.courseName[i].knowId+">"+program.courseName[i].knowName+"</option>");
			 flag++;
	});
	/*
	 * 保存问题模板数据
	 */
	$("#queTempla").on('click','.saveTemp',function(){
			 var rowIndex = this.id.split("+")[0];
			 var knowId = $(".courseName-"+rowIndex+" option:selected").val();
			 var levelId =  $(".level-"+rowIndex+" option:selected").val();
			 var score = $(".score-"+rowIndex).val();
			 var allscore = 0;
		 	 var allquestion = 0;
			 var num =  /^[0-9]+.?[0-9]*$/;
			 //获取文本框中的内容
			 var kownArr=[],
				levelArr=[],
				scoreArr=[],
				quesArr=[];
			$(".iknowName").each(function(i) {
					if($(this).val()=="知识点"){
				      	kownArr[i] = "";
					}else{
						kownArr[i] = $(this).val();
					}				
			});
			console.log(kownArr);
			$(".level").each(function(i) {
				if($(this).val()=="知识点"){
				      	levelArr[i] = "";
					}else{
						levelArr[i] = $(this).val();
					}	
			});
			$(".score").each(function(i) {
					scoreArr[i] = $(this).val();
					allscore += parseInt(scoreArr[i]);
			});
		
			 allquestion = $("#queTempla tr:last").children("td:first").text();
			 program.kownArr = kownArr.join(',');
			 program.levelArr = levelArr.join(',');
			 program.scoreArr = scoreArr.join(',');
			 program.expmArr = program.expmIds.join(',');
			 if(levelId=="难度"||knowId=="知识点"){
				 pubMeth.alertInfo("alert-info", "请选择难度和知识点！");
			 	return ;
			 }else if(score == ""){
				 pubMeth.alertInfo("alert-info", "请填写分数！");
				 return;
			 }else if(!num.test(allscore)){
				   pubMeth.alertInfo("alert-info","分数请输入数字");
			     return;
			 }else if(program.count == 0){
				  pubMeth.alertInfo("alert-info","问题总量不可为0，请修改参数");
			 }else{
				 program.insertExamParam();
			 }
	});
	/*
	 * 查询问题总量
	 */
	$("#queTempla").delegate('tr','change',function(){
			program.selectNum(this.rowIndex - 2);
			
	});
	//删除问题模板
	$("#queTempla").on('click','.deleteTemp',function(){
		flag--;
		var expmlength = program.expmIds.length;
		var index = this.id.split("-")[0];
		if(parseInt(index) + 1 > expmlength){
			$("."+this.id).remove();
		}else{
			if(confirm("确定要删除该模板吗？")){
			var index = this.id.split("-")[0];
			program.expmId = program.expmIds[index];
			program.delQuesTemp();
			$("."+this.id).remove();
			}
		}
	});
	//改变
	 $("#queTempla").delegate('.courseName','change',function(){
	 		var id = this.id;
	 		if(id!=""){
	 		var classname = "."+id;
	 	    $(classname).html("");
			var parentId = $("#"+id+" option:selected").val();
			if(parentId =="课程"){
				$(classname).html("<option>知识点</option>");
				return ;
			}
			for(var i = 0; i < program.course.length ; i++){
				if(program.course[i].parentId == parentId){
					$(classname).append("<option value="+program.course[i].knowId+">"+program.course[i].knowName+"</option>");
				}
			}
	 	}
	 	});
	 	
	 	//点击上一步返回题库
	 	$(".upParm").click(function(){
	 		if(par.examId){
	 			window.location.href = 'editBank.html?examId='+program.examId;
	 		}else if(par.Id){
	 			window.location.href = 'editBank.html?Id='+program.examId;
	 		}
	 	}); 
	 	//点击下一步到上传学生名单
	 	$(".downParm").click(function(){
	 		var kownArr=[],
			levelArr=[],
			scoreArr=[],
			quesArr=[];	
	 		var allscore = 0;
	 		var allquestion = 0;
		    $(".iknowName").each(function(i) {
				if($(this).val()=="知识点"){
			      	kownArr[i] = "";
				}else{
					kownArr[i] = $(this).val();
				}
				
	    	});
	    	$(".level").each(function(i) {
			if($(this).val()=="知识点"){
			      	levelArr[i] = "";
				}else{
					levelArr[i] = $(this).val();
				}	
		    });
		   $(".score").each(function(i) {
				scoreArr[i] = $(this).val();	
				allscore += parseInt(scoreArr[i]);
	    	});
		    allquestion = $("#queTempla tr:last").children("td:first").text();

	    	 program.kownArr = kownArr.join(',');
	    	 program.levelArr = levelArr.join(',');
		     program.scoreArr = scoreArr.join(',');
		     program.expmArr = program.expmIds.join(',');  
	 		if(par.examId){
				if(program.kownArr !="" && program.levelArr != "" && program.scoreArr !=""){
					var num =  /^[0-9]+.?[0-9]*$/;
					if(!num.test(allscore)){
						pubMeth.alertInfo("alert-info","分数请输入数字");
					}else{
						if(program.count == 0){		
							pubMeth.alertInfo("alert-info","问题总量不可为0，请修改参数");						
						} else {
							$("#modaladdpram").modal({
								backdrop : 'static'							    
							});
							$(".allscore").html(allscore);
							$(".allques").html(allquestion);
							$(".parmadd").click(function(){
								program.insertExamParam();	
							});
						}	
					}												
				} else {
					pubMeth.alertInfo("alert-info","试卷参数的为必填");
				}
			 }else if(par.Id){
				 if(program.kownArr !="" && program.levelArr != "" && program.scoreArr !=""){
					 var num =  /^[0-9]+.?[0-9]*$/;
					 if(!num.test(allscore)){
						 pubMeth.alertInfo("alert-info","分数请输入数字");
					 }else{
						 if(program.count == 0){
							 pubMeth.alertInfo("alert-info","问题总量不可为0，请修改参数");					 
							} else {
								$("#modaladdpram").modal({
									backdrop : 'static'							    
								});
								$(".allscore").html(allscore);
								$(".allques").html(allquestion);
								$(".parmadd").click(function(){
									program.updateExamParam();	
								});
							}	 
					 }				
					} else {
						pubMeth.alertInfo("alert-info","试卷参数的为必填");
					}			 
			}
	 	});
});
