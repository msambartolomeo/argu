<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<body>
<c:url value="/debates/${debate.debateId}" var="postPath"/>
<%--@elvariable id="argumentForm" type="ar.edu.itba.paw.webapp.form.ArgumentForm"--%>
<form:form enctype="multipart/form-data" modelAttribute="argumentForm" action="${postPath}"
           method="post" acceptCharset="utf-8" id="postform">
    <div class="card-content">

        <c:choose>
            <c:when test="${empty lastArgument || (lastArgument.status.name == 'introduction' && lastArgument.user.username == debate.creator.username)}">
                <span class="card-title"><spring:message code="components.post-comment.introduction"/></span>
            </c:when>
            <c:when test="${debate.status.name == 'closing'}">
                <span class="card-title"><spring:message code="components.post-comment.conclusion"/></span>
            </c:when>
            <c:otherwise>
                <span class="card-title"><spring:message code="components.post-comment.argument"/></span>
            </c:otherwise>
        </c:choose>

        <div class="input-field">
            <c:set var="contentError"><form:errors path="content"/></c:set>
            <form:label path="content"><spring:message
                    code="components.post-comment.content"/></form:label>
            <form:textarea maxlength="280"
                           class="materialize-textarea ${not empty contentError ? 'invalid' : ''}"
                           rows="5" cols="30" path="content"/>
            <form:errors path="content" element="span" cssClass="error"/>
        </div>
        <div class="image-selector">
            <div class="file-field input-field">
                <div class="btn">
                    <c:set var="imageError"><form:errors path="file"/></c:set>
                    <form:label class="white-text" path="file" for="file"><spring:message
                            code="components.user-image-button"/></form:label>
                    <form:input id="file" path="file" type="file"/>
                </div>
                <div class="file-path-wrapper">
                    <form:input path="fileName"
                                class="file-path validate ${not empty imageError ? 'invalid' : ''}"
                                type="text" onchange="updateVisibilityOfX()"/>
                </div>
                <form:errors path="file" cssClass="helper-text error"/>
            </div>
            <a id="x" class="material-icons ${empty imageError? 'x' : ''}" onclick="resetFileValue('file', 'fileName')">close</a>
        </div>
        <button class="btn waves-effect center-block submitBtn" type="submit" name="argument" form="postform">
            <c:choose>
                <c:when test="${empty lastArgument || (lastArgument.status.name == 'introduction' && lastArgument.user.username == debate.creator.username)}">
                    <spring:message code="components.post-intro.submit"/>
                </c:when>
                <c:when test="${debate.status.name == 'closing'}">
                    <spring:message code="components.post-conclusion.submit"/>
                </c:when>
                <c:otherwise>
                    <spring:message code="components.post-argument.submit"/>
                </c:otherwise>
            </c:choose>
            <i class="material-icons right">send</i>
        </button>
    </div>
</form:form>
</body>
</html>