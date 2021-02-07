<%--
  Created by IntelliJ IDEA.
  User: Макс
  Date: 25.01.2021
  Time: 22:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <%@ include file="/WEB-INF/view/layout/meta.jspf" %>
    <title>Rooms</title>
</head>
<body>

<c:set var="queryString" value="${pageContext.request.queryString}"/>

<c:choose>
    <c:when test="${fn:contains(queryString, 'sortBy=')}">
        <c:set var = "SortPriceHL" value="?${fn:replace(fn:replace(queryString, 'sortBy='.concat(param.sortBy), 'sortBy='.concat('PriceHL')), '&page='.concat(currentPage), '')}"/>
        <c:set var = "SortPriceLH" value="?${fn:replace(fn:replace(queryString, 'sortBy='.concat(param.sortBy), 'sortBy='.concat('PriceLH')), '&page='.concat(currentPage), '')}"/>
        <c:set var = "SortCapacityHL" value="?${fn:replace(fn:replace(queryString, 'sortBy='.concat(param.sortBy), 'sortBy='.concat('CapacityHL')), '&page='.concat(currentPage), '')}"/>
        <c:set var = "SortCapacityLH" value="?${fn:replace(fn:replace(queryString, 'sortBy='.concat(param.sortBy), 'sortBy='.concat('CapacityLH')), '&page='.concat(currentPage), '')}"/>
        <c:set var = "SortAppartment" value="?${fn:replace(fn:replace(queryString, 'sortBy='.concat(param.sortBy), 'sortBy='.concat('Appartment')), '&page='.concat(currentPage), '')}"/>
        <c:set var = "SortAviability" value="?${fn:replace(fn:replace(queryString, 'sortBy='.concat(param.sortBy), 'sortBy='.concat('Aviability')), '&page='.concat(currentPage), '')}"/>
    </c:when>
    <c:otherwise>
        <c:set var = "SortPriceHL" value="?${fn:replace(queryString.concat('&sortBy=').concat('PriceHL'), 'page='.concat(currentPage), '')}"/>
        <c:set var = "SortPriceLH" value="?${fn:replace(queryString.concat('&sortBy=').concat('PriceLH'), 'page='.concat(currentPage), '')}"/>
        <c:set var = "SortCapacityHL" value="?${fn:replace(queryString.concat('&sortBy=').concat('CapacityHL'), 'page='.concat(currentPage), '')}"/>
        <c:set var = "SortCapacityLH" value="?${fn:replace(queryString.concat('&sortBy=').concat('CapacityLH'), 'page='.concat(currentPage), '')}"/>
        <c:set var = "SortAppartment" value="?${fn:replace(queryString.concat('&sortBy=').concat('Appartment'), 'page='.concat(currentPage), '')}"/>
        <c:set var = "SortAviability" value="?${fn:replace(queryString.concat('&sortBy=').concat('Aviability'), 'page='.concat(currentPage), '')}"/>
    </c:otherwise>
</c:choose>

<c:set var = "isAdminPath" value = "${fn:contains(requestScope['javax.servlet.forward.servlet_path'], 'admin/applications')}"/>

<%@ include file="/WEB-INF/view/layout/header.jspf" %>
<c:if test="${not isAdminPath}">
    <form method="GET" action="rooms" style = "padding-left: 7%">
        <div class="row mt-4 mb-2">
            <div class="col-sm-5">
                <label for="check_in" class="form-label"><fmt:message key="rooms.checkin"/></label>
                <input class="form-control" id="check_in"
                       value="${requestScope.checkin}" type="date" name="checkin"
                       onchange="check_out.min = this.value" required>
            </div>
            <div class="col-sm-5">
                <label for="check_out" class="form-label"><fmt:message key="rooms.checkout"/></label>
                <input class="form-control" id="check_out"
                       value="${requestScope.checkout}" type="date" name="checkout" required>
            </div>
            <div class="col-sm-2 mt-auto">
                <button type="submit" class="btn btn-success"><fmt:message key="rooms.search"/></button>
            </div>
        </div>
    </form>
</c:if>
<c:if test="${not empty param.checkin and not empty param.checkout}">
    <div class="dropdown mt-2" style = "padding-left: 7%">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                data-bs-toggle="dropdown" aria-expanded="false">
            <fmt:message key="sort.sort"/>
        </button>
        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <li><a class="dropdown-item" href="${SortPriceHL}"><fmt:message key="sort.pricehl"/></a></li>
            <li><a class="dropdown-item" href="${SortPriceLH}"><fmt:message key="sort.pricelh"/></a></li>
            <li><a class="dropdown-item" href="${SortCapacityHL}"><fmt:message key="sort.capacityhl"/></a></li>
            <li><a class="dropdown-item" href="${SortCapacityLH}"><fmt:message key="sort.capacitylh"/></a></li>
            <li><a class="dropdown-item" href="${SortAppartment}"><fmt:message key="sort.class"/></a></li>
            <li><a class="dropdown-item" href="${SortAviability}"><fmt:message key="sort.aviability"/></a></li>
            <c:if test="${fn:contains(queryString, 'sortBy=')}">
                <li><a class="dropdown-item" href="?${fn:replace(queryString, '&sortBy='.concat(param.sortBy), '')}">
                    <fmt:message key="sort.default"/>
                </a></li>
            </c:if>
        </ul>
    </div>
    <div class="row mx-4 my-4">
        <c:forEach var="room" items="${rooms}">
            <div class="col-sm-4 my-3">
                <div class="card">
                    <div class="card-header"><fmt:message key="room.number"/> ${room.number}</div class="card-header">
                    <div class="card-body">
                        <h4>${room.roomType.subname}</h4>
                        <img src="<c:url value="/img/${room.roomType.img}"/>" class="card-img-top"
                             alt="${room.roomType.subname}">
                        <ul class="list-group">
                            <li class="list-group-item"><fmt:message key="room.price"/> ${room.roomType.price} UAH</li>
                            <li class="list-group-item"><fmt:message key="room.capacity"/> ${room.roomType.capacity}</li>
                            <li class="list-group-item"><fmt:message key="room.class"/> ${room.roomType.name}</li>
                            <li class="list-group-item"><fmt:message key="room.roomincludes"/>
                                <ul class="list-inline">
                                    <c:forEach var="amenity" items="${room.roomType.amenityList}">
                                        <li class="list-inline-item">${amenity.name}</li>
                                    </c:forEach>
                                </ul>
                            </li>
                            <li class="list-group-item">${room.windowView.name}</li>
                        </ul>
                        <div class="col text-center">
                            <c:choose>
                                <c:when test="${room.status ne 'Free'}">
                                    <c:choose>
                                        <c:when test="${room.status eq 'Repairment'}">
                                            <a class="btn btn-warning disabled mt-3 px-4"><fmt:message key="room.repairment"/></a>
                                        </c:when>
                                        <c:when test="${room.status eq 'Occupied'}">
                                            <a class="btn btn-warning disabled mt-3 px-4"><fmt:message key="room.occupied"/></a>
                                        </c:when>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                        <c:choose>
                                            <c:when test="${isAdminPath}">
                                                <form action="${contextPath}/admin/applications" method="GET" style ="margin-bottom: 0">
                                                    <input type="hidden" name="checkin" value="${param.checkin}">
                                                    <input type="hidden" name="checkout" value="${param.checkout}">
                                                    <input type="hidden" name="roomNum" value="${room.number}">
                                                    <input type="hidden" name="applicationId" value="${param.applicationId}">
                                                    <input type="hidden" name="message" value="Select room">
                                                    <button type="submit" class="btn btn-primary mt-3 px-4">Select</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${contextPath}/rooms/confirmation" method="post" style ="margin-bottom: 0">
                                                    <input type="hidden" name="checkin" value="${param.checkin}">
                                                    <input type="hidden" name="checkout" value="${param.checkout}">
                                                    <input type="hidden" name="roomNum" value="${room.number}">
                                                    <button type="submit" class="btn btn-success mt-3 px-4"><fmt:message key="room.book"/></button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <%@include file="/WEB-INF/view/layout/pagination.jspf"%>
</c:if>

<%@ include file="/WEB-INF/view/layout/footer.jspf" %>
<%@include file="/WEB-INF/view/layout/scripts.jspf"%>
<script src = ${pageContext.request.contextPath}/js/DateValidation.js></script>
</html>