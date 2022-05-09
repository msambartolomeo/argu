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
                    <h2 class="header center">
                        <i class="medium material-icons">explore</i>
                        <spring:message code="pages.landing.page.explore"/>
                    </h2>
                    <div class="row center">
                        <c:forEach items="${categories}" var="category">
                            <a href="<c:url value="/debates/category/${category.name}" />" class="waves-effect btn-small">
                                <spring:message code="category.${category.name}"/>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="parallax"><img src="<c:url value="/resources/images/speech-bubbles.jpeg"/>" alt="Debate background image"></div>
        </div>

        <div class="container">
            <div class="section">
                <div>
                    <h2 class="brown-text">
                        <spring:message code="pages.landing.page.about"/>
                    </h2>
                    <p>Argu cuts through the noise typically associated with social and online media, making it easy to engage in focused discussion.</p>
                    <p>One can only comment as long as the moderator gave their permission.</p>
                </div>
                <img src="<c:url value="/resources/images/debate-icon.png"/>">

            </div>
        </div>

        <div class="parallax-container">
            <div class="container">
                <div class="section email-format">
                    <img class="image-size" src="<c:url value="/resources/images/create-participate-debate.png"/>">
                    <div class="right-text">
                        <h2 class="light">Be part of debates!</h2>
                        <p>Create your own debate or participate in another one.</p>
                        <p>Note that only moderators can use this feature.</p>
                        <p>If you aren't a moderator yet, become one by clicking the button above!</p>
                    </div>
                </div>
            </div>
            <div class="parallax"><img src="<c:url value="/resources/images/conference-debate.webp"/>" alt="Unsplashed background img 2"></div>
        </div>

        <div class="container">
            <div class="section">
                <div>
                    <h2 class="light">
                        <spring:message code="pages.landing-page.engage-debates"/>
                    </h2>
                    <p><spring:message code="pages.landing-page.subscribe"/></p>
                    <p><spring:message code="pages.landing-page.like"/></p>
                    <p><spring:message code="pages.landing-page.vote"/></p>
                </div>
                <img class="image-size" src="<c:url value="/resources/images/subscribe-like-vote.png"/>" alt="Subscribe, like and vote">
            </div>
        </div>

        <div class="parallax-container">
            <div class="section no-pad-bot">
                <div class="container">
                    <h2 class="header center light">
                        <i class="medium material-icons">local_fire_department</i>
                        <spring:message code="pages.landing.page.hottest.debates"/>
                    </h2>
                    <c:forEach var="debate" items="${debates}">
                        <div class="list-item">
                            <c:set var="debate" value="${debates}" scope="request"/>
                            <%@include file="../components/debates-list-item.jsp" %>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="parallax"><img src="<c:url value="/resources/images/group-discussion.jpeg"/>" alt="Unsplashed background img 3"></div>
        </div>

        <footer class="page-footer">
            <div class="container">
                <p>© Copyright 2022, Argu</p>
                <p>
                    <spring:message code="pages.landing.page.materialize"/>
                    <a class="brown-text text-lighten-3" href="http://materializecss.com"> Materialize</a>
                </p>
            </div>
        </footer>
        <%@include file="../components/JS_imports.jsp" %>
        <script>
            $(document).ready(function(){
                $('.parallax').parallax();
            });
        </script>
    </body>
</html>