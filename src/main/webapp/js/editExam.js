define(function (require, exports, module) {
	    require('jquery');
	    require('jCookie');
		require('../js/common.js');
		require('fileupload');
		require('bootstrap');
	$(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".examList").css("color","white");
	$(".stinfo").addClass("on");	
	
	 var flag = 0;
	 var pubMeth = require('../js/public.js');
	 var program ={
	 	title:"",
	 	startTime :"",
	 	endTime :'',
	 	description :'',
	 	examId :'',
	 	count:'',
	    expmArr : "",
	    expmIds :[],
	    examip:'',
	 	//增加一场考试
	 	addExam:function(){
	 		console.log(program.examip);
	 	     $.ajax({
     					type : "post",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../exam/insertExam",		
     					dataType : "json",
     					async : false,
     					data:{
     						title : program.title,
     						startTime : program.startTime,
     						endTime :program.endTime,
     						description : program.description,  
     						allowIp : program.examip,
     					},
     					success:function(result){
     						console.log(result);
     						program.examId = result.examId;    						   						
     						if(result.status=='1'){
     							pubMeth.alertInfo("alert-success","保存成功");
     							window.location.href="editBank.html?examId="+program.examId;
     						}else {
     							pubMeth.alertInfo("alert-warning","保存失败");
     						}
     					/*	$('#import').modal({
								backdrop : 'static'
							});
							$(".comImport").click(function() {
								program.importList();
							});*/
     						/*if(id=="justSave"){
     							pubMeth.alertInfo("alert-success","添加成功");
     							window.location.href="exam.html";
     						}else if(id=="saveEdtor"){
     							pubMeth.alertInfo("alert-success","添加成功");
     						}else if(id=="saveAdd"){
     							 pubMeth.alertInfo("alert-success","添加成功");
     							 location.reload(false);
     						}else if(id=="justUp"){
     							pubMeth.alertInfo("alert-success","添加成功");
     						}
     						else{
     							pubMeth.alertInfo("alert-warning","添加失败！");
     						}*/
     					 
     					},
     					error:function(){
     						  pubMeth.alertInfo("alert-danger","请求错误");
     					}
	 	});
	 	},
	 	//更新展示考试
	 	updateExam:function(){
			   $.ajax({
     					type : "POST",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../exam/updateExam",	
     					dataType : "json",
     					async : false,
     					data:{
     						examId:program.examId,
     						title : program.title,
     						startTime : program.startTime,
     						endTime :program.endTime,
     						description : program.description,
     						allowIp : program.examip,
     					},
     					success:function(result){
     						console.log(result);
     						if(result.status =='1' ){
     							pubMeth.alertInfo("alert-success","修改成功");
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
		//获得考试信息展示
		getExamInfoById:function(){
				$.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../exam/selectExam",		
     					dataType : 'json',
     					async : false,
     					data:{
     						examId:program.examId
     					},
     					success:function(result){
     						console.log(result);
     						program.title = result.data[0].title;
     						program.startTime = result.data[0].startTime;
     						program.endTime = result.data[0].endTime;
     						program.description = result.data[0].description;
     						program.examip = result.data[0].allowIp;
     					}
				});
		},
	    //日期的设置
		legTimeRange:function(){
			var stadate = program.startTime.split(" ");
			var enddate = program.endTime.split(" ");
			var stayear =stadate[0].split('-');
			var endyear = enddate[0].split('-');
			var stadate = stadate[1].split(':');
			var enddate = enddate[1].split(':');
			
			//年份更大，同年月份更大，同年同月日更大，同年同月同日时更大，同年同月同日同时分更大，同年同月同日同时同分秒更大
			if(stayear[0]>endyear[0] || stayear[0]== endyear[0]&&stayear[1]>endyear[1] ||
			stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]>endyear[2] ||
			stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]==endyear[2]&&stadate[0]>enddate[0]||
			stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]==endyear[2]&&stadate[0]==enddate[0]&&stadate[1]>enddate[1]||
			stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]==endyear[2]&&stadate[0]==enddate[0]&&stadate[1]==enddate[1]&&stadate[2]>enddate[2]){
				return false;
			}
			return true;
		},
		setValue:function(){
			$(".examTitle").val(program.title);
			 $(".description").val(program.description);
			 $(".startTime").val(program.startTime);
			 $(".endTime").val(program.endTime);
			 $(".examip").val(program.examip);
		},
	 };
	 function getUrlParam(name){
		 var reg = new RegExp();
	 }
	require('datetimepicker');	
	var date = new Date();
	 $(".form_datetime").datetimepicker({
	 		format: 'yyyy-mm-dd hh:ii:ss',
	 		startDate: date
	 });
	pubMeth.serCourse();
	var par = pubMeth.getQueryObject();
	if(par.examId){
		$(".pageName").text("添加考试");
		program.examId = par.examId;
		program.getExamInfoById();
		program.setValue();
		$(".downInfo").click(function(){
			window.location.href = "editBank.html?examId="+program.examId;
		});
	}
	//编辑考试
	if(par.Id){
		$(".pageName").text("修改考试");
		program.examId = par.Id;
		program.getExamInfoById();
		program.setValue();	
		$(".downInfo").click(function(){
			window.location.href = "editBank.html?Id="+program.examId;
		});
	}
		$(".downInfo").click(function(){	 		
	 		program.title = $(".examTitle").val();
	 		program.description = $(".description").val();
	 		program.startTime = $(".startTime").val();
			program.endTime = $(".endTime").val();	
			program.examip = $(".examip").val();
			if(program.examId==""){
				if(program.title!=""&& program.startTime!=""&&program.endTime!="" && program.examip != ""){
					if(pubMeth.legTimeRange( program.startTime,program.endTime)){
						 program.addExam();						 
					}else{
						pubMeth.alertInfo("alert-info","开始日期不能大于结束日期");
					}
				}else{				
					pubMeth.alertInfo("alert-info","带*号的为必填");
				} 
			 }else if(par.Id){
			 	if(program.title!=""&& program.startTime!=""&&program.endTime!=""){
					if(pubMeth.legTimeRange(program.startTime,program.endTime)){
						 program.updateExam();
					}else{
						pubMeth.alertInfo("alert-info","开始日期不能大于结束日期");
					}
				}else{		
					pubMeth.alertInfo("alert-info","带*号的为必填");
				    } 				 
			}
	 	});
		
		//列表导航
});
