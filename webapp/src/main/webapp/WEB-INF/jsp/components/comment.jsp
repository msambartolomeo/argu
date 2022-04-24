<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>

<body>
<div class="speech-bubble sb-left">
    <p class="comment-owner">
        <c:out value="${post.username}" />
        <spring:message code="components.user-comment" />
    </p>
    <br>
    <p>
        <c:out value="${post.content}" />
    </p>
    <br>
    <span class="new badge blue-grey darken-2" data-badge-caption="${post.createdDate}"></span>
</div>
</body>

</html>