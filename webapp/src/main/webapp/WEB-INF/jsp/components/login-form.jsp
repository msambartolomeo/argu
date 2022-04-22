<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>
    <div class="card login-container">
        <form method="post" action="<c:url value="/login"/>" accept-charset="UTF-8">
            <div class="card-content">
                <span class="card-title center">Login</span>
                <div class="input-field">
                    <label for="username">Username:</label>
                    <input type="text" name="j_username" id="username" />
                </div>
                <div class="input-field">
                    <label for="password">Password:</label>
                    <input type="password" name="j_password" id="password" />
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
</body>
</html>
