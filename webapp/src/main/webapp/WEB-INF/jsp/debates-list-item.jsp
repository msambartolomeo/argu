<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

    <head>
        <title>Debate List</title>
        <!--Import Google Icon Font-->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <!-- Compiled and minified CSS -->
        <link rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    </head>

    <body>
        <a class="card radius" href="/debate/${debate.debateId}">
            <div class="card-content">
                <span class="card-title">
                    <c:out value="${debate.name}"/>
<%--                    <span class="new badge blue-grey darken-2" data-badge-caption="16:00 15/04/22"></span>--%>
<%--                    <span class="new badge blue-grey darken-2" data-badge-caption="Category1"></span>--%>
                </span>
                <p><c:out value="${debate.description}"/></p>
            </div>
        </a>
    </body>

</html>

<style>
    :root {
        --badge-color: #364156;
    }

    .normalized-margins {
        margin: 1% auto;
        justify-content: center;
    }

    .radius {
        border-radius: 10px;
    }
</style>