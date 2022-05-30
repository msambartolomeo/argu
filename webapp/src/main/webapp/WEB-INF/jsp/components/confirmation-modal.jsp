<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<body>
<c:url value="/profile/delete" var="deleteUserPostPath"/>
<%--@elvariable id="confirmationModal" type="ar.edu.itba.paw.webapp.form.ConfirmationForm"--%>
<form:form modelAttribute="confirmationModal" method="post" action="${deleteUserPostPath}"
           acceptCharset="utf-8" id="confirmationForm" enctype="multipart/form-data">
    <c:set var="deleteError" scope="request"><form:errors path="password"/></c:set>
    <div class="modal-content">
        <h4>
            <spring:message code="delete.user.confirmation"/>
        </h4>
        <div class="file-field input-field">
            <div class="btn">
                <form:label path="password"><spring:message code="delete.user"/></form:label>
                <form:input path="password" type="file"/>
            </div>
            <form:errors path="password" cssClass="helper-text error"/>
        </div>
    </div>
    <div class="modal-footer">
        <a href="" class="modal-close waves-effect btn-flat">
            <spring:message code="pages.profile.close"/>
        </a>
        <button class="modal-close waves-effect btn-flat" type="submit" form="photoForm"
                id="photoForm" onclick="this.form.submit();" name="action">
            <spring:message code="pages.profile.confirm"/>
        </button>
    </div>
</form:form>
</body>
</html>