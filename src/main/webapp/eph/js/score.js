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
        });
        //当前时间
        var time = new Date();
        var nowtime = time.toLocaleString();
        $(".nowtime").html(nowtime);
        /*生成二维码*/
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
    },
    /*查询成绩单*/
    selectScore: function () {
        patest.request({
            url: "../ep/selectExamGradeByUserName"
        }, {
            username: score.usernumber
        }, function (result) {
            score.usernumber = $("#inputnumber").val();
            var i = 0;
            if (result.status === 1) {
                var banklist = '<tr>'
                    + '<th>考试题目</th>'
                    + '<th>考试成绩</th>'
                    + '<th>开始时间</th>'
                    + '<th>结束时间</th>'
                    + '<tr>';
                $("#listhead").html(banklist);
                score.infohtml = " ";
                score.lookhtml = "";
                result = result.data;
                for (i = 0; i < result.examInfoList.length; i++) {
                    score.lookhtml += '<tr>'
                        + '<td >'
                        + result.examInfoList[i].title
                        + '</td>'
                        + '<td >'
                        + result.examPaperList[i].score
                        + '</td>'
                        + '<td >'
                        + result.examInfoList[i].startTime
                        + '</td>'
                        + '<td >'
                        + result.examInfoList[i].endTime
                        + '</td>'
                        + '</tr>';
                }
                $('#serchlist').html(score.lookhtml);
                console.log(result.userProfile.realName);
                score.infohtml += '<form class="form-horizontal"><div class="form-group"><label class="col-md-6 control-label">姓名：</label>'
                    + '<div class="col-md-6 pname" style="margin-top:8px;">' + result.userProfile.realName + '</div></div>'
                    + '<div class="form-group"><label class="col-md-6 control-label">准考证号：</label>'
                    + '<div class="col-md-6 pcon" style="margin-top:8px;">' + result.userInfo.username + '</div></div></form>'
                $('#infosysuser').html(score.infohtml);

            }
            else if (result.status === 0) {
                score.lookhtmls = " ";
                score.lookhtmls += '<p style="margin-top:20px;color:red;">' + result.info + '    ' + '!</p>';
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
            alert("请输入准考证号!");
        }
    }
};
