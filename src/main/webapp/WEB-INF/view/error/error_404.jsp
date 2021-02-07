<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 07.02.2021
  Time: 16:05
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
<div class="p-5 text-center bg-light">
    <h1 class="mb-3"><fmt:message key="404.header"/></h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/" role="button"><fmt:message key="404.href"/></a>
</div>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>
