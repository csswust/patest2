define(function (require, exports, module) {
	require('jquery');
	require('bootstrap');
	require('ueditor_config');
	require('ueditor');
	require('../js/common.js');
	$(".sysMana").next(".treeview-menu").toggle("slow");
	$(".sysMana").addClass("leftActive");
	$(".systemset").css("color","white");
	
    var pubMeth = require('../js/public.js')
	$(".footer p").css('display','block');
	$(".footer p").hover(function(){
		$(this).css({"color":"#333","cursor":"default"});
	});
			
	var program = {
		faqs:'',
		index:'',
		examNotes :'',
		dataName :'',
		data:[],
		rowsnum:'',
		getSiteInfo :function(){
			$.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../system/selectSiteInfo",
     					dataType : 'json',
     					async : false,
     					data :{
     						
     					},
     					success:function(result){ 
     						console.log(result);
     						if(result.status=="1"){
     							var length = result.data.length;
     							for(var i = 0;i < length;i++){
     								if(result.data[i].name=="FAQs"){
     									program.faqs = result.data[i].value;
     								}
     								if(result.data[i].name == "index"){
     									program.index = result.data[i].value;
     								}
     								if(result.data[i].name == "examNotes"){
     									program.examNotes = result.data[i].value;
     								}
     								if(result.data[i].name == "rows"){
     									$(".rowsNum").val(result.data[i].value);
     								}
     								if(result.data[i].name == "systemname"){
     									$(".systemName").val(result.data[i].value);
     								}
     								if(result.data[i].name == "footer"){
     									$(".footerName").val(result.data[i].value);
     								}
     								program.data[i] = {};
     								program.data[i].name = result.data[i].name;
     								program.data[i].stioId = result.data[i].stioId;
     								program.data[i].values = result.data[i].value;
     							}
     						}
     					}
     			});
		},
		update:function(stoIds,values){
			$.ajax({
					type : "post",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../system/updateSiteInfo",
					dataType : 'json',
					async : false,
					traditional: true,//传数组
					data:{
						stioIds : stoIds,
						values : values
					},
					success:function(result){ 
						console.log(result);
						if(result.status!="0"){
							pubMeth.alertInfo("alert-success","修改成功");
						}else{
							pubMeth.alertInfo("alert-warning","修改失败");
						}
					}
			});
		}
	};
	program.getSiteInfo();
	
	var  ue = UE.getEditor('data', {
  		  /* initialFrameWidth:868 ,//初始化编辑器宽度,默认1000
*/  	       initialFrameHeight:200  //初始化编辑器高度,默认320
  		});
	$(".index").click(function(){
		$('#settingdata').modal();
		$("#myModalLabel").text("首页数据");
		ue.ready(function() {
			ue.setContent(program.index);
		 });
		program.dataName = this.className ;
	});
	$(".faqs").click(function(){
		$('#settingdata').modal();
		$("#myModalLabel").text("FAQS数据");
		ue.ready(function() {
			ue.setContent(program.faqs);
		 });
		program.dataName = this.className ;
	});
	$(".examNotes").click(function(){
		$('#settingdata').modal();
		$("#myModalLabel").text("考试须知数据");
		ue.ready(function() {
			ue.setContent(program.examNotes);
		 });		
		program.dataName = this.className ;
	});
	$(".save").click(function(){
		if(program.dataName=="faqs"){
			ue.ready(function() {
				program.faqs = ue.getContent();
			 });
		}
		if(program.dataName=="index"){
			ue.ready(function() {
				program.index = ue.getContent();
			 });		   
		}
		if(program.dataName=="examNotes"){
			ue.ready(function() {
				program.examNotes = ue.getContent();
			 });	 
		}
		$('#settingdata').modal("hide");
	});
	$(".savedata").click(function(){
		var webName = $(".systemName").val();
		var copyright = $(".footerName").val();
		var rowsNum = $(".rowsNum").val();
		var stoIds = [],
			   values= [];
		for(var i = 0 ; i < program.data.length;i++){
			if(program.data[i].name =="FAQs"){
     				program.data[i].value = program.faqs;
     		}
			if(program.data[i].name =="examNotes"){
 				program.data[i].value = program.examNotes;
			}
     		if(program.data[i].name == "index"){
     				program.data[i].value = program.index;
     		}
     		if(program.data[i].name == "rows"){
     			   program.data[i].value = rowsNum;
     		}
     		if(program.data[i].name == "systemname"){
				  program.data[i].value = webName;
     		}
     		if(program.data[i].name == "footer"){
     			 program.data[i].value = copyright;
     		}
     		stoIds[i] = program.data[i].stioId;
     		values[i]= program.data[i].value;
		}
		console.log(stoIds,values);
		program.update(stoIds,values);
	})
});