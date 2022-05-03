<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <title>Argu | <spring:message code="pages.profile"/></title>
        <%@include file="../components/imports.jsp"%>
    </head>
    <body class="profile-wrapper">
        <%@include file="../components/navbar.jsp" %>
        <div class="profile-container">
            <div class="card profile-data">
                <c:choose>
                    <c:when test="${user.imageId != 0}">
                        <img src="<c:url value="/images/${user.imageId}"/>" class="center circle responsive-img" alt="<spring:message
                        code="pages.profile.picture"/>"/>
                    </c:when>
                    <c:otherwise>
                        <img src="<c:url value="/resources/images/user-profile-default.png"/>" class="responsive-img" alt="<spring:message
                        code="pages.profile.picture"/>">
                    </c:otherwise>
                </c:choose>
                <!-- Modal Trigger -->
                <a class="waves-effect waves-light btn modal-trigger" href="#edit-profile-image">
                    <spring:message code="pages.profile.edit-profile-image"/>
                </a>
                <!-- Modal Structure -->
                <div id="edit-profile-image" class="modal">
                    <%@include file="../components/profile-photo-form.jsp"%>
                </div>
                <h4>
                    <c:out value="${user.username}"/>
                </h4>
                <div class="email-format">
                    <i class="material-icons">email</i>
                    <h6>
                        <c:out value="${user.email}"/>
                    </h6>
                </div>
                <h6>
                    <spring:message code="pages.profile.created-in"/> <c:out value="${user.createdDate}"/>
                </h6>
            </div>
            <div class="card user-debates">
                <h3>
                    <spring:message code="pages.profile.debates-subscribed"/>
                </h3>
                <c:choose>
                    <c:when test="${subscribed_debates.size() > 0}">
                        <c:forEach var="debate" items="${subscribed_debates}">
                            <div class="list-item">
                                <c:set var="debate" value="${subscribed_debates}" scope="request"/>
                                <%@include file="../components/debates-list-item.jsp" %>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <h5 class="center">
                            <spring:message code="pages.profile.no-debates-subscribed"/>
                        </h5>
                    </c:otherwise>
                </c:choose>

                <%@include file="../components/pagination.jsp"%>
            </div>
        </div>
        <%@include file="../components/JS_imports.jsp" %>
        <script>
            const elem = document.getElementById('edit-profile-image');
            const instance = M.Modal.init(elem);
            if (${not empty imageError})
                instance.open();
        </script>
    </body>
</html>
