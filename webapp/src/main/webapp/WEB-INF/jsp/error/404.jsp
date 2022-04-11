<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Error 404</title>
        <%@include file="../components/imports.jsp"%>
    </head>
    <body>
    <div class="error-container">
        <div class="error-div">
            <img src="/resources/images/error-404.png">
            <h1>Oops!</h1>
            <h6>The page requested could not be found.</h6>
            <a href="/">Back to home</a>
        </div>
    </div>
    </body>
</html>