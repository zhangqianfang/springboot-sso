<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<!DOCTYPE html>  
<html>  
<head>  
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
  <title>登录页面</title>
  <link href="${ctx }/static/css/admin/icon.css" rel="stylesheet">
  <link href="${ctx }/static/css/admin/common.css" rel="stylesheet">
  <link href="${ctx }/static/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
  <script type="text/javascript" src="${ctx }/static/scripts/jquery-1.11.1.js"></script>
</head>

<body style="min-height: 740px;">
  <div class="login-body clearfix">
    <div class="login-main clearfix">
      <div class="user-login fr">
        <img class='drop' src="${ctx }/static/css/image/drop.png">
        <p class="login-fwxt" style="left:150px;">单点登录系统</p>
        <button onclick="check()" class='denglu'>登录</button>
        <div class="login">
          <div class="h-46 mb-20" style='margin-top: 160px;'>
            <!--<span class="icon-img user-icon"></span>-->
            <i class="iconfont icon-zhanghuguanli thisicon"></i>
            <input type="text" placeholder="请输入用户名" id="username" name="username" class='bgcolor userinfo' style="background: #000;" value="${username}">
          </div>
          <div class="h-46 mb-20">
            <!--<span class="icon-img password-icon"></span>-->
            <i class="iconfont icon-mima thisicon"></i>
            <input type="password" placeholder="请输入密码" id="password" name="password" class='bgcolor userinfo' style="background: #000;" value="${password}">
          </div>
          <div class="h-46" style="border: 1px solid #849295; width: 201px; border-radius: 7px; position: relative;">
            <i class="iconfont icon-ecurityCode thisicon"></i>
            <input id="verifyCode" name="verifyCode" class="small" type="text" class='bgcolor' style="background: #000; font-size: 16px; color: #fff;" placeholder="验证码" />
            <img id="kaptchaImage" class="code-img" title="验证码" src="${ctx}/sso/getVerifyCode" style="cursor: pointer; top: 0px !important;" />
          </div>
          <div>
            <span style=""><font color=#ff0000 id="errorMess"></font></span>
            <!-- 提示 -->
            <div id="warningDiv" style="margin: 0px 0; color: red">${loginError}</div>
          </div>
        </div>
      </div>
    </div>
  </div>

<script type="text/javascript">
$(function() {
    $('#username').val('');
    $('#password').val('');
    $('#verifyCode').val('');
    // 生成验证码  
    $('#kaptchaImage').click(function() {
        $(this).hide().attr('src', '${ctx}/sso/getVerifyCode?random='+ Math.random()).fadeIn();
        event.cancelBubble = true;
    });
    // 键盘绑定回车事件
    document.onkeydown = function(e) {
        var e = window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 13) {
            return check();
        }
    }
});

function changeCode() {
    $('#kaptchaImage').hide().attr('src', '${ctx}/sso/getVerifyCode?random=' + Math.random()).fadeIn();
    event.cancelBubble = true;
    $("#kaptchaImage").click(function() {
        getVerifyCode();
    });
}

// 刷新验证码
function getVerifyCode() {
    $("#kaptchaImage").attr("src", "${ctx}/sso/getVerifyCode?random=" + Math.random());
}

function check() {
  
    if (username.length == 0) {
        $("#errorMess").text('请输入用户名!');
        $("#username").focus();
        return false;
    } else if (password.length == 0) {
        $("#errorMess").text('请输入密码!');
        $("#password").focus();
        return false;
    } else if (verifyCode.length == 0) {
        $("#errorMess").text('请输入验证码!');
        $("#verifyCode").focus();
        return false;
    }
    $.ajax({
        url: '${ctx}/sso/login?clientUrl=${clientUrl}',
        contentType: 'application/json;charset=UTF-8',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(getLoginData()),
        success: function(data) {
            console.log(data);
            if (data.success == true) {
                //window.location.href = '${ctx}/main';
                window.location.href = data.clientUrl==null?"":data.clientUrl;
            } else {
                changeCode();
                $("#errorMess").text(data.loginError+"");
                // 防止暴力链接
                $('#username').html('');
                $('#password').html('');
            }
        },
        error: function(e) {
            $("#errorMess").text("网络异常，请稍后重试!");
        }
    });
}

function getLoginData(){
  return {
    username: $('#username').val(),
    password: $('#password').val(),
    verifyCode: $('#verifyCode').val()
  }
}
</script>

</body>
</html> 