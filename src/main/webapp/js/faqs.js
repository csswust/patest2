define(function (require, exports, module) {
	require('jquery');
	require('bootstrap');
	var program = {
		faqs:'',
		index:'',
		role:'',
		getWebInfo:function(){
				$.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../system/selectSiteInfo",
     					dataType : 'json',
     					async : false,
     					success:function(result){ 
     						var length = result.data.length;
     						for(var i = 0;i < length;i++){
     							if(result.data[i].name.toLowerCase()=="faqs"){
     								program.faqs = result.data[i].value;
     								$(".content").html(program.faqs);
     							}else if(result.data[i].name.toLowerCase()=="index"){
     								program.index = result.data[i].value;
     								$(".main").html(program.index);
     							}
     						}
     					}
     			});
		},
		getCookie:function(objName){
		   var arrStr = document.cookie.split("; "); 
           for (var i = 0; i < arrStr.length; i++) { 
                 var temp = arrStr[i].split("="); 
                  if (temp[0] == objName)
                  	program.role = unescape(temp[1]);
      		 }
      		 if(program.role==""||program.role=="undefined"){
     				window.location.href="login.html";
     		}
		}
	};
	program.getCookie("role");
	console.log(program.role);
	if(program.role =="Admin"){
		require('../js/common.js');
	}else if(program.role == "Student"){
		require('../js/nav.js');
	}
	program.getWebInfo();
});