define(function (require, exports, module) {
	require('jquery');
	 var pubMeth = {
	 	    course:[],
	 	    courseName:[],
			knowName:'',
			content :'',
			courseNameId :'',
			knowNameId : '',
			rowsnum : '',
			row:'',
			sumList :[],
		 	alertInfo:function(className,info){
				    if($(".tip").text().trim()==""){
	     				$(".tip").html(' <div class="alert  '+className+'"  id="tip">'+
	  			  							   '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
	  			  							   '<strong>'+info+'</strong></div>');
	     				}else{
						$("#tip").removeClass();
						$("#tip").addClass("alert " +className);
	     				$("strong").text(info);
	     				}
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
			legTimeRange:function(startTime,endTime){
				var stadate = startTime.split(" ");
				var enddate = endTime.split(" ");
				var stayear =stadate[0].split('-');
				var endyear = enddate[0].split('-');
				var stadate = stadate[1].split(':');
				var enddate = enddate[1].split(':');
				
				//年份更大，同年月份更大，同年同月日更大，同年同月同日时更大，同年同月同日同时分更大，同年同月同日同时同分秒更大
				if(stayear[0]>endyear[0] || stayear[0]== endyear[0]&&stayear[1]>endyear[1] ||
				stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]>endyear[2] ||
				stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]==endyear[2]&&stadate[0]>enddate[0]||
				stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]==endyear[2]&&stadate[0]==enddate[0]&&stadate[1]>enddate[1]||
				stayear[0]== endyear[0]&&stayear[1]==endyear[1]&& stayear[2]==endyear[2]&&stadate[0]==enddate[0]&&stadate[1]==enddate[1]&&stadate[2]>enddate[2]){
					return false;
				}
				return true;
			},
			serCourse:function(){
					$.ajax({
						type:"get",
						content:"application/x-www-form-urlencoded;charset=UTF-8",
						url:"../problem/selectKnowLedge",
						dataType : 'json',
						async : false,
						success:function( result ){
							console.log(result);
							var length = result.total;
							var currId = "";
							var flag = 0;
							$(".courseName").empty();
							$(".courseName").append("<option>课程</option>");
							for(var i = 0 ; i < length ; i++){
								if(result.data[i].isCourse){
									pubMeth.courseName.push(result.data[i]);
									$(".courseName").append("<option value="+result.data[i].knowId+">"+result.data[i].knowName+"</option>");
								}else{
									pubMeth.course.push(result.data[i]);
									pubMeth.sumList.push(result.sumList[i]);
								}
							}
							pubMeth.onchange();
						}
					});
			},
			onchange:function(){
				$(".knowName").html("");
				var parentId = $(".courseName option:selected").val();
				if(parentId =="课程"){
					$(".knowName").html("<option>知识点</option>");
					return ;
				}
				for(var i = 0; i < pubMeth.course.length ; i++){
					if(pubMeth.course[i].parentId == parentId){
						$(".knowName").append("<option value="+pubMeth.course[i].knowId+">"+pubMeth.course[i].knowName+"</option>");
					}
				}
			},
			getRowsnum:function(){
				$.ajax({
					type : "get",
					content : "application/x-www-form-urlencoded;charset=UTF-8",
					url:"../system/selectSiteInfo",
					dataType : 'json',
					async : false,
					data :{
						
					},
 					success:function(result){ 
 						if(result.status=="1"){
 							var length = result.data.length;
 							for(var i = 0;i < length;i++){
 								if(result.data[i].name == "rows"){
 									pubMeth.rowsnum = result.data[i].value;
 								}
 							}
 						}
 					},
			})
			},
		 };
		 $(".courseName").change(function(){
				pubMeth.onchange();
			});
		$(".check_list").click(function(){
			if(this.checked){    
			 	$("#listInfo").find("input[type='checkbox']").prop("checked", true);      
		    }else{    
		       $("#listInfo").find("input[type='checkbox']").prop("checked", false);
		    }    
		});
		module.exports = pubMeth;
});