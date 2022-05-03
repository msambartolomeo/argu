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
            <h3><spring:message code="pages.debates-list.categories"/></h3>
            <c:forEach items="${categories}" var="category">
                <a href="<c:url value="?category=${category.name}" />" class="waves-effect btn-large badge-margin category-button ${category.name == param.category ? "selected-button" : ""}">
                    <spring:message code="category.${category.name}"/>
                </a>
            </c:forEach>
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
                <label for="select-order" class="order-text">
                    <h6><b>Order by</b></h6>
                </label>
                <select id="select-order" onchange="location.href=this.value">
                    <c:forEach items="${orders}" var="order">
                        <spring:url value="" var="option">
                            <c:forEach items="${param}" var="curParam">
                                <c:if test="${curParam.key != 'order'}">
                                    <spring:param name="${curParam.key}" value="${curParam.value}"/>
                                </c:if>
                            </c:forEach>
                            <spring:param name="order" value="${order}"/>
                        </spring:url>
                        <option value="${option}" ${order == param.order ? "selected" : ""}>
                            <spring:message code="orders.${order.name}"/>
                        </option>
                    </c:forEach>
                </select>
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
                <h3 class="center"><spring:message code="pages.debates-list.no-debates"/></h3>
            </c:if>
            <%@include file="../components/pagination.jsp"%>
        </div>
    </div>
    <%@include file="../components/JS_imports.jsp" %>
    </body>
</html>