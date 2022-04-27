<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Argu | <c:out value="${debate.name}"/></title>
    <%@include file="../components/imports.jsp" %>
</head>

<body>
<%@include file="../components/navbar.jsp" %>
<div class="card normalized-margins">
    <div class="card-content debate-info-holder">
        <div class="debate-holder-separator">
            <div class="debate-text-holder">
                <h1 class="debate-title word-wrap"><c:out value="${debate.name}"/></h1>
                <hr class="dashed">
                <h5 class="debate-description word-wrap"><c:out value="${debate.description}"/></h5>
                <c:if test="${debate.creatorUsername != null}">
                    <c:set var="creator"><c:out value="${debate.creatorUsername}"/></c:set>
                    <c:set var="opponent"><c:out value="${debate.opponentUsername}"/></c:set>
                    <p><spring:message code="pages.debate.for" arguments="${creator}"/></p>
                    <p><spring:message code="pages.debate.against" arguments='${opponent}'/></p>
                </c:if>
            </div>
            <div class="debate-footer">
                <sec:authorize access="hasAuthority('USER')">
                    <c:url var="subscribePath" value="/subscribe/${debate.debateId}"/>
                    <form id="subscribeForm" method="post" action="${subscribePath}">
                        <c:choose>
                            <c:when test="${isSubscribed == false}">
                                <button class="btn waves-effect" type="submit" name="subscribe">
                                    <spring:message code="pages.debate-subscribe"/>
                                    <i class="material-icons right">notifications_active</i>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn waves-effect" type="submit" name="unsubscribe">
                                    <spring:message code="pages.debate-unsubscribe"/>
                                    <i class="material-icons right">notifications_off</i>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </sec:authorize>
                <span class="new badge blue-grey darken-2"
                      data-badge-caption="<spring:message code="category.${debate.debateCategory.name}"/>"></span>
                <span class="new badge blue-grey darken-2"
                      data-badge-caption="<spring:message code="components.debate-created-on"/> ${debate.createdDate}"></span>
                <span class="new badge blue-grey darken-2"
                      data-badge-caption="<spring:message code="status.${debate.debateStatus.name}"/>"></span>
                <span class="new badge blue-grey darken-2"
                      data-badge-caption="<spring:message code="page.debate.subscribed" arguments="${debate.subscribedUsers}"/>"></span>
            </div>
        </div>
        <c:if test="${debate.imageId != 0}">
            <div class="image-width">
                <img src="<c:url value="/images/${debate.imageId}"/>" class="limit-img responsive-img" alt="<spring:message
                                    code="pages.debate-picture"/>"/>
            </div>
        </c:if>
    </div>
</div>

<div class="debate-content">

    <div class="z-depth-3 comment-list">
        <c:if test="${posts.size() > 0}">
            <c:forEach var="post" items="${posts}">
                <div class="list-item">
                    <c:set var="post" value="${post}" scope="request"/>
                    <%@include file="../components/comment.jsp" %>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${posts.size() == 0}">
            <h3 class="center"><spring:message code="pages.debate.no-posts"/></h3>
        </c:if>

        <%@include file="../components/pagination.jsp" %>
    </div>

    <div class="post-comments">
        <%@include file="../components/post-comment.jsp" %>
    </div>

</div>
<%@include file="../components/JS_imports.jsp" %>
</body>
</html>