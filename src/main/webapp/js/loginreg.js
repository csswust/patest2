define(function (require, exports, module) {
	require("jquery");
	var urlMeth = {
			username:'',
			password:'',
			nickname:'',
			inputMail:'',
			inputpassword:'',
			inputpass:'',
			realName:'',
			telephone:'',
			/**
			 * 登陆
			 */
			login: function() {
				$.ajax({
					type: "POST",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../sysUser/login",
					dataType: 'json',
					async: false,
					data:{
						sysusrName: urlMeth.username,
						password: urlMeth.password
					},
					success: function(result) {
						console.log(result);
						
						var exp = new Date();
						
//						document.cookie ="sysname="+escape(result.sysUserName);
//						document.cookie ="sysuserId="+escape(result.sysuserId);
						
						if (result.status == "0") {
							alert("登录失败!");
						}	
						else if (result.status=="1" ) {
							$.cookie("sysname",result.sysUserName);
							$.cookie("sysuserId",result.sysuserId);
							window.location.href = "homepaged.html";
						}
					
					},
					error: function() {
						alert("账号或密码错误，请重新登录!");
						$("#username").val("");
						$("#password").val("");
					}
				});
			},
			judge:function(){
				urlMeth.username = $("#username").val();
				urlMeth.password = $("#password").val();
				if (urlMeth.username != "" && urlMeth.password != "") 
				{
					urlMeth.login();
				} 
				else if(urlMeth.username == "" && urlMeth.password !="") {
					alert("用户名不能为空");
				} else if(urlMeth.password == "" && urlMeth.username !="") {
					alert("密码不能为空");
				} else {
					alert("请完善登录信息或去注册");
					// 给出提示信息
				}
			},
			/**
			 * 注册
			 */
			register: function() {		
				$.ajax({
					type: "POST",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../sysUser/insertSysUser",
					dataType: 'json',
					async: false,
					data:{
						sysusrName:urlMeth.nickname,
						eMail:urlMeth.inputMail,
						password:urlMeth.inputpassword,
						phone:urlMeth.telephone,
						unit:urlMeth.unit,
					},
					success: function(result) {
		                console.log(result);
		                if( result.status > 0){
		                	$("#myReg").modal('hide');
		                	alert("注册成功");
		                }else{
		                	alert("注册失败");
		                }
						
					},
					error: function() {
						alert("请求失败");
					}
				});
			},
			getQueryObject:function () {
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
				},
				/**
				 * 内容查询
				 */
				selectEpinfo : function(){
					$.ajax({
							type : "get",
							content : "application/x-www-form-urlencoded;charset=UTF-8",
							url:"../epInfo/selectEpInfo",
							dataType : 'json',
							async : false,
							data :{
								
							},
							success:function(result){ 
				                console.log(result);
				                if(result.status == 1){
				                	$(".contpeo").html(result.data.servPeople);
				                	$(".contway").html(result.data.servConcern);
				                	$(".teaminfo").html(result.data.teamInfo);
				                	$(".principal").html(result.data.principal);
				                	$(".phone").html("("+result.data.telephone+")");
				                	$(".email").html(result.data.eMail);
				                	$(".taddress").html(result.data.address);
				                	$(".address").html(result.data.address);
				                }		   
							}
					});
				},
	};
	urlMeth.selectEpinfo();
	$("#btnlogin").click(function() {
		urlMeth.judge();
	});	
	$("#userlogin").click(function(){
		$('#myModal').modal();
	});
	$("#userreg").click(function(){
		$('#myReg').modal();
	});
	$("#register").click(function(){
		var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
		urlMeth.nickname = $("#nickname").val();
		urlMeth.inputMail = $("#inputMail").val();
		urlMeth.inputpassword = $("#inputpassword").val();
		urlMeth.inputpass = $("#inputpass").val();
		urlMeth.unit = $("#unit").val();
		urlMeth.telephone = $("#telephone").val();
		if(urlMeth.nickname != ""&& urlMeth.inputMail != "" && urlMeth.inputpassword != "" 
			&& urlMeth.inputpass != "" && urlMeth.unit != "" && urlMeth.telephone != "")
		{
			if(!reg.test(urlMeth.inputMail)){
				alert("邮箱格式不正确");
			}
			else
				{
					if(urlMeth.inputpassword != urlMeth.inputpass){
						alert("密码和确认密码不一致");
					}
					else{
						urlMeth.register();
					}
				}
			
		}
		else{
			alert("请完善信息");
		}
	});
	module.exports = urlMeth;
});