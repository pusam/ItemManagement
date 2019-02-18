<%--
  Created by IntelliJ IDEA.
  User: nets
  Date: 2019-02-07
  Time: 오전 10:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script>
$(function () {
    $("#login").click(function () {
        var login_data = {
            id : $("#id").val(),
            pwd : $("#pwd").val()
        };
        $.ajax({
            type : "POST",
            data : login_data,
            url : "login.do",
            success :  function (data) {
                if(data==1){
                    location.replace(login_data.id+"/main.do");
                }else{
                    alert("아이디 또는 패스워드를 확인해주세요.")
                }
            },
            error : function (xvr) {
              alert("서버와 연결이 원활하지 않습니다. 잠시 후 다시 시도해주세요.")
            }
        })
    })
});
</script>
<body>
가계부 로그인<br/>
<input type="text" id="id" placeholder="아이디">
<input type="password" id="pwd" placeholder="패스워드">
<input type="button" value="로그인" id="login">
</body>
</html>
