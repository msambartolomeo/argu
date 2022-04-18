<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>
<div class="card">
    <div class="card-content">
        <span class="card-title"><c:out value="${post.userEmail}"/></span>
        <p class="word-wrap"><c:out value="${post.content}"/></p>
    </div>
</div>
</body>
</html>