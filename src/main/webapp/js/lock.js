define(function (require, exports, module) {
	require('jquery');
	require('../js/common.js');
	require('bootstrap');
	require('paginator');
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    $(".check_list").click(function(){
		if(this.checked){    
		 	$("#listinfo").find("input[type='checkbox']").prop("checked", true);      
	    }else{    
	       $("#listinfo").find("input[type='checkbox']").prop("checked", false);
	    }    
	});
	var program = {
		ids:'',
		page:"1",
		selectLock:function(){
			$("#listinfo").empty();
			$.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../user/selectLockedUser",
     					dataType : 'json',
     					async : false,
     					data:{
     						page :program.page,
     						rows :pubMeth.rowsnum,
     					},
     					success:function(result){ 
     						console.log(result);
     						var rander = template("getcontent",result);
     						program.count = result.total;
     						$("#listinfo").append(rander);
     					},
     					error:function(){
     						alert("error");
     					}
     			});
		},
		unlock:function(){
				$.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../user/releaseLockByIds",
     					dataType : 'json',
     					async : false,
     					data:{
     						ids : program.ids
     					},
     					success:function(result){ 
     						var rander = template("getcontent",result);
     						$("#listinfo").append(rander);
     						if(result.status=="0"){
     						pubMeth.alertInfo("alert-danger","解锁失败！");
     						}else if(result.status=="1"){
     						pubMeth.alertInfo("alert-success","解锁成功！");
     						}
     					}
     			});
		}
	};
	pubMeth.getRowsnum("rowsnum");
	program.selectLock();
	$(".lock").click(function(){
		var valArr = new Array;
		$(":checkbox[name='title']:checked").each(function(i) {
			valArr[i] = $(this).val();
		});
		program.ids= valArr.join(',');// 转换为逗号隔开的字符串
		console.log(valArr);
		if(program.ids!=""){
		    program.unlock();
		    window.location.reload();
		}else{
			pubMeth.alertInfo("alert-info","请先勾选删除项！");
		}
		
	});
	if(program.count > 0) {
		$(".countnum").html(program.count);
		$.jqPaginator('#pagination', {
			totalCounts : program.count,
			visiblePages : 5,
			currentPage : 1,
			pageSize : parseInt(pubMeth.rowsnum),
			first : '<li class="first"><a href="javascript:;">首页</a></li>',
			last : '<li class="last"><a href="javascript:;">尾页</a></li>',
			page : '<li class="page"><a href="javascript:;">{{page}}</a></li>',
			onPageChange : function(num, type) {
				if (type == 'init') {
					return;
				}
				program.page = num;
				program.selectLock();
			}
		});
	} else {
		$(".pagenum").css("display","none");
	}
});