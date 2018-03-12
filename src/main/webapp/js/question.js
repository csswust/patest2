define(function (require, exports, module) {
	    require('jquery');
	    require('../js/common.js');
	    require('ueditor_config');
		require('ueditor');
		require('fileupload');
		require('bootstrap');
	var pubMeth = require('../js/public.js');
	$(".quesMana").next(".treeview-menu").toggle("slow");
	$(".quesMana").addClass("leftActive");
	$(".problemList").css("color","white");
		var program = {
			date:'',
			title :'',
			probId :'',
			totalSubmit :'',
			acedNum :'',
			level :'',
			limitTime :'',
			limitMemory :'',
			author :'',
			scoreRatio :'',
			codeLimit :'',
			judgeModel :'',
			description :'',
			inputTip:'',
			outputTip :'',
			inputSample : '',
			outputSample : '',
			standardSource :'',
			testdataNum :'',
			knowName:'',
			content :"",
			courseNameId :'',
			knowNameId : "",
			input:[],
			output:[],
			status:[],
			flag:true,
			book:true,
		/*	getCourseName:function(){	
				$.ajax({
					type:"get",
					content:"application/x-www-form-urlencoded;charset=UTF-8",
					url:"../problem/selectKnowLedge",
					dataType : 'json',
					async : false,
					success:function( result ){
						var length = result.total;
						var currId = "";
						var flag = 0;
						console.log(result);
						for(var i = 0 ; i < length ; i++){
							if(result.data[i].isCourse){
								if(!flag){
									currId = result.data[i].knowId;
									flag = 1;
								}
								$(".courseName").append("<option value="+result.data[i].knowId+">"+result.data[i].knowName+"</option>");
							}else{
								program.course.push(result.data[i]);
							}
						}
						for(var i = 0; i <length ; i++){
							if(result.data[i].parentId == currId){
								$(".knowName").append("<option value="+result.data[i].knowId+">"+result.data[i].knowName+"</option>");
							}
						}
					}
				});
			},*/
			addProblem:function(id){
				 $.ajax({
     					type : "post",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../problem/insertProblem",		
     					dataType : "json",
     					async : false,
     					data:{
     						"problemInfo.title" :program.title,
     						//"problemInfo.createTime":program.date,
			     			"problemInfo.level" :program.level,
			     			"problemInfo.limitTime" :program.limitTime,
			     			"problemInfo.limitMemory" :program.limitMemory,
			     			"problemInfo.author":program.author,
			     			"problemInfo.testdataNum" :program.testdataNum,
			     			"problemInfo.scoreRatio" :program.scoreRatio,
			     			"programInfo.codeLimit" :program.codeLimit,
			     			"programInfo.judgeModel" :program.judgeModel,
			     			"problemInfo.description" :program.description,
			     			"problemInfo.inputTip" :program.inputTip,
			     			"problemInfo.outputTip" :program.outputTip,
			     			"problemInfo.inputSample" :program.inputSample,
			     			"problemInfo.outputSample" :program.outputSample,
			     			"problemInfo.standardSource" : program.standardSource,
			     			 knowId :program.knowName
     					},
     					success:function(result){ 
     						console.log(result);
     						if(result.status=='1'){
     							program.probId = result.proId;
     						$('#upload').modal({
								backdrop : 'static'
							});
							$(".uploadData").click(function(){
							    program.uploadByFile();
							if( id=="justSave"){
								pubMeth.alertInfo("alert-success","添加成功");
     							window.location.href="problem.html";
     						}else if( id=="saveEdtor"){
     							pubMeth.alertInfo("alert-success","添加成功");
     						}else if (id=="saveAdd"){
     							 pubMeth.alertInfo("alert-success","添加成功");
     							 $(".ptitle").val("");
     							 ue.ready(function() {
     								 ue.setContent("");
     							 });
     							 $(".inputTip").val("");
								 $(".outputTip").val("");
								 $(".inputSample").val("");
								 $(".outputSample").val("");
							     $(".standardSource").val("");
							     $(".author").val("");
							     $(".scoreRatio").val("");
							     $(".testdataNum").val("");
							     $(".codeLimit").val("");
							     $(".judgeModel").val("");
     						}
							 });
     						}else{
     							pubMeth.alertInfo("alert-warning","问题添加失败！");
     						}
     					},
     					error:function(){
     						alert("1");
     						pubMeth.alertInfo("alert-warning","请求失败！");
     					}
     				});
		},
		 change:function(tagert , className){
			$(tagert).change(function() {
				var path = $(this).val();
				var path1 = path.lastIndexOf("\\");
				var name = path.substring(path1 + 1);
				$(className).val(name);
			});
		},
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
     					    program.content = result.data[0];
     					    console.log(program.content);
     					    program.courseNameId = result.course[0].knowId;//课程
     						program.knowNameId = result.knowledge[0].knowId;//知识点
     					    var parentId =program.courseNameId;
						    for(var i = 0; i < pubMeth.course.length ; i++){
							if(pubMeth.course[i].parentId == parentId){
								$(".knowName").append("<option value="+pubMeth.course[i].knowId+">"+pubMeth.course[i].knowName+"</option>");
							}
						}
     					},error:function(){
     					   pubMeth.alertInfo("alert-info","请求错误");
     					}
     				});
		},
	/*	getQueryObject:function () {
		   var  url = window.location.href;
		    var search = url.substring(url.lastIndexOf("?") + 1);
		    var obj = {};
		    var reg = /([^?&=]+)=([^?&=]*)/g;
		    search.replace(reg, function (rs, $1, $2) {
		        var name = decodeURIComponent($1);
		        var val = decodeURIComponent($2);                
		        val = String(val);
		        obj[name] = val;
		        return rs;
		    });
		    return obj;
		},*/
		getValue:function(){
			program.title = $(".ptitle").val();
			program.limitTime = $(".limitTime").val();
			program.limitMemory = $(".limitMemory").val();
			program.author = $(".author").val();
			program.level = $(".level option:selected").val();
			program.knowName = $(".knowName option:selected").val();
			program.judgeModel = $(".judgeModel option:selected").val();
			program.testdataNum = $(".testdataNum").val();
			program.scoreRatio = $(".scoreRatio").val();
			program.codeLimit = $(".codeLimit").val();
			program.description = ue.getContent();
			program.inputTip = $(".inputTip").val();
			program.outputTip = $(".outputTip").val();
			program.inputSample = $(".inputSample").val();
			program.outputSample = $(".outputSample").val();
			program.standardSource =$(".standardSource").val();
			if(program.knowName=="知识点") program.knowName = '';
			//program.date = program.getNowFormatDate();
			if(program.title!=""&&program.description!=""&&program.inputSample!=""
				&&program.scoreRatio!=""&&program.codeLimit!=""&&program.inputTip!=""&&program.testdataNum!=""
			    &&program.outputSample!=""&&program.outputTip!=""&&program.judgeModel!=""){
					return true;
			}else{
				return false;
			}
		},
		updateProblem:function(id){
			 $.ajax({
     					type : "post",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../problem/updateProblem",		
     					dataType : "json",
     					async : false,
     					data:{
     						"problemInfo.probId" :program.probId,
     						"problemInfo.title" :program.title,
     						"problemInfo.createTime":program.date,
			     			"problemInfo.level" :program.level,
			     			"problemInfo.limitTime" :program.limitTime,
			     			"problemInfo.limitMemory" :program.limitMemory,
			     			"problemInfo.author":program.author,
			     			"problemInfo.testdataNum" :program.testdataNum,
			     			"problemInfo.scoreRatio" :program.scoreRatio,
			     			"problemInfo.codeLimit" :program.codeLimit,
			     			"problemInfo.judgeModel" :program.judgeModel,
			     			"problemInfo.description" :program.description,
			     			"problemInfo.inputTip" :program.inputTip,
			     			"problemInfo.outputTip" :program.outputTip,
			     			"problemInfo.inputSample" :program.inputSample,
			     			"problemInfo.outputSample" :program.outputSample,
			     			"problemInfo.standardSource" : program.standardSource,
			     			 knowId :program.knowName
     					},
     					success:function(result){
     						if(result.status=='1' && id=="justSave"){
     							pubMeth.alertInfo("alert-success","修改成功");
     							window.location.href="problem.html";
     						}else if(result.status=='1' && id=="saveEdtor"){
     							pubMeth.alertInfo("alert-success","修改成功");
     						}else if (result.status=='1' && id=="saveAdd"){
     							 pubMeth.alertInfo("alert-success","修改成功");
     							 $(".ptitle").val("");
     							 ue.ready(function() {
     								 ue.setContent("");
     							 });
     							 $(".inputTip").val("");
								 $(".outputTip").val("");
								 $(".inputSample").val("");
								 $(".outputSample").val("");
							     $(".standardSource").val("");
							     $(".author").val("");
							     $(".scoreRatio").val("");
							     $(".testdataNum").val("");
							     $(".codeLimit").val("");
     						}
     						else{
     							pubMeth.alertInfo("alert-warning","修改失败！");
     						}
     					},
     					error:function(){
     						pubMeth.alertInfo("alert-warning","请求失败！");
     					}
     				});
		},
		setValue:function(){
			$(".ptitle").val(program.content.title);
			$(".limitTime").val(program.content.limitTime);
			$(".limitMemory").val(program.content.limitMemory);
			$(".author").val(program.content.author);
			$(".scoreRatio").val(program.content.scoreRatio);
			$(".testdataNum").val(program.content.testdataNum);
			$(".codeLimit").val(program.content.codeLimit);
			$(".judgeModel option[value='"+program.content.judgeModel+"']").attr("selected",true);
			$(".level option[value='"+program.content.level+"']").attr("selected",true);
			$(".courseName option[value='"+program.courseNameId+"']").attr("selected",true);
			$(".knowName option[value='"+program.knowNameId+"']").attr("selected",true);
			 ue.ready(function() {
				 ue.ready(function() {
					 ue.setContent(program.content.description);
				 });
			});
			$(".inputTip").val(program.content.inputTip);
			$(".outputTip").val(program.content.outputTip);
			$(".inputSample").val(program.content.inputSample);
			$(".outputSample").val(program.content.outputSample);
			$(".standardSource").val(program.content.standardSource);
		},
		/*getNowFormatDate:function () {
		    var date = new Date();
		    var seperator1 = "-";
		    var seperator2 = ":";
		    var month = date.getMonth() + 1;
		    var strDate = date.getDate();
		    if (month >= 1 && month <= 9) {
		        month = "0" + month;
		    }
		    if (strDate >= 0 && strDate <= 9) {
		        strDate = "0" + strDate;
		    }
		    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
		            + " " + date.getHours() + seperator2 + date.getMinutes()
		            + seperator2 + date.getSeconds();
		    return currentdate;
			},*/
		uploadByFile:function(){
				$.ajaxFileUpload({
					url : "../problem/insertProblemdata",
					secureuri : false,
					fileElementId : "datafile",// 文件选择框的id属性
					dataType : "json",
					data : {
						probId : program.probId 
					},
					success : function(data, status) {
						if(data.status){
							alert("添加成功！");
							$('#upload').modal('hide');
						}
					},
					error:function(){
						alert("添加失败！");
					}
					});
		},
		uploadByForm:function(){
			   $.ajax({
     					type : "post",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../problem/insertProblemdata",		
     					dataType : 'json',
     					async : false,
     					traditional: true,
     					data:{
     						probId : program.probId,
     						input : program.input,
     						output : program.output
     					},
     					success:function(result){ 
     					  console.log(result);
     					  if(result.status == 1){
     						 $(".testData").text("保存成功！！");
     					  }
     					 
     					},error:function(){
     					   pubMeth.alertInfo("alert-info","请求错误");
     					}
     				});
		},
		saveTestData:function(){
			var selectItem = $(".testdatatotal option:selected").val();
			var input = $(".testDataInput").val();
			var output = $(".testDataOutput").val();
			program.input[selectItem] = input;
			program.output[selectItem] = output;
			program.uploadByForm();
//			if(input == program.input[selectItem]&&output==program.output[selectItem]){
//				$(".testData").text("保存成功！！");
//			}
		},
		showTestData:function(){
			console.log("requestTestData");
			 $.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../problem/selectProblemData",		
     					dataType : 'json',
     					async : false,
     					traditional: true,
     					data:{
     						probId : program.probId
     					},
     					success:function(result){ 
     					  var length = result.total;
     					  for(var i = 0 ;i<length;i++){
     					  	$(".testdatatotal").append('<option>'+i+'</option>');
     					  	program.input.push(result.input[i]);
     					  	program.output.push(result.output[i]);
     					  }
     					},error:function(){
     					   pubMeth.alertInfo("alert-info","请求错误");
     					}
     				});
		},
		 getStatus :function(){
				$.ajax({
		     			type : "get",
		     			content : "application/x-www-form-urlencoded;charset=UTF-8",
		     			url:"../submit/selectAllSubmitResultInfo",
		     			dataType : 'json',
		     			async : false,
		     			success:function(result){ 
		     				program.status[0] = "未提交";
		     				for(var i = 0 ; i < result.data.length;i++){
		     					program.status[result.data[i].resuId] = result.data[i].name;
		     				}
		     			}
		     	});
		},
		testCode:function(){
			$.ajax({
					type : "post",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../submit/insertSubmitInfo",		
					dataType : 'json',
					async : false,
					data:{
						"submitInfo.submSource" : program.code,
						"submitInfo.judId" : program.judId,
						"submitInfo.probId" : program.probId,
						"submitInfo.isTeacherTest" :'1'
					},
					success:function(result){ 
						console.log(result);
						if(result.status == 1){
							program.submitId = result.submId;
						}
					},error:function(){
					   pubMeth.alertInfo("alert-info","请求错误");
					}
				});
		},
		
		getsubmit:function(){
			$.ajax({
				type:"get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url:"../submit/selectSubmitInfo",
				dataType:'json',
				async : false,
				data:{
					"submitInfo.submId" : program.submitId,			
				},
				success:function(result){
					console.log(result);
					program.submitResult = result.submitInfoList[0];//得到提交后信息
					var className ="",statuResult = "",result="";
				    if(program.submitResult == null){
				    	$(".submitResult").show().delay(1000).fadeOut();
				    }else{
				    	if(program.submitResult.status == 2){//PE
					     	className = 'label-primary';
					     }else if(program.submitResult.status == 5){//WA
					     	className = 'label-danger';
					     }else if(program.submitResult.status == 11){//OW
					     	className = 'label-default';
					     }else if(program.submitResult.status == 10){//OW
					     	className = 'label-info';
					     }else if(program.submitResult.status == 1){//AC
					     	className = 'label-success';
					     }else{//Others
					     	className = 'label-warning';
					     }
					     result = '<span class="label '+className+'">'+program.status[program.submitResult.status]+'</span>';
				    	
					     if(program.submitResult.status==8){
								statuResult = "ceStatus";
							}else{
								statuResult = "noCeStatus";
							}
					    $(".submitResult").html('<span class="'+statuResult+'" id='+program.submitResult.submId+'>'+result+'</span>');
				    	
				    	$(".submitResult").css('display','block'); 
				    	if(program.submitResult.status==11){
				    		setTimeout(program.getsubmit,500);
				    	}
				    }
				},
				error:function(){
					alert("1");
				}
			});
		},
		testGradeRatio:function(){
			program.book = true;
			var testdata = $(".testdataNum").val();
			var testdataNum = 0;
			if($.isNumeric(testdata)){
				testdataNum = parseInt(testdata);
				if(testdataNum <= 0 || testdataNum == NaN) {
					program.book = false;
					$(".ScoreSetTips").html("测试组数不能小于1").css("color","red");
					return;
				}
			}else{
				program.book = false;
				$(".ScoreSetTips").html("测试组数输入非法 请输入数字").css("color","red");
				return;
			}
			
			var scoreRatio = $(".scoreRatio.scoreprop").val().split(",");
			if(scoreRatio.length != testdataNum){
				program.flag = false;
				$(".ScoreSetTips").html("测试组数与分数比例组数不匹配或输入不合法").css("color","red");
				return;
			}
			var scoreTotal = 0;
			for(var i = 0 ; i < testdataNum; i ++){
				(function(num){
					if($.isNumeric(scoreRatio[num]) == false){
						program.flag = false;
						$(".ScoreSetTips").html("分数比例输入不合法，请输入数字").css("color","red");
						return;
					}
					scoreTotal += parseInt(scoreRatio[num]);
				}(i));
			}
			if(scoreTotal != 100){
				program.flag = false;
				$(".ScoreSetTips").html("分数比例之和不为100").css("color","red");
				return;
			}
			
			$(".ScoreSetTips").html(tipContent).css("color","black");
		}
		
		   /* foucsinfo:function(){
		    	
		    }*/
			/*getKnowById:function(){
					 $.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../problem/selectKnowLedgeByProbId",		
     					dataType : 'json',
     					async : false,
     					data:{
     						probId : program.probId 
     					},
     					success:function(result){ 
     						console.log(result);
     					if(result.status){
     						$(".knowName").html("");
     						program.courseNameId = result.course.knowId;//课程
     						program.knowNameId = result.knowledge.knowId;//知识点
     					    var parentId =program.courseNameId;
						    for(var i = 0; i < pubMeth.course.length ; i++){
							if(pubMeth.course[i].parentId == parentId){
								$(".knowName").append("<option value="+pubMeth.course[i].knowId+">"+pubMeth.course[i].knowName+"</option>");
							}
						}
     					}
     					},error:function(){
     					   pubMeth.alertInfo("alert-info","请求错误");
     					}
     				});
			}*/
		};	
		var tipContent = $(".ScoreSetTips").html();
		program.change('input[id=datafile]','.testdata');
		program.getStatus();
		pubMeth.serCourse();
		var  ue = UE.getEditor('description', {
  		   initialFrameWidth:750 ,//初始化编辑器宽度,默认1000
  	       initialFrameHeight:300  //初始化编辑器高度,默认320
  		});
		var par = pubMeth.getQueryObject();
		program.probId = par['id'];
		if(program.probId == undefined) program.probId = "";
		if(par.id){
				$(".pageName").text("修改问题");
				program.probId = par.id;
				program.getProById();
				program.setValue();
			$(".formTest").click(function(){
					$('#upload').modal({
						backdrop : 'static'
					});
			$(".uploadData").click(function(){
				program.uploadByFile();
				window.location.reload();
			});
			});	
		}else{
				$(".testDataByForm").hide();
				$(".testCode").hide();
		}
		
		$(".searchTest").click(function(){
			$(".testdatatotal").removeAttr("disabled"); 
			program.showTestData();
		});
		
		$("#testCode").on('click', function () {
		    var $btn = $(this).button('loading');
		    	program.code = $("#standardSource").val();
		    	program.judId = $(".selectJudId option:selected").val();
	  	  		program.testCode();
	  	  		if(program.submitId !=''){
		  	  		setTimeout(program.getsubmit,500);
			    	setTimeout(function () { $btn.button('reset'); },1000);
	  	  		}
	  	  		
	  	  });
		$(".download").click(function(){
			window.location.href="../problem/importProblmData?probId="+program.probId;
		});
		$(".saveTestData").click(function(){
		   program.saveTestData();
		});
		$(".save").click(function(){
			if(program.getValue() && program.flag == true && program.book == true){
						var id = this.id;
						 if(program.probId==""){
						    program.addProblem(id);
						 }else{
						 	program.updateProblem(id);
						 }
					
			}else{
				pubMeth.alertInfo("alert-danger","必填字段请填写完整！");
			}
		});
		$(".testdataNum").blur(function(){
			program.testGradeRatio();
		});
		$(".scoreRatio.scoreprop").blur(function(){
			program.testGradeRatio();
        });
		 $(".testdatatotal").change(function(){
		 	$(".testData").text("");
			var selectItem = $(".testdatatotal option:selected").val();
			if(selectItem=="选择参数" || selectItem==""){
				$(".testDataInput").val("");
			   $(".testDataOutput").val("");
			}else{
		    $(".testDataInput").val(program.input[selectItem]);
			$(".testDataOutput").val(program.output[selectItem]);
			}
		});
});