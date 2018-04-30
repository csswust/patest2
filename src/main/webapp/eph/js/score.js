var score = {
    usernumber: '',
    infohtml: '',
    lookhtml: '',
    lookhtmls: '',
    init: function () {
        commonet.init(); // 公共模块初始化
        $(".score").addClass("onet");

        var str = window.location.href;
        $("#serchScore").click(function () {
            score.judgeselect();
            /*生成二维码*/
            $("#qrcode").html("");
            $("#qrcode").qrcode({
                render: "canvas",
                width: 150,
                height: 150,
                foreground: "#3c8dbc",
                background: "#FFF",
                text: str
            });
        });

        /*打印成绩表*/
        $("#print").click(function () {
            //当前时间
            var time = new Date();
            var nowtime = time.toLocaleString();
            $(".nowtime").html(nowtime);

            var img = document.getElementById("image");
            var canvas = document.getElementsByTagName("canvas")[0];  /// get canvas element
            img.src = canvas.toDataURL();
            $(".my_show").jqprint();
        });
    },
    /*查询成绩单*/
    selectScore: function () {
        patest.request({
            url: "../ep/selectExamGradeByNumber"
        }, {
            number: score.usernumber
        }, function (result) {
            score.usernumber = $("#inputnumber").val();
            if (result.status >= 1) {
                var banklist = '<tr>'
                    + '<th>考试题目</th>'
                    + '<th>准考证号</th>'
                    + '<th>考试成绩</th>'
                    + '<th>开始时间</th>'
                    + '<th>结束时间</th>'
                    + '<tr>';
                $("#listhead").html(banklist);
                score.infohtml = " ";
                score.lookhtml = "";
                result = result.data;

                for (var i = 0; i < result.userInfoList.length; i++) {
                    for (var j = 0; j < result.examPaperLists[i].length; j++) {
                        score.lookhtml += '<tr>'
                            + '<td >' + result.ExamInfoLists[i][j].title + '</td>'
                            + '<td >' + result.userInfoList[i].username + '</td>'
                            + '<td >' + result.examPaperLists[i][j].score + '</td>'
                            + '<td >' + result.ExamInfoLists[i][j].startTime + '</td>'
                            + '<td >' + result.ExamInfoLists[i][j].endTime + '</td>'
                            + '</tr>';
                    }
                }
                $('#serchlist').html(score.lookhtml);
                score.infohtml += '<form class="form-horizontal"><div class="form-group"><label class="col-md-6 control-label">姓名：</label>'
                    + '<div class="col-md-6 pname" style="margin-top:8px;">' + result.userProfile.realName + '</div></div>'
                    + '<div class="form-group"><label class="col-md-6 control-label">学号：</label>'
                    + '<div class="col-md-6 pcon" style="margin-top:8px;">' + score.usernumber + '</div></div></form>';
                $('#infosysuser').html(score.infohtml);
            }
            else if (result.status === 0) {
                score.lookhtmls = " ";
                score.lookhtmls += '<p style="margin-top:20px;color:red;">' + result.desc + '    ' + '!</p>';
                $('#infosysuser').html(score.lookhtmls);
            } else {
                alert(result.desc);
            }
        });
    },
    judgeselect: function () {
        score.usernumber = $("#inputnumber").val();
        if (score.usernumber != "") {
            $('#serchlist').empty();
            $('#infosysuser').empty();
            score.selectScore();
        }
        else {
            alert("请输入学号!");
        }
    }
};
