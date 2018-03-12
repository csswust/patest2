define(function (require, exports, module) {
	require('jquery');
	require('jCooike');
	require('jSession');
	require('../js/commonet.js');
	require('paginator');
	require('bootstrap');
	var template = require('artTemplate');
	var pubMeth = require('../js/public.js');
	$(".quesMana").next(".treeview-menu").toggle("slow");
	$(".quesMana").addClass("leftActive");
	$(".mytest").addClass("onet");	
	$(".problemList").css("color","white");
	$(".check_list").click(function(){
		if(this.checked){    
		 	$("#listInfo").find("input[type='checkbox']").prop("checked", true);      
	    }else{    
	       $("#listInfo").find("input[type='checkbox']").prop("checked", false);
	    }    
	});
	var program ={
		count:'',
		title:'',
		page:'1',
		level:'',
		know:'',
		data:'',
		valArr:[],
		probIdArr:[],
		probtiArr:[],
		bankId:'',
		bankIds:'',
		probIdstr:"",
	    titlestr:"",
	    levelstr:"",
	    knowNamestr:"",
	    problist:[],
	 	knowlist:[],	    
	    probIdList:[],
	    titleList:[],
	    levelList:[],
	    knowNameList:[],
		getProblemInfo:function(){
			 $.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../problem/selectProblem",		
     					dataType : 'json',
     					async : false,
     					data:{
     						knowId:program.know,
     						page:program.page,
     						rows:"20"
     					},
     					success:function(result){ 
     					     program.count = result.total;
     					     program.data = result;
     					     program.showProblem();    			
     					},error:function(){
     					   pubMeth.alertInfo("alert-info","请求错误");
     					}
     				});
		},
		showProblem:function(){
			 $("#listInfo").empty();	
			 var length = program.data.data.length;
     	     var flag = 1;
     		 for(var i = 0; i< length;i++){
     				var difficult ;
//     				if(program.data.data[i].totalSubmit == 0){
//     						difficult = 0 ;
//     				}else{
//     						difficult = program.data.data[i].acedNum/program.data.data[i].totalSubmit;
//     				}
     				difficult=program.data.data[i].level;
     				var courseName = "";
     				var knowName="";
     				if(program.data.course[i]!=null){
     					courseName = program.data.course[i].knowName;
     				}
     				if(program.data.knowledge[i]!=null){
     					knowName = program.data.knowledge[i].knowName;
     				}
     				$("#listInfo").append('<tr>' +
     				'<td><input type="checkbox" value="'+program.data.data[i].probId +','+ program.data.data[i].title + '" name="title" class="show"/></td>'+
					'<td>'+program.data.data[i].probId+'</td>'+
					'<td>'+program.data.data[i].title+'</td>'+
					'<td>'+difficult+'</td>'+
					'<td>'+program.data.data[i].acedNum+'/'+program.data.data[i].totalSubmit+'</td>'+
					'<td>'+program.data.data[i].createTime+'</td>'+
					'<td>'+courseName+'</td>'+
					'<td>'+knowName+'</td>'+
     				'</tr>');
     				 flag++;
     		 }
		},
		alertInfo:function(className,info){
			    if($(".tip").text().trim()==""){
     				$(".tip").html(' <div class="alert  '+className+'" style="margin-top:10px;" id="tip">'+
  			  							   '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
  			  							   '<strong>'+info+'</strong></div>');
     				}else{
					$("#tip").removeClass();
					$("#tip").addClass("alert " +className);
     				$("strong").text(info);
     				}
		},
		searchProblem:function(){
			$.ajax({
				type:"post",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
     			url:"../problem/selectProblem",		
     			dataType : 'json',
     			async : false,
     			data:{
     				"problemInfo.title": program.title,
     				"problemInfo.level" : program.level,
     				 knowId : program.know,
     				 page : program.page,
     				 rows :"20"
     			},
     			success:function(result){
     				 program.count = result.total;
     				 program.data = result;
     				 program.showProblem();
     			},
     			error:function(){
     				pubMeth.alertInfo("alert-info","请求错误");
     			}
			});
		},
		queryCondition:function(){//为真为有查询条件，为假为无条件查询
			program.title = $(".searTitle").val().trim();
			program.level = $(".level option:selected").val();
			program.know = $(".knowName option:selected").val();
			if(program.know=="知识点")
				program.know="";
			if(program.know=="难度")
				program.level="";
			if(parm["id"]!=null){
			    program.know = parm["id"];
			}
		    if(program.title=="" &&program.level==""&&program.know==""){
				return false;
			}
			return true;
		},		
		getIds:function(){
			var proIds = new Array();
			var proinfo = $(".proinfo").text().replace(/(^\s+)|(\s+$)/g,"").replace(/\s/g,"").split(";");
			for(var i = 0;i<proinfo.length-1;i++){
				proIds[i] = proinfo[i].split(",")[0];
			}
			program.bankIds = proIds.join(",");
			console.log(program.bankIds);
			//$.cookie("bank",program.bankIds);
		},
		//后
		getproblemInfoById:function(){
			$.ajax({
				    type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../problem/selectProblemByIds",		
					dataType : 'json',
					async : false,
					data:{
						probIds:program.bankIds,
					},
					success:function(result){
						console.log(result);
						program.problist = result.data;
						program.knowlist = result.knowledge;
						program.getproinfo();							
						},
			});
		},
		getproinfo:function(){
			var length = program.problist.length;
			for(var i = 0;i < length;i++){	
				program.probIdList[i] = program.problist[i].probId;
				program.titleList[i] = program.problist[i].title;
				program.levelList[i] = program.problist[i].level;
				program.knowNameList[i] = program.knowlist[i].knowName;
			}
		    program.probIdstr = program.probIdList.join(",");
		    program.titlestr = program.titleList.join(",");
		    program.levelstr = program.levelList.join(",");
		    program.knowNamestr = program.knowNameList.join(",");		
		},
		//将所选的问题题库添加到数据库
		insertTempProblem:function(){
			console.log(program.examId);
			$.ajax({
			    type : "get",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url:"../exam/insertTempProblem",		
				dataType : 'json',
				async : false,
				data:{
					probIdList:program.probIdstr,
					titleList:program.titlestr,
					levelList:program.levelstr,
					knowNameList:program.knowNamestr,
					examId:program.examId,
				},
				success:function(result){
					console.log(result);
					if(result.status == 1){						
					    if(parm.examId){
					    	window.location.href = "addBank.html?examId="+parm.examId;	
					    }else if(parm.Id){
					    	window.location.href = "addBank.html?Id="+parm.Id;
					    }
						pubMeth.alertInfo("alert-success","保存成功");
					}else{
						pubMeth.alertInfo("alert-success","保存失败");
					}
				},
				error:function(){
					
				}
		});
		},
		checkbank:function(){
			$("input[name='title']").click(function(){
				var proIdTi = $(this).val();
				if($(this).prop("checked") == true){
					$(".proinfo").show();
					$(".proinfo").append('<span>'
							+ proIdTi
							+';'
							+'</span>'
							);
					program.getIds();
				}else{
					var proinfo = $(".proinfo").text().replace(/(^\s+)|(\s+$)/g,"").replace(/\s/g,"").split(";");
					var thisinfo = $(this).val().replace(/(^\s+)|(\s+$)/g,"").replace(/\s/g,"");
					$(".proinfo").empty();			
					for(var i=0;i<proinfo.length-1;i++){
						if(proinfo[i] != thisinfo){
							$(".proinfo").append(proinfo[i]+';'); 					
						}				
					}
					program.getIds();
				}
			});
		},
	};
	var parm = pubMeth.getQueryObject();
	if(parm["id"]!=null){
			program.know = parm["id"];
	}
	program.getProblemInfo();
	program.checkbank();
	pubMeth.serCourse();

	$(".search").click(function(){
		program.page ="1";
		if(program.queryCondition()){
			program.searchProblem();
			program.checkbank();
		}else{
			program.getProblemInfo();
			program.checkbank();
		}
	});
	
	$(".addProblem").click(function(){
		window.location.href="question.html";
	});
	var parm = pubMeth.getQueryObject();	
	$('.addbank').on('click', function (e) {
		if(program.bankIds!=""){
			if(parm.examId){
				$("#modaladdbank").modal({
					backdrop : 'static'
			    });
				$(".confirmadd").click(function(){
					program.getproblemInfoById();
					program.examId = parm["examId"];
					program.insertTempProblem();
				});
				/*if(confirm("你确定要添加这些题目吗？")){
					program.getproblemInfoById();
					program.examId = parm["examId"];
					program.insertTempProblem();					
				}*/
			}else if(parm.Id) {
				$("#modaladdbank").modal({
					backdrop : 'static'
			    });
				$(".confirmadd").click(function(){
					program.getproblemInfoById();
					program.examId = parm["Id"];
					program.insertTempProblem();
				});
				/*if(confirm("你确定爱上 要添加这些题目吗？")){
					program.getproblemInfoById();
					program.examId = parm["Id"];
					program.insertTempProblem();					
				}*/
			}		 
		}else{
			pubMeth.alertInfo("alert-info","请先勾选添加项！");
		}
		});
	
	/*console.log(program.count);*/
	 $.jqPaginator('#pagination', {
	    	    	totalCounts : program.count,
	    	        visiblePages: 5,
	    	        currentPage: 1,
	    	        pageSize: 20,
	    	        first: '<li class="first"><a href="javascript:;">首页</a></li>',
	    	        last: '<li class="last"><a href="javascript:;">尾页</a></li>',
	    	        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
	    	        onPageChange: function (num, type) {
	    	      	if(type == 'init') {return;}
	    	      		program.page = num;
	    	      			if(program.queryCondition()){
								program.searchProblem();
							}else{
								program.getProblemInfo();
								program.checkbank();
							}
	    	        }
	    });
});