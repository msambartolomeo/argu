<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>

<body>
<div class="speech-bubble ${pageContext.request.userPrincipal != null && post.username == pageContext.request.userPrincipal.name ? "sb-right" : "sb-left" }">
    <div>
        <p class="comment-owner">
            <c:set var="arg" value="${post.username != null ? post.username : ''}" />
            <c:set var="code" value="${post.username != null ? 'components.user-comment' : 'components.user-comment.anonymous'}" />
            <spring:message code="${code}" arguments="${arg}"/>
        </p>
        <br>
        <p>
            <c:out value="${post.content}" />
        </p>
        <c:if test="${post.imageId != 0}">
            <img src="<c:url value="/images/${post.imageId}"/>" alt="<spring:message code="components.user-image-alt"/>"
                 class="responsive-img"/>
        </c:if>
    </div>
    <div class="comment-extra">
        <sec:authorize access="hasAuthority('USER')">
        <form method="post" action="/like/${post.debateId}/${post.postId}">
            <c:choose>
                <c:when test="${post.liked}">
                    <button type="submit" class="btn-flat" name="unlike">
                        <i class="large material-icons">favorite</i>
                    </button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn-flat" name="like">
                        <i class="large material-icons">favorite_border</i>
                    </button>
                </c:otherwise>
            </c:choose>
        </form>
        </sec:authorize>
        <span class="new badge blue-grey darken-2 badge-margin" data-badge-caption="${post.createdDate}"></span>
    </div>
</div>
</body>

</html>