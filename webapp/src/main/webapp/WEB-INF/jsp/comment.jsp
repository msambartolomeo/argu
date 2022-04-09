<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<body>
<div class="card">
    <div class="card-content">
        <span class="card-title"><c:out value="${post.userId}"/></span>
        <p class="comment-text"><c:out value="${post.content}"/></p>
    </div>
</div>


</body>
</html>

<style>

    .card {
        border-radius: 10px;
    }

</style>