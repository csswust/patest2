$(".examMana").next(".treeview-menu").toggle("slow");
$(".examMana").addClass("leftActive");
$(".similarityInfo").css("color", "white");
var program = {
    count: '',
    page: "1",
    stunum: '',
    examId: '',
    seardu: '',
    content: '',
    html: '',
    submitSimilarityList: null,
    submitInfoList1: null,
    submitInfoList2: null,
    userInfoList1: null,
    userInfoList2: null,
    userProfileList1: null,
    userProfileList2: null,
    showSimilarity: function () {
        var length = program.submitSimilarityList.length;
        program.html = "";
        for (var i = 0; i < length; i++) {
            program.html += '<tr>'
                + '<td><input type="checkbox" name="title"/></td>'
                + '<td>' + program.submitSimilarityList[i].subSimId + '</td>'
                + '<td>' + program.submitSimilarityList[i].submitId1 + '+' + program.userInfoList1[i].username + '+' + program.userProfileList1[i].realName + '</td>'
                + '<td>' + program.submitSimilarityList[i].submitId2 + '+' + program.userInfoList2[i].username + '+' + program.userProfileList2[i].realName + '</td>'
                + '<td>' + program.submitSimilarityList[i].similarity + '</td>'
                + '</tr>';

        }
    },
    searchbyid: function () {
        $.ajax({
            type: "post",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../submitSimilarity/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                userName: program.stunum,
                lowerLimit: program.seardu,
                examId: program.examId,
                page: program.page,
                rows: pubMeth.rowsnum
            },
            success: function (result) {
                console.log(result);
                program.count = result.total;
                program.submitSimilarityList = result.submitSimilarityList;
                program.submitInfoList1 = result.submitInfoList1;
                program.submitInfoList2 = result.submitInfoList2;
                program.userInfoList1 = result.userInfoList1;
                program.userInfoList2 = result.userInfoList2;
                program.userProfileList1 = result.userProfileList1;
                program.userProfileList2 = result.userProfileList2;
                /*program.userProfileList = result.userProfileList;*/
                program.showSimilarity();
                $("#listInfo").html("");
                $("#listInfo").append(program.html);
            },
            error: function () {
                console.log(0);
            }
        });
    },
};
pubMeth.getRowsnum("rowsnum");
var par = pubMeth.getQueryObject();
program.examId = par.id;
program.searchbyid();
$(".search").click(function () {
    program.page = 1;
    $('#pagination').jqPaginator('option', {
        currentPage: program.page
    });
    program.stunum = $(".searTitle").val();
    program.seardu = $(".seardu ").val();
    program.searchbyid();
    $(".countnum").html(program.count);
    $('#pagination').jqPaginator('option', {
        totalCounts: program.count
    });
});

if (program.count > 0) {
    $(".countnum").html(program.count);
    $.jqPaginator('#pagination', {
        totalCounts: program.count,
        visiblePages: 5,
        currentPage: 1,
        pageSize: parseInt(pubMeth.rowsnum),
        first: '<li class="first"><a href="javascript:;">首页</a></li>',
        last: '<li class="last"><a href="javascript:;">尾页</a></li>',
        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
        onPageChange: function (num, type) {
            if (type == 'init') {
                return;
            }
            program.page = num;
            program.searchbyid();
        }
    });
} else {
    $(".pagenum").css("display", "none");
}