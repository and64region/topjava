<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table border=1>
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealsListWithExcees}" var="mealTo">
        <tr>
            <td><fmt:parseDate value="${ mealTo.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /></td>
            <%--<td><fmt:formatDate pattern="yyyy-MMM-dd" value="${mealTo.getDateTime}" /></td>--%>
            <td><c:out value="${mealTo.description}" /></td>
            <td><c:out value="${mealTo.calories}" /></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
