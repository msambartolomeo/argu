<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<body>
<div class="card login-container">
    <c:url value="/moderator" var="postPath"/>
    <%--@elvariable id="moderatorForm" type="ar.edu.itba.paw.webapp.form.ModeratorForm"--%>
    <form:form modelAttribute="moderatorForm" action="${postPath}" method="post" acceptCharset="utf-8">
        <div class="card-content">
            <c:set var="globalErrors"><form:errors/></c:set>
            <span class="card-title"><spring:message code="components.moderator-form.title"/></span>
            <p><spring:message code="components.moderator-form.message"/></p>
            <div class="input-field">
                <c:set var="reasonError"><form:errors path="reason"/></c:set>
                <form:label path="reason"> <spring:message code="components.moderator-form.label"/> </form:label>
                <form:textarea type="text" path="reason" cssClass="materialize-textarea ${not empty reasonError ? 'invalid' : ''}"/>
                <form:errors path="reason" cssClass="helper-text error"/>
            </div>
            <button class="btn waves-effect center-block" type="submit"
                    name="action"><spring:message code="components.moderator-form.submit"/>
                <i class="material-icons right">send</i>
            </button>
        </div>
    </form:form>
</div>
</body>
</html>
