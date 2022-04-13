<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>

    <body>
        <div class="card">
            <c:url value="/debate/${debate.debateId}" var="postPath" />
                <%--@elvariable id="postForm" type="ar.edu.itba.paw.webapp.form.PostForm"--%>
                <form:form modelAttribute="postForm" action="${postPath}" method="post" acceptCharset="utf-8">
                    <div class="card-content">
                        <span class="card-title"><spring:message code="components.post-comment.title"/></span>
                        <div class="input-field">
                            <form:label path="content"><spring:message code="components.post-comment.content"/></form:label>
                            <form:textarea class="materialize-textarea" rows="5" cols="30" path="content" />
                            <form:errors path="content" element="span" cssClass="error" />
                        </div>
                        <div class="input-field">
                            <form:label path="email"><spring:message code="components.post-comment.email"/></form:label>
                            <form:input type="text" path="email" />
                            <form:errors path="email" element="p" cssClass="error" />
                        </div>
                        <button class="btn waves-effect center-block" type="submit"
                            name="action"><spring:message code="components.post-comment.submit"/>
                            <i class="material-icons right">send</i>
                        </button>
                    </div>
                </form:form>
        </div>
    </body>
</html>