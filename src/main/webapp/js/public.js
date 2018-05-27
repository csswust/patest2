var pubMeth = {
    course: [],
    courseName: [],
    knowName: '',
    content: '',
    courseNameId: '',
    knowNameId: '',
    rowsnum: '',
    row: '',
    sumList: [],
    alertInfo: function (className, info) {
        if ($(".tip").text().trim() == "") {
            $(".tip").html(' <div class="alert  ' + className + '"  style="margin-top: 10px;" id="tip">' +
                '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                '<strong>' + info + '</strong></div>');
        } else {
            $("#tip").removeClass();
            $("#tip").addClass("alert " + className);
            $(".tip > div > strong").text(info);
        }
    },
    getQueryObject: function () {
        var url = window.location.href;
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
    legTimeRange: function (startTime, endTime) {
        var stadate = startTime.split(" ");
        var enddate = endTime.split(" ");
        var stayear = stadate[0].split('-');
        var endyear = enddate[0].split('-');
        var stadate = stadate[1].split(':');
        var enddate = enddate[1].split(':');

        //年份更大，同年月份更大，同年同月日更大，同年同月同日时更大，同年同月同日同时分更大，同年同月同日同时同分秒更大
        if (stayear[0] > endyear[0] || stayear[0] == endyear[0] && stayear[1] > endyear[1] ||
            stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] > endyear[2] ||
            stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] == endyear[2] && stadate[0] > enddate[0] ||
            stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] == endyear[2] && stadate[0] == enddate[0] && stadate[1] > enddate[1] ||
            stayear[0] == endyear[0] && stayear[1] == endyear[1] && stayear[2] == endyear[2] && stadate[0] == enddate[0] && stadate[1] == enddate[1] && stadate[2] > enddate[2]) {
            return false;
        }
        return true;
    },
    serCourse: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../courseInfo/selectByCondition",
            dataType: 'json',
            async: false,
            success: function (result) {
                var length = result.total;
                $(".courseName").empty();
                $(".courseName").append("<option>课程</option>");
                for (var i = 0; i < length; i++) {
                    pubMeth.courseName.push(result.list[i]);
                    $(".courseName").append("<option value=" + result.list[i].couId + ">" + result.list[i].courseName + "</option>");
                }
                pubMeth.onchange();
            }
        });
    },
    getKnowledgeInfo: function (courseId) {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../knowledgeInfo/selectByCondition",
            dataType: 'json',
            async: false,
            data: {
                containSum: false,
                courseId: courseId
            },
            success: function (result) {
                pubMeth.course = result.knowledgeInfoList;
            }
        });
    },
    onchange: function () {
        $(".knowName").html("");
        var parentId = $(".courseName option:selected").val();
        if (parentId == "课程") {
            $(".knowName").html("<option>知识点</option>");
            return;
        }
        if (parentId) {
            pubMeth.getKnowledgeInfo(parentId);
            for (var i = 0; i < pubMeth.course.length; i++) {
                $(".knowName").append("<option value=" + pubMeth.course[i].knowId + ">" + pubMeth.course[i].knowName + "</option>");
            }
        }
    },
    getRowsnum: function () {
        $.ajax({
            type: "get",
            content: "application/x-www-form-urlencoded;charset=UTF-8",
            url: "../siteInfo/selectByName",
            dataType: 'json',
            async: false,
            data: {
                name: "rows"
            },
            success: function (result) {
                pubMeth.rowsnum = result.value;
            }
        });
    }
};
$(".courseName").change(function () {
    pubMeth.onchange();
});
$(".check_list").click(function () {
    if (this.checked) {
        $("#listInfo").find("input[type='checkbox']").prop("checked", true);
    } else {
        $("#listInfo").find("input[type='checkbox']").prop("checked", false);
    }
});