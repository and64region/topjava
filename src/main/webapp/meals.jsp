<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>TopJava</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<table border=1>
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan=2>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealsListWithExcees}" var="mealTo">


        <c:if test="${mealTo.excess}">
            <c:set var="col" value="color:#FF0000"/>
        </c:if >
        <c:if test="${!mealTo.excess}">
            <c:set var="col" value="color:#008000"/>
        </c:if>
        <tr>
            <td style="${col}"><fmt:parseDate value="${ mealTo.dateTime }" pattern="yyyy-MM-dd'T'HH:mm"
                                              var="parsedDateTime"
                                              type="both"/>
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/></td>
            <td style="${col}"><c:out value="${mealTo.description}"/></td>
            <td style="${col}"><c:out value="${mealTo.calories}"/></td>
            <td style="${col}"><a href="mealServlet?action=edit&userId=<c:out value="${mealTo.id}"/>">Редактировать</a></td>
            <td style="${col}"><a href="mealServlet?action=delete&userId=<c:out value="${mealTo.id}"/>">Удалить</a></td>

        </tr>
    </c:forEach>
    </tbody>
</table>

<p><a href="mealServlet?action=insert">Добавить</a></p>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>
