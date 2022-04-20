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
  <c:url value="/register" var="postPath" />
<form:form modelAttribute="registerForm" action="${postPath}" method="post" acceptCharset="utf-8">
  <div class="card-content">
      <c:set var="globalErrors"><form:errors/></c:set>
      <span class="card-title center">Register</span>
      <div class="input-field">
          <c:set var="emailError"><form:errors path="email"/></c:set>
          <form:label path="email">Email</form:label>
          <form:input type="text" path="email" cssClass="${not empty emailError ? 'invalid' : ''}" />
          <form:errors path="email" cssClass="helper-text error"/>
      </div>
      <div class="input-field">
          <c:set var="usernameError"><form:errors path="username"/></c:set>
          <form:label path="username">Username</form:label>
          <form:input type="text" path="username" cssClass="${not empty usernameError ? 'invalid' : ''}"/>
          <form:errors path="username" cssClass="helper-text error" />
      </div>
      <div class="input-field">
          <c:set var="passwordError"><form:errors path="password"/></c:set>
          <form:label path="password">Password</form:label>
          <form:input type="password" path="password" cssClass="${not empty passwordError || not empty globalErrors ? 'invalid' : ''}"/>
          <form:errors path="password" cssClass="helper-text error" />
      </div>
      <div class="input-field">
          <c:set var="ConfirmPasswordError"><form:errors path="passwordConfirmation"/></c:set>
          <form:label path="passwordConfirmation">Confirm Password</form:label>
          <form:input type="password" path="passwordConfirmation" cssClass="${not empty ConfirmPasswordError || not empty globalErrors ? 'invalid' : ''}"/>
          <form:errors path="passwordConfirmation" cssClass="helper-text error" />
      </div>
      <form:errors cssClass="error"/>
      <button class="btn waves-effect center-block" type="submit"
              name="action">Register
        <i class="material-icons right">send</i>
      </button>
    </div>
  </form:form>
</div>

<h6 class="center">Already have an account? <a class="link" href="<c:url value="/login"/>"> Login here</a></h6>
</body>
</html>