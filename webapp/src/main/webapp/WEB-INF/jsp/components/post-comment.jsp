<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
    <body>
        <div class="card">
            <%--@elvariable id="postForm" type="ar.edu.itba.paw.webapp.form.PostForm"--%>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                    <c:url value="/debates/${debate.debateId}" var="postPath" />
                    <form:form enctype="multipart/form-data" modelAttribute="postForm" action="${postPath}" method="post" acceptCharset="utf-8" id="myForm">
                        <div class="card-content">
                            <span class="card-title"><spring:message code="components.post-comment.title"/></span>
                            <div class="input-field">
                                <form:label for="comment" path="content"><spring:message code="components.post-comment.content"/></form:label>
                                <form:textarea id="comment" maxlength="280" class="materialize-textarea" rows="5" cols="30"
                                               path="content"/>
                                <form:errors path="content" element="span" cssClass="error" />
                            </div>

    <%--                        TODO : Correctly implement image input--%>
                            <div class="file-field input-field">
                                <div class="btn">
                                    <form:label path="file" for="file"><spring:message code="components.user-image-button"/></form:label>
                                    <form:input id="file" path="file" type="file" accept="image/jpeg, image/png"/>
                                    <form:errors path="file" cssClass="helper-text error"/>
                                </div>
                                <div class="file-path-wrapper">
                                    <input class="file-path validate" type="text"/>
                                </div>
                            </div>


    <%--                        TODO : Deprecate the following--%>
    <%--                        <div class="input-field">--%>
    <%--                            <form:label path="email"><spring:message code="components.post-comment.email"/></form:label>--%>
    <%--                            <form:input type="text" path="email" />--%>
    <%--                            <form:errors path="email" element="p" cssClass="error" />--%>
    <%--                        </div>--%>
                            <button class="btn waves-effect center-block" type="submit" form="myForm" id="myForm"
                                    onclick="this.form.submit(); this.disabled=true; this.value='Sending...';"
                                    name="action"><spring:message code="components.post-comment.submit"/>
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </form:form>
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