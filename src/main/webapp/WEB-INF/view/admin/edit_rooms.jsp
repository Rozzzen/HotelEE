<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 03.02.2021
  Time: 21:25
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
<c:if test = "${sessionScope.alert != null}">
<div class="alert alert-danger" role="alert">${sessionScope.alert}</div>
        <% request.getSession().removeAttribute("alert");%>
</c:if>
<div class = "mt-4" style = "padding-left: 7%">
    <a href = "${contextPath}/admin/create/rooms" class="btn btn-primary" type="button">
        <fmt:message key="create.room"/>
    </a>
</div>
<div class="row mx-4 my-4">
    <c:forEach var="room" items="${rooms}">
        <div class="col-sm-4 my-3">
            <div class="card">
                <div class="card-header"><fmt:message key="room.number"/> ${room.number}</div class="card-header">
                <div class="card-body">
                    <form action="${contextPath}/admin/edit/rooms" method="POST">
                        <select class="form-select form-select-lg mb-3" name="subtype">
                            <option selected value="${room.roomType.id}">${room.roomType.subname}</option>
                            <c:forEach var="subtype" items="${subtypes}">
                                <c:if test="${subtype.subname ne room.roomType.subname}">
                                    <option value="${subtype.id}">${subtype.subname}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <img src="<c:url value="/img/${room.roomType.img}"/>" class="card-img-top"
                             alt="${room.roomType.subname}">
                        <ul class="list-group">
                            <li class="list-group-item"><fmt:message key="room.price"/> ${room.roomType.price} UAH</li>
                            <li class="list-group-item"><fmt:message key="room.capacity"/> ${room.roomType.capacity}</li>
                            <li class="list-group-item"><fmt:message key="room.class"/> ${room.roomType.name}</li>
                            <li class="list-group-item"><fmt:message key="room.roomincludes"/>
                                <ul class="list-inline">
                                    <c:forEach var="amenity" items="${room.roomType.amenityList}">
                                        <li class="list-inline-item">${amenity.name}</li>
                                    </c:forEach>
                                </ul>
                            </li>
                            <select class="form-select form-select mb-3 list-group-item" name="windowName">
                                <option selected value="${room.windowView.id}">${room.windowView.name}</option>
                                <c:forEach var="view" items="${windowViews}">
                                    <c:if test="${room.windowView.name ne view.name}">
                                        <option value="${view.id}">${view.name}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </ul>
                        <div class="col text-center">
                            <button type="submit" class="btn btn-success mt-3 px-4"><fmt:message key="edit"/></button>
                        </div>
                        <input type = hidden name="message" value="Edit">
                        <input type = hidden name="roomNum" value="${room.number}">
                    </form>
                    <form action="${contextPath}/admin/edit/rooms" method="POST" style="margin:0;">
                        <div class="col text-center">
                            <button type="submit" class="btn btn-danger"><fmt:message key="remove"/></button>
                        </div>
                        <input type = hidden name="message" value="Remove">
                        <input type = hidden name="roomNum" value="${room.number}">
                    </form>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
<%@include file="/WEB-INF/view/layout/pagination.jspf" %>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>