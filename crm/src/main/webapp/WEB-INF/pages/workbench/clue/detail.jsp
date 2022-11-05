<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        let cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            /*$(".remarkDiv").mouseover(function(){
                $(this).children("div").children("div").show();
            });*/
            $("#remarkDivList").on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            });

            /*$(".remarkDiv").mouseout(function () {
                $(this).children("div").children("div").hide();
            });*/
            $("#remarkDivList").on("mouseout", ".remarkDiv", function () {
                $(this).children("div").children("div").hide();
            });

            /*$(".myHref").mouseover(function(){
                $(this).children("span").css("color","red");
            });*/
            $("#remarkDivList").on("mouseover", ".myHref", function () {
                $(this).children("span").css("color", "red");
            });

            /*$(".myHref").mouseout(function(){
                $(this).children("span").css("color","#E6E6E6");
            });*/
            $("#remarkDivList").on("mouseout", ".myHref", function () {
                $(this).children("span").css("color", "#E6E6E6");
            });



            //给"关联市场活动"按钮添加单击事件
            $("#bindActivityBtn").click(function () {
                //初始化工作
                //清空搜索框
                $("#searchActivityTxt").val("");
                //清空搜索的市场活动列表
                $("#tBody").html("");

                //弹出"线索关联市场活动"的模态窗口
                $("#bindModal").modal("show");
            });

            //给市场活动搜索框添加键盘弹起事件
            $("#searchActivityTxt").keyup(function () {
                //收集参数
                let activityName = this.value;
                let clueId = '${clue.id}';
                //发送请求
                $.ajax({
                    url: 'workbench/clue/selectActivityByNameAndClueId.do',
                    data: {
                        activityName: activityName,
                        clueId: clueId
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (json) {
                        //遍历data，显示搜索到的市场活动列表
                        let htmlStr = "";
                        $.each(json, function (index, obj) {
                            htmlStr += "<tr>";
                            htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
                            htmlStr += "<td>" + obj.name + "</td>";
                            htmlStr += "<td>" + obj.startDate + "</td>";
                            htmlStr += "<td>" + obj.endDate + "</td>";
                            htmlStr += "<td>" + obj.owner + "</td>";
                            htmlStr += "</tr>";
                        });
                        $("#tBody").html(htmlStr);
                    }
                });
            });

            //给"关联"按钮添加单击事件
            $("#saveBindActivityBtn").click(function () {
                //收集参数
                //获取列表中所有被选中的checkbox
                let checkedIds = $("#tBody input[type='checkbox']:checked");
                //表单验证
                if (checkedIds.size() === 0) {
                    alert("请选择要关联的市场活动");
                    return;
                }
                let ids = "";
                $.each(checkedIds, function () {
                    ids += "activityId=" + this.value + "&";
                });
                ids += "clueId=${clue.id}";//activityId=xxx&activityId=xxx&....&activityId=xxx&clueId=xxx

                //发送请求
                $.ajax({
                    url: 'workbench/clue/saveBind.do',
                    data: ids,
                    type: 'post',
                    dataType: 'json',
                    success: function (json) {
                        if (json.code === "1") {
                            //关闭模态窗口
                            $("#bindModal").modal("hide");
                            //刷新已经关联过的市场活动列表
                            let htmlStr = "";
                            $.each(json.data, function (index, obj) {
                                htmlStr += "<tr id=\"tr_" + obj.id + "\">";
                                htmlStr += "<td>" + obj.name + "</td>";
                                htmlStr += "<td>" + obj.startDate + "</td>";
                                htmlStr += "<td>" + obj.endDate + "</td>";
                                htmlStr += "<td>" + obj.owner + "</td>";
                                htmlStr += "<td><a href=\"javascript:void(0);\" activityId=\"" + obj.id + "\"  style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>解除关联</a></td>";
                                htmlStr += "</tr>";
                            });
                            $("#relationTBody").append(htmlStr);
                        } else {
                            //提示信息
                            alert(json.message);
                            //模态窗口不关闭
                            $("#bindModal").modal("show");
                        }
                    }
                });
            });

            //给所有的"解除关联"按钮添加单击事件
            $("#relationTBody").on("click", "a", function () {
                //收集参数
                let activityId = $(this).attr("activityId");
                let clueId = "${clue.id}";

                if (window.confirm("确定删除吗？")) {
                    //发送请求
                    $.ajax({
                        url: 'workbench/clue/relieveBind.do',
                        data: {
                            activityId: activityId,
                            clueId: clueId
                        },
                        type: 'post',
                        dataType: 'json',
                        success: function (json) {
                            if (json.code === "1") {
                                //刷新已经关联的市场活动列表
                                $("#tr_" + activityId).remove();
                            } else {
                                //提示信息
                                alert(json.message);
                            }
                        }
                    });
                }
            });

            //给"转换"按钮添加单击事件
            $("#convertClueBtn").click(function () {
                //收集参数
                let id = '${clue.id}';
                //发送同步请求
                window.location.href = "workbench/clue/toConvert.do?id=" + id;
            });
        });

    </script>

</head>
<body>

<!-- 关联市场活动的模态窗口 -->
<div class="modal fade" id="bindModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">关联市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" id="searchActivityTxt" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td><input type="checkbox"/></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody id="tBody">
                    <%--<tr>
                        <td><input type="checkbox"/></td>
                        <td>发传单</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                        <td>zhangsan</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox"/></td>
                        <td>发传单</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                        <td>zhangsan</td>
                    </tr>--%>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="saveBindActivityBtn">关联</button>
            </div>
        </div>
    </div>
</div>


<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>${clue.fullName}${clue.appellation} <small>${clue.company}</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" id="convertClueBtn"><span
                class="glyphicon glyphicon-retweet"></span> 转换
        </button>

    </div>
</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 200px; top:
			-20px;"><b>${clue.fullName}${clue.appellation}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.owner}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">公司</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>
            <c:if test="${not empty clue.company}">
                ${clue.company}
            </c:if>&nbsp;
        </b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>
            <c:if test="${not empty clue.job}">
                ${clue.job}
            </c:if>&nbsp;
        </b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">邮箱</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>
            <c:if test="${not empty clue.email}">
                ${clue.email}
            </c:if>&nbsp;
        </b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>
            <c:if test="${not empty clue.phone}">
                ${clue.phone}
            </c:if>&nbsp;
        </b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">公司网站</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>
            <c:if test="${not empty clue.website}">
                ${clue.website}
            </c:if>&nbsp;
        </b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>
            <c:if test="${not empty clue.mPhone}">
                ${clue.mPhone}
            </c:if>&nbsp;
        </b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">线索状态</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>
            <c:if test="${not empty clue.state}">
                ${clue.state}
            </c:if>&nbsp;
        </b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>
            <c:if test="${not empty clue.source}">
                ${clue.source}
            </c:if>&nbsp;
        </b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>
            <c:if test="${not empty clue.createBy}">
                ${clue.createBy}
            </c:if>&nbsp;&nbsp;
            &nbsp;</b><small style="font-size: 10px; color: gray;">
            <c:if test="${not empty clue.createTime}">
                ${clue.createTime}
            </c:if>&nbsp;
        </small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 60px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>
            <c:if test="${not empty clue.editBy}">
                ${clue.editBy}
            </c:if>&nbsp;&nbsp;
            &nbsp;</b><small style="font-size: 10px; color: gray;">
            <c:if test="${not empty clue.editTime}">
                ${clue.editTime}
            </c:if>&nbsp;&nbsp;
        </small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 70px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                <c:if test="${not empty clue.description}">
                    ${clue.description}
                </c:if>&nbsp;&nbsp;
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 80px;">
        <div style="width: 300px; color: gray;">联系纪要</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                <c:if test="${not empty clue.contactSummary}">
                    ${clue.contactSummary}
                </c:if>&nbsp;&nbsp;
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 90px;">
        <div style="width: 300px; color: gray;">下次联系时间</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>
            <c:if test="${not empty clue.nextContactTime}">
                ${clue.nextContactTime}
            </c:if>&nbsp;
        </b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 100px;">
        <div style="width: 300px; color: gray;">详细地址</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                <c:if test="${not empty clue.address}">
                    ${clue.address}
                </c:if>&nbsp;
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div id="remarkDivList" style="position: relative; top: 40px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <c:forEach items="${remarkList}" var="remark">
        <div class="remarkDiv" id="div_${remark.id}" style="height: 60px;">
            <img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
            <div style="position: relative; top: -40px; left: 40px;">
                <h5>${remark.noteContent}</h5>
                <font color="gray">线索</font> <font color="gray">-</font> <b>${clue.fullName}${clue.appellation}-
                    ${clue.company}</b> <small
                    style="color: gray;"> ${remark.editFlag=='0'?remark.createTime:remark.editTime}
                由${remark.editFlag=='0'?remark.createBy:remark.editBy}${remark.editFlag=='0'?'创建':'修改'}</small>
                <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                    <a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span
                            class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a class="myHref" name="deleteA" remarkId="${remark.id}" href="javascript:void(0);"><span
                            class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
                </div>
            </div>
        </div>
    </c:forEach>

    <!-- 备注1 -->
    <%--<div class="remarkDiv" style="height: 60px;">
        <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
        <div style="position: relative; top: -40px; left: 40px;" >
            <h5>哎呦！</h5>
            <font color="gray">线索</font> <font color="gray">-</font> <b>李四先生-动力节点</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
            </div>
        </div>
    </div>--%>

    <!-- 备注2 -->
    <%--<div class="remarkDiv" style="height: 60px;">
        <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
        <div style="position: relative; top: -40px; left: 40px;" >
            <h5>呵呵！</h5>
            <font color="gray">线索</font> <font color="gray">-</font> <b>李四先生-动力节点</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
            </div>
        </div>
    </div>--%>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary">保存</button>
            </p>
        </form>
    </div>
</div>

<!-- 市场活动 -->
<div>
    <div style="position: relative; top: 60px; left: 40px;">
        <div class="page-header">
            <h4>市场活动</h4>
        </div>
        <div style="position: relative;top: 0px;">
            <table class="table table-hover" style="width: 900px;">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>名称</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                    <td>所有者</td>
                    <td></td>
                </tr>
                </thead>
                <tbody id="relationTBody">
                <c:forEach items="${activityList}" var="act">
                    <tr id="tr_${act.id}">
                        <td>${act.name}</td>
                        <td>${act.startDate}</td>
                        <td>${act.endDate}</td>
                        <td>${act.owner}</td>
                        <td><a href="javascript:void(0);" activityId="${act.id}" style="text-decoration: none;"><span
                                class="glyphicon glyphicon-remove"></span>解除关联</a></td>
                    </tr>
                </c:forEach>
                <%--<tr>
                    <td>发传单</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                    <td>zhangsan</td>
                    <td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
                </tr>
                <tr>
                    <td>发传单</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                    <td>zhangsan</td>
                    <td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
                </tr>--%>
                </tbody>
            </table>
        </div>

        <div>
            <a href="javascript:void(0);" id="bindActivityBtn" style="text-decoration: none;"><span
                    class="glyphicon glyphicon-plus"></span>关联市场活动</a>
        </div>
    </div>
</div>


<div style="height: 200px;"></div>
</body>
</html>
