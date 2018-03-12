define(function (require, exports, module) {
	require('jquery');
	require('bootstrap');
	require('ueditor_config');
	require('ueditor');
	require('../js/common.js');
	
	$(".sysMana").next(".treeview-menu").toggle("slow");
	$(".sysMana").addClass("leftActive");
	$(".epstemset").css("color", "white");
	
    var pubMeth = require('../js/public.js');
	$(".footer p").css('display','block');
	$(".footer p").hover(function(){
		$(this).css({"color":"#333","cursor":"default"});
	});

	var program = {
			principal : '',
			phone : '',
			email : '',
			severpeo : '',
			severway : '',
			teaminpro : '',
			address : '',
			epInfo : '',
			epId : '',
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
 		                	program.epInfo = result.data;
 		                	program.epId = result.data.epId;
 		                }		   
 					}
 			});
			},
			setEpinfo : function(){
				$(".principal").val(program.epInfo.principal);
				$(".phone").val(program.epInfo.telephone);
				$(".email").val(program.epInfo.eMail);
			    $(".address").val(program.epInfo.address);
			},
			updateEpinfo : function(){
				console.log(program.epId);
				$.ajax({
 					type : "get",
 					content : "application/x-www-form-urlencoded;charset=UTF-8",
 					url:"../epInfo/updateEpInfo",
 					dataType : 'json',
 					async : false,
 					data :{
 						epId:program.epId,
 						address:program.address,
 						principal:program.principal,
 						servConcern:program.severway,
 						servPeople:program.severpeo,
 						teamInfo:program.teaminpro,
 						telephone:program.phone,
 						eMail:program.email,
 					},
 					success:function(result){ 
 		                console.log(result);
 		                if(result.status == 1){
 		                	pubMeth.alertInfo("alert-success","修改成功！");
 		                	window.location.reload();
 		                }else{
 		                	pubMeth.alertInfo("alert-danger","修改失败！");
 		                }		        
 					},
 					error:function(){
 						pubMeth.alertInfo("alert-danger","请求失败！");
 					},
 			});
			}
			
	};
	var  uep = UE.getEditor('perdata', {
		 //  initialFrameWidth:870 ,//初始化编辑器宽度,默认1000
	       initialFrameHeight:200  //初始化编辑器高度,默认320
		});
	var  uew = UE.getEditor('waydata', {
		 //  initialFrameWidth:870 ,//初始化编辑器宽度,默认1000
	       initialFrameHeight:200  //初始化编辑器高度,默认320
		});
	var uet = UE.getEditor('teamdata',{
	    //   initialFrameWidth:870 ,//初始化编辑器宽度,默认1000
	       initialFrameHeight:200  //初始化编辑器高度,默认320
	});
	$(".saveper").click(function(){	
		uep.ready(function(){
			program.severpeo = uep.getContent();
		});
		$("#settingperdata").modal("hide");
	});
	$(".saveway").click(function(){
		
		uew.ready(function(){
			program.severway = uew.getContent();
		});
		$("#settingwaydata").modal("hide");
	});
	$(".saveteam").click(function(){
		uet.ready(function(){
			program.teaminpro = uet.getContent();
		});
		$("#settingteamdata").modal("hide");
	});
	$(".severpeo").click(function(){
		$("#settingperdata").modal();
		uep.setContent(program.epInfo.servPeople);
	});
	$(".severway").click(function(){
		$("#settingwaydata").modal();
		uew.setContent(program.epInfo.servConcern);
	});
	$(".teaminpro").click(function(){
		$("#settingteamdata").modal();
		uet.setContent(program.epInfo.teamInfo);
	});
	program.selectEpinfo();
    program.setEpinfo();
	$(".savedata").click(function(){
		program.principal = $(".principal").val();
		program.phone = $(".phone").val();
		program.email = $(".email").val();
		program.address = $(".address").val();
		if(program.principal != null && program.phone != null && program.email != null
				&&program.severpeo != null && program.severway != null && program.teaminpro != null
				&&program.address != null){
			program.updateEpinfo();
		}
		
	});
});