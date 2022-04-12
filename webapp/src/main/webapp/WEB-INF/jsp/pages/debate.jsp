<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title><c:out value="${debate.name}"/></title>
        <%@include file="../components/imports.jsp"%>
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

            <div class="z-depth-3 comment-list">
                <c:if test="${posts.size() > 0}">
                        <c:forEach var="post" items="${posts}">
                            <div class="list-item">
                                <c:set var="post" value="${post}" scope="request"/>
                                <%@include file="../components/comment.jsp" %>
                            </div>
                        </c:forEach>
                </c:if>
                <c:if test="${posts.size() == 0}">
                    <h3 class="center"><spring:message code="pages.debate.no-posts"/></h3>
                </c:if>
            </div>

            <div class="post-comments">
                <%@include file="../components/post-comment.jsp" %>
            </div>

        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    </body>
</html>