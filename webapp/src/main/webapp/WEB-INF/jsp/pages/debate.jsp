<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title>Argu | <c:out value="${debate.name}"/></title>
        <%@include file="../components/imports.jsp"%>
    </head>

    <body>
        <%@include file="../components/navbar.jsp" %>
        <div class="card normalized-margins">
            <c:choose>
                <c:when test="${debate.imageId != 0}">
                    <div class="card-content">
                        <div class="row">
                            <div class="col s3">
                                <img src="<c:url value="/images/${debate.imageId}"/>" class="circle responsive-img" alt="<spring:message
                                    code="pages.debate-picture"/>"/>
                            </div>
                            <div class="col s7">
                                <span class="card-title debate-title"><c:out value="${debate.name}"/></span>
                                <p class="debate-description"><c:out value="${debate.description}"/></p>
                            </div>
                            <div class="col s2">
                                <div class="row">
                                    <c:url value="/debates/${debateId}" var="debatePath"/>
                                    <sec:authorize access="hasAuthority('USER')">
                                        <c:choose>
                                            <c:when test="${isSubscribed == false}">
                                                <%--@elvariable id="subscribeForm" type="ar.edu.itba.paw.webapp.form.SubscribeForm"--%>
                                                <form:form id="subscribeForm" method="post" modelAttribute="subscribeForm"
                                                           action="${debatePath}" acceptCharset="utf-8">
                                                    <button class="btn waves-effect" type="submit" form="subscribeForm"
                                                            id="subscribeForm" onclick="this.form.submit();" name="subscribe">
                                                        <spring:message code="pages.debate-subscribe"/>
                                                        <i class="material-icons right">notifications_active</i>
                                                    </button>
                                                </form:form>
                                            </c:when>
                                            <c:otherwise>
                                                <%--@elvariable id="unsubscribeForm" type=""--%>
                                                <form:form id="unsubscribeForm" method="post" modelAttribute="unsubscribeForm"
                                                           action="${debatePath}" acceptCharset="utf-8">
                                                    <button class="btn waves-effect" type="submit" form="unsubscribeForm"
                                                            id="unsubscribeForm"
                                                            onclick="this.form.submit();" name="unsubscribe">
                                                        <spring:message code="pages.debate-unsubscribe"/>
                                                        <i class="material-icons right">notifications_off</i>
                                                    </button>
                                                </form:form>
                                            </c:otherwise>
                                        </c:choose>
                                    </sec:authorize>
                                </div>
                                <div class="row">
                                    <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="category.${debate.debateCategory.name}"/>"></span>
                                </div>
                                <div class="row">
                                    <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="components.debate-created-on"/> ${debate.createdDate}"></span>
                                </div>
                                <div class="row">
                                    <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="status.${debate.debateStatus.name}"/>"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="card-content">
                        <div class="row">
                            <div class="col s10">
                                <span class="card-title debate-title"><c:out value="${debate.name}"/></span>
                                <p class="debate-description"><c:out value="${debate.description}"/></p>
                            </div>
                                <div class="col s2">
                                    <div class="row">
                                        <c:url value="/debates/${debateId}" var="debatePath"/>
                                        <sec:authorize access="hasAuthority('USER')">
                                            <c:choose>
                                                <c:when test="${isSubscribed == false}">
                                                    <%--@elvariable id="subscribeForm" type="ar.edu.itba.paw.webapp.form.SubscribeForm"--%>
                                                    <form:form id="subscribeForm" method="post" modelAttribute="subscribeForm"
                                                               action="${debatePath}" acceptCharset="utf-8">
                                                        <button class="btn waves-effect" type="submit" form="subscribeForm"
                                                                id="subscribeForm"
                                                                onclick="this.form.submit();" name="subscribe">
                                                            <spring:message code="pages.debate-subscribe"/>
                                                            <i class="material-icons right">notifications_active</i>
                                                        </button>
                                                    </form:form>
                                                </c:when>
                                                <c:otherwise>
                                                    <%--@elvariable id="unsubscribeForm" type=""--%>
                                                    <form:form id="unsubscribeForm" method="post" modelAttribute="unsubscribeForm"
                                                               action="${debatePath}" acceptCharset="utf-8">
                                                        <button class="btn waves-effect" type="submit" form="unsubscribeForm"
                                                                id="unsubscribeForm"
                                                                onclick="this.form.submit();" name="unsubscribe">
                                                            <spring:message code="pages.debate-unsubscribe"/>
                                                            <i class="material-icons right">notifications_off</i>
                                                        </button>
                                                    </form:form>
                                                </c:otherwise>
                                            </c:choose>
                                        </sec:authorize>
                                    </div>
                                    <div class="row">
                                        <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="category.${debate.debateCategory.name}"/>"></span>
                                    </div>
                                    <div class="row">
                                        <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="components.debate-created-on"/> ${debate.createdDate}"></span>
                                    </div>
                                    <div class="row">
                                        <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="status.${debate.debateStatus.name}"/>"></span>
                                    </div>
                                </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
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

                <%@include file="../components/pagination.jsp"%>
            </div>

            <div class="post-comments">
                <%@include file="../components/post-comment.jsp" %>
            </div>

        </div>
        <%@include file="../components/JS_imports.jsp" %>
    </body>
</html>