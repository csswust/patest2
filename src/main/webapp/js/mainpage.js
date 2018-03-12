define(function (require, exports, module) {
	require('jquery');
    require('jCookie');
	require('bootstrap');
	require('paginator');
	require('../js/loginreg.js');
	$('#username').focus();
	

	var program = {
			username: "",
			password: "",
			lookhtml:"",
			lookhtmls:"",
			infohtml:"",
			data:"",
			html:"",
			count:"",
			epnoIds:[],
			/**
			 * 考试场次数目
			 */
			examselect:function(){
				$.ajax({
					type: "POST",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../exam/selectExamTotal",
					dataType: 'json',
					async: false,
					data:{
						page:'1',
						rows:'10',
					},
					success: function(result) {
		                console.log(result);
		                var num=document.getElementById("examnum");
		                num.innerText=result.status+"场"; 
		                if( result.status > 0){
		                }else{
		                }
						
					},
					error: function() {
						alert("请求失败");
					}
				});
			},
			/**
			 * 考试人数
			 */
			userselect:function(){
				$.ajax({
					type: "POST",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../user/selectUserTotal",
					dataType: 'json',
					async: false,
					data:{
						page:'1',
						rows:'10',
					},
					success: function(result) {
		                console.log(result);
		                var num=document.getElementById("usernum");
		                num.innerText=result.status+"人"; 
		                if( result.status > 0){
		                }else{
		                }
						
					},
					error: function() {
						alert("请求失败");
					}
				});
			},
			/**
			 * 考试题目数目
			 */
			problemselect:function(){
				$.ajax({
					type: "POST",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../problem/selectProblemAllCount",
					dataType: 'json',
					async: false,
					data:{
						page:'1',
						rows:'10',
					},
					success: function(result) {
		                console.log(result);
		                var num=document.getElementById("problemnum");
		                num.innerText=result.problmeCount+"个"; 
		                if( result.status > 0){
		                }else{
		                }
						
					},
					error: function() {
						alert("请求失败");
					}
				});
			},
			/**
			 * 系统题目类型数目
			 */
			knowledgeselect:function(){
				$.ajax({
					type: "POST",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../problem/selectKnowledgeAllCount",
					dataType: 'json',
					async: false,
					data:{
						page:'1',
						rows:'10',
					},
					success: function(result) {
		                console.log(result);
		                var num=document.getElementById("knowledgenum");
		                num.innerText=result.knowledgeCount+"个"; 
		                if( result.status > 0){
		                }else{
		                }
						
					},
					error: function() {
						alert("请求失败");
					}
				});
			},
			/**
			 * 公告展示
			 */
			selectNotice:function(){
				$.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url : "../epNotice/selecByCondition",
					dataType : 'json',
					async : false,
					data : {
						page:program.page,
						row:'10',				
					},
					success : function(result) {
						console.log(result);	
						if(result.status == 1){
							program.data = result.list;
							program.count = result.total;
							program.showNotice();
							$(".shownotice").empty();
							$(".shownotice").append(program.html);
						}else{
							pubMeth.alertInfo("alert-danger","查询错误");
						}					
					},
					error:function(){
						  pubMeth.alertInfo("alert-danger","请求错误");
					}
				});
			},
			showNotice:function(){
				program.html = "";	
				var length;
				if(program.data.length >= 8){
					length = 8;
				}else{
					length = program.data.length;
				}
				program.html="";
				for(var i = 0;i < length;i++){
					var time = program.data[i].addTime.split(" ")[0];
					program.html+='<li class="list-group-item"><div class="record clearfix"><div class="link">'
						+'<span class="glyphicon glyphicon-chevron-right " aria-hidden="true "></span>'
						+'<a href="epnoticetext.html?epid='+ program.data[i].epnoId+'">'+ program.data[i].title +'</a></div>'
						+'<div class="badge">['+ time +']</div></div></li>';
				}
			},
			
		};
		program.examselect();
		program.userselect();
		program.problemselect();
		program.knowledgeselect();
		program.selectNotice();
	
		// 左侧导航
		var oDivlf = document.getElementById('topnav');
	    var links = oDivlf.getElementsByTagName("a");	
		for(var i = 0; i < links.length; i++) {
			links[i].index = i;
			links[i].onclick = function() {

				for (var j = 0; j < links.length; j++) {
					links[j].className = ' ';
					
				}
				this.className = 'active ';
				
			};
		}
		$('#carousel-example-generic').carousel({
			interval: 2000
		}); //每隔5秒自动轮播 
});
	