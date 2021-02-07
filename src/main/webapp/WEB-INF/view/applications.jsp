<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 01.02.2021
  Time: 18:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/view/layout/meta.jspf" %>
    <title>Hotel</title>
</head>
<body>

<c:set var = "isAdmin" value="${sessionScope.user.role.name eq 'Admin'}"/>
<c:set var="queryString" value="${pageContext.request.queryString}"/>

<%@ include file="/WEB-INF/view/layout/header.jspf" %>
<table class="table table-hover table-responsive mt-5">
    <thead>
    <tr>
        <c:if test="${isAdmin}">
            <th scope="col">#</th>
            <th scope="col"><fmt:message key="register.firstname"/></th>
            <th scope="col"><fmt:message key="register.lastname"/></th>
            <th scope="col"><fmt:message key="register.phonenumber"/></th>
        </c:if>
        <th scope="col"><fmt:message key="application.roomtype"/></th>
        <th scope="col"><fmt:message key="application.capacity"/></th>
        <c:if test="${not isAdmin}">
            <th scope="col"><fmt:message key="application.price"/></th>
        </c:if>
        <th scope="col"><fmt:message key="application.roomnumber"/></th>
        <th scope="col"><fmt:message key="application.booked"/></th>
        <th scope="col"><fmt:message key="application.paid"/></th>
        <th scope="col"><fmt:message key="application.actions"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="application" items="${applications}">
        <tr>
            <c:if test="${isAdmin}">
                <th scope="row">${application.id}</th>
                <td>${application.user.name}</td>
                <td>${application.user.surname}</td>
                <td>${application.user.phone}</td>
            </c:if>
            <td>${application.roomType.name}</td>
            <td>${application.capacity}</td>
            <c:if test="${not isAdmin}">
                <td>
                    <c:choose>
                        <c:when test ="${application.roomType.price eq 0}">?</c:when>
                        <c:when test ="${application.roomType.price ne 0}">${application.roomType.price}</c:when>
                    </c:choose>
                </td>
            </c:if>
            <td>
                <c:choose>
                    <c:when test ="${application.room.number eq 0}"><fmt:message key="application.notassigned"/></c:when>
                    <c:when test ="${application.room.number ne 0}">${application.room.number}</c:when>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${application.isBooked eq 0}"><fmt:message key="no"/></c:when>
                    <c:when test="${application.isBooked ne 0}"><fmt:message key="yes"/></c:when>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${application.isPaid eq 0}"><fmt:message key="no"/></c:when>
                    <c:when test="${application.isPaid ne 0}"><fmt:message key="yes"/></c:when>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test ="${isAdmin}">
                        <div class="row" style="margin:0; padding:0">
                            <c:if test="${application.room.number eq 0}">
                                <div class="col" style="margin:0; padding:0">
                                    <form action="${contextPath}/admin/applications" method="GET" style ="margin: 0; padding: 0">
                                        <input type="hidden" name="checkin" value="${application.checkin}">
                                        <input type="hidden" name="checkout" value="${application.checkout}">
                                        <input type="hidden" name="applicationId" value="${application.id}">
                                        <input type="hidden" name="message" value="Find room">
                                        <button type="submit" class="btn btn-sm btn-success"><fmt:message key="application.selectroom"/></button>
                                    </form>
                                </div>
                            </c:if>
                            <c:if test="${application.isPaid eq 0}">
                                <div class="col" style="margin:0; padding:0">
                                    <form action="${contextPath}/admin/applications" method="GET" style ="margin: 0; padding: 0">
                                        <input type="hidden" name="applicationId" value="${application.id}">
                                        <input type="hidden" name="message" value="Reject">
                                        <button type="submit" class="btn btn-sm btn-danger"><fmt:message key="application.reject"/></button>
                                    </form>
                                </div>
                            </c:if>
                            <c:if test="${application.isBooked ne 0 and application.isPaid ne 0}"><fmt:message key="application.booked"/></c:if>
                        </div>
                    </c:when>
                    <c:when test="${not isAdmin and application.room.number eq 0}">
                        <fmt:message key="application.adminmessage"/>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${application.isBooked eq 0}">
                                <div class="row" style ="margin: 0; padding: 0">
                                    <div class="col" style ="margin: 0; padding: 0">
                                        <form action="${contextPath}/applications" method="GET" style ="margin: 0; padding: 0">
                                            <input type="hidden" name="applicationId" value="${application.id}">
                                            <input type="hidden" name="message" value="Confirm">
                                            <button type="submit" class="btn btn-sm btn-success"><fmt:message key="application.confirm"/></button>
                                        </form>
                                    </div>
                                    <div class="col" style ="margin: 0; padding: 0">
                                        <form action="${contextPath}/applications" method="GET"
                                              style="margin: 0; padding: 0">
                                            <input type="hidden" name="applicationId" value="${application.id}">
                                            <input type="hidden" name="message" value="Reject">
                                            <button type="submit" class="btn btn-sm btn-danger"><fmt:message key="application.requestchange"/></button>
                                        </form>
                                    </div>
                                </div>
                            </c:when>
                            <c:when test="${application.isBooked ne 0 and application.isPaid eq 0}">
                                <form action="${contextPath}/applications" method="POST"
                                      style="margin: 0; padding: 0">
                                    <input type="hidden" name="checkin" value="${application.checkin}">
                                    <input type="hidden" name="checkout" value="${application.checkout}">
                                    <input type="hidden" name="roomNum" value="${application.room.number}">
                                    <input type="hidden" name="applicationId" value="${application.id}">
                                    <button type="submit" class="btn btn-sm btn-primary"><fmt:message key="application.paymentconfirm"/></button>
                                </form>
                            </c:when>
                            <c:otherwise><fmt:message key="application.successbook"/></c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%@ include file="/WEB-INF/view/layout/pagination.jspf"%>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>
