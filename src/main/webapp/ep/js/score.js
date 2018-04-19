define(function (require, exports, module) {
    require('jquery');
    require('jCookie');
    require('jMigrate');
    require('jQrcode');
    require('jQprint');
    require('bootstrap');
    require('paginator');
    require('./loginreg.js');
    var program = {
        usernumber: '',
        infohtml: '',
        lookhtml: '',
        lookhtmls: '',
        /*查询成绩单*/
        selectScore: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../sysUser/selectExamGradeByUserName",
                dataType: 'json',
                async: false,
                data: {
                    userName: program.usernumber,
                },
                success: function (result) {
                    console.log(result);
                    program.usernumber = $("#inputnumber").val();
                    var i = 0;
                    if (result.status == "1") {
                        var banklist = '<tr>'
                            + '<th>考试题目</th>'
                            + '<th>考试成绩</th>'
                            + '<th>开始时间</th>'
                            + '<th>结束时间</th>'
                            + '<tr>';
                        $("#listhead").html(banklist);
                        program.infohtml = " ";
                        program.lookhtml = "";
                        for (i = 0; i < result.examList.length; i++) {
                            program.lookhtml += '<tr>'
                                + '<td >'
                                + result.examList[i].title
                                + '</td>'
                                + '<td >'
                                + result.scoreList[0]
                                + '</td>'
                                + '<td >'
                                + result.examList[i].startTime
                                + '</td>'
                                + '<td >'
                                + result.examList[i].endTime
                                + '</td>'
                                + '</tr>';
                        }
                        $('#serchlist').html(program.lookhtml);
                        console.log(result.userProfile.realName);
                        program.infohtml += '<form class="form-horizontal"><div class="form-group"><label class="col-md-6 control-label">姓名：</label>'
                            + '<div class="col-md-6 pname" style="margin-top:8px;">' + result.userProfile.realName + '</div></div>'
                            + '<div class="form-group"><label class="col-md-6 control-label">准考证号：</label>'
                            + '<div class="col-md-6 pcon" style="margin-top:8px;">' + result.userInfo.username + '</div></div></form>'
                        $('#infosysuser').html(program.infohtml);

                    }
                    else if (result.status == "0") {
                        program.lookhtmls = " ";
                        program.lookhtmls += '<p style="margin-top:20px;color:red;">' + result.info + '    ' + '!</p>';
                        $('#infosysuser').html(program.lookhtmls);
                    }
                },
                error: function () {
                    $("#inputnumber").val("");
                }
            });
        },
        judgeselect: function () {
            program.usernumber = $("#inputnumber").val();
            if (program.usernumber != "") {
                $('#serchlist').empty();
                $('#infosysuser').empty();
                program.selectScore();
            }
            else {
                alert("请输入准考证号!");
            }
        },
    };
    var str = window.location.href;
    $("#serchScore").click(function () {
        program.judgeselect();
    });
    //当前时间
    var time = new Date();
    var nowtime = time.toLocaleString();
    $(".nowtime").html(nowtime);
    /*生成二维码*/
    console.log(str);
    $("#qrcode").qrcode({
        render: "canvas",
        width: 64,
        height: 64,
        foreground: "#3c8dbc",
        background: "#FFF",
        text: str,
    });
    /*打印成绩表*/
    $("#print").click(function () {
        var img = document.getElementById("image");
        var canvas = document.getElementsByTagName("canvas")[0];  /// get canvas element
        img.src = canvas.toDataURL();
        $(".my_show").jqprint();
    });
});