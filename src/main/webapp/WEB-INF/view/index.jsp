<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 22.01.2021
  Time: 1:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
<c:if test = "${sessionScope.success != null}">
    <div class="alert alert-success" role="alert">${sessionScope.success}</div>
    <% request.getSession().removeAttribute("success");%>
</c:if>
<div class="p-5 text-center mt-3 mb-3">
    <h1 class="mb-3"><fmt:message key="hotel.name"/></h1>
    <p><fmt:message key="project.desctiption1"/></p>
    <p><fmt:message key="project.description2"/></p>
    <p><fmt:message key="project.description3"/></p>
    <p><fmt:message key="project.description4"/></p>
    <p><fmt:message key="project.description5"/></p>
    <p><fmt:message key="project.description6"/></p>
    <p><fmt:message key="project.description7"/></p>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>