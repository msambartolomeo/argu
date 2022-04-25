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
                    <a class="to-debates" href="<c:url value="/debates" />">
                        <h2 class="header center">
                            <i class="medium material-icons">explore</i>
                            <spring:message code="pages.landing.page.explore"/>
                        </h2>
                    </a>
                    <div class="row center">
                        <c:forEach items="${categories}" var="category">
                            <a href="<c:url value="/debates/category/${category.name}" />" class="waves-effect btn-small">
                                <spring:message code="category.${category.name}"/>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="parallax"><img src="${pageContext.request.contextPath}/resources/images/speech-bubbles.jpeg" alt="Debate background image"></div>
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
                <img src="${pageContext.request.contextPath}/resources/images/debate-icon.png">

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
            <div class="parallax"><img src="${pageContext.request.contextPath}/resources/images/group-discussion.jpeg" alt="Unsplashed background img 3"></div>
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