$(function() {
	$('#username').focus();
	var program = {
		username: "",
		password: "",
		login: function() {
			console.log(program.username);
			$.ajax({
				type: "POST",
				content : "application/x-www-form-urlencoded;charset=UTF-8",
				url:"../sysUser/login",
				dataType: 'json',
				async: false,
				data:{
					sysusrName: program.username,
					password: program.password
				},
				success: function(result) {
					console.log(result);
					if (result.status == "0") {
						alert("登录失败!");
					}	
					else if (result.status=="1" ) {
						window.location.href = "homepaged.html";

					}
				
				},
				error: function() {
					alert("登录失败!");
					$("#username").val("");
					$("#password").val("");
				}
			});
		},
		judge:function(){
			program.username = $("#username").val();
			program.password = $("#password").val();
			if (program.username != "" && program.password != "") 
			{
				program.login();
			} 
			else 
			{
				alert("请输入账户和密码!");
			}
		},
	};
	$("#btnlogin").click(function() {
		program.judge();
	});
	$("body").keydown(function() {
		if (event.keyCode == "13") {        //keyCode=13是回车键
	      program.judge();
		}
	});
});