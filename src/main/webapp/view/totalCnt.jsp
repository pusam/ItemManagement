<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    호호호 ${param.totalCnt}
    <c:forEach var="tc" items="${param.totalCnt}">
        <tr>
<%--            <td>${tc.artiKinds}</td>
            <td>${tc.artiMoney}</td>--%>
        </tr>
    </c:forEach>
</table>

