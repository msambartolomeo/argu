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
                    <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <div class="right debate-buttons-display">
                        <div class="col">
                        <c:if test="${debate.status.name == 'open' && (pageContext.request.userPrincipal.name == debate.creator.username || pageContext.request.userPrincipal.name == debate.opponent.username)}">
                            <c:url var="closeDebatePath" value="/debates/${debate.debateId}/close"/>
                            <form:form method="post" action="${closeDebatePath}">
                                <button type="submit" class="btn waves-effect chip">
                                    <spring:message code="pages.debate-close"/>
                                    <i class="material-icons right">close</i>
                                </button>
                            </form:form>
                        </c:if>
                        <c:if test="${debate.status.name != 'deleted' && pageContext.request.userPrincipal.name == debate.creator.username}">
                            <!-- Modal Trigger -->
                            <a class="btn waves-effect chip chip-delete modal-trigger" href="#delete-debate">
                                <spring:message code="pages.debate-delete"/>
                                <i class="material-icons right">delete</i>
                            </a>
                            <!-- Modal Structure -->
                            <div id="delete-debate" class="modal">
                                <c:url var="deleteDebatePath" value="/debates/${debate.debateId}/delete"/>
                                <form:form method="post" action="${deleteDebatePath}">
                                    <div class="modal-content">
                                        <h4><spring:message code="pages.debate.delete-confirmation"/></h4>
                                    </div>
                                    <div class="modal-footer">
                                        <a href="" class="modal-close waves-effect btn-flat">
                                            <spring:message code="pages.debate.cancel"/>
                                        </a>

                                            <button type="submit" class="modal-close waves-effect btn-flat">
                                                <spring:message code="pages.debate.yes"/>
                                            </button>
                                    </div>
                                </form:form>
                            </div>
                        </c:if>
                        </div>
                    </div>
                    </c:if>
                </div>
                <hr class="dashed">
                <h5 class="debate-description word-wrap"><c:out value="${debate.description}"/></h5>
                <c:choose>
                    <c:when test="${debate.isCreatorFor}">
                        <div class="username-container">
                            <h6>
                                <b><spring:message code="pages.debate.for"/></b>
                                <c:choose>
                                    <c:when test="${debate.creator.username != null}">
                                        <a class="link" href="<c:url value="/user/${debate.creator.username}"/>"> <c:out value="${debate.creator.username}"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <i><spring:message code="username.deleted"/></i>
                                    </c:otherwise>
                                </c:choose>
                            </h6>
                        </div>
                        <div class="username-container">
                            <h6>
                                <b><spring:message code="pages.debate.against"/></b>
                                <c:choose>
                                    <c:when test="${debate.opponent.username != null}">
                                        <a class="link" href="<c:url value="/user/${debate.opponent.username}"/>"> <c:out value="${debate.opponent.username}"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <i><spring:message code="username.deleted"/></i>
                                    </c:otherwise>
                                </c:choose>
                            </h6>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="username-container">
                            <h6>
                                <b><spring:message code="pages.debate.for"/></b>
                                <c:choose>
                                    <c:when test="${debate.opponent.username != null}">
                                        <a class="link" href="<c:url value="/user/${debate.opponent.url}"/>"> <c:out
                                                value="${debate.opponent.username}"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <i><spring:message code="username.deleted"/></i>
                                    </c:otherwise>
                                </c:choose>
                            </h6>
                        </div>
                        <div class="username-container">
                            <h6>
                                <b><spring:message code="pages.debate.against"/></b>
                                <c:choose>
                                    <c:when test="${debate.creator.username != null}">
                                        <a class="link" href="<c:url value="/user/${debate.creator.url}"/>"> <c:out
                                                value="${debate.creator.username}"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <i><spring:message code="username.deleted"/></i>
                                    </c:otherwise>
                                </c:choose>
                            </h6>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="debate-footer">
                <c:if test="${pageContext.request.userPrincipal.name != null}">
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
                </c:if>
                <a class="chip btn" href="<c:url value="/debates?category=${debate.category.name}"/>"><spring:message code="category.${debate.category.name}"/></a>
                <button class="chip btn" onclick="dateFilter('${debate.formattedDate}')"><spring:message code="components.debate-created-on"/> <c:out value="${debate.formattedDate}"/></button>
                <a class="chip btn" href="<c:url value="/debates?status=${debate.status.name == 'closed' ? 'closed' : 'open'}"/>"><spring:message code="status.${debate.status.name}"/></a>
                <div class="chip non-clickable-chip btn" ><spring:message code="page.debate.subscribed"
                                                  arguments="${debate.subscribedUsersCount}"/></div>
            </div>
        </div>
        <c:choose>
            <c:when test="${debate.image.id != null}">
                <div class="image-width">
                    <img src="<c:url value="/images/${debate.image.id}"/>" class="limit-img responsive-img" alt="<spring:message
                                        code="pages.debate-picture"/>"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="image-width">
                <img src="<c:url value="/resources/images/debate_stock.png"/>" class="limit-img responsive-img" alt="<spring:message
                        code="pages.debate-picture"/>">
                </div>
            </c:otherwise>
        </c:choose>
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
                    <c:when test="${(debate.status.name == 'closing' && argument.status.name == 'conclusion') || ((debate.status.name == 'closed' || debate.status.name == 'voting') && argument.status.name == 'conclusion' && (status.index == 0 || arguments[status.index - 1].status.name == 'argument'))}">
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
        <c:choose>
            <c:when test="${debate.status.name != 'closed' && debate.status.name != 'voting' && (pageContext.request.userPrincipal.name ==
            debate.creator.username || pageContext.request.userPrincipal.name == debate.opponent.username)}">
                <div class="card no-top-margin">
                    <div class="card-content">
                        <c:choose>
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
            </c:when>
            <c:when test="${pageContext.request.userPrincipal.name == null}">
                <div class="card no-top-margin">
                    <div class="card-content">
                        <div class="card-title card-title-margins">
                            <spring:message code="components.post-need-to-log-in"/>
                            <a href="<c:url value="/login"/>">
                                <spring:message code="components.first-log-in"/>
                            </a>
                        </div>
                    </div>
                </div>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${pageContext.request.userPrincipal.name != null && debate.creator.username != null && debate.opponent.username != null}">
                <div class="card vote-section no-top-margin">
                    <c:choose>
                        <c:when test="${userVote == null && (debate.status.name == 'voting' || debate.status.name == 'open' || debate.status.name == 'closing')}">
                            <h5 class="center"><spring:message code="pages.debate.who-wins"/></h5>
                            <div class="vote-buttons">
                                <c:url var="voteForPath" value="/debates/${debate.debateId}/vote/for"/>
                                <form:form method="post" action="${voteForPath}">
                                    <c:choose>
                                        <c:when test="${debate.isCreatorFor}">
                                            <button class="btn waves-effect" type="submit"><c:out value="${debate.creator.username}"/></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn waves-effect" type="submit"><c:out value="${debate.opponent.username}"/></button>
                                        </c:otherwise>
                                    </c:choose>
                                </form:form>

                                <c:url var="voteAgainstPath" value="/debates/${debate.debateId}/vote/against"/>
                                <form:form method="post" action="${voteAgainstPath}">
                                    <c:choose>
                                        <c:when test="${debate.isCreatorFor}">
                                            <button class="btn waves-effect" type="submit"><c:out value="${debate.opponent.username}"/></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn waves-effect" type="submit"><c:out value="${debate.creator.username}"/></button>
                                        </c:otherwise>
                                    </c:choose>
                                </form:form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h6><spring:message code="pages.debate.voted"/>
                                <c:choose>
                                    <c:when test="${(userVoteValue == 0 && debate.isCreatorFor) || (userVoteValue == 1 &&
                                    !debate.isCreatorFor) && debate.creator.username != null}">
                                        <c:out value="${debate.creator.username}"/>
                                    </c:when>
                                    <c:when test="${(userVoteValue == 1 && debate.isCreatorFor) || (userVoteValue == 0 &&
                                    !debate.isCreatorFor) && debate.opponent.username != null}">
                                        <c:out value="${debate.opponent.username}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="username.deleted"/>
                                    </c:otherwise>
                                </c:choose>
                            </h6>
                            <div class="progress red">
                                <c:if test="${debate.forCount > 0}">
                                    <div class="votes-format blue" style="width: ${debate.forCount}%">
                                        <c:choose>
                                            <c:when test="${debate.isCreatorFor}">
                                                <span><c:out value="${debate.creator.username}"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span><c:out value="${debate.opponent.username}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                        <span><c:out value="${debate.forCount}"/>%</span>
                                    </div>
                                </c:if>
                                <c:if test="${debate.againstCount > 0}">
                                    <div class="votes-format" style="width: ${debate.againstCount}%">
                                        <span>${debate.againstCount}%</span>
                                        <c:choose>
                                            <c:when test="${debate.isCreatorFor}">
                                                <span><c:out value="${debate.opponent.username}"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span><c:out value="${debate.creator.username}"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </div>
                            <c:if test="${debate.status.name == 'closed'}">
                                <c:choose>
                                    <c:when test="${debate.debateResult == 'DRAW'}">
                                        <h6><spring:message code="pages.debate.draw"/></h6>
                                    </c:when>
                                    <c:otherwise>
                                        <h6><spring:message code="pages.debate.winner"/>
                                            <c:choose>
                                                <c:when test="${debate.debateResult == 'FOR'}">
                                                    <c:choose>
                                                        <c:when test="${debate.isCreatorFor}"><c:out value="${debate.creator.username}"/></c:when>
                                                        <c:otherwise><c:out value="${debate.opponent.username}"/> </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${debate.isCreatorFor}"><c:out value="${debate.opponent.username}"/></c:when>
                                                        <c:otherwise><c:out value="${debate.creator.username}"/> </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </h6>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <c:if test="${debate.status.name == 'open' || debate.status.name == 'voting' || debate.status.name == 'voting'}">
                                <h6><spring:message code="page.debate.change-vote"/></h6>
                                <c:url var="unvotePath" value="/debates/${debate.debateId}/unvote"/>
                                <form:form method="delete" action="${unvotePath}">
                                    <button class="btn waves-effect" type="submit"><spring:message code="page.debate.unvote"/></button>
                                </form:form>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${debate.status.name == 'voting'}">
                        <h6 class="center"><spring:message code="page.debate.voting-ends" arguments="${debate.formattedDateToClose}"/></h6>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card vote-section no-top-margin">
                    <c:choose>
                        <c:when test="${debate.forCount + debate.againstCount > 0}">
                            <h5><spring:message code="pages.debate-votes"/></h5>
                            <div class="progress red">
                                <c:if test="${debate.forCount > 0}">
                                    <div class="votes-format blue" style="width: ${debate.forCount}%">
                                        <c:choose>
                                            <c:when test="${debate.creator.username != null}">
                                                <span><c:out value="${debate.creator.username}"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span><spring:message code="username.deleted"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                        <span><c:out value="${debate.forCount}"/>%</span>
                                    </div>
                                </c:if>
                                <c:if test="${debate.againstCount > 0}">
                                    <div class="votes-format" style="width: ${debate.againstCount}%">
                                        <span><c:out value="${debate.againstCount}"/>%</span>
                                        <c:choose>
                                            <c:when test="${debate.opponent.username != null}">
                                                <span><c:out value="${debate.opponent.username}"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span><spring:message code="username.deleted"/></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </div>
                            <c:if test="${debate.status.name == 'closed'}">
                                <c:choose>
                                    <c:when test="${debate.debateResult == 'DRAW'}">
                                        <h6><spring:message code="pages.debate.draw"/></h6>
                                    </c:when>
                                    <c:otherwise>
                                        <h6><spring:message code="pages.debate.winner"/>
                                            <c:choose>
                                                <c:when test="${debate.debateResult == 'FOR'}">
                                                    <c:choose>
                                                        <c:when test="${debate.isCreatorFor}"><c:out value="${debate.creator.username}"/></c:when>
                                                        <c:otherwise><c:out value="${debate.opponent.username}"/> </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${debate.isCreatorFor}"><c:out value="${debate.opponent.username}"/></c:when>
                                                        <c:otherwise><c:out value="${debate.creator.username}"/> </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </h6>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <h5 class="center"><spring:message code="pages.debate-no-votes"/></h5>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${debate.status.name == 'voting'}">
                        <h6 class="center"><spring:message code="page.debate.voting-ends" arguments="${debate.formattedDateToClose}"/></h6>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="card">
            <c:if test="${(debate.status.name != 'open' && debate.status.name != 'closing') || (pageContext.request.userPrincipal == null ||
                        (pageContext.request.userPrincipal.name != debate.creator.username && pageContext.request.userPrincipal.name != debate.opponent.username))}">
                <c:set var="chats" value="${chats}" scope="request"/>
                <%@include file="../components/chat.jsp" %>
            </c:if>
        </div>
        <c:if test="${recommendedDebates.size() > 0}">
            <div class="card vote-section">
                <h5><spring:message code="pages.debate.recommended-debates"/></h5>
                <div class="row">
                    <div class="slideshow-container">
                        <c:forEach var="debate" items="${recommendedDebates}">
                            <div class="mySlides fade">
                                <%@include file="../components/debates-list-item.jsp"%>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="image-selector">
                    <c:if test="${recommendedDebates.size() > 1}">
                        <a class="prev btn image-selector" onclick="plusSlides(-1)">❮</a>
                    </c:if>
                    <c:if test="${recommendedDebates.size() > 1}">
                        <a class="next btn image-selector" onclick="plusSlides(1)">❯</a>
                    </c:if>
                </div>
            </div>

        </c:if>
    </div>
</div>
<%@include file="../components/JS_imports.jsp" %>
<script>
    let slideIndex = 1;
showSlides(slideIndex);

function plusSlides(n) {
  showSlides(slideIndex += n);
}

function currentSlide(n) {
  showSlides(slideIndex = n);
}

function showSlides(n) {
  let slides = document.getElementsByClassName("mySlides");
                let dots = document.getElementsByClassName("dot");
                if (n > slides.length) {slideIndex = 1}
                if (n < 1) {slideIndex = slides.length}
                for (i = 0; i < slides.length; i++) {
                slides[i].style.display = "none";
                }
                for (i = 0; i < dots.length; i++) {
                dots[i].className = dots[i].className.replace(" active", "");
                }
                slides[slideIndex-1].style.display = "block";
                dots[slideIndex-1].className += " active";
                }
</script>
</body>
</html>