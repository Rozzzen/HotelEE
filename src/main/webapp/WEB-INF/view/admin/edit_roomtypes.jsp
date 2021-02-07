<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 03.02.2021
  Time: 21:34
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
    <a href = "${contextPath}/admin/create/roomtypes" class="btn btn-primary" type="button">
        <fmt:message key="create.roomtype"/>
    </a>
</div>
<div class="row mx-4 my-4">
    <c:forEach var="subtype" items="${subtypes}">
        <div class="col-sm-4 my-3">
            <div class="card">
                <div class="card-body">
                    <form action="${contextPath}/admin/edit/roomtypes" method="POST">
                        <h4><input class="form-control form-control-lg" id="subname" type="text"
                                   name="subname"
                                   value="${subtype.subname}">
                        </h4>
                        <img src="<c:url value="/img/${subtype.img}"/>" class="card-img-top"
                             alt="${subtype.subname}">
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="form-group row">
                                    <label for="price" class="col-sm-6 col-form-label"><fmt:message key="admin.pricepernightuah"/> </label>
                                    <div class="col-sm-6">
                                        <input class="form-control" id="price" type="number"
                                               name="price"
                                               value="${subtype.price}">
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group row">
                                    <label for="capacity" class="col-sm-6 col-form-label"><fmt:message key="room.capacity"/></label>
                                    <div class="col-sm-6">
                                        <input class="form-control" id="capacity" type="number" min = "0" max = "4"
                                               name="capacity"
                                               value="${subtype.capacity}">
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group row">
                                    <label for="img" class="col-sm-6 col-form-label"><fmt:message key="admin.imgfilename"/></label>
                                    <div class="col-sm-6">
                                        <input class="form-control" id="img" type="text"
                                               name="image"
                                               value="${subtype.img}">
                                    </div>
                                </div>
                            </li>
                            <select class="form-select" name="roomtype">
                                <c:forEach var="roomtype" items="${roomtypes}">
                                    <c:choose>
                                        <c:when test="${subtype.name ne roomtype.name}">
                                            <option value="${roomtype.id}">${roomtype.name}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option selected value="${roomtype.id}">${subtype.name}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                            <li class="list-group-item"><fmt:message key="room.roomincludes"/>
                                <ul class="list-inline">
                                    <c:forEach var="amenity" items="${amenities}" varStatus="status">
                                        <c:forEach var="amenity2" items="${subtype.amenityList}">
                                            <c:if test="${amenity2.id eq amenity.id}">
                                                <c:set var="isSigned" value="checked"/>
                                            </c:if>
                                        </c:forEach>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="checkbox" name ="amenity" id="${'id'.concat(amenity.id).concat(status)}" value="${amenity.id}" ${isSigned}>
                                            <label class="form-check-label" for="${'id'.concat(amenity.id).concat(status)}">${amenity.name}</label>
                                        </div>
                                        <c:set var="isSigned" value=""/>
                                    </c:forEach>
                                </ul>
                            </li>
                        </ul>
                        <div class="col text-center">
                            <button type="submit" class="btn btn-success mt-3 px-4"><fmt:message key="edit"/></button>
                        </div>
                        <input type = hidden name="message" value="Edit">
                        <input type = hidden name="subtypeId" value="${subtype.id}">
                    </form>
                    <form action="${contextPath}/admin/edit/roomtypes" method="POST" style="margin:0;">
                        <div class="col text-center">
                            <button type="submit" class="btn btn-danger"><fmt:message key="remove"/></button>
                        </div>
                        <input type = hidden name="message" value="Remove">
                        <input type = hidden name="subtypeId" value="${subtype.id}">
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
