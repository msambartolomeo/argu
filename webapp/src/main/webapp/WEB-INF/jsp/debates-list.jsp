<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
		<!--Import Google Icon Font-->
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
		<!-- Compiled and minified CSS -->
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
        <title>Debates</title>
    </head>

    <%@include file="navbar.jsp" %>

    <body>
        <div class="items normalized-margins">
            <c:if test="${debates.size() > 0}">
                <c:forEach var="debate" items="${debates}">
                    <div class="each-item">
                        <c:set var="debate" value="${debates}" scope="request"/>
                        <%@include file="debates-list-item.jsp" %>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${debates.size() == 0}">
                <h3>No debates found</h3>
            </c:if>
        </div>
      <!-- Compiled and minified JavaScript -->
      <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    </body>
</html>
<style>
    :root {
        --primary-color: #212D40;
        --secondary-color: #D66853;
        --white: white;
    }
    .normalized-margins {
        width: 90%;
        margin: 1% auto;
    }
    .items {
        background: var(--white);
        border-radius: 10px;
        display: flex;
        flex-direction: column;
        /* justify-content: center; */
    }
    .each-item {
        display: flex;
        justify-content: center;
        padding: 10px;
    }
    body {
        background: var(--secondary-color);
    }
</style>