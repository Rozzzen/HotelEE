<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 22.01.2021
  Time: 1:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/view/layout/meta.jspf" %>
    <title>Register</title>
</head>
<body>
<%@ include file="/WEB-INF/view/layout/header.jspf" %>
<c:if test = "${sessionScope.alert != null}">
    <div class="alert alert-danger" role="alert">${sessionScope.alert}</div>
    <% request.getSession().removeAttribute("alert");%>
</c:if>
<div class="mx-auto w-25 my-5">
    <form method="post" action="register"
          oninput='password2.setCustomValidity(password2.value !== password.value ? "Passwords do not match." : "")'>
        <div class="row mb-3">
            <div class="form-group col-md-6">
                <label for="name" class="form-label"><fmt:message key="register.firstname"/></label>
                <input type="text" class="form-control" id="name" required name="name">
            </div>
            <div class="form-group col-md-6">
                <label for="surname" class="form-label"><fmt:message key="register.lastname"/></label>
                <input type="text" class="form-control" id="surname" required name="surname">
            </div>
        </div>
        <div class="mb-3">
            <div class="mt-2">
                <p><fmt:message key="register.gender"/></p>
            </div>
            <c:forEach items="${genders}" var="gender">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="${gender.id}"
                           value="${gender.id}" required name="gender">
                    <label class="form-check-label" for="${gender.id}">${gender.name}</label>
                </div>
            </c:forEach>
            <div class="row">
            </div>
        </div>

        <div class="mb-3">
            <label for="email" class="form-label"><fmt:message key="login.email"/></label>
            <input type="email" class="form-control" id="email" aria-describedby="emailHelp" required name="email">
            <div id="emailHelp" class="form-text"><fmt:message key="register.emailinfo"/></div>
        </div>
        <div class="mb-3">
            <label for="phone" class="form-label"><fmt:message key="register.phonenumber"/></label>
            <input type="tel" class="form-control" id="phone" aria-describedby="phoneHelp" required name="phone">
            <div id="phoneHelp" class="form-text"><fmt:message key="register.phonenumberinfo"/></div>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label"><fmt:message key="login.password"/></label>
            <input type="password" class="form-control" id="password" required name="password">
        </div>
        <div class="mb-3">
            <label for="repeatPassword" class="form-label"><fmt:message key="register.repeatpassword"/></label>
            <input type="password" class="form-control" id="repeatPassword" required name="password2">
        </div>
        <div class="mb-3">
            <div class="mt-2">
                <p><fmt:message key="register.birthdate"/></p>
            </div>
            <div class="row">
                <div class="form-group col-md-4">
                    <select class="form-control" id="day" required name="day">
                        <option value="" selected disabled><fmt:message key="register.day"/></option>
                        <c:forEach begin="1" end="31" varStatus="loop">
                            <option>${loop.index}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group col-md-4">
                    <select class="form-control" id="month" required name="month">
                        <option value="" selected disabled><fmt:message key="register.month"/></option>
                        <c:forEach items="${months}" var="month" begin="0" end="12" varStatus="loop">
                            <option value = "${loop.index}">${month}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group col-md-4">
                    <select class="form-control" id="year" required name="year">
                        <option value="" selected disabled><fmt:message key="register.year"/></option>
                        <c:forEach begin="1940" end="2005" varStatus="loop">
                            <option>${loop.index}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
        <button type="submit" class="btn btn-primary mb-3"><fmt:message key="register.register"/></button>
    </form>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</body>
</html>
