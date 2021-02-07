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
    <title>Login</title>
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
<div class="mx-auto w-25 my-5">
    <form method="post" action="login">
        <div class="mb-3">
            <label for="email" class="form-label"><fmt:message key="login.email"/></label>
            <input type="email" class="form-control" id="email" required name="email">
        </div>
        <div class="mb-3">
            <label for="password" class="form-label"><fmt:message key="login.password"/></label>
            <input type="password" class="form-control" id="password" required name="password">
        </div>
        <button type="submit" class="btn btn-primary"><fmt:message key="header.login"/></button>
        <div class="text-center mt-2">
            <p><fmt:message key="login.questiontext"/> <a class = "text-decoration-none" href="register"><fmt:message key="login.registerhref"/></a></p>
        </div>
    </form>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</body>
</html>
