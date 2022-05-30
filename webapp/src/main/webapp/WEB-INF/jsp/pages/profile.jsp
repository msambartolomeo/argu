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
                <!-- Modal Trigger -->
                <a class="waves-effect waves-light btn modal-trigger" href="#edit-profile-image">
                    <spring:message code="pages.profile.edit-profile-image"/>
                </a>
                <!-- Modal Structure -->
                <div id="edit-profile-image" class="modal">
                    <%@include file="../components/profile-photo-form.jsp"%>
                </div>
                <!-- Modal Trigger -->
                <a class="waves-effect waves-light btn modal-trigger" href="#delete-account">
                    <spring:message code="delete.user"/>
                </a>
                <!-- Modal Structure -->
                <div id="delete-account" class="modal">
                    <%@include file="../components/confirmation-modal.jsp"%>
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
                <a class="waves-effect waves-light btn logout-btn" href="<c:url value="/logout"/>">
                    <i class="material-icons left">logout</i>
                    <spring:message code="pages.profile.logout"/>
                </a>
            </div>
            <div class="debates-column">
                <div class="section">
                    <a href="<c:url value="/profile?list=subscribed"/>" class="waves-effect btn-large ${(param.list == null || param.list == "subscribed")? "selected-button" : ""}">
                        <spring:message code="pages.profile.debates-subscribed"/>
                    </a>
                    <a href="<c:url value="/profile?list=mydebates"/>" class="waves-effect btn-large ${(param.list == "mydebates")? "selected-button" : ""}">
                        <spring:message code="pages.profile.my-debates"/>
                    </a>
                </div>
                <div class="card user-debates">
                    <h3 class="center">
                        <c:choose>
                            <c:when test="${param.list == null || param.list == 'subscribed'}">
                                <spring:message code="pages.profile.debates-subscribed"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="pages.profile.my-debates"/>
                            </c:otherwise>
                        </c:choose>
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
                                <spring:message code="pages.profile.no-debates-subscribed"/>
                            </h5>
                        </c:otherwise>
                    </c:choose>

                    <%@include file="../components/pagination.jsp"%>
                </div>
            </div>
        </div>
        <%@include file="../components/JS_imports.jsp" %>
        <script>
            const elem = document.getElementById('edit-profile-image');
            const instance = M.Modal.init(elem);
            const elem2 = document.getElementById('delete-account');
            const instance2 = M.Modal.init(elem2);
            if (${not empty imageError})
                instance.open();
            if (${not empty deleteError})
                instance2.open();
        </script>
    </body>
</html>
