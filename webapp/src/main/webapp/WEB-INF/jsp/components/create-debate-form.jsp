<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>
    <div class="card">
        <c:url value="/create-debate" var="postPath" />
        <%--@elvariable id="createDebateForm" type="ar.edu.itba.paw.webapp.form.CreateDebateForm"--%>
        <form:form method="post" action="${postPath}" modelAttribute="createDebateForm" acceptCharset="utf-8">
            <div class="card-content">
                <span class="card-title center"><spring:message code="components.create-debate"/></span>
                <div class="input-field">
                    <form:label path="title" for="title"><spring:message code="components.create-debate-title"/></form:label>
                    <form:textarea path="title" id="title" maxlength="64" cssClass="materialize-textarea"/>
                    <form:errors path="title" element="span" cssClass="error" />
                </div>

                <div class="input-field">
                    <form:label path="description" for="description"><spring:message code="components.create-debate-description"/></form:label>
                    <form:textarea path="description" id="description" maxlength="280" cssClass="materialize-textarea"/>
                    <form:errors path="description" element="span" cssClass="error" />
                </div>

                <div class="input-field">
                    <form:select path="categoryId">
                        <form:option value="0"><spring:message code="components.debate-category-00"/></form:option>
                        <form:option value="1"><spring:message code="components.debate-category-01"/></form:option>
                        <form:option value="2"><spring:message code="components.debate-category-02"/></form:option>
                        <form:option value="3"><spring:message code="components.debate-category-03"/></form:option>
                        <form:option value="4"><spring:message code="components.debate-category-04"/></form:option>
                        <form:option value="5"><spring:message code="components.debate-category-05"/></form:option>
                        <form:option value="6"><spring:message code="components.debate-category-06"/></form:option>
                        <form:option value="7"><spring:message code="components.debate-category-07"/></form:option>
                        <form:option value="8"><spring:message code="components.debate-category-08"/></form:option>
                        <form:option value="9"><spring:message code="components.debate-category-09"/></form:option>
                        <form:option value="10"><spring:message code="components.debate-category-10"/></form:option>
                        <form:option value="11"><spring:message code="components.debate-category-11"/></form:option>
                    </form:select>
                </div>

                <div class="file-field input-field">
                    <div class="btn">
                        <form:label path="image" for="image"><spring:message code="components.user-image-button"/></form:label>
                        <form:input id="image" path="image" type="file"/>
                    </div>
                    <div class="file-path-wrapper">
                        <input class="file-path validate" type="text"/>
                    </div>
                    <form:errors path="image" cssClass="helper-text error"/>
                </div>
            </div>
        </form:form>
    </div>
</body>
</html>
