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
      <span class="card-title center">Register</span>
      <div class="input-field">
        <label for="email">Email:</label>
        <input type="text" name="email" id="email" />
      </div>
      <div class="input-field">
        <label for="username">Username:</label>
        <input type="text" name="username" id="username" />
      </div>
      <div class="input-field">
        <label for="password">Password:</label>
        <input type="password" name="password" id="password" />
      </div>
      <div class="input-field">
        <label for="repassword">Password again:</label>
        <input type="password" name="repassword" id="repassword" />
      </div>
      <button class="btn waves-effect center-block" type="submit"
              name="action">Register
        <i class="material-icons right">send</i>
      </button>
    </div>
  </form>
</div>

<h6 class="center">Already have an account? <a class="link" href="<c:url value="/login"/>"> Login here</a></h6>
</body>
</html>