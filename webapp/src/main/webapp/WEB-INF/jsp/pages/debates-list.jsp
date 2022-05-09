<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title>Argu | Debates</title>
        <%@include file="../components/imports.jsp"%>
    </head>

    <body>
    <%@include file="../components/navbar.jsp" %>
    <div class="debates-separator">
        <div class="category-list z-depth-3">
            <ul class="collection with header">
                <li class="collection-header"><h5><spring:message code="pages.debates-list.categories"/></h5></li>
                <li>
                    <a href="<c:url value="/debates" />" class="collection-item waves-effect badge-margin category-button ${ empty
                    param.category ? "active" : ""}">
                        <spring:message code="category.all"/>
                    </a>
                </li>

                <c:forEach items="${categories}" var="category">
                    <li>
                        <a href="<c:url value="?category=${category.name}" />"
                           class="collection-item waves-effect badge-margin category-button ${category.name == param.category ?
                           "active" : ""}">
                            <spring:message code="category.${category.name}"/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="z-depth-3 debate-list">
            <c:if test="${param.search != null}">
                <h3 class="center"><spring:message code="pages.debates-list.search-results" arguments="${param.search}"/></h3>
            </c:if>
            <c:if test="${param.category != null}">
                <spring:message code="category.${param.category}" var="categoryCode"/>
                <h3 class="center"><spring:message code="pages.debates-list.category-results" arguments="${categoryCode}"/></h3>
            </c:if>
            <c:if test="${param.search == null && param.category == null}">
                <h3 class="center"><spring:message code="pages.debates-list.all-debates"/></h3>
            </c:if>

            <div class="order-div">
                <div class="input-field margin-left">
                    <select id="select-order" onchange="addParamToUrlAndRedirect('order', this.value)">
                        <c:forEach items="${orders}" var="order">--%>
                            <option value="${order.name}" ${order.name == param.order ? "selected" : ""}><spring:message code="orders.${order.name}"/></option>
                        </c:forEach>
                    </select>
                    <label for="select-order"><spring:message code="pages.debates-list.order-by"/></label>
                </div>
                <div class="input-field margin-left">
                    <select id="select-status" onchange="addParamToUrlAndRedirect('status', this.value)">
                        <option value="" selected ><spring:message code="status.all"/></option>
                        <option value="open" ${ 'open' == param.status ? "selected" : ""}><spring:message code="status.open"/></option>
                        <option value="closed" ${ 'closed' == param.status ? "selected" : ""}><spring:message code="status.closed"/></option>
                    </select>
                    <label for="select-status"><spring:message code="pages.debates-list.status"/></label>
                </div>
                <div class="input-field margin-left flex">
                    <label for="datepicker"><spring:message code="pages.debates-list.created-date"/></label>
                    <input placeholder="date" id="datepicker" type="text" class="datepicker" value="${param.date}" onchange="addParamToUrlAndRedirect('date', this.value)">
                    <i class="material-icons x" onclick="addParamToUrlAndRedirect('date', '')">close</i>
                </div>
            </div>

            <c:if test="${debates.size() > 0}">
                <c:forEach var="debate" items="${debates}">
                    <div class="list-item">
                        <c:set var="debate" value="${debates}" scope="request"/>
                        <%@include file="../components/debates-list-item.jsp" %>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${debates.size() == 0}">
                <h5 class="center"><spring:message code="pages.debates-list.no-debates"/></h5>
            </c:if>
            <%@include file="../components/pagination.jsp"%>
        </div>
    </div>
    <%@include file="../components/JS_imports.jsp" %>
    </body>
</html>