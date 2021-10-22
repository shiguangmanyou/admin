<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link href="/crm/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="/crm/jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="/crm/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<%--导入分页插件--%>
<link href="/crm/jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/crm/jquery/bs_pagination/en.js"></script>
<script type="text/javascript" src="/crm/jquery/bs_pagination/jquery.bs_pagination.min.js"></script>

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
				
					<form id="saveForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" name="owner" id="create-marketActivityOwner">
									<c:forEach items="${users}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" name="name" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="startDate" class="form-control" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="endDate" class="form-control" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" name="cost" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea name="description" class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" onclick="saveActivity()" data-dismiss="modal">保存</button>
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
				
					<form class="form-horizontal" id="updateForm" role="form">
						<input type="hidden" name="id" id="id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" name="owner" id="edit-marketActivityOwner">
									<c:forEach items="${users}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" name="name" class="form-control" id="edit-marketActivityName" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="startDate" class="form-control" id="edit-startTime">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="endDate" class="form-control" id="edit-endTime">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="cost" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" name="description" rows="3" id="edit-describe">

								</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" onclick="updateActivity()" data-dismiss="modal">更新</button>
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
				      <input class="form-control" id="name" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="owner" type="text">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="endTime">
				    </div>
				  </div>
				  
				  <button type="button" id="queryBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-toggle="modal" onclick="openAddModal()"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" onclick="openUpdateModal()"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" onclick="deleteBatch()"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				  <button type="button" class="btn btn-success" onclick="exportExcel()"><span class="glyphicon glyphicon-minus"></span> 导出报表</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="father" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>

	<script src="/crm/jquery/layer/layer.js"></script>
<script>
	var rsc_bs_pag = {
		go_to_page_title: 'Go to page',
		rows_per_page_title: 'Rows per page',
		current_page_label: 'Page',
		current_page_abbr_label: 'p.',
		total_pages_label: 'of',
		total_pages_abbr_label: '/',
		total_rows_label: 'of',
		rows_info_records: 'records',
		go_top_text: '首页',
		go_prev_text: '上一页',
		go_next_text: '下一页',
		go_last_text: '末页'
	};

	refresh(1,2);

	//刷新页面数据
	function refresh(page,pageSize){
		$.get("/crm/workbench/activity/list",{
			'page' : page,//当前页码
			'pageSize' : pageSize,//每页记录数
			//查询条件的数据
			'name' : $('#name').val(),
			'owner' : $('#owner').val(),
			'startDate' : $('#startTime').val(),
			'endDate' : $('#endTime').val()
		},function (data) {
			var pageInfo = data;
			//清空内容
			$('#activityBody').html("");
			//data:List<Activity>
			for(var i = 0; i < pageInfo.list.length; i++){
				var activity = pageInfo.list[i];
				$('#activityBody').append("<tr class=\"active\">\n" +
						"\t\t\t\t\t\t\t<td><input type=\"checkbox\" value="+activity.id+" class='son' onclick='change()' /></td>\n" +
						"\t\t\t\t\t\t\t<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='/crm/toView/workbench/activity/detail?id="+activity.id+"';\">"+activity.name+"</a></td>\n" +
						"                            <td>"+activity.owner+"</td>\n" +
						"\t\t\t\t\t\t\t<td>"+activity.startDate+"</td>\n" +
						"\t\t\t\t\t\t\t<td>"+activity.endDate+"</td>\n" +
						"\t\t\t\t\t\t</tr>");
			}

			//分页插件
			$("#activityPage").bs_pagination({
				currentPage: pageInfo.pageNum, // 页码
				rowsPerPage: pageInfo.pageSize, // 每页显示的记录条数
				maxRowsPerPage: 20, // 每页最多显示的记录条数
				totalPages: pageInfo.pages, // 总页数
				totalRows: pageInfo.total, // 总记录条数
				visiblePageLinks: 4, // 显示几个卡片
				showGoToPage: true,
				showRowsPerPage: true,
				showRowsInfo: true,
				showRowsDefaultInfo: true,
				onChangePage : function(event, obj){
					refresh(obj.currentPage,obj.rowsPerPage);
				}
			});
		},'json');
	}

	;(function($){
		$.fn.datetimepicker.dates['zh-CN'] = {
			days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
			daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			today: "今天",
			suffix: [],
			meridiem: ["上午", "下午"]
		};
	}(jQuery));


	/*日历插件*/
	$(".time").datetimepicker({
		language:  "zh-CN",
		format: "yyyy-mm-dd",//显示格式
		minView: "month",//设置只显示到月份
		initialDate: new Date(),//初始化当前日期
		autoclose: true,//选中自动关闭
		todayBtn: true, //显示今日按钮
		clearBtn : true,
		pickerPosition: "bottom-left"
	});

	//异步查询市场活动数据 参数1:事件的名称 参数2:事件触发的函数
	$('#queryBtn').bind('click',function () {
		refresh(1,2);
	});


	//for...in 有的时候会出现数据丢失现象
/*	var person = {'name':'张三','age':10,'speak':function () {
			alert("我是张三")
		}};
	person.speak();
	for(var i in person){

	}*/


	function openAddModal() {
		//手动弹出模态窗口 show hide
		$('#createActivityModal').modal('show');

		//异步查询所有者信息
		/*$.get("/crm/workbench/activity/queryUsers",function (data) {
			//data:List<User> create-marketActivityOwner
			   var content = "";
				for(var i = 0; i < data.length; i++){
					var user = data[i];
					content += "<option value="+user.id+">"+user.name+"</option>";
				}
				//append:没添加一条数据会把页面上的所有dom元素都重新生成
				$('#create-marketActivityOwner').html(content);
		},'json');*/
	}

	//保存市场活动数据
	function saveActivity() {
		//表单序列化只能适合单表
	/*	var form = $('#saveForm').serialize();
		alert(typeof form);
		alert(form);*/
		var form = $('#saveForm').serialize();
		$.post("/crm/workbench/activity/saveOrUpdate",form,function (data) {
			//data:resultVo
			if(data.ok){
				alert(data.message)
				//查询最新数据
				refresh(1,2);
			}
		},'json');
	}

	//全选和反选bind
	$('#father').click(function () {
		//attr prop 方式一
		/*if($(this).prop('checked')){
			$('.son').prop('checked',true);
		}else{
			$('.son').prop('checked',false);
		}*/
		//方式二
		$('.son').prop('checked',$(this).prop('checked'));
	});


	//当给动态生成的元素添加事件函数，会自动的把元素创建出来
	function change() {
		//获取勾中的son的个数
		var checkedLength = $('.son:checked').length;
		//获取所有son的个数
		var length = $('.son').length;

		if(checkedLength == length){
			//全部勾中了
			$('#father').prop('checked',true);
		}else{
			$('#father').prop('checked',false);
		}
	}

	//son√中的个数决定father是否勾中 判断son勾中的个数和所有son的个数是否相等
	//事件委托 动态生成的元素js会实效，把生成的元素的事件委托给其第一个不是动态生成的父元素
	//参数1:事件名称 参数2:委托的元素 参数3:触发的函数
	/*$('#activityBody').on('click','.son',function () {
		//获取勾中的son的个数
		var checkedLength = $('.son:checked').length;
		//获取所有son的个数
		var length = $('.son').length;

		if(checkedLength == length){
			//全部勾中了
			$('#father').prop('checked',true);
		}else{
			$('#father').prop('checked',false);
		}
	})*/
	
	//修改js
	function openUpdateModal() {
		var checkedLength = $('.son:checked').length;

		if(checkedLength == 0){
			layer.alert("至少选中一条记录", {icon: 5});
		}else if(checkedLength > 1){
			layer.alert("只能操作一条记录", {icon: 5});
		}else{
			//弹出修改模态窗口 show hide
			$('#editActivityModal').modal('show');
			//获取勾中的son的主键值
			/**
			 * js jquery
			 * var person; $(person)
			 * $(person).get(0) $(person)[0]
			 * @type {*|jQuery|*|*}
			 */
			var id = $($('.son:checked')[0]).val();

			//根据查询市场活动数据
			$.get("/crm/workbench/activity/queryById",{'id':id},function (data) {
				//data:activity
				var activity = data;
				//把查询出来的数据设置到页面上
				$('#edit-marketActivityName').val(activity.name);
				$('#edit-startTime').val(activity.startDate);
				$('#edit-endTime').val(activity.endDate);
				$('#edit-cost').val(activity.cost);
				$('#edit-describe').val(activity.description);
				//设置主键到隐藏域中
				$('#id').val(activity.id);

				$('#edit-marketActivityOwner').val(activity.owner);
				//异步查询所有者信息
				/*$.get("/crm/workbench/activity/queryUsers",function (data) {
					//data:List<User> create-marketActivityOwner
					var content = "";
					for(var i = 0; i < data.length; i++){
						var user = data[i];
						/!*if(activity.owner == user.id){
							content += "<option selected value="+user.id+">"+user.name+"</option>";
						}else{
							content += "<option value="+user.id+">"+user.name+"</option>";
						}*!/
						content += "<option value="+user.id+">"+user.name+"</option>";
					}
					//append:没添加一条数据会把页面上的所有dom元素都重新生成
					$('#edit-marketActivityOwner').html(content);
					//直接选中满足值的对应的option
					$('#edit-marketActivityOwner').val(activity.owner);
				},'json');*/

			},'json');

		}
	}
	
	function deleteBatch() {
		var checkedLength = $('.son:checked').length;
		if(checkedLength == 0){
			layer.alert("至少选中一条记录", {icon: 5});
		}else{
			//信息框-例2
			layer.alert("确定删除勾中的"+checkedLength+"条数据吗？", {
				time: 0 //不自动关闭
				,btn: ['确定', '取消']
				,yes: function(index){
					//点击确定按钮后，关闭弹窗
					layer.close(index);
					//异步删除
					//获取勾中的son的主键
					var ids = [];
					$('.son:checked').each(function () {
						var id = $(this).val();
						ids.push(id);
					});
					//join:把数组中的内容以指定分隔符的方式拼接成字符串，分隔符默认是逗号 1:2:3
				    //异步删除
					$.post("/crm/workbench/activity/deleteBatch",{'ids':ids.join()},function (data) {
						//data:resultVo
						if(data.ok){
							layer.alert(data.message, {icon: 6});
							//刷新数据
							refresh(1,2);
						}
					},'json');
				}
			});
		}
	}
	//更新市场活动
	function updateActivity() {
		$.post("/crm/workbench/activity/saveOrUpdate",$('#updateForm').serialize(),function (data) {
			//resultVo
			if(data.ok){
				layer.alert(data.message, {icon: 5});
				//查询最新数据
				refresh(1,2);
			}
		},'json');
	}

	function exportExcel() {
		location.href = "/crm/workbench/activity/exportExcel";
	}
</script>
</body>
</html>