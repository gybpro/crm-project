<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<body>
<script type="text/javascript">
    $(function () {
        $(window).keyup(function (event) {
            if (event.keyCode === 13) {
                $("#loginBtn").click();
            }
        });

        //给"登录"按钮添加单击事件
        $("#loginBtn").click(function () {
            //收集参数
            let loginAct = $.trim($("#loginAct").val());
            let loginPwd = $.trim($("#loginPwd").val());
            // 选择器$().attr()获取属性，但属性不能是Boolean类型值
            // 选择器$().prop()获取值是Boolean类型的属性
            let isRemPwd = $("#isRemPwd").prop("checked");
            //表单验证
            if (loginAct === "") {
                alert("用户名不能为空");
                return;
            }
            if (loginPwd === "") {
                alert("密码不能为空");
                return;
            }

            //显示正在验证
            $("#msg").text("正在努力验证....");
            //发送请求
            $.ajax({
                url: 'settings/qx/user/login.do',
                data: {
                    loginAct: loginAct,
                    loginPwd: loginPwd,
                    isRemPwd: isRemPwd
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    if (data.code === "1") {
                        //跳转到业务主页面
                        window.location.href = "workbench/index.do";
                    } else {
                        //提示信息
                        $("#msg").text(data.message);
                    }
                },
                /*beforeSend:function () {//当ajax向后台发送请求之前，会自动执行本函数；
                    //该函数的返回值能够决定ajax是否真正向后台发送请求：
                    //如果该函数返回true,则ajax会真正向后台发送请求；否则，如果该函数返回false，则ajax放弃向后台发送请求。
                    $("#msg").text("正在努力验证....");
                    return true;
                }*/
            });
        });
    })
</script>

<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2022&nbsp;high</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.html" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" id="loginAct" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" id="loginPwd" placeholder="密码">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">
                    <label>
                        <input type="checkbox" id="isRemPwd"> 十天内免登录
                    </label>
                    &nbsp;&nbsp;
                    <span id="msg" style="color: red"></span>
                </div>
                <button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
