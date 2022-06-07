<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <title>Argu | ${user.username}</title>
        <%@include file="../components/imports.jsp"%>
    </head>
    <body class="profile-wrapper">
        <%@include file="../components/navbar.jsp" %>
        <div class="profile-container">
            <div class="card profile-data">
                <div class="profile-image">
                    <c:choose>
                        <c:when test="${user.image.id != null}">
                            <img src="<c:url value="/images/${user.image.id}"/>"  alt="<spring:message
                        code="pages.profile.picture"/>"/>
                        </c:when>
                        <c:otherwise>
                            <img src="<c:url value="/resources/images/user-profile-default.png"/>" class="responsive-img" alt="<spring:message
                        code="pages.profile.picture"/>">
                        </c:otherwise>
                    </c:choose>
                </div>
                <h4>
                    <c:out value="${user.username}"/>
                </h4>
                <h5>
                    <i class="material-icons left">stars</i>
                    <c:out value="${user.points}"/>
                </h5>
                <h6>
                    <spring:message code="pages.profile.created-in"/> <c:out value="${user.createdDate}"/>
                </h6>
            </div>
            <div class="debates-column">
                <div class="card user-debates">
                    <h3 class="center">
                        <spring:message code="pages.profile.debates" arguments="${user.username}"/>
                    </h3>
                    <c:choose>
                        <c:when test="${debates.size() > 0}">
                            <c:forEach var="debate" items="${debates}">
                                <div class="list-item">
                                    <c:set var="debate" value="${debates}" scope="request"/>
                                    <%@include file="../components/debates-list-item.jsp" %>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <h5 class="center">
                                <spring:message code="pages.profile.debates.no-debates"/>
                            </h5>
                        </c:otherwise>
                    </c:choose>

                    <%@include file="../components/pagination.jsp"%>
                </div>
            </div>
        </div>
        <%@include file="../components/JS_imports.jsp" %>
    </body>
</html>
