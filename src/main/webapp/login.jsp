<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="/crm/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/crm/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
	<img src="/crm/image/IMG_7114.JPG" style="width: 100%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
	<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2020&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
	<div style="position: absolute; top: 0px; right: 60px;">
		<div class="page-header">
			<h1>登录</h1>
		</div>
		<form action="workbench/index.html" class="form-horizontal" role="form">
			<div class="form-group form-group-lg">
				<div style="width: 350px;">
					<input autofocus class="form-control" id="loginAct" type="text" placeholder="用户名">
				</div>
				<div style="width: 350px; position: relative;top: 20px;">
					<input class="form-control" id="loginPwd" type="password" placeholder="密码">
				</div>

				<div style="width: 350px; position: relative;top: 40px;">
					<input style="width: 210px" class="form-control" id="code" type="text" placeholder="请输入验证码">
					<img src="/crm/code" onclick="changeCode($(this))"
						 style="cursor:pointer;width: 120px;height: 45px;position: absolute;right: 0;top:0" />
				</div>
				<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

					<span id="msg"></span>

				</div>
				<button type="button" onclick="login()" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
			</div>
		</form>
	</div>
</div>
	<script src="/crm/jquery/layer/layer.js"></script>
<script>


	if(window.top != window){
		top.location.href = "/crm/login.jsp";
	}

	//触发单击事件
	function login(){

	}

	//触发回车事件 keypress = keydown + keyup
	$('body').keypress(function (event) {
		//event:当前触发的事件对象
	   if(event.keyCode == 13){
	   	//按下回车键
		   $.post("/crm/settings/user/login",{
			   'loginAct' : $('#loginAct').val(),
			   'loginPwd' :$('#loginPwd').val(),
			   'code' : $('#code').val()//用户输入的验证码
		   },function (data) {
			   //data:ResultVo
			   var resultVo = data;
			   if(!resultVo.ok){
				   layer.alert(resultVo.message, {icon: 5});
			   }else{
			   	 //登录成功，跳转到后台首页
			   location.href = "/crm/toView/workbench/index";
			   }
		   },'json');
	   }
	});

	/*$('body').mousemove(function (event) {
		//event:当前触发的事件对象
		$('#myDiv').css({'top':event.clientY + 'px','left':event.clientX + 'px'});
	});*/


	//点击切换图片验证码
	function changeCode($this) {
		/**
		 * 如果每次发送相同的请求，浏览器会帮我们自动缓存请求的结果
		 * 在地址后加时间戳或者随机值
		 * attr:获取元素属性 1:获取属性 2:设置属性值 设置自定义属性
		 * prop:设置固有属性
		 * w3c
		 */
		$this.prop('src','/crm/code?time='+new Date());
	}
</script>
</body>
</html>