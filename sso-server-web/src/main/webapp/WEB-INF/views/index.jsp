<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<!DOCTYPE html> 
<html>
  <body>
    <h1>登录成功!</h1><br><br>
    <form action="${pageContext.request.contextPath}/sso/logout" method="get">
        <input type="submit" value="注销登录">
    </form>
  </body>
</html>
