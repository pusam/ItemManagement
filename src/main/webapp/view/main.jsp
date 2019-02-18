<%--
  Created by IntelliJ IDEA.
  User: nets
  Date: 2019-02-07
  Time: 오전 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>가계부</title>
    <style>
        table {
            border: 1px solid #444444;
            border-collapse: collapse;
        }

        th, td {
            /*width: 150px;*/
            border: 1px solid #444444;
            text-align: center;
        }

        th {
            background-color: darkgrey;
            color: white;
        }

    </style>
</head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>
<script>
    $(function () {

        document.getElementById('date').value = new Date().toISOString().substring(0, 10);
        $("#money").val().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        $("#regDate").datepicker({
            changeMonth: true,
            dateFormat: "yy-mm-dd",
            dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
            dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'],
            monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
            monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
        });

        $("#date").datepicker({
            changeMonth: true,
            dateFormat: "yy-mm-dd",
            dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
            dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'],
            monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
            monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
        });

        $("#add").click(function () {
            if ($("#date").val() == "" || $("#money").val() == "") {
                alert("날짜와 금액은 필수 입니다.")
                return;
            }

            var kinds_data = {
                date: $("#date").val(),
                kinds: $("#kinds").val(),
                money: $("#money").val(),
                useRea: $("#use_rea").val()
            };
            $.ajax({
                type: "GET",
                data: kinds_data,
                url: "expAdd.do",
                success: function (data) {
                    switch (data) {
                        case 0 : alert("추가 중에 문제가 발생하였습니다. 잠시 후에 다시 시도해주세요."); return;
                        case 1 : alert("추가 성공"); return;
                        case 2 : alert("입력 값이 너무 큽니다. 날짜는 YYYY-MM-DD 형식만 가능 합니다."); return;
                        case 3 : alert("박스에 있는 값만 입력 가능합니다."); return;
                        case 4 : alert("금액의 범위가 너무 큽니다. 10억까지 입력 가능합니다."); return;
                        case 5 : alert("사용내역은 최대 30자리까지 입력 가능합니다."); return;
                        case 10 : alert("악의적으로 값을 바꾸지 마세요. 로그인 페이지로 이동합니다."); location.replace("../index.do"); return;
                    }
                },
                error: function (xvr) {
                    alert("서버에 문제가 있습니다. 잠시 후 다시 시도해주세요.");
                }
            })
        });

        $("#regBtn").click(function () {
            $("#frm").submit();
        });

        $("#deleteBtn").click(function () {

            var expSeqArr = Array();
            var expSeqCnt = 0;
            if ($('.checkExp').is(':checked')) {
                if (confirm("정말 삭제 하시겠습니까?")) {
                    $("input[name=checkExp]:checked").each(function () {
                        expSeqArr[expSeqCnt] = $(this).val();
                        expSeqCnt++;
                    })
                    $.ajax({
                        traditional: true,
                        type: "POST",
                        data: {
                            expSeqArr: expSeqArr,
                            regDate: $("#regDate").val()
                        },
                        dataType: "json",
                        url: "deleteExp.do",
                        success: function (data) {
                            alert(data.cnt + "건이 삭제되었습니다.")
                            // location.href="main.do";
                            $("#regDate").val(data.regDate);
                        },
                        error: function (xvr) {
                            alert("서버에 문제가 있습니다. 잠시 후 다시 시도해주세요.");
                        }
                    })
                }
            } else {
                alert("선택되지 않았습니다. 선택해주세요")
            }
        })


        $(".updateBtn").click(function () {
            var tdArr = new Array();
            var tr = $(this).parent().parent()
            var td = tr.children();
            var flagArr = {};

            td.find(":input").each(function () {
                tdArr.push(this.value)
            });
            flagArr[tdArr[0]] = $(this).val();

            if (flagArr[tdArr[0]] == "수정") {
                if (confirm("해당 데이터를 수정 하시겠습니까?")) {
                    td.find(":input").removeAttr('disabled');
                    flagArr[tdArr[0]] = "진짜 수정";
                    $(this).val("진짜 수정");
                    $(this).attr("src", "../assets/image/open.png");
                }
            } else {
                if (confirm("바뀐 데이터로 수정 하시겠습니까?")) {
                    // $(this).val("수정");
                    // flagArr[tdArr[0]]="수정";
                    var kinds_data = {
                        expSeq: tdArr[0],
                        date: tdArr[1],
                        kinds: tdArr[2],
                        money: tdArr[3],
                        useRea: tdArr[4]
                    };
                    $.ajax({
                        type: "POST",
                        data: kinds_data,
                        url: "update.do",
                        success: function (data) {
                            if (data == 1) {
                                alert("업데이트 성공");
                                // location.href = "main.do?regDate=" + tdArr[1];
                                // location.href = "main.do";
                            } else {
                                alert("업데이트에 실패했습니다.");
                            }
                        },
                        error: function (xvr) {
                            alert("서버에 문제가 있습니다. 잠시 후 다시 시도해주세요.");
                        }
                    })
                }
            }
        })
    })
</script>
<body style="width: 75%; margin: 0 auto;">
<div style="width: 90%;">
    <div style="width: 1000px">
        <div>
            <div style="text-align: right; padding-top: 10px; font-weight: bold; padding-right: 150px">
                ${id}님 접속을 환영 합니다. <br/>
                <a href="../logout.do">로그 아웃</a>
            </div>


        </div>
        <br/><br/>
        <a href="totalPage.do">토탈 정보</a><br/><br/>

        <div>
            <form id="frm" name="frm" method="post">
                내가 등록한 리스트<br/>
                날짜 입력 <input type="date" id="regDate" name="regDate" value="${regDate}"><input type="button" value="클릭"
                                                                                               id="regBtn" readonly
                                                                                               maxlength="8">
            </form>
            <table id="tabList" width="860px">
                <tr>
                    <th width="60px"><input type="button" id="deleteBtn" value="삭제"></th>
                    <th width="150px">날짜</th>
                    <th width="100px">물품종류</th>
                    <th width="150px">금액</th>
                    <th width="250px">사용내역</th>
                    <th width="150px">수정여부</th>
                </tr>
                <div id="divExpList">
                    <c:forEach var="ael" items="${accExpList}">
                        <tr class="test">
                            <td><input type="checkbox" name="checkExp" class="checkExp" id="checkExp"
                                       value="${ael.expSeq}"></td>
                            <td><input type="date" value="${ael.regDate}" class="listRegDate" maxlength="10" disabled>
                            </td>
                            <td><select class="listArtiKinds" disabled>
                                <option selected="selected" value="${ael.artiKinds}">${ael.artiKinds}</option>
                                <c:forEach var="ekl" items="${expKindsList}">
                                    <c:if test="${ael.artiKinds != ekl.expKinds}">
                                        <option value="${ekl.expKinds}">${ekl.expKinds}</option>
                                    </c:if>
                                </c:forEach>
                            </select></td>
                            <td><input type="text" value="${ael.artiMoney}" class="listArtiMoney" maxlength="9"
                                       disabled style="text-align: right"></td>
                            <td><input type="text" value="${ael.userRea}" class="listUserRea" maxlength="30" disabled>
                            </td>
                            <td><input type="image" class="updateBtn" value="수정" src="../assets/image/close.png"
                                       width="30px" height="30px"></td>
                        </tr>
                    </c:forEach>
                </div>

            </table>
        </div>
        <br/><br/>
        등록하기
        <div>
            <table width="860px">
                <tr>
                    <th width="60px"></th>
                    <th width="150px">날짜</th>
                    <th width="100px">물품종류</th>
                    <th width="150px">금액</th>
                    <th width="250px">사용내역</th>
                    <th width="150px">추가버튼</th>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="text" id="date" maxlength="8" readonly></td>
                    <td><select id="kinds">
                        <c:forEach var="ekl" items="${expKindsList}">
                            <option value="${ekl.expSeq}">${ekl.expKinds}</option>
                        </c:forEach>
                    </select></td>
                    <td><input type="number" id="money" maxlength="9"></td>
                    <td><input type="text" id="use_rea" maxlength="30"></td>
                    <td><input type="button" value="추가" id="add"></td>
                </tr>
            </table>
        </div>

    </div>
</div>
</body>
</html>
