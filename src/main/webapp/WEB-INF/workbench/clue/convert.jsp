<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link href="/crm/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/crm/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});
	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						  <div class="form-group has-feedback">
						    <input type="text" id="searchBtn" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="body">
							<%--<tr>
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
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" onclick="bind()" class="btn btn-primary" data-dismiss="modal">关联</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small id="content"></small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：动力节点
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：李四先生
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" value="0" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" >
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control" id="expectedClosingDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control">
		    	<option></option>
		    	<c:forEach items="${map['stage']}" var="stage">
					<option value="${stage.value}">${stage.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
			  <input type="hidden" id="activityId">
		    <label>市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#searchActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
			  <%--
			  	readonly
			  	disabled
			  --%>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
		  </div>

	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>zhangsan</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" onclick="convert()" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>

<script>
	//根据线索主键查询页面上关于线索的信息
	$.get("/crm/workbench/clue/queryById",{'id':'${id}'},function (data) {
		//data:clue
		$('#content').text(data.fullname + data.appellation + data.company);
	},'json');

	function convert(){
		$.post("/crm/workbench/clue/convert",{
			'id':'${id}',//线索主键
			'isCreateTransaction' : $('#isCreateTransaction').val(),//是否发生交易

			//交易数据
			'money' : $('#amountOfMoney').val(),//交易金额
			'name' : $('#tradeName').val(),//交易名称
			'expectedDate' : $('#expectedClosingDate').val(),//预计成交日期
			'stage' : $('#stage').val(),//交易阶段
			'activityId' : $('#activityId').val()//市场活动外键
		},function (data) {
			//data:resultVo
			if(data.ok){
				alert(data.message);
			}
		},'json');
	}


	//判断转换过程中是否发生交易
	$('#isCreateTransaction').click(function () {
		if($(this).prop("checked")){
			//勾中
			$(this).val("1");
		}else{
			//没有勾中
			$(this).val("0");
		}
	});

	//点击搜索线索转换中关联过的市场活动
	$('#searchBtn').keypress(function (event) {
		if(event.keyCode == 13){
			$.get("/crm/workbench/clue/searchActivity",{
				'id':'${id}',
				'name':$(this).val()
			},function (data) {
				$('#body').html("");
				//data:List<Activity>
				for(var i = 0; i < data.length; i++){
					var activity = data[i];
					$('#body').append("<tr>\n" +
							"\t\t\t\t\t\t\t\t<td><input class='son' type=\"radio\" value="+activity.id+" name=\"activity\"/></td>\n" +
							"\t\t\t\t\t\t\t\t<td>"+activity.name+"</td>\n" +
							"\t\t\t\t\t\t\t\t<td>"+activity.startDate+"</td>\n" +
							"\t\t\t\t\t\t\t\t<td>"+activity.endDate+"</td>\n" +
							"\t\t\t\t\t\t\t\t<td>"+activity.owner+"</td>\n" +
							"\t\t\t\t\t\t\t</tr>");
				}
			},'json');
		}
	});

	//点击关联按钮，获取线索已经关联的市场活动
	function bind() {
		var activityId = $($('.son:checked')[0]).val();
		//把获取市场活动的主键设置到隐藏域中
		$('#activityId').val(activityId);

		//获取勾中市场活动名称，设置到页面上
		var text = $($('.son:checked')[0]).parent().next().text();
		$('#activityName').val(text);
	}
</script>
</html>