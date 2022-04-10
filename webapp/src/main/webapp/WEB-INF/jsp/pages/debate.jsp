<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>

    <head>
        <title>Debate</title>
        <!-- Compiled and minified CSS -->
        <link rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    </head>

    <body>

        <%@include file="../components/navbar.jsp" %>
        <div class="card normalized-margins">
            <div class="card-content">
                <span class="card-title debate-title"><c:out value="${debate.name}"/></span>
                <p class="debate-description"><c:out value="${debate.description}"/></p>
            </div>
        </div>

        <div class="debate-content">

            <div class="z-depth-3 comments">
                <c:if test="${posts.size() > 0}">
                        <c:forEach var="post" items="${posts}">
                            <div class="user-comment">
                                <c:set var="post" value="${post}" scope="request"/>
                                <%@include file="../components/comment.jsp" %>
                            </div>
                        </c:forEach>
                </c:if>
                <c:if test="${posts.size() == 0}">
                    <h3>No posts found</h3>
                </c:if>
            </div>

            <div class="post-comments">
                <%@include file="../components/post-comment.jsp" %>
            </div>

        </div>

    </body>

</html>

<style>
    :root {
        --primary-color: #212D40;
        --secondary-color: #D66853;
        --white: white;
    }

    .debate-title {
        font-size: 50px !important;
        padding-bottom: 1%;
    }

    .debate-description {
        font-size: 20px;
    }

    .comments {
        background: var(--white);
        border-radius: 10px;
        display: flex;
        flex-direction: column;
        width: 54%;
        margin-right: 1%;
    }

    .post-comments {
        width: 34%;
        margin-left: 1%;
    }

    @media only screen and (max-width: 1300px) {
        .post-comments {
            width: 30%;
        }
    }

    @media only screen and (max-width: 900px) {
        .comments {
            width: 45%;
        }

        .post-comments {
            width: 45%;
        }
    }

    .normalized-margins {
        width: 90%;
        margin: 1% auto;
    }

    .user-comment {
        margin: 1% 3%;
    }

    .debate-content {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }

    body {
        background: var(--secondary-color);
    }
</style>