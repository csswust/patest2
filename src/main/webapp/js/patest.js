/**
 * Created by 972536780 on 2018/3/27.
 */
var patest = {
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
    }
};
