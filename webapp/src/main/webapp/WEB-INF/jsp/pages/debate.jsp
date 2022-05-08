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
                <div class="debate-info-holder">
                    <h3 class="debate-title word-wrap"><c:out value="${debate.name}"/></h3>
                    <c:if test="${debate.debateStatus.name != 'closed' && (pageContext.request.userPrincipal.name == debate.creatorUsername || pageContext.request.userPrincipal.name == debate.opponentUsername)}">
                        <c:url var="closeDebatePath" value="/debates/${debate.debateId}/close"/>
                        <form:form method="post" action="${closeDebatePath}">
                            <button type="submit" class="btn waves-effect">
                                <spring:message code="pages.debate-close"/>
                                <i class="large material-icons right">close</i>
                            </button>
                        </form:form>
                    </c:if>
                </div>
                <hr class="dashed">
                <h5 class="debate-description word-wrap"><c:out value="${debate.description}"/></h5>
                <c:if test="${debate.creatorUsername != null}">
                    <c:set var="creator"><c:out value="${debate.creatorUsername}"/></c:set>
                    <c:set var="opponent">
                        <c:choose>
                            <c:when test="${debate.opponentUsername != null}">
                                <c:out value="${debate.opponentUsername}"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="pages.debate.no-opponent"/>
                            </c:otherwise>
                        </c:choose>
                    </c:set>
                    <h6><spring:message code="pages.debate.for" arguments="${creator}"/></h6>
                    <h6><spring:message code="pages.debate.against" arguments='${opponent}'/></h6>
                </c:if>
            </div>
            <div class="debate-footer">
                <sec:authorize access="hasAuthority('USER')">
                    <c:choose>
                        <c:when test="${isSubscribed == false}">
                            <c:url var="subscribePath" value="/debates/${debate.debateId}/subscribe"/>
                            <form:form id="subscribeForm" method="post" action="${subscribePath}">
                                <button class="btn waves-effect chip" type="submit">
                                    <spring:message code="pages.debate-subscribe"/>
                                    <i class="material-icons right">notifications_active</i>
                                </button>
                            </form:form>
                        </c:when>
                        <c:otherwise>
                            <c:url var="unsubscribePath" value="/debates/${debate.debateId}/unsubscribe"/>
                            <form:form id="subscribeForm" method="delete" action="${unsubscribePath}">
                                <button class="btn waves-effect chip" type="submit">
                                    <spring:message code="pages.debate-unsubscribe"/>
                                    <i class="material-icons right">notifications_off</i>
                                </button>
                            </form:form>
                        </c:otherwise>
                    </c:choose>
                </sec:authorize>
                <div class="chip"><spring:message code="category.${debate.debateCategory.name}"/></div>
                <div class="chip"><spring:message code="components.debate-created-on"/> ${debate.createdDate}</div>
                <div class="chip"><spring:message code="status.${debate.debateStatus.name}"/></div>
                <div class="chip"><spring:message code="page.debate.subscribed"
                                                  arguments="${debate.subscribedUsers}"/></div>
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
            <h5 class="center"><spring:message code="pages.debate.no-posts"/></h5>
        </c:if>

        <%@include file="../components/pagination.jsp" %>
    </div>

    <div class="post-comments">
        <c:if test="${debate.debateStatus.name != 'closed' && (pageContext.request.userPrincipal.name == debate.creatorUsername || pageContext.request.userPrincipal.name == debate.opponentUsername)}">
            <div class="card no-top-margin">
                <div class="card-content">
                    <c:choose>
                        <c:when test="${pageContext.request.userPrincipal.name == null}">
                            <div class="card-title card-title-margins">
                                <spring:message code="components.post-need-to-log-in"/>
                                <a href="<c:url value="/login"/>">
                                    <spring:message code="components.first-log-in"/>
                                </a>
                            </div>
                        </c:when>
                        <c:when test="">
                        </c:when>
                        <c:when test="${((empty lastArgument && pageContext.request.userPrincipal.name == debate.creatorUsername) || (not empty lastArgument && pageContext.request.userPrincipal.name != lastArgument.username))}">
                            <%@include file="../components/post-comment.jsp" %>
                        </c:when>
                        <c:otherwise>
                            <div class="card-title card-title-margins">
                                <spring:message code="components.post-comment.wait-turn"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:if>
    </div>

</div>
<%@include file="../components/JS_imports.jsp" %>
</body>
</html>