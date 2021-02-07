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
<div class="mx-auto w-25 my-5">
    <form method="post" action="${contextPath}/admin/create/roomtypes">
        <div class="mb-3">
            <label for="nameEN" class="form-label"><span class="flag-icon flag-icon-us"></span> Roomtype name</label>
            <input type="text" class="form-control" id="nameEN" required name="nameEN">
        </div>
        <div class="mb-3">
            <label for="nameRU" class="form-label"><span class="flag-icon flag-icon-ru"></span> Название вида</label>
            <input type="text" class="form-control" id="nameRU" required name="nameRU">
        </div>
        <div class="mb-3">
            <label for="nameUA" class="form-label"><span class="flag-icon flag-icon-ua"></span> Назва виду</label>
            <input type="text" class="form-control" id="nameUA" required name="nameUA">
        </div>
        <div class="mb-3">
            <label for="price" class="form-label"><fmt:message key="application.price"/></label>
            <input class="form-control" id="price" type="number" required name="price">
        </div>
        <div class="mb-3">
            <label for="capacity" class="form-label"><fmt:message key="application.capacity"/></label>
            <input class="form-control" id="capacity" type="number" required name="capacity">
        </div>
        <div class="mb-3">
            <label for="img" class="form-label"><fmt:message key="admin.imgfilename"/></label>
            <input class="form-control" id="img" type="text" required name="image">
        </div>
        <div class="mb-3">
            <select class="form-select" name="roomtype" required>
                <option disabled selected value=""><fmt:message key="room.class"/></option>
                <c:forEach var="roomtype" items="${roomtypes}">
                    <option value="${roomtype.id}">${roomtype.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3"><fmt:message key="room.roomincludes"/>
            <ul class="list-inline">
                <c:forEach var="amenity" items="${amenities}" varStatus="status">
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="checkbox" name="amenity"
                               id="${'id'.concat(amenity.id).concat(status)}" value="${amenity.id}">
                        <label class="form-check-label"
                               for="${'id'.concat(amenity.id).concat(status)}">${amenity.name}</label>
                    </div>
                </c:forEach>
            </ul>
        </div>
        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="create.roomtype"/></button>
    </form>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>
