define(function (require, exports, module) {
	require('jquery');
	require('jCookie');
	require('../js/commonet.js');
	require('bootstrap');
	require('paginator');
	
	$(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".examList").css("color","white");
	$(".perinfo").addClass("onet");
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    
	require('datetimepicker');
	 $(".form_datetime").datetimepicker({
	 		format: 'yyyy-mm-dd hh:ii:ss'
	 });
	var program ={
			sysuserid:'',
			testname:'',
			mail:'',
			address:'',
			telephone:'',
			realname:'',
			selectallbill:function(){
				$.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../sysUser/selectById",
					dataType : 'json',
					async : false,
					data:{
						sysusrId:program.sysuserid,
					},
					success:function(result){ 
				        console.log(result);
				        program.testname = result.data.sysusrName;
				        program.mail = result.data.eMail;
				        program.address = result.data.unit;
				        program.telephone = result.data.phone;
				        program.realname = result.data.realName;
					}
				});
			},
			updateallbill:function(){
				$.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../sysUser/updateSysUser",
					dataType : 'json',
					async : false,
					data:{
						sysusrId:program.sysuserid,
						sysusrName:program.testname,
						eMail:program.mail,
						unit:program.address,
						phone:program.telephone,
						realName:program.realname,
					},
					success:function(result){ 
				        console.log(result);
				        if(result.status == 1){
				        	pubMeth.alertInfo("alert-success","保存成功");
				        	location.reload(false);
				        	program.selectallbill();
				        	program.setInfo();
				        }else {
				        	pubMeth.alertInfo("alert-danger","保存失败");
				        }
					}
				});
			},
			
			setInfo:function(){
				if(program.realname == null){
					program.realname = "数据为空";
				}
				$(".testname").val(program.testname);
				$(".mail").val(program.mail);
				$(".address").val(program.address);
				$(".telephone").val(program.telephone);
				$(".realname").val(program.realname);
			},
	
	};
	program.sysuserid = $.cookie("sysuserId");
	program.selectallbill();
	program.setInfo();
	$(".saveInfo").click(function(){
		$('#perinfo').modal({
	 		 backdrop : 'static'
		});	
		$(".mtestname").val(program.testname);
		$(".mmail").val(program.mail);
		$(".maddress").val(program.address);
		$(".mtelephone").val(program.telephone);
		$(".mrealname").val(program.realname);
	});
	$(".saveper").click(function(){
		 program.testname = $(".mtestname").val();
		 program.mail = $(".mmail").val();
		 program.address = $(".maddress").val();
		 program.telephone = $(".mtelephone").val();
		 program.realname = $(".mrealname").val();
		 program.updateallbill();	
	});
});