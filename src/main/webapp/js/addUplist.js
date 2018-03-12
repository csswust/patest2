define(function (require, exports, module) {
	    require('jquery');
	    require('jCookie');
	    require('jSession');
		require('../js/commonet.js');
		require('fileupload');
		require('paginator');
		require('bootstrap');
	$(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".examList").css("color","white");
	$(".etlist").addClass("on");
	$(".mytest").addClass("onet");

	 var pubMeth = require('../js/public.js');
	 var program ={
		page: '1',
	 	examId :'',
	 	html:'',
	 	userInfo:'',
	 	userProfile:'',
	 	count:'',
	 	path:'',
	    expmArr : "",
	    expmIds :[],
	    expmId:"",
		showInfo:function(){
			var length = program.userInfo.length;
			if(length != 0){
				var stulist = '<tr>'
					 + '<th>序号</th>'
		             + '<th>姓名</th>' 
		             + '<th>学号</th>'
		             + '<th>考号</th>'
		             + '<th>班级</th>'
		             + '<tr>';	
				$("#stuhead").html(stulist);
			}
			program.html="";
			for(var i = 0;i<length;i++){	
				var order = i+1;
				program.html += '<tr>'
					    +'<td>' + order + '</td>'
						+'<td>' + program.userProfile[i].realName + '</td>'
						+'<td>' + program.userProfile[i].studentNumber + '</td>'
						+'<td>' + program.userInfo[i].username + '</td>'
						+'<td>' + program.userProfile[i].className + '</td>'
						+'</tr>';				
			}
		},
		//上传学生名单
		importList:function(){
				$.ajaxFileUpload({
					url : "../exam/leadUserData",
					secureuri : false,
					fileElementId : "namefile",// 文件选择框的id属性
					dataType : "json",
					data : {
						examId : program.examId					
					},
					success : function(result) {
						console.log(result);
						console.log(result.status);
						if(result.status=='1'){
                 			pubMeth.alertInfo("alert-success","上传成功");
							console.log(result.path);
							program.path = result.path;
							window.location.href = '../img/'+result.path;						
							program.selectUserBaseInfo();
						} else {
							pubMeth.alertInfo("alert-warning","上传失败");
						}
					},
					error:function(){
						pubMeth.alertInfo("alert-warning","上传失败");
					}
					});
		},
		selectUserBaseInfo:function(){
			$.ajax({
			    type : "post",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url:"../exam/selectUserBaseInfo",		
				dataType : 'json',
				async : false,
				data:{
					examId:program.examId,
					page:program.page,
					rows:"20"
				},
				success:function(result){
					console.log(result);
	                program.userInfo = result.userInfoList;
	                program.userProfile = result.userProfileList;
	                program.count = result.total;
	                program.showInfo();
	        		$("#stuInfo").empty();
	    			$("#stuInfo").append(program.html);	   
				},
				error:function(){
					
				}
		});
		},
		change:function(tagert , className){
			$(tagert).change(function() {
				var path = $(this).val();
				var path1 = path.lastIndexOf("\\");
				var name = path.substring(path1 + 1);
				console.log(path);
				console.log(path1);
				console.log(name);
				$(className).val(name);
			});
		},
		
	 };
	 
		 function getUrlParam(name){
		 var reg = new RegExp();
	    }			
		pubMeth.serCourse();
		var par = pubMeth.getQueryObject();
		if(par.examId){
			$(".pageName").text("添加考试");
			program.examId = par.examId;	
		}
		if(par.Id){
			$(".pageName").text("修改考试");
			program.examId = par.Id;		
		}
		program.selectUserBaseInfo(); 
	    //上传学生名单
	 	program.change('input[id=namefile]','.namefile');
	 	$(".importList").show();
	 	$(".importList").click(function(){
	 		$('#import').modal({
					backdrop : 'static'
			});
			$(".comImport").click(function(){
				program.importList();		
			});
		});
	 	 program.selectUserBaseInfo();  
	 	$(".save").click(function(){	 				      
			if(par.examId){
				if(program.userInfo){
					window.location.href = "test.html";
				} else {
					pubMeth.alertInfo("alert-warning","考生名单没有上传");
				}
			 }else if(par.Id){
				 if(program.userInfo){
					 window.location.href = "exam.html";
					} else {
						pubMeth.alertInfo("alert-warning","考生名单没有上传");
					}			 
			}
		});
		//下载学生上传模板
	 	$(".downloada").attr("href","../img/ListTemplate.xls");
	 	//点击上一步返回题库
	 	$(".upList").click(function(){
	 		if(par.examId){
	 			window.location.href = 'addParm.html?examId='+program.examId;
	 		}else if(par.Id){
	 			window.location.href = 'addParm.html?Id='+program.examId;
	 		}
	 	});
	 	if(program.count > 0){
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
					program.selectUserBaseInfo();
		        }
	        });
	 	} 	 	  	
});
