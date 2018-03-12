define(function (require, exports, module) {
	require('jquery');
	require('jCookie');
	require('../js/commonet.js');
	require('bootstrap');
	require('paginator');
	$(".mytest").addClass("onet");
	
	var now = (new Date()).toLocaleString();
	$('.nowTime').text(now);
	setInterval(function() {
	    now = (new Date()).toLocaleString();
	    $('.nowTime').text(now);
	}, 1000);	
	
	$(".examRoom").next(".treeview-menu").toggle("slow");
	$(".examRoom").addClass("leftActive");
	$(".examination").css("color","white");
	
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    
	var program ={
		title:'',
		page :"1",
		data:[],
		testdata:[],
		state:[],
		info:[],
		html:'',
		studentTotalList :[],
		examId:'',
		sysuserid:'',
		getsysuserid:function(objName){
			  var arrStr = document.cookie.split("; "); 
	           for (var i = 0; i < arrStr.length; i++) { 
	                 var temp = arrStr[i].split("="); 
	                  if (temp[0] == objName)
	                  	program.sysuserid = unescape(temp[1]);        
	      		 }
		},
		getExamInfo:function(){
			console.log(program.sysuserid);
			 $.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../sysUser/selecExamBySysUsrId",
     					dataType : 'json',
     					async : false,
     					data:{
     						sysusrId:program.sysuserid ,
     						page:program.page,
     						rows:"10"
     					},
     					success:function(result){ 
     						console.log(result);
     						program.count = result.total;  						
     						program.info = result.data;
     						var k=0;
     						for(var i=0;i<program.info.length;i++){
     							if(program.info[i] != null){
     								program.testdata[k++] = program.info[i].data;
     								//console.log(program.testdata[i]);
     							}
     						}
     						console.log(program.testdata);    						
     						program.data = program.testdata;    						     					
     						program.state = result.statusList;
     						program.studentTotalList = result.studentTotalList;
     						program.showInfo();
     						$("#listInfo").empty();	
     					  	$("#listInfo").append(program.html);
     					}
     				});
		},
		drawQuestion:function(){
				 $.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../problem/drawProblem",
     					dataType : 'json',
     					async : false,
     					data:{
     						examId : program.examId
     					},
     					success:function(result){ 
     						console.log(result);
     						if(result.status=="-3"){
     							alert("alert-danger","缺少用户名单！");
     						}
     						else if(result.status =='0'){
     							alert("抽题成功！");
     						}
     						else if(result.status=="-1"){
     							alert("alert-danger","参数异常！");
     						}
     						else if(result.status=="-4"){
     							alert("alert-danger","缺少题目模板！");
     						}
     						else if(result.status=="-5"){
     							alert("alert-danger","模板的重复次数大于满足条件的题目数目！");
     						}
     						else if(result.status=="-2"){
     							alert("alert-danger","该场考试不存在！");
     						}
     					}
     		});
		},
		showInfo:function(){
			var length = program.data.length;
			var order = 1;
			var stateInfo="";
			var title = '';
			program.html = "";
			for(var i = 0; i < length;i++){
				title = '<a  href="testInfo.html?id='+program.data[i][0].examId+'">' +program.data[i][0].title + '</a>';
				if(program.state[i]=='2'){
					stateInfo='已结束&nbsp;';
				}else if(program.state[i]=='1'){
					stateInfo="进行中";
				}else if(program.state[i]=='0'){
					stateInfo='未开始&nbsp;';
					//title = program.data[i].title;
				}
		    program.html+='<tr><td>'+order+'</td>'
					    +      '<td>'+title+'</td>'
					    +      '<td>'+program.data[i][0].startTime+'</td>'
					    +      '<td>'+program.data[i][0].endTime+'</td>'
					    +      '<td>'+program.studentTotalList[i]+'</td>'
					    +      '<td>'+stateInfo+'</td>'
					    +  '</tr>';
				order++;
			}
		}
	};
   program.getsysuserid("sysuserId");
   program.getExamInfo();
   	$(".drawQuestion").on('click',function(){
		console.log(this.id);
		program.examId = this.id;
		program.drawQuestion();
	});		
	
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
	    		     	program.getExamInfo();
	    	        }
	 });
});