<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<body>
<div class="card login-container">
    <c:url value="/register" var="postPath" />
    <form:form modelAttribute="registerForm" action="${postPath}" method="post" acceptCharset="utf-8">
        <div class="card-content">
            <c:set var="globalErrors"><form:errors/></c:set>
            <span class="card-title center"><spring:message code="components.register-first-time"/></span>
            <div class="input-field">
                <c:set var="emailError"><form:errors path="email"/></c:set>
                <form:label path="email"><spring:message code="components.register-email"/></form:label>
                <form:input type="text" path="email" cssClass="${not empty emailError ? 'invalid' : ''}" />
                <form:errors path="email" cssClass="helper-text error"/>
            </div>
            <div class="input-field">
                <c:set var="usernameError"><form:errors path="username"/></c:set>
                <form:label path="username"><spring:message code="components.login-username"/></form:label>
                <form:input type="text" path="username" cssClass="${not empty usernameError ? 'invalid' : ''}"/>
                <form:errors path="username" cssClass="helper-text error" />
            </div>
            <div class="input-field">
                <c:set var="passwordError"><form:errors path="password"/></c:set>
                <form:label path="password"><spring:message code="components.login-password"/></form:label>
                <form:input type="password" path="password" cssClass="${not empty passwordError || not empty globalErrors ? 'invalid' : ''}"/>
                <form:errors path="password" cssClass="helper-text error" />
            </div>
            <div class="input-field">
                <c:set var="ConfirmPasswordError"><form:errors path="passwordConfirmation"/></c:set>
                <form:label path="passwordConfirmation"><spring:message code="components.register-confirm-password"/></form:label>
                <form:input type="password" path="passwordConfirmation" cssClass="${not empty ConfirmPasswordError || not empty globalErrors ? 'invalid' : ''}"/>
                <form:errors path="passwordConfirmation" cssClass="helper-text error" />
            </div>
            <form:errors cssClass="error"/>
            <button class="btn waves-effect center-block" type="submit"
                    name="action"><spring:message code="components.register"/>
                <i class="material-icons right">send</i>
            </button>
        </div>
    </form:form>

    <h6 class="center"><spring:message code="components.already-have-account"/> <a class="link" href="<c:url value="/login"/>"><spring:message code="components.login-here"/></a></h6>
</div>

</body>
</html>
