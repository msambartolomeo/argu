<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<body>
<div class="speech-bubble ${post.username == debate.creatorUsername ? "sb-left" : "sb-right" }">
    <div class="comment-info">
        <h6 class="comment-owner">
            <c:set var="arg" value="${post.username != null ? post.username : ''}" />
            <c:set var="code" value="${post.username != null ? 'components.user-comment' : 'components.user-comment.anonymous'}" />
            <spring:message code="${code}" arguments="${arg}"/>
        </h6>
        <div class="comment-extra">
            <sec:authorize access="hasAuthority('USER')">
                <c:choose>
                    <c:when test="${!post.liked}">
                        <c:url var="likePath" value="/debates/${post.debateId}/like/${post.postId}"/>
                        <form:form method="post" action="${likePath}">
                            <button type="submit" class="btn-flat">
                                <i class="large material-icons">favorite_border</i>
                            </button>
                        </form:form>
                    </c:when>
                    <c:otherwise>
                        <c:url var="unlikePath" value="/debates/${post.debateId}/unlike/${post.postId}"/>
                        <form:form method="delete" action="${unlikePath}">
                            <button type="submit" class="btn-flat">
                                <i class="large material-icons">favorite</i>
                            </button>
                        </form:form>
                    </c:otherwise>
                </c:choose>
                <div>
                    <p><c:out value="${post.likes}"/></p>
                </div>
            </sec:authorize>
            <span class="new badge blue-grey darken-2 badge-margin" data-badge-caption="${post.createdDate}"></span>
            <span class="new badge blue-grey darken-2 badge-margin" data-badge-caption="<spring:message code='status.${post.status.name}'/>"></span>
        </div>
    </div>
    <div>
        <p>
            <c:out value="${post.content}" />
        </p>
    </div>
    <div>
        <c:if test="${post.imageId != 0}">
            <img src="<c:url value="/images/${post.imageId}"/>" alt="<spring:message code="components.user-image-alt"/>"
                 class="responsive-img"/>
        </c:if>
    </div>
</div>
</body>

</html>