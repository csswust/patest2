define(function (require, exports, module) {
	    require('jquery');
	    require('jCookie');
		require('bootstrap');
		require('../js/loginreg.js');
		require('paginator');
		$(".homepage").addClass("onet");	
    
	 var urlMeth = require('../js/loginreg.js');
	 var program ={
			 epnoId:'',
			 /**
				 * 查询某个公告
				 */
				selectId:function(){
					$.ajax({
						type : "get",
						content : "application/x-www-form-urlencoded;charset=UTF-8",
						url : "../epNotice/selecByCondition",
						dataType : 'json',
						async : false,
						data : {
							epnoId :program.epnoId,			
						},
						success : function(result) {
							console.log(result);	
							if(result.status == 1){
								$(".title").html(result.list[0].title);
								$(".content").html(result.list[0].content);
							}else{
								pubMeth.alertInfo("alert-danger","查询错误");
							}					
						},
						error:function(){
							  pubMeth.alertInfo("alert-danger","请求错误");
						}
					});
				},
	    };
	 var par = urlMeth.getQueryObject();
     console.log(par.epid);
     program.epnoId = par.epid;
     program.selectId();
});
