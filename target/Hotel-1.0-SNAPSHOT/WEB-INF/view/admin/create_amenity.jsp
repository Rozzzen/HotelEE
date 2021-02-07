<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 05.02.2021
  Time: 19:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/view/layout/meta.jspf" %>
    <title>Hotel</title>
</head>
<body>
<%@ include file="/WEB-INF/view/layout/header.jspf" %>
<c:if test="${sessionScope.success != null}">
<div class="alert alert-success" role="alert">${sessionScope.success}</div>
        <% request.getSession().removeAttribute("success");%>
</c:if>
<div class="mx-auto w-25 my-5">
    <form method="post" action="${contextPath}/admin/create/amenities">
        <div class="mb-3">
            <label for="nameEN" class="form-label"><span class="flag-icon flag-icon-us"></span> Amenity name</label>
            <input type="text" class="form-control" id="nameEN" required name="nameEN">
        </div>
        <div class="mb-3">
            <label for="nameRU" class="form-label"><span class="flag-icon flag-icon-ru"></span> Название удобства</label>
            <input type="text" class="form-control" id="nameRU" required name="nameRU">
        </div>
        <div class="mb-3">
            <label for="nameUA" class="form-label"><span class="flag-icon flag-icon-ua"></span> Назва зручності</label>
            <input type="text" class="form-control" id="nameUA" required name="nameUA">
        </div>
        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="create.newamenity"/></button>
    </form>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>
