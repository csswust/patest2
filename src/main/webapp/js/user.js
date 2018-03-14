define(function (require, exports, module) {
    require('jquery');
    require('../js/common.js');
    require('bootstrap');
    require('paginator');
    require('fileupload');
    var template = require('artTemplate');
    var pubMeth = require('../js/public.js');
    $(".userMana").next(".treeview-menu").toggle("slow");
    $(".userMana").addClass("leftActive");
    $(".userInfo").css("color", "white");

    var program = {
        page: '1',
        userId: '',
        className: '',
        realName: '',
        term: '',
        umid: '',
        mumid: '',
        studentNum: "",
        schoolName: '',
        dschoolName: '',
        majorName: '',
        school: [],
        major: [],
        students: [],
        html: '',
        searTitle: '',
        searnum: '',
        majorValue: '',
        count: '',
        /**
         * 展示表格
         */
        showProfile: function () {
            var length = program.students.data.length;
            program.html = "";
            for (var i = 0; i < length; i++) {
                if (program.students.data[i].schoolMajor.schoolName == null) {
                    program.students.data[i].schoolMajor.schoolName = "";
                }
                if (program.students.data[i].schoolMajor.majorName == null) {
                    program.students.data[i].schoolMajor.majorName = "";
                }
                if (program.students.data[i].term == null) {
                    program.students.data[i].term == " ";
                }
                program.html += '<tr>'
                    + '<td style="width:80px;"><input type="checkbox" value="' + program.students.data[i].usrpfId + '" name="title"/></td>'
                    + '<td>' + program.students.data[i].usrpfId + '</td>'
                    + '<td>' + program.students.data[i].studentNumber + '</td>'
                    + '<td>' + program.students.data[i].realName + '</td>'
                    + '<td>' + program.students.data[i].schoolMajor.schoolName + '</td>'
                    + '<td>' + program.students.data[i].schoolMajor.majorName + '</td>'
                    + '<td>' + program.students.data[i].className + '</td>'
                    + '<td>' + program.students.data[i].term + '</td>'
                    + '<td><a href="javascript:;"class="title" value="' + program.students.data[i].usrpfId + '" >修改</a></td>'
                    + '</tr>';
            }
        },
        /**
         * 查询用户信息
         */
        selectProfile: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../user/selectProfileandSchoolMajorBycondition",
                dataType: 'json',
                async: false,
                data: {
                    "umid": program.majorValue,
                    "realName": program.searTitle,
                    "studentNumber": program.searnum,
                    page: program.page,
                    rows: pubMeth.rowsnum,
                },
                success: function (result) {
                    console.log(result);
                    program.students = result;
                    program.count = result.total;
                    program.showProfile();
                    $("#listInfo").empty();
                    $("#listInfo").append(program.html);
                    $("#listInfo tr").each(function (i) {
                        $("td:last a.title", this).click(function () {
                            program.userId = $(this).attr("value");
                            $('#user').modal({
                                backdrop: 'static'
                            });
                            $(".schoolName").empty();
                            $(".knowName").empty();
                            program.selectById();

                        });
                    });
                }
            });
        },
        setValue: function () {
            $(".studentNum").val("");
            $(".realName").val("");
            $(".className").val("");
            $(".term").val("");
        },
        /**
         * 查询某个人的信息
         */
        selectById: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../user/selectProfile",
                dataType: 'json',
                async: false,
                data: {
                    usrpfId: program.userId
                },
                success: function (result) {
                    console.log(result);
                    program.mumid = result.data[0].umid;
                    program.getSchool();
                    for (var i = 0; i < program.school.length; i++) {
                        $(".schoolName").append("<option value=" + program.school[i].umid + ">" + program.school[i].schoolName + "</option>");
                    }
                    if (result.schoolMajor[0] != null) {
                        program.getMajorName(result.schoolMajor[0].schoolName);
                        for (var i = 0; i < program.major.length; i++) {
                            $(".knowName").append("<option value=" + program.major[i].umid + ">" + program.major[i].majorName + "</option>");
                        }
                    }
                    $(".schoolName option[value=" + result.schoolUmid[0] + "]").attr("selected", true);
                    $(".studentNum").val(result.data[0].studentNumber);
                    $(".realName").val(result.data[0].realName);
                    $(".className").val(result.data[0].className);
                    $(".term").val(result.data[0].term);
                    $(".knowName option[value=" + program.mumid + "]").attr("selected", true);
                }
            });
        },
        /**
         * 删除用户
         */
        deleteUser: function (vals) {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../user/deleteProfile",
                dataType: 'json',
                async: false,
                data: {
                    ids: vals
                },
                success: function (result) {
                    console.log(result);
                    if (result.status > 0) {
                        pubMeth.alertInfo("alert-success", "删除成功！");
                        program.selectProfile();
                    } else {
                        pubMeth.alertInfo("alert-danger", "删除失败！");
                    }
                }
            });
        },
        /**
         * 更新信息
         */
        updateUser: function () {
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../user/updateProfile",
                dataType: 'json',
                async: false,
                data: {
                    usrpfId: program.userId,
                    realName: program.realName,
                    className: program.className,
                    studentNumber: program.studentNum,
                    isStudent: 1,
                    term: program.term,
                    umid: program.umid,
                },
                success: function (result) {
                    console.log(result);
                    if (result.status == 1) {
                        $("#user").modal('hide');
                        pubMeth.alertInfo("alert-success", "修改成功！");
                        program.selectProfile();
                    } else {
                        pubMeth.alertInfo("alert-danger", "修改失败！");
                    }
                }
            });
        },
        /**
         * 添加信息
         */
        addUser: function () {
            console.log(program.umid);
            $.ajax({
                type: "post",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../user/insertProfile",
                dataType: 'json',
                async: false,
                data: {
                    realName: program.realName,
                    className: program.className,
                    studentNumber: program.studentNum,
                    term: program.term,
                    isStudent: '1',
                    umid: program.umid,
                },
                success: function (result) {
                    console.log(result);
                    if (result.status == 1) {
                        pubMeth.alertInfo("alert-success", "添加成功！");
                    } else {
                        pubMeth.alertInfo("alert-danger", "添加失败！");
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-danger", "请求失败");
                }
            });
        },
        /**
         * 查询所有学院
         */
        getSchool: function () {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../major/selectOnlySchoolName",
                dataType: 'json',
                async: false,
                success: function (result) {
                    console.log(result);
                    program.school = result.list;
                }
            });
            program.dappendSchool();
        },
        /**
         * 查询所有专业
         */
        getMajorName: function (webschoolName) {
            $.ajax({
                type: "get",
                content: "application/x-www-form-urlencoded;charset=UTF-8",
                url: "../major/selectMajorName",
                dataType: 'json',
                async: false,
                data: {
                    schoolName: webschoolName,
                },
                success: function (result) {
                    console.log(result);
                    program.major = result.list;

                }
            });
        },
        //改变
        change: function (tagert, className) {
            $(tagert).change(function () {
                var path = $(this).val();
                var path1 = path.lastIndexOf("\\");
                var name = path.substring(path1 + 1);
                $(className).val(name);
            });
        },
        //上传学生名单
        importList: function () {
            $.ajaxFileUpload({
                url: "../user/leadUserInfo",
                secureuri: false,
                fileElementId: "namefile",// 文件选择框的id属性
                dataType: "json",
                data: {},
                success: function (result) {
                    console.log(result);
                    console.log(result.status);
                    if (result.status == '1') {
                        pubMeth.alertInfo("alert-success", "上传成功");
                        program.selectProfile();
                    } else {
                        pubMeth.alertInfo("alert-warning", "上传失败");
                    }
                },
                error: function () {
                    pubMeth.alertInfo("alert-warning", "上传失败");
                }
            });
        },
        dappendSchool: function () {
            $(".dschoolName").empty();
            $(".dschoolName").append("<option>学院</option>");
            var length = program.school.length;
            for (var i = 0; i < length; i++) {
                $(".dschoolName").append("<option value=" + program.school[i].umid + ">" + program.school[i].schoolName + "</option>");
            }
            program.dmachange();
        },
        appendSchool: function () {
            $(".schoolName").empty();
            $(".schoolName").append("<option>学院</option>");
            var length = program.school.length;
            for (var i = 0; i < length; i++) {
                $(".schoolName").append("<option value=" + program.school[i].umid + ">" + program.school[i].schoolName + "</option>");
            }
            program.machange();
        },
        dmachange: function () {
            $(".dknowName").html("");
            program.dschoolName = $(".dschoolName option:selected").text();
            if (program.dschoolName == "学院") {
                $(".dknowName").html("<option>专业</option>");
                return;
            }
            console.log(program.dschoolName);
            program.getMajorName(program.dschoolName);
            for (var i = 0; i < program.major.length; i++) {
                $(".dknowName").append("<option value=" + program.major[i].umid + ">" + program.major[i].majorName + "</option>");
            }
        },
        machange: function () {
            $(".knowName").html("");
            program.schoolName = $(".schoolName option:selected").text();
            if (program.schoolName == "学院") {
                $(".knowName").html("<option>专业</option>");
                return;
            }
            program.getMajorName(program.schoolName);
            for (var i = 0; i < program.major.length; i++) {
                $(".knowName").append("<option value=" + program.major[i].umid + ">" + program.major[i].majorName + "</option>");
            }
        },
    };
    pubMeth.getRowsnum("rowsnum");
    program.selectProfile();
    program.getSchool();
    program.getMajorName();
    $(".schoolName").change(function () {
        program.machange();
    });
    $(".dschoolName").change(function () {
        program.dmachange();
    });
    $(".addUser").click(function () {
        $('#user').modal({
            backdrop: 'static'
        });
        program.setValue();
        program.appendSchool();
        program.userId = "";
    });
    $(".closebutton").click(function () {
        program.setValue();
    });
    $(".saveData").click(function () {
        program.className = $(".className").val();
        program.realName = $(".realName").val();
        program.studentNum = $(".studentNum").val();
        program.term = $(".term").val();
        program.umid = $(".knowName option:selected").val();
        console.log(program.umid);
        if (program.userId != "") {
            console.log(program.term);
            program.updateUser();
        } else {
            if (program.realName != "" && program.className != "" && program.umid != "") {
                program.addUser();
            } else {
                pubMeth.alertInfo("alert-danger", "添加失败！");
            }
        }
        $("#user").modal('hide');
        program.selectProfile();
    });
    $(".deleteUser").click(function () {
        var valArr = new Array;
        $(":checkbox[name='title']:checked").each(function (i) {
            valArr[i] = $(this).val();
        });
        var vals = valArr.join(',');// 转换为逗号隔开的字符串
        if (vals != "") {
            program.deleteUser(vals);
        } else {
            pubMeth.alertInfo("alert-info", "请先勾选删除项！");
        }
    });
    $(".search").click(function () {
        program.majorValue = $(".dknowName option:selected").val();
        program.searnum = $(".searnum").val();
        program.searTitle = $(".searTitle").val();
        if (program.majorValue == "专业") {
            program.majorValue = '';
        }
        program.selectProfile();
    });
    //上传学生名单
    program.change('input[id=namefile]', '.namefile');
    $(".addUsers").click(function () {
        $('#importuser').modal({
            backdrop: 'static'
        });
        $(".comImport").click(function () {
            program.importList();
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
                program.selectProfile();
            }
        });
    } else {
        $(".pagenum").css("display", "none");
    }
});
