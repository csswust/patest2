define(function (require, exports, module) {
	require('jquery');
	require('../js/common.js');
	require('bootstrap');
	require('paginator');
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
	var program = {
		page:"1",
		count:'',
		selectOnline:function(){
			$("#listinfo").empty();
			$.ajax({
     					type : "get",
     					content : "application/x-www-form-urlencoded;charset=UTF-8",
     					url:"../user/selectOnline",
     					dataType : 'json',
     					async : false,
     					data:{
     						page :program.page,
     						rows :pubMeth.rowsnum,
     					},
     					success:function(result){ 
     					console.log(result);
     					program.count = result.total;
     					var iplength = result.iPList.length;
     					var userlength = result.userinfoList.length;
     					for(var i = 0;i<iplength;i++){
     						var username = result.userinfoList[i].username;
     						var realName = result.userProfileList[i].realName;
     						var studentNum= result.userProfileList[i].studentNumber;
     						var classroom = result.userinfoList[i].classroom;
     						var className = result.userProfileList[i].className;
     						var ip = result.iPList[i];
     						if(username==null ||username=="undefined"){
     							username="";
     						}
     						if(realName==null ||realName=="undefined"){
     							realName="";
     						}
     						if(studentNum==null ||studentNum=="undefined"){
     							studentNum="";
     						}
     						if(classroom==null ||classroom=="undefined"){
     							classroom="";
     						}
     						if(className==null ||className=="undefined"){
     							className="";
     						}
     						if(ip==null ||ip=="undefined"){
     							ip="";
     						}
     						$("#listinfo").append('<tr><td>'+username+'</td>' +
     												  '<td>'+realName+'</td><td>'+studentNum+'</td>'+'<td>'+className+'</td>'+
     								                  '<td>'+classroom+'</td><td>'+ip+'</td></tr>');
     					}
     					},
     					error:function(){
     						alert("111");
     					}
     			});
		}
	};
	pubMeth.getRowsnum("rowsnum");
	program.selectOnline();
	console.log(program.count);
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
				program.selectOnline();
			}
		});
	} else {
		$(".pagenum").css("display","none");
	}

});