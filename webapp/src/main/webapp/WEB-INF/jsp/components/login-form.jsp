<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>
    <div class="card login-container">
        <form method="post" action="<c:url value="/login"/>" accept-charset="UTF-8">
            <div class="card-content">
                <span class="card-title center"><spring:message code="components.login-welcome-back"/></span>
                <div class="input-field">
                    <label for="username"><spring:message code="components.login-username"/></label>
                    <input type="text" name="username" id="username" />
                </div>
                <div class="input-field">
                    <label for="password"><spring:message code="components.login-password"/></label>
                    <input type="password" name="password" id="password" />
                </div>
                <div>
                    <label>
                        <input type="checkbox" class="filled-in" name="rememberme" id="rememberme" />
                        <span><spring:message code="components.login-remember-me"/></span>
                    </label>
                </div>
                <button class="btn waves-effect center-block" type="submit"
                        name="action"><spring:message code="components.login"/>
                    <i class="material-icons right">send</i>
                </button>
            </div>
        </form>

        <h6 class="center"><spring:message code="components.no-account"/> <a class="link" href="<c:url value="/register"/>">
            <spring:message code="components.register-here"/></a></h6>
    </div>
</body>
</html>
