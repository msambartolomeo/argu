<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title>Error 404</title>
        <%@include file="../components/imports.jsp"%>
    </head>
    <body>
    <div class="error-container">
        <div class="error-div">
            <img src="${pageContext.request.contextPath}/resources/images/error-404.png" alt="404 error image">
            <h1><spring:message code="error.404.title"/></h1>
            <h6><spring:message code="error.404.message"/></h6>
            <a href="${pageContext.request.contextPath}/"><spring:message code="errpr.404.back"/></a>
        </div>
    </div>
    </body>
</html>