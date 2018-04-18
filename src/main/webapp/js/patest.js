/**
 * Created by 972536780 on 2018/3/27.
 */
var patest = {
    courseName: [],
    request: function (requst, data, call, errCall) {
        if (!requst.type) requst.type = "post";
        if (!requst.content) requst.content = "application/x-www-form-urlencoded;charset=UTF-8";
        if (!requst.dataType) requst.dataType = "json";
        if (requst.async === null || requst.async === undefined) requst.async = false;
        if (!data) data = {};
        $.ajax({
            type: requst.type,
            content: requst.content,
            url: requst.url,
            dataType: requst.dataType,
            async: requst.async,
            data: data,
            success: function (result) {
                console.log(result);
                try {
                    call(result);
                } catch (e) {
                    console.log(e);
                }
            },
            error: function (error) {
                console.log(error);
                try {
                    errCall(error);
                } catch (e) {
                    console.log(e);
                }
            }
        });
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
    getRowsnum: function () {
        patest.request({
            url: "../siteInfo/selectByName"
        }, {
            name: "rows"
        }, function (result) {
            patest.rowsnum = result.value;
        });
    },
    legTimeRange: function (startTime, endTime) {
        var stadate = startTime.split(" ");
        var enddate = endTime.split(" ");
        var stayear = stadate[0].split('-');
        var endyear = enddate[0].split('-');
        var stadate = stadate[1].split(':');
        var enddate = enddate[1].split(':');
        if (stayear[0] > endyear[0] || stayear[0] === endyear[0] && stayear[1] > endyear[1] ||
            stayear[0] === endyear[0] && stayear[1] === endyear[1] && stayear[2] > endyear[2] ||
            stayear[0] === endyear[0] && stayear[1] === endyear[1] && stayear[2] === endyear[2] && stadate[0] > enddate[0] ||
            stayear[0] === endyear[0] && stayear[1] === endyear[1] && stayear[2] === endyear[2] && stadate[0] === enddate[0] && stadate[1] > enddate[1] ||
            stayear[0] === endyear[0] && stayear[1] === endyear[1] && stayear[2] === endyear[2] && stadate[0] === enddate[0] && stadate[1] === enddate[1] && stadate[2] > enddate[2]) {
            return false;
        }
        return true;
    },
    alertInfo: function (className, info) {
        if ($(".tip").text().trim() === "") {
            $(".tip").html(' <div class="alert  ' + className + '"  style="margin-top: 10px;" id="tip">' +
                '<a href="#" class="close" data-dismiss="alert">&times;</a>' +
                '<strong>' + info + '</strong></div>');
        } else {
            $("#tip").removeClass();
            $("#tip").addClass("alert " + className);
            $("strong").text(info);
        }
    },
    serCourse: function () {
        patest.request({
            url: "../courseInfo/selectByCondition"
        }, null, function (result) {
            var length = result.total;
            $(".courseName").empty();
            $(".courseName").append("<option>课程</option>");
            for (var i = 0; i < length; i++) {
                patest.courseName.push(result.list[i]);
                $(".courseName").append("<option value=" + result.list[i].couId + ">" + result.list[i].courseName + "</option>");
            }
            $(".courseName").change(function () {
                patest.onchange();
            });
        });
    },
    getKnowledgeInfo: function (courseId) {
        patest.request({
            url: "../knowledgeInfo/selectByCondition"
        }, {
            containSum: false,
            courseId: courseId
        }, function (result) {
            patest.course = result.knowledgeInfoList;
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
            patest.getKnowledgeInfo(parentId);
            for (var i = 0; i < patest.course.length; i++) {
                $(".knowName").append("<option value=" + patest.course[i].knowId + ">" + patest.course[i].knowName + "</option>");
            }
        }
    }
};
