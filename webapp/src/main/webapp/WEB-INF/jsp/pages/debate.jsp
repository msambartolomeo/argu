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
<c:set var="debate" value="${debate}" scope="request"/>
<c:set var="lastArgument" value="${lastArgument}" scope="request"/>
<div class="card normalized-margins">
    <div class="card-content debate-info-holder">
        <div class="debate-holder-separator">
            <div class="debate-text-holder">
                <div class="debate-info-holder">
                    <h4 class="debate-title word-wrap"><c:out value="${debate.name}"/></h4>
                    <c:if test="${debate.status.name == 'open' && (pageContext.request.userPrincipal.name == debate.creator.username || pageContext.request.userPrincipal.name == debate.opponent.username)}">
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
                <c:if test="${debate.creator.username != null}">
                    <c:set var="creator"><c:out value="${debate.creator.username}"/></c:set>
                    <c:set var="opponent">
                        <c:choose>
                            <c:when test="${debate.opponent.username != null}">
                                <c:out value="${debate.opponent.username}"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="pages.debate.no-opponent"/>
                            </c:otherwise>
                        </c:choose>
                    </c:set>
                    <h6><b><spring:message code="pages.debate.for"/></b> ${creator}</h6>
                    <h6><b><spring:message code="pages.debate.against"/></b> ${opponent}</h6>
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
                <a class="chip btn" href="<c:url value="/debates?category=${debate.category.name}"/>"><spring:message code="category.${debate.category.name}"/></a>
                <button class="chip btn" onclick="dateFilter('${debate.createdDate}')"><spring:message code="components.debate-created-on"/> ${debate.createdDate}</button>
                <a class="chip btn" href="<c:url value="/debates?status=${debate.status.name == 'closed' ? 'closed' : 'open'}"/>"><spring:message code="status.${debate.status.name}"/></a>
                <a class="chip btn" href="<c:url value="/debates?order=subs_desc"/>"><spring:message code="page.debate.subscribed"
                                                  arguments="${debate.subscribedUsersCount}"/></a>
            </div>
        </div>
        <c:if test="${debate.image.id != null}">
            <div class="image-width">
                <img src="<c:url value="/images/${debate.image.id}"/>" class="limit-img responsive-img" alt="<spring:message
                                    code="pages.debate-picture"/>"/>
            </div>
        </c:if>
    </div>
</div>

<div class="debate-content">

    <div class="z-depth-3 comment-list">
        <c:if test="${arguments.size() > 0}">
            <c:forEach var="argument" items="${arguments}" varStatus="status">
                <c:choose>
                    <c:when test="${argument.status.name == 'introduction' && status.first}">
                        <h5 class="center"><spring:message code="components.comment.introduction" /></h5>
                    </c:when>
                    <c:when test="${argument.status.name == 'argument' && (arguments[status.index - 1].status.name == 'introduction' || status.first)}">
                        <h5 class="center"><spring:message code="components.comment.argument" /></h5>
                    </c:when>
                    <c:when test="${(debate.status.name == 'closing' && argument.status.name == 'conclusion') || (debate.status.name == 'closed' && argument.status.name == 'conclusion' && (status.index == 0 || arguments[status.index - 1].status.name == 'argument'))}">
                        <h5 class="center"><spring:message code="components.comment.conclusion" /></h5>
                    </c:when>
                </c:choose>
                <div class="list-item">
                    <c:set var="argument" value="${argument}" scope="request"/>
                    <%@include file="../components/comment.jsp" %>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${arguments.size() == 0}">
            <h5 class="center"><spring:message code="pages.debate.no-posts"/></h5>
        </c:if>

        <%@include file="../components/pagination.jsp" %>
    </div>

    <div class="post-comments">
        <c:if test="${debate.status.name != 'closed' && (pageContext.request.userPrincipal.name == debate.creator.username || pageContext.request.userPrincipal.name == debate.opponent.username)}">
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
                        <c:when test="${((empty lastArgument && pageContext.request.userPrincipal.name == debate.creator.username) || (not empty lastArgument && pageContext.request.userPrincipal.name != lastArgument.user.username))}">
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
        <c:choose>
            <c:when test="${pageContext.request.userPrincipal.name != null && debate.creator.username != null && debate.opponent.username != null}">
                <div class="card vote-section no-top-margin">
                    <c:choose>
                        <c:when test="${userVote == null}">
                            <h5 class="center"><spring:message code="pages.debate.who-wins"/></h5>
                            <div class="vote-buttons">
                                <c:url var="voteForPath" value="/debates/${debate.debateId}/vote/for"/>
                                <form:form method="post" action="${voteForPath}">
                                    <button class="btn waves-effect" type="submit">${debate.creator.username}</button>
                                </form:form>

                                <c:url var="voteAgainstPath" value="/debates/${debate.debateId}/vote/against"/>
                                <form:form method="post" action="${voteAgainstPath}">
                                    <button class="btn waves-effect" type="submit">${debate.opponent.username}</button>
                                </form:form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h6><spring:message code="pages.debate.voted"/> ${userVote}</h6>
                            <div class="progress red">
                                <c:if test="${debate.forCount > 0}">
                                    <div class="votes-format blue" style="width: ${debate.forCount}%">
                                        <span>${debate.creator.username}</span>
                                        <span>${debate.forCount}%</span>
                                    </div>
                                </c:if>
                                <c:if test="${debate.againstCount > 0}">
                                    <div class="votes-format" style="width: ${debate.againstCount}%">
                                        <span>${debate.opponent.username}</span>
                                        <span>${debate.againstCount}%</span>
                                    </div>
                                </c:if>
                            </div>
                            <h6><spring:message code="page.debate.change-vote"/></h6>
                            <c:url var="unvotePath" value="/debates/${debate.debateId}/unvote"/>
                            <form:form method="delete" action="${unvotePath}">
                                <button class="btn waves-effect" type="submit"><spring:message code="page.debate.unvote"/></button>
                            </form:form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card vote-section no-top-margin">
                    <c:choose>
                        <c:when test="${debate.forCount + debate.againstCount > 0}">
                            <h5>Votes</h5>
                            <div class="progress red">
                                <c:if test="${debate.forCount > 0}">
                                    <div class="votes-format blue" style="width: ${debate.forCount}%">
                                        <span>${debate.creator.username}</span>
                                        <span>${debate.forCount}%</span>
                                    </div>
                                </c:if>
                                <c:if test="${debate.againstCount > 0}">
                                    <div class="votes-format" style="width: ${debate.againstCount}%">
                                        <span>${debate.opponent.username}</span>
                                        <span>${debate.againstCount}%</span>
                                    </div>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h5 class="center">No votes yet</h5>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>
<%@include file="../components/JS_imports.jsp" %>
</body>
</html>