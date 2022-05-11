<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title>Error 500</title>
    <%@include file="../components/imports.jsp"%>
</head>
<body>
<div class="error-container">
    <div class="error-div">
        <img src="<c:url value="/resources/images/error500.png"/>" alt="500 error image">
        <h1><spring:message code="error.page.title"/></h1>
        <c:choose>
            <c:when test="${empty exception}">
                <h6><spring:message code="error.500.message"/></h6>
            </c:when>
            <c:otherwise>
                <h6><spring:message code="${exception}"/></h6>
            </c:otherwise>
        </c:choose>
        <a href="<c:url value="/"/>"><spring:message code="error.go.back"/></a>
    </div>
</div>
</body>
</html>