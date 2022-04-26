<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form2" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
    <body>
        <div class="card">
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name == debate.creatorUsername || pageContext.request.userPrincipal.name == debate.opponentUsername }">
                    <c:url value="/debates/${debate.debateId}" var="postPath" />
                    <%--@elvariable id="postForm" type="ar.edu.itba.paw.webapp.form.PostForm"--%>
                    <form2:form enctype="multipart/form-data" modelAttribute="postForm" action="${postPath}" method="post" acceptCharset="utf-8" id="myForm">
                        <div class="card-content">
                            <span class="card-title"><spring:message code="components.post-comment.title"/></span>
                            <div class="input-field">
                                <c:set var="contentError"><form2:errors path="content"/></c:set>
                                <form2:label path="content"><spring:message code="components.post-comment.content"/></form2:label>
                                <form2:textarea maxlength="280" class="materialize-textarea ${not empty contentError ? 'invalid' : ''}"
                                               rows="5" cols="30" path="content"/>
                                <form2:errors path="content" element="span" cssClass="error" />
                            </div>

                            <div class="file-field input-field">
                                <div class="btn">
                                    <c:set var="imageError"><form2:errors path="file"/></c:set>
                                    <form2:label path="file" for="file"><spring:message code="components.user-image-button"/></form2:label>
                                    <form2:input id="file" path="file" type="file"/>
                                </div>
                                <div class="file-path-wrapper">
                                    <form2:input path="fileName" class="file-path validate ${not empty imageError ? 'invalid' : ''}" type="text"/>
                                </div>
                                <form2:errors path="file" cssClass="helper-text error"/>
                            </div>

                            <button class="btn waves-effect center-block" type="submit" form="myForm" id="myForm"
                                    onclick="this.form.submit(); this.disabled=true; this.value='Sending...';"
                                    name="action"><spring:message code="components.post-comment.submit"/>
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </form2:form>
                </c:when>

                <c:otherwise>
                    <div class="card-title card-title-margins">
                        <spring:message code="components.post-need-to-log-in"/>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>

    </body>
</html>