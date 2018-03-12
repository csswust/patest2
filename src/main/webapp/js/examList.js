define(function (require, exports, module) {
	require('jquery');
	require('jCookie');
	require('../js/common.js');
	require('bootstrap');
	require('paginator');
    require('fake');
	var now = (new Date()).toLocaleString();
	$('.nowTime').text(now);
	setInterval(function() {
	    now = (new Date()).toLocaleString();
	    $('.nowTime').text(now);
	}, 1000);	
	
	$(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".examination").css("color","white");
	
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    
	var program ={
		title:'',
		page :"1",
		data:[],
		state:[],
		prostate:[],
		html:'',
		studentTotalList :[],
		examId:'',
		getExamInfo:function(){
			 $.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../exam/selectExamList",
     					dataType : 'json',
     					async : false,
     					data:{
     						page:program.page,
     						rows:pubMeth.rowsnum,
     					},
     					success:function(result){ 
     						console.log(result);
     						program.count = result.total;
     						program.data = result.data;
     						program.state = result.statusList;
     						program.prostate = result.proState;
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
     						$(".loading").html("<img />");    				
     						if(result.status >'0'){
     							pubMeth.alertInfo("alert-success","抽题成功！");   							
     						}
     						else if(result.status=="-1"){
     							pubMeth.alertInfo("alert-danger","未添加考生！");
     						}else if(result.status=="-2"){
     							pubMeth.alertInfo("alert-danger","未添加试卷参数！");
     						}
     						else if(result.status=="-4"){
     							pubMeth.alertInfo("alert-danger","试卷参数对应的题目数目为0！");
     						}
     						else if(result.status=="-5"){
     							pubMeth.alertInfo("alert-danger","试卷参数与题目数目异常，请检查重复参数是否小于或者等于对应的题目数目！");
     						}else if(result.status == "-6"){
     							pubMeth.alertInfo("alert-danger","正在抽题，请等待！");
     						}
     						program.showInfo();
     					},
     					error:function(){
     						pubMeth.alertInfo("alert-danger","请求错误！");
     					}
     		});
		},
		showInfo:function(){
			var length = program.data.length;
			var stateInfo="";
			var title = '';
			program.html = "";
			for(var i = 0; i < length;i++){
				title = '<a  href="examInfo.html?id='+program.data[i].examId+'">' +program.data[i].title + '</a>';
				if(program.state[i]=='2'){
					if(program.prostate[i] == '1'){
						stateInfo='已结束&nbsp;<a href="remark.html?id='+program.data[i].examId+' "class="markPaper">阅卷</a>';
					}else if(program.prostate[i] == '0'){
						stateInfo='已结束 未抽题';
					}		
				}else if(program.state[i]=='1'){
					if(program.prostate[i] == '1'){
						stateInfo="进行中";
					}else if(program.prostate[i] == '0'){
						stateInfo='进行中&nbsp;未抽题<a href="#" class="drawQuestion" id="'+program.data[i].examId+'">抽题</a>&nbsp;<span class="loading"><img /></span>';						
					}
				}else if(program.state[i]=='0'){
					if(program.prostate[i] == '0'){
						stateInfo='未开始&nbsp;未抽题<a href="#" class="drawQuestion" id="'+program.data[i].examId+'">抽题</a>&nbsp;<span class="loading"><img /></span>';
					}else if(program.prostate[i] == '1'){
						stateInfo='未开始&nbsp;已抽题';
					}			
				}
		    program.html+='<tr><td>'+program.data[i].examId+'</td>'
					    +      '<td>'+title+'</td>'
					    +      '<td>'+program.data[i].startTime+'</td>'
					    +      '<td>'+program.data[i].endTime+'</td>'
					    +      '<td>'+program.studentTotalList[i]+'</td>'
					    +      '<td>'+stateInfo+'</td>'
					    +  '</tr>';
			}
		},
	};
   pubMeth.getRowsnum("rowsnum");
   program.getExamInfo();
   	$(".drawQuestion").on('click',function(){
		console.log(this.id);
		$(".loading img").attr("src","../img/loading.gif");
		$(".loading img").css({
			width:20,
			height:20,
		})
		program.examId = this.id;
		program.drawQuestion();		
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
 		     	program.getExamInfo();
 	        }
       });
    } else {
    	$(".pagenum").css("display","none");
    }
});