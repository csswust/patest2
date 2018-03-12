define(function (require, exports, module) {
	    require('jquery');
	    require('jCookie');
		require('../js/commonet.js');
		require('fileupload');
		require('bootstrap');
		$(".applyexam").addClass("onet");	

	 var pubMeth = require('../js/public.js');
	 var program ={
	    applyid:'',
	    testName:'',
	    peopleNum:'',
	    applicant:'',
	    phone:'',
	    isbank:'',
	    isPass:'',
	 	startTime :'',
	 	endTime :'',
	 	sysuserid:'',
	 	//添加一场申请
	 	insertApplyExam:function(){
			   $.ajax({
					type : "post",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../applyExam/insertApplyExam",	
					dataType : "json",
					async : false,
					data:{
						examName:program.testName,
						peopleNumber:program.peopleNum,
						applicant:program.applicant,
						phone:program.phone,
						isProv:program.isbank,
						startTime:program.startTime,
						endTime:program.endTime,
						isPass:'0',
						sysusrId:program.sysuserid,
					},
					success:function(result){
						console.log(result);
						program.applyid = result.status;
						if(result.status > 0){
							pubMeth.alertInfo("alert-success","保存成功");
							window.location.href = "applywait.html?applyid=" + program.applyid;
						}
						else{
							pubMeth.alertInfo("alert-warning","保存失败！");
						}
					},
					error:function(){
						  pubMeth.alertInfo("alert-danger","请求错误");
					}
         	});
	 	},	 	
	 	//查询申请
	 	selectInfo:function(){
	 		$.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../applyExam/selectById",		
					dataType : 'json',
					async : false,
					data:{
						applyExamId:program.applyid,
					},
					success:function(result){
						console.log(result);
						program.testName = result.data.examName;
						program.peopleNum = result.data.peopleNumber;
						program.applicant = result.data.applicant;
						program.phone = result.data.phone;
						program.isbank = result.data.isProv;
						program.startTime = result.data.startTime;
						program.endTime = result.data.endTime;
					}
		});
	 	},
	 	getTop : function() {
			var radios = $('input[name="status"]');
			 for(var i = 0;i < radios.length; i++) {	
				 program.isbank = $('input[name="status"]:checked').val();
				 radios[i].onclick = function() {
					 program.isbank = $('input[name="status"]:checked').val();
				 }
			 }
		},
		setValue:function(){
			$(".testname").val(program.testName);
			$(".peoplenum").val(program.peopleNum);
			$(".applypeo").val(program.applicant);
			$(".telephone").val(program.phone);
			$(".startTime").val(program.startTime);
			$(".endTime").val(program.endTime);	
			if(program.isbank == 1){
				$(".yesbank").prop("checked",true);
			}else{
				$(".nobank").prop("checked",true);
			}
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
	program.sysuserid = $.cookie("sysuserId");
	if(par.applyid){
		program.applyid = par.applyid;
		$(".pageName").text("修改申请");
		program.selectInfo();
		program.setValue();
	}
	program.getTop();
	$("#applyexam").click(function(){
		program.testName = $(".testname").val();
		program.peopleNum = $(".peoplenum").val();
		program.applicant = $(".applypeo").val();
		program.phone = $(".telephone").val();
		program.startTime = $(".startTime").val();
		program.endTime = $(".endTime").val();	
		if(program.testName != "" && program.peopleNum != "" &&program.applicant != "" &&
				program.phone != "" && program.isbank !="" && program.startTime != "" && program.endTime !=""){
			if(pubMeth.legTimeRange(program.startTime,program.endTime)){
				program.insertApplyExam();
			}else {
				pubMeth.alertInfo("alert-info","开始日期不能大于结束日期");
			}
			}else{
				pubMeth.alertInfo("alert-info","带*号的为必填");
			}
	});
	
});
