<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>
<div class="card no-top-margin">
    <c:choose>
    <c:when test="${pageContext.request.userPrincipal.name == null}">
        <div class="card-title card-title-margins">
            <spring:message code="components.post-need-to-log-in"/>
            <a href="<c:url value="/login"/>">
                <spring:message code="components.first-log-in"/>
            </a>
        </div>
    </c:when>
    <c:otherwise>
    <c:choose>
        <c:when test="${debate.debateStatus.name == 'closed'}">
            <div class="card-title card-title-margins">
                <spring:message code="components.post-closed-debate"/>
            </div>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name == debate.creatorUsername || pageContext.request.userPrincipal.name == debate.opponentUsername}">
                    <c:url value="/debates/${debate.debateId}" var="postPath"/>
                    <%--@elvariable id="postForm" type="ar.edu.itba.paw.webapp.form.PostForm"--%>
                    <form:form enctype="multipart/form-data" modelAttribute="postForm" action="${postPath}"
                               method="post" acceptCharset="utf-8" id="postform">
                        <div class="card-content">
                            <span class="card-title"><spring:message code="components.post-comment.title"/></span>
                            <div class="input-field">
                                <c:set var="contentError"><form:errors path="content"/></c:set>
                                <form:label path="content"><spring:message
                                        code="components.post-comment.content"/></form:label>
                                <form:textarea maxlength="280"
                                               class="materialize-textarea ${not empty contentError ? 'invalid' : ''}"
                                               rows="5" cols="30" path="content"/>
                                <form:errors path="content" element="span" cssClass="error"/>
                            </div>
                            <div class="file-field input-field">
                                <div class="btn">
                                    <c:set var="imageError"><form:errors path="file"/></c:set>
                                    <form:label path="file" for="file"><spring:message
                                            code="components.user-image-button"/></form:label>
                                    <form:input id="file" path="file" type="file"/>
                                </div>
                                <div class="file-path-wrapper">
                                    <form:input path="fileName"
                                                class="file-path validate ${not empty imageError ? 'invalid' : ''}"
                                                type="text"/>
                                </div>
                                <form:errors path="file" cssClass="helper-text error"/>
                            </div>
                            <button class="btn waves-effect center-block" type="submit"
                                    onclick="this.disabled=true; document.getElementById('postform').submit()">
                                <spring:message code="components.post-comment.submit"/>
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </form:form>
                </c:when>
                <c:otherwise>
                    <div class="card-title card-title-margins">
                        <spring:message code="components.post-not-participant"/>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
    </c:otherwise>
</c:choose>
</div>
</body>
</html>