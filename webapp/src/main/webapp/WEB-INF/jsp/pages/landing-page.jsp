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
                            <a href="<c:url value="/debates?category=${category.name}" />" class="waves-effect btn-small">
                                <spring:message code="category.${category.name}"/>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="parallax"><img src="<c:url value="/resources/images/speech-bubbles.jpeg"/>" alt="Debate background image"></div>
        </div>

        <div class="hottest-debates-container">
            <div class="section no-pad-bot">
                <div class="container">
                    <h2 class="header center light hottest-title">
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
<%--            <div class="parallax"><img src="<c:url value="/resources/images/group-discussion.jpeg"/>" alt="Unsplashed background img 3"></div>--%>
        </div>

        <div class="container">
            <div class="section">
                <div class="about-container">
                    <h2 class="light">
                        <spring:message code="pages.landing.page.about"/>
                    </h2>
                    <h6><spring:message code="pages.landing-page.description"/></h6>
                    <h6><spring:message code="pages.landing-page.structure"/></h6>
                    <h6><spring:message code="pages.landing-page.turns"/></h6>
                </div>
                <img src="<c:url value="/resources/images/debate-icon.png"/>" alt="Image of two humans arguing">

            </div>
        </div>

        <div class="dark-container">
            <div class="container">
                <div class="section email-format">
                    <img class="image-size" src="<c:url value="/resources/images/create-participate-debate.png"/>">
                    <div class="right-text about-container">
                        <h2 class="light"><spring:message code="pages.landing-page.participate"/></h2>
                        <h6><spring:message code="pages.landing-page.create-debate"/></h6>
                        <h6><spring:message code="pages.landing-page.only-moderators"/></h6>
                        <h6><spring:message code="pages.landing-page.become-moderator"/></h6>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="section">
                <div class="about-container">
                    <h2 class="light">
                        <spring:message code="pages.landing-page.choose-favorites"/>
                    </h2>
                    <h6><spring:message code="pages.landing-page.subscribe"/></h6>
                    <h6><spring:message code="pages.landing-page.like"/></h6>
                    <h6><spring:message code="pages.landing-page.vote"/></h6>
                </div>
                <img class="image-size" src="<c:url value="/resources/images/subscribe-like-vote.png"/>" alt="Subscribe, like and vote">
            </div>
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