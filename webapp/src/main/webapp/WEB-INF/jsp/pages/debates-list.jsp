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
        <div class="z-depth-3 debate-list normalized-margins">
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
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    </body>
</html>