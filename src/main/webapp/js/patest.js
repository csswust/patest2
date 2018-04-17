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
    }
};
