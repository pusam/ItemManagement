<%--
  Created by IntelliJ IDEA.
  User: nets
  Date: 2019-02-13
  Time: 오후 4:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css" />
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>
<script>
    $("#datepicker").datepicker();

    $(function () {
        document.getElementById('startDate').value = new Date().toISOString().substring(0, 10);
        document.getElementById('endDate').value = new Date().toISOString().substring(0, 10);
        $( "#startDate" ).datepicker({
            changeMonth: true,
            dateFormat : "yy-mm-dd",
            dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
            dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'],
            monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
            monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
        });
        $( "#endDate" ).datepicker({
            changeMonth: true,
            dateFormat : "yy-mm-dd",
            dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
            dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'],
            monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
            monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
        });

        $("#totalBtn").click(function () {
            if ($("#startDate").val() == "" || $("#endDate").val() == "") {
                alert("시작 날짜와 끝 날짜를 입력해주세요");
                return;
            }

            var date = {
                startDate: $("#startDate").val(),
                endDate: $("#endDate").val(),
            };
            $.ajax({
                type: "POST",
                data: date,
                dataType: "json",
                url: "totalCnt.do",
                success: function (data) {
                    $("#totalList tr").remove();
                    for (var key in data) {
                        $('#totalList').append("<tr><td>" + key + "</td><td>" + data[key] + "원</td></tr>")
                    }
                },
                error: function (xvr) {
                    alert("서버에 문제가 있습니다. 잠시 후 다시 시도해주세요.");
                }
            })
        });
    });
</script>
<body>
<div>
    <div></div>
지출 내역&nbsp;&nbsp;&nbsp; <input type="button" value="뒤로가기" onclick="history.back(-1);"/><br/>
날짜 입력 <input type="text" id="startDate" name="startDate" maxlength="10" value="${startDate}" readonly/><input
        type="text" id="endDate" name="endDate" max="10" value="${endDate}" readonly }/><input type="button"
                                                                                      value="클릭"
                                                                                      id="totalBtn"/>
<table id="totalList" name="totalList">
<c:forEach var="tl" items="${tcList}">
    <tr><td>${tl.artiKinds}</td><td>${tl.artiMoney}</td></tr>
</c:forEach>
</div>
</table>
</body>
</html>
