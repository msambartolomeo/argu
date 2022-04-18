<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <title>Argu</title>
        <%@include file="../components/imports.jsp"%>
    </head>
    <body>
        <%@include file="../components/navbar.jsp" %>

        <div class="parallax-container">
            <div class="section no-pad-bot">
                <div class="container">
                    <br><br>
                    <h2 class="header center">Explore debates</h2>
                    <div class="row center">
                        <a class="waves-effect btn-small">Culture</a>
                        <a class="waves-effect btn-small">Economics</a>
                        <a class="waves-effect btn-small">Education</a>
                        <a class="waves-effect btn-small">Entertainment</a>
                        <a class="waves-effect btn-small">History</a>
                        <a class="waves-effect btn-small">Literature</a>
                    </div>
                    <div class="row center">
                        <a class="waves-effect btn-small">Politics</a>
                        <a class="waves-effect btn-small">Religion</a>
                        <a class="waves-effect btn-small">Science</a>
                        <a class="waves-effect btn-small">Technology</a>
                        <a class="waves-effect btn-small">World</a>
                    </div>
                    <br><br>

                </div>
            </div>
            <div class="parallax"><img src="${pageContext.request.contextPath}/resources/images/speech-bubbles.jpeg" alt="Debate background image"></div>
        </div>

        <div class="container">
            <div class="section">
                <div>
                    <h2 class="brown-text">About Argu</h2>
                    <p>Argu cuts through the noise typically associated with social and online media, making it easy to engage in focused discussion.</p>
                    <p>One can only comment as long as the moderator gave their permission.</p>
                </div>
                <img src="${pageContext.request.contextPath}/resources/images/debate-icon.png">

            </div>
        </div>

        <div class="parallax-container">
            <div class="section no-pad-bot">
                <div class="container">
                    <h5 class="header center light">Have to complete this section</h5>
                </div>
            </div>
            <div class="parallax"><img src="${pageContext.request.contextPath}/resources/images/group-discussion.jpeg" alt="Unsplashed background img 3"></div>
        </div>

        <footer class="page-footer">
            <div class="container">
                <p>© Copyright 2022, Argu</p>
                <p>Made using <a class="brown-text text-lighten-3" href="http://materializecss.com">Materialize</a></p>
            </div>
        </footer>
    </body>
</html>
<script>
    M.AutoInit();
    $(document).ready(function(){
        $('.parallax').parallax();
    });
</script>