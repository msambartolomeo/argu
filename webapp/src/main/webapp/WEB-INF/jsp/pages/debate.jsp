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
                <h3 class="debate-title word-wrap"><c:out value="${debate.name}"/></h3>
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
        <%@include file="../components/post-comment.jsp" %>
        <sec:authorize access="hasAuthority('USER')">
            <c:if test="${debate.creatorUsername != null && debate.opponentUsername != null}">
                <div class="card vote-section">
                    <c:choose>
                        <c:when test="${userVote == null}">
                            <h5><spring:message code="pages.debate.who-wins"/></h5>
                            <div class="vote-buttons">
                                    <c:url var="voteForPath" value="/debates/${debate.debateId}/vote/for"/>
                                    <form:form method="post" action="${voteForPath}">
                                        <button class="btn waves-effect" type="submit">${debate.creatorUsername}</button>
                                    </form:form>

                                    <c:url var="voteAgainstPath" value="/debates/${debate.debateId}/vote/against"/>
                                    <form:form method="post" action="${voteAgainstPath}">
                                        <button class="btn waves-effect" type="submit">${debate.opponentUsername}</button>
                                    </form:form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h6><spring:message code="pages.debate.voted"/> ${userVote}</h6>
                            <div class="progress red">
                                <c:if test="${debate.forCount > 0}">
                                    <div class="votes-format blue" style="width: ${debate.forCount}%">
                                        <span>${debate.creatorUsername}</span>
                                        <span>${debate.forCount}%</span>
                                    </div>
                                </c:if>
                                <c:if test="${debate.againstCount > 0}">
                                    <div class="votes-format" style="width: ${debate.againstCount}%">
                                        <span>${debate.opponentUsername}</span>
                                        <span>${debate.againstCount}%</span>
                                    </div>
                                </c:if>
                            </div>
                            <h6><spring:message code="page.debate.change-vote"/></h6>
                            <c:url var="unvotePath" value="/debates/${debate.debateId}/unvote"/>
                            <form:form method="post" action="${unvotePath}">
                                <button class="btn waves-effect" type="submit"><spring:message code="page.debate.unvote"/></button>
                            </form:form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </sec:authorize>
    </div>

</div>
<%@include file="../components/JS_imports.jsp" %>
</body>
</html>