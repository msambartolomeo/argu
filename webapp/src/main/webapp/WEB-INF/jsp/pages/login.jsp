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
    <div class="card login-container">
        <form method="post" action="<c:url value="/login"/>" accept-charset="UTF-8">
            <div class="card-content">
                <span class="card-title center">Login</span>
                <div class="input-field">
                    <label for="email">Email:</label>
                    <input type="text" name="email" id="email" />
                </div>
                <div class="input-field">
                    <label for="password">Password:</label>
                    <input type="password" name="password" id="password" />
                </div>
                <div>
                    <label>
                        <input type="checkbox" class="filled-in" name="rememberme" id="rememberme" />
                        <span>Remember me</span>
                    </label>
                </div>
                <button class="btn waves-effect center-block" type="submit"
                        name="action">Login
                    <i class="material-icons right">send</i>
                </button>
            </div>
        </form>
    </div>

    <h6 class="center">Dont have an account? <a class="link" href="<c:url value="/register"/>"> Register here</a></h6>
</body>
</html>