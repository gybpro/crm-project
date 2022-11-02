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
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">

        $(function () {
            // 页面加载时刷新市场活动数据
            selectActivityByConditionForPage(1, 5);

            // 页面加载时绑定查询按钮单击事件
            $("#selectBtn").click(function () {
                selectActivityByConditionForPage(1, $("#turnPage").bs_pagination("getOption", 'rowsPerPage'));
            });

            // 页面加载时绑定创建按钮点击事件
            $("#createActivityBtn").click(function () {
                // 清空添加表单
                // $("#createActivityForm")[0].reset();
                $("#createActivityForm").get(0).reset();
                // 显示表单页面
                $("#createActivityModal").modal("show");
            });

            // 页面加载时绑定添加市场活动保存按钮点击事件
            $("#saveCreateActivityBtn").click(function () {
                // 获取表单数据
                let owner = $("#create-marketActivityOwner").val();
                let name = $.trim($("#create-marketActivityName").val());
                let startDate = $("#create-startDate").val();
                let endDate = $("#create-endDate").val();
                let cost = $.trim($("#create-cost").val());
                let description = $.trim($("#create-description").val());
                // 表单验证
                if (owner === "") {
                    alert("所有者不能为空");
                    return;
                }
                if (name === "") {
                    alert("名称不能为空");
                    return;
                }
                if (startDate!=="" && endDate!=="") {
                    if (endDate < startDate) {
                        alert("结束日期不能小于开始日期");
                        return;
                    }
                }
                /*
                正则表达式：也是一种语言，用于定义字符串的匹配模式
                    语法：
                    1.//：在JS中定义一个正则表达式，let regExp = /....../;
                    2.^：匹配字符串的开头位置
                      $：匹配字符串的结尾
                      let regExp = /^......$/;
                    3.[]：匹配指定字符集中的一位字符，let regExp = /^[a-z0-9]$/;
                    4.{}：匹配次数，let regExp = /^[a-z0-9]{5}$/;
                        {m}：匹配m次
                        {m,n}：匹配m~n次
                        {m,}：匹配m次或更多次
                    5.特殊符号：
                        \d：匹配一位数字，相当于[0-9]
                        \D：匹配一位非数字
                        \w：匹配所有非特殊字符，包括字母、数字、下划线、各种语言字符，除了特殊字符
                        \W：匹配所有特殊字符

                        *：匹配0到多次，相当于{0,}
                        +：匹配1到多次，相当于{1,}
                        ?：匹配0或1次，相当于{0,1}

                        非负整数：^(([1-9]\d*)|0)$
                        (第一位1到9，之后任意数字出现0到多次)或0
                 */
                let regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能为非负整数");
                    return;
                }
                // 发送请求
                $.ajax({
                    type: "POST",
                    url: "workbench/activity/insert.do",
                    data: {
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    dataType: 'json',
                    async: true,
                    success: function (json) {
                        if (json.code === '1') {
                            // 关闭模态窗
                            $("#createActivityModal").modal("hide");
                            // 刷新市场活动显示列，显示第一页数据，保持每页显示条数不变
                            selectActivityByConditionForPage(1, $("#turnPage").bs_pagination("getOption",
                                'rowsPerPage'));
                        } else {
                            alert(json.message);
                            // 模态窗不关闭
                            $("#createActivityModal").modal("show");// 可以不写
                        }
                    }
                });
            });

            // 页面加载时加载日历
            $(".myDate").datetimepicker({
                language: "zh-CN",// 语言
                format: "yyyy-mm-dd",// 日期格式
                minView: "month",// 可选择的最小视图
                initialDate: new Date(),// 初始化日期
                todayBtn: true,// 设置跳转到今天的按钮，默认是false
                clearBtn: true // 设置清空日期的按钮，默认是false
            });

            // 页面加载时绑定全选按钮单击事件
            $("#checkAll").click(function () {
                $("#tBody input[type='checkbox']").prop("checked", this.checked);
            });

            // 页面加载时绑定市场活动列表变化事件
            // on("事件类型", "子选择器", fun)是标签内容发生变化时事件，用于为子标签动态绑定事件
            // fun是子标签事件函数，函数内的this是指向发生子标签事件的元素
            $("#tBody").on("click", "input[type='checkbox']", function () {
                if ($("#tBody input[type='checkbox']").size() === $("#tBody input[type='checkbox']:checked").size()) {
                    $("#checkAll").prop("checked", true);
                } else {
                    $("#checkAll").prop("checked", false);
                }
            });

            // 页面加载时绑定删除按钮点击事件
            $("#deleteActivityBtn").click(function () {
                // 收集参数
                let checkedIds = $("#tBody input[type='checkbox']:checked");
                // 判空
                if (checkedIds.size() === 0) {
                    alert("请选择要删除的市场活动");
                    return;
                }
                // 发送请求
                if (confirm("注意：删除后无法恢复，是否确定删除？")) {
                    let ids = "";
                    $.each(checkedIds, function () {
                        ids += "id=" + this.value + "&";
                    });
                    ids = ids.substring(0, ids.length-1);
                    $.ajax({
                        type: "POST",
                        url: "workbench/activity/delete.do",
                        data: ids,
                        dataType: "json",
                        async: true,
                        success: function (json) {
                            if (json.code === "1") {
                                // 刷新市场活动列表
                                selectActivityByConditionForPage(1, $("#turnPage").bs_pagination("getOption",
                                    "rowsPerPage"));
                            } else {
                                alert(json.message);
                            }
                        }
                    })
                }
            });

            // 页面加载时绑定修改按钮点击事件
            $("#editActivityBtn").click(function () {
                // 收集参数
                let checkedId = $("#tBody input[type='checkbox']:checked");
                // 判断选择数
                if (checkedId.size() === 0) {
                    alert("请选择要修改的市场活动");
                    return;
                } else if (checkedId.size() > 1) {
                    alert("一次只能修改一个市场活动");
                    return;
                }
                // 清空市场活动修改表单
                $("#editActivityForm")[0].reset();
                // 查询并将市场活动的值写入表单
                // alert(checkedId.val());
                $.ajax({
                    type: "GET",
                    url: "workbench/activity/selectOne.do",
                    data: "id=" + checkedId.val(),
                    dataType: "json",
                    async: true,
                    success: function (json) {
                        if (json.code === "1") {
                            // 设置参数
                            $("#edit-marketActivityName").val(json.data.name);
                            $("#edit-startDate").val(json.data.startDate);
                            $("#edit-endDate").val(json.data.endDate);
                            $("#edit-cost").val(json.data.cost);
                            $("#edit-description").val(json.data.description);
                            // 显示表单页面
                            $("#editActivityModal").modal("show");
                        } else {
                            alert(json.message);
                        }
                    }
                });
            });

            // 页面加载时绑定修改表单更新按钮点击事件
            $("#updateEditActivityBtn").click(function () {
                // 获取表单数据
                let id = $("#tBody input[type='checkbox']:checked").val();
                let owner = $("#edit-marketActivityOwner").val();
                let name = $.trim($("#edit-marketActivityName").val());
                let startDate = $("#edit-startDate").val();
                let endDate = $("#edit-endDate").val();
                let cost = $.trim($("#edit-cost").val());
                let description = $.trim($("#edit-description").val());
                // 表单验证
                if (owner === "") {
                    alert("所有者不能为空");
                    return;
                }
                if (name === "") {
                    alert("名称不能为空");
                    return;
                }
                if (startDate!=="" && endDate!=="") {
                    if (endDate < startDate) {
                        alert("结束日期不能小于开始日期");
                        return;
                    }
                }
                let regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能为非负整数");
                    return;
                }
                // 发送请求
                $.ajax({
                    type: "POST",
                    url: "workbench/activity/update.do",
                    data: {
                        id: id,
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    dataType: 'json',
                    async: true,
                    success: function (json) {
                        if (json.code === '1') {
                            // 关闭模态窗
                            $("#editActivityModal").modal("hide");
                            // 刷新市场活动显示列，显示第一页数据，保持每页显示条数不变
                            let turnPage = $("#turnPage");
                            selectActivityByConditionForPage(turnPage.bs_pagination("getOption", "currentPage"),
                                turnPage.bs_pagination("getOption", 'rowsPerPage'));
                        } else {
                            alert(json.message);
                            // 模态窗不关闭
                            $("#editActivityModal").modal("show");// 可以不写
                        }
                    }
                });
            });

            // 页面加载时绑定批量导出按钮点击事件
            $("#exportActivityAllBtn").click(function () {
                // 发送同步请求，文件传输必须是同步请求
                window.location.href = "workbench/activity/exportAllActivity.do"
            });
        });

        // 查询刷新市场活动列表及数据
        function selectActivityByConditionForPage(pageNo, pageSize) {
            let name = $("#select-name").val();
            let owner = $("#select-owner").val();
            let startDate = $("#select-startDate").val();
            let endDate = $("#select-endDate").val();
            $.ajax({
                type: "GET",
                url: "workbench/activity/select.do",
                data: {
                    name: name,
                    owner: owner,
                    startDate: startDate,
                    endDate: endDate,
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                dataType: "json",
                async: true,
                success: function (json) {
                    // $("#totalRows").text(data.data.totalRows);
                    let html = "";
                    $.each(json.data.activityList, function (index, obj) {
                        html += "<tr class=\"active\">";
                        html += "    <td><input type=\"checkbox\" value='" + obj.id + "'/></td>";
                        html += "    <td><a style=\"text-decoration: none; cursor: pointer;\"";
                        html += "           onclick=\"window.location.href='workbench/activity/detail.do?id=" +
                            obj.id  + "';\">" + obj.name + "</a>";
                        html += "    </td>";
                        html += "    <td>" + obj.owner + "</td>";
                        html += "    <td>" + obj.startDate + "</td>";
                        html += "    <td>" + obj.endDate + "</td>";
                        html += "</tr>";
                    });
                    // 选择器.html()，覆盖显示
                    // 选择器.append()，追加显示
                    $("#tBody").html(html);

                    // 取消全选
                    $("#checkAll").prop("checked", false);

                    // 翻页插件使用
                    $("#turnPage").bs_pagination({
                        currentPage: pageNo,// 当前页号
                        rowsPerPage: pageSize,// 页面大小(显示记录条数)
                        totalRows: json.data.totalRows,// 总记录数
                        totalPages: Math.ceil(json.data.totalRows / pageSize),

                        visiblePageLinks: 5,// 最多可显示卡片数

                        showGoToPage: true,//是否显示"跳转到"部分,默认true--显示
                        showRowsPerPage: true,//是否显示"每页显示条数"部分。默认true--显示
                        showRowsInfo: true,//是否显示记录的信息，默认true--显示

                        // 页面变换事件
                        // event是事件
                        onChangePage: function (event, pageObj) {
                            selectActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                        }
                    });
                }
            });
        }

    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="createActivityForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="create-startDate" readonly>
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="create-endDate" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" id="saveCreateActivityBtn" class="btn btn-primary">保存
                </button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="editActivityForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="edit-startDate" readonly>
                        </div>
                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDate" id="edit-endDate" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" id="updateEditActivityBtn" class="btn btn-primary">更新
                </button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <div class="modal-body" style="height: 350px;">
                <div style="position: relative;top: 20px; left: 50px;">
                    请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                </div>
                <div style="position: relative;top: 40px; left: 50px;">
                    <input type="file" id="activityFile">
                </div>
                <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                    <h3>重要提示</h3>
                    <ul>
                        <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                        <li>给定文件的第一行将视为字段名。</li>
                        <li>请确认您的文件大小不超过5MB。</li>
                        <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                        <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                        <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                        <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input id="select-name" class="form-control" type="text">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input id="select-owner" class="form-control" type="text">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input id="select-startDate" class="form-control" type="text" id="startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input id="select-endDate" class="form-control" type="text" id="endDate">
                    </div>
                </div>

                <button type="button" id="selectBtn" class="btn btn-default">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createActivityBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editActivityBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）
                </button>
                <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="tBody">
                <%--<tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a>
                    </td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox"/></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a>
                    </td>
                    <td>zhangsan</td>
                    <td>2020-10-10</td>
                    <td>2020-10-20</td>
                </tr>--%>
                </tbody>
            </table>
            <div id="turnPage"></div>
        </div>

        <%--<div style="height: 50px; position: relative;top: 30px;">
            <div>
                <button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRows">50</b>条记录
                </button>
            </div>
            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">
                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>
                <div class="btn-group">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                        10
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">20</a></li>
                        <li><a href="#">30</a></li>
                    </ul>
                </div>
                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
            </div>
            <div style="position: relative;top: -88px; left: 285px;">
                <nav>
                    <ul class="pagination">
                        <li class="disabled"><a href="#">首页</a></li>
                        <li class="disabled"><a href="#">上一页</a></li>
                        <li class="active"><a href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                        <li><a href="#">下一页</a></li>
                        <li class="disabled"><a href="#">末页</a></li>
                    </ul>
                </nav>
            </div>
        </div>--%>

    </div>

</div>
</body>
</html>
