<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 30.01.2021
  Time: 17:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/view/layout/meta.jspf" %>
    <title>Confirm booking</title>
</head>
<body>

<c:set var = "isFromBooking" value = "${fn:contains(requestScope['javax.servlet.forward.servlet_path'], '/applications')}"/>

<%@ include file="/WEB-INF/view/layout/header.jspf" %>
<div class="row mt-5 mx-5">
    <div class="card col-md-4 order-md-2 px-0" style="border: none; border-left: 1px solid #e5e5e5;">
        <img class="card-img-top" src="<c:url value="/img/${room.img}"/>" alt="Card image cap">
        <div class="card-body">
            <h5 class="card-title my-2">${room.subname}</h5>
            <p class="card-text my-1"><fmt:message key="confirmation.totalprice"/>: ${room.price} UAH</p>
            <p class="card-text my-1"><fmt:message key="room.number"/> ${room.id}</p>
            <p class="card-text my-1"><fmt:message key="confirmation.bookedfrom"/>${param.checkin} <fmt:message key="to"/> ${param.checkout}</p>
        </div>
    </div>
    <div class="col-md-8 order-md-1">
        <form
                <c:if test="${isFromBooking}">
                    action="${pageContext.request.contextPath}/applications"
                    method="GET"
                </c:if>
                <c:if test="${not isFromBooking}">
                    action="${pageContext.request.contextPath}/rooms/confirmation"
                    method="POST"
                </c:if>
            >
            <h4 class="mb-3"><fmt:message key="confirmation.creditcardinfo"/></h4>
            <div class="mb-3">
                <label for="address"><fmt:message key="confirmaion.adress"/></label>
                <input type="text" class="form-control" id="address" placeholder="" required="">
            </div>
            <div class="mb-3">
                <label for="address2"><fmt:message key="confirmation.address2"/> <span class="text-muted">(<fmt:message key="confirmation.optional"/>)</span></label>
                <input type="text" class="form-control" id="address2" placeholder="">
            </div>
            <div class="d-block my-3">
                <div class="custom-control custom-radio">
                    <input id="credit" name="paymentMethod" type="radio" class="custom-control-input" checked=""
                           required="">
                    <label class="custom-control-label" for="credit"><fmt:message key="confirmation.creditcard"/></label>
                </div>
                <div class="custom-control custom-radio">
                    <input id="debit" name="paymentMethod" type="radio" class="custom-control-input" required="">
                    <label class="custom-control-label" for="debit"><fmt:message key="confirmation.debitcard"/></label>
                </div>
                <div class="custom-control custom-radio">
                    <input id="paypal" name="paymentMethod" type="radio" class="custom-control-input" required="">
                    <label class="custom-control-label" for="paypal"><fmt:message key="confirmation.paypal"/></label>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="cc-name"><fmt:message key="confirmation.nameoncard"/></label>
                    <input type="text" class="form-control" id="cc-name" placeholder="" required="">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="cc-number"><fmt:message key="confirmation.creditcardnumber"/></label>
                    <input minlength="13" maxlength="19"pattern="[0-9]*" type="text" class="form-control" id="cc-number" placeholder="" required="">
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 mb-3">
                    <label for="cc-expiration"><fmt:message key="confirmation.expiration"/></label>
                    <input pattern = "^(0[1-9]|1[0-2])\/?([0-9]{4}|[0-9]{2})$" type="text" class="form-control" id="cc-expiration" placeholder="" required="">
                </div>
                <div class="col-md-3 mb-3">
                    <label for="cc-cvv"><fmt:message key="confirmation.cvv"/></label>
                    <input pattern="^[0-9]{3,4}$" type="text" class="form-control" id="cc-cvv" placeholder="" required="">
                </div>
            </div>
            <hr class="mb-4">
            <c:if test="${not isFromBooking}">
                <input type="hidden" name="checkin" value="${param.checkin}">
                <input type="hidden" name="checkout" value="${param.checkout}">
                <input type="hidden" name="roomNum" value="${room.id}">
                <input type="hidden" name="userID" value="${user.id}">
            </c:if>
            <c:if test="${isFromBooking}">
                <input type="hidden" name="message" value="Payment">
                <input type="hidden" name="applicationId" value="${param.applicationId}">
            </c:if>
            <button class="btn btn-primary btn-lg btn-block" type="submit"><fmt:message key="application.confirm"/></button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
</html>
