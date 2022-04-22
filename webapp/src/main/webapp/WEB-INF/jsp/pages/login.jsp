<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Login</title>
    <%@include file="../components/imports.jsp"%>
</head>
<body>
    <%@include file="../components/navbar.jsp"%>
    <%@include file="../components/login-form.jsp"%>

    <h6 class="center">Dont have an account? <a class="link" href="<c:url value="/register"/>"> Register here</a></h6>
</body>
</html>