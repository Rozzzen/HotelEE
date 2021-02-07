<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 05.02.2021
  Time: 17:57
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
    <form method="post" action="${contextPath}/admin/create/rooms">
        <div class="mb-4">
            <label for="roomNum"><fmt:message key="application.roomnumber"/></label>
            <input pattern="^[0-9]{3,4}$" type="text" class="form-control" id="roomNum" placeholder="" required name="roomNum">
        </div>
        <div class="mb-4">
            <select class="form-control" id="subtype" required name="subtypeId">
                <option value="" selected disabled><fmt:message key="booking.roomtype"/></option>
                <c:forEach items="${subtypes}" var="subtype">
                    <option value="${subtype.id}">${subtype.subname}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-4">
            <select class="form-control" id="windowView" required name="windowViewId">
                <option value="" selected disabled><fmt:message key="window.view"/></option>
                <c:forEach items="${windowViews}" var="windowView">
                    <option value="${windowView.id}">${windowView.name}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="create.room"/></button>
    </form>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>
