<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Debate</title>
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
</head>
<body>

<%@include file="navbar.jsp" %>
<div class="card normalized-margins">
    <div class="card-content">
        <span class="card-title debate-title">Debate1</span>
        <p class="debate-description">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum eu lacus urna.
            Aliquam erat volutpat. Sed
            gravida tortor id est commodo varius. Suspendisse nulla justo, commodo non mauris eu, pharetra sodales nisi.
            Proin accumsan aliquet ipsum, a vulputate mauris sodales sit amet. Sed quis aliquam velit, ac faucibus
            sapien. Morbi ac scelerisque risus.</p>
    </div>
</div>

<div class="debate-content">

    <div class="z-depth-3 comments">
        <div class="user-comment">
            <%@include file="comment.jsp" %>
        </div>
        <div class="user-comment">
            <%@include file="comment.jsp" %>
        </div>
        <div class="user-comment">
            <%@include file="comment.jsp" %>
        </div>
        <div class="user-comment">
            <%@include file="comment.jsp" %>
        </div>
        <div class="user-comment">
            <%@include file="comment.jsp" %>
        </div>
        <div class="user-comment">
            <%@include file="comment.jsp" %>
        </div>
        <div class="user-comment">
            <%@include file="comment.jsp" %>
        </div>
    </div>

    <div class="post-comments">
        <div class="post-comment">
            <%@include file="post-comment.jsp" %>
        </div>
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
        padding-bottom: 10px;
    }

    .debate-description {
        font-size: 20px;
    }

    .comments {
        background: var(--white);
        border-radius: 10px;
        display: flex;
        flex-direction: column;
        width: 40%;
        margin-right: 15px;
    }

    .post-comments {
        width: 20%;
        margin-left: 15px;
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
        margin: 0 10px;
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