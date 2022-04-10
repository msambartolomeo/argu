<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
<div class="card">
    <div class="card-content">
        <span class="card-title"><c:out value="${post.userEmail}"/></span>
        <p class="comment-text"><c:out value="${post.content}"/></p>
    </div>
</div>
</body>
</html>