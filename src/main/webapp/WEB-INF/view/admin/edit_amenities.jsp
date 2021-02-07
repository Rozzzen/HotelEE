<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 05.02.2021
  Time: 17:17
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
<div class = "mt-4" style = "padding-left: 7%">
    <a href = "${contextPath}/admin/create/amenities" class="btn btn-primary" type="button">
        <fmt:message key="create.newamenity"/>
    </a>
</div>
<div class="row mx-4 my-4">
    <c:forEach var="amenity" items="${amenities}">
        <div class="col-sm-4 my-3">
            <div class="card">
                <div class="card-header"># ${amenity.id}</div class="card-header">
                <div class="card-body">
                    <form action="${contextPath}/admin/edit/amenities" method="POST">
                        <h4><input class="form-control form-control-lg" id="name" type="text"
                                   name="name"
                                   value="${amenity.name}">
                        </h4>
                        <div class="col text-center">
                            <button type="submit" class="btn btn-success mt-3 px-4"><fmt:message key="edit"/></button>
                        </div>
                        <input type = hidden name="message" value="Edit">
                        <input type = hidden name="amenityId" value="${amenity.id}">
                    </form>
                    <form action="${contextPath}/admin/edit/amenities" method="POST" style="margin:0;">
                        <div class="col text-center">
                            <button type="submit" class="btn btn-danger"><fmt:message key="remove"/></button>
                        </div>
                        <input type = hidden name="message" value="Remove">
                        <input type = hidden name="amenityId" value="${amenity.id}">
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
