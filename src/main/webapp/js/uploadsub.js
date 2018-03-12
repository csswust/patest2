define(function (require, exports, module) {
	require('jquery');
	require('../js/commonet.js');
	require('bootstrap');
	require('paginator');
	
	$(".examMana").next(".treeview-menu").toggle("slow");
	$(".examMana").addClass("leftActive");
	$(".examList").css("color","white");
	$(".uploadsub").addClass("onet");
	var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    
	require('datetimepicker');
	 $(".form_datetime").datetimepicker({
	 		format: 'yyyy-mm-dd hh:ii:ss'
	 });
	var program ={
	
	};
	
	 $.jqPaginator('#pagination', {
	    	    	totalCounts : program.count,
	    	        visiblePages: 5,
	    	        currentPage: 1,
	    	        pageSize: 20,
	    	        first: '<li class="first"><a href="javascript:;">首页</a></li>',
	    	        last: '<li class="last"><a href="javascript:;">尾页</a></li>',
	    	        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
	    	        onPageChange: function (num, type) {
	    	        	if(type == 'init') {return;}
	    	        	program.page = num;
	    		     	program.getExamInfo();
	    	        }
	 });
});