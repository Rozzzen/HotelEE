<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 26.01.2021
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <%@ include file="/WEB-INF/view/layout/meta.jspf" %>
    <title>Booking</title>
</head>
<body>
<%@ include file="/WEB-INF/view/layout/header.jspf" %>
<div class="mx-auto w-25 my-5">
    <form method="post" action="booking">
        <div class="mb-4">
            <select class="form-control" id="roomtype" required name="roomtypeID">
                <option value="" selected disabled><fmt:message key="booking.roomtype"/></option>
                <c:forEach items="${roomtypes}" var="roomtype">
                    <option value="${roomtype.id}">${roomtype.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-4">
            <select class="form-control" id="capacity" required name="capacity">
                <option value="" selected disabled><fmt:message key="booking.capacity"/></option>
                <c:forEach items="${capacities}" var="capacity">
                    <option value="${capacity}">${capacity}</option>
                </c:forEach>
            </select>
        </div>
        <div class="row mb-4">
            <div class="col-sm-6">
                <label for="check_in" class="form-label"><fmt:message key="rooms.checkin"/></label>
                <input class="form-control" id="check_in"
                       value="${requestScope.checkin}" type="date" name="checkin"
                       onchange="check_out.min = this.value" required>
            </div>
            <div class="col-sm-6">
                <label for="check_out" class="form-label"><fmt:message key="rooms.checkout"/></label>
                <input class="form-control" id="check_out"
                       value="${requestScope.checkout}" type="date" name="checkout" required>
            </div>
        </div>
        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="booking.submitapplication"/></button>
    </form>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
<script src = ${pageContext.request.contextPath}/js/DateValidation.js></script>
</html>
