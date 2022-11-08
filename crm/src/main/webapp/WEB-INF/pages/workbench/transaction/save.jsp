<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
	<script type="text/javascript">
	$(function () {
		//给"阶段"下拉框添加change事件
		$("#create-stage").change(function () {
			//收集参数
			//alert($(this).find("option:selected").text());
			var stageValue=$("#create-stage option:selected").text();
			//表单验证
			if(stageValue==""){
				//清空可能性输入框
				$("#create-possibility").val("");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/transaction/getPossibilityByStage.do',
				data:{
					stageValue:stageValue
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					//把可能性显示在输入框中
					$("#create-possibility").val(data);
				}
			});
		});

		//当容器加载完成之后，对容器调用工具函数
		$("#create-customerName").typeahead({
			source:function (jquery,process) {//每次键盘弹起，都自动触发本函数；我们可以向后台送请求，查询客户表中所有的名称，把客户名称以[]字符串形式返回前台，赋值给source
				//process：是个函数，能够将['xxx','xxxxx','xxxxxx',.....]字符串赋值给source，从而完成自动补全
				//jquery：在容器中输入的关键字
				//var customerName=$("#customerName").val();
				//发送查询请求
				$.ajax({
					url:'workbench/transaction/queryCustomerNameByName.do',
					data:{
						customerName:jquery
					},
					type:'post',
					dataType:'json',
					success:function (data) {//['xxx','xxxxx','xxxxxx',.....]
						process(data);
					}
				});
			}
		});

		//给"保存"按钮添加单击事件
		$("#saveCreateTranBtn").click(function () {
			//收集参数
			var owner          =$("#create-owner").val();
			var money          =$.trim($("#create-money").val());
			var name           =$.trim($("#create-name").val());
			var expectedDate   =$("#create-expectedDate").val();
			var customerName   =$.trim($("#create-customerName").val());
			var stage          =$("#create-stage").val();
			var type           =$("#create-type").val();
			var source         =$("#create-source").val();
			var activityId     =$("#create-activityId").val();
			var contactsId     =$("#create-contactsId").val();
			var description    =$.trim($("#create-description").val());
			var contactSummary =$.trim($("#create-contactSummary").val());
			var nextContactTime=$("#create-nextContactTime").val();
			//表单验证(作业)

			//发送请求
			$.ajax({
				url:'workbench/transaction/saveCreateTran.do',
				data:{
					owner          :owner          ,
					money          :money          ,
					name           :name           ,
					expectedDate   :expectedDate   ,
					customerName   :customerName   ,
					stage          :stage          ,
					type           :type           ,
					source         :source         ,
					activityId     :activityId     ,
					contactsId     :contactsId     ,
					description    :description    ,
					contactSummary :contactSummary ,
					nextContactTime:nextContactTime
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						//跳转到交易主页面
						window.location.href="workbench/transaction/index.do";
					}else{
						//提示信息
						alert(data.message);
					}
				}
			});
		});
	});
</script>
</head>
<body>

	<!-- 查找市场活动模态窗口 -->
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人模态窗口 -->
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveCreateTranBtn">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-owner">
				  <c:forEach items="${userList}" var="u">
                    <option value="${u.id}">${u.name}</option>
                  </c:forEach>
				</select>
			</div>
			<label for="create-money" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-name">
			</div>
			<label for="create-expectedDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-expectedDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-customerName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-stage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-stage">
			  	<option></option>
                  <c:forEach items="${stageList}" var="s">
                     <option value="${s.id}">${s.value}</option>
                  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-type" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-type">
				  <option></option>
				  <c:forEach items="${transactionTypeList}" var="tt">
                    <option value="${tt.id}">${tt.value}</option>
                  </c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-source" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-source">
				  <option></option>
				  <c:forEach items="${sourceList}" var="so">
                    <option value="${so.id}">${so.value}</option>
                  </c:forEach>
				</select>
			</div>
			<label for="create-activityName" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
                <input type="hidden" id="create-activityId" value="0aa857265ea2449b852a9811475ea959">
				<input type="text" class="form-control" id="create-activityName" value="i测试04">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
                <input type="hidden" id="create-contactsId" value="5f6626071ef247b094a474d1bc5ca6eb">
				<input type="text" class="form-control" id="create-contactsName" value="张三">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-description" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>