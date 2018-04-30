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
            var examTitle = program.examInfoList[i].title;
            var examId = program.examInfoList[i].examId;
            var problemTitle = program.problemInfoList[i].title;
            var problemId = program.problemInfoList[i].probId;
            program.html += '<tr>'
                + '<td><input type="checkbox" name="title"/></td>'
                + '<td>' + program.submitSimilarityList[i].subSimId + '</td>'
                + '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + examTitle + '"><a href="editExam.html?Id=' + examId + '">' + examTitle + '(' + examId + ')' + '</a></td>'
                + '<td class="tdhidden" data-toggle="tooltip" data-placement="top" title="' + problemTitle + '"><a href="question.html?id=' + problemId + '">' + problemTitle + '(' + problemId + ')' + '</a></td>'
                + '<td>' + program.userInfoList1[i].username + '_' + program.userProfileList1[i].realName + '26626262626</td>'
                + '<td>' + program.userInfoList2[i].username + '_' + program.userProfileList2[i].realName + '266266262626</td>'
                + '<td>' + program.submitSimilarityList[i].similarity + '</td>'
                + '<td><button class="btn btn-success btn-xs diff" type="button" value=' + i + '>查看</button></td>'
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
                program.examInfoList = result.examInfoList;
                program.problemInfoList = result.problemInfoList;

                program.submitInfoList1 = result.submitInfoList1;
                program.submitInfoList2 = result.submitInfoList2;
                program.userInfoList1 = result.userInfoList1;
                program.userInfoList2 = result.userInfoList2;
                program.userProfileList1 = result.userProfileList1;
                program.userProfileList2 = result.userProfileList2;

                program.showSimilarity();
                $("#listInfo").html("");
                $("#listInfo").append(program.html);
            },
            error: function () {
                console.log(0);
            }
        });
    }
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
    program.examId = $(".searExamId").val();
    program.searchbyid();
    $(".countnum").html(program.count);
    $('#pagination').jqPaginator('option', {
        totalCounts: program.count
    });
});

$("#listInfo").on('click', '.diff', function () {
    var index = this.value;

    /*$('#diffCode').modal('shown.bs.modal');*/
    $('#diffCode').on('shown.bs.modal', function () {
        var value = program.submitInfoList1[index].source;
        var orig2 = program.submitInfoList2[index].source;
        var dv, highlight = true, connect = "align", collapse = false;
        var target = document.getElementById("view");
        target.innerHTML = "";
        dv = CodeMirror.MergeView(target, {
            value: value,
            origLeft: null,
            orig: orig2,
            lineNumbers: true,
            mode: "text/x-c++src",
            highlightDifferences: highlight,
            connect: connect,
            collapseIdentical: collapse
        });
    });
    $('#diffCode').modal();
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
function mergeViewHeight(mergeView) {
    function editorHeight(editor) {
        if (!editor) return 0;
        return editor.getScrollInfo().height;
    }

    return Math.max(editorHeight(mergeView.leftOriginal()),
        editorHeight(mergeView.editor()),
        editorHeight(mergeView.rightOriginal()));
}

function resize(mergeView) {
    var height = mergeViewHeight(mergeView);
    for (; ;) {
        if (mergeView.leftOriginal())
            mergeView.leftOriginal().setSize(null, height);
        mergeView.editor().setSize(null, height);
        if (mergeView.rightOriginal())
            mergeView.rightOriginal().setSize(null, height);

        var newHeight = mergeViewHeight(mergeView);
        if (newHeight >= height) break;
        else height = newHeight;
    }
    mergeView.wrap.style.height = height + "px";
}