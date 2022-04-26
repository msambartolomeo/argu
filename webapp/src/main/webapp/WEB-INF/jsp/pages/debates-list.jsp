<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title>Argu</title>
        <%@include file="../components/imports.jsp"%>
    </head>

    <body>
    <%@include file="../components/navbar.jsp" %>
    <div class="debates-separator">
        <div class="category-list z-depth-3">
            <h3><spring:message code="pages.debates-list.categories"/></h3>
            <c:forEach items="${categories}" var="category">
                <a href="<c:url value="/debates/category/${category.name}" />" class="waves-effect btn-large badge-margin category-button ${category.name == currentCategory ? "selected-button" : ""}">
                    <spring:message code="category.${category.name}"/>
                </a>
            </c:forEach>
        </div>
        <div class="z-depth-3 debate-list">
            <c:if test="${search != null}">
                <h3 class="center"><spring:message code="pages.debates-list.search-results" arguments="${search}"/></h3>
            </c:if>
            <c:if test="${currentCategory != null}">
                <spring:message code="category.${currentCategory}" var="categoryCode"/>
                <h3 class="center"><spring:message code="pages.debates-list.category-results" arguments="${categoryCode}"/></h3>
            </c:if>
            <c:if test="${search == null && currentCategory == null}">
                <h3 class="center"><spring:message code="pages.debates-list.all-debates"/></h3>
            </c:if>

            <c:if test="${debates.size() > 0}">
                <c:forEach var="debate" items="${debates}">
                    <div class="list-item">
                        <c:set var="debate" value="${debates}" scope="request"/>
                        <%@include file="../components/debates-list-item.jsp" %>
                    </div>
                </c:forEach>
                <div class="center pagination-margin">
                    <ul class="pagination">
                        <c:forEach var="page" begin="0" end="${total_pages}">
                            <li class="active page-number">
                                <a href="<c:url value="/debates?page=${page}"/>">${page + 1}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            <c:if test="${debates.size() == 0}">
                <h3 class="center"><spring:message code="pages.debates-list.no-debates"/></h3>
            </c:if>
        </div>
    </div>
    <%@include file="../components/JS_imports.jsp" %>
    </body>
</html>